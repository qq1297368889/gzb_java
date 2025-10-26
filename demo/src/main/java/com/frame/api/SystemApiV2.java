package com.frame.api;

import com.frame.dao.*;
import com.frame.entity.*;
import gzb.entity.FileUploadEntity;
import gzb.entity.TableInfo;
import gzb.frame.PublicData;
import gzb.frame.annotation.*;
import gzb.frame.db.DataBase;
import gzb.frame.factory.v4.FactoryImplV2;
import gzb.frame.generate.GenerateJavaCode;
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
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
//@CrossDomain(allowCredentials = false)
@Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")})
@RequestMapping(value = "/system/v2")
public class SystemApiV2 {
    @Resource
    Log log;
    @Resource
    SysUsersDao sysUsersDao;
    @Resource
    SysRoleDao sysRoleDao;
    @Resource
    SysRoleGroupDao sysRoleGroupDao;
    @Resource
    SysPermissionDao sysPermissionDao;
    @Resource
    SysGroupDao sysGroupDao;
    @Resource
    SysFileDao sysFileDao;
    @Resource
    SysRoleTableDao sysRoleTableDao;
    @Resource
    SysRoleColumnDao sysRoleColumnDao;
    @Resource
    SysMappingTableDao sysMappingTableDao;
    @Resource
    SysMappingColumnDao sysMappingColumnDao;
    @Resource
    SysOptionDao sysOptionDao;
    @Resource
    SysGroupPermissionDao sysGroupPermissionDao;
    @Resource
    SysOptionSqlDao sysOptionSqlDao;
    @Resource
    SysOptionRequestDao sysOptionRequestDao;


    @GetMapping(value = "read/data")
    public Object readData() {
        Object obj=FactoryImplV2.reqInfo;
        FactoryImplV2.reqInfo=new ConcurrentHashMap<>();
        return obj;
    }

    //获取验证码  /system/v2/image/code
    @Header(item = {@HeaderItem(key = "content-type", val = "image/gif")})
    @GetMapping(value = "image/code")
    public Object image_code(Request request) {
        Session session = request.getSession();
        GifCaptcha gifCaptcha = new GifCaptcha(140, 45, 5);
        String verCode = gifCaptcha.text().toLowerCase();
        session.put(Config.get("key.system.login.image.code"), verCode);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gifCaptcha.out(outputStream);
        log.d("verCode", session.getString(Config.get("key.system.login.image.code")));
        return outputStream.toByteArray();
    }

    //登录
    @PostMapping(value = "login")
    public Object login(SysUsers sysUsers, String code, Request request, GzbJson result) throws Exception {
        Session session = request.getSession();
        if (sysUsers == null || sysUsers.toString().equals("{}") || sysUsers.getSysUsersAcc() == null || sysUsers.getSysUsersPwd() == null) {
            return result.fail("login 请输入账号密码");
        }
        if (code == null) {
            return result.fail("login 请输入 验证码");
        }
        String key = Config.get("key.system.login.image.code");
        Object code_s = session.getString(key);
        session.delete(key);
        if (code_s == null) {
            return result.fail("login 请先获取验证码");
        }
        if (!code_s.toString().equals(code)) {
            return result.fail("login 验证码输入错误");
        }

        SysUsers sysUsers1 = sysUsersDao.find(sysUsers);
        if (sysUsers1 == null) {
            return result.fail("login 账号不存在或不正确");
        }
        if (sysUsers1.getSysUsersStatus() < 1) {
            session.delete();
            return result.fail("login 账号状态异常");
        }
        session.put(Config.get("key.system.login.info"), sysUsers1.toString());
        return result.success("登陆成功", Tools.createHashMap("token", session.getId()));
    }

    //注册
    @PostMapping(value = "register")
    public Object register(GzbJson result, SysUsers sysUsers, String code, Request request) throws Exception {
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

        SysUsers sysUsers0 = sysUsersDao.find(new SysUsers().setSysUsersId(sysUsers.getSysUsersSup()));
        if (sysUsers0 == null) {
            sysUsers.setSysUsersSup(0L);
        }
        if (sysUsersDao.find(new SysUsers().setSysUsersAcc(sysUsers.getSysUsersAcc())) != null) {
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
                .setSysUsersSup(sysUsers.getSysUsersId())
                .setSysUsersRegTime(Tools.getTimestamp().toLocalDateTime())
                .setSysUsersPhone(sysUsers.getSysUsersPhone());

        if (sysUsersDao.save(sysUsers1) < 0) {
            return result.fail("注册账号 失败，请稍后重试");
        }
        session.put(Config.get("key.system.login.info"), sysUsers1.toString());
        return result.success("登陆成功", Tools.createHashMap("token", session.getId()));
    }

    //获取用户信息
    @DecoratorOpen
    @GetMapping(value = "read/user/info")
    public Object readUserInfo(Request request, GzbJson res) {
        Session session = request.getSession();
        String jsonString = session.getString(Config.get("key.system.login.info"));
        return res.success("获取成功", new SysUsers(jsonString));
    }

    //退出登录
    @DecoratorOpen
    @GetMapping(value = "exit/user")
    public Object exitUser(Request request, GzbJson res) {
        Session session = request.getSession();
        session.delete();
        return res.success("登陆已失效");
    }

    //更新端点映射到数据库 /system/v2/update/mapping
    @DecoratorOpen
    @GetMapping(value = "update/mapping")
    public Object updateMapping(GzbJson res, SysUsers sysUsers) throws Exception {
        if (sysUsers == null || sysUsers.getSysUsersType() != 4) {
            return res.fail("越权行为");
        }
        GenerateJavaCode.update_mapping(sysUsersDao.getDataBase(), sysUsersDao.getDataBase().getTableInfo());
        return res.success("映射已更新");
    }

    /// 更新角色专属映射 /system/v2/update/role/mapping?sysRoleId=462239296393216
    @DecoratorOpen
    @GetMapping(value = "update/role/mapping")
    public Object updateRoleMapping(SysUsers sysUsers, SysRole sysRole, GzbJson res) throws Exception {
        if (sysUsers == null || sysUsers.getSysUsersType() != 4L) {
            return res.fail("越权行为");
        }
        if (sysRole == null || sysRole.getSysRoleId() == null) {
            return res.fail("缺少必要参数");
        }
        List<SysRole> listSysRole = sysRoleDao.query(new SysRole().setSysRoleId(sysRole.getSysRoleId()));
        if (listSysRole.size() != 1) {
            return res.fail("对应角色不存在-1");
        }
        update_role_mapping(sysGroupDao.getDataBase(), sysGroupDao.getDataBase().getTableInfo(), listSysRole);
        return res.success("角色映射已更新");
    }

    //生成基本 mapping
    public static void update_role_mapping(DataBase dataBase, List<TableInfo> listTableInfo, List<SysRole> listSysRole) throws Exception {
        String sql = "";

        for (SysRole sysRole : listSysRole) {
            for (TableInfo tableInfo : listTableInfo) {
                Long id;
                List<GzbMap> listGzbMap = dataBase.selectGzbMap("select sys_role_table_id from sys_role_table where sys_role_table_role=? and sys_role_table_name=?", new Object[]{sysRole.getSysRoleId(), tableInfo.nameHumpLowerCase});
                if (listGzbMap.size() == 0) {
                    id = OnlyId.getDistributed();
                    sql = "INSERT INTO sys_role_table(" +
                            "sys_role_table_id, sys_role_table_name, sys_role_table_save, sys_role_table_query, sys_role_table_delete, " +
                            "sys_role_table_update, sys_role_table_delete_sgin, sys_role_table_role, sys_role_table_width) VALUES " +
                            "(" + id + ", '" + tableInfo.nameHumpLowerCase + "', 1, 1, 1, " +
                            "1, 1, " + sysRole.getSysRoleId() + ", 150)";
                    dataBase.runSqlAsync(sql, null);
                } else {
                    id = listGzbMap.get(0).getLong("sysRoleTableId");
                }
                for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                    String name = tableInfo.columnNamesHumpLowerCase.get(i);
                    listGzbMap = dataBase.selectGzbMap("select sys_role_column_id from sys_role_column where sys_role_column_table = ? and sys_role_column_name=?", new Object[]{id, name});
                    if (listGzbMap.size() == 0) {
                        sql = "INSERT INTO sys_role_column(" +
                                "sys_role_column_id, sys_role_column_table, sys_role_column_name, sys_role_column_table_show, sys_role_column_query, " +
                                "sys_role_column_save, sys_role_column_update, sys_role_column_edit, sys_role_column_query_def, sys_role_column_query_symbol, " +
                                "sys_role_column_query_montage, sys_role_column_save_def, sys_role_column_update_def, sys_role_column_role) VALUES " +
                                "(" + OnlyId.getDistributed() + ", " + id + ", '" + name + "', 1, 0, " +
                                "1, 1, 1, NULL, 1, " +
                                "1, NULL, NULL, " + sysRole.getSysRoleId() + ")";
                        dataBase.runSqlAsync(sql, null);
                    }
                }

            }
        }

    }

    //获取权限信息
    //获取映射信息   /system/v2/read/mapping?sysRoleTableName=sysMapping
    @DecoratorOpen
    @GetMapping(value = "read/mapping")
    public Object readMapping(SysUsers sysUsers, SysRoleTable sysRoleTable, GzbJson res) throws Exception {
        //角色专属元数据
        SysRoleTable sysRoleTable0 = sysRoleTableDao.find(sysRoleTable.setSysRoleTableRole(sysUsers.getSysUsersRole()));
        if (sysRoleTable0 == null) {
            return res.fail("无权限获取对应映射-1");
        }
        List<SysRoleColumn> listSysRoleColumn = sysRoleColumnDao.query(new SysRoleColumn().setSysRoleColumnTable(sysRoleTable0.getSysRoleTableId()));
        if (listSysRoleColumn.size() == 0) {
            return res.fail("无权限获取对应映射-3");
        }
        //底层元数据
        SysMappingTable sysMappingTable = sysMappingTableDao.find(new SysMappingTable().setSysMappingTableName(sysRoleTable0.getSysRoleTableName()));
        if (sysMappingTable == null) {
            return res.fail("无权限获取对应映射-2");
        }
        //分别写入基础元数据
        for (SysRoleColumn sysRoleColumn : listSysRoleColumn) {
            SysMappingColumn sysMappingColumn = sysMappingColumnDao.find(new SysMappingColumn()
                    .setSysMappingColumnName(sysRoleColumn.getSysRoleColumnName())
                    .setSysMappingColumnTable(sysMappingTable.getSysMappingTableId()), 10);
            sysRoleColumn.setData(sysMappingColumn);
            // 配置表寻找
            if (sysMappingColumn.getSysMappingColumnOption() != null && sysMappingColumn.getSysMappingColumnOption().length() > 0) {
                List<SysOption> list = sysOptionDao.query("select sys_option_value,sys_option_title from sys_option where sys_option_key=?",
                        new Object[]{sysMappingColumn.getSysMappingColumnOption()}, null, null, 0, 0, 10);
                sysMappingColumn.putMap("sysMappingColumnOption", list);
                sysMappingColumn.setSysMappingColumnOption(null);
                if (list.size() == 0) {
                    log.w("无效的配置表KEY ", sysMappingColumn.getSysMappingColumnName(), " -> ", sysMappingColumn.getSysMappingColumnOption());
                }
            }
            //从sql查询
            if (sysMappingColumn.getSysMappingColumnSql() != null && sysMappingColumn.getSysMappingColumnSql().length() > 0) {
                SysOptionSql sysOptionSql = sysOptionSqlDao.find(new SysOptionSql().setSysOptionSqlKey(sysMappingColumn.getSysMappingColumnSql()));
                if (sysOptionSql != null) {
                    if (sysOptionSql.getSysOptionSqlTitleName() == null) {
                        sysOptionSql.setSysOptionSqlTitleName("sysOptionTitle");
                    }
                    if (sysOptionSql.getSysOptionSqlValName() == null) {
                        sysOptionSql.setSysOptionSqlValName("sysOptionValue");
                    }
                    List<GzbMap> list = sysOptionDao.getDataBase().selectGzbMap(sysOptionSql.getSysOptionSqlSql());
                    List<Map> list2 = new ArrayList<>(list.size());
                    for (GzbMap gzbMap : list) {
                        String title = gzbMap.getString(sysOptionSql.getSysOptionSqlTitleName());
                        String val = gzbMap.getString(sysOptionSql.getSysOptionSqlValName());
                        if (title == null) {
                            continue;
                        }
                        Map map = new HashMap();
                        map.put("sysOptionTitle", title);
                        map.put("sysOptionValue", val);
                        list2.add(map);
                    }
                    sysMappingColumn.putMap("sysMappingColumnOption", list2);
                } else {
                    log.w("无效的配置项SQL KEY ", sysMappingColumn.getSysMappingColumnName(), " -> ", sysMappingColumn.getSysMappingColumnSql());
                }
                sysMappingColumn.setSysMappingColumnSql(null);
            }
            //请求方式 允许可变参数 需要从客户端请求
            if (sysMappingColumn.getSysMappingColumnRequest() != null && sysMappingColumn.getSysMappingColumnRequest().length() > 0) {
                SysOptionRequest sysOptionRequest = sysOptionRequestDao.find(new SysOptionRequest().setSysOptionRequestKey(sysMappingColumn.getSysMappingColumnRequest()));
                if (sysOptionRequest == null) {
                    log.w("无效的引用KEY ", sysMappingColumn.getSysMappingColumnName(), " -> ", sysMappingColumn.getSysMappingColumnRequest());
                }
                sysMappingColumn.putMap("sysMappingColumnRequest", sysOptionRequest);
                sysMappingColumn.setSysMappingColumnRequest(null);
            }
        }
        //写入 表 标题
        //sysRoleTable0.putMap("sysRoleTableTitle", sysMappingTable.getSysMappingTableTitle());
        sysRoleTable0.setData(listSysRoleColumn);
        return res.success("映射信息读取成功", sysRoleTable0);
    }

    //上传文件
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
            log.d("sysFileDao", sysFileDao.toString());
            SysFile sysFile = sysFileDao.find(new SysFile().setSysFileMd5(md5));
            if (sysFile == null) {
                String md5Path = Tools.toMD5Path(md5) + fileUploadEntity.getFilename();
                sysFile = new SysFile();
                sysFile.setSysFileMd5(md5)
                        .setSysFilePath(md5Path)
                        .setSysFileTime(Tools.getTimestamp().toLocalDateTime())
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

    //读取文件  /system/v2/read/file?sysFileId=
    @DecoratorOpen
    @GetMapping(value = "read/file")
    public Object readFile(GzbJson res, SysFile sysFile, SysFileDao sysFileDao, Boolean b, Double t, Integer w, Integer h, Response response) throws Exception {
        if (sysFile == null || sysFile.toString().equals("{}")) {
            return res.fail("必要参数未填写[1000]");
        }
        if (sysFile.getSysFileId() == null && sysFile.getSysFilePath() == null && sysFile.getSysFileMd5() == null) {
            return res.fail("必要参数未填写[1001]");
        }
        sysFile = sysFileDao.find(sysFile, 60);
        if (sysFile == null) {
            return res.fail("文件不存在或无权限访问[1000]");
        }

        String path = Config.uploadDir;
        String filePath = path + "/" + sysFile.getSysFilePath();
        File file = new File(filePath);
        if (!file.exists() || !file.canRead()) {
            return res.fail("文件不存在或无权限访问[1001]");
        }
        String contentType = sysFile.getSysFileType();
        byte[] bytes = null;
        if (t != null || w != null || h != null || (b != null && b)) {
            if (t != null) {
                w = null;
                h = null;
            } else if (w != null) {
                h = null;
            }
            File fileCache = new File(filePath + (t != null ? "_" + t : "_1.0") + (w != null ? "_" + w : "_0") + (h != null ? "_" + h : "_0") + (b != null ? "_" + b : "_false") + ".cache");
            // 设置内容类型
            if (!fileCache.exists() || !fileCache.canRead()) {
                // 读取文件内容
                bytes = FileTools.readByte(file);
                if (bytes == null || bytes.length < 1) {
                    return res.fail("文件不存在或无权限访问[1002]");
                }
                bytes = resize(bytes, t, w, h, contentType);
                if (b != null && b) {
                    bytes = Base64.getEncoder().encode(bytes);
                    byte[] b01 = ("data:" + contentType + ";base64,").getBytes(Config.encoding);
                    byte[] bytes2 = new byte[b01.length + bytes.length];
                    System.arraycopy(b01, 0, bytes2, 0, b01.length);
                    System.arraycopy(bytes, 0, bytes2, b01.length, bytes.length);
                    bytes = bytes2;
                }
                FileTools.save(fileCache, bytes);
            } else {
                bytes = FileTools.readByte(fileCache);
                if (bytes == null || bytes.length < 1) {
                    return res.fail("文件不存在或无权限访问[1002]");
                }
            }

        } else {
            // 读取文件内容
            bytes = FileTools.readByte(file);
            if (bytes == null || bytes.length < 1) {
                return res.fail("文件不存在或无权限访问[1002]");
            }
        }
        if (b != null && b) {
            response.setHeader("content-type", "text/plain; charset=UTF-8");
        } else {
            if (contentType != null) {
                response.setHeader("content-type", contentType);
            } else {
                response.setHeader("content-type", "application/octet-stream");
            }
        }
        return bytes;
    }

    private byte[] resize(byte[] bytes, Double t, Integer w, Integer h, String contentType) throws IOException {
        // 压缩图片质量范围 0.1-1.0
        if (t != null && t > 0 && t <= 1) {
            if (contentType.contains("image")) {
                bytes = PicUtils.resize(bytes, t);
            } else {
                log.w("警告，文件非图片但是却被当作图片处理");
            }
        } else if (w != null && w > 0) {
            if (contentType.contains("image")) {
                bytes = PicUtils.resizeByWidth(bytes, w);
            } else {
                log.w("警告，文件非图片但是却被当作图片处理");
            }
        } else if (h != null && h > 0) {
            if (contentType.contains("image")) {
                bytes = PicUtils.resizeByHeight(bytes, h);
            } else {
                log.w("警告，文件非图片但是却被当作图片处理");
            }
        }
        return bytes;
    }

    String sql_read_per = "select sys_permission.* from sys_permission where sys_permission.sys_permission_id in" +
            "(" +
            "select sys_group_permission.sys_group_permission_pid from sys_group_permission where sys_group_permission.sys_group_permission_gid in" +
            "(" +
            "select sys_role_group.sys_role_group_gid from sys_role_group where sys_role_group_rid = ?" +
            ")" +
            ") and sys_permission_type = ?";


    //读取index 界面信息
    @DecoratorOpen
    @GetMapping(value = "read/permission")
    public Object readPermission(SysUsers sysUsers, GzbJson gzbJson) throws Exception {
        //获取角色 对应权限组
        List<SysPermission> list = null;
        if (sysUsers.getSysUsersType() == 4L) {
            list = sysPermissionDao.query(new SysPermission().setSysPermissionType(1L), "sys_permission_sort,sys_permission_id", "asc", 0, 0, 10);
        } else {
            list = sysPermissionDao.query(sql_read_per, new Object[]{sysUsers.getSysUsersRole(), 1}, "sys_permission_sort,sys_permission_id", "asc", 0, 0, 10);
        }
        if (list.size() == 0) {
            return gzbJson.fail("未查询到已获取的权限");
        }
        return gzbJson.success("权限查询成功", list);
    }

    String sql_read_per2 = "select sys_permission.* from sys_permission where sys_permission.sys_permission_id in" +
            "(" +
            "select sys_group_permission.sys_group_permission_pid from sys_group_permission where sys_group_permission.sys_group_permission_gid = ?" +
            ")";

    /// read/permission/all?gid=
    @DecoratorOpen
    @GetMapping(value = "read/permission/all")
    public Object readPermissionAll(SysUsers sysUsers, GzbJson gzbJson, Long gid) throws Exception {
        if (sysUsers.getSysUsersType() != 4L) {
            return gzbJson.fail("越权操作");
        }
        //获取角色 对应权限组
        List<SysPermission> list_all = null;
        List<SysPermission> list_group = null;
        if (gid == null) {
            return gzbJson.fail("请指定组");
        }
        list_all = sysPermissionDao.query(new SysPermission(), "sys_permission_sort,sys_permission_id", "asc", 0, 0, -1);
        List<SysPermission> list_all2 = new ArrayList<>();
        Map<Object, SysPermission> map0 = new HashMap<>();
        list_group = sysPermissionDao.query(sql_read_per2, new Object[]{gid}, "sys_permission_sort,sys_permission_id", "asc", 0, 0, -1);
        for (SysPermission sysPermission : list_group) {
            map0.put(sysPermission.getSysPermissionId(), sysPermission);
        }
        for (SysPermission sysPermission : list_all) {
            if (map0.get(sysPermission.getSysPermissionId()) == null) {
                list_all2.add(sysPermission);
            }
        }
        Map<Object, List<SysPermission>> map = new HashMap<>();
        map.put("all", list_all2);
        map.put("group", list_group);
        return gzbJson.success("权限查询成功", map);
    }


    /// 授权或取消授权  type=1授权给组  type=2取消对组的授权
    @DecoratorOpen
    @GetMapping(value = "update/permission/all")
    public Object updatePermissionAll(SysUsers sysUsers, GzbJson gzbJson, Long gid, Long[] pid, int type) throws Exception {
        if (sysUsers.getSysUsersType() != 4L) {
            return gzbJson.fail("越权操作");
        }
        if (pid == null || gid == null) {
            return gzbJson.fail("缺乏必要参数");
        }
        SysGroup sysGroup = sysGroupDao.find(new SysGroup().setSysGroupId(gid));
        if (sysGroup == null) {
            return gzbJson.fail("组不存在");
        }
        DateTime dateTime = new DateTime();
        for (Long id : pid) {
            SysPermission sysPermission = sysPermissionDao.find(new SysPermission().setSysPermissionId(id));
            if (sysPermission == null) {
                return gzbJson.fail("权限不存在");
            }
            if (type == 1) {
                if (sysGroupPermissionDao.find(new SysGroupPermission().setSysGroupPermissionPid(sysPermission.getSysPermissionId()).setSysGroupPermissionGid(gid)) == null) {
                    SysGroupPermission sysGroupPermission = new SysGroupPermission();
                    sysGroupPermission.setSysGroupPermissionGid(gid)
                            .setSysGroupPermissionPid(sysPermission.getSysPermissionId())
                            .setSysGroupPermissionTime(Tools.getTimestamp().toLocalDateTime());
                    sysGroupPermissionDao.save(sysGroupPermission);
                }
            }
            if (type == 2) {
                if (sysGroupPermissionDao.find(new SysGroupPermission().setSysGroupPermissionPid(sysPermission.getSysPermissionId()).setSysGroupPermissionGid(gid)) != null) {
                    SysGroupPermission sysGroupPermission = new SysGroupPermission();
                    sysGroupPermission.setSysGroupPermissionGid(gid)
                            .setSysGroupPermissionPid(sysPermission.getSysPermissionId());
                    sysGroupPermissionDao.delete(sysGroupPermission);
                }
            }

        }
        return gzbJson.success("权限更新成功");
    }

    @DecoratorOpen
    @GetMapping(value = "read/group/all")
    public Object readGroupAll(SysUsers sysUsers, GzbJson gzbJson, Long rid) throws Exception {
        if (sysUsers.getSysUsersType() != 4L) {
            return gzbJson.fail("越权操作");
        }
        //获取角色 对应权限组
        List<SysGroup> list_all = null;
        List<SysGroup> list_group = null;
        if (rid == null) {
            return gzbJson.fail("请指定角色");
        }
        list_all = sysGroupDao.query(new SysGroup(), "sys_group_id", "asc", 0, 0, -1);
        List<SysGroup> list_all2 = new ArrayList<>();
        Map<Object, SysGroup> map0 = new HashMap<>();
        list_group = sysGroupDao.query("select * from sys_group where sys_group_id in (select sys_role_group_gid from sys_role_group where sys_role_group_rid = ?)",
                new Object[]{rid}, "sys_group_id", "asc", 0, 0, -1);
        for (SysGroup sysGroup : list_group) {
            map0.put(sysGroup.getSysGroupId(), sysGroup);
        }
        for (SysGroup sysGroup : list_all) {
            if (map0.get(sysGroup.getSysGroupId()) == null) {
                list_all2.add(sysGroup);
            }
        }
        Map<Object, List<SysGroup>> map = new HashMap<>();
        map.put("all", list_all2);
        map.put("group", list_group);
        return gzbJson.success("权限查询成功", map);
    }


    /// 授权或取消授权  type=1授权给组  type=2取消对组的授权
    @DecoratorOpen
    @GetMapping(value = "update/group/all")
    public Object updateGroupAll(SysUsers sysUsers, GzbJson gzbJson, Long rid, Long[] gid, int type) throws Exception {
        if (sysUsers.getSysUsersType() != 4L) {
            return gzbJson.fail("越权操作");
        }
        if (rid == null || gid == null) {
            return gzbJson.fail("缺乏必要参数");
        }
        SysRole sysRole = sysRoleDao.find(new SysRole().setSysRoleId(rid));
        if (sysRole == null) {
            return gzbJson.fail("角色不存在");
        }

        for (Long id : gid) {
            SysGroup sysGroup = sysGroupDao.find(new SysGroup().setSysGroupId(id));
            if (sysGroup == null) {
                return gzbJson.fail("权限不存在");
            }
            if (type == 1) {
                SysRoleGroup sysRoleGroup=new SysRoleGroup().setSysRoleGroupRid(rid).setSysRoleGroupGid(id);
                if (sysRoleGroupDao.find(sysRoleGroup) == null) {
                    sysRoleGroupDao.save(sysRoleGroup);
                }
            }
            if (type == 2) {
                SysRoleGroup sysRoleGroup=new SysRoleGroup().setSysRoleGroupRid(rid).setSysRoleGroupGid(id);
                if (sysRoleGroupDao.find(sysRoleGroup) != null) {
                    sysRoleGroupDao.delete(sysRoleGroup);
                }
            }

        }
        return gzbJson.success("权限更新成功");
    }


}
