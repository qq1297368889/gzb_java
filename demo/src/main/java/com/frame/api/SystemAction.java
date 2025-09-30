package com.frame.api;

import com.frame.dao.*;
import com.frame.dao.impl.SysFileDaoImpl;
import com.frame.entity.*;
import gzb.entity.SqlTemplate;
import gzb.frame.annotation.*;
import gzb.frame.db.DataBase;
import gzb.frame.db.DataBaseFactory;
import gzb.frame.netty.entity.FileUploadEntity;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;
import gzb.tools.*;
import gzb.tools.cache.session.Session;
import gzb.tools.img.GifCaptcha;
import gzb.tools.img.PicUtils;
import gzb.tools.json.GzbJson;
import gzb.tools.log.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
//@CrossDomain(allowCredentials = false)
@Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")})
@RequestMapping(value = "/system/v1.0.0")
public class SystemAction {
    public SysFileDao sysFileDao = new SysFileDaoImpl();
    //key.system.login.image.code  验证码储存key
    //key.system.login.info  登录信息储存KEY
    public DataBase dataBase = DataBaseFactory.getDataBase(Config.frameDbKey);
    @Resource()
    public Log log;

    public SystemAction() throws Exception {
    }


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
    @Header(item = {@HeaderItem(key = "content-type", val = "image/gif")})
    @GetMapping(value = "image/code")
    public Object image_code(Request request) throws Exception {
        Session session = request.getSession();
        GifCaptcha gifCaptcha = new GifCaptcha(140, 45, 5);
        String verCode = gifCaptcha.text().toLowerCase();
        session.put(Config.get("key.system.login.image.code"), verCode);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gifCaptcha.out(outputStream);
        //Response也可以在参数上注入
        //request.getResponse().write(outputStream.toByteArray());  //这样也可以
        //request.getResponse().setHeader("Content-Type", "image/gif");//这样也可以
        return outputStream.toByteArray();
    }

    /// 登陆
    /// /system/v1.0.0/image/code
    /// /system/v1.0.0/login?sysUsersAcc=admin&sysUsersPwd=admin&code=vkvss
    @GetMapping(value = "login")
    public Object login(GzbJson result, SysUsers sysUsers, String mac, String code, Request request) throws Exception {
        Session session = request.getSession();
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
        String ip = request.getRemoteIp();
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
    public Object reg5(GzbJson result, SysUsers sysUsers, String mac, String code, Request request) throws Exception {
        Session session = request.getSession();
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

        String ip = request.getRemoteIp();
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
    public Object exitUser(Request request, GzbJson res) {
        Session session = request.getSession();
        session.delete();
        return res.success("登陆已失效");
    }

    /// 获取当前登录用户的 信息
    /// /system/v1.0.0/read/user/info
    @DecoratorOpen
    @GetMapping(value = "read/user/info")
    public Object readUserInfo(Request request, GzbJson res) {
        Session session = request.getSession();
        String jsonString = session.getString(Config.get("key.system.login.info"));
        return res.success("获取成功", new SysUsers(jsonString));
    }

    /// 更新权限列表 从当前http端点映射
    /// /system/v1.0.0/update/permission
    @DecoratorOpen
    @GetMapping(value = "update/updatePermissionAndMapping")
    public Object updatePermissionAndMapping(GzbJson res, Long sysGroupId, SysUsers sysUsers) throws Exception {
        if (sysUsers == null || sysUsers.getSysUsersType() != 4) {
            return res.fail("没有合适权限操作");
        }
        if (sysGroupId == null) {
            return res.fail("必填参数为空");
        }
        Tools.updateMapping(dataBase, null, sysGroupId);
        return res.success("映射已更新");
    }

    /// /system/v1.0.0/authorize/permission?pid=&gid=&type=
    @DecoratorOpen
    @PostMapping(value = "authorize/permission")
    public Object authorize(GzbJson res, Long[] pid, Long gid, Long type, SysUsers sysUsers) throws Exception {
        if (pid == null || pid.length < 1 || gid == null) {
            return res.fail("必填参数不允许为空");
        }
        DateTime dateTime = new DateTime();
        for (Long id : pid) {
            List<GzbMap> list2 = dataBase.selectGzbMap(
                    "select * from sys_group_permission where sys_group_permission_gid = ? and sys_group_permission_pid = ?",
                    new Object[]{gid, id});
            if (list2.size() == 1) {
                if (type == 0L) {
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
    public Object readPermission(GzbJson res,
                                 Request request,
                                 Long uid, SysPermissionDao sysPermissionDao, SysUsersDao sysUsersDao) throws Exception {
        Session session = request.getSession();
        SysUsers sysUsers0 = new SysUsers(session.getString(Config.get("key.system.login.info")));
        SysUsers sysUsers = null;
        if (uid != null) {
            List<SysUsers> list1 = null;
            if (sysUsers0.getSysUsersType() == 4) {

                list1 = sysUsersDao.query(new SysUsers().setSysUsersId(uid),60);
            } else {
                list1 = sysUsersDao.query(new SysUsers().setSysUsersId(uid).setSysUsersSup(sysUsers0.getSysUsersId()),60);
            }
            if (list1.size() != 1) {
                return res.fail("无权干预该用户权限分配");
            }
            sysUsers = list1.get(0);
        } else {
            sysUsers = sysUsers0;
        }
        List<SysPermission> list1 = null;
        if (sysUsers.getSysUsersType() == 4) {
            list1 = sysPermissionDao.query("SELECT p.* FROM sys_permission p where sys_permission_type = ? and p.sys_permission_sup = ? " + "ORDER BY p.sys_permission_sort DESC"
                    , new Object[]{1, 0},null,null,0,0,60);
            for (SysPermission sysPermission : list1) {
                sysPermission.setList(sysPermissionDao.query(new SysPermission().setSysPermissionSup(sysPermission.getSysPermissionId()), 60));
            }
            return res.success("权限查询成功", list1);
        } else {
            list1 = sysPermissionDao.query("SELECT p.* FROM sys_permission p " + "WHERE p.sys_permission_id IN (" + "    SELECT gp.sys_group_permission_pid " + "    FROM sys_group_permission gp " + "    WHERE gp.sys_group_permission_gid = (" + "        SELECT g.sys_group_id " + "        FROM sys_group g " + "        WHERE g.sys_group_id = (" + "            SELECT u.sys_users_group " + "            FROM sys_users u " + "            WHERE u.sys_users_id = ? " + "            AND u.sys_users_status > ?" + "        )" + "    )" + ") AND p.sys_permission_sup = ? AND p.sys_permission_type = ? " + "ORDER BY p.sys_permission_sort DESC",
                    new Object[]{sysUsers.getSysUsersId(), 0, 0, 1}, null,null,0,0,60);
        }
        for (SysPermission sysPermission : list1) {
            sysPermission.setList(sysPermissionDao.query("select sys_permission.* from sys_permission where " + "sys_permission.sys_permission_id in " + "(select sys_group_permission_pid from sys_group_permission where sys_group_permission_gid = " + "(select sys_users.sys_users_group from sys_users where sys_users_id = ? and sys_users_status > ?" + ") " + ") " + "and sys_permission.sys_permission_sup = ?",
                    new Object[]{sysUsers.getSysUsersId(), 0, sysPermission.getSysPermissionId()}, null,null,0,0,60));
        }
        return res.success("权限查询成功", list1);
    }

    @DecoratorOpen
    @GetMapping(value = "read/permission/all")
    public Object readPermissionAll(GzbJson res,
                                    Request request,
                                    Long gid) throws Exception {
        Session session = request.getSession();
        SysUsers sysUsers0 = new SysUsers(session.getString(Config.get("key.system.login.info")));
        if (sysUsers0.getSysUsersType() != 4) {
            log.w("越权访问 ", gid, sysUsers0);
            return null;
        }
        List<GzbMap> list1 = null;
        if (gid != null) {
            list1 = dataBase.selectGzbMap(
                    "select sys_permission.* from sys_permission where sys_permission.sys_permission_id in " +
                            "(select sys_group_permission.sys_group_permission_pid from sys_group_permission where " +
                            "sys_group_permission.sys_group_permission_gid = ?) and sys_permission_sup = ?",
                    new Object[]{gid, 0});
        } else {
            list1 = dataBase.selectGzbMap(
                    "select sys_permission.* from sys_permission where sys_permission_sup = ? order by sys_permission_sort desc",
                    new Object[]{0});
        }
        List<SysPermission> list = new ArrayList<>(list1.size());
        for (GzbMap gzbMap : list1) {
            list.add(new SysPermission(gzbMap));
        }
        for (SysPermission sysPermission : list) {
            if (!sysPermission.getSysPermissionType().toString().equals("1")) {
                continue;
            }
            List<GzbMap> list2 = null;
            if (gid != null) {
                list2 = dataBase.selectGzbMap(
                        "select sys_permission.* from sys_permission where " +
                                "sys_permission.sys_permission_id in " +
                                "(select sys_group_permission_pid from sys_group_permission where sys_group_permission_gid = ?" +
                                ") " +
                                "and sys_permission.sys_permission_sup = ?",
                        new Object[]{gid, sysPermission.getSysPermissionId()});
            } else {
                list2 = dataBase.selectGzbMap(
                        "select sys_permission.* from sys_permission where sys_permission.sys_permission_sup = ?",
                        new Object[]{sysPermission.getSysPermissionId()});
            }
            sysPermission.setList(list2);
        }

        return res.success("权限查询成功", list);
    }

    /// 获取某个表的映射信息
    /// read/mapping?key=sys_Users
    @DecoratorOpen
    @GetMapping(value = "read/mapping")
    public Object readMapping(GzbJson res, Request request, String key,
                              SysGroupDao sysGroupDao,
                              SysGroupTableDao sysGroupTableDao,
                              SysGroupColumnDao sysGroupColumnDao,
                              SysMappingDao sysMappingDao,
                              SysOptionDao sysOptionDao) throws Exception {
        log.d("key",key);
        Session session = request.getSession();
        SysUsers sysUsers = new SysUsers(session.getString(Config.get("key.system.login.info")));
        if (key == null) {
            return res.fail("参数为空");
        }
        key = Tools.lowStr_x(Tools.lowStr_hump(key));
        log.d("key",key);
        List<SysGroup> listSysGroup = sysGroupDao.query(new SysGroup().setSysGroupId(sysUsers.getSysUsersGroup()),60);
        if (listSysGroup.size() != 1) {
            return res.fail("组信息查询失败");
        }
        //查询表信息
        List<SysGroupTable> listSysGroupTable = sysGroupTableDao.query(new SysGroupTable().setSysGroupTableKey(key).setSysGroupTableGid(sysUsers.getSysUsersGroup()),60);
        if (listSysGroupTable.isEmpty()) {
            return res.fail("表映射信息获取失败");
        }
        //查询列信息 sysGroupColumnDao
        List<SysGroupColumn> listSysGroupColumn = sysGroupColumnDao.query(new SysGroupColumn().setSysGroupColumnKey(key).setSysGroupColumnGid(sysUsers.getSysUsersGroup()),60);
        if (listSysGroupColumn.isEmpty()) {
            return res.fail("列映射信息获取失败");
        }
        for (SysGroupColumn sysGroupColumn : listSysGroupColumn) {
            //查询映射信息
            List<SysMapping> listSysMapping = sysMappingDao.query(new SysMapping().setSysMappingVal(sysGroupColumn.getSysGroupColumnName()).setSysMappingKey(key),60);
            if (!listSysMapping.isEmpty()) {
                sysGroupColumn.putMap("mapping", listSysMapping.get(0));
                for (SysMapping sysMapping : listSysMapping) {
                    String sysMappingSelect = sysMapping.getSysMappingSelect();
                    if (sysMappingSelect != null) {
                        if (sysMappingSelect.startsWith("request:")) {
                            continue;
                        }
                        List<SysOption> listOption = null;
                        if (sysMappingSelect.startsWith("key:")) {
                            listOption = sysOptionDao.query("select sys_option_title,sys_option_value from sys_option where sys_option_key = ?",
                                    new Object[]{sysMappingSelect.replace("key:", "")},null,null,0,0,60);
                        } else if (sysMappingSelect.toLowerCase().contains("select")) {
                            listOption = sysOptionDao.query(sysMappingSelect, null,null,null,0,0,60);
                        }
                        sysMapping.putMap("sysMappingSelect", listOption);
                    }
                }
            }
        }
        if (!listSysGroupColumn.isEmpty()) {
            listSysGroupTable.get(0).putMap("sysGroupColumn", listSysGroupColumn);
            listSysGroup.get(0).putMap("sysGroup", listSysGroupTable.get(0));
        }

        return res.success("权限查询完成", listSysGroup);
    }

    /// /system/v1.0.0/upload?sys_Users
    @PostMapping(value = "upload")
    public Object upload(GzbJson res, FileUploadEntity[] file, Map<String, Object> requestMap) throws Exception {
        log.d("requestMap", requestMap);
        if (file == null) {
            return res.fail("未检测到上传的文件-1001");
        }
        List<SysFile> list = new ArrayList<>();
        for (FileUploadEntity fileUploadEntity : file) {
            File file0 = fileUploadEntity.getFile();
            if (file0 == null) {
                return res.fail("上传的文件不存在-1002");
            }
            String rootPath = Config.uploadDir;
            String md5 = Tools.fileToMd5(file0);
            File file2 = new File(Tools.toMD5Path(rootPath, md5));
            FileTools.mkdir(file2);
            File file3 = new File(file2.getPath() + "/" + fileUploadEntity.getFilename());
            if (!file3.exists()) {
                if (!file0.renameTo(file3)) {
                    continue;
                }
            }

            SysFile sysFile = sysFileDao.find(new SysFile().setSysFileMd5(md5));
            if (sysFile == null) {
                String md5Path = Tools.toMD5Path(md5) + fileUploadEntity.getFilename();
                sysFile = new SysFile();
                sysFile.setSysFileMd5(md5)
                        .setSysFilePath(md5Path)
                        .setSysFileTime(new DateTime().toString())
                        .setSysFileType(fileUploadEntity.getContentType());
                sysFileDao.save(sysFile);
            }
            list.add(sysFile);
        }
        if (list.size() != file.length) {
            return res.fail("上传文件为[" + file.length + "]个，成功处理[" + list.size() + "]个");
        }
        return res.success("上传完成", list);
    }

    ///  /system/v1.0.0/read/file?sysFileMd5=9340ece5e1ebdbf4d875af05fb08e3f0
    @DecoratorOpen
    @GetMapping(value = "read/file")
    public Object readFile0(GzbJson res, SysFile sysFile, SysFileDao sysFileDao, Boolean b64, Double t, Response response, Request request) throws Exception {
        if (sysFile == null || sysFile.toString().equals("{}")) {
            return res.fail("必要参数未填写[1000]");
        }
        if (sysFile.getSysFileId() == null && sysFile.getSysFilePath() == null && sysFile.getSysFileMd5() == null) {
            return res.fail("必要参数未填写[1001]");
        }
        sysFile = sysFileDao.find(sysFile);
        if (sysFile == null) {
            return res.fail("文件不存在或无权限访问[1000]");
        }
        String path = Config.uploadDir;
        String filePath = path + "/" + sysFile.getSysFilePath();
        File file = new File(filePath);
        if (!file.exists() || !file.canRead()) {
            return res.fail("文件不存在或无权限访问[1001]");
        }

        // 设置内容类型
        String contentType = sysFile.getSysFileType();
        if (contentType != null) {
            response.setHeader("content-type", contentType);
        } else {
            response.setHeader("content-type", "application/octet-stream");
        }

        // 读取文件内容
        byte[] bytes = FileTools.readByte(file);
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
        return bytes;
    }

    public static Map<String, Object> mapArrayToNoArray(Map<String, List<Object>> map) {
        Map<String, Object> map0 = new HashMap<>();
        for (Map.Entry<String, List<Object>> stringListEntry : map.entrySet()) {
            if (stringListEntry.getValue() == null || stringListEntry.getValue().toString().isEmpty()) {
                continue;
            }
            map0.put(stringListEntry.getKey(), stringListEntry.getValue().get(0));
        }
        return map0;
    }

    @Resource
    SysUsersDao sysUsersDao;
    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysUsers的参数
     * /system/v1.0.0/sysUsers/find?参数同下,只是仅接受查询结果为1条的情况
     */
    @DecoratorOpen
    @GetMapping(value = "/sysUsers/find")
    public Object find(GzbJson result, SysUsers sysUsers0, Map<String, List<Object>> req) throws Exception {
        Map<String, Object> re2 = mapArrayToNoArray(req);
        SysUsers sysUsers = new SysUsers(re2);
        if (sysUsers.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }
        return result.success("查询成功", sysUsersDao.find(sysUsers));
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysUsers的参数
     * /system/v1.0.0/sysUsers/list?参数同下,只是是根据实体类查询
     */
    @DecoratorOpen
    @GetMapping("/sysUsers/list")
    public Object list(GzbJson result, SysUsers sysUsers0, String sortField, String sortType, Integer page, Integer size, Map<String, List<Object>> req) throws Exception {
        Map<String, Object> re2 = mapArrayToNoArray(req);
        SysUsers sysUsers = new SysUsers(re2);
        return result.paging(sysUsersDao.query(sysUsers,sortField,sortType,page,size), page, size);
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysUsers的参数
     * /system/v1.0.0/sysUsers/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     */
    @DecoratorOpen
    @GetMapping("/sysUsers/query")
    public Object query(GzbJson result, SysUsers sysUsers0, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
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
    public Object save(GzbJson result, SysUsers sysUsers0, Map<String, List<Object>> req) throws Exception {
        Map<String, Object> re2 = mapArrayToNoArray(req);
        SysUsers sysUsers = new SysUsers(re2);
        if (sysUsers.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (sysUsersDao.save(sysUsers) < 0) {
            return result.fail("update 失败");
        }
        return result.success("save 成功");
    }
    /**
     * 修改,根据id修改其他参数 请提供 SysUsers的参数
     * /system/v1.0.0/sysUsers/update?sysUsersId=
     */
    @DecoratorOpen
    @PostMapping("/sysUsers/update")
    public Object update(GzbJson result, SysUsers sysUsers0, Map<String, List<Object>> req) throws Exception {
        Map<String, Object> re2 = mapArrayToNoArray(req);
        SysUsers sysUsers = new SysUsers(re2);
        if (sysUsers.toString().equals("{}") || sysUsers.getSysUsersId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (sysUsersDao.update(sysUsers) < 0) {
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
    public Object delete(GzbJson result, SysUsers sysUsers0, Map<String, List<Object>> req) throws Exception {
        Map<String, Object> re2 = mapArrayToNoArray(req);
        SysUsers sysUsers = new SysUsers(re2);
        if (sysUsers.toString().equals("{}") || sysUsers.getSysUsersId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (sysUsersDao.delete(sysUsers) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 sysUsersId
     * /system/v1.0.0/sysUsers/deleteAll?sysUsersId=
     */
    @DecoratorOpen
    @Transaction
    @DeleteMapping("/sysUsers/deleteAll")
    public Object deleteAll(GzbJson result, Long[] sysUsersId) throws Exception {
        if (sysUsersId == null || sysUsersId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num = 0;
        for (Long aLong : sysUsersId) {
            if (sysUsersDao.delete(new SysUsers().setSysUsersId(aLong)) >-1) {
                num++;
            }
        }
        return result.success("成功删除[" + num + "]条数据");
    }








}
