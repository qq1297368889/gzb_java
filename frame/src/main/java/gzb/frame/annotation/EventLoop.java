package gzb.frame.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/// 不懂不要注解 这个 可能阻塞主线程
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventLoop {
    /// false 在事件循环上 阻塞运行 某些无阻塞小请求可以提升性能
    /// true 在业务线程池运行
    boolean async() default false;
}
