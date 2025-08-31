package gzb.frame.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EntityAttribute {
    boolean key() default false;//true是id  否则不是
    int size() default  0;//0不限制 大于0会检查
    String name() default  "";//0不限制 大于0会检查
    String desc() default  "";//备注信息
}
