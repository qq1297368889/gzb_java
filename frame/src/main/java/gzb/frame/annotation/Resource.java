package gzb.frame.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, METHOD})
@Retention(RUNTIME)
public @interface Resource {
    //默认会匹配实现了本接口的 实现类 注意如果 service 的value 和这个value需要一致   多个实现类的情况 通过这里指定对应类名
    String value() default "";
}
