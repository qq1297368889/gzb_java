package gzb.tools;

public class RSAEntity {
    public String pub;
    public String pri;

    public RSAEntity(String pub, String pri) {
        this.pub = pub;
        this.pri = pri;
        this.pub=this.pub.replaceAll(" ","");
        this.pri=this.pri.replaceAll(" ","");
    }
}