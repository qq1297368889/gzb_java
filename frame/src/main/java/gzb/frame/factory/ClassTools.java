package gzb.frame.factory;

import gzb.entity.SqlTemplate;
import gzb.frame.annotation.*;
import gzb.frame.db.BaseDao;
import gzb.frame.netty.entity.FileUploadEntity;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;

import java.lang.reflect.*;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClassTools {
    public static Log log = Config.log;
    static Map<Class<?>, Object> mapLoadObjectObject = new ConcurrentHashMap<>();
    public static Map<String, Field[]> mapField = new ConcurrentHashMap<>();

    private static final Set<Class<?>> WRAPPER_TYPES = new HashSet<>();

    static {
        WRAPPER_TYPES.add(Byte.class);
        WRAPPER_TYPES.add(Short.class);
        WRAPPER_TYPES.add(Integer.class);
        WRAPPER_TYPES.add(Long.class);
        WRAPPER_TYPES.add(Float.class);
        WRAPPER_TYPES.add(Double.class);
        WRAPPER_TYPES.add(Boolean.class);
        WRAPPER_TYPES.add(Character.class);
    }

    static Lock lock = new ReentrantLock();


    public static String toName(Class<?> type) {
        if (type == null) {
            return null;
        }
        if (type == int.class) {
            return "java.lang.Integer";
        }
        if (type == byte.class) {
            return "java.lang.Byte";
        }
        if (type == short.class) {
            return "java.lang.Short";
        }
        if (type == long.class) {
            return "java.lang.Long";
        }
        if (type == float.class) {
            return "java.lang.Float";
        }
        if (type == double.class) {
            return "java.lang.Double";
        }
        if (type == boolean.class) {
            return "java.lang.Boolean";
        }
        if (type == char.class) {
            return "java.lang.Character";
        }

        return type.getName();
    }


    public static boolean isWrapperOrPrimitive(Class<?> type) {
        if (type == null) {
            return false;
        }
        if (type == String.class) {
            return true;
        }

        return type.isPrimitive() || WRAPPER_TYPES.contains(type);
    }

    /*
        public static List<String> getParameterNames(Method method) {
            List<String> paramNames = new ArrayList<>();

            Class<?> clazz=method.getDeclaringClass();
            // 获取类加载器，处理可能为null的情况
            ClassLoader classLoader = clazz.getClassLoader();
            if (classLoader == null) {
                classLoader = Thread.currentThread().getContextClassLoader();
            }

            // 构建类文件资源路径
            String classNameAsResource = clazz.getName().replace('.', '/') + ".class";

            // 读取类字节码
            try (InputStream is = classLoader.getResourceAsStream(classNameAsResource)) {
                if (is == null) {
                    System.err.println("无法加载类资源: " + classNameAsResource);
                    return paramNames;
                }

                ClassReader cr = new ClassReader(is);
                MethodNode methodNode = findMethodNode(cr, method);

                if (methodNode != null && methodNode.localVariables != null) {
                    // 确定第一个参数的索引：静态方法从0开始，实例方法从1开始
                    int firstParamIndex = Modifier.isStatic(method.getModifiers()) ? 0 : 1;

                    // 获取方法描述符以计算实际参数数量
                    String methodDescriptor = Type.getMethodDescriptor(method);
                    Type[] argumentTypes = Type.getArgumentTypes(methodDescriptor);

                    // 按顺序收集参数名
                    for (int i = 0; i < argumentTypes.length; i++) {
                        // 局部变量表中的索引
                        int localVarIndex = firstParamIndex + i;

                        // 查找匹配索引的局部变量节点
                        LocalVariableNode lvn = findLocalVariableNode(methodNode, localVarIndex);
                        if (lvn != null) {
                            paramNames.add(lvn.name);
                        } else {
                            // 无法从局部变量表获取参数名
                            paramNames.add("arg" + i);
                        }
                    }
                }

            } catch (IOException e) {
                System.err.println("读取类字节码失败: " + e.getMessage());
            }

            return paramNames;
        }
        */
    //code 源代码  methodName 方法名 pararType 参数类型  返回参数名称List
    public static List<String> getParameterNames(String code, String methodName, Class<?>[] pararType) throws Exception {
        List<String> names = new ArrayList<>();
        if (pararType == null || pararType.length == 0) {
            //log.d("pararType == null || pararType.length == 0");
            return names;
        }
        List<String> list1 = Tools.textMid(code, " " + methodName, "{");
        for (int i = 0; i < list1.size(); i++) {
            String str1 = list1.get(i);
            //log.d("str1", str1);
            if (str1.contains("//")) {
                int a = str1.indexOf("//");
                int b = str1.indexOf("\r", a);
                int c = str1.indexOf("\n", a);
                int n = -1;
                if (b > -1 && c > -1) {
                    n = Math.min(b, c);
                } else {
                    if (b > -1) {
                        n = b;
                    } else {
                        n = c;
                    }
                }
                String t01 = str1.substring(a, n);
                str1 = str1.replace(t01, " ");
            }
            str1 = str1.trim();
            if (!str1.startsWith("(")) {
                continue;
            }
            names.clear();
            str1 = Tools.textMid(str1, "(", ")", 1);
            if (str1 == null) {
                //log.d("str1 == null");
                continue;
            }
            //log.d("str1 处理开始", str1);
            str1 = str1.replace("]", "] ");
            str1 = str1.replace(">", "> ");
            while (str1.contains("<")) {
                int a = str1.indexOf("<");
                int b = str1.indexOf(">", a);
                str1 = str1.replace(str1.substring(a, b), " ");
            }
            while (str1.contains(">")) {
                str1 = str1.replace(">", "");
            }
            while (str1.contains("\r")) {
                str1 = str1.replace("\r", " ");
            }
            while (str1.contains("\n")) {
                str1 = str1.replace("\n", " ");
            }
            while (str1.contains("\t")) {
                str1 = str1.replace("\t", " ");
            }
            while (str1.contains(", ")) {
                str1 = str1.replace(", ", ",");
            }
            while (str1.contains(" [")) {
                str1 = str1.replace(" [", "[");
            }
            while (str1.contains("  ")) {
                str1 = str1.replace("  ", " ");
            }
            str1 = str1.trim();
            //log.d("str1 处理完毕", str1);
            String[] p01 = str1.split(",");
            //log.d("p01", p01);
            int num = 0;
            for (int i1 = 0; i1 < p01.length; i1++) {
                String str2 = p01[i1];
                String type = null;
                if (str2.contains("<")) {
                    type = str2.substring(0, str2.indexOf("<"));
                } else if (str2.contains(" ")) {
                    type = str2.substring(0, str2.indexOf(" "));
                } else {
                    num++;
                    continue;
                }
                String name = null;
                if (str2.contains(">")) {
                    name = str2.substring(str2.lastIndexOf(">") + 1);
                } else if (str2.contains(" ")) {
                    name = str2.substring(str2.indexOf(" ") + 1);
                } else {
                    num++;
                    continue;
                }
                name = name.trim();
                type = type.trim();
                //log.d(methodName, type, name, str2);
                if (i1 - num > pararType.length - 1) {
                    names.clear();
                    break;
                }
                String typename1 = pararType[i1 - num].getCanonicalName();
                if (i1 - num < pararType.length && typename1.contains(type)) {
                    names.add(name);
                }
            }
            if (names.size() == pararType.length) {
                break;
            }

        }
        if (names.size() != pararType.length) {
            names.clear();
        }
        log.t("获取参数名", methodName, names);
        return names;

    }

    public static String updateCode(String code, String name, String interfaceName, String appendCode) throws Exception {
        String tmp0 = Tools.textMid(code, name, "{", 1);
        if (tmp0 == null) {
            return null;
        }
        String tmp = tmp0;
        if (tmp.contains("//")) {
            int a = tmp.indexOf("//");
            int b = tmp.indexOf("\r", a);
            int c = tmp.indexOf("\n", a);
            int n = -1;
            if (b > -1 && c > -1) {
                n = Math.min(b, c);
            } else {
                if (b > -1) {
                    n = b;
                } else {
                    n = c;
                }
            }
            String t01 = tmp.substring(a, n);
            tmp = tmp.replace(t01, " ");
        }
        int a = tmp.indexOf("implements");
        if (a > -1) {
            tmp += "," + interfaceName;
        } else {
            tmp += " implements " + interfaceName;
        }
        code = code.replace(name + tmp0 + "{", name + tmp + "{");

        code = code.substring(0, code.lastIndexOf("}"));
        code += appendCode + "\r\n}";
        return code;
    }

    static ClassLoader appClassLoader = ClassTools.class.getClassLoader();

    public static List<Object> getMethodParameterList(
            Map<String, List<Object>> requestDataMap,
            Map<String, Object> mapObject,
            Object[] arrayObject,
            Class<?>[] TypeClass,
            String[] TypeName,
            String[] implNames) {
        List<Object> listObject = new ArrayList<>();
        if (TypeClass != null) {
            for (int n = 0; n < TypeClass.length; n++) {
                Class<?> paramType = TypeClass[n];
                String paramName = TypeName[n];
                String implName = null;
                Object paramValue = null;
                if (implNames != null) {
                    implName = implNames[n];
                } else {
                    implName = paramType.getName();
                }
                try {
                    if (mapObject != null) {
                        paramValue = mapObject.get(implName);
                    }
                    if (arrayObject != null) {
                        for (Object object : arrayObject) {
                            if (object == null) {
                                continue;
                            }
                            if (paramType.isAssignableFrom(object.getClass())) {
                                paramValue = object;
                            }
                        }
                    }
                    if (paramValue == null && requestDataMap != null) {
                        List<Object> values = requestDataMap.get(paramName);
                        if (values != null && !values.isEmpty()) {
                            if (paramType.isArray()) {
                                Class<?> componentType = paramType.getComponentType();
                                Object array = Array.newInstance(componentType, values.size());
                                for (int i = 0; i < values.size(); i++) {
                                    if (values.get(i) == null) {
                                        Array.set(array, i, values.get(i));
                                    } else if (componentType == FileUploadEntity.class) {
                                        Array.set(array, i, values.get(i));
                                    } else if (componentType == String.class) {
                                        Array.set(array, i, values.get(i).toString());
                                    } else if (componentType == int.class || componentType == Integer.class) {
                                        Array.set(array, i, Integer.parseInt(values.get(i).toString()));
                                    } else if (componentType == float.class || componentType == Float.class) {
                                        Array.set(array, i, Float.parseFloat(values.get(i).toString()));
                                    } else if (componentType == short.class || componentType == Short.class) {
                                        Array.set(array, i, Short.parseShort(values.get(i).toString()));
                                    } else if (componentType == long.class || componentType == Long.class) {
                                        Array.set(array, i, Long.parseLong(values.get(i).toString()));
                                    } else if (componentType == double.class || componentType == Double.class) {
                                        Array.set(array, i, Double.parseDouble(values.get(i).toString()));
                                    } else if (componentType == boolean.class || componentType == Boolean.class) {
                                        Array.set(array, i, Boolean.parseBoolean(values.get(i).toString()));
                                    } else {
                                        Array.set(array, i, values.get(i));
                                    }
                                }
                                paramValue = array;
                            } else {
                                if (values.get(0) == null) {
                                    paramValue = values.get(0);
                                } else if (paramType == FileUploadEntity.class) {
                                    paramValue = values.get(0);
                                } else if (paramType == String.class) {
                                    paramValue = values.get(0);
                                } else if (paramType == int.class || paramType == Integer.class) {
                                    paramValue = Integer.parseInt(values.get(0).toString());
                                } else if (paramType == float.class || paramType == Float.class) {
                                    paramValue = Float.parseFloat(values.get(0).toString());
                                } else if (paramType == short.class || paramType == Short.class) {
                                    paramValue = Short.parseShort(values.get(0).toString());
                                } else if (paramType == long.class || paramType == Long.class) {
                                    paramValue = Long.parseLong(values.get(0).toString());
                                } else if (paramType == double.class || paramType == Double.class) {
                                    paramValue = Double.parseDouble(values.get(0).toString());
                                } else if (paramType == boolean.class || paramType == Boolean.class) {
                                    paramValue = Boolean.parseBoolean(values.get(0).toString());
                                } else {
                                    paramValue = values.get(0);
                                }
                            }
                        }
                    }

                    if (paramValue == null && requestDataMap != null) {
                        if (paramType != Boolean.class &&
                                paramType != Byte.class &&
                                paramType != Short.class &&
                                paramType != Integer.class &&
                                paramType != Long.class &&
                                paramType != Float.class &&
                                paramType != Double.class &&
                                paramType != Character.class &&
                                paramType != String.class
                                && !paramType.isPrimitive()) {
                            Object[] objects = loadObject(paramType, requestDataMap, log);
                            if (objects != null && objects.length > 0) {
                                if (paramType.isArray()) {
                                    paramValue = objects;
                                } else {
                                    paramValue = objects[0];
                                }
                            }
                        }

                    }
                    listObject.add(paramValue);
                } catch (NumberFormatException e) {
                    throw new RuntimeException(
                            "请求参数 数字类型 转换错误 -> 名称:" + paramName + ",类型:" + paramType + ",参数:" + requestDataMap
                            , e);
                }
            }
        }
        return listObject;
    }

    public static GzbEntityInterface readObject(Class<?> aClass) {
        Object obj = mapLoadObjectObject.get(aClass);
        if (obj == null) {
            lock.lock();
            try {
                if (obj == null) {
                    String code;
                    if (aClass.isArray()) {
                        code = ClassTools.gen_code_entity_load_map(aClass.getComponentType());
                    } else {
                        code = ClassTools.gen_code_entity_load_map(aClass);
                    }
                    //System.out.println(code);
                    if (code != null) {
                        Class<?> aClass0 = ClassLoad.compileJavaCode(code);
                        if (aClass0 != null) {
                            obj = aClass0.getDeclaredConstructor().newInstance();
                            mapLoadObjectObject.put(aClass, obj);
                        }
                    } else {
                        mapLoadObjectObject.put(aClass, "1");
                    }
                }
            } catch (Exception e) {
                log.e(e);
            } finally {
                lock.unlock();
            }
        }
        if (obj == null) {
            return null;
        }
        if (obj.toString().equals("1")) {
            return null;
        }
        return (GzbEntityInterface) obj;
    }

    public static SqlTemplate toSelectSql(Object object) {
        GzbEntityInterface gzbEntityInterface = readObject(object.getClass());
        if (gzbEntityInterface == null) {
            return null;
        }
        try {
            return gzbEntityInterface.toSelectSql(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static SqlTemplate toDeleteSql(Object object) {
        GzbEntityInterface gzbEntityInterface = readObject(object.getClass());
        if (gzbEntityInterface == null) {
            return null;
        }
        try {
            return gzbEntityInterface.toDeleteSql(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static SqlTemplate toSaveSql(Object object) {
        GzbEntityInterface gzbEntityInterface = readObject(object.getClass());
        if (gzbEntityInterface == null) {
            return null;
        }
        try {
            return gzbEntityInterface.toSaveSql(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static SqlTemplate toUpdateSql(Object object) {
        GzbEntityInterface gzbEntityInterface = readObject(object.getClass());
        if (gzbEntityInterface == null) {
            return null;
        }
        try {
            return gzbEntityInterface.toUpdateSql(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object[] loadObject(Class<?> aClass, Map<String, List<Object>> map, Log log) {
        GzbEntityInterface gzbEntityInterface = readObject(aClass);
        if (gzbEntityInterface == null) {
            return null;
        }
        try {
            return gzbEntityInterface.loadObject(map, log);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJsonObject(Class<?> aClass, Object obj, Log log) {
        GzbEntityInterface gzbEntityInterface = readObject(aClass);
        if (gzbEntityInterface == null) {
            return null;
        }
        try {
            return gzbEntityInterface.toJson(obj, log);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, String> humpMap = new HashMap<>();

    private static String getName(String name) {
        if (name == null) {
            return null;
        }
        String nameHump = humpMap.get(name);
        if (nameHump != null) {
            return nameHump;
        }
        String[] arr1 = name.split("_");
        String n = arr1[0];
        for (int i = 1; i < arr1.length; i++) {
            char[] chars = arr1[i].toCharArray();
            for (int i1 = 0; i1 < chars.length; i1++) {
                if (i1 == 0) {
                    n += String.valueOf(chars[i1]).toUpperCase();
                } else {
                    n += String.valueOf(chars[i1]).toLowerCase();
                }
            }
        }
        humpMap.put(name, n);
        return n;
    }

    public static String gen_code_entity_load_map(Class<?> aClass) throws NoSuchMethodException {
        int num = 0;
        Field[] fields = aClass.getDeclaredFields();
        String className = aClass.getName();
        String code = "public class " + (className.replaceAll("\\.", "_")) + "_sub implements gzb.frame.factory.GzbEntityInterface{\n" +
                "   public String toJson(Object object,gzb.tools.log.Log _gzb_log) throws Exception{\n" +
                "try{\n" +
                "        StringBuilder sb=new StringBuilder(\"{\");\n" +
                "        if (object instanceof " + className + ") {\n" +
                "           " + className + " obj0=(" + className + ")object;\n";

        for (Field field : fields) {
            String name = (field.getName());
            String name_d = Tools.lowStr_d(field.getName());
            if (!ClassTools.isWrapperOrPrimitive(field.getType())) {
                continue;
            }
            if (Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            int type = 0;
            if (field.getType() != Boolean.class &&
                    field.getType() != Byte.class &&
                    field.getType() != Short.class &&
                    field.getType() != Integer.class &&
                    field.getType() != Long.class &&
                    field.getType() != Float.class &&
                    field.getType() != Double.class &&
                    field.getType() != Character.class &&
                    field.getType() != String.class
                    && !field.getType().isPrimitive()) {
                type = 0;
            } else {
                if (field.getType() == Long.class || field.getType() == Integer.class || field.getType() == Short.class || field.getType() == Byte.class
                        || field.getType() == long.class || field.getType() == int.class || field.getType() == short.class
                        || field.getType() == byte.class) {
                    type = 2;//不再区分 数字类型
                } else {
                    type = 2;
                }
            }

            if (Modifier.isPublic(field.getModifiers())) {

                if (type == 0) {
                    code += (field.getType().isPrimitive() ? "" : "           if (obj0." + name + "!=null) {\n") +
                            "               sb.append(\"\\\"" + name + "\\\":\").append(gzb.tools.Tools.toJson(obj0." + name + ")).append(\",\");\n" +
                            (field.getType().isPrimitive() ? "" : "           }\n");
                } else if (type == 1) {
                    code += (field.getType().isPrimitive() ? "" : "           if (obj0." + name + "!=null) {\n") +
                            "               sb.append(\"\\\"" + name + "\\\":\").append(obj0." + name + ").append(\",\");\n" +
                            (field.getType().isPrimitive() ? "" : "           }\n");
                } else if (type == 2) {
                    code += (field.getType().isPrimitive() ? "" : "           if (obj0." + name + "!=null) {\n") +
                            "               sb.append(\"\\\"" + name + "\\\":\\\"\").append(obj0." + name + ").append(\"\\\"\").append(\",\");\n" +
                            (field.getType().isPrimitive() ? "" : "           }\n");
                }
                num++;
            } else {
                try {
                    aClass.getMethod("get" + name_d);
                    if (type == 0) {
                        code += (field.getType().isPrimitive() ? "" : "           if (obj0.get" + name_d + "()!=null) {\n") +
                                "               sb.append(\"\\\"" + name + "\\\":\").append(gzb.tools.Tools.toJson(obj0.get" +
                                name_d + "())).append(\",\");\n" +
                                (field.getType().isPrimitive() ? "" : "           }\n");
                    } else if (type == 1) {
                        code += (field.getType().isPrimitive() ? "" : "           if (obj0.get" + name_d + "()!=null) {\n") +
                                "               sb.append(\"\\\"" + name + "\\\":\").append(obj0.get" + name_d + "()).append(\",\");\n" +
                                (field.getType().isPrimitive() ? "" : "           }\n");
                    } else if (type == 2) {
                        code += (field.getType().isPrimitive() ? "" : "           if (obj0.get" + name_d + "()!=null) {\n") +
                                "               sb.append(\"\\\"" + name + "\\\":\\\"\").append(obj0.get" + name_d + "()).append(\"\\\"\").append(\",\");\n" +
                                (field.getType().isPrimitive() ? "" : "           }\n");
                    }

                    num++;
                } catch (Exception e) {

                }
            }
        }


        code += "        }\n" +
                "        if (sb.length()>1) {\n" +
                "            sb.delete(sb.length()-1, sb.length());\n" +
                "        }\n" +
                "        sb.append(\"}\");\n" +
                "        return sb.toString();\n" +
                "     }catch (Exception e){\n" +
                "        throw new RuntimeException(\"实体类->" + className + "转换JSON异常\",e);\n" +
                "   }\n" +
                "   }\n";

        code += "   public Object[] loadObject" +
                "(java.util.Map<String,java.util.List<Object>> map,gzb.tools.log.Log _gzb_log) throws Exception {\n";
        try {
            // 尝试获取公共的无参构造函数
            Constructor<?> constructor = aClass.getConstructor();
            code += "        java.util.List<" + className + "> returnObj = null;\n" +
                    "        java.util.List<Object> list = null;\n" +
                    "        try {\n";
            for (Field field : fields) {
                if (!ClassTools.isWrapperOrPrimitive(field.getType())) {
                    continue;
                }
                if (Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                String tName = ClassTools.toName(field.getType());
                if (Modifier.isPublic(field.getModifiers())) {
                    code += "            list = map.get(\"" + field.getName() + "\");\n" +
                            "            if (list!=null) {\n" +
                            "                if (returnObj==null) {\n" +
                            "                    returnObj=new java.util.ArrayList<>(list.size());\n" +
                            "                }\n" +
                            "                for (int i = 0; i < list.size(); i++) {\n" +
                            "                    if(list.get(i)==null || list.get(i).toString().length()==0 || " +
                            "list.get(0).equals(\"undefined\")){\n" +
                            "                        continue;" +
                            "                    }\n" +
                            "                    if (i>returnObj.size()-1) {\n" +
                            "                        returnObj.add(new " + className + "());\n" +
                            "                    }\n";
                    code += "                    returnObj.get(i)." + field.getName() + "=" + tName + ".valueOf(list.get(i).toString());\n" +
                            "                }\n" +
                            "            }\n";

                    num++;
                } else {
                    String name_d = Tools.lowStr_d(field.getName());
                    try {
                        aClass.getMethod("set" + name_d, field.getType());
                        code += "            list = map.get(\"" + field.getName() + "\");\n" +
                                "            if (list!=null) {\n" +
                                "                if (returnObj==null) {\n" +
                                "                    returnObj=new java.util.ArrayList<>(list.size());\n" +
                                "                }\n" +
                                "                for (int i = 0; i < list.size(); i++) {\n" +
                                "                    if(list.get(i)==null || list.get(i).toString().length()==0 || list.get(0).equals(\"undefined\")){\n" +
                                "                        continue;" +
                                "                    }\n" +
                                "                    if (i>returnObj.size()-1) {\n" +
                                "                        returnObj.add(new " + className + "());\n" +
                                "                    }\n";
                        code += "                    returnObj.get(i).set" + name_d + "(" + tName + ".valueOf(list.get(i).toString()));\n" +
                                "                }\n" +
                                "            }\n";
                        num++;
                    } catch (Exception e) {
                        //log.e(e);
                    }
                }
            }

            code += "        }catch (Exception e){\n" +
                    "            throw new RuntimeException(\"类型转换出现错误 输入的参数不正确->" + className + "\",e);\n" +
                    "        } \n";
            code += "                    if (returnObj==null) {\n" +
                    "                        return null;\n" +
                    "                    }\n";
            code += "        return returnObj.toArray(new " + className + "[]{});\n";

        } catch (NoSuchMethodException ignored) {
            code += "        return null;\n";
        }
        code += "    }\n";
        EntityAttribute classAttribute = aClass.getAnnotation(EntityAttribute.class);

        //生成 sql 删除
        code += "   public gzb.entity.SqlTemplate toDeleteSql(Object obj0) throws Exception {\n";
        if (classAttribute != null) {
            code += "       " + aClass.getName() + " obj=(" + aClass.getName() + ")obj0;\n" +
                    "        String sql = \"delete from " + classAttribute.name() + "\";\n" +
                    "        StringBuilder stringBuilder = new StringBuilder();\n" +
                    "        java.util.List<Object> params = new java.util.ArrayList<>();\n";
            for (Field field : fields) {
                EntityAttribute fieldAttribute = field.getAnnotation(EntityAttribute.class);
                if (fieldAttribute == null) {
                    continue;
                }
                boolean key = fieldAttribute.key();
                if (!key) {
                    continue;
                }
                String c_name = fieldAttribute.name();
                String c_h_name = field.getName();
                String c_h_d_name = Tools.lowStr_d(c_h_name);
                try {
                    aClass.getMethod("get" + c_h_d_name);
                } catch (Exception e) {
                    continue;
                }
                code += "        if (obj.get" + c_h_d_name + "() != null) {\n" +
                        "            stringBuilder.append(\"" + c_name + "=? and \");\n" +
                        "            params.add(obj.get" + c_h_d_name + "());\n" +
                        "        }\n";
            }
            for (Field field : fields) {
                EntityAttribute fieldAttribute = field.getAnnotation(EntityAttribute.class);
                if (fieldAttribute == null) {
                    continue;
                }
                boolean key = fieldAttribute.key();
                if (key) {
                    continue;
                }
                String c_name = fieldAttribute.name();
                String c_h_name = field.getName();
                String c_h_d_name = Tools.lowStr_d(c_h_name);
                try {
                    aClass.getMethod("get" + c_h_d_name);
                } catch (Exception e) {
                    continue;
                }
                code += "        if (obj.get" + c_h_d_name + "() != null) {\n" +
                        "            stringBuilder.append(\"" + c_name + "=? and \");\n" +
                        "            params.add(obj.get" + c_h_d_name + "());\n" +
                        "        }\n";
            }
            code += "        if (stringBuilder.length() > 0) {\n" +
                    "            stringBuilder.delete(stringBuilder.length() - 5, stringBuilder.length());\n" +
                    "            sql += \" where \";\n" +
                    "        }\n" +
                    "        return new gzb.entity.SqlTemplate(sql + stringBuilder,params.toArray());\n";
        } else {
            code += "        return null;\n";
        }
        code += "    }\n";


        //生成 sql 修改
        code += "   public gzb.entity.SqlTemplate toUpdateSql(Object obj0) throws Exception {\n";
        if (classAttribute != null) {
            code += "       " + aClass.getName() + " obj=(" + aClass.getName() + ")obj0;\n";
            String ids1 = "";
            String ids2 = "";
            for (Field field : fields) {
                EntityAttribute fieldAttribute = field.getAnnotation(EntityAttribute.class);
                if (fieldAttribute == null) {
                    continue;
                }
                boolean key = fieldAttribute.key();
                String c_name = fieldAttribute.name();
                String c_h_name = field.getName();
                String c_h_d_name = Tools.lowStr_d(c_h_name);
                try {
                    aClass.getMethod("get" + c_h_d_name);
                } catch (Exception e) {
                    continue;
                }
                if (key) {
                    code += "        if (obj.get" + c_h_d_name + "()==null) {\n" +
                            "            throw new RuntimeException(\"生成SQL（update）时 发现ID为空 \"+obj);\n" +
                            "        }\n";
                    ids1 += c_name + "=? and ";
                    ids2 += "        params.add(obj.get" + c_h_d_name + "());\n";
                }
            }
            if (ids1.length() > 5) {
                ids1 = ids1.substring(0, ids1.length() - 5);
            }
            code += "        String sql = \"update " + classAttribute.name() + " set \";\n" +
                    "        StringBuilder stringBuilder = new StringBuilder();\n" +
                    "        java.util.List<Object> params = new java.util.ArrayList<>();\n";
            for (Field field : fields) {
                EntityAttribute fieldAttribute = field.getAnnotation(EntityAttribute.class);
                if (fieldAttribute == null) {
                    continue;
                }
                boolean key = fieldAttribute.key();
                if (key) {
                    continue;
                }
                String c_name = fieldAttribute.name();
                String c_h_name = field.getName();
                String c_h_d_name = Tools.lowStr_d(c_h_name);
                try {
                    aClass.getMethod("get" + c_h_d_name);
                } catch (Exception e) {
                    continue;
                }
                code += "        if (obj.get" + c_h_d_name + "() != null) {\n" +
                        "            stringBuilder.append(\"" + c_name + "=?,\");\n" +
                        "            params.add(obj.get" + c_h_d_name + "());\n" +
                        "        }\n";
            }
            code += "        if (stringBuilder.length() > 0) {\n" +
                    "            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());\n" +
                    "        }else{\n" +
                    "            throw new RuntimeException(\"生成SQL（update）时 发现没有可修改的内容 \"+obj);\n" +
                    "        }\n" +
                    ids2 +
                    "        return new gzb.entity.SqlTemplate(sql +stringBuilder +\" where " + ids1 + "\",params.toArray());\n";
        } else {
            code += "        return null;\n";
        }
        code += "    }\n";
        //生成 sql 保存
        code += "   public gzb.entity.SqlTemplate toSaveSql(Object obj0) throws Exception {\n";
        if (classAttribute != null) {
            code += "       " + aClass.getName() + " obj=(" + aClass.getName() + ")obj0;\n";
            code += "        StringBuilder fields = new StringBuilder();\n" +
                    "        StringBuilder values = new StringBuilder();\n" +
                    "        java.util.List<Object> params = new java.util.ArrayList<>();\n";
            for (Field field : fields) {
                EntityAttribute fieldAttribute = field.getAnnotation(EntityAttribute.class);
                if (fieldAttribute == null) {
                    continue;
                }
                boolean key = fieldAttribute.key();
                String c_name = fieldAttribute.name();
                String c_h_name = field.getName();
                String c_h_d_name = Tools.lowStr_d(c_h_name);
                try {
                    aClass.getMethod("set" + c_h_d_name, field.getType());
                } catch (Exception e) {
                    continue;
                }
                if (key) {
                    code += "        if (obj.get" + c_h_d_name + "()==null) {\n" +
                            "            obj.set" + c_h_d_name + "(gzb.tools.OnlyId.getDistributed());\n" +
                            "        }\n";
                }

            }

            for (Field field : fields) {
                EntityAttribute fieldAttribute = field.getAnnotation(EntityAttribute.class);
                if (fieldAttribute == null) {
                    continue;
                }
                boolean key = fieldAttribute.key();

                String c_name = fieldAttribute.name();
                String c_h_name = field.getName();
                String c_h_d_name = Tools.lowStr_d(c_h_name);
                try {
                    aClass.getMethod("get" + c_h_d_name);
                } catch (Exception e) {
                    continue;
                }
                code += "        if (obj.get" + c_h_d_name + "() != null) {\n" +
                        "            values.append(\"?,\");\n" +
                        "            fields.append(\"" + c_name + ",\");\n" +
                        "            params.add(obj.get" + c_h_d_name + "());\n" +
                        "        }\n";
            }
            code += "        if (fields.length() > 0) {\n" +
                    "            fields.delete(fields.length() - 1, fields.length());\n" +
                    "            values.delete(values.length() - 1, values.length());\n" +
                    "        }else{\n" +
                    "            throw new RuntimeException(\"生成SQL（save）时 发现没有可插入的内容 \"+obj);\n" +
                    "        }\n" +
                    "        return new gzb.entity.SqlTemplate(\"insert into " + classAttribute.name() + "(\" +fields +\")values(\"+values+\")\",params.toArray());\n";
        } else {
            code += "        return null;\n";
        }
        code += "    }\n";

        //生成 sql 查询
        code += "   public gzb.entity.SqlTemplate toSelectSql(Object obj0) throws Exception {\n";
        if (classAttribute != null) {
            code += "       " + aClass.getName() + " obj=(" + aClass.getName() + ")obj0;\n";
            code += "        String sql = \"select * from " + classAttribute.name() + "\";\n" +
                    "        StringBuilder stringBuilder = new StringBuilder();\n" +
                    "        java.util.List<Object> params = new java.util.ArrayList<>();\n";
            for (Field field : fields) {
                EntityAttribute fieldAttribute = field.getAnnotation(EntityAttribute.class);
                if (fieldAttribute == null) {
                    continue;
                }
                boolean key = fieldAttribute.key();
                String c_name = fieldAttribute.name();
                String c_h_name = field.getName();
                String c_h_d_name = Tools.lowStr_d(c_h_name);
                try {
                    aClass.getMethod("get" + c_h_d_name);
                } catch (Exception e) {
                    continue;
                }
                if (key) {
                    code += "        if (obj.get" + c_h_d_name + "() != null) {\n" +
                            "            stringBuilder.append(\"" + c_name + "=? and \");\n" +
                            "            params.add(obj.get" + c_h_d_name + "());\n" +
                            "        }\n";
                }
            }

            for (Field field : fields) {
                EntityAttribute fieldAttribute = field.getAnnotation(EntityAttribute.class);
                if (fieldAttribute == null) {
                    continue;
                }
                boolean key = fieldAttribute.key();
                if (key) {
                    continue;
                }
                String c_name = fieldAttribute.name();
                String c_h_name = field.getName();
                String c_h_d_name = Tools.lowStr_d(c_h_name);
                try {
                    aClass.getMethod("get" + c_h_d_name);
                } catch (Exception e) {
                    continue;
                }
                code += "        if (obj.get" + c_h_d_name + "() != null) {\n" +
                        "            stringBuilder.append(\"" + c_name + "=? and \");\n" +
                        "            params.add(obj.get" + c_h_d_name + "());\n" +
                        "        }\n";
            }
            code += "        if (stringBuilder.length() > 0) {\n" +
                    "            stringBuilder.delete(stringBuilder.length() - 5, stringBuilder.length());\n" +
                    "            sql += \" where \";\n" +
                    "        }\n" +
                    "        return new gzb.entity.SqlTemplate(sql + stringBuilder,params.toArray());\n";
        } else {
            code += "        return null;\n";
        }
        code += "    }\n";


        code += "}";
        if (num == 0) {
            return null;
        }
        return code;
    }

    public static Object handlePrimitiveArray(Class<?> componentType, Object[] objects) {
        try {
            if (objects != null && objects.length > 0 && objects[0] != null) {
                if (componentType == int.class) {
                    return Tools.toArrayType(objects, new int[objects.length]);
                } else if (componentType == long.class) {
                    return Tools.toArrayType(objects, new long[objects.length]);
                } else if (componentType == double.class) {
                    return Tools.toArrayType(objects, new double[objects.length]);
                } else if (componentType == float.class) {
                    return Tools.toArrayType(objects, new float[objects.length]);
                } else if (componentType == boolean.class) {
                    return Tools.toArrayType(objects, new boolean[objects.length]);
                } else if (componentType == char.class) {
                    return Tools.toArrayType(objects, new char[objects.length]);
                } else if (componentType == short.class) {
                    return Tools.toArrayType(objects, new short[objects.length]);
                } else if (componentType == byte.class) {
                   /*
                    log.d(objects,objects.getClass(),objects instanceof Object[]);
                    return Tools.toArrayType(objects, new byte[objects.length]);
                    */
                }
            }
        } catch (NumberFormatException e) {

        }
        return null;
    }

    public static Object handlePrimitive(Class<?> type, Object[] objects) {
        if (objects != null && objects.length > 0 && objects[0] != null) {
            if (type == int.class || type == Integer.class) {
                if (objects[0].toString().isEmpty()) {
                    return null;
                }
                return Integer.parseInt(objects[0].toString());
            } else if (type == long.class || type == Long.class) {
                if (objects[0].toString().isEmpty()) {
                    return null;
                }
                return Long.valueOf(objects[0].toString());
            } else if (type == double.class || type == Double.class) {
                if (objects[0].toString().isEmpty()) {
                    return null;
                }
                return Double.valueOf(objects[0].toString());
            } else if (type == float.class || type == Float.class) {
                if (objects[0].toString().isEmpty()) {
                    return null;
                }
                return Float.valueOf(objects[0].toString());
            } else if (type == boolean.class || type == Boolean.class) {
                if (objects[0].toString().isEmpty()) {
                    return null;
                }
                return Boolean.valueOf(objects[0].toString());
            } else if (type == char.class) {
                if (objects[0].toString().isEmpty()) {
                    return null;
                }
                return objects[0].toString().charAt(0);
            } else if (type == short.class || type == Short.class) {
                if (objects[0].toString().isEmpty()) {
                    return null;
                }
                return Short.valueOf(objects[0].toString());
            } else if (type == byte.class || type == Byte.class) {
                if (objects[0].toString().isEmpty()) {
                    return null;
                }
                return Byte.valueOf(objects[0].toString());
            } else if (type == String.class) {
                if (objects[0].toString().isEmpty()) {
                    return "";
                }
                return objects[0].toString();
            }
        }
        return null;
    }

    //传入对象 给对象的所有类变量进行注入 如果不是接口那么 mapObject.get(类变量类型全名) 能取出对应实现类对象 如果是null说明不存在  如果存在就注入进去
    public static boolean classInject(Object object, Object[] data, Map<String, Object> mapObjectAll) throws Exception {
        if (object == null) {
            return false;
        }

        Field[] fields = getCombinedFields(object.getClass(), false);
        for (int i = 0; i < fields.length; i++) {
            Resource resources = fields[i].getDeclaredAnnotation(Resource.class);
            Object obj1 = null;
            String key = fields[i].getType().getName();
            if (resources != null) {
                if (resources.value().length() > 0) {
                    key = resources.value();
                }
                obj1 = mapObjectAll.get(key);
                if (obj1 == null && data != null) {
                    for (int i1 = 0; i1 < data.length; i1++) {
                        if (data[i1] != null) {
                            if (data[i1].getClass().getName().equals(key)) {
                                obj1 = data[i1];
                                break;
                            }
                            if (obj1 == null) {
                                Class[] classes = data[i1].getClass().getInterfaces();
                                for (Class aClass : classes) {
                                    if (aClass.getName().equals(key)) {
                                        obj1 = data[i1];
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (obj1 != null) {
                    log.t("类字段注入", "将", obj1, "注入到对象", object, "的类变量", fields[i].getName(), "该变量的类型为", key);
                    classInject(obj1, data, mapObjectAll);
                    fields[i].setAccessible(true);
                    fields[i].set(object, obj1);
                    fields[i].setAccessible(false);
                }
            }
        }
        return true;
    }

    // 使用静态常量来提高可读性和集中管理
    private static final char PATH_SEPARATOR_CHAR = '/';
    private static final String PATH_SEPARATOR_STRING = "/";
    private static final char WINDOWS_SEPARATOR_CHAR = '\\';
    private static final char LAST_CHAR = '\\';

    public static String webPathFormat(String path) {
        if (path == null || path.isEmpty()) {
            return PATH_SEPARATOR_STRING;
        }

        StringBuilder sb = new StringBuilder();

        // 1. 确保以 '/' 开头
        if (path.charAt(0) != PATH_SEPARATOR_CHAR) {
            sb.append(PATH_SEPARATOR_CHAR);
        }

        // 2. 遍历并处理
        char lastChar = LAST_CHAR;
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);

            // 将所有 \ 替换为 /
            if (c == WINDOWS_SEPARATOR_CHAR) {
                c = PATH_SEPARATOR_CHAR;
            }

            // 过滤掉连续的 /
            if (c == PATH_SEPARATOR_CHAR && lastChar == PATH_SEPARATOR_CHAR) {
                continue;
            }

            sb.append(c);
            lastChar = c;
        }

        // 3. 确保以 '/' 结尾
        if (sb.length() > 1 && sb.charAt(sb.length() - 1) != PATH_SEPARATOR_CHAR) {
            sb.append(PATH_SEPARATOR_CHAR);
        }

        return sb.toString();
    }

    /**
     * 根据 CrossDomain 注解和请求头信息生成对应的 HTTP 响应头。
     */
    public static Map<String, String> generateCORSHeaders(CrossDomain crossDomain, Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        String[] origins = crossDomain.origins();
        boolean isStarAllowed = Arrays.asList(origins).contains("*");
        if (isStarAllowed) {
            headers.put("access-control-allow-origin", "*");
        } else {
            headers.put("access-control-allow-origin", "origin"); // 响应时替换
        }
        String[] methods = crossDomain.methods();
        if (methods.length > 0) {
            headers.put("access-control-allow-methods", String.join(", ", methods));
        }

        String[] reqHeaders = crossDomain.headers();
        if (reqHeaders.length > 0) {
            headers.put("access-control-allow-headers", String.join(", ", reqHeaders));
        }

        String[] exposedHeaders = crossDomain.exposedHeaders();
        if (exposedHeaders.length > 0) {
            headers.put("access-control-expose-headers", String.join(", ", exposedHeaders));
        }

        if (crossDomain.allowCredentials()) {
            headers.put("access-control-allow-credentials", "true");
            headers.put("access-control-allow-origin", "origin"); // 响应时替换
        }

        long maxAge = crossDomain.maxAge();
        if (maxAge >= 0) {
            headers.put("access-control-max-age", String.valueOf(maxAge));
        }

        return headers;
    }

    /**
     * 根据 Method 生成签名
     *
     * @param method method
     * @return key
     */
    public static String getSing(Method method, Class<?> aClass) {
        Class[] classes = method.getParameterTypes();
        StringBuilder key = new StringBuilder(aClass.getName() + "-" + method.getName() + "(");
        for (int i = 0; i < classes.length; i++) {
            key.append(classes[i].getName());
            if (i < classes.length - 1) {
                key.append(",");
            }
        }
        key.append(")");
        return key.toString();
    }

    public static Field[] getCombinedFields(Class<?> clazz) {
        return getCombinedFields(clazz, true);
    }

    /**
     * 获取所有类字段
     */
    public static Field[] getCombinedFields(Class<?> clazz, boolean isCache) {
        Field[] fields = null;
        if (isCache) {
            fields = mapField.get(clazz.getName());
            if (fields != null) {
                return fields;
            }
        }

        Set<Field> combinedFields = new HashSet<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        Field[] publicFields = clazz.getFields();
        combinedFields.addAll(Arrays.asList(declaredFields));
        combinedFields.addAll(Arrays.asList(publicFields));
        fields = combinedFields.toArray(new Field[0]);
        mapField.put(clazz.getName(), fields);
        return fields;
    }

    public static Field[] getCombinedFieldsNotCache(Class<?> clazz) {
        Set<Field> combinedFields = new HashSet<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        Field[] publicFields = clazz.getFields();
        combinedFields.addAll(Arrays.asList(declaredFields));
        combinedFields.addAll(Arrays.asList(publicFields));
        return combinedFields.toArray(new Field[0]);
    }

    /**
     * 获取所有方法
     */
    public static Method[] getCombinedMethods(Class<?> clazz) {
        Set<Method> combinedFields = new HashSet<>();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        Method[] publicMethods = clazz.getMethods();
        combinedFields.addAll(Arrays.asList(declaredMethods));
        combinedFields.addAll(Arrays.asList(publicMethods));
        return combinedFields.toArray(new Method[0]);
    }

    /**
     * 获取类名+接口名
     */
    public static String[] getCombinedInterfaces(Class<?> clazz) {
        return getCombinedInterfaces(clazz, null);
    }

    /**
     * 获取类名+接口名
     */
    public static String[] getCombinedInterfaces(Class<?> clazz, String name) {
        Class<?>[] interfaces = clazz.getInterfaces();
        List<String> list = new ArrayList<>();
        for (Class<?> anInterface : interfaces) {
            if (anInterface.getName().equals("groovy.lang.GroovyObject")) {
                continue;
            }
            list.add(anInterface.getName());
        }
        list.add(clazz.getName());
        if (name != null) {
            list.add(name);
        }
        return list.toArray(new String[0]);
    }

    public static Object putObject(Class<?> clazz, String name, Map<String, Object> map) throws Exception {
        Object object = null;

        object = clazz.getDeclaredConstructor().newInstance();
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            if (anInterface.getName().equals("groovy.lang.GroovyObject")) {
                continue;
            }
            if (anInterface.getName().equals("gzb.frame.factory.GzbOneInterface")) {
                continue;
            }
            map.put(anInterface.getName(), object);
            log.t("储存对象", anInterface.getName(), " -> ", object);
        }
        map.put(clazz.getName(), object);
        if (name != null && !name.isEmpty()) {
            map.put(name, object);
        }
        return object;
    }


    // 定义一个枚举来区分操作
    private enum TransactionAction {
        COMMIT, ROLLBACK, CLOSE
    }

    // 通用的事务操作
    private static void handleTransaction(List<BaseDao> listBaseDao, TransactionAction action) throws Exception {
        if (listBaseDao == null) {
            return;
        }
        // 使用 Set 确保每个 DataBase 实例只处理一次
        Set<Connection> connectionsToProcess = new HashSet<>();
        for (BaseDao baseDao : listBaseDao) {
            if (baseDao != null && baseDao.getConnect() != null) {
                connectionsToProcess.add(baseDao.getConnect());
            }
        }

        // 对每个连接执行操作
        for (Connection connection : connectionsToProcess) {
            if (connection != null && !connection.isClosed()) {
                switch (action) {
                    case COMMIT:
                        if (!connection.getAutoCommit()) {
                            connection.commit();
                        }
                        log.t("commit", connection);
                        break;
                    case ROLLBACK:
                        if (!connection.getAutoCommit()) {
                            connection.rollback();
                        }
                        log.t("rollback", connection);
                        break;
                    case CLOSE:
                        if (!connection.getAutoCommit()) {
                            connection.setAutoCommit(true); // 恢复自动提交
                        }
                        connection.close();
                        log.t("close", connection);
                        break;
                }
            }
        }
    }

    public static List<BaseDao> transactionOpen(boolean isOpenTransaction, List<Object> list) throws Exception {
        List<BaseDao> listBaseDao = null;
        Map<String, Connection> mapConnection = new HashMap<>();
        if (isOpenTransaction) {
            listBaseDao = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof BaseDao) {
                    BaseDao baseDao1 = (BaseDao) list.get(i);
                    BaseDao baseDao2 = baseDao1.getClass().getConstructor().newInstance();
                    Connection connection = mapConnection.get(baseDao1.getDataBase().getSign());
                    if (connection != null) {
                        baseDao2.setConnect(connection, true);
                    } else {
                        connection = baseDao2.getConnect();
                        mapConnection.put(baseDao2.getDataBase().getSign(), connection);
                        baseDao2.setConnect(connection, true);
                    }
                    list.set(i, baseDao2);
                    listBaseDao.add(baseDao2);
                    log.t("openTransaction", baseDao1, "->", baseDao2);
                }
            }
        }
        return listBaseDao;
    }

    public static void transactionCommit(List<BaseDao> listBaseDao) throws Exception {
        handleTransaction(listBaseDao, TransactionAction.COMMIT);
    }

    public static void transactionRollback(List<BaseDao> listBaseDao) throws Exception {
        handleTransaction(listBaseDao, TransactionAction.ROLLBACK);
    }

    public static void transactionEndTransaction(List<BaseDao> listBaseDao) throws Exception {
        handleTransaction(listBaseDao, TransactionAction.CLOSE);
    }
}
