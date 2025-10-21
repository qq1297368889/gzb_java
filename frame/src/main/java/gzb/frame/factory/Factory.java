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

package gzb.frame.factory;

import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;
import gzb.entity.RunRes;

import java.util.Map;

public interface Factory {
    RunRes request(Request request, Response response);

    void loadJavaDir(String classDir, String pwd, String iv) throws Exception;

     Map<String, Object> getMapObject();

    void loadServerHttp() throws Exception;
}
