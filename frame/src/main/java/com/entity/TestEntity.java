package com.entity;

import gzb.tools.Config;
import gzb.tools.Tools;

import java.util.Map;

public class TestEntity {
    public Long id;
    public String acc;
    public String name;
    public String pwd;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(102);
        boolean app01 = false;
        sb.append("{");
        if (this.id != null) {
            if (app01) {
                sb.append(",");
            }
            app01 = true;
            sb.append("\"id\":\"").append(id).append("\"");
        }
        if (this.acc != null) {
            if (app01) {
                sb.append(",");
            }
            app01 = true;
            sb.append("\"acc\":");
            Tools.escapeJsonString0(acc,sb);
        }
        if (this.name != null) {
            if (app01) {
                sb.append(",");
            }
            app01 = true;
            sb.append("\"name\":");
            Tools.escapeJsonString0(name,sb);
        }
        if (this.pwd != null) {
            if (app01) {
                sb.append(",");
            }
            app01 = true;
            sb.append("\"pwd\":");
            Tools.escapeJsonString0(pwd,sb);
        }
        return sb.append("}").toString();
    }
}
