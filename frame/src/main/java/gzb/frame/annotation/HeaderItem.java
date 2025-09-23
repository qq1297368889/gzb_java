package gzb.frame.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义一个用于单个 HTTP Header 的注解。
 * 这个注解将用于 @Header 注解的数组中。
 */
@Target({}) // 只能作为其他注解的嵌套元素使用
@Retention(RetentionPolicy.RUNTIME)
public @interface HeaderItem {
    /**
     * Header 的键。
     */
    String key();

    /**
     * Header 的值。
     */
    String val();
}
