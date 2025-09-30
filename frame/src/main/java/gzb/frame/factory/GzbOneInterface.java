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

import java.util.Map;

public interface GzbOneInterface {
/*    public Object _gzb_call_x01(
            int methodName,
                                Map<String, java.util.List<Object>> requestMap,
                                Map<String, Object> mapObject, Object[] arrayObject,
                                boolean openTransaction
    )throws Exception;*/
public Object _gzb_call_x01(
        int _gzb_one_c_id,
        java.util.Map<String, Object> _gzb_one_c_mapObject,
        Request _g_p_req,
        Response _g_p_resp,
        java.util.Map<String, java.util.List<Object>> _gzb_one_c_requestMap,
        Object[] arrayObject,
        boolean _gzb_x001_openTransaction
)throws Exception;
}
