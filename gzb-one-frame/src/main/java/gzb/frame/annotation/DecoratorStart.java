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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//方法级别注释后 可指定为 请求进入前调用
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DecoratorStart {
    //指定拦截的链接 默认为全部拦截 比如  请求链接 /a/b/c/d/e  value=/a/b/c/ 那么会拦截 只要从头开始 包含就会拦截
    String []value() default {"/"};
    //指定为 true  只要链接 包含就会拦截 不再只匹配开头
    boolean type() default false;
    //指定为 true  原包含匹配 改为不包含匹配
    boolean turn() default false;
    //要匹配的方法 GET POST PUT DELETE
    String[]method() default {"GET","POST","PUT","DELETE"};
    int sort() default 0;//排序 越小越先执行
}
