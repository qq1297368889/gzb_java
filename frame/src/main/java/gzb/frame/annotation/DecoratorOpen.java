package gzb.frame.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//方法级别注释后 可指定为 请求进入后调用
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DecoratorOpen {
    //为true  本类对象会被装饰器匹配
    boolean value() default true;
}
