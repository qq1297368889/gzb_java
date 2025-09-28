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

package gzb.frame.generate;

import gzb.tools.FileTools;
import gzb.tools.Tools;

import java.io.File;
import java.io.IOException;

public class Base {
    public String path;
    public String pkg;
    public String table;

    public String getHump(String str) {
        return Tools.lowStr_hump(str);
    }

    public String getHump_h_x(String str) {
        return Tools.lowStr_x(getHump(str));
    }

    public String getHump_h_d(String str) {
        return Tools.lowStr_d(getHump(str));
    }

    public String getPkgEntity(String dbName) {
        return pkg + "." + dbName + ".entity";
    }

    public String getFilePathEntity(String dbName, String tableName) {
        String this_path = path + "/" + (getPkgEntity(dbName).replace(".", "/"))
                + (tableName == null ? "" : "/" + (Tools.lowStr_d(tableName)) + ".java");
        this_path = this_path.replaceAll("\\\\", "/");
        while (this_path.contains("//")) {
            this_path = this_path.replaceAll("//", "/");
        }
        if (tableName == null) {
            new File(this_path).mkdirs();
        } else {
            new File(this_path).getParentFile().mkdirs();
        }

        return this_path;
    }

    public String outCodeEntity(boolean save, String code, String dbName, String tableName) throws IOException {
        saveFile(getFilePathEntity(dbName, tableName), code, save);
        return code;
    }

    public String getPkgDb(String dbName) {
        return pkg + "." + dbName + ".entity";
    }

    public String getFilePathDb(String dbName) {
        String this_path = path + "/" + (dbName == null ? "" : "/" + (Tools.lowStr_d(dbName)) + ".java");
        this_path = this_path.replaceAll("\\\\", "/");
        while (this_path.contains("//")) {
            this_path = this_path.replaceAll("//", "/");
        }
        new File(this_path).mkdirs();

        return this_path;
    }

    public String outCodeDb(boolean save, String code, String dbName) throws IOException {
        saveFile(getFilePathDb(dbName), code, save);
        return code;
    }

    public String getFilePath(String pkg, String name) {
        String this_path = path + "/" + (pkg.replace(".", "/"))
                + (name == null ? "" : "/" + (Tools.lowStr_d(name)) + ".java");
        this_path = this_path.replaceAll("\\\\", "/");
        while (this_path.contains("//")) {
            this_path = this_path.replaceAll("//", "/");
        }
        if (name == null) {
            new File(this_path).mkdirs();
        } else {
            new File(this_path).getParentFile().mkdirs();
        }
        return this_path;
    }

    public String getHtmlPath() {
        String this_path = new File(path).getParent() + "/resources/templates/page";
        new File(this_path).mkdirs();
        return this_path;
    }

    public void saveFile(String path, String code, boolean save) {
        FileTools.mkdir(new File(path).getParentFile());
        //System.out.println(path);
        if (save) {
            FileTools.save(new File(path), code);
        }
    }
}
