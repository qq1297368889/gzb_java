package gzb.frame.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义一个可用于类或方法上的注解，用于指定一个或多个 HTTP Header。
 * 它可以包含多个 @HeaderItem 注解。
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Header {
    /**
     * 包含一个或多个 Header 键值对的数组。
     */
    HeaderItem[] item() default {};
}
