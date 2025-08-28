package gzb.tools.session;

import gzb.tools.Tools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebGzbSession {
    private static final String CookPwd = "AG5gHJKISXlums0r";
    private static final String CookIV = "HJYx5D6w1mbms09k";
    private int mm = -1;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public WebGzbSession(int mm, HttpServletRequest request, HttpServletResponse response) {
        this.mm = mm;
        this.request = request;
        this.response = response;
    }

    public int GetInt(String key) {
        String str = Tools.CookieGet(key, request);
        str = Tools.textAESEncoder(str, CookPwd, CookIV);
        if (str == null || str.length() < 1) {
            str = request.getParameter(key);
            if (str == null || str.length() < 1) {
                return 0;
            }
        }
        return Integer.valueOf(str);
    }

    public String GetString(String key) {
        String str = Tools.CookieGet(key, request);
        if (str == null || str.length() < 1) {
            str = request.getParameter(key);
            if (str == null || str.length() < 1) {
                return null;
            }
        }
        return Tools.textAESDecoder(str, CookPwd, CookIV);
    }

    public void Put(String key, String val) {
        val = Tools.textAESEncoder(val, CookPwd, CookIV);
        if (val != null) {
            Tools.CookieSet(key, val, mm, response, request);
        }
    }

    public void Del(String key) {
        Tools.CookieSet(key, null, mm, response, request);
    }
}
