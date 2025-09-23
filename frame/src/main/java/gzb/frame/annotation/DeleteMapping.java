package gzb.frame.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DeleteMapping {
    //映射匹配
    public String[] value() default {"/"};
    //非空的话就是跨域域名
    public String crossDomain() default "";
    //返回协议头
    public String[] header() default {};
}
