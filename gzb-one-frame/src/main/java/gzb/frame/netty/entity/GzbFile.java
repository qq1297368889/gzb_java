package gzb.frame.netty.entity;

import gzb.tools.FileTools;

import java.io.File;
import java.io.IOException;

public interface GzbFile {
    String getName();

    String getFileName();

    String getContentType();

    File getFile() throws IOException;

    byte[] getBytes() throws IOException;

    void saveFile(String fileUrl) throws IOException;

    void saveFile(File file) throws IOException;

    boolean deleteAllFile();
}
