package gzb.frame.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//写在会被扫描的类里就行
//参数如果有可注入的 会自动注入 类变量也会注入
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ThreadInterval {
    //定时 间隔时间 单位毫秒 默认1000 (1秒)
    long value() default 1000;

    //启动定时器数量 默认1
    int num() default 1;

    //false 异步  true同步  不要随意使用异步  默认 true
    boolean async() default true;

}
