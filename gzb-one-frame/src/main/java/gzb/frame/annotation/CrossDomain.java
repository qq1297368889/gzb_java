/*
 *
 *  * Copyright [2025] [GZB ONE]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package gzb.frame.annotation;

import java.lang.annotation.*;

/**
 * 用于配置跨域资源共享 (CORS) 的注解。
 * 可用于类级别 (Controller) 或方法级别 (具体接口)。
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CrossDomain {

    /**
     * 允许进行跨域请求的源（域名）。
     * "*" 表示允许所有域名，但当 allowCredentials 为 true 时不能使用。
     * 示例: {"https://www.example.com", "http://api.test.org"}
     *
     * @return 允许的源列表
     */
    String[] origins() default {"*"};

    /**
     * 允许的 HTTP 方法列表。
     * 常用的方法包括 "GET", "POST", "PUT", "DELETE", "OPTIONS" 等。
     *
     * @return 允许的方法列表
     */
    String[] methods() default {"GET","POST","PUT","DELETE"};

    /**
     * 允许在实际请求中使用的自定义请求头。
     * 例如 "Content-Type", "Authorization" 等。
     *
     * @return 允许的请求头列表
     */
    String[] headers() default {"*"};

    /**
     * 允许浏览器访问的响应头列表。
     * 默认情况下，浏览器只能访问一些简单的响应头，如果后端需要返回自定义头，
     * 必须在这里列出。
     * 示例: {"X-Custom-Header", "X-Auth-Token"}
     *
     * @return 暴露给浏览器的响应头列表
     */
    String[] exposedHeaders() default {"*"};

    /**
     * 指示是否允许浏览器随请求发送凭证（如 cookies, HTTP 认证或客户端 SSL 证书）。
     * 如果设置为 true，origins 属性就不能是 "*"。
     *
     * @return 是否允许发送凭证
     */
    boolean allowCredentials() default false;

    /**
     * 预检请求（OPTIONS）的结果可以被浏览器缓存的时间，单位为秒。
     * 这可以减少不必要的预检请求，提高性能。
     *
     * @return 缓存时间（秒）
     */
    long maxAge() default 600;
}