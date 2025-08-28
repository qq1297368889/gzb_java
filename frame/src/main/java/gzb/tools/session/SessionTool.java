package gzb.tools.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionTool {
    public static final Session getSession(HttpServletRequest request, HttpServletResponse response) {
        return new SessionImpl(request, response);
    }
}
