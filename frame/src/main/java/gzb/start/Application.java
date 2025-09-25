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
import gzb.frame.netty.NettyServer;
import gzb.tools.Config;
import gzb.tools.Tools;
public class Application {
    public static NettyServer server = null;
    public static void main(String[] args) throws Exception {
        run(Application.class);
    }
    public static void run(Class<?>aClass) throws Exception {
        String path = Tools.getProjectRoot(aClass);
        System.setProperty("file.encoding","UTF-8");
        System.setProperty("this.dir", path);
        server = new NettyServer();
        if (Config.WS_PORT>0) {

        }
        if (Config.TCP_PORT>0) {

        }
        if (Config.UDP_PORT>0) {

        }
        if (Config.HTTP_PORT>0) {
            server.startHTTPServer(Config.HTTP_PORT, Math.max(Config.cpu / 10, 1), Config.threadNum);
        }
    }

}
