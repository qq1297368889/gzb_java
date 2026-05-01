package gzb.frame.netty.entity;

import gzb.tools.Config;
import gzb.tools.FileTools;
import gzb.tools.OnlyId;
import java.io.File;
import java.io.IOException;

public class GzbFileImpl implements GzbFile {
    private final String name;
    private final String fileName;
    private final String contentType;
    private File file;
    public byte[] data;

    public GzbFileImpl(String name, String fileName, String contentType, byte[] data){
        this.name = name;
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public File getFile() throws IOException {
        if (file == null) {
            this.file = new File(Config.tempDir + File.separator + OnlyId.getDistributed() + File.separator + fileName);
            saveFile(this.file);
        }
        return file;
    }

    @Override
    public byte[] getBytes() throws IOException {
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
        if (file == null) {
            return false;
        }
        return file.delete();
    }
}
