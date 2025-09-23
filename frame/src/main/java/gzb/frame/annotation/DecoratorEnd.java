package gzb.frame.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//方法级别注释后 可指定为 请求进入后调用
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DecoratorEnd {
    //指定拦截的链接 默认为全部拦截 比如  请求链接 /a/b/c/d/e  value=/a/b/c/ 那么会拦截 只要从头开始 包含就会拦截 多个相同的value会被覆盖
    String []value() default {"/"};
    //指定为 true  只要链接 包含就会拦截 不再只匹配开头
    boolean type() default false;
    //指定为 true  原包含匹配 改为不包含匹配
    boolean turn() default false;
    //要匹配的方法 GET POST PUT DELETE
    String[]method() default {"GET","POST","PUT","DELETE"};
}
