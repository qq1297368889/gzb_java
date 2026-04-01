<html>
<%@ page import="gzb.frame.annotation.Resource" %>
<%@ page import="com.frame.dao.SysFileDao" %>
<%@ page import="java.util.List" %>
<%@ page import="com.frame.entity.SysFile" %>
<%--类级别变量--%>
<%!
    @Resource
    SysFileDao sysFileDao;
%>
<%--请求参数映射 也就是方法参数--%>
<jsp:useBean id="acc" class="java.lang.String" />
<jsp:useBean id="pwd" class="java.lang.String" />
<jsp:useBean id="sysUsers" class="com.frame.entity.SysUsers" />
<table>
    <tr>
        <%
            System.out.println(acc);
            System.out.println(pwd);
            System.out.println(sysUsers);
            List<SysFile> list = sysFileDao.query(new SysFile());
            for (SysFile sysFile : list) {%>
        <td><%=sysFile.getSysFileId()%></td>
        <%}%>
    </tr>
</table>
<body>
<script></script>
</body>
</html>