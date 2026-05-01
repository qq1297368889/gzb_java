package gzb.frame.netty.entity;

import gzb.exception.GzbException0;
import gzb.tools.FileTools;
import io.netty.handler.codec.http.multipart.FileUpload;

import java.io.File;
import java.io.IOException;

public class GzbFileNetty implements GzbFile {
    private String name;
    private String fileName;
    private String contentType;
    private File file;
    public byte[] data;
    public FileUpload nettyFileUpload;

    public GzbFileNetty(FileUpload nettyFileUpload) throws IOException {
        this.nettyFileUpload = nettyFileUpload;

    }

    @Override
    public String getName() {
        if (name == null) {
            if (nettyFileUpload != null) {
                name = nettyFileUpload.getName();
            }

            if (name == null) {
                throw new GzbException0("文件接受对象为空");
            }
        }
        return name;
    }

    @Override
    public String getFileName() {
        if (fileName == null) {
            if (nettyFileUpload != null) {
                fileName = nettyFileUpload.getFilename();
            }

            if (fileName == null) {
                throw new GzbException0("文件接受对象为空");
            }
        }
        return fileName;
    }

    @Override
    public String getContentType() {
        if (contentType == null) {
            if (nettyFileUpload != null) {
                contentType = nettyFileUpload.getContentType();
            }

            if (contentType == null) {
                throw new GzbException0("文件接受对象为空");
            }
        }
        return contentType;
    }

    @Override
    public File getFile() throws IOException {
        if (file == null) {
            if (nettyFileUpload != null) {
                file = nettyFileUpload.getFile();
            }

            if (file == null) {
                throw new GzbException0("文件接受对象为空");
            }
        }
        return file;
    }

    @Override
    public byte[] getBytes() throws IOException {
        if (data == null) {
            if (nettyFileUpload != null) {
                data = nettyFileUpload.get();
            }

            if (data == null) {
                throw new GzbException0("文件接受对象为空");
            }
        }
        return data;
    }

    @Override
    public void saveFile(String fileUrl) throws IOException {
        saveFile(new File(fileUrl));
    }

    @Override
    public void saveFile(File file) throws IOException {
        byte[] bytes = getBytes();
        FileTools.save(file, bytes);
    }

    @Override
    public boolean deleteAllFile() {
        if (nettyFileUpload != null) {
            nettyFileUpload.delete();
        }
        return true;
    }
}
