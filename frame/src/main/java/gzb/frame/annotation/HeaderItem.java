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
