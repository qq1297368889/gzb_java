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

package gzb.start;
import gzb.frame.netty.Server;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;

public class Application {
    public static void main(String[] args) throws Exception {
        run(Application.class);
    }
    public static void run() throws Exception {
        run(null);
    }
    public static void run(Class<?>aClass) throws Exception {
        long start=System.currentTimeMillis();
        if (aClass!=null) {
            String path = Tools.getProjectRoot(aClass);
            System.setProperty("this.dir", path);
        }
        System.setProperty("file.encoding","UTF-8");
        if (Config.WS_PORT>0) {

        }
        if (Config.TCP_PORT>0) {
            Server.startTCPServer(Config.TCP_PORT);
        }
        if (Config.UDP_PORT>0) {
            Server.startUDPServer(Config.UDP_PORT);
        }
        if (Config.HTTP_PORT>0) {
            Server.startHTTPServer(Config.HTTP_PORT);
        }
        long end=System.currentTimeMillis();
        Log.log.i("服务器自动总耗时",end-start);
    }


}
