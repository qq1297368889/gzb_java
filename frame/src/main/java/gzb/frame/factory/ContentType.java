package gzb.frame.factory;

import gzb.tools.Config;

public class ContentType {
    public final static String json = "application/json; charset="+Config.encoding;
    public final static String from = "application/x-www-form-urlencoded; charset="+Config.encoding;
    public final static String xml = "application/xml; charset="+Config.encoding;
    public final static String text = "text/plain; charset="+Config.encoding;
    public final static String html = "text/html; charset="+ Config.encoding;
    public final static String css = "text/css; charset="+Config.encoding;
    public final static String javaScript = "text/javascript; charset="+Config.encoding;
    public final static String file = "application/octet-stream";
}
