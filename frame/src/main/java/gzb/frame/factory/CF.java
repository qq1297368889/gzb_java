package gzb.frame.factory;

import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
//想实现 0反射 不过遇到了一些问题 比较复杂有空再解决
public class CF {
    static Log log= new LogImpl();
    public static void main(String[] args) throws NoSuchMethodException {
        User user = new User();
        gen_code_entity_load_map(User.class);
    }
    public static String gen_code_servlet_do_all(Class<?>aClass) throws NoSuchMethodException {
        Field []fields = aClass.getDeclaredFields();
        String className=aClass.getName();
        String code="";

        for (Field field : fields) {
            String name= field.getName();
            String name_d= Tools.lowStr_d(name);

        }

        return code;
    }
    public static String gen_code_servlet_do_all000(Class<?>aClass) throws NoSuchMethodException {
        Field []fields = aClass.getDeclaredFields();
        String className=aClass.getName();
        String code="";

        for (Field field : fields) {
            String name= field.getName();
            String name_d= Tools.lowStr_d(name);

        }

        return code;
    }
    public static String gen_code_entity_load_map(Class<?>aClass) throws NoSuchMethodException {
        Field []fields = aClass.getDeclaredFields();
        String className=aClass.getName();
        String code = "   public "+className+" loadObj(Map<String,Object> map) throws Exception {\n" +
                "        "+className+" returnObj = null;\n" +
                "        Object obj = null;\n" +
                "        try {\n";
        System.out.println(className);
        for (Field field : fields) {
            code+="            obj = map.get(\""+field.getName()+"\");\n" +
                    "            if (obj!=null) {\n" +
                    "                if (returnObj==null) {\n" +
                    "                    returnObj=new "+className+"();\n" +
                    "                }\n" ;
            if (Modifier.isPublic(field.getModifiers())){
                code+="                returnObj."+field.getName()+"="+field.getType().getName()+".valueOf(obj.toString());\n";
            }else{
                String name_d= Tools.lowStr_d(field.getName());
                try {
                    aClass.getMethod("set"+name_d,field.getType());
                    code+="                returnObj.set"+name_d+"("+field.getType().getName()+".valueOf(obj.toString()));\n";
                }catch (Exception e){
                    log.e(e);
                }
            }
                    code+="            }\n";
        }
        code+="        }catch (Exception e){\n" +
                "            log.e(e);\n" +
                "        } \n";
        code+="        return returnObj;\n" +
                "    }";
        System.out.println(code);
        return code;
    }

    public gzb.frame.factory.User loadObj(Map<String,Object> map) throws Exception {
        gzb.frame.factory.User returnObj = null;
        Object obj = null;
        try {
            obj = map.get("name");
            if (obj!=null) {
                if (returnObj==null) {
                    returnObj=new gzb.frame.factory.User();
                }
                returnObj.setName(java.lang.String.valueOf(obj.toString()));
            }
            obj = map.get("age");
            if (obj!=null) {
                if (returnObj==null) {
                    returnObj=new gzb.frame.factory.User();
                }
                returnObj.setAge(java.lang.Integer.valueOf(obj.toString()));
            }
            obj = map.get("password");
            if (obj!=null) {
                if (returnObj==null) {
                    returnObj=new gzb.frame.factory.User();
                }
                returnObj.setPassword(java.lang.String.valueOf(obj.toString()));
            }
            obj = map.get("price");
            if (obj!=null) {
                if (returnObj==null) {
                    returnObj=new gzb.frame.factory.User();
                }
                returnObj.price=java.lang.Double.valueOf(obj.toString());
            }
            obj = map.get("open");
            if (obj!=null) {
                if (returnObj==null) {
                    returnObj=new gzb.frame.factory.User();
                }
                returnObj.open=java.lang.Boolean.valueOf(obj.toString());
            }
        }catch (Exception e){
            log.e(e);
        }
        return returnObj;
    }


    public static void test001(String name,int age,boolean flag,Double price) {

    }
    public static void call() {

    }
    public static Class<?>gen_MetParaClass(Method method) {
        Class []arr1=method.getParameterTypes();
        for (Class aClass : arr1) {

        }

        return null;
    }
}
class call01{

}
class User {
    private String name;
    private Integer age;
    private String password;
    public Double price;
    public Boolean open;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
