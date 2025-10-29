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

import com.alibaba.fastjson2.JSON;
import gzb.tools.Config;
import gzb.tools.Tools;

import java.util.ArrayList;
import java.util.List;

public class GzbJsonImpl implements GzbJson {
    public String desc = "JSON序列化工具,为了节省性能所以支持单例模式运行，本类不做任何状态保存。无需额外再创建对象";
    public String className = GzbJsonImpl.class.getName();

    @Override
    public String initJson(Integer code, String msg, String url, Object data, Integer page, Integer size, Integer total) {
        StringBuilder sb=new StringBuilder(50);
        sb.append("{\"").append(Config.stateName).append("\":\"").append(code).append("\"");

        sb.append(",\"").append(Config.timeName).append("\":\"").append(System.currentTimeMillis()).append("\"");

        if (msg != null) {
            sb.append(",\"").append(Config.messageName).append("\":\"").append(Tools.escapeJsonString(msg)).append("\"");
        }
        if (url != null) {
            sb.append(",\"").append(Config.urlName).append("\":\"").append(Tools.escapeJsonString(url)).append("\"");
        }
        if (page != null) {
            sb.append(",\"").append(Config.pageName).append("\":\"").append(page).append("\"");
        }
        if (size != null) {
            sb.append(",\"").append(Config.sizeName).append("\":\"").append(size).append("\"");
        }
        if (total != null) {
            sb.append(",\"").append(Config.totalName).append("\":\"").append(total).append("\"");
        }

        if (data != null) {
            try {
                sb.append(",\"").append(Config.dataName).append("\":").append(Tools.toJson(data));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public String paging(List<?> list, Integer page, Integer size, Integer maxPage, Integer maxSize) {
        int start = 0;
        if (page == null || page < 0) {
            page = 1;
        }
        if (size == null || size < 0) {
            size = 10;
        }
        if (maxPage > 0 && page > maxPage) {
            page = maxPage;
        }
        if (maxSize > 0 && size > maxSize) {
            size = maxSize;
        }
        if (page > 1) {
            start = (page - 1) * size;
        }
        List<Object> listThis = new ArrayList<>();
        for (int i = start; i < list.size(); i++) {
            listThis.add(list.get(i));
            if (size > 0 && listThis.size() == size) {
                break;
            }
        }
        return initJson(Config.successVal, "分页查询成功", null, listThis, page, size, list.size());
    }

    public String paging(List<?> list, Integer page, Integer size) {
        return paging(list, page, size, 0, 0);
    }

    public String paging(List<?> list, Integer page, Integer size, int total) {
        return initJson(Config.successVal, "分页查询成功", null, list, page, size, total);
    }

    /**
     * 通用响应方法
     *
     * @param code 状态码
     * @param msg  消息
     * @param data 数据
     * @return JSON字符串
     */
    @Override
    public String response(int code, String msg, String url, Object data) {
        StringBuilder sb=new StringBuilder(50);
        sb.append("{\"").append(Config.stateName).append("\":\"").append(code).append("\"")
                .append(",\"")
                .append(Config.timeName).append("\":\"").append(System.currentTimeMillis()).append("\"");

        if (msg != null) {
            sb.append(",\"").append(Config.messageName).append("\":\"").append(Tools.escapeJsonString(msg)).append("\"");
        }
        if (url != null) {
            sb.append(",\"").append(Config.urlName).append("\":\"").append(Tools.escapeJsonString(url)).append("\"");
        }
        if (data != null) {
            try {
                sb.append(",\"").append(Config.dataName).append("\":").append(Tools.toJson(data));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        sb.append("}");
        return sb.toString();
    }

    /**
     * 成功响应（无数据）
     *
     * @param msg
     */
    @Override
    public String success(String msg) {
        return response(Config.successVal, msg, null, null);
    }

    /**
     * 成功响应（带数据）
     *
     * @param msg
     * @param data
     */
    @Override
    public String success(String msg, Object data) {
        return response(Config.successVal, msg, null, data);
    }

    /**
     * 失败响应（带数据）
     *
     * @param msg
     * @param data
     */
    @Override
    public String fail(String msg, Object data) {
        return response(Config.failVal, msg, null, data);
    }

    /**
     * 失败响应（无数据）
     *
     * @param msg
     */
    @Override
    public String fail(String msg) {
        return response(Config.failVal, msg, null, null);
    }

    /**
     * 错误响应（无数据）
     *
     * @param msg
     */
    @Override
    public String error(String msg) {
        return response(Config.errorVal, msg, null, null);
    }

    /**
     * 错误响应（带数据）
     *
     * @param msg
     * @param data
     */
    @Override
    public String error(String msg, Object data) {
        return response(Config.errorVal, msg, null, data);
    }

    /**
     * 跳转响应（默认地址）
     *
     * @param msg
     */
    @Override
    public String jump(String msg) {
        return response(Config.jumpVal, msg, null, null);
    }

    /**
     * 跳转响应（指定地址）
     *
     * @param msg
     * @param url
     */
    @Override
    public String jump(String msg, String url) {
        return response(Config.jumpVal, msg, url, null);
    }

    /**
     * 跳转响应（指定地址和数据）
     *
     * @param msg
     * @param url
     * @param data
     */
    @Override
    public String jump(String msg, String url, Object data) {
        return response(Config.jumpVal, msg, url, data);
    }
}
