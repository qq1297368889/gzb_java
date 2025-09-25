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

import gzb.frame.generate.GenerateJavaCode;
import gzb.tools.Config;
import gzb.tools.Tools;

public class Auto {
    public static void main(String[] args) throws Exception {
        System.setProperty("this.dir", Tools.getProjectRoot(Auto.class));
        String path = Config.thisPath() + "/src/main/java";
        String pkg = "com";
        String dbName = "frame";
        GenerateJavaCode.generateCode(path,pkg,2,true,dbName,dbName);
    }
}
