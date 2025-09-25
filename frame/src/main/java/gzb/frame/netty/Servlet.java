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

package gzb.frame.netty;

import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;
import io.netty.handler.codec.http.HttpMethod;

public class Servlet {
    public Servlet() {

    }
    public String get(Request request,Response response){

        return "{\"code\":\"1\"}";
    }
    public String post(Request request,Response response){

        return "{\"code\":\"1\"}";
    }
    public String put(Request request,Response response){

        return "{\"code\":\"1\"}";
    }
    public String delete(Request request,Response response){

        return "{\"code\":\"1\"}";
    }
    public String options(Request request,Response response){

        return "{\"code\":\"1\"}";
    }
    public String head(Request request,Response response){

        return "{\"code\":\"1\"}";
    }
    public String patch(Request request,Response response){

        return "{\"code\":\"1\"}";
    }
    public String trace(Request request,Response response){

        return "{\"code\":\"1\"}";
    }
    public String connect(Request request,Response response){

        return "{\"code\":\"1\"}";
    }

}
