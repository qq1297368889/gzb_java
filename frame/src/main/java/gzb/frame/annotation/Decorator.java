package gzb.frame.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//类级别注释后 该类才会被加入 候选项
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Decorator {
    //为true  本类对象加入候选项
    boolean value() default true;
}
