package gzb.frame.factory;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import groovyjarjarasm.asm.*;
import groovyjarjarasm.asm.tree.LocalVariableNode;
import groovyjarjarasm.asm.tree.MethodNode;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ClassTools {

    ///  获取方法的参数名 在代码中查找
    public static List<String> getParameterNames(String javaCode, String methodName, Class[] paramTypes) {
        List<String> parameterNames = new ArrayList<>();
        try {
            // 创建 JavaParser 实例
            JavaParser javaParser = new JavaParser();

            // 使用实例调用 parse 方法
            ParseResult<CompilationUnit> parseResult = javaParser.parse(javaCode);

            if (parseResult.isSuccessful()) {
                CompilationUnit cu = parseResult.getResult().get();
                // 查找所有方法声明
                List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);
                for (MethodDeclaration method : methods) {
                    if (method.getNameAsString().equals(methodName)) {
                        // 检查参数类型
                        if (paramTypes.length == method.getParameters().size()) {
                            boolean paramsMatch = true;
                            for (int i = 0; i < paramTypes.length; i++) {
                                String simpleTypeName;
                                String t01 = null;
                                if (method.getParameters().get(i).getType().isArrayType()) {
                                    if (!paramTypes[i].isArray()) {
                                        break;
                                    }
                                    t01 = method.getParameters().get(i).getType().asArrayType().getComponentType().asString();
                                    simpleTypeName = paramTypes[i].getComponentType().getSimpleName();
                                } else {
                                    if (paramTypes[i].isArray()) {
                                        break;
                                    }
                                    t01 = method.getParameters().get(i).getType().asString();
                                    simpleTypeName = paramTypes[i].getSimpleName();
                                }
                                if (t01 == null || !t01.startsWith(simpleTypeName)) {
                                    paramsMatch = false;
                                    break;
                                }
                            }
                            if (paramsMatch) {
                                for (Parameter parameter : method.getParameters()) {
                                    parameterNames.add(parameter.getNameAsString());
                                }
                            }
                        }
                    }
                }
            } else {
                System.out.println("Java code parsing failed."); // 添加调试信息
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parameterNames;
    }
    ///  获取方法的参数名 在字节码中查找
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

    private static MethodNode findMethodNode(ClassReader cr, Method targetMethod) {
        String methodDescriptor = Type.getMethodDescriptor(targetMethod);

        final MethodNode[] result = new MethodNode[1];

        cr.accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor,
                                             String signature, String[] exceptions) {
                if (name.equals(targetMethod.getName()) && descriptor.equals(methodDescriptor)) {
                    MethodNode methodNode = new MethodNode(Opcodes.ASM9, access, name,
                            descriptor, signature, exceptions);
                    result[0] = methodNode;
                    return methodNode;
                }
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        }, ClassReader.EXPAND_FRAMES);

        return result[0];
    }

    // 根据索引查找局部变量节点
    private static LocalVariableNode findLocalVariableNode(MethodNode methodNode, int index) {
        for (LocalVariableNode lvn : methodNode.localVariables) {
            if (lvn.index == index) {
                return lvn;
            }
        }
        return null;
    }
}
