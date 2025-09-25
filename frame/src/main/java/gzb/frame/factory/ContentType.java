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

import gzb.tools.Config;

public class ContentType {
    public final static String json = "application/json; charset="+Config.encoding;
    public final static String from = "application/x-www-form-urlencoded; charset="+Config.encoding;
    public final static String xml = "application/xml; charset="+Config.encoding;
    public final static String text = "text/plain; charset="+Config.encoding;
    public final static String html = "text/html; charset="+ Config.encoding;
    public final static String css = "text/css; charset="+Config.encoding;
    public final static String javaScript = "text/javascript; charset="+Config.encoding;
    public final static String file = "application/octet-stream";
}
