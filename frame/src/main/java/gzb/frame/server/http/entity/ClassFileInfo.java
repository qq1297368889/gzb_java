package gzb.frame.server.http.entity;

import java.io.File;

public class ClassFileInfo {
    File file;
    String className;
    int state;
    long time;

    @Override
    public String toString() {
        return "ClassFileInfo{" +
                "file=" + file +
                ", className='" + className + '\'' +
                ", state=" + state +
                '}';
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
