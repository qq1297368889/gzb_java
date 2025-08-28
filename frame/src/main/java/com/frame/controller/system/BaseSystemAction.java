package com.frame.controller.system;

import com.frame.dao.*;
import com.frame.dao.impl.*;
import com.frame.entity.*;
import gzb.entity.SqlTemplate;
import gzb.frame.annotation.*;
import gzb.frame.db.DataBase;
import gzb.frame.db.DataBaseFactory;
import gzb.tools.*;
import gzb.tools.img.PicUtils;
import gzb.tools.log.Log;
import gzb.tools.session.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Controller
@RequestMapping(value = "/system/v1.0.0/", header = "content-type:application/json;charset=UTF-8")
public class BaseSystemAction extends BaseAction {


    //key.system.login.image.code  验证码储存key
    //key.system.login.info  登录信息储存KEY
    @Resource()
    public Log log;


    private int loginInfo(SysUsers sysUsers, String mac, String ip, DateTime dateTime, String desc, Session session) throws Exception {
        SysUsersLoginLog sysUsersLoginLog = new SysUsersLoginLog();
        sysUsersLoginLog.setSysUsersLoginLogDesc(desc)
                .setSysUsersLoginLogIp(ip)
                .setSysUsersLoginLogTime(dateTime.toString())
                .setSysUsersLoginLogUid(sysUsers.getSysUsersId())
                .setSysUsersLoginLogToken(session.getId())
                .setSysUsersLoginLogMac(mac);
        SqlTemplate sqlTemplate = dataBase.toSave(sysUsersLoginLog);
        return dataBase.runSqlAsync(sqlTemplate.getSql(), sqlTemplate.getObjects());
    }


    /// /system/v1.0.0/image/code
    @GetMapping(value = "image/code")
    public Object image_code(Session session, HttpServletResponse response) {
        try {
            String code= Tools.getPictureCode2(response);
            if (code==null||code.length()<1) {
                session.put(Config.get("key.system.login.image.code"), "123456");
            }else{
                session.put(Config.get("key.system.login.image.code"), code);
            }
        } catch (Exception e) {
            session.put(Config.get("key.system.login.image.code"), "123456");
        }
        return null;
    }

    /// 登陆
    /// /system/v1.0.0/image/code
    /// /system/v1.0.0/login?sysUsersAcc=admin&sysUsersPwd=admin&code=vkvss
    @GetMapping(value = "login")
    public Object login(JSONResult result, SysUsers sysUsers, String mac, String code, Session session, HttpServletRequest request) throws Exception {
        if (sysUsers == null || sysUsers.toString().equals("{}") || sysUsers.getSysUsersAcc() == null || sysUsers.getSysUsersPwd() == null) {
            return result.fail("login 请输入账号密码");
        }
        if (code == null) {
            return result.fail("login 请输入 验证码");
        }
        Object code_s = session.delete(Config.get("key.system.login.image.code"));
        if (code_s == null) {
            return result.fail("login 请先获取验证码");
        }
        if (!code_s.toString().equals(code)) {
            return result.fail("login 验证码输入错误");
        }
        SqlTemplate sqlTemplate = dataBase.toSelect(sysUsers, 1, 2, null, null);
        List<GzbMap> list = dataBase.selectGzbMap(sqlTemplate.getSql(), sqlTemplate.getObjects());
        if (list.size() != 1) {
            return result.fail("login 账号不存在或不正确");
        }
        SysUsers sysUsers1 = new SysUsers(list.get(0));
        String ip = Tools.getIpAddress(request);
        DateTime dateTime = new DateTime();
        if (sysUsers1.getSysUsersStatus() < 1) {
            session.delete();
            loginInfo(sysUsers1, mac, ip, dateTime, "用户登录-失败", session);
            return result.fail("login 账号状态异常");
        }

        loginInfo(sysUsers1, mac, ip, dateTime, "用户登录-成功", session);
        session.put(Config.get("key.system.login.info"), sysUsers1.toString());
        return result.success("登陆成功", "{\"token\":\"" + session.getId() + "\"}");
    }

    /// 注册 注册后的用户 归属权限组  默认权限_注册_限时用户
    /// /system/v1.0.0/image/code
    /// /system/v1.0.0/reg5?sysUsersAcc=admin&sysUsersPwd=admin&code=vkvss
    @GetMapping(value = "reg5")
    public Object reg5(JSONResult result, SysUsers sysUsers, String mac, String code, Session session, HttpServletRequest request) throws Exception {
        if (sysUsers == null || sysUsers.toString().equals("{}") || sysUsers.getSysUsersAcc() == null || sysUsers.getSysUsersPwd() == null) {
            return result.fail("注册账号 请输入账号密码");
        }
        if (code == null) {
            return result.fail("注册账号 请输入 验证码");
        }
        Object code_s = session.delete(Config.get("key.system.login.image.code"));
        if (code_s == null) {
            return result.fail("注册账号 请先获取验证码");
        }
        if (!code_s.toString().equals(code)) {
            return result.fail("注册账号 验证码输入错误");
        }

        SqlTemplate sqlTemplate = dataBase.toSelect(new SysUsers().setSysUsersId(sysUsers.getSysUsersSup()), 1, 2, null, null);
        List<GzbMap> list = dataBase.selectGzbMap(sqlTemplate.getSql(), sqlTemplate.getObjects());
        if (list.size() != 1) {
            return result.fail("注册账号 上级账号不存在");
        }
        SysUsers sysUsers0 = new SysUsers(list.get(0));

        SqlTemplate sqlTemplate2 = dataBase.toSelect(new SysUsers().setSysUsersAcc(sysUsers.getSysUsersAcc()), 1, 2, null, null);
        List<GzbMap> list2 = dataBase.selectGzbMap(sqlTemplate2.getSql(), sqlTemplate2.getObjects());
        if (list2.size() == 1) {
            return result.fail("注册账号 账号已存在,请更换");
        }
        DateTime dateTime = new DateTime();
        SysUsers sysUsers1 = new SysUsers();
        sysUsers1.setSysUsersAcc(sysUsers.getSysUsersAcc());
        sysUsers1.setSysUsersPwd(sysUsers.getSysUsersPwd());
        sysUsers1.setSysUsersPrice(0L)
                .setSysUsersDesc("自助注册")
                .setSysUsersMail(sysUsers.getSysUsersMail())
                .setSysUsersType(5L)
                .setSysUsersStatus(1L)
                .setSysUsersSup(sysUsers0.getSysUsersId())
                .setSysUsersGroup(-100L)
                .setSysUsersRegTime(dateTime.toString())
                .setSysUsersPhone(sysUsers.getSysUsersPhone());

        String ip = Tools.getIpAddress(request);
        SqlTemplate sqlTemplate3 = dataBase.toSave(sysUsers1);
        dataBase.runSql(sqlTemplate3.getSql(), sqlTemplate3.getObjects());

        loginInfo(sysUsers1, mac, ip, dateTime, "注册账号-成功", session);
        session.put(Config.get("key.system.login.info"), sysUsers1.toString());
        return result.success("注册账号 成功", "{\"token\":\"" + session.getId() + "\"}");
    }

    /// 退出登录 删除会话
    /// /system/v1.0.0/exit/user
    @DecoratorOpen
    @GetMapping(value = "exit/user")
    public Object exitUser(Session session, JSONResult res) {
        session.delete();
        return res.success("登陆已失效");
    }

    /// 获取当前登录用户的 信息
    /// /system/v1.0.0/read/user/info
    @DecoratorOpen
    @GetMapping(value = "read/user/info")
    public Object readUserInfo(Session session, JSONResult res) {
        String jsonString = session.getString(Config.get("key.system.login.info"));
        log.d(jsonString);
        return res.success("获取成功", new SysUsers(jsonString).toString());
    }

    /// 更新权限列表 从当前http端点映射
    /// /system/v1.0.0/update/permission
    @DecoratorOpen
    @GetMapping(value = "update/updatePermissionAndMapping")
    public Object updatePermissionAndMapping(JSONResult res,Long sysGroupId,SysUsers sysUsers) throws Exception {
        System.out.println(sysGroupId);
        System.out.println(sysUsers);
        if (sysUsers==null || sysUsers.getSysUsersType() != 4) {
            return res.fail("没有合适权限操作");
        }
        if (sysGroupId==null) {
            return res.fail("必填参数为空");
        }

        return res.success("权限已更新，当前权限数量为：" + Tools.updateMapping(dataBase,null,sysGroupId) + "个");
    }

    /// /system/v1.0.0/authorize/permission?pid=&gid=&type=
    @DecoratorOpen
    @PostMapping(value = "authorize/permission")
    public Object authorize(JSONResult res, Long[] pid, Long gid,Long type, SysUsers sysUsers) throws Exception {
        if (pid == null || pid.length < 1 || gid == null) {
            return res.fail("必填参数不允许为空");
        }
        DateTime dateTime = new DateTime();
        for (Long id : pid) {
            List<GzbMap> list2 = dataBase.selectGzbMap(
                    "select * from sys_group_permission where sys_group_permission_gid = ? and sys_group_permission_pid = ?",
                    new Object[]{gid, id});
            if (list2.size() == 1) {
                if (type==0L) {
                    SysGroupPermission sysGroupPermission = new SysGroupPermission();
                    sysGroupPermission.setSysGroupPermissionId(list2.get(0).getLong("sysGroupPermissionId"));
                    SqlTemplate sqlTemplate = dataBase.toDelete(sysGroupPermission);
                    dataBase.runSql(sqlTemplate.getSql(), sqlTemplate.getObjects());
                }
                continue;
            }
            SysGroupPermission sysGroupPermission = new SysGroupPermission();
            sysGroupPermission.setSysGroupPermissionGid(gid).setSysGroupPermissionPid(id)
                    .setSysGroupPermissionTime(dateTime.toString());
            SqlTemplate sqlTemplate = dataBase.toSave(sysGroupPermission);
            dataBase.runSql(sqlTemplate.getSql(), sqlTemplate.getObjects());
        }
        return res.success("授权成功");
    }


    /// 获取当前登录用户的权限列表
    /// /system/v1.0.0/read/permission
    @DecoratorOpen
    @GetMapping(value = "read/permission")
    public Object readPermission(JSONResult res,
                                 Session session,
                                 Long uid) throws Exception {
        SysUsers sysUsers0 = new SysUsers(session.getString(Config.get("key.system.login.info")));
        SysUsers sysUsers=null;
        if (uid != null) {
            List<GzbMap> list1=null;
            if (sysUsers0.getSysUsersType() == 4){
                list1 = dataBase.selectGzbMap("select * from sys_users where sys_users_id = ?",
                        new Object[]{uid});
            }else{
                list1 = dataBase.selectGzbMap("select * from sys_users where sys_users_id = ? and sys_users_sup = ?",
                        new Object[]{uid, sysUsers0.getSysUsersId()});
            }
            if (list1.size() != 1) {
                return res.fail("无权干预该用户权限分配");
            }
            sysUsers = new SysUsers(list1.get(0));
        }else{
            sysUsers=sysUsers0;
        }
        List<GzbMap> list1 = null;
        if (sysUsers.getSysUsersType() == 4) {
            list1 = dataBase.selectGzbMap(
                    "SELECT p.* FROM sys_permission p where sys_permission_type = ? and p.sys_permission_sup = ? " +
                            "ORDER BY p.sys_permission_sort DESC",
                    new Object[]{1,0}
            );
            log.d("list1",list1.toString());
            List<SysPermission> list = new ArrayList<>(list1.size());
            for (GzbMap gzbMap : list1) {
                SysPermission sysPermission = new SysPermission(gzbMap);
                log.d("sysPermission",sysPermission.toString());
                List<GzbMap> list2 = dataBase.selectGzbMap(
                        "select sys_permission.* from sys_permission where sys_permission.sys_permission_sup = ?",
                        new Object[]{sysPermission.getSysPermissionId()});
                sysPermission.setList(list2);
                list.add(sysPermission);
            }

            return res.success("权限查询成功", list);
        }else{
            list1 = dataBase.selectGzbMap(
                    "SELECT p.* FROM sys_permission p " +
                            "WHERE p.sys_permission_id IN (" +
                            "    SELECT gp.sys_group_permission_pid " +
                            "    FROM sys_group_permission gp " +
                            "    WHERE gp.sys_group_permission_gid = (" +
                            "        SELECT g.sys_group_id " +
                            "        FROM sys_group g " +
                            "        WHERE g.sys_group_id = (" +
                            "            SELECT u.sys_users_group " +
                            "            FROM sys_users u " +
                            "            WHERE u.sys_users_id = ? " +
                            "            AND u.sys_users_status > ?" +
                            "        )" +
                            "    )" +
                            ") AND p.sys_permission_sup = ? " +
                            "ORDER BY p.sys_permission_sort DESC",
                    new Object[]{sysUsers.getSysUsersId(), 0, 0}
            );
        }


        List<SysPermission> list = new ArrayList<>(list1.size());
        for (GzbMap gzbMap : list1) {
            SysPermission sysPermission = new SysPermission(gzbMap);
            List<GzbMap> list2 = dataBase.selectGzbMap(
                    "select sys_permission.* from sys_permission where " +
                            "sys_permission.sys_permission_id in " +
                            "(select sys_group_permission_pid from sys_group_permission where sys_group_permission_gid = " +
                            "(select sys_users.sys_users_group from sys_users where sys_users_id = ? and sys_users_status > ?" +
                            ") " +
                            ") " +
                            "and sys_permission.sys_permission_sup = ?",
                    new Object[]{sysUsers.getSysUsersId(), 0, sysPermission.getSysPermissionId()});
            sysPermission.setList(list2);
            list.add(sysPermission);
        }

        return res.success("权限查询成功", list);
    }
    @DecoratorOpen
    @GetMapping(value = "read/permission/all")
    public Object readPermissionAll(JSONResult res,
                                 Session session,
                                 Long gid) throws Exception {
        SysUsers sysUsers0 = new SysUsers(session.getString(Config.get("key.system.login.info")));
        if (sysUsers0.getSysUsersType() != 4){
            log.w("越权访问 ",gid,sysUsers0);
            return null;
        }
        List<GzbMap> list1 = null;
        if (gid!=null) {
            list1 = dataBase.selectGzbMap(
                    "select sys_permission.* from sys_permission where sys_permission.sys_permission_id in " +
                            "(select sys_group_permission.sys_group_permission_pid from sys_group_permission where " +
                            "sys_group_permission.sys_group_permission_gid = ?) and sys_permission_sup = ?" ,
                    new Object[]{gid,0});
        }else{
            list1 = dataBase.selectGzbMap(
                    "select sys_permission.* from sys_permission where sys_permission_sup = ? order by sys_permission_sort desc",
                    new Object[]{0});
        }
        List<SysPermission> list = new ArrayList<>(list1.size());
        for (GzbMap gzbMap : list1) {
            list.add(new SysPermission(gzbMap));
        }
        for (SysPermission sysPermission : list) {
            if (!sysPermission.getSysPermissionType().toString().equals("1") ) {
                continue;
            }
            List<GzbMap> list2=null;
            if (gid!=null) {
                  list2 = dataBase.selectGzbMap(
                        "select sys_permission.* from sys_permission where " +
                                "sys_permission.sys_permission_id in " +
                                "(select sys_group_permission_pid from sys_group_permission where sys_group_permission_gid = ?" +
                                ") " +
                                "and sys_permission.sys_permission_sup = ?",
                        new Object[]{gid,sysPermission.getSysPermissionId()});
            }else{
                list2 = dataBase.selectGzbMap(
                        "select sys_permission.* from sys_permission where sys_permission.sys_permission_sup = ?",
                        new Object[]{sysPermission.getSysPermissionId()});
            }
            sysPermission.setList(list2);
            log.d("list2",list2);
        }

        return res.success("权限查询成功", list);
    }

    /// 获取某个表的映射信息
    /// read/mapping?key=sys_Users
    @DecoratorOpen
    @GetMapping(value = "read/mapping")
    public Object readMapping(JSONResult res, Session session, String key) throws Exception {
        SysUsers sysUsers = new SysUsers(session.getString(Config.get("key.system.login.info")));
        if (key == null) {
            return res.fail("参数为空");
        }
        key = Tools.lowStr_x(Tools.lowStr_hump(key));
        List<GzbMap> listSysGroup = dataBase.selectGzbMap("select * from sys_group where sys_group_id = ?"
                ,new Object[]{sysUsers.getSysUsersGroup()});
        if (listSysGroup.size() != 1) {
            return res.fail("组信息查询失败");
        }
        Long sysGroupId=listSysGroup.get(0).getLong("sysGroupId");
        //查询表信息
        List<GzbMap> listSysGroupTable = dataBase.selectGzbMap("select sys_group_table.* from sys_group_table where " +
                        "sys_group_table.sys_group_table_key = ? and " +
                        "sys_group_table.sys_group_table_gid = ?"
                ,new Object[]{key,sysGroupId});
        if (listSysGroupTable.size() != 1) {
            return res.fail("表映射信息获取失败");
        }
        //查询列信息
        List<GzbMap> listSysGroupColumn = dataBase.selectGzbMap("select sys_group_column.* from sys_group_column where " +
                        "sys_group_column.sys_group_column_key = ? and " +
                        "sys_group_column.sys_group_column_gid = ?"
                ,new Object[]{key,sysGroupId});
        for (GzbMap gzbMap : listSysGroupColumn) {
            //查询映射信息
            List<GzbMap> listSysMapping = dataBase.selectGzbMap("select sys_mapping.* from sys_mapping where " +
                            "sys_mapping.sys_mapping_val = ? and " +
                            "sys_mapping.sys_mapping_key = ?"
                    ,new Object[]{gzbMap.get("sysGroupColumnName"),key});
            if (!listSysMapping.isEmpty()) {
                gzbMap.set("mapping",listSysMapping.get(0));
                for (GzbMap map : listSysMapping) {
                    String sysMappingSelect = map.getString("sysMappingSelect");//sys_mapping_select
                    if (sysMappingSelect!=null) {
                        if (sysMappingSelect.startsWith("request:")) {
                            continue;
                        }
                        List<GzbMap> listOption=null;
                        if (sysMappingSelect.startsWith("key:")) {
                            listOption=dataBase.selectGzbMap("select sys_option_title,sys_option_value from sys_option where sys_option_key = ?",
                                    new Object[]{sysMappingSelect.replace("key:", "")});
                        }
                        if (sysMappingSelect.toLowerCase().contains("select")) {
                            listOption=dataBase.selectGzbMap(sysMappingSelect);
                        }
                        map.put("sysMappingSelect",listOption);
                    }
                }
            }
        }
        if (!listSysGroupColumn.isEmpty()) {
            listSysGroupTable.get(0).set("sysGroupColumn",listSysGroupColumn);
            listSysGroup.get(0).set("sysGroup",listSysGroupTable.get(0));
        }

        return res.success("权限查询完成",listSysGroup);
    }


    /// upload?filesys_Users
    @PostMapping(value = "upload")
    public Object upload(JSONResult res, File file) throws Exception {
        if (file == null) {
            return res.fail("未检测到上传的文件");
        }
        log.d(file);
        String path = file.getPath().replace("\\", "/");
        String[] arr1 = path.split("/");
        if (arr1.length < 3) {
            return res.fail("本地上传目录设置错误,请联系管理员[1001]");
        }
        String md5 = arr1[arr1.length - 2];
        StringBuilder basePath = new StringBuilder();
        int open = 0;
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i].equals("resources")) {
                open = 1;
            }
            if (open == 1) {
                if (basePath.length() > 0 && arr1[i].equals("resources")) {
                    basePath = new StringBuilder();
                }
                basePath.append(arr1[i]);
                if (i < arr1.length - 1) {
                    basePath.append("/");
                }
            }
        }
        if (basePath.length() < 1) {
            return res.fail("本地上传目录设置错误,请联系管理员[1002]");
        }
        List<SysFile> list = new ArrayList<>();
        SqlTemplate sqlTemplate1 = dataBase.toSelect(new SysFile().setSysFileMd5(md5), 1, 2, null, null);
        List<GzbMap> listGzbMap1 = dataBase.selectGzbMap(sqlTemplate1.getSql(), sqlTemplate1.getObjects());
        SysFile sysFile = null;
        if (listGzbMap1.size() != 1) {
            sysFile = new SysFile();
            String contentType = URLConnection.guessContentTypeFromName(file.getName());
            sysFile.setSysFileMd5(md5)
                    .setSysFilePath(basePath.toString())
                    .setSysFileTime(new DateTime().toString())
                    .setSysFileType(contentType);
            sqlTemplate1 = dataBase.toSave(sysFile);
            dataBase.runSql(sqlTemplate1.getSql(), sqlTemplate1.getObjects());

        } else {
            sysFile = new SysFile(listGzbMap1.get(0));
        }
        list.add(sysFile);
        return res.success("上传完成", list);
    }

    @DecoratorOpen
    @GetMapping(value = "read/file")
    public Object readFile0(JSONResult res, SysFile sysFile, Boolean b64, Double t, HttpServletResponse response, HttpServletRequest request) throws Exception {
        if (sysFile == null || sysFile.toString().equals("{}")) {
            return res.fail("必要参数未填写[1000]");
        }
        if (sysFile.getSysFileId() == null && sysFile.getSysFilePath() == null && sysFile.getSysFileMd5() == null) {
            return res.fail("必要参数未填写[1001]");
        }
        SqlTemplate sqlTemplate1 = dataBase.toSelect(sysFile, 1, 2, null, null);
        List<GzbMap> listGzbMap1 = dataBase.selectGzbMap(sqlTemplate1.getSql(), sqlTemplate1.getObjects());
        if (listGzbMap1.size() != 1) {
            return res.fail("文件不存在或无权限访问[1000]");
        }
        sysFile = new SysFile(listGzbMap1.get(0));
        String path = Config.get("key.system.upload.dir", Config.tmpPath());
        String filePath = path + "/" + sysFile.getSysFilePath();
        File file = new File(filePath);
        if (!file.exists() || !file.canRead()) {
            return res.fail("文件不存在或无权限访问[1001]");
        }

        // 设置内容类型
        String contentType = sysFile.getSysFileType();
        if (contentType != null) {
            response.setContentType(contentType);
        } else {
            response.setContentType("application/octet-stream");
        }

        // 读取文件内容
        byte[] bytes = Tools.fileReadByte(file);
        if (bytes == null || bytes.length < 1) {
            return res.fail("文件不存在或无权限访问[1002]");
        }

        // 压缩图片质量范围 0.1-1.0
        if (t != null && t > 0 && t <= 1) {
            bytes = PicUtils.resize(bytes, t);
        }

        // 返回Base64格式
        if (b64 != null && b64) {
            return Tools.textBase64Encoder(bytes);
        }

        // 直接返回文件流
        try (OutputStream os = response.getOutputStream()) {
            os.write(bytes);
        }
        response.setHeader("Content-Length", String.valueOf(bytes.length));
        return null;
    }


    SysFileDao sysFileDao=new SysFileDaoImpl();
    SysGroupColumnDao sysGroupColumnDao=new SysGroupColumnDaoImpl();
    SysGroupDao sysGroupDao=new SysGroupDaoImpl();
    SysGroupPermissionDao sysGroupPermissionDao=new SysGroupPermissionDaoImpl();
    SysGroupTableDao sysGroupTableDao=new SysGroupTableDaoImpl();
    SysLogDao sysLogDao=new SysLogDaoImpl();
    SysMappingDao sysMappingDao=new SysMappingDaoImpl();
    SysOptionDao sysOptionDao=new SysOptionDaoImpl();
    SysPermissionDao sysPermissionDao=new SysPermissionDaoImpl();
    SysUsersLoginLogDao sysUsersLoginLogDao=new SysUsersLoginLogDaoImpl();

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysUsers的参数
     * /system/v1.0.0/sysUsers/find?参数同下,只是仅接受查询结果为1条的情况
     */
    @DecoratorOpen
    @GetMapping(value = "/sysUsers/find")
    public Object find(JSONResult result, SysUsers sysUsers0, Map<String, Object> req) throws Exception {
        Map<String, Object> re2 = Tools.mapArrayToNoArray(req);
        SysUsers sysUsers = new SysUsers(re2);
        if (sysUsers.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }
        SqlTemplate sqlTemplate1 = dataBase.toSelect(sysUsers, 1, 2, null, null);
        List<GzbMap> listGzbMap1 = dataBase.selectGzbMap(sqlTemplate1.getSql(), sqlTemplate1.getObjects());
        if (listGzbMap1.size() != 1) {
            return result.fail("文件不存在或无权限访问[1000]");
        }
        SysUsers sysUsers1 = new SysUsers(listGzbMap1.get(0));
        return result._data(sysUsers1.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysUsers的参数
     * /system/v1.0.0/sysUsers/list?参数同下,只是是根据实体类查询
     */
    @DecoratorOpen
    @GetMapping("/sysUsers/list")
    public Object list(JSONResult result, SysUsers sysUsers0, String sortField, String sortType, Integer page, Integer size, Map<String, Object> req) throws Exception {
        Map<String, Object> re2 = Tools.mapArrayToNoArray(req);
        SysUsers sysUsers = new SysUsers(re2);
        SqlTemplate sqlTemplate1 = dataBase.toSelect(sysUsers, page, size * 100, sortField, sortType);
        List<GzbMap> listGzbMap1 = dataBase.selectGzbMap(sqlTemplate1.getSql(), sqlTemplate1.getObjects());
        return result.paging(listGzbMap1, page, size);
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysUsers的参数
     * /system/v1.0.0/sysUsers/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     */
    @DecoratorOpen
    @GetMapping("/sysUsers/query")
    public Object query(JSONResult result, SysUsers sysUsers0, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = dataBase.toSelect("sys_users", field, symbol, value, montage, sortField, sortType);
        List<GzbMap> listGzbMap1 = dataBase.selectGzbMap(sqlTemplate.getSql(), sqlTemplate.getObjects());
        return result.paging(listGzbMap1, page, limit);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysUsers的参数
     * /system/v1.0.0/sysUsers/save
     */
    @DecoratorOpen
    @PostMapping("/sysUsers/save")
    public Object save(JSONResult result, SysUsers sysUsers0, Map<String, Object> req) throws Exception {
        Map<String, Object> re2 = Tools.mapArrayToNoArray(req);
        SysUsers sysUsers = new SysUsers(re2);
        if (sysUsers.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        SqlTemplate sqlTemplate = dataBase.toSave(sysUsers);
        if (dataBase.runSql(sqlTemplate.getSql(), sqlTemplate.getObjects()) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysUsers的参数
     * /system/v1.0.0/sysUsers/update?sysUsersId=
     */
    @DecoratorOpen
    @PostMapping("/sysUsers/update")
    public Object update(JSONResult result, SysUsers sysUsers0, Map<String, Object> req) throws Exception {
        Map<String, Object> re2 = Tools.mapArrayToNoArray(req);
        SysUsers sysUsers = new SysUsers(re2);
        if (sysUsers.toString().equals("{}") || sysUsers.getSysUsersId() == null) {
            return result.fail("update 输入参数错误");
        }
        SqlTemplate sqlTemplate = dataBase.toUpdate(sysUsers);
        if (dataBase.runSql(sqlTemplate.getSql(), sqlTemplate.getObjects()) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 sysUsersId
     * /system/v1.0.0/sysUsers/delete?sysUsersId=
     */
    @DecoratorOpen
    @DeleteMapping("/sysUsers/delete")
    public Object delete(JSONResult result, SysUsers sysUsers) throws Exception {
        if (sysUsers == null || sysUsers.toString().equals("{}") || sysUsers.getSysUsersId() == null) {
            return result.fail("delete 输入参数错误");
        }
        SqlTemplate sqlTemplate = dataBase.toDelete(sysUsers);
        if (dataBase.runSql(sqlTemplate.getSql(), sqlTemplate.getObjects()) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 sysUsersId
     * /system/v1.0.0/sysUsers/deleteAll?sysUsersId=
     */
    @DecoratorOpen
    @GetMapping("/sysUsers/deleteAll")
    public Object deleteAll(JSONResult result, Long[] sysUsersId) throws Exception {
        if (sysUsersId == null || sysUsersId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num = 0;
        for (Long aLong : sysUsersId) {
            SqlTemplate sqlTemplate = dataBase.toDelete(new SysUsers().setSysUsersId(aLong));
            if (dataBase.runSql(sqlTemplate.getSql(), sqlTemplate.getObjects()) > 0) {
                num++;
            }
        }
        return result.success("成功删除[" + num + "]条数据");
    }


    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysFile的参数
     * /system/v1.0.0/sysFile/find?参数同下,只是仅接受查询结果为1条的情况
     */
    @DecoratorOpen
    @GetMapping(value = "/sysFile/find")
    public Object findSysFile(JSONResult result, SysFile sysFile) throws Exception {
        if (sysFile == null || sysFile.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        SysFile sysFile1 = sysFileDao.find(sysFile);
        if (sysFile1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result._data(sysFile1.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysFile的参数
     * /system/v1.0.0/sysFile/list?参数同下,只是是根据实体类查询
     */
    @DecoratorOpen
    @GetMapping("/sysFile/list")
    public Object listSysFile(JSONResult result, SysFile sysFile, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return sysFileDao.queryPage(sysFile, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysFile的参数
     * /system/v1.0.0/sysFile/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     */
    @DecoratorOpen
    @GetMapping("/sysFile/query")
    public Object querySysFile(JSONResult result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = sysFileDao.getDataBase().toSelect("sys_file", field, symbol, value, montage, sortField, sortType);
        return sysFileDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysFile的参数
     * /system/v1.0.0/sysFile/save
     */
    @DecoratorOpen
    @PostMapping("/sysFile/save")
    public Object saveSysFile(JSONResult result, SysFile sysFile) throws Exception {
        if (sysFile == null || sysFile.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (sysFileDao.save(sysFile) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysFile的参数
     * /system/v1.0.0/sysFile/update?sysFileId=
     */
    @DecoratorOpen
    @PostMapping("/sysFile/update")
    public Object updateSysFile(JSONResult result, SysFile sysFile) throws Exception {
        if (sysFile == null || sysFile.toString().equals("{}") || sysFile.getSysFileId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (sysFileDao.update(sysFile) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 sysFileId
     * /system/v1.0.0/sysFile/delete?sysFileId=
     */
    @DecoratorOpen
    @DeleteMapping("/sysFile/delete")
    public Object deleteSysFile(JSONResult result, SysFile sysFile) throws Exception {
        if (sysFile == null || sysFile.toString().equals("{}") || sysFile.getSysFileId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (sysFileDao.delete(sysFile) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 sysFileId
     * /system/v1.0.0/sysFile/deleteAll?sysFileId=
     */
    @DecoratorOpen
    @GetMapping("/sysFile/deleteAll")
    public Object deleteAllSysFile(JSONResult result, Long[] sysFileId) throws Exception {
        if (sysFileId == null || sysFileId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num = 0;
        SysFile frame = new SysFile();
        for (Long aLong : sysFileId) {
            sysFileDao.delete(frame.setSysFileId(aLong));
            num++;
        }
        return result.success("成功删除[" + num + "]条数据");
    }


    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysGroupColumn的参数
     * /system/v1.0.0/sysGroupColumn/find?参数同下,只是仅接受查询结果为1条的情况
     */
    @DecoratorOpen
    @GetMapping(value = "/sysGroupColumn/find")
    public Object findSysGroupColumn(JSONResult result, SysGroupColumn sysGroupColumn) throws Exception {
        if (sysGroupColumn == null || sysGroupColumn.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        SysGroupColumn sysGroupColumn1 = sysGroupColumnDao.find(sysGroupColumn);
        if (sysGroupColumn1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result._data(sysGroupColumn1.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysGroupColumn的参数
     * /system/v1.0.0/sysGroupColumn/list?参数同下,只是是根据实体类查询
     */
    @DecoratorOpen
    @GetMapping("/sysGroupColumn/list")
    public Object listSysGroupColumn(JSONResult result, SysGroupColumn sysGroupColumn, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return sysGroupColumnDao.queryPage(sysGroupColumn, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysGroupColumn的参数
     * /system/v1.0.0/sysGroupColumn/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     */
    @DecoratorOpen
    @GetMapping("/sysGroupColumn/query")
    public Object querySysGroupColumn(JSONResult result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = sysGroupColumnDao.getDataBase().toSelect("sys_group_column", field, symbol, value, montage, sortField, sortType);
        return sysGroupColumnDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysGroupColumn的参数
     * /system/v1.0.0/sysGroupColumn/save
     */
    @DecoratorOpen
    @PostMapping("/sysGroupColumn/save")
    public Object saveSysGroupColumn(JSONResult result, SysGroupColumn sysGroupColumn) throws Exception {
        if (sysGroupColumn == null || sysGroupColumn.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (sysGroupColumnDao.save(sysGroupColumn) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysGroupColumn的参数
     * /system/v1.0.0/sysGroupColumn/update?sysGroupColumnId=
     */
    @DecoratorOpen
    @PostMapping("/sysGroupColumn/update")
    public Object updateSysGroupColumn(JSONResult result, SysGroupColumn sysGroupColumn) throws Exception {
        if (sysGroupColumn == null || sysGroupColumn.toString().equals("{}") || sysGroupColumn.getSysGroupColumnId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (sysGroupColumnDao.update(sysGroupColumn) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 sysGroupColumnId
     * /system/v1.0.0/sysGroupColumn/delete?sysGroupColumnId=
     */
    @DecoratorOpen
    @DeleteMapping("/sysGroupColumn/delete")
    public Object deleteSysGroupColumn(JSONResult result, SysGroupColumn sysGroupColumn) throws Exception {
        if (sysGroupColumn == null || sysGroupColumn.toString().equals("{}") || sysGroupColumn.getSysGroupColumnId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (sysGroupColumnDao.delete(sysGroupColumn) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 sysGroupColumnId
     * /system/v1.0.0/sysGroupColumn/deleteAll?sysGroupColumnId=
     */
    @DecoratorOpen
    @GetMapping("/sysGroupColumn/deleteAll")
    public Object deleteAllSysGroupColumn(JSONResult result, Long[] sysGroupColumnId) throws Exception {
        if (sysGroupColumnId == null || sysGroupColumnId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num = 0;
        SysGroupColumn frame = new SysGroupColumn();
        for (Long aLong : sysGroupColumnId) {
            sysGroupColumnDao.delete(frame.setSysGroupColumnId(aLong));
            num++;
        }
        return result.success("成功删除[" + num + "]条数据");
    }


    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysGroup的参数
     * /system/v1.0.0/sysGroup/find?参数同下,只是仅接受查询结果为1条的情况
     */
    @DecoratorOpen
    @GetMapping(value = "/sysGroup/find")
    public Object findSysGroup(JSONResult result, SysGroup sysGroup) throws Exception {
        if (sysGroup == null || sysGroup.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        SysGroup sysGroup1 = sysGroupDao.find(sysGroup);
        if (sysGroup1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result._data(sysGroup1.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysGroup的参数
     * /system/v1.0.0/sysGroup/list?参数同下,只是是根据实体类查询
     */
    @DecoratorOpen
    @GetMapping("/sysGroup/list")
    public Object listSysGroup(JSONResult result, SysGroup sysGroup, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return sysGroupDao.queryPage(sysGroup, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysGroup的参数
     * /system/v1.0.0/sysGroup/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     */
    @DecoratorOpen
    @GetMapping("/sysGroup/query")
    public Object querySysGroup(JSONResult result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = sysGroupDao.getDataBase().toSelect("sys_group", field, symbol, value, montage, sortField, sortType);
        return sysGroupDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysGroup的参数
     * /system/v1.0.0/sysGroup/save
     */
    @DecoratorOpen
    @PostMapping("/sysGroup/save")
    public Object saveSysGroup(JSONResult result, SysGroup sysGroup) throws Exception {
        if (sysGroup == null || sysGroup.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (sysGroupDao.save(sysGroup) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysGroup的参数
     * /system/v1.0.0/sysGroup/update?sysGroupId=
     */
    @DecoratorOpen
    @PostMapping("/sysGroup/update")
    public Object updateSysGroup(JSONResult result, SysGroup sysGroup) throws Exception {
        if (sysGroup == null || sysGroup.toString().equals("{}") || sysGroup.getSysGroupId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (sysGroupDao.update(sysGroup) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 sysGroupId
     * /system/v1.0.0/sysGroup/delete?sysGroupId=
     */
    @DecoratorOpen
    @DeleteMapping("/sysGroup/delete")
    public Object deleteSysGroup(JSONResult result, SysGroup sysGroup) throws Exception {
        if (sysGroup == null || sysGroup.toString().equals("{}") || sysGroup.getSysGroupId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (sysGroupDao.delete(sysGroup) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 sysGroupId
     * /system/v1.0.0/sysGroup/deleteAll?sysGroupId=
     */
    @DecoratorOpen
    @GetMapping("/sysGroup/deleteAll")
    public Object deleteAllSysGroup(JSONResult result, Long[] sysGroupId) throws Exception {
        if (sysGroupId == null || sysGroupId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num = 0;
        SysGroup frame = new SysGroup();
        for (Long aLong : sysGroupId) {
            sysGroupDao.delete(frame.setSysGroupId(aLong));
            num++;
        }
        return result.success("成功删除[" + num + "]条数据");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysGroupPermission的参数
     * /system/v1.0.0/sysGroupPermission/find?参数同下,只是仅接受查询结果为1条的情况
     */
    @DecoratorOpen
    @GetMapping(value = "/sysGroupPermission/find")
    public Object findSysGroupPermission(JSONResult result, SysGroupPermission sysGroupPermission) throws Exception {
        if (sysGroupPermission == null || sysGroupPermission.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        SysGroupPermission sysGroupPermission1 = sysGroupPermissionDao.find(sysGroupPermission);
        if (sysGroupPermission1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result._data(sysGroupPermission1.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysGroupPermission的参数
     * /system/v1.0.0/sysGroupPermission/list?参数同下,只是是根据实体类查询
     */
    @DecoratorOpen
    @GetMapping("/sysGroupPermission/list")
    public Object listSysGroupPermission(JSONResult result, SysGroupPermission sysGroupPermission, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return sysGroupPermissionDao.queryPage(sysGroupPermission, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysGroupPermission的参数
     * /system/v1.0.0/sysGroupPermission/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     */
    @DecoratorOpen
    @GetMapping("/sysGroupPermission/query")
    public Object querySysGroupPermission(JSONResult result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = sysGroupPermissionDao.getDataBase().toSelect("sys_group_permission", field, symbol, value, montage, sortField, sortType);
        return sysGroupPermissionDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysGroupPermission的参数
     * /system/v1.0.0/sysGroupPermission/save
     */
    @DecoratorOpen
    @PostMapping("/sysGroupPermission/save")
    public Object saveSysGroupPermission(JSONResult result, SysGroupPermission sysGroupPermission) throws Exception {
        if (sysGroupPermission == null || sysGroupPermission.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (sysGroupPermissionDao.save(sysGroupPermission) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysGroupPermission的参数
     * /system/v1.0.0/sysGroupPermission/update?sysGroupPermissionId=
     */
    @DecoratorOpen
    @PostMapping("/sysGroupPermission/update")
    public Object updateSysGroupPermission(JSONResult result, SysGroupPermission sysGroupPermission) throws Exception {
        if (sysGroupPermission == null || sysGroupPermission.toString().equals("{}") || sysGroupPermission.getSysGroupPermissionId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (sysGroupPermissionDao.update(sysGroupPermission) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 sysGroupPermissionId
     * /system/v1.0.0/sysGroupPermission/delete?sysGroupPermissionId=
     */
    @DecoratorOpen
    @DeleteMapping("/sysGroupPermission/delete")
    public Object deleteSysGroupPermission(JSONResult result, SysGroupPermission sysGroupPermission) throws Exception {
        if (sysGroupPermission == null || sysGroupPermission.toString().equals("{}") || sysGroupPermission.getSysGroupPermissionId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (sysGroupPermissionDao.delete(sysGroupPermission) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 sysGroupPermissionId
     * /system/v1.0.0/sysGroupPermission/deleteAll?sysGroupPermissionId=
     */
    @DecoratorOpen
    @GetMapping("/sysGroupPermission/deleteAll")
    public Object deleteAllSysGroupPermission(JSONResult result, Long[] sysGroupPermissionId) throws Exception {
        if (sysGroupPermissionId == null || sysGroupPermissionId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num = 0;
        SysGroupPermission frame = new SysGroupPermission();
        for (Long aLong : sysGroupPermissionId) {
            sysGroupPermissionDao.delete(frame.setSysGroupPermissionId(aLong));
            num++;
        }
        return result.success("成功删除[" + num + "]条数据");
    }


    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysGroupTable的参数
     * /system/v1.0.0/sysGroupTable/find?参数同下,只是仅接受查询结果为1条的情况
     */
    @DecoratorOpen
    @GetMapping(value = "/sysGroupTable/find")
    public Object findSysGroupTable(JSONResult result, SysGroupTable sysGroupTable) throws Exception {
        if (sysGroupTable == null || sysGroupTable.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        SysGroupTable sysGroupTable1 = sysGroupTableDao.find(sysGroupTable);
        if (sysGroupTable1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result._data(sysGroupTable1.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysGroupTable的参数
     * /system/v1.0.0/sysGroupTable/list?参数同下,只是是根据实体类查询
     */
    @DecoratorOpen
    @GetMapping("/sysGroupTable/list")
    public Object listSysGroupTable(JSONResult result, SysGroupTable sysGroupTable, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return sysGroupTableDao.queryPage(sysGroupTable, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysGroupTable的参数
     * /system/v1.0.0/sysGroupTable/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     */
    @DecoratorOpen
    @GetMapping("/sysGroupTable/query")
    public Object querySysGroupTable(JSONResult result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = sysGroupTableDao.getDataBase().toSelect("sys_group_table", field, symbol, value, montage, sortField, sortType);
        return sysGroupTableDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysGroupTable的参数
     * /system/v1.0.0/sysGroupTable/save
     */
    @DecoratorOpen
    @PostMapping("/sysGroupTable/save")
    public Object saveSysGroupTable(JSONResult result, SysGroupTable sysGroupTable) throws Exception {
        if (sysGroupTable == null || sysGroupTable.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (sysGroupTableDao.save(sysGroupTable) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysGroupTable的参数
     * /system/v1.0.0/sysGroupTable/update?sysGroupTableId=
     */
    @DecoratorOpen
    @PostMapping("/sysGroupTable/update")
    public Object updateSysGroupTable(JSONResult result, SysGroupTable sysGroupTable) throws Exception {
        if (sysGroupTable == null || sysGroupTable.toString().equals("{}") || sysGroupTable.getSysGroupTableId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (sysGroupTableDao.update(sysGroupTable) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 sysGroupTableId
     * /system/v1.0.0/sysGroupTable/delete?sysGroupTableId=
     */
    @DecoratorOpen
    @DeleteMapping("/sysGroupTable/delete")
    public Object deleteSysGroupTable(JSONResult result, SysGroupTable sysGroupTable) throws Exception {
        if (sysGroupTable == null || sysGroupTable.toString().equals("{}") || sysGroupTable.getSysGroupTableId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (sysGroupTableDao.delete(sysGroupTable) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 sysGroupTableId
     * /system/v1.0.0/sysGroupTable/deleteAll?sysGroupTableId=
     */
    @DecoratorOpen
    @GetMapping("/sysGroupTable/deleteAll")
    public Object deleteAllSysGroupTable(JSONResult result, Long[] sysGroupTableId) throws Exception {
        if (sysGroupTableId == null || sysGroupTableId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num = 0;
        SysGroupTable frame = new SysGroupTable();
        for (Long aLong : sysGroupTableId) {
            sysGroupTableDao.delete(frame.setSysGroupTableId(aLong));
            num++;
        }
        return result.success("成功删除[" + num + "]条数据");
    }


    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysLog的参数
     * /system/v1.0.0/sysLog/find?参数同下,只是仅接受查询结果为1条的情况
     */
    @DecoratorOpen
    @GetMapping(value = "/sysLogDao/find")
    public Object findSysLogDao(JSONResult result, SysLog sysLog) throws Exception {
        if (sysLog == null || sysLog.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        SysLog sysLog1 = sysLogDao.find(sysLog);
        if (sysLog1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result._data(sysLog1.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysLog的参数
     * /system/v1.0.0/sysLog/list?参数同下,只是是根据实体类查询
     */
    @DecoratorOpen
    @GetMapping("/sysLogDao/list")
    public Object listSysLogDao(JSONResult result, SysLog sysLog, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return sysLogDao.queryPage(sysLog, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysLog的参数
     * /system/v1.0.0/sysLog/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     */
    @DecoratorOpen
    @GetMapping("/sysLogDao/query")
    public Object querySysLogDao(JSONResult result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = sysLogDao.getDataBase().toSelect("sys_log", field, symbol, value, montage, sortField, sortType);
        return sysLogDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysLog的参数
     * /system/v1.0.0/sysLog/save
     */
    @DecoratorOpen
    @PostMapping("/sysLogDao/save")
    public Object saveSysLogDao(JSONResult result, SysLog sysLog) throws Exception {
        if (sysLog == null || sysLog.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (sysLogDao.save(sysLog) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysLog的参数
     * /system/v1.0.0/sysLog/update?sysLogId=
     */
    @DecoratorOpen
    @PostMapping("/sysLogDao/update")
    public Object updateSysLogDao(JSONResult result, SysLog sysLog) throws Exception {
        if (sysLog == null || sysLog.toString().equals("{}") || sysLog.getSysLogId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (sysLogDao.update(sysLog) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 sysLogId
     * /system/v1.0.0/sysLog/delete?sysLogId=
     */
    @DecoratorOpen
    @DeleteMapping("/sysLogDao/delete")
    public Object deleteSysLogDao(JSONResult result, SysLog sysLog) throws Exception {
        if (sysLog == null || sysLog.toString().equals("{}") || sysLog.getSysLogId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (sysLogDao.delete(sysLog) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 sysLogId
     * /system/v1.0.0/sysLog/deleteAll?sysLogId=
     */
    @DecoratorOpen
    @GetMapping("/sysLogDao/deleteAll")
    public Object deleteAllSysLogDao(JSONResult result, Long[] sysLogId) throws Exception {
        if (sysLogId == null || sysLogId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num = 0;
        SysLog frame = new SysLog();
        for (Long aLong : sysLogId) {
            sysLogDao.delete(frame.setSysLogId(aLong));
            num++;
        }
        return result.success("成功删除[" + num + "]条数据");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysMapping的参数
     * /system/v1.0.0/sysMapping/find?参数同下,只是仅接受查询结果为1条的情况
     */
    @DecoratorOpen
    @GetMapping(value = "/sysMapping/find")
    public Object findSysMapping(JSONResult result, SysMapping sysMapping) throws Exception {
        if (sysMapping == null || sysMapping.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        SysMapping sysMapping1 = sysMappingDao.find(sysMapping);
        if (sysMapping1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result._data(sysMapping1.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysMapping的参数
     * /system/v1.0.0/sysMapping/list?参数同下,只是是根据实体类查询
     */
    @DecoratorOpen
    @GetMapping("/sysMapping/list")
    public Object listSysMapping(JSONResult result, SysMapping sysMapping, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return sysMappingDao.queryPage(sysMapping, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysMapping的参数
     * /system/v1.0.0/sysMapping/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     */
    @DecoratorOpen
    @GetMapping("/sysMapping/query")
    public Object querySysMapping(JSONResult result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = sysMappingDao.getDataBase().toSelect("sys_mapping", field, symbol, value, montage, sortField, sortType);
        return sysMappingDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysMapping的参数
     * /system/v1.0.0/sysMapping/save
     */
    @DecoratorOpen
    @PostMapping("/sysMapping/save")
    public Object saveSysMapping(JSONResult result, SysMapping sysMapping) throws Exception {
        if (sysMapping == null || sysMapping.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (sysMappingDao.save(sysMapping) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysMapping的参数
     * /system/v1.0.0/sysMapping/update?sysMappingId=
     */
    @DecoratorOpen
    @PostMapping("/sysMapping/update")
    public Object updateSysMapping(JSONResult result, SysMapping sysMapping) throws Exception {
        if (sysMapping == null || sysMapping.toString().equals("{}") || sysMapping.getSysMappingId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (sysMappingDao.update(sysMapping) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 sysMappingId
     * /system/v1.0.0/sysMapping/delete?sysMappingId=
     */
    @DecoratorOpen
    @DeleteMapping("/sysMapping/delete")
    public Object deleteSysMapping(JSONResult result, SysMapping sysMapping) throws Exception {
        if (sysMapping == null || sysMapping.toString().equals("{}") || sysMapping.getSysMappingId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (sysMappingDao.delete(sysMapping) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 sysMappingId
     * /system/v1.0.0/sysMapping/deleteAll?sysMappingId=
     */
    @DecoratorOpen
    @GetMapping("/sysMapping/deleteAll")
    public Object deleteAllSysMapping(JSONResult result, Long[] sysMappingId) throws Exception {
        if (sysMappingId == null || sysMappingId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num = 0;
        SysMapping frame = new SysMapping();
        for (Long aLong : sysMappingId) {
            sysMappingDao.delete(frame.setSysMappingId(aLong));
            num++;
        }
        return result.success("成功删除[" + num + "]条数据");
    }


    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysOption的参数
     * /system/v1.0.0/sysOption/find?参数同下,只是仅接受查询结果为1条的情况
     */
    @DecoratorOpen
    @GetMapping(value = "/sysOption/find")
    public Object findSysOption(JSONResult result, SysOption sysOption) throws Exception {
        if (sysOption == null || sysOption.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        SysOption sysOption1 = sysOptionDao.find(sysOption);
        if (sysOption1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result._data(sysOption1.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysOption的参数
     * /system/v1.0.0/sysOption/list?参数同下,只是是根据实体类查询
     */
    @DecoratorOpen
    @GetMapping("/sysOption/list")
    public Object listSysOption(JSONResult result, SysOption sysOption, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return sysOptionDao.queryPage(sysOption, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysOption的参数
     * /system/v1.0.0/sysOption/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     */
    @DecoratorOpen
    @GetMapping("/sysOption/query")
    public Object querySysOption(JSONResult result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = sysOptionDao.getDataBase().toSelect("sys_option", field, symbol, value, montage, sortField, sortType);
        return sysOptionDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysOption的参数
     * /system/v1.0.0/sysOption/save
     */
    @DecoratorOpen
    @PostMapping("/sysOption/save")
    public Object saveSysOption(JSONResult result, SysOption sysOption) throws Exception {
        if (sysOption == null || sysOption.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (sysOptionDao.save(sysOption) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysOption的参数
     * /system/v1.0.0/sysOption/update?sysOptionId=
     */
    @DecoratorOpen
    @PostMapping("/sysOption/update")
    public Object updateSysOption(JSONResult result, SysOption sysOption) throws Exception {
        if (sysOption == null || sysOption.toString().equals("{}") || sysOption.getSysOptionId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (sysOptionDao.update(sysOption) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 sysOptionId
     * /system/v1.0.0/sysOption/delete?sysOptionId=
     */
    @DecoratorOpen
    @DeleteMapping("/sysOption/delete")
    public Object deleteSysOption(JSONResult result, SysOption sysOption) throws Exception {
        if (sysOption == null || sysOption.toString().equals("{}") || sysOption.getSysOptionId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (sysOptionDao.delete(sysOption) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 sysOptionId
     * /system/v1.0.0/sysOption/deleteAll?sysOptionId=
     */
    @DecoratorOpen
    @GetMapping("/sysOption/deleteAll")
    public Object deleteAllSysOption(JSONResult result, Long[] sysOptionId) throws Exception {
        if (sysOptionId == null || sysOptionId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num = 0;
        SysOption frame = new SysOption();
        for (Long aLong : sysOptionId) {
            sysOptionDao.delete(frame.setSysOptionId(aLong));
            num++;
        }
        return result.success("成功删除[" + num + "]条数据");
    }


    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysPermission的参数
     * /system/v1.0.0/sysPermission/find?参数同下,只是仅接受查询结果为1条的情况
     */
    @DecoratorOpen
    @GetMapping(value = "/sysPermission/find")
    public Object findSysPermission(JSONResult result, SysPermission sysPermission) throws Exception {
        if (sysPermission == null || sysPermission.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        SysPermission sysPermission1 = sysPermissionDao.find(sysPermission);
        if (sysPermission1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result._data(sysPermission1.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysPermission的参数
     * /system/v1.0.0/sysPermission/list?参数同下,只是是根据实体类查询
     */
    @DecoratorOpen
    @GetMapping("/sysPermission/list")
    public Object listSysPermission(JSONResult result, SysPermission sysPermission, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return sysPermissionDao.queryPage(sysPermission, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysPermission的参数
     * /system/v1.0.0/sysPermission/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     */
    @DecoratorOpen
    @GetMapping("/sysPermission/query")
    public Object querySysPermission(JSONResult result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = sysPermissionDao.getDataBase().toSelect("sys_permission", field, symbol, value, montage, sortField, sortType);
        return sysPermissionDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysPermission的参数
     * /system/v1.0.0/sysPermission/save
     */
    @DecoratorOpen
    @PostMapping("/sysPermission/save")
    public Object saveSysPermission(JSONResult result, SysPermission sysPermission) throws Exception {
        if (sysPermission == null || sysPermission.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (sysPermissionDao.save(sysPermission) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysPermission的参数
     * /system/v1.0.0/sysPermission/update?sysPermissionId=
     */
    @DecoratorOpen
    @PostMapping("/sysPermission/update")
    public Object updateSysPermission(JSONResult result, SysPermission sysPermission) throws Exception {
        if (sysPermission == null || sysPermission.toString().equals("{}") || sysPermission.getSysPermissionId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (sysPermissionDao.update(sysPermission) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 sysPermissionId
     * /system/v1.0.0/sysPermission/delete?sysPermissionId=
     */
    @DecoratorOpen
    @DeleteMapping("/sysPermission/delete")
    public Object deleteSysPermission(JSONResult result, SysPermission sysPermission) throws Exception {
        if (sysPermission == null || sysPermission.toString().equals("{}") || sysPermission.getSysPermissionId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (sysPermissionDao.delete(sysPermission) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 sysPermissionId
     * /system/v1.0.0/sysPermission/deleteAll?sysPermissionId=
     */
    @DecoratorOpen
    @GetMapping("/sysPermission/deleteAll")
    public Object deleteAllSysPermission(JSONResult result, Long[] sysPermissionId) throws Exception {
        if (sysPermissionId == null || sysPermissionId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num = 0;
        SysPermission frame = new SysPermission();
        for (Long aLong : sysPermissionId) {
            sysPermissionDao.delete(frame.setSysPermissionId(aLong));
            num++;
        }
        return result.success("成功删除[" + num + "]条数据");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysUsersLoginLog的参数
     * /system/v1.0.0/sysUsersLoginLog/find?参数同下,只是仅接受查询结果为1条的情况
     */
    @DecoratorOpen
    @GetMapping(value = "/sysUsersLoginLog/find")
    public Object findSysUsersLoginLog(JSONResult result, SysUsersLoginLog sysUsersLoginLog) throws Exception {
        if (sysUsersLoginLog == null || sysUsersLoginLog.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        SysUsersLoginLog sysUsersLoginLog1 = sysUsersLoginLogDao.find(sysUsersLoginLog);
        if (sysUsersLoginLog1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result._data(sysUsersLoginLog1.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysUsersLoginLog的参数
     * /system/v1.0.0/sysUsersLoginLog/list?参数同下,只是是根据实体类查询
     */
    @DecoratorOpen
    @GetMapping("/sysUsersLoginLog/list")
    public Object listSysUsersLoginLog(JSONResult result, SysUsersLoginLog sysUsersLoginLog, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return sysUsersLoginLogDao.queryPage(sysUsersLoginLog, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysUsersLoginLog的参数
     * /system/v1.0.0/sysUsersLoginLog/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     */
    @DecoratorOpen
    @GetMapping("/sysUsersLoginLog/query")
    public Object querySysUsersLoginLog(JSONResult result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = sysUsersLoginLogDao.getDataBase().toSelect("sys_users_login_log", field, symbol, value, montage, sortField, sortType);
        return sysUsersLoginLogDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysUsersLoginLog的参数
     * /system/v1.0.0/sysUsersLoginLog/save
     */
    @DecoratorOpen
    @PostMapping("/sysUsersLoginLog/save")
    public Object saveSysUsersLoginLog(JSONResult result, SysUsersLoginLog sysUsersLoginLog) throws Exception {
        if (sysUsersLoginLog == null || sysUsersLoginLog.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (sysUsersLoginLogDao.save(sysUsersLoginLog) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysUsersLoginLog的参数
     * /system/v1.0.0/sysUsersLoginLog/update?sysUsersLoginLogId=
     */
    @DecoratorOpen
    @PostMapping("/sysUsersLoginLog/update")
    public Object updateSysUsersLoginLog(JSONResult result, SysUsersLoginLog sysUsersLoginLog) throws Exception {
        if (sysUsersLoginLog == null || sysUsersLoginLog.toString().equals("{}") || sysUsersLoginLog.getSysUsersLoginLogId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (sysUsersLoginLogDao.update(sysUsersLoginLog) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 sysUsersLoginLogId
     * /system/v1.0.0/sysUsersLoginLog/delete?sysUsersLoginLogId=
     */
    @DecoratorOpen
    @DeleteMapping("/sysUsersLoginLog/delete")
    public Object deleteSysUsersLoginLog(JSONResult result, SysUsersLoginLog sysUsersLoginLog) throws Exception {
        if (sysUsersLoginLog == null || sysUsersLoginLog.toString().equals("{}") || sysUsersLoginLog.getSysUsersLoginLogId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (sysUsersLoginLogDao.delete(sysUsersLoginLog) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 sysUsersLoginLogId
     * /system/v1.0.0/sysUsersLoginLog/deleteAll?sysUsersLoginLogId=
     */
    @DecoratorOpen
    @GetMapping("/sysUsersLoginLog/deleteAll")
    public Object deleteAllSysUsersLoginLog(JSONResult result, Long[] sysUsersLoginLogId) throws Exception {
        if (sysUsersLoginLogId == null || sysUsersLoginLogId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num = 0;
        SysUsersLoginLog frame = new SysUsersLoginLog();
        for (Long aLong : sysUsersLoginLogId) {
            sysUsersLoginLogDao.delete(frame.setSysUsersLoginLogId(aLong));
            num++;
        }
        return result.success("成功删除[" + num + "]条数据");
    }



}
