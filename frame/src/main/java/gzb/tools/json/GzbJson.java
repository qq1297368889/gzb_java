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

package gzb.tools.json;

import java.util.List;

public interface GzbJson {
    /**
     * 分页 基础方法
     */
    public String initJson(Integer code, String msg, String url, Object data, Integer page, Integer size, Integer total);

    /**
     * 分页 自动分页 内存分页
     */
    public String paging(List<?> list, Integer page, Integer size, Integer maxPage, Integer maxSize);

    /**
     * 分页 自动分页 内存分页
     */
    public String paging(List<?> list, Integer page, Integer size) ;

    /**
     * 分页 仅仅组装数据，不做任何处理
     */
    public String paging(List<?> list, Integer page, Integer size, int total);
    /**
     * 通用响应方法
     * @param code 状态码
     * @param msg 消息
     * @param data 数据
     * @return JSON字符串
     */
    String response(int code, String msg,String url, Object data);

    /**
     * 成功响应（无数据）
     */
    String success(String msg);

    /**
     * 成功响应（带数据）
     */
    String success(String msg, Object data);

    /**
     * 失败响应（带数据）
     */
    String fail(String msg, Object data);

    /**
     * 失败响应（无数据）
     */
    String fail(String msg);

    /**
     * 错误响应（无数据）
     */
    String error(String msg);

    /**
     * 错误响应（带数据）
     */
    String error(String msg, Object data);

    /**
     * 跳转响应（默认地址）
     */
    String jump(String msg);

    /**
     * 跳转响应（指定地址）
     */
    String jump(String msg, String url);

    /**
     * 跳转响应（指定地址和数据）
     */
    String jump(String msg, String url, Object data);
}