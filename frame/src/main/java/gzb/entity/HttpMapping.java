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

package gzb.entity;

import gzb.frame.factory.GzbOneInterface;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class HttpMapping {
    //http端点映射方法
    public GzbOneInterface httpMappingFun;
    //直接映射运行方法
    //public Runnable runnable;

    public String sign;
    public int id;
    public String path;
    public int met;
    //是否开启事务
    public Integer transaction;
    //是否开启跨域 域名替换
    public boolean isCrossDomainOrigin;
    //是否异步 同步的话就在时间循环上执行 默认异步
    public boolean async = true;
    // 开启接口缓存
    public String[]cacheKey = null;
    public Integer cacheSecond= null;

    public Semaphore semaphore;
    public List<DecoratorEntity> start;
    public List<DecoratorEntity> end;
    public Map<String, String> header;



/*

    //http端点映射方法
    public GzbOneInterface httpMappingFun;

    public String sign;
    public int id;
    public String path;
    public int met;
    //是否开启事务
    public boolean isOpenTransaction;
    //是否开启跨域 域名替换
    public boolean isCrossDomainOrigin;
    public int limitation;
    public List<DecoratorEntity> start;
    public List<DecoratorEntity> end;
    public Map<String,String> header;

*/

    @Override
    public String toString() {
        return "{" +
                "httpMappingFun=" + httpMappingFun +
                ", sign='" + sign + '\'' +
                ", id=" + id +
                ", path='" + path + '\'' +
                ", met=" + met +
                ", transaction=" + transaction +
                ", isCrossDomainOrigin=" + isCrossDomainOrigin +
                ", async=" + async +
                ", semaphore=" + semaphore +
                ", start=" + start +
                ", end=" + end +
                ", header=" + header +
                '}';
    }
}
