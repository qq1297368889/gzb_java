package gzb.start;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GZB 框架 Radix Tree 核心原理演示
 * 目标：演示基于路径前缀的O(K)稳定路由查找
 */
public class SimpleRadixTree<T> {

    public static void main(String[] args) {
       // GzbNodeMap<String> gzbNodeMap = new GzbNodeMap<>();
        Map<String, String> gzbNodeMap = new ConcurrentHashMap<>() ;

        // 插入示例路由
        gzbNodeMap.put("/user/view", "HandlerUserView");
        gzbNodeMap.put("/user/edit", "HandlerUserEdit");
        gzbNodeMap.put("/admin/create", "HandlerAdminCreate");
        gzbNodeMap.put("/admin/delete", "HandlerAdminDelete");
        gzbNodeMap.put("/user", "HandlerUserRoot"); // 路径比其他路径短
        gzbNodeMap.put("/test", "HandlerTest");
        gzbNodeMap.put("/testing", "HandlerTesting");

        // --- 查找测试 ---

        System.out.println("--- 路由查找测试 ---");

        // 查找成功
        System.out.println("/user/view: " + gzbNodeMap.get("/user/view")); // 预期: HandlerUserView
        System.out.println("/user: " + gzbNodeMap.get("/user"));           // 预期: HandlerUserRoot
        System.out.println("/admin/delete: " + gzbNodeMap.get("/admin/delete")); // 预期: HandlerAdminDelete
        System.out.println("/test: " + gzbNodeMap.get("/test"));           // 预期: HandlerTest
        System.out.println("/testing: " + gzbNodeMap.get("/testing"));     // 预期: HandlerTesting

        // 查找失败
        System.out.println("/admin: " + gzbNodeMap.get("/admin"));         // 预期: null (因为它不是一个完整的路由)
        System.out.println("/test/a: " + gzbNodeMap.get("/test/a"));       // 预期: null
        for (int i = 0; i < 4; i++) {
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        long start=System.nanoTime();
                        String res = gzbNodeMap.get("/testing");
                        long end=System.nanoTime();
                        System.out.println((end-start) +"  "+res);
                    }
                }
            }.start();
        }

    }
}

class GzbNodeMap<T> {

    // 内部节点类
    private static class Node<T> {
        String pathSegment; // 存储的路径片段（压缩前缀）
        T handler;          // 路由对应的业务处理器 (Endpoint)
        Map<Character, Node<T>> children; // 子节点，键是下一个字符的首字母

        public Node(String pathSegment) {
            this.pathSegment = pathSegment;
            this.children = new HashMap<>();
        }

        // 检查节点是否代表一个完整的路由
        public boolean isEndpoint() {
            return handler != null;
        }
    }

    private final Node<T> root;

    public GzbNodeMap() {
        // 根节点不存储任何路径片段
        this.root = new Node<>("");
    }

    /**
     * 插入路由 (简化版，仅演示压缩逻辑)
     */
    public void put(String route, T handler) {
        if (route.isEmpty()) return;
        Node<T> current = root;
        String path = route;

        while (!path.isEmpty()) {
            char firstChar = path.charAt(0);
            Node<T> next = current.children.get(firstChar);

            if (next == null) {
                // 情况 1: 没有匹配的边，直接添加新节点
                Node<T> newNode = new Node<>(path);
                newNode.handler = handler;
                current.children.put(firstChar, newNode);
                return;
            }

            // 情况 2: 找到匹配的边，计算公共前缀长度
            String nextPath = next.pathSegment;
            int prefixLen = 0;
            while (prefixLen < path.length() && prefixLen < nextPath.length() &&
                    path.charAt(prefixLen) == nextPath.charAt(prefixLen)) {
                prefixLen++;
            }

            if (prefixLen == nextPath.length()) {
                // 情况 2a: 现有边完全是新路径的前缀 (路径压缩)
                path = path.substring(prefixLen);
                current = next;
                if (path.isEmpty()) {
                    // 如果路径结束，标记当前节点为端点
                    next.handler = handler;
                    return;
                }
            } else {
                // 情况 2b: 公共前缀导致节点分裂 (核心压缩逻辑)

                // 1. 创建新节点作为公共前缀节点 (New Parent)
                String commonPrefix = nextPath.substring(0, prefixLen);
                Node<T> newParent = new Node<>(commonPrefix);

                // 2. 将原节点作为新父节点的子节点
                next.pathSegment = nextPath.substring(prefixLen); // 原节点路径缩短
                newParent.children.put(next.pathSegment.charAt(0), next);

                // 3. 将新父节点插入到当前节点的子集中
                current.children.put(firstChar, newParent);

                // 4. 处理剩余路径 (新路由的分支)
                String remainingNewPath = path.substring(prefixLen);
                if (!remainingNewPath.isEmpty()) {
                    Node<T> newChild = new Node<>(remainingNewPath);
                    newChild.handler = handler;
                    newParent.children.put(remainingNewPath.charAt(0), newChild);
                } else {
                    // 如果新路由在公共前缀处结束，标记新父节点为端点
                    newParent.handler = handler;
                }
                return;
            }
        }
    }

    /**
     * 查找路由
     */
    public T get(String route) {
        if (route.isEmpty()) return null;
        Node<T> current = root;
        String path = route;

        while (!path.isEmpty()) {
            char firstChar = path.charAt(0);
            Node<T> next = current.children.get(firstChar);

            if (next == null) {
                return null; // 找不到匹配
            }

            String nextPath = next.pathSegment;

            // 检查当前路径是否匹配当前边的路径片段
            if (!path.startsWith(nextPath)) {
                return null; // 路径不匹配
            }

            // 路径匹配，消耗路径片段，继续向下查找
            path = path.substring(nextPath.length());
            current = next;
        }

        // 路径完全匹配后，检查当前节点是否是端点
        return current.handler;
    }

}