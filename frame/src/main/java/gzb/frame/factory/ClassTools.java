/*
 *
 *  * Copyright [2025] [GZB ONE]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package gzb.frame.factory;

import com.alibaba.fastjson2.JSON;
import gzb.entity.RunRes;
import gzb.entity.SqlTemplate;
import gzb.exception.GzbException0;
import gzb.frame.annotation.*;
import gzb.frame.db.BaseDao;
import gzb.entity.FileUploadEntity;
import gzb.frame.db.DataBase;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;
import gzb.tools.*;
import gzb.tools.json.GzbJson;
import gzb.tools.json.Result;
import gzb.tools.log.Log;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClassTools {
    public static void main(String[] args) {
        RunRes runRes = new RunRes();
        runRes.setState(1);
        RunRes runRes2 = new RunRes();
        runRes2.setState(1);
        runRes.setData(runRes2);
        System.out.println(Arrays.toString("\",:{}".getBytes()));
        System.out.println(new String(Tools.toJson(runRes)));
        System.out.println(new String(toJsonObjectByte(runRes)));
        System.out.println(new String(JSON.toJSONBytes(runRes)));
        long size = 0;
        long t01 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            size += toJsonObjectByte(runRes).length;
        }
        long t02 = System.currentTimeMillis();
        System.out.println((t02 - t01));
        size = 0;
        t01 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            size += JSON.toJSONString(runRes).length();
        }
        t02 = System.currentTimeMillis();
        System.out.println((t02 - t01));

        size = 0;
        t01 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            size += toJsonObjectByte(runRes).length;
        }
        t02 = System.currentTimeMillis();
        System.out.println((t02 - t01));
        size = 0;
        t01 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            size += Tools.toJson(runRes).length();
        }
        t02 = System.currentTimeMillis();
        System.out.println((t02 - t01));
    }

    public static Log log = Log.log;
    static Map<Class<?>, Object> mapLoadObjectObject = new ConcurrentHashMap<>();
    public static Map<String, Field[]> mapField = new ConcurrentHashMap<>();

    static Lock lock = new ReentrantLock();


    public static String toName(Class<?> type, boolean noArray) {
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
        if (noArray) {
            Class<?> c01 = type.getComponentType();
            if (c01 != null) {
                return toName(type.getComponentType(), true);
            }
        }
        return type.getCanonicalName();
    }

    public static String toName(Class<?> type) {

        return toName(type, false);
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
        }*/

    /**
     * 通过 ASM 字节码技术，获取指定 Method 的参数名列表。
     * <p>
     * 注意：目标类必须使用 -parameters 选项编译，否则参数名将是 arg0, arg1 等默认值。
     *
     * @param method 要获取参数名的反射 Method 对象
     * @return 参数名列表，如果无法获取则返回空列表
     */
    public static List<String> getParameterNamesByAsm(final Method method, final Class<?>[] parameterTypes) {
        List<String> parameterNames = new ArrayList<>();
        try {
            Class<?> declaringClass = method.getDeclaringClass();

            // 如果方法没有参数，直接返回空列表
            if (parameterTypes.length == 0) {
                return parameterNames;
            }

            // 1. 获取类文件的输入流
            String className = declaringClass.getName().replace('.', '/') + ".class";
            InputStream is = declaringClass.getClassLoader().getResourceAsStream(className);

            if (is == null) {
                // 无法找到 .class 文件，通常发生在动态代理或特殊的类加载器中
                return parameterNames;
            }

            try {
                // 2. 创建 ClassReader 来读取字节码
                ClassReader classReader = new ClassReader(is);

                // 3. 定义 ClassVisitor
                classReader.accept(new ClassVisitor(Opcodes.ASM9) {

                    @Override
                    public MethodVisitor visitMethod(int access, String name, String descriptor,
                                                     String signature, String[] exceptions) {

                        if (name.equals(method.getName()) && descriptor.equals(org.objectweb.asm.Type.getMethodDescriptor(method))) {

                            return new MethodVisitor(Opcodes.ASM9) {

                                // 非静态方法从索引 1 开始（索引 0 是 'this'），静态方法从 0 开始
                                private int nextExpectedIndex = Modifier.isStatic(method.getModifiers()) ? 0 : 1;
                                private int paramIndex = 0; // 跟踪当前已获取的参数数量

                                @Override
                                public void visitLocalVariable(String name, String descriptor, String signature,
                                                               org.objectweb.asm.Label start, org.objectweb.asm.Label end, int index) {

                                    // 1. 只处理方法参数 (即 index 必须是预期参数的索引)
                                    if (index == nextExpectedIndex && paramIndex < parameterTypes.length) {

                                        // 排除 Java 编译器默认生成的参数名 (arg0, arg1...)
                                        if (!name.matches("arg\\d+")) {
                                            parameterNames.add(name);
                                        } else {
                                            // 如果是默认名，我们仍然占位，防止影响后续参数的索引计算
                                            parameterNames.add(null);
                                        }

                                        // 2. 处理 long/double 类型的索引槽位跳跃
                                        Class<?> currentParamType = parameterTypes[paramIndex];
                                        if (currentParamType == long.class || currentParamType == double.class) {
                                            // long/double 占用两个槽位
                                            nextExpectedIndex += 2;
                                        } else {
                                            // 其他类型占用一个槽位
                                            nextExpectedIndex += 1;
                                        }

                                        paramIndex++; // 实际参数计数器前进
                                    }
                                }
                            };
                        }
                        return super.visitMethod(access, name, descriptor, signature, exceptions);
                    }
                }, 0);

            } catch (IOException e) {
                // 实际应用中应该记录错误日志
                log.e("getParameterNamesByAsm", e);
            } finally {
                try {
                    is.close();
                } catch (IOException ignored) {

                    log.e("getParameterNamesByAsm", ignored);
                }
            }
            // 清理：如果中间有 null 占位，且最终参数名数量与期望不符，说明编译时没有保留参数名，需要根据 parameterTypes 数量截断或填充
            if (parameterNames.size() != parameterTypes.length) {
                // 通常情况下，如果正确编译，这里不应该发生数量不匹配
                // 容错处理：清除所有不完整的或不规范的结果
                parameterNames.clear();
            }
        } catch (Exception e) {
            log.e("getParameterNamesByAsm", e);
        }
        return parameterNames;

    }

    /**
     * 辅助方法：获取 Method 的描述符 (Descriptor)
     *
     * @param method 反射方法
     * @return 方法描述符字符串
     */
    private static String getMethodDescriptor(Method method) {
        StringBuilder sb = new StringBuilder("(");
        for (Class<?> paramType : method.getParameterTypes()) {
            sb.append(org.objectweb.asm.Type.getDescriptor(paramType));
        }
        sb.append(")");
        sb.append(org.objectweb.asm.Type.getDescriptor(method.getReturnType()));
        return sb.toString();
    }

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
        //log.d("获取参数名", methodName, names);
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
        if (!code.contains("public Object _gzb_call_x01")) {
            code = code.replace(name + tmp0 + "{", name + tmp + "{");
            code = code.substring(0, code.lastIndexOf("}"));
            code += appendCode + "\r\n}";
        }

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
                                    } else if (componentType.isAssignableFrom(values.get(0).getClass())) {
                                        log.d("数组参数注入", i, paramName, paramValue, values.get(i), array.getClass());
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
                            Object[] objects = loadObject(paramType, requestDataMap);
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
                    throw new GzbException0("请求参数 数字类型 转换错误 -> 名称:" + paramName + ",类型:" + paramType + ",参数:" + requestDataMap);
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
                obj = mapLoadObjectObject.get(aClass);
                if (obj == null) {
                    String code;
                    Class<?> aClass1 = null;
                    if (aClass.isArray()) {
                        aClass1 = aClass.getComponentType();
                    } else {
                        aClass1 = aClass;
                    }
                    //"package "+className+"_v1;\npublic class " + (className.replaceAll("\\.", "_")) + "_inner_v1
                    Class<?> aClass0 = null;
                    //兼容打包后的版本 打包后 是exe文件 类无需生成 生成会出错 所以要这么做
                    String classNameAll = aClass1.getName() + "_v1" + "." + (aClass1.getName().replaceAll("\\.", "_")) + "_inner_v1";
                    try {
                        aClass0 = Class.forName(classNameAll); //直接加载已有类 打包后必定有
                    } catch (Exception e) {//预期内的可能错误 吞掉 真实编译的错误才有意义
                        code = ClassTools.gen_entity_load_code_v1(aClass1); //通过反射 生成新类
                        if (code != null) {
                            aClass0 = ClassLoad.compileJavaCode(code);//编译新类
                        } else {
                            mapLoadObjectObject.put(aClass, "1");
                        }
                    }
                    if (aClass0 != null) {
                        obj = aClass0.getDeclaredConstructor().newInstance();
                        mapLoadObjectObject.put(aClass, obj);
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

    public static SqlTemplate toSaveSql(Object object, gzb.frame.db.DataBase dataBase, boolean reset) {
        GzbEntityInterface gzbEntityInterface = readObject(object.getClass());
        if (gzbEntityInterface == null) {
            return null;
        }
        try {
            return gzbEntityInterface.toSaveSql(object, dataBase, reset);
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

    public static Object[] loadObject(Class<?> aClass, Map<String, List<Object>> map) {
        GzbEntityInterface gzbEntityInterface = readObject(aClass);
        if (gzbEntityInterface == null) {
            return null;
        }
        try {
            return gzbEntityInterface.loadObject(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T loadResultSet(Class<?> aClass, java.sql.ResultSet resultSet, java.util.Set<String> names) {
        GzbEntityInterface gzbEntityInterface = readObject(aClass);
        if (gzbEntityInterface == null) {
            return null;
        }
        try {
            return (T) gzbEntityInterface.loadResultSet(resultSet, names);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJsonObject(Object obj) {
        if (obj == null) {
            return null;
        }
        GzbEntityInterface gzbEntityInterface = readObject(obj.getClass());
        if (gzbEntityInterface == null) {
            return null;
        }
        try {
            return gzbEntityInterface.toJson(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void toJsonObject(Object obj, StringBuilder stringBuilder) {
        if (obj == null) {
            return;
        }
        GzbEntityInterface gzbEntityInterface = readObject(obj.getClass());
        if (gzbEntityInterface == null) {
            return;
        }
        try {
            gzbEntityInterface.toJson(obj, stringBuilder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] toJsonObjectByte(Object obj) {
        if (obj == null) {
            log.d("1", obj);
            return null;
        }
        GzbEntityInterface gzbEntityInterface = readObject(obj.getClass());
        if (gzbEntityInterface == null) {
            return null;
        }
        try {
            return gzbEntityInterface.toJsonBytes(obj);
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

    public static Object gen_call_code_v4_object(Class<?> aClass, String javaCode) throws Exception {
        Class<?> a01 = gen_call_code_v4_class(aClass, javaCode);
        return a01.getDeclaredConstructor().newInstance();
    }

    public static Class<?> gen_call_code_v4_class(Class<?> aClass, String javaCode) throws Exception {
        String new_code = gen_call_code_v4_all_code(aClass, javaCode);
        //System.out.println(new_code);
        Class a01 = null;
        try {
            a01 = ClassLoad.compileJavaCode(new_code);
        } catch (Exception e) {
            FileTools.save(new File(Config.configFile.getParentFile().getPath() + "/" + System.currentTimeMillis() + ".java.txt"), new_code);
            throw new RuntimeException(e);
        }
        return a01;
    }

    public static String gen_call_code_v4_all_code(Class<?> aClass, String javaCode) throws Exception {
        if (javaCode.contains("gzb.frame.factory.GzbOneInterface") && javaCode.contains("_gzb_call_x01")) {
            return javaCode;
        }
        String t_code = ClassTools.gen_call_code_v4(aClass, javaCode);
        String new_code = ClassTools.updateCode(javaCode, aClass.getSimpleName(), "gzb.frame.factory.GzbOneInterface", t_code);
        return new_code;
    }

    public static String gen_entity_load_code_v1(Class<?> aClass) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes01 = new byte[]{34, 44, 58}; //引号 逗号 冒号
        if (!Modifier.isPublic(aClass.getModifiers())) {
            return null;
        }
        if (aClass.getName().contains("gzb.frame") || aClass.getName().contains("gzb.tools")) {
            return null;
        }
        EntityAttribute classAttribute = aClass.getAnnotation(EntityAttribute.class);

        int num = 0;
        Field[] fields = aClass.getDeclaredFields();
        String className = aClass.getName();
        String code = "package " + className + "_v1;\npublic class " + (className.replaceAll("\\.", "_")) + "_inner_v1 implements gzb.frame.factory.GzbEntityInterface{\n" +
                "   public "+ (className.replaceAll("\\.", "_")) +"_inner_v1(){\n" +
                "        \n" +
                "    }\n";
        for (Field field : fields) {
            if (field.getType() == byte.class || field.getType() == Byte.class
                    || field.getType() == byte[].class || field.getType() == Byte[].class) {
                continue;
            }
            code += "byte[]_" + field.getName() + "_bytes=new byte[]{";
            byte[] bytes = field.getName().getBytes(Config.encoding);
            for (int i = 0; i < bytes.length; i++) {
                code += bytes[i];
                if (i < bytes.length - 1) {
                    code += ",";
                }
            }
            code += "};\n";
        }

        code += "byte[]bytes01=new byte[]{34, 44, 58, 123, 125}; //引号 逗号 冒号 大括号左 大括号右\n";
        code += "   public byte[] toJsonBytes(Object object) throws Exception{\n" +
                "      try{\n" +
                "        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream(256);\n" +
                "        baos.write(bytes01[3]);" +
                "        if (object instanceof " + className + ") {\n" +
                "           " + className + " obj0=(" + className + ")object;\n";

        for (Field field : fields) {
            if (field.getType() == byte.class || field.getType() == Byte.class
                    || field.getType() == byte[].class || field.getType() == Byte[].class) {
                continue;
            }
            String name = (field.getName());
            String name_d = Tools.lowStr_d(field.getName());
        /*    if (!ClassTools.isWrapperOrPrimitive(field.getType())) {
                continue;
            }*/
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

                baos.write(bytes01[0]);//引号
                baos.write(bytes01[2]);//冒号
                baos.write(bytes01[1]);//逗号
                if (type == 0) {
                    code += (field.getType().isPrimitive() ? "" : "           if (obj0." + name + "!=null) {\n") +
                            "if (baos.size()>1) {baos.write(bytes01[1]);}\n" +
                            "baos.write(bytes01[0]);\n" +
                            "baos.write(_" + name + "_bytes);\n" +//需要预生成一个 byte[]
                            "baos.write(bytes01[0]);\n" +
                            "baos.write(bytes01[2]);\n" +
                            "gzb.tools.Tools.toJsonBytes(obj0." + name + ",baos);\n" +
                            (field.getType().isPrimitive() ? "" : "}\n");
                } else if (type == 1) {
                    code += (field.getType().isPrimitive() ? "" : "           if (obj0." + name + "!=null) {\n") +
                            "if (baos.size()>1) {baos.write(bytes01[1]);}\n" +
                            "baos.write(bytes01[0]);\n" +
                            "baos.write(_" + name + "_bytes);\n" +//需要预生成一个 byte[]
                            "baos.write(bytes01[0]);\n" +
                            "baos.write(bytes01[2]);\n" +
                            "baos.write(gzb.tools.Tools.toBytes(obj0." + name + "));\n" +
                            (field.getType().isPrimitive() ? "" : "}\n");
                } else if (type == 2) {
                    code += (field.getType().isPrimitive() ? "" : "           if (obj0." + name + "!=null) {\n") +
                            "if (baos.size()>1) {baos.write(bytes01[1]);}\n" +
                            "baos.write(bytes01[0]);\n" +
                            "baos.write(_" + name + "_bytes);\n" +//需要预生成一个 byte[]
                            "baos.write(bytes01[0]);\n" +
                            "baos.write(bytes01[2]);\n" +
                            "baos.write(bytes01[0]);\n" +
                            "baos.write(gzb.tools.Tools.toBytes(obj0." + name + "));\n" +
                            "baos.write(bytes01[0]);\n" +
                            (field.getType().isPrimitive() ? "" : "}\n");
                }
                num++;
            } else {
                try {
                    aClass.getMethod("get" + name_d);

                    if (type == 0) {
                        code += (field.getType().isPrimitive() ? "" : "           if (obj0.get" + name_d + "()!=null) {\n") +
                                "if (baos.size()>1) {baos.write(bytes01[1]);}\n" +
                                "baos.write(bytes01[0]);\n" +
                                "baos.write(_" + name + "_bytes);\n" +//需要预生成一个 byte[]
                                "baos.write(bytes01[0]);\n" +
                                "baos.write(bytes01[2]);\n" +
                                "gzb.tools.Tools.toJsonBytes(obj0.get" + name_d + "(),baos);\n" +
                                (field.getType().isPrimitive() ? "" : "}\n");
                    } else if (type == 1) {
                        code += (field.getType().isPrimitive() ? "" : "           if (obj0.get" + name_d + "()!=null) {\n") +
                                "if (baos.size()>1) {baos.write(bytes01[1]);}\n" +
                                "baos.write(bytes01[0]);\n" +
                                "baos.write(_" + name + "_bytes);\n" +//需要预生成一个 byte[]
                                "baos.write(bytes01[0]);\n" +
                                "baos.write(bytes01[2]);\n" +
                                "baos.write(gzb.tools.Tools.toBytes(obj0.get" + name_d + "()));\n" +
                                (field.getType().isPrimitive() ? "" : "}\n");
                    } else if (type == 2) {
                        code += (field.getType().isPrimitive() ? "" : "           if (obj0.get" + name_d + "()!=null) {\n") +
                                "if (baos.size()>1) {baos.write(bytes01[1]);}\n" +
                                "baos.write(bytes01[0]);\n" +
                                "baos.write(_" + name + "_bytes);\n" +//需要预生成一个 byte[]
                                "baos.write(bytes01[0]);\n" +
                                "baos.write(bytes01[2]);\n" +
                                "baos.write(bytes01[0]);\n" +
                                "baos.write(gzb.tools.Tools.toBytes(obj0.get" + name_d + "()));\n" +
                                "baos.write(bytes01[0]);\n" +
                                (field.getType().isPrimitive() ? "" : "}\n");
                    }
                    num++;
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        }


        code += "        }\n" +
                "        baos.write(bytes01[4]);\n" +
                "        return baos.toByteArray();\n" +
                "     }catch (Exception e){\n" +
                "        throw new RuntimeException(\"实体类->" + className + "转换JSON异常\",e);\n" +
                "     }\n" +
                "   }\n";


        code += "   public String toJson(Object object) throws Exception{\n" +
                "try{\n" +
                "        StringBuilder sb=new StringBuilder(\"{\");\n" +
                "        if (object instanceof " + className + ") {\n" +
                "           " + className + " obj0=(" + className + ")object;\n";

        for (Field field : fields) {
            if (field.getType() == byte.class || field.getType() == Byte.class
                    || field.getType() == byte[].class || field.getType() == Byte[].class) {
                continue;
            }
            String name = (field.getName());
            String name_d = Tools.lowStr_d(field.getName());
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
        code += "   public void toJson(Object object,StringBuilder sb) throws Exception{\n" +
                "try{\n" +
                "        boolean app01=false;\n" +
                "        sb.append(\"{\");\n" +
                "        if (object!=null &&  object.getClass() == " + className + ".class) {\n" +
                "           " + className + " obj0=(" + className + ")object;\n";

        for (Field field : fields) {
            if (field.getType() == byte.class || field.getType() == Byte.class
                    || field.getType() == byte[].class || field.getType() == Byte[].class) {
                continue;
            }
            String name = (field.getName());
            String name_d = Tools.lowStr_d(field.getName());
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
                            "if(app01){\n" +
                            "sb.append(\",\");\n" +
                            "}\n" +
                            "app01=true;\n" +
                            "               sb.append(\"\\\"" + name + "\\\":\");\n" +
                            "               gzb.tools.Tools.toJson(obj0." + name + ",sb);\n" +
                            (field.getType().isPrimitive() ? "" : "           }\n");
                } else if (type == 1) {
                    code += (field.getType().isPrimitive() ? "" : "           if (obj0." + name + "!=null) {\n") +
                            "if(app01){\n" +
                            "sb.append(\",\");\n" +
                            "}\n" +
                            "app01=true;\n" +
                            "               sb.append(\"\\\"" + name + "\\\":\").append(obj0." + name + ");\n" +
                            (field.getType().isPrimitive() ? "" : "           }\n");
                } else if (type == 2) {
                    code += (field.getType().isPrimitive() ? "" : "           if (obj0." + name + "!=null) {\n") +
                            "if(app01){\n" +
                            "sb.append(\",\");\n" +
                            "}\n" +
                            "app01=true;\n" +
                            "               sb.append(\"\\\"" + name + "\\\":\\\"\").append(obj0." + name + ").append(\"\\\"\");\n" +
                            (field.getType().isPrimitive() ? "" : "           }\n");
                }
                num++;
            } else {
                try {
                    aClass.getMethod("get" + name_d);
                    if (type == 0) {
                        code += (field.getType().isPrimitive() ? "" : "           if (obj0.get" + name_d + "()!=null) {\n") +
                                "if(app01){\n" +
                                "sb.append(\",\");\n" +
                                "}\n" +
                                "app01=true;\n" +
                                "               sb.append(\"\\\"" + name + "\\\":\");\n" +
                                "gzb.tools.Tools.toJson(obj0.get" +
                                name_d + "(),sb);\n" +
                                (field.getType().isPrimitive() ? "" : "           }\n");
                    } else if (type == 1) {
                        code += (field.getType().isPrimitive() ? "" : "           if (obj0.get" + name_d + "()!=null) {\n") +
                                "if(app01){\n" +
                                "sb.append(\",\");\n" +
                                "}\n" +
                                "app01=true;\n" +
                                "               sb.append(\"\\\"" + name + "\\\":\").append(obj0.get" + name_d + "());\n" +
                                (field.getType().isPrimitive() ? "" : "           }\n");
                    } else if (type == 2) {
                        code += (field.getType().isPrimitive() ? "" : "           if (obj0.get" + name_d + "()!=null) {\n") +
                                "if(app01){\n" +
                                "sb.append(\",\");\n" +
                                "}\n" +
                                "app01=true;\n" +
                                "               sb.append(\"\\\"" + name + "\\\":\\\"\").append(obj0.get" + name_d + "()).append(\"\\\"\");\n" +
                                (field.getType().isPrimitive() ? "" : "           }\n");
                    }

                    num++;
                } catch (Exception e) {

                }
            }
        }


        code += "        }\n" +
                "        sb.append(\"}\");\n" +
                "     }catch (Exception e){\n" +
                "        throw new RuntimeException(\"实体类->" + className + "转换JSON异常\",e);\n" +
                "   }\n" +
                "   }\n";

        code += "   public Object[] loadObject" +
                "(java.util.Map<String,java.util.List<Object>> map) throws Exception {\n";
        try {
            // 尝试获取公共的无参构造函数
            Constructor<?> constructor = aClass.getConstructor();
            code += "        java.util.List<" + className + "> returnObj = null;\n" +
                    "        java.util.List<Object> list = null;\n" +
                    "        try {\n";
            for (Field field : fields) {
                if (field.getType() == byte.class || field.getType() == Byte.class
                        || field.getType() == byte[].class || field.getType() == Byte[].class) {
                    continue;
                }
                if (field.getType().isArray()) {
                    continue;
                }
                if (!ClassTools.isBasicTypes(field.getType())) {
                    continue;
                }
                if (Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                String tName = ClassTools.toName(field.getType(), true);
                if (Modifier.isPublic(field.getModifiers())) {
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
                    if (field.getType() == java.time.LocalDateTime.class) {
                        code += "                    returnObj.get(i)." + field.getName() + "=new gzb.tools.DateTime(list.get(i).toString()).toLocalDateTime();\n";
                    } else if (field.getType() == java.sql.Timestamp.class) {
                        code += "                    returnObj.get(i)." + field.getName() + "=new gzb.tools.DateTime(list.get(i).toString()).toTimestamp();\n";
                    } else if (field.getType() == DateTime.class) {
                        code += "                    returnObj.get(i)." + field.getName() + "=new gzb.tools.DateTime(list.get(i).toString());\n";
                    } else if (field.getType() == Date.class) {
                        code += "                    returnObj.get(i)." + field.getName() + "=new gzb.tools.DateTime(list.get(i).toString()).toDate();\n";
                    } else {
                        code += "                    returnObj.get(i)." + field.getName() + "=" + tName + ".valueOf(list.get(i).toString());\n";
                    }
                    code += "                }\n" +
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
                        if (field.getType() == java.time.LocalDateTime.class) {
                            code += "                    returnObj.get(i).set" + name_d + "(new gzb.tools.DateTime(list.get(i).toString()).toLocalDateTime());\n";
                        } else if (field.getType() == java.util.Date.class) {
                            code += "                    returnObj.get(i).set" + name_d + "(new gzb.tools.DateTime(list.get(i).toString()).toDate());\n";
                        } else if (field.getType() == java.sql.Timestamp.class) {
                            code += "                    returnObj.get(i).set" + name_d + "(new gzb.tools.DateTime(list.get(i).toString()).toTimestamp());\n";
                        } else if (field.getType() == DateTime.class) {
                            code += "                    returnObj.get(i).set" + name_d + "(new gzb.tools.DateTime(list.get(i).toString()));\n";
                        } else {
                            code += "                    returnObj.get(i).set" + name_d + "(" + tName + ".valueOf(list.get(i).toString()));\n";
                        }
                        code += "                }\n" +
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
        code += "    public Object loadResultSet(java.sql.ResultSet resultSet, java.util.Set<String> names) throws Exception{\n";
        if (classAttribute != null) {
            try {
                aClass.getDeclaredConstructor();
                code += "        " + className + " obj = new " + className + "();\n";
                for (Field field : fields) {
                    if (field.getType() == byte.class || field.getType() == Byte.class
                            || field.getType() == byte[].class || field.getType() == Byte[].class) {
                        continue;
                    }
                    EntityAttribute fieldAttribute = field.getAnnotation(EntityAttribute.class);
                    if (fieldAttribute == null) {
                        continue;
                    }
                    String name = (field.getName());
                    String name_d = Tools.lowStr_d(field.getName());
                    if (Modifier.isPublic(field.getModifiers())) {
                        num++;
                        code += "        if (names.contains(\"" + fieldAttribute.name() + "\")) {\n" +
                                "            obj." + name + " = resultSet.getObject(\"" + fieldAttribute.name() + "\"," + field.getType().getName() + ".class);\n" +
                                "        }\n";

                    } else {
                        try {
                            aClass.getMethod("set" + name_d, field.getType());
                            code += "        if (names.contains(\"" + fieldAttribute.name() + "\")) {\n";
                            code += "            obj.set" + name_d + "(resultSet.getObject(\"" + fieldAttribute.name() + "\"," + field.getType().getName() + ".class));\n";
                            code += "        }\n";

                            num++;
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                code += "        " + className + " obj = null;\n";
            }
        } else {
            code += "        " + className + " obj = null;\n";
        }
        code += "        return obj;\n" +
                "    }\n";
        //生成 sql 删除
        code += "   public gzb.entity.SqlTemplate toDeleteSql(Object obj0) throws Exception {\n";
        if (classAttribute != null) {
            code += "       " + aClass.getName() + " obj=(" + aClass.getName() + ")obj0;\n" +
                    "        String sql = \"delete from " + classAttribute.name() + "\";\n" +
                    "        StringBuilder stringBuilder = new StringBuilder();\n" +
                    "        java.util.List<Object> params = new java.util.ArrayList<>();\n";
            for (Field field : fields) {
                if (field.getType() == byte.class || field.getType() == Byte.class
                        || field.getType() == byte[].class || field.getType() == Byte[].class) {
                    continue;
                }
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
                if (field.getType() == byte.class || field.getType() == Byte.class
                        || field.getType() == byte[].class || field.getType() == Byte[].class) {
                    continue;
                }
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
                if (field.getType() == byte.class || field.getType() == Byte.class
                        || field.getType() == byte[].class || field.getType() == Byte[].class) {
                    continue;
                }
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
                if (field.getType() == byte.class || field.getType() == Byte.class
                        || field.getType() == byte[].class || field.getType() == Byte[].class) {
                    continue;
                }
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

                code += "            stringBuilder.append(\"" + c_name + "=?,\");\n" +
                        "            params.add(obj.get" + c_h_d_name + "());\n";
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
        code += "   public gzb.entity.SqlTemplate toSaveSql(Object obj0,gzb.frame.db.DataBase dataBase,boolean reset) throws Exception {\n";
        if (classAttribute != null) {
            code += "       " + aClass.getName() + " obj=(" + aClass.getName() + ")obj0;\n";
            code += "        StringBuilder fields = new StringBuilder();\n" +
                    "        StringBuilder values = new StringBuilder();\n" +
                    "        java.util.List<Object> params = new java.util.ArrayList<>();\n";
            for (Field field : fields) {
                if (field.getType() == byte.class || field.getType() == Byte.class
                        || field.getType() == byte[].class || field.getType() == Byte[].class) {
                    continue;
                }
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
                    //dataBase.getOnlyIdNumber(classAttribute.name(),fieldAttribute.name())
                    if (field.getType() == Integer.class || field.getType() == int.class) {
                        code += "        if (obj.get" + c_h_d_name + "()==null || reset) {\n" +
                                "            obj.set" + c_h_d_name + "(dataBase.getOnlyIdNumber(\"" + classAttribute.name() + "\",\"" + fieldAttribute.name() + "\",reset));\n" +
                                "        }\n";

                    }
                    if (field.getType() == Long.class || field.getType() == long.class) {
                        code += "        if (obj.get" + c_h_d_name + "()==null) {\n" +
                                "            obj.set" + c_h_d_name + "(dataBase.getOnlyIdDistributed());\n" +
                                "        }\n";
                    }
                    if (field.getType() == String.class) {
                        code += "        if (obj.get" + c_h_d_name + "()==null) {\n" +
                                "            obj.set" + c_h_d_name + "(dataBase.getOnlyIdDistributedString());\n" +
                                "        }\n";
                    }

                }

            }

            for (Field field : fields) {
                if (field.getType() == byte.class || field.getType() == Byte.class
                        || field.getType() == byte[].class || field.getType() == Byte[].class) {
                    continue;
                }
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
                if (field.getType() == byte.class || field.getType() == Byte.class
                        || field.getType() == byte[].class || field.getType() == Byte[].class) {
                    continue;
                }
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
                if (field.getType() == byte.class || field.getType() == Byte.class
                        || field.getType() == byte[].class || field.getType() == Byte[].class) {
                    continue;
                }
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
        //System.out.println(code);
        return code;
    }

    public static boolean isBasicTypes(Class<?> aClass) {
        if (aClass == null) {
            return false;
        }
        if (aClass.isArray()) {
            aClass = aClass.getComponentType();
        }

        return aClass == String.class
                || aClass == int.class || aClass == Integer.class
                || aClass == long.class || aClass == Long.class
                || aClass == short.class || aClass == Short.class
                || aClass == byte.class || aClass == Byte.class
                || aClass == boolean.class || aClass == Boolean.class
                || aClass == float.class || aClass == Float.class
                || aClass == double.class || aClass == Double.class
                || aClass == char.class || aClass == Character.class
                || aClass == Date.class
                || aClass == LocalDateTime.class
                || aClass == DateTime.class
                || aClass == Timestamp.class;
    }

    //可维护性堪忧 不过应该不需要维护其实
    public static String gen_call_code_v4(Class<?> aClass, String javaCode) throws Exception {
        Method[] methods = ClassTools.getCombinedMethods(aClass);
        Field[] fields = ClassTools.getCombinedFields(aClass, false);

        String code = "";
        code += "    public Object _gzb_call_x01(" +
                "int _gzb_one_c_id," +
                "java.util.Map<String, Object> _gzb_one_c_mapObject," +
                "gzb.frame.netty.entity.Request _g_p_req," +
                "gzb.frame.netty.entity.Response _g_p_resp," +
                "java.util.Map<String, java.util.List<Object>> _gzb_one_c_requestMap, " +
                "gzb.tools.json.GzbJson _g_p_gzbJson," +
                "gzb.tools.log.Log _g_p_log," +
                "Object[] arrayObject" +
                ") throws Exception {\n" +
                //"long start_0=System.nanoTime();\n"+
                "        Object object_return = null;\n";

//不再需要注入 因为编译时注入了
        for (int i = 0; i < fields.length - 10000000; i++) {
            Resource resource = fields[i].getAnnotation(Resource.class);
            String impl = "";
            if (resource == null) {
                continue;
            }
            impl = resource.value();
            if (impl.length() == 0) {
                impl = fields[i].getType().getName();
            }
            code += "        //如果有类变量 且 类型正确 就加入\n" +
                    "        this." + fields[i].getName() + " = (" + fields[i].getType().getName() + ") _gzb_one_c_mapObject.get(\"" + impl + "\");\n";

        }
        code += "        java.util.List<Object>t_map_list=null;\n";

        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            int met_id = ClassTools.getSingInt(method, aClass);
            String met_Sign = ClassTools.getSing(method, aClass);
            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            PutMapping putMapping = method.getAnnotation(PutMapping.class);
            DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
            RequestMapping requestMappingMethod = method.getAnnotation(RequestMapping.class);
            Transaction transaction = method.getAnnotation(Transaction.class);
            ThreadInterval threadInterval = method.getAnnotation(ThreadInterval.class);
            DecoratorStart decoratorStart = method.getAnnotation(DecoratorStart.class);
            DecoratorEnd decoratorEnd = method.getAnnotation(DecoratorEnd.class);
            Decorator decorator = method.getAnnotation(Decorator.class);

            DataBaseEventSelect dataBaseEventSelect = method.getAnnotation(DataBaseEventSelect.class);
            DataBaseEventSave dataBaseEventSave = method.getAnnotation(DataBaseEventSave.class);
            DataBaseEventDelete dataBaseEventDelete = method.getAnnotation(DataBaseEventDelete.class);
            DataBaseEventUpdate dataBaseEventUpdate = method.getAnnotation(DataBaseEventUpdate.class);

            if (getMapping == null
                    && postMapping == null
                    && putMapping == null
                    && deleteMapping == null
                    && requestMappingMethod == null
                    && transaction == null
                    && threadInterval == null
                    && decoratorStart == null
                    && decoratorEnd == null
                    && decorator == null
                    && dataBaseEventSelect == null
                    && dataBaseEventSave == null
                    && dataBaseEventDelete == null
                    && dataBaseEventUpdate == null
            ) {
                continue;
            }
            Class<?>[] types = method.getParameterTypes();
            String[] names = ClassTools.getParameterNamesByAsm(method, types).toArray(new String[0]);

            if (names.length != types.length) {
                names = ClassTools.getParameterNames(javaCode, method.getName(), types).toArray(new String[0]);
            }
            if (names.length != types.length) {
                Log.log.w("获取参数失败", method);
                continue;
            }
            code += "        //方法ID匹配\n" +
                    "        if (_gzb_one_c_id == " + met_id + ") {\n";

            //code+="long time_0=System.nanoTime();\n";
            //固定对象 避免传递 直接生成注入代码
            for (int i1 = 0; i1 < names.length; i1++) {
                String typeName = ClassTools.toName(types[i1]);
                if (typeName.startsWith(Request.class.getName())) {
                    code += "        " + typeName + " _c_u_" + names[i1] + " = _g_p_req;\n";
                } else if (typeName.startsWith(Response.class.getName())) {
                    code += "        " + typeName + " _c_u_" + names[i1] + " = _g_p_resp;\n";
                } else if (typeName.startsWith(GzbJson.class.getName())) {
                    code += "        " + typeName + " _c_u_" + names[i1] + " = _g_p_gzbJson;\n";
                } else if (typeName.startsWith(Log.class.getName())) {
                    code += "        " + typeName + " _c_u_" + names[i1] + " = _g_p_log;\n";
                } else if (typeName.startsWith("java.util.Map")) {
                    code += "        " + typeName + " _c_u_" + names[i1] + " = _gzb_one_c_requestMap;\n";
                } else {
                    code += "        " + typeName + " _c_u_" + names[i1] + " = null;\n";
                }
            }
            //注入 传入 本次调用 私有对象
            code += "            for (Object object : arrayObject) {\n" +
                    "                if (object == null) {\n" +
                    "                    continue;\n" +
                    "                }\n";

            for (int i1 = 0; i1 < names.length; i1++) {
                String typeName = ClassTools.toName(types[i1]);
                if (typeName.startsWith("gzb.frame.netty.entity.Request")) {

                } else if (typeName.startsWith("gzb.frame.netty.entity.Response")) {

                } else if (typeName.startsWith("java.util.Map")) {

                } else {
                    boolean res01 = !isBasicTypes(types[i1]);
                    if (res01) {
                        code += "                if (_c_u_" + names[i1] + "==null && " + types[i1].getCanonicalName() + ".class.isAssignableFrom(object.getClass())) {\n" +
                                "                    _c_u_" + names[i1] + "=(" + types[i1].getCanonicalName() + ") object;\n" +
                                "                } \n";
                    }

                }

            }
            code += "            }\n";
            //参数注入代码生成
            for (int i1 = 0; i1 < types.length; i1++) {
                String typeName = ClassTools.toName(types[i1]);
                if (typeName.startsWith("gzb.frame.netty.entity.Request")) {
                } else if (typeName.startsWith("gzb.frame.netty.entity.Response")) {
                } else if (typeName.startsWith("java.util.Map")) {
                } else {
                    boolean res01 = !isBasicTypes(types[i1]);
                    code += "                if (_c_u_" + names[i1] + "==null) {\n";

                    //System.out.println(aClass.getName() + " " + res01 + " " + types[i1].getName());
                    if (res01) {
                        //尝试注入 service 等注解对象
                        code += "                //复杂对象\n" +
                                "                   _c_u_" + names[i1] + " = (" + types[i1].getCanonicalName() + ")_gzb_one_c_mapObject.get(\"" + types[i1].getName() + "\");\n";

                        //上一步注入失败的话 尝试注入 请求参数
                        code += "                if (_c_u_" + names[i1] + "==null) {\n";
                        code += "                t_map_list = _gzb_one_c_requestMap.get(\"" + names[i1] + "\");";
                        if (types[i1].isArray()) {
                            code += "                //如果是数组 则这样输出\n" +
                                    "                if (t_map_list != null && t_map_list.size() > 0) {\n" +
                                    "                    _c_u_" + names[i1] + "=new " + types[i1].getComponentType().getName() + "[t_map_list.size()];\n" +
                                    "                    for (int i = 0; i < t_map_list.size(); i++) {\n" +
                                    "                        _c_u_" + names[i1] + "[i]=(" + types[i1].getComponentType().getName() + ")t_map_list.get(i);\n" +
                                    "                    }\n" +
                                    "                }\n";
                        } else {
                            code += "                //如果不是数组 则这样输出\n" +
                                    "                if (t_map_list != null && t_map_list.size() > 0 && t_map_list.get(0)!=null){\n" +
                                    "                        _c_u_" + names[i1] + " = (" + types[i1].getName() + ")t_map_list.get(0);\n" +
                                    "                }\n";
                        }
                        code += "                }\n";
                        //尝试 把请求参数注入到 实体类 如果依然无法生成对应对象 那么 视为 null
                        code += "                       if (_c_u_" + names[i1] + "==null) {\n";
                        if (types[i1].isArray()) {
                            code += "                           Object[] objects = gzb.frame.factory.ClassTools.loadObject(" + (
                                    types[i1].getComponentType() == null ? types[i1].getName() : types[i1].getComponentType().getName()
                            ) + ".class, _gzb_one_c_requestMap);\n" +
                                    "                               if(objects!=null && objects.length>0){\n" +
                                    "                                   //如果是数组 则这样输出\n" +
                                    "                                   _c_u_" + names[i1] + " =(" + typeName + ") objects;\n" +
                                    "                               }\n";
                        } else {
                            code += "                           Object[] objects = gzb.frame.factory.ClassTools.loadObject(" + (
                                    types[i1].getComponentType() == null ? types[i1].getName() : types[i1].getComponentType().getName()
                            ) + ".class, _gzb_one_c_requestMap);\n" +
                                    "                               if(objects!=null && objects.length>0){\n" +
                                    "                                   //如果不是数组 则这样输出\n" +
                                    "                                   _c_u_" + names[i1] + " =(" + typeName + ") objects[0];\n" +
                                    "                               }\n";
                        }
                        code += "                       }\n";

                    } else {
                        code += "                //简单对象\n" +
                                "                t_map_list = _gzb_one_c_requestMap.get(\"" + names[i1] + "\");";
                        if (types[i1].isArray()) {
                            String funName = "parse";
                            if (types[i1] == String[].class) {
                                funName = "valueOf";
                            } else if (types[i1] == LocalDateTime[].class) {
                                funName = "valueOf";
                            } else if (types[i1] == Timestamp[].class) {
                                funName = "valueOf";
                            } else if (types[i1] == Date[].class) {
                                funName = "valueOf";
                            } else {
                                funName += ClassTools.toName(types[i1], true);
                                funName = funName.replaceAll("java.lang.", "");
                                funName = funName.replaceAll("Integer", "Int");
                            }

                            code += "            //如果是数组 则这样输出\n" +
                                    "            if (t_map_list != null && t_map_list.size() > 0) {\n" +
                                    "                _c_u_" + names[i1] + " = new " + types[i1].getComponentType().getSimpleName() + "[t_map_list.size()];\n" +
                                    "                for (int i = 0; i < t_map_list.size(); i++) {\n" +
                                    "                    if(t_map_list.get(i)==null){\n" +
                                    "                       continue;\n" +
                                    "                    }\n" +
                                    "                    _c_u_" + names[i1] + "[i] = ";
                            if (types[i1] == LocalDateTime[].class) {
                                code += "new gzb.tools.DateTime(t_map_list.get(i).toString()).toLocalDateTime();\n";
                            } else if (types[i1] == Date[].class) {
                                code += "new gzb.tools.DateTime(t_map_list.get(i).toString()).toDate();\n";
                            } else if (types[i1] == Timestamp[].class) {
                                code += "new gzb.tools.DateTime(t_map_list.get(i).toString()).toTimestamp();\n";
                            } else if (types[i1] == DateTime[].class) {
                                code += "new gzb.tools.DateTime(t_map_list.get(i).toString());\n";
                            } else {
                                code += ClassTools.toName(types[i1].getComponentType()) + "." + funName +
                                        "(" + ("t_map_list.get(i) instanceof String ? (String) t_map_list.get(i) : t_map_list.get(i).toString()") + ");\n";
                            }


                            code += "                }\n" +
                                    "            }\n";
                        } else {
                            String funName = "parse";
                            if (types[i1].getSimpleName().contains("String")) {
                                funName = "valueOf";
                            } else {
                                funName += ClassTools.toName(types[i1], true);
                                funName = funName.replaceAll("java.lang.", "");
                                funName = funName.replaceAll("Integer", "Int");
                            }
                            code += "            //如果不是数组 则这样输出\n" +
                                    "            if (t_map_list != null && t_map_list.size() > 0 && t_map_list.get(0)!=null){\n" +
                                    "                _c_u_" + names[i1] + " = ";
                            if (types[i1] == LocalDateTime.class) {
                                code += "new gzb.tools.DateTime(t_map_list.get(0).toString()).toLocalDateTime();\n";
                            } else if (types[i1] == Date.class) {
                                code += "new gzb.tools.DateTime(t_map_list.get(0).toString()).toDate();\n";
                            } else if (types[i1] == Timestamp.class) {
                                code += "new gzb.tools.DateTime(t_map_list.get(0).toString()).toTimestamp();\n";
                            } else if (types[i1] == DateTime.class) {
                                code += "new gzb.tools.DateTime(t_map_list.get(0).toString());\n";
                            } else {
                                code += ClassTools.toName(types[i1]) + "." + funName +
                                        "(" + ("t_map_list.get(0) instanceof String ? (String) t_map_list.get(0) : t_map_list.get(0).toString()") + ");\n";
                            }


                            code += "            }\n";

                            //这里可能存在特殊情况 基本数据类型 如果存在基本数据类型 且为null 直接提示某个参数不能为空
                            if (types[i1].isPrimitive()) {
                                code += "                                if (_c_u_" + names[i1] + " == null){\n" +
                                        "                                    gzb.tools.log.Log.log.d(\"参数:" + names[i1] + ",类型为：" + types[i1].getName() + ",不允许为 NULL\");\n" +
                                        "                                    return \"{\\\"\"+gzb.tools.Config.stateName+\"\\\":\\\"\"+gzb.tools.Config.failVal+\"\\\",\\\"\"+gzb.tools.Config.messageName+\"\\\":\\\"有必填参数不允许为空,请检查日志\\\"}\";\n" +
                                        "                                }";

                            }
                        }

                    }
                    code += "                }\n";
                }
            }
            //code+="long time_1=System.nanoTime();\n";
            //检查是否开启事务 生成对应代码
            if (transaction == null) {
                //不开启事务+aClass.getName()

                code += "            //不开事物直接生成这个\n" +
                        //"System.out.println(\"事务没开启  "+aClass.getName()+"\");\n" +
                        "            " + (method.getReturnType() == void.class ? "" : "object_return =") + " " + method.getName() + "(";
                for (int i1 = 0; i1 < names.length; i1++) {
                    code += "_c_u_" + names[i1];
                    if (i1 < names.length - 1) {
                        code += ",";
                    }
                }
                code += ");\n";
            } else if (transaction.simulate()) {
                //开启模拟事务 仅保证 一起成功或一起失败 不保证原子性 但是性能更好
                code += "            //如果 开启模拟事务 则添加事务处理  不存在不生成\n" +
                        "            java.util.Map<String, java.sql.Connection> mapConnection = new java.util.HashMap<>();\n" +
                        "            try {\n";
                for (int i1 = 0; i1 < types.length; i1++) {
                    String typeName = ClassTools.toName(types[i1]);
                    if (!types[i1].isArray() && BaseDao.class.isAssignableFrom(types[i1])) {
                        code += "                //每个dao\n" +
                                "           if(_c_u_" + names[i1] + "!=null && _c_u_" + names[i1] + ".getDataBase().readTransactionState()==null){\n" +
                                "                    _c_u_" + names[i1] + ".getDataBase().openTransaction(true);\n" +
                                "           }\n";
                    }
                }
                for (int i1 = 0; i1 < fields.length; i1++) {
                    if (!fields[i1].getType().isArray() && BaseDao.class.isAssignableFrom(fields[i1].getType())) {
                        code += "                //每个dao\n" +
                                "           if(this." + fields[i1].getName() + "!=null && this." + fields[i1].getName() + ".getDataBase().readTransactionState()==null){\n" +
                                "                    this." + fields[i1].getName() + ".getDataBase().openTransaction(true);\n" +
                                "           }\n";
                    }
                }

                code += "                " + (method.getReturnType() == void.class ? "" : "object_return =") + " " + method.getName() + "(";
                for (int i1 = 0; i1 < names.length; i1++) {
                    code += "_c_u_" + names[i1];
                    if (i1 < names.length - 1) {
                        code += ",";
                    }
                }
                code += ");\n";
                for (int i1 = 0; i1 < types.length; i1++) {
                    if (!types[i1].isArray() && BaseDao.class.isAssignableFrom(types[i1])) {
                        code += "                //每个dao\n" +
                                "           if(_c_u_" + names[i1] + "!=null){\n" +
                                "                _c_u_" + names[i1] + ".getDataBase().commit();\n" +
                                "           }\n";
                    }
                }
                for (int i1 = 0; i1 < fields.length; i1++) {
                    if (!fields[i1].getType().isArray() && BaseDao.class.isAssignableFrom(fields[i1].getType())) {
                        code += "                //每个dao\n" +
                                "           if(this." + fields[i1].getName() + "!=null){\n" +
                                "                this." + fields[i1].getName() + ".getDataBase().commit();\n" +
                                "           }\n";
                    }
                }
                code += "            } catch (Exception e) {\n";

                for (int i1 = 0; i1 < types.length; i1++) {
                    if (!types[i1].isArray() && BaseDao.class.isAssignableFrom(types[i1])) {
                        code += "                //每个dao\n" +
                                "           if(_c_u_" + names[i1] + "!=null){\n" +
                                "                try {_c_u_" + names[i1] + ".getDataBase().rollback();}catch (Exception e0){gzb.tools.log.Log.log.e(\"事物出现预料外错误\",e0);}\n" +
                                "           }\n";
                    }
                }
                for (int i1 = 0; i1 < fields.length; i1++) {
                    if (!fields[i1].getType().isArray() && BaseDao.class.isAssignableFrom(fields[i1].getType())) {
                        code += "                //每个dao\n" +
                                "           if(this." + fields[i1].getName() + "!=null){\n" +
                                "                try {this." + fields[i1].getName() + ".getDataBase().rollback();}catch (Exception e0){gzb.tools.log.Log.log.e(\"事物出现预料外错误\",e0);}\n" +
                                "           }\n";
                    }
                }

                code += "                throw e;\n" +
                        "            } finally {\n";
                for (int i1 = 0; i1 < types.length; i1++) {
                    if (!types[i1].isArray() && BaseDao.class.isAssignableFrom(types[i1])) {
                        code += "                //每个dao\n" +
                                "           if(_c_u_" + names[i1] + "!=null){\n" +
                                "                try {_c_u_" + names[i1] + ".getDataBase().endTransaction();}catch (Exception e0){gzb.tools.log.Log.log.e(\"事物 回滚 出现预料外错误\",e0);}\n" +
                                "           }\n";
                    }
                }
                for (int i1 = 0; i1 < fields.length; i1++) {
                    if (!fields[i1].getType().isArray() && BaseDao.class.isAssignableFrom(fields[i1].getType())) {
                        code += "                //每个dao\n" +
                                "           if(this." + fields[i1].getName() + "!=null){\n" +
                                "                try {this." + fields[i1].getName() + ".getDataBase().endTransaction();}catch (Exception e0){gzb.tools.log.Log.log.e(\"事物 关闭连接 出现预料外错误\",e0);}\n" +
                                "           }\n";
                    }
                }
                code += "            }\n";
            } else {
                //开启真实事务
                code += "            //如果开启事物 则添加事务处理  不存在不生成\n" +
                        "            try {\n";
                for (int i1 = 0; i1 < types.length; i1++) {
                    String typeName = ClassTools.toName(types[i1]);
                    if (!types[i1].isArray() && BaseDao.class.isAssignableFrom(types[i1])) {
                        code += "                //每个dao\n" +
                                "           if(_c_u_" + names[i1] + "!=null && _c_u_" + names[i1] + ".getDataBase().readTransactionState()==null){\n" +
                                "                    _c_u_" + names[i1] + ".getDataBase().openTransaction(false);\n" +
                                "           }\n";
                    }
                }
                for (int i1 = 0; i1 < fields.length; i1++) {
                    if (!fields[i1].getType().isArray() && BaseDao.class.isAssignableFrom(fields[i1].getType())) {
                        code += "                //每个dao\n" +
                                "           if(this." + fields[i1].getName() + "!=null && this." + fields[i1].getName() + ".getDataBase().readTransactionState()==null){\n" +
                                "                    this." + fields[i1].getName() + ".getDataBase().openTransaction(false);\n" +
                                "           }\n";
                    }
                }
                code +=
                        "                " + (method.getReturnType() == void.class ? "" : "object_return =") + " " + method.getName() + "(";
                for (int i1 = 0; i1 < names.length; i1++) {
                    code += "_c_u_" + names[i1];
                    if (i1 < names.length - 1) {
                        code += ",";
                    }
                }
                code += ");\n";

                for (int i1 = 0; i1 < types.length; i1++) {
                    if (!types[i1].isArray() && BaseDao.class.isAssignableFrom(types[i1])) {
                        code += "                //每个dao\n" +
                                "           if(_c_u_" + names[i1] + "!=null){\n" +
                                "                _c_u_" + names[i1] + ".getDataBase().commit();\n" +
                                "           }\n";
                    }
                }
                for (int i1 = 0; i1 < fields.length; i1++) {
                    if (!fields[i1].getType().isArray() && BaseDao.class.isAssignableFrom(fields[i1].getType())) {
                        code += "                //每个dao\n" +
                                "           if(this." + fields[i1].getName() + "!=null){\n" +
                                "                this." + fields[i1].getName() + ".getDataBase().commit();\n" +
                                "           }\n";
                    }
                }
                code += "            } catch (Exception e) {\n";

                for (int i1 = 0; i1 < types.length; i1++) {
                    if (!types[i1].isArray() && BaseDao.class.isAssignableFrom(types[i1])) {
                        code += "                //每个dao\n" +
                                "           if(_c_u_" + names[i1] + "!=null){\n" +
                                "                try {_c_u_" + names[i1] + ".getDataBase().rollback();}catch (Exception e0){gzb.tools.log.Log.log.e(\"事物出现预料外错误\",e0);}\n" +
                                "           }\n";
                    }
                }
                for (int i1 = 0; i1 < fields.length; i1++) {
                    if (!fields[i1].getType().isArray() && BaseDao.class.isAssignableFrom(fields[i1].getType())) {
                        code += "                //每个dao\n" +
                                "           if(this." + fields[i1].getName() + "!=null){\n" +
                                "                try {this." + fields[i1].getName() + ".getDataBase().rollback();}catch (Exception e0){gzb.tools.log.Log.log.e(\"事物出现预料外错误\",e0);}\n" +
                                "           }\n";
                    }
                }

                code += "                throw e;\n" +
                        "            } finally {\n";
                for (int i1 = 0; i1 < types.length; i1++) {
                    if (!types[i1].isArray() && BaseDao.class.isAssignableFrom(types[i1])) {
                        code += "                //每个dao\n" +
                                "           if(_c_u_" + names[i1] + "!=null){\n" +
                                "                try {_c_u_" + names[i1] + ".getDataBase().endTransaction();}catch (Exception e0){gzb.tools.log.Log.log.e(\"事物 回滚 出现预料外错误\",e0);}\n" +
                                "           }\n";
                    }
                }
                for (int i1 = 0; i1 < fields.length; i1++) {
                    if (!fields[i1].getType().isArray() && BaseDao.class.isAssignableFrom(fields[i1].getType())) {
                        code += "                //每个dao\n" +
                                "           if(this." + fields[i1].getName() + "!=null){\n" +
                                "                try {this." + fields[i1].getName() + ".getDataBase().endTransaction();}catch (Exception e0){gzb.tools.log.Log.log.e(\"事物 关闭连接 出现预料外错误\",e0);}\n" +
                                "           }\n";
                    }
                }
                code += "            }\n";
            }

            //code+="long time_2=System.nanoTime();\n" +"gzb.tools.log.Log.log.t(\"组装变量耗时\",time_1-time_0,\"调用函数耗时\",time_2-time_1,\"总耗时\",time_2-start_0);\n";

            code += "        return object_return;\n" +
                    "       }\n";
        }

        code += "        gzb.tools.log.Log.log.t(\"miss v4\",_gzb_one_c_id,this);\n" +
                "        return null;\n" +
                "    }";
        //自己调试用
        //System.out.println(code);
        return code;
    }


    public static String gen_call_code_v1(Class clazz, String code) throws Exception {
        String method_call_code = "";
        String class_method_type_code = "        java.util.List<Class<?>[]> _g_m_1_t=new java.util.ArrayList<>();\n{\n";
        String class_method_name_code = "        java.util.List<String[]> _g_m_1_n=new java.util.ArrayList<>();\n{\n";
        //String class_method_type_code = "";
        //String class_method_name_code = "";

        String class_field_type_code = "";
        String class_field_name_code = "";
        String class_field_impl_code = "";
        String class_field_put_code = "";

        Method[] methods = ClassTools.getCombinedMethods(clazz);
        int met_int = 0;
        for (int j = 0; j < methods.length; j++) {
            if (!Modifier.isPublic(methods[j].getModifiers())) {
                continue;
            }
            Method method = methods[j];

            String methodName = method.getName();
            Class[] parameterTypes = method.getParameterTypes();
            if (method.getAnnotation(GetMapping.class) == null
                    && method.getAnnotation(PostMapping.class) == null
                    && method.getAnnotation(PutMapping.class) == null
                    && method.getAnnotation(DeleteMapping.class) == null
                    && method.getAnnotation(ThreadInterval.class) == null
                    && method.getAnnotation(DecoratorStart.class) == null
                    && method.getAnnotation(DecoratorEnd.class) == null
                    && method.getAnnotation(RequestMapping.class) == null
            ) {
                continue;
            }
            String[] parameterTypeNames = null;
            parameterTypeNames = ClassTools.getParameterNamesByAsm(method, parameterTypes).toArray(new String[]{});
            if (parameterTypeNames.length != parameterTypes.length) {
                parameterTypeNames = ClassTools.getParameterNames(code, methodName, parameterTypes).toArray(new String[]{});
            }
            if (parameterTypeNames.length != parameterTypes.length) {
                log.w(clazz, methodName, "参数名获取失败,请确保源码中存在方法签名");
                continue;
            }
            //log.d("gen_code0",methodName,parameterTypeNames,parameterTypes);
            method_call_code +=
                    //"        if ( _gzb_x001_methodName.equals(\"" + methodSign + "\")) {\n" +
                    "        //" + ClassTools.getSing(method, clazz) + "\n" +
                            "        if ( _gzb_x001_methodName == " + ClassTools.getSingInt(method, clazz) + ") {\n" +
                            "            Object object=null;\n" +
                            "            java.util.List<gzb.frame.db.BaseDao>listBaseDao=null;\n" +
                            "            try {\n";

            int num = 0;

            class_method_type_code += "_g_m_1_t.add(new Class[]{";
            class_method_name_code += "_g_m_1_n.add(new String[]{";
            String met_par = "";
            for (int i = 0; i < parameterTypes.length; i++) {
                class_method_type_code += parameterTypes[i].getSimpleName() + ".class";
                class_method_name_code += "\"" + parameterTypeNames[i] + "\"";
                met_par += "(" + parameterTypes[i].getTypeName() + ")list.get(" + num + ")";
                if (num == 0) {
                    method_call_code += "                java.util.List<Object> list= " +
                            "gzb.frame.factory.ClassTools.getMethodParameterList(" +
                            "_gzb_x001_requestMap," +
                            "_gzb_x001_mapObject,_gzb_x001_arrayObject," +
                            "_g_m_1_t.get(" + met_int + "),_g_m_1_n.get(" + met_int + "),null);\n" +
                            "                if(list==null){return gzb.tools.json.GzbJsonImpl.json.fail(\"Incorrect parameters\");}\n";
                    method_call_code += "                listBaseDao=gzb.frame.factory.ClassTools.transactionOpen(_gzb_x001_openTransaction,list);\n";
                }
                //method_call_code += "                " + parameterTypes[i].getTypeName() + " _c_p_" + num + "=(" + parameterTypes[i].getTypeName() + ")list.get(" + num + ");\n";
                num++;
                if (i < parameterTypes.length - 1) {
                    class_method_type_code += ",";
                    class_method_name_code += ",";
                    met_par += ",";
                }
            }
            met_int++;


            if (method.getReturnType().getName().equals("void")) {
                method_call_code += "                " + method.getName() + "(";
            } else {
                method_call_code += "                object = " + method.getName() + "(";
            }
            method_call_code += met_par + ");\n";


            method_call_code += "                gzb.frame.factory.ClassTools.transactionCommit(listBaseDao);\n" +
                    "            }catch (Exception e){\n" +
                    "                gzb.frame.factory.ClassTools.transactionRollback(listBaseDao);\n" +
                    "                throw e;\n" +
                    "            }finally {\n" +
                    "                gzb.frame.factory.ClassTools.transactionEndTransaction(listBaseDao);\n" +
                    "            }\n" +
                    "            return object;\n" +
                    "        }\n";
            class_method_type_code += "});\n";
            class_method_name_code += "});\n";
        }
        class_method_type_code += "\n}\n";
        class_method_name_code += "\n}\n";
        int num = 0;

        Field[] fields = ClassTools.getCombinedFieldsNotCache(clazz);

        class_field_type_code += "Class<?>[]_gzb_x001_field_types=new Class[]{";
        class_field_name_code += "String[]_gzb_x001_field_names=new String[]{";
        class_field_impl_code += "String[]_gzb_x001_field_impl=new String[]{";
        for (int i = 0; i < fields.length; i++) {
            Resource resource = fields[i].getAnnotation(Resource.class);
            if (resource == null) {
                continue;
            }
            class_field_type_code += fields[i].getType().getCanonicalName() + ".class";
            class_field_name_code += "\"" + fields[i].getName() + "\"";

            class_field_impl_code += "\"" + (resource.value().isEmpty() ? fields[i].getType().getName() : resource.value()) + "\"";
            class_field_put_code += "        this." + fields[i].getName() + "=(" + fields[i].getType().getCanonicalName() + ")list0.get(" + num + ");\n";


            num++;
            if (i < fields.length - 1) {
                class_field_type_code += ",";
                class_field_name_code += ",";
                class_field_impl_code += ",";
            }
        }
        if (class_field_put_code.length() > 0) {
            class_field_put_code = "        java.util.List<Object> list0= gzb.frame.factory.ClassTools.getMethodParameterList" +
                    "(_gzb_x001_requestMap,_gzb_x001_mapObject,_gzb_x001_arrayObject," +
                    "_gzb_x001_field_types," +
                    "_gzb_x001_field_names," +
                    "_gzb_x001_field_impl" +
                    ");\n" + class_field_put_code;
        }

        if (class_field_type_code.endsWith(",")) {
            class_field_type_code = class_field_type_code.substring(0, class_field_type_code.length() - 1);
        }
        if (class_field_name_code.endsWith(",")) {
            class_field_name_code = class_field_name_code.substring(0, class_field_name_code.length() - 1);
        }
        if (class_field_impl_code.endsWith(",")) {
            class_field_impl_code = class_field_impl_code.substring(0, class_field_impl_code.length() - 1);
        }

        class_field_type_code += "};\n";
        class_field_name_code += "};\n";
        class_field_impl_code += "};\n";
        if (class_field_type_code.contains("{}")) {
            class_field_type_code = "";
        }
        if (class_field_name_code.contains("{}")) {
            class_field_name_code = "";
        }
        if (class_field_impl_code.contains("{}")) {
            class_field_impl_code = "";
        }


        String method_fun_code = "///  ############## 生成代码开始 ##############\n"
                + class_method_type_code
                + class_method_name_code
                + class_field_type_code
                + class_field_name_code
                + class_field_impl_code
                +
                "    @Override\n" +
                //"    public Object call(String _gzb_x001_methodName,\n" +
                "    public Object _gzb_call_x01(int _gzb_x001_methodName,\n" +
                "                       java.util.Map<String, Object> _gzb_x001_mapObject,\n" +
                "                       gzb.frame.netty.entity.Request _g_p_req,\n" +
                "                       gzb.frame.netty.entity.Response _g_p_resp,\n" +
                "                       java.util.Map<String, java.util.List<Object>> _gzb_x001_requestMap,\n" +
                "                       Object[] _gzb_x001_arrayObject," +
                "                       boolean _gzb_x001_openTransaction" +
                ") throws Exception {\n" +
                //"        System.out.println(_gzb_x001_methodName);\n" +
                class_field_put_code +
                method_call_code +
                "        return null;\n" +
                "    }\n" +
                "///  ############## 生成代码结束 ##############\n";


        return method_fun_code;
    }


    public static String gen_call_code_v0(Class clazz, String code) throws Exception {
        String method_call_code = "";
        String class_method_type_code = "        java.util.List<Class<?>[]> _g_m_1_t=new java.util.ArrayList<>();\n{\n";
        String class_method_name_code = "        java.util.List<String[]> _g_m_1_n=new java.util.ArrayList<>();\n{\n";
        //String class_method_type_code = "";
        //String class_method_name_code = "";

        String class_field_type_code = "";
        String class_field_name_code = "";
        String class_field_impl_code = "";
        String class_field_put_code = "";

        Method[] methods = ClassTools.getCombinedMethods(clazz);
        int met_int = 0;
        for (int j = 0; j < methods.length; j++) {
            if (!Modifier.isPublic(methods[j].getModifiers())) {
                continue;
            }
            Method method = methods[j];

            String methodName = method.getName();
            Class[] parameterTypes = method.getParameterTypes();
            if (method.getAnnotation(GetMapping.class) == null
                    && method.getAnnotation(PostMapping.class) == null
                    && method.getAnnotation(PutMapping.class) == null
                    && method.getAnnotation(DeleteMapping.class) == null
                    && method.getAnnotation(ThreadInterval.class) == null
                    && method.getAnnotation(DecoratorStart.class) == null
                    && method.getAnnotation(DecoratorEnd.class) == null
                    && method.getAnnotation(RequestMapping.class) == null
            ) {
                continue;
            }
            String[] parameterTypeNames = null;
            parameterTypeNames = ClassTools.getParameterNamesByAsm(method, parameterTypes).toArray(new String[]{});
            if (parameterTypeNames.length != parameterTypes.length) {
                parameterTypeNames = ClassTools.getParameterNames(code, methodName, parameterTypes).toArray(new String[]{});
            }
            if (parameterTypeNames.length != parameterTypes.length) {
                log.w(clazz, methodName, "参数名获取失败,请确保源码中存在方法签名");
                continue;
            }
            //log.d("gen_code0",methodName,parameterTypeNames,parameterTypes);
            method_call_code +=
                    //"        if ( _gzb_x001_methodName.equals(\"" + methodSign + "\")) {\n" +
                    "        //" + ClassTools.getSing(method, clazz) + "\n" +
                            "        if ( _gzb_x001_methodName == " + ClassTools.getSingInt(method, clazz) + ") {\n" +
                            "            Object object=null;\n" +
                            "            java.util.List<gzb.frame.db.BaseDao>listBaseDao=null;\n" +
                            "            try {\n";

            int num = 0;

            class_method_type_code += "_g_m_1_t.add(new Class[]{";
            class_method_name_code += "_g_m_1_n.add(new String[]{";
            String met_par = "";
            for (int i = 0; i < parameterTypes.length; i++) {
                class_method_type_code += parameterTypes[i].getSimpleName() + ".class";
                class_method_name_code += "\"" + parameterTypeNames[i] + "\"";
                met_par += "(" + parameterTypes[i].getTypeName() + ")list.get(" + num + ")";
                if (num == 0) {
                    method_call_code += "                java.util.List<Object> list= " +
                            "gzb.frame.factory.ClassTools.getMethodParameterList(" +
                            "_gzb_x001_requestMap," +
                            "_gzb_x001_mapObject,_gzb_x001_arrayObject," +
                            "_g_m_1_t.get(" + met_int + "),_g_m_1_n.get(" + met_int + "),null);\n" +
                            "                if(list==null){return gzb.tools.json.GzbJsonImpl.json.fail(\"Incorrect parameters\");}\n";
                    method_call_code += "                listBaseDao=gzb.frame.factory.ClassTools.transactionOpen(_gzb_x001_openTransaction,list);\n";
                }
                //method_call_code += "                " + parameterTypes[i].getTypeName() + " _c_p_" + num + "=(" + parameterTypes[i].getTypeName() + ")list.get(" + num + ");\n";
                num++;
                if (i < parameterTypes.length - 1) {
                    class_method_type_code += ",";
                    class_method_name_code += ",";
                    met_par += ",";
                }
            }
            met_int++;


            if (method.getReturnType().getName().equals("void")) {
                method_call_code += "                " + method.getName() + "(";
            } else {
                method_call_code += "                object = " + method.getName() + "(";
            }
            method_call_code += met_par + ");\n";


            method_call_code += "                gzb.frame.factory.ClassTools.transactionCommit(listBaseDao);\n" +
                    "            }catch (Exception e){\n" +
                    "                gzb.frame.factory.ClassTools.transactionRollback(listBaseDao);\n" +
                    "                throw e;\n" +
                    "            }finally {\n" +
                    "                gzb.frame.factory.ClassTools.transactionEndTransaction(listBaseDao);\n" +
                    "            }\n" +
                    "            return object;\n" +
                    "        }\n";
            class_method_type_code += "});\n";
            class_method_name_code += "});\n";
        }
        class_method_type_code += "\n}\n";
        class_method_name_code += "\n}\n";
        int num = 0;

        Field[] fields = ClassTools.getCombinedFieldsNotCache(clazz);

        class_field_type_code += "Class<?>[]_gzb_x001_field_types=new Class[]{";
        class_field_name_code += "String[]_gzb_x001_field_names=new String[]{";
        class_field_impl_code += "String[]_gzb_x001_field_impl=new String[]{";
        for (int i = 0; i < fields.length; i++) {
            Resource resource = fields[i].getAnnotation(Resource.class);
            if (resource == null) {
                continue;
            }
            class_field_type_code += fields[i].getType().getCanonicalName() + ".class";
            class_field_name_code += "\"" + fields[i].getName() + "\"";

            class_field_impl_code += "\"" + (resource.value().isEmpty() ? fields[i].getType().getName() : resource.value()) + "\"";
            class_field_put_code += "        this." + fields[i].getName() + "=(" + fields[i].getType().getCanonicalName() + ")list0.get(" + num + ");\n";


            num++;
            if (i < fields.length - 1) {
                class_field_type_code += ",";
                class_field_name_code += ",";
                class_field_impl_code += ",";
            }
        }
        if (class_field_put_code.length() > 0) {
            class_field_put_code = "        java.util.List<Object> list0= gzb.frame.factory.ClassTools.getMethodParameterList" +
                    "(_gzb_x001_requestMap,_gzb_x001_mapObject,_gzb_x001_arrayObject," +
                    "_gzb_x001_field_types," +
                    "_gzb_x001_field_names," +
                    "_gzb_x001_field_impl" +
                    ");\n" + class_field_put_code;
        }

        if (class_field_type_code.endsWith(",")) {
            class_field_type_code = class_field_type_code.substring(0, class_field_type_code.length() - 1);
        }
        if (class_field_name_code.endsWith(",")) {
            class_field_name_code = class_field_name_code.substring(0, class_field_name_code.length() - 1);
        }
        if (class_field_impl_code.endsWith(",")) {
            class_field_impl_code = class_field_impl_code.substring(0, class_field_impl_code.length() - 1);
        }

        class_field_type_code += "};\n";
        class_field_name_code += "};\n";
        class_field_impl_code += "};\n";
        if (class_field_type_code.contains("{}")) {
            class_field_type_code = "";
        }
        if (class_field_name_code.contains("{}")) {
            class_field_name_code = "";
        }
        if (class_field_impl_code.contains("{}")) {
            class_field_impl_code = "";
        }


        String method_fun_code = "///  ############## 生成代码开始 ##############\n"
                + class_method_type_code
                + class_method_name_code
                + class_field_type_code
                + class_field_name_code
                + class_field_impl_code
                +
                "    @Override\n" +
                //"    public Object call(String _gzb_x001_methodName,\n" +
                "    public Object _gzb_call_x01(int _gzb_x001_methodName,\n" +
                "                       java.util.Map<String, java.util.List<Object>> _gzb_x001_requestMap,\n" +
                "                       java.util.Map<String, Object> _gzb_x001_mapObject,\n" +
                "                       Object[] _gzb_x001_arrayObject,boolean _gzb_x001_openTransaction) throws Exception {\n" +
                //"        System.out.println(_gzb_x001_methodName);\n" +
                class_field_put_code +
                method_call_code +
                "        return null;\n" +
                "    }\n" +
                "///  ############## 生成代码结束 ##############\n";

        return method_fun_code;
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

    public static Map<String, Map<String, Integer>> map_getSingInt = new HashMap<>();

    public static void saveSingAll(String filePath) {
        String all="";
        for (Map.Entry<String, Map<String, Integer>> stringMapEntry : map_getSingInt.entrySet()) {
            for (Map.Entry<String, Integer> stringIntegerEntry : stringMapEntry.getValue().entrySet()) {
                all+=stringMapEntry.getKey()+"###"+stringIntegerEntry.getKey()+"="+stringIntegerEntry.getValue()+"\n";
            }

        }
        FileTools.save(new File(filePath),all);
    }
    public static void readSingAll(String filePath) {
        String[]arr1=FileTools.readArray(new File(filePath));
        if (arr1==null) {
            return;
        }
        for (String string : arr1) {
            if (string==null||string.length()<1) {
                continue;
            }
            String[] arr2=string.split("=");
            if (arr2.length!=2) {
                continue;
            }
            String[] arr3=arr2[0].split("###");
            if (arr3.length!=2) {
                continue;
            }
            Map<String, Integer> map0=map_getSingInt.get(arr3[0]);
            if (map0==null) {
                map0=new HashMap<>();
                map_getSingInt.put(arr3[0],map0);
            }
            map0.put(arr3[1],Integer.parseInt(arr2[1]));
        }
    }
    public static int getSingInt(Method method, Class<?> aClass) {
        lock.lock();
        try {
            String key0 = aClass.getName();
            String key1 = getSing(method, aClass);
            Map<String, Integer> map0 = map_getSingInt.get(key0);
            if (map0 == null) {
                map0 = new HashMap<>();
                map_getSingInt.put(key0, map0);
            }
            int id = map0.getOrDefault(key1, -1);
            if (id == -1) {
                id = map0.size();
                map0.put(key1, id);
            }
            //System.out.println("getSingInt|"+aClass.getName()+" "+method.getName()+" "+id);
            return id;
        } finally {
            lock.unlock();
        }
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
        return putObject(clazz, name, map, null);
    }

    public static Object putObject(Class<?> clazz, String name, Map<String, Object> map, Object object) throws Exception {
//问题排查 空的构造函数 - 猜测是 跳过就行了
        if (object == null) {
            object = clazz.getDeclaredConstructor().newInstance();
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            if (anInterface.getName().equals("groovy.lang.GroovyObject")) {
                continue;
            }
            if (anInterface.getName().equals("gzb.frame.factory.GzbOneInterface")) {
                continue;
            }
            if (anInterface.getName().equals("gzb.frame.factory.GzbEntityInterface")) {
                continue;
            }
            map.put(anInterface.getName(), object);
            log.t("储存对象", anInterface.getName(), object.toString());
        }
        map.put(clazz.getName(), object);
        log.t("储存对象", clazz.getName(), object.toString());
        if (name != null && !name.isEmpty()) {
            map.put(name, object);
            log.t("储存对象", name, object.toString());
        }
        return object;
    }


}
