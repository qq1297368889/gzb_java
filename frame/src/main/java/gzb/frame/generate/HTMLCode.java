package gzb.frame.generate;

import gzb.entity.TableInfo;
import gzb.tools.Config;
import gzb.tools.Tools;
import java.io.File;
import java.util.List;

public class HTMLCode extends Base {
    public HTMLCode(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }

    public void start(List<TableInfo> list, boolean save) {
        gen_login_html();
        gen_index_html();
        gen_list_html();
        gen_save_html();
        gen_update_html();
        for (TableInfo tableInfo : list) {
            gen_jsCode(tableInfo.getNameHumpLowerCase(), tableInfo.getIdHumpLowerCase());
        }

    }

    public void gen_login_html() {
        String code = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\""+ Config.encoding+"\">\n" +
                "    <title>登入 - 系统</title>\n" +
                "    <meta name=\"renderer\" content=\"webkit\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
                "    <meta name=\"viewport\"\n" +
                "          content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0\">\n" +
                "    <link rel=\"stylesheet\" href=\"/static/layuiadmin/layui/css/layui.css\" media=\"all\">\n" +
                "    <link rel=\"stylesheet\" href=\"/static/layuiadmin/style/admin.css\" media=\"all\">\n" +
                "    <link rel=\"stylesheet\" href=\"/static/layuiadmin/style/login.css\" media=\"all\">\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<div class=\"layadmin-user-login layadmin-user-display-show\" id=\"LAY-user-login\" style=\"display: none;\">\n" +
                "\n" +
                "    <div class=\"layadmin-user-login-main\">\n" +
                "        <div class=\"layadmin-user-login-box layadmin-user-login-header\">\n" +
                "            <h2>后台系统登录</h2>\n" +
                "        </div>\n" +
                "        <div class=\"layadmin-user-login-box layadmin-user-login-body layui-form\">\n" +
                "            <div class=\"layui-form-item\">\n" +
                "                <label class=\"layadmin-user-login-icon layui-icon layui-icon-username\"\n" +
                "                       for=\"LAY-user-login-username\"></label>\n" +
                "                <input type=\"text\" name=\"username\" id=\"LAY-user-login-username\" lay-com.verify=\"required\"\n" +
                "                       placeholder=\"用户名\"\n" +
                "                       class=\"layui-input\">\n" +
                "            </div>\n" +
                "            <div class=\"layui-form-item\">\n" +
                "                <label class=\"layadmin-user-login-icon layui-icon layui-icon-password\"\n" +
                "                       for=\"LAY-user-login-password\"></label>\n" +
                "                <input type=\"password\" name=\"password\" id=\"LAY-user-login-password\" lay-com.verify=\"required\"\n" +
                "                       placeholder=\"密码\" class=\"layui-input\">\n" +
                "            </div>\n" +
                "            <div class=\"layui-form-item\">\n" +
                "                <div class=\"layui-row\">\n" +
                "                    <div class=\"layui-col-xs7\">\n" +
                "                        <label class=\"layadmin-user-login-icon layui-icon layui-icon-vercode\"\n" +
                "                               for=\"LAY-user-login-vercode\"></label>\n" +
                "                        <input type=\"text\" name=\"vercode\" id=\"LAY-user-login-vercode\" lay-com.verify=\"required\"\n" +
                "                               placeholder=\"图形验证码\" class=\"layui-input\">\n" +
                "                    </div>\n" +
                "                    <div class=\"layui-col-xs5\">\n" +
                "                        <div style=\"margin-left: 10px;\"> \n" +
                "                            <img class=\"layadmin-user-login-codeimg\" id=\"vercode\">\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            \n" +
                "            <div class=\"layui-form-item\">\n" +
                "                <button class=\"layui-btn layui-btn-fluid\" type=\"button\" id=\"LAY-user-login-submit\">登入 系统</button>\n" +
                "            </div>\n" +
                "            \n" +
                "        </div>\n" +
                "    </div> \n" +
                "\n" +
                "</div>\n" +
                "\n" +
                "<script src=\"/static/layuiadmin/layui/layui.all.js\"></script>\n" +
                "<script src=\"/static/js/layui.gzb.js\"></script>\n" +
                "<script>\n" +
                "    let url_image_code = \"/system/v1.0.0/image/code\";\n" +
                "    let url_login = \"/system/v1.0.0/login\";\n" +
                "    let url_index_page = 'index.html?v=1.0.0.1&r=login.html';\n" +
                "    layui.config({\n" +
                "        base: '/static/layuiadmin/' //静态资源所在路径\n" +
                "    }).extend({\n" +
                "        index: 'lib/index' //主入口模块\n" +
                "    }).use(['index', 'user'], function () {\n" +
                "        var $ = layui.$\n" +
                "            , setter = layui.setter\n" +
                "            , admin = layui.admin\n" +
                "            , form = layui.form\n" +
                "            , router = layui.router()\n" +
                "            , search = router.search;\n" +
                "        $(\"#vercode\").click(function (){\n" +
                "            update_image()\n" +
                "        });\n" +
                "        function update_image() {\n" +
                "            $(\"#vercode\").attr(\"src\", url_image_code + \"?t=\" + new Date().getTime());\n" +
                "        }\n" +
                "        update_image();\n" +
                "        form.render();\n" +
                "        $(\"#LAY-user-login-submit\").click(function () {\n" +
                "            let sysUsersAcc = $(\"#LAY-user-login-username\").val();\n" +
                "            let sysUsersPwd = $(\"#LAY-user-login-password\").val();\n" +
                "            let code = $(\"#LAY-user-login-vercode\").val();\n" +
                "            if (sysUsersAcc == null || sysUsersAcc.length < 2) {\n" +
                "                gzb.msgErr(\"请输入 账号\");\n" +
                "                return;\n" +
                "            }\n" +
                "            if (sysUsersPwd == null || sysUsersPwd.length < 2) {\n" +
                "                gzb.msgErr(\"请输入 密码\");\n" +
                "                return;\n" +
                "            }\n" +
                "            if (code == null || code.length < 2) {\n" +
                "                gzb.msgErr(\"请输入 验证码\");\n" +
                "                return;\n" +
                "            }\n" +
                "            gzb.get(url_login, \"sysUsersAcc=\" + sysUsersAcc + \"&sysUsersPwd=\" + sysUsersPwd + \"&code=\" + code, function (res) {\n" +
                "                if (!gzb.jsonVerify(res, true)) {\n" +
                "                    update_image()\n" +
                "                    return;\n" +
                "                }\n" +
                "                location.href = url_index_page;\n" +
                "            }, function (res) {\n" +
                "                gzb.jsonVerify(res, true)\n" +
                "                update_image()\n" +
                "            })\n" +
                "        });\n" +
                "\n" +
                "    });\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";
        String path = getHtmlPath();
        path = Tools.pathFormat(path);
        if (!new File(path + "/login.html").exists()) {
            saveFile(path + "/login.html", code, true);
        }

    }

    public void gen_index_html() {
        String code = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\""+Config.encoding+"\">\n" +
                "    <title class=\"titleName\">后台系统</title>\n" +
                "    <meta name=\"renderer\" content=\"webkit\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
                "    <meta name=\"viewport\"\n" +
                "          content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0\">\n" +
                "    <link rel=\"stylesheet\" href=\"/static/layuiadmin/layui/css/layui.css\" media=\"all\">\n" +
                "    <link rel=\"stylesheet\" href=\"/static/layuiadmin/style/admin.css\" media=\"all\">\n" +
                "\n" +
                "</head>\n" +
                "<body class=\"layui-layout-body\">\n" +
                "\n" +
                "<div id=\"LAY_app\">\n" +
                "    <div class=\"layui-layout layui-layout-admin\">\n" +
                "        <div class=\"layui-header\">\n" +
                "            \n" +
                "            <ul class=\"layui-nav layui-layout-left\">\n" +
                "                <li class=\"layui-nav-item layadmin-flexible\" lay-unselect>\n" +
                "                    <a href=\"javascript:;\" layadmin-event=\"flexible\" title=\"侧边伸缩\">\n" +
                "                        <i class=\"layui-icon layui-icon-shrink-right\" id=\"LAY_app_flexible\"></i>\n" +
                "                    </a>\n" +
                "                </li>\n" +
                "                <li class=\"layui-nav-item\" lay-unselect>\n" +
                "                    <a href=\"javascript:;\" layadmin-event=\"refresh\" title=\"刷新\">\n" +
                "                        <i class=\"layui-icon layui-icon-refresh-3\"></i>\n" +
                "                    </a>\n" +
                "                </li>\n" +
                "            </ul>\n" +
                "            <ul class=\"layui-nav layui-layout-right\" lay-filter=\"layadmin-layout-right\">\n" +
                "\n" +
                "                <li class=\"layui-nav-item\" lay-unselect>\n" +
                "                    <a href=\"javascript:;\">\n" +
                "                        <cite id=\"usersName\"></cite>\n" +
                "                    </a>\n" +
                "                    <dl class=\"layui-nav-child\" id=\"usersTab\">\n" +
                "                        \n" +
                "                        \n" +
                "                    </dl>\n" +
                "                </li>\n" +
                "                <li class=\"layui-nav-item layui-show-xs-inline-block layui-hide-sm\" lay-unselect>\n" +
                "                    <a href=\"javascript:;\" layadmin-event=\"more\"><i class=\"layui-icon layui-icon-more-vertical\"></i></a>\n" +
                "                </li>\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "\n" +
                "        \n" +
                "        <div class=\"layui-side layui-side-menu\">\n" +
                "            <div class=\"layui-side-scroll\">\n" +
                "                <div class=\"layui-logo\" lay-href=\"\" id=\"home-3\">\n" +
                "                    <span class=\"titleName\" id=\"titleName\">layuiAdmin</span>\n" +
                "                </div>\n" +
                "                <ul class=\"layui-nav layui-nav-tree\" lay-shrink=\"all\" id=\"LAY-system-side-menu\"\n" +
                "                    lay-filter=\"layadmin-system-side-menu\">\n" +
                "                </ul>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"layadmin-pagetabs\" id=\"LAY_app_tabs\">\n" +
                "            <div class=\"layui-icon layadmin-tabs-control layui-icon-prev\" layadmin-event=\"leftPage\"></div>\n" +
                "            <div class=\"layui-icon layadmin-tabs-control layui-icon-next\" layadmin-event=\"rightPage\"></div>\n" +
                "            <div class=\"layui-icon layadmin-tabs-control layui-icon-down\">\n" +
                "                <ul class=\"layui-nav layadmin-tabs-select\" lay-filter=\"layadmin-pagetabs-nav\">\n" +
                "                    <li class=\"layui-nav-item\" lay-unselect>\n" +
                "                        <a href=\"javascript:;\"></a>\n" +
                "                        <dl class=\"layui-nav-child layui-anim-fadein\">\n" +
                "                            <dd layadmin-event=\"closeThisTabs\"><a href=\"javascript:;\">关闭当前标签页</a></dd>\n" +
                "                            <dd layadmin-event=\"closeOtherTabs\"><a href=\"javascript:;\">关闭其它标签页</a></dd>\n" +
                "                            <dd layadmin-event=\"closeAllTabs\"><a href=\"javascript:;\">关闭全部标签页</a></dd>\n" +
                "                        </dl>\n" +
                "                    </li>\n" +
                "                </ul>\n" +
                "            </div>\n" +
                "            <div class=\"layui-tab\" lay-unauto lay-allowClose=\"true\" lay-filter=\"layadmin-layout-tabs\">\n" +
                "                <ul class=\"layui-tab-title\" id=\"LAY_app_tabsheader\">\n" +
                "                    <li lay-id=\"\" lay-attr=\"\" class=\"layui-this\" id=\"home-1\"><i\n" +
                "                            class=\"layui-icon layui-icon-home\"></i></li>\n" +
                "                </ul>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "\n" +
                "\n" +
                "        \n" +
                "        <div class=\"layui-body\" id=\"LAY_app_body\">\n" +
                "            <div class=\"layadmin-tabsbody-item layui-show\">\n" +
                "                <iframe src=\"\" frameborder=\"0\" class=\"layadmin-iframe\" id=\"home-2\"></iframe>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "\n" +
                "        \n" +
                "        <div class=\"layadmin-body-shade\" layadmin-event=\"shade\"></div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>\n" +
                "\n" +
                "\n" +
                "<script src='/static/layuiadmin/layui/layui.all.js'></script>\n" +
                "<script src=\"/static/js/layui.gzb.js\"></script>\n" +
                "<script>\n" +
                "    let deleteToken=null;\n" +
                "    layui.config({\n" +
                "        base: '/static/layuiadmin/' //静态资源所在路径\n" +
                "    }).extend({\n" +
                "        index: 'lib/index' //主入口模块\n" +
                "    }).use(['index', 'form', 'laydate'], function () {\n" +
                "        var $ = layui.$\n" +
                "            , admin = layui.admin\n" +
                "            , element = layui.element\n" +
                "            , layer = layui.layer\n" +
                "            , laydate = layui.laydate\n" +
                "            , form = layui.form;\n" +
                "        //api的状态 定义\n" +
                "        let listAuthorityApi = \"/system/v1.0.0/read/permission\";\n" +
                "        let userInfoApi = \"/system/v1.0.0/read/user/info\";\n" +
                "        let exitUserApi = \"/system/v1.0.0/exit/user\";\n" +
                "\n" +
                "        $(\"#titleName\").text(\"后台管理系统\");\n" +
                "        let leftData = {};\n" +
                "        function initIndex(){\n" +
                "            gzb.get(listAuthorityApi, null, function (res) {\n" +
                "                if (gzb.jsonVerify(res)) {\n" +
                "                    let home=\"\";\n" +
                "                    for (let json of res.data) {\n" +
                "                        for (let sub of json.data) {\n" +
                "                            let name=json.sysPermissionName;\n" +
                "                            let subName=sub.sysPermissionName;\n" +
                "                            let data=sub.sysPermissionData;\n" +
                "                            if (subName.toLowerCase()===\"home\" || subName===\"主页\") {\n" +
                "                                continue;\n" +
                "                            }\n" +
                "                            if (leftData[name] == null) {\n" +
                "                                leftData[name]=[]\n" +
                "                            }\n" +
                "                            leftData[name].push({\"frameAuthorityName\":subName,\"frameAuthorityPath\":data});\n" +
                "                        }\n" +
                "                    }\n" +
                "                    $(\"#home-1\").attr(\"lay-attr\",home);\n" +
                "                    $(\"#home-1\").attr(\"lay-id\",home);\n" +
                "                    $(\"#home-2\").attr(\"src\",home);\n" +
                "                    $(\"#home-3\").attr(\"lay-href\",home);\n" +
                "                    gzb.appendNode(leftData);\n" +
                "                }\n" +
                "            });\n" +
                "            deleteToken=function () {\n" +
                "                gzb.get(exitUserApi, null, function (res) {\n" +
                "                    console.log(res)\n" +
                "                    location.reload();\n" +
                "                });\n" +
                "            }\n" +
                "            gzb.get(userInfoApi, null, function (res) {\n" +
                "                if (gzb.jsonVerify(res)) {\n" +
                "                    console.log(\"res1 \" , res)\n" +
                "                    $(\"#usersName\").text(res.data.sysUsersAcc)\n" +
                "                    $(\"#usersTab\").append(\"<dd style=\\\"text-align: center;\\\"><a>用户ID:\"+res.data.sysUsersId+\"</a></dd>\")\n" +
                "                    $(\"#usersTab\").append(\"<dd style=\\\"text-align: center;\\\"><a href=\\\"javascript:deleteToken();\\\">退出登录</a></dd>\")\n" +
                "                }\n" +
                "            });\n" +
                "            setInterval(function () {\n" +
                "                let url = localStorage[\"主页跳转\"]\n" +
                "                localStorage.removeItem(\"主页跳转\")\n" +
                "                if (url==null) {\n" +
                "                    return;\n" +
                "                }\n" +
                "                if (url==\"null\") {\n" +
                "                    return;\n" +
                "                }\n" +
                "                if (url.length > 0) {\n" +
                "                    console.log(\"跳转：\"+url)\n" +
                "                    location.href = url;\n" +
                "                }\n" +
                "            },1000)\n" +
                "\n" +
                "        }\n" +
                "        initIndex()\n" +
                "\n" +
                "    });\n" +
                "</script>\n" +
                "\n";
        String path = getHtmlPath();
        path = Tools.pathFormat(path);
        if (!new File(path + "/index.html").exists()) {
            saveFile(path + "/index.html", code, true);
        }
    }

    public void gen_list_html() {
        String code = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\""+Config.encoding+"\">\n" +
                "    <title>数据列表</title>\n" +
                "    <meta name=\"renderer\" content=\"webkit\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
                "    <meta name=\"viewport\"\n" +
                "          content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0\">\n" +
                "    <link rel=\"stylesheet\" href=\"/static/layuiadmin/layui/css/layui.css\" media=\"all\">\n" +
                "    <link rel=\"stylesheet\" href=\"/static/layuiadmin/style/admin.css\" media=\"all\">\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"layui-card\">\n" +
                "    <br>\n" +
                "    <form class=\"layui-form\" lay-filter=\"component-form-group\">\n" +
                "        <div id=\"but_gzb_001\">\n" +
                "        </div>\n" +
                "    </form>\n" +
                "\n" +
                "    <div id=\"but_gzb_002\"></div>\n" +
                "    <div class=\"layui-card-body\">\n" +
                "        <table class=\"layui-hide\" id=\"table_001\" lay-filter=\"table_001\"></table>\n" +
                "    </div>\n" +
                "</div>\n" +
                "<script src='/static/layuiadmin/layui/layui.all.js'></script>\n" +
                "<script src=\"/static/js/layui.gzb.js\"></script>\n" +
                "<script>\n" +
                "\n" +
                "initListPage()\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";
        String path = getHtmlPath();
        path = Tools.pathFormat(path);
        if (!new File(path + "/list.html").exists()) {
            saveFile(path + "/list.html", code, true);
        }

    }

    public void gen_save_html() {
        String code = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\""+Config.encoding+"\">\n" +
                "    <title>开启单元格编辑 - 数据表格</title>\n" +
                "    <meta name=\"renderer\" content=\"webkit\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
                "    <meta name=\"viewport\"\n" +
                "          content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0\">\n" +
                "    <link rel=\"stylesheet\" href=\"/static/layuiadmin/layui/css/layui.css\" media=\"all\">\n" +
                "    <link rel=\"stylesheet\" href=\"/static/layuiadmin/style/admin.css\" media=\"all\">\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"layui-card\">\n" +
                "\n" +
                "    <br>\n" +
                "    <form class=\"layui-form\" lay-filter=\"component-form-group\">\n" +
                "        <div id=\"save_input_gzb_c001\"></div>\n" +
                "        <div class=\"layui-form-item\">\n" +
                "            <label class=\"layui-form-label\">操作按钮</label>\n" +
                "            <div class=\"layui-input-inline\">\n" +
                "                <button type=\"button\" class=\"layui-btn layui-btn-danger layui-btn-radius\" id=\"but_001\">保存数据</button>\n" +
                "            </div>\n" +
                "            <div class=\"layui-input-inline\">\n" +
                "                <button type=\"button\" class=\"layui-btn layui-btn-warm layui-btn-radius\" id=\"but_003\">清空输入</button>\n" +
                "            </div>\n" +
                "            <div class=\"layui-input-inline\">\n" +
                "                <button type=\"button\" class=\"layui-btn layui-btn-normal layui-btn-radius\" id=\"but_002\">重置数据</button>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div id=\"save_input_gzb_c002\">\n" +
                "        </div>\n" +
                "    </form>\n" +
                "</div>\n" +
                "\n" +
                "<script src='/static/layuiadmin/layui/layui.all.js'></script>\n" +
                "<script src=\"/static/js/layui.gzb.js\"></script>\n" +
                "<script>\n" +
                "    initSave()\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";
        String path = getHtmlPath();
        path = Tools.pathFormat(path);
        if (!new File(path + "/save.html").exists()) {
            saveFile(path + "/save.html", code, true);
        }

    }

    public void gen_update_html() {
        String code = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\""+Config.encoding+"\">\n" +
                "    <title>修改数据</title>\n" +
                "    <meta name=\"renderer\" content=\"webkit\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
                "    <meta name=\"viewport\"\n" +
                "          content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0\">\n" +
                "    <link rel=\"stylesheet\" href=\"/static/layuiadmin/layui/css/layui.css\" media=\"all\">\n" +
                "    <link rel=\"stylesheet\" href=\"/static/layuiadmin/style/admin.css\" media=\"all\">\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"layui-card\">\n" +
                "\n" +
                "    <br>\n" +
                "    <form class=\"layui-form\" lay-filter=\"component-form-group\">\n" +
                "        <div id=\"save_input_gzb_c001\"></div>\n" +
                "        <div class=\"layui-form-item\">\n" +
                "            <label class=\"layui-form-label\">操作按钮</label>\n" +
                "            <div class=\"layui-input-inline\">\n" +
                "                <button type=\"button\" class=\"layui-btn layui-btn-danger layui-btn-radius\" id=\"but_001\">修改数据</button>\n" +
                "            </div>\n" +
                "            <div class=\"layui-input-inline\">\n" +
                "                <button type=\"button\" class=\"layui-btn layui-btn-warm layui-btn-radius\" id=\"but_003\">清空输入</button>\n" +
                "            </div>\n" +
                "            <div class=\"layui-input-inline\">\n" +
                "                <button type=\"button\" class=\"layui-btn layui-btn-normal layui-btn-radius\" id=\"but_002\">重置数据</button>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div id=\"save_input_gzb_c002\">\n" +
                "        </div>\n" +
                "    </form>\n" +
                "</div>\n" +
                "\n" +
                "<script src='/static/layuiadmin/layui/layui.all.js'></script>\n" +
                "<script src=\"/static/js/layui.gzb.js\"></script>\n" +
                "<script>\n" +
                "    initUpdate()\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";
        String path = getHtmlPath();
        path = Tools.pathFormat(path);
        if (!new File(path + "/update.html").exists()) {
            saveFile(path + "/update.html", code, true);
        }

    }

    public void gen_jsCode(String name, String id) {
        String path = getHtmlPath();
        path += "/js/edit/";
        new File(path).mkdirs();
        path = Tools.pathFormat(path);

        String code = "console.log(\"load:js/edit/" + name + ".js\")";
        if (!new File(path + "/" + name + ".js").exists()) {
            saveFile(path + "/" + name + ".js", code, true);
        }

        code = "console.log(\"load:js/" + name + ".js\")\n" +
                "gzb.base = \"/system/v1.0.0/\";\n" +
                "gzb.entityName = \"" + name + "\";\n" +
                "gzb.entityIdName = \"" + id + "\";\n";

        path = getHtmlPath();
        path += "/js/";
        new File(path).mkdirs();
        path = Tools.pathFormat(path);
        saveFile(path + "/" + name + ".js", code, true);
    }
}