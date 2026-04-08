package gzb.frame.template;

import gzb.tools.FileTools;
import gzb.tools.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GzbTemplate {
    public static class Entity  {
        public String importCode = "";
        public String classVariable = "";
        public String methodParar = ""; // 组装成: String acc, String pwd, com.frame.entity.SysUsers sysUsers
        public String classAnno = "";
        public String classExtend = "";
        public String methodAnno = "";
        public String templateCode = "";
        public String appendData = "";//追加数据 可以用来实现 继承方法之类的
        public File file;
    }

    public static Entity parse(File file) {
        String raw = FileTools.readString(file);
        return parse(raw,file);
    }

    public static Entity parse(String templateCode,File file) {
        Entity entity = new Entity();
        entity.file=file;
        // 1. 提取 Import <%@ page import="..." %>
        String importRegex = "<%@\\s+page\\s+import\\s*=\\s*[\"']([^\"']*)[\"']\\s*%>";
        Pattern importPattern = Pattern.compile(importRegex);
        Matcher importMatcher = importPattern.matcher(templateCode);
        StringBuilder imports = new StringBuilder();
        while (importMatcher.find()) {
            imports.append("import ").append(importMatcher.group(1)).append(";\n");
        }
        entity.importCode = imports.toString();
        templateCode = importMatcher.replaceAll("");

        // 2. 提取类变量声明 <%! ... %> (支持多行)
        String varRegex = "(?s)<%!(.*?)%>";
        Pattern varPattern = Pattern.compile(varRegex);
        Matcher varMatcher = varPattern.matcher(templateCode);
        StringBuilder variables = new StringBuilder();
        while (varMatcher.find()) {
            variables.append(varMatcher.group(1).trim()).append("\n");
        }
        entity.classVariable = variables.toString();
        templateCode = varMatcher.replaceAll("");

        // 3. 提取方法参数 <jsp:useBean id="..." class="..." />
        // 正则解释：不区分 id 和 class 的先后顺序
        String beanRegex = "<jsp:useBean\\s+id=\"([^\"]*)\"\\s+class=\"([^\"]*)\"\\s*/>" +
                "|<jsp:useBean\\s+class=\"([^\"]*)\"\\s+id=\"([^\"]*)\"\\s*/>";
        Pattern beanPattern = Pattern.compile(beanRegex);
        Matcher beanMatcher = beanPattern.matcher(templateCode);
        List<String> params = new ArrayList<>();
        while (beanMatcher.find()) {
            String id, clazz;
            if (beanMatcher.group(1) != null) { // 第一种顺序: id, class
                id = beanMatcher.group(1);
                clazz = beanMatcher.group(2);
            } else { // 第二种顺序: class, id
                id = beanMatcher.group(4);
                clazz = beanMatcher.group(3);
            }
            // 简单处理常用类型的缩写，比如 java.lang.String 转为 String
            String shortClazz = clazz.startsWith("java.lang.") ? clazz.substring(10) : clazz;
            params.add(shortClazz + " " + id);
        }
        entity.methodParar = String.join(", ", params);
        templateCode = beanPattern.matcher(templateCode).replaceAll("");

        // 4. 清理剩余的注释和空白
        templateCode = templateCode.replaceAll("(?s)<%--.*?--%>", "");
        entity.templateCode = templateCode.trim();

        return entity;
    }

    public static String generate(Entity entity, String pkgName) {
        if (pkgName == null) {
            pkgName = "gzb.frame.jsp";
        }
        StringBuilder code = new StringBuilder();

        // 1. 写入包名和导包
        code.append("package ");
        code.append(pkgName);
        code.append(";\n\n");

        code.append("import gzb.tools.thread.GzbThreadLocal;\n");
        code.append("import gzb.frame.annotation.*;\n");
        code.append("import gzb.frame.factory.GzbOneInterface;\n");
        code.append("import gzb.frame.netty.entity.Request;\n");
        code.append("import gzb.frame.netty.entity.Response;\n");
        code.append("import gzb.tools.json.GzbJson;\n");
        code.append("import gzb.tools.log.Log;\n");
        code.append("import java.util.Map;\n");
        code.append("import java.util.List;\n");
        code.append(entity.importCode).append("\n");
        if (entity.classAnno!=null) {
            code.append(entity.classAnno).append("\n");
        }
        code.append("public class c_").append(Tools.textToMd5(entity.file.getPath().toLowerCase()), 0, 16);
        if (entity.classExtend!=null) {
            code.append(entity.classExtend);
        }
        code.append(" {\n\n");

        code.append("    // --- Class Variables ---\n");
        code.append("    ").append(entity.classVariable.replace("\n", "\n    ")).append("\n");
        if (entity.methodAnno!=null) {
            code.append(entity.methodAnno).append("\n");
        }
        code.append("    public String _gzb_tem_001(").append(entity.methodParar).append(") throws Exception {\n");
        code.append("        GzbThreadLocal.Entity entity1 =GzbThreadLocal.context.get();\n" +
                "        int index0=entity1.stringBuilderCacheEntity.open();\n" +
                "        StringBuilder sb =entity1.stringBuilderCacheEntity.get(index0);\n" +
                "        try {\n");
        Pattern tagPattern = Pattern.compile("<%(=?)(.*?)%>", Pattern.DOTALL);
        Matcher m = tagPattern.matcher(entity.templateCode);
        int lastEnd = 0;

        while (m.find()) {
            // A. 处理标签前的静态 HTML
            String text = entity.templateCode.substring(lastEnd, m.start());
            if (!text.isEmpty()) {
                code.append("        sb.append(\"").append(escapeJava(text)).append("\");\n");
            }

            String type = m.group(1);   // "=" 或者空
            String content = m.group(2).trim(); // Java 代码块

            if ("=".equals(type)) {
                // B. 处理 <%= expression %>
                code.append("        sb.append(").append(content).append(");\n");
            } else {
                // C. 处理 <% java code %>
                code.append("        ").append(content.replace("\n", "\n        ")).append("\n");
            }
            lastEnd = m.end();
        }

        // D. 处理剩余的静态文本
        if (lastEnd < entity.templateCode.length()) {
            String tail = entity.templateCode.substring(lastEnd);
            code.append("        sb.append(\"").append(escapeJava(tail)).append("\");\n");
        }

        code.append("        return sb.toString();\n");

        code.append("        }finally {\n" +
                "            entity1.stringBuilderCacheEntity.close(index0);\n" +
                "        }\n");
        code.append("    }\n\n");

        if (entity.appendData!=null) {
            code.append(entity.appendData);
        }
        code.append("}\n");

        return code.toString();
    }

    /**
     * 处理 Java 字符串转义，防止 HTML 里的引号和换行搞死生成的 Java 源码
     */
    private static String escapeJava(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }
    public static String generate(File file) {
        return generate(parse(file), null);
    }

    public static void main(String[] args) {
        System.out.println("generate:\n" + generate(new File("E:\\codes_20220814\\java\\250913_code\\demo\\src\\main\\resources\\templates\\demo.jsp")));
    }

}