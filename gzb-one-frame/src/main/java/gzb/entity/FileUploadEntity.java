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

import gzb.tools.Config;
import gzb.tools.OnlyId;
import io.netty.handler.codec.http.multipart.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * 与Netty解耦的文件上传类，可通过Netty的FileUpload对象自动初始化
 */
public class FileUploadEntity {
    private final String name;
    private final String fileName;
    private final File file;
    private final String contentType;
    public final FileUpload nettyFileUpload;
    public byte[]data;

    public FileUploadEntity(FileUpload nettyFileUpload) throws IOException {
        this.nettyFileUpload = nettyFileUpload;
        this.name = nettyFileUpload.getName();
        this.fileName=nettyFileUpload.getFilename();
        this.file=nettyFileUpload.getFile();
        this.contentType=nettyFileUpload.getContentType();
        nettyFileUpload.get();
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return fileName;
    }
    public String getContentType() {
        return contentType;
    }

    public File getFile() throws IOException {
/*        if (file == null) {
            this.file = new File(Config.tempDir + File.separator + OnlyId.getDistributed() + ".data");
            if (!nettyFileUpload.getFile().renameTo(file)) {
                throw new RuntimeException("Can't rename file 文件移动失败，请注意:" + file.getPath());
            }
        }*/
        return file;
    }


    public boolean delete() {
        if (nettyFileUpload != null) {
            nettyFileUpload.delete();
        }
        if (file != null) {
            return file.delete();
        }
        return true;
    }

}
