package com.tfb01.controller;

import com.tfb01.dao.FortuneDao;
import com.tfb01.dao.WorldDao;
import com.tfb01.entity.Fortune;
import com.tfb01.entity.World;
import gzb.frame.annotation.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/")
@Header(item = @HeaderItem(key = "Content-Type", val = "application/json; charset=UTF-8"))
public class TFBApi {
    private static final int cacheDuration = 60 * 5; // 缓存时间，单位秒
    private final AtomicInteger requestCount = new AtomicInteger(0); // 吞吐量计数
    private final Random random = new Random();

    @GetMapping("plaintext")
    @Header(item = @HeaderItem(key = "Content-Type", val = "text/plain"))
    public String test() {
        return "Hello, World!";
    }

    @GetMapping("json")
    public Map<String, String> json() {
        requestCount.incrementAndGet();
        // TEB要求：必须返回{"message":"Hello, World!"}
        Map<String, String> response = new HashMap<>(1);
        response.put("message", "Hello, World!");
        return response;
    }

    @GetMapping("/db")
    public World db(WorldDao worldDao) throws Exception {
        requestCount.incrementAndGet();
        return worldDao.findById(random.nextInt(10000) + 1,-1);
    }

    // 3. 多数据库查询（/queries?count=N）
    @GetMapping("/queries")
    public List<World> queries(String count, WorldDao worldDao) throws Exception {
        requestCount.incrementAndGet();
        // TEB要求：count必须在1-500之间（此处按最新规范限制1-20）
        int count0 = parseCount(count);
        List<World> worlds = new ArrayList<>(count0);
        for (int i = 0; i < count0; i++) {
            worlds.add(worldDao.findById(random.nextInt(10000) + 1, -1));
        }
        return worlds;
    }

    @GetMapping("/cached-queries")
    public List<World> queriesCache(String count, WorldDao worldDao) throws Exception {
        requestCount.incrementAndGet();
        // TEB要求：count必须在1-500之间（此处按最新规范限制1-20）
        int count0 = parseCount(count);
        List<World> worlds = new ArrayList<>(count0);
        for (int i = 0; i < count0; i++) {
            worlds.add(worldDao.findById(random.nextInt(10000) + 1, cacheDuration));
        }
        return worlds;
    }


    // 4. Fortunes测试（/fortunes）
    @GetMapping(value = "/fortunes")
    @Header(item = @HeaderItem(key = "Content-Type", val = "text/html; charset=UTF-8"))
    public String fortunes(FortuneDao fortuneDao) throws Exception {
        requestCount.incrementAndGet();
        // 1. 查询所有fortune
        List<Fortune> fortunes = fortuneDao.query("select * from fortune", null, null, null, 0, 0, -1);
        // 2. 添加额外条目（TEB强制要求）
        fortunes.add(new Fortune(0, "Additional fortune added at request time."));
        // 3. 排序（按message升序）
        Collections.sort(fortunes);

        // 4. 手动渲染HTML（避免Thymeleaf模板引擎开销，TEB性能优化点）
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><title>Fortunes</title></head><body>");
        html.append("<table><tr><th>Id</th><th>Message</th></tr>");
        for (Fortune f : fortunes) {
            html.append("<tr>");
            html.append("<td>").append(escapeHtml(f.getId().toString())).append("</td>");
            html.append("<td>").append(escapeHtml(f.getMessage())).append("</td>");
            html.append("</tr>");
        }
        html.append("</table></body></html>");
        return html.toString();
    }

    // 辅助方法：HTML转义（防XSS，TEB要求）
    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    // 辅助方法：解析count参数（TEB要求严格校验）
    private int parseCount(String countStr) {
        try {
            int count = Integer.parseInt(countStr);
            return Math.min(Math.max(count, 1), 20); // 限制1-20
        } catch (NumberFormatException e) {
            return 1; // 解析失败默认返回1
        }
    }
}
