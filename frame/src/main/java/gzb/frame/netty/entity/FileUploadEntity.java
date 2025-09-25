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

package gzb.frame.netty.entity;

import gzb.tools.Config;
import gzb.tools.OnlyId;
import io.netty.handler.codec.http.multipart.FileUpload;

import java.io.File;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * 与Netty解耦的文件上传类，可通过Netty的FileUpload对象自动初始化
 */
public class FileUploadEntity {
    // 表单字段名称
    private final String name;
    // 文件名
    private final String filename;
    // 内容类型
    private final String contentType;
    // 字符集
    private final Charset charset;
    // 临时文件
    private final File file;
    // 是否为临时文件
    private final boolean isInMemory;
    // 文件大小
    private final long size;
    // 唯一标识符
    private final String id;

    /**
     * 构造方法：通过Netty的FileUpload对象自动初始化
     *
     * @param nettyFileUpload Netty的FileUpload对象
     */
    public FileUploadEntity(FileUpload nettyFileUpload) {
        // 从Netty对象复制核心属性
        this.name = nettyFileUpload.getName();
        this.filename = nettyFileUpload.getFilename();
        this.contentType = nettyFileUpload.getContentType();
        this.charset = nettyFileUpload.getCharset();
        this.isInMemory = nettyFileUpload.isInMemory();
        this.size = nettyFileUpload.length();
        this.id = generateUniqueId();

        // 复制文件内容或临时文件引用
        try {
            this.file = new File(Config.tempDir + File.separator + OnlyId.getDistributed() + ".data");
            if (!nettyFileUpload.getFile().renameTo(file)) {
                throw new RuntimeException("Can't rename file 文件移动失败，请注意"+file.getPath());
            }
        } catch (Exception e) {
            // 处理可能的IO异常
            throw new RuntimeException("Failed to copy content from Netty FileUpload", e);
        }
    }

    /**
     * 独立构造方法：不依赖Netty的初始化方式
     * （用于后续完全移除Netty依赖时使用）
     */
    public FileUploadEntity(String name, String filename, String contentType,
                            Charset charset, long size, boolean isInMemory, File file) {
        this.name = name;
        this.filename = filename;
        this.contentType = contentType;
        this.charset = charset;
        this.size = size;
        this.isInMemory = isInMemory;
        this.id = generateUniqueId();
        this.file = file;
    }

    /**
     * 生成唯一标识符
     */
    private String generateUniqueId() {
        return "file-upload-" + UUID.randomUUID().toString();
    }

    // 以下为getter和setter方法，保持与Netty接口类似的方法签名
    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public String getContentType() {
        return contentType;
    }

    public Charset getCharset() {
        return charset;
    }

    public File getFile() {
        return file;
    }

    public long getSize() {
        return size;
    }

    public boolean getIsInMemory() {
        return isInMemory;
    }

    public String getId() {
        return id;
    }

    /**
     * 释放资源
     */
    public boolean delete() {
        if (!isInMemory && file != null) {
            return file.delete();
        }
        return true;
    }

    @Override
    public String toString() {
        return "DecoupledFileUpload{" +
                "file='" + file.getPath() + '\'' +
                "name='" + name + '\'' +
                ", filename='" + filename + '\'' +
                ", contentType='" + contentType + '\'' +
                ", size=" + size +
                '}';
    }
}
