package gzb.frame.factory.v4;

import gzb.exception.GzbException0;
import gzb.frame.factory.ClassLoad;
import gzb.frame.factory.ClassTools;
import gzb.tools.Config;
import gzb.tools.FileTools;
import gzb.tools.Tools;
import gzb.tools.http.HTTP_V3;
import gzb.tools.log.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateServer {
    String ip;
    int port;
    String hotClusterKey;
    Long hotClusterId;
    Long name;
    long version = 0L;
    String base_server = "";
    String base_data = "";
    Log log = Log.log;

    public UpdateServer() {
        this(Config.code_http_ip, Config.code_http_port, Config.code_http_key, Config.code_http_aid, Config.server_name);
    }
    public UpdateServer(String ip, int port, String hotClusterKey, Long hotClusterId, Long name) {
        if (ip==null) {
            throw new GzbException0("ip不允许为空");
        }
        if (port<0) {
            throw new GzbException0("port必须大于0");
        }
        if (hotClusterKey==null) {
            throw new GzbException0("hotClusterKey 不能为空");
        }
        if (hotClusterId==null) {
            throw new GzbException0("hotClusterId 不能为空");
        }
        if (name==null) {
            throw new GzbException0("name 不能为空");
        }
        this.ip = ip;
        this.port = port;
        this.hotClusterKey = hotClusterKey;
        this.hotClusterId = hotClusterId;
        this.name = name;
        base_server = "http://" + ip + ":" + port + "/system/v1.0.1/api/";
        base_data = "hotClusterId=" + hotClusterId + "&hotClusterKey=" + hotClusterKey + "&serverName=" + name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHotClusterKey() {
        return hotClusterKey;
    }

    public void setHotClusterKey(String hotClusterKey) {
        this.hotClusterKey = hotClusterKey;
    }

    public Long getHotClusterId() {
        return hotClusterId;
    }

    public void setHotClusterId(Long hotClusterId) {
        this.hotClusterId = hotClusterId;
    }

    public Long getName() {
        return name;
    }

    public void setName(Long name) {
        this.name = name;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    //装载更新列表
    public boolean readListCode(Map<String, String> map, String code) {
        if (code == null || code.isEmpty()) {
            System.out.println("code null " + code);
            return false;
        }
        String name = ClassTools.extractPublicClassName(code);
        if (map.get(name) != null) {
            System.out.println("code 重复 " + code);
        }
        map.put(name, code);
        return true;
    }

    //创建新版本
    public long create_version() throws Exception {
        String url = base_server + "create/version";
        String data = base_data;
        HTTP_V3 httpV3 = new HTTP_V3();
        httpV3.get(url, data);
        Map<String, Object> map = Tools.jsonToMap(httpV3.asString());
        if (map != null && map.get("code").toString().equals("1")) {
            return Long.parseLong(((Map<String, Object>) map.get("data")).get("hotFileVersionId").toString());
        }
        return -1;
    }

    //注册服务器 并指定状态  isLogin 1在线 0离线
    public boolean reg_server(Long isLogin) throws Exception {
        String url = base_server + "reg/server";
        String data = base_data + "&isLogin=" + isLogin;
        HTTP_V3 httpV3 = new HTTP_V3();
        httpV3.get(url, data);
        Map<String, Object> map = Tools.jsonToMap(httpV3.asString());
        return map.get("code").toString().equals("1");
    }

    //读取待更新文件列表
    public List<Map<String, Object>> readFileList(long this_version) throws Exception {
        String url = base_server + "update/examine";
        String data = base_data + "&version=" + this_version;
        HTTP_V3 httpV3 = new HTTP_V3();
        httpV3.get(url, data);
        String resData=httpV3.asString();
        Map<String, Object> map = Tools.jsonToMap(resData);
        log.t("readFileList", url,data,map.get("code"),!map.get("code").toString().equals("1"),map.get("data") == null);
        if (map == null || map.get("code") == null || map.get("data") == null || !map.get("code").toString().equals("1")) {
            return null;
        }
        Object object = map.get("data");
        Map<String, Object> map0 = null;
        if (object instanceof Map) {
            map0 = (Map<String, Object>) object;
        }
        if (map0 == null) {
            return null;
        }
        Object hotServerCurrentOnline = map0.get("hotServerCurrentOnline");
        if (hotServerCurrentOnline == null) {
            return null;
        }
        version = Long.parseLong(hotServerCurrentOnline.toString());
        List<Map<String, Object>> list1 = null;
        Object object0 = map0.get("data");
        if (object0 instanceof List) {
            list1 = (List<Map<String, Object>>) object0;
        }
        return list1;
    }

    public int uploadFiles(long hotFileVersionId) throws Exception {
       return uploadFiles(Config.codeDir, Config.thisPath(),hotFileVersionId);
    }
    public int uploadFiles(String folders, String thisPath, long hotFileVersionId) throws Exception {
        String url = base_server + "uploads";
        String[] arr1 = folders.split(",");
        HTTP_V3 httpV3 = new HTTP_V3();
        int num = 0;
        for (String string : arr1) {

            List<File> list1 = FileTools.subFileAll(new File(string.trim()),2,".java");
            for (File file : list1) {
                Map<String, List<File>> files = new HashMap<>();
                List<File> list0 = new ArrayList<>();
                files.put("files", list0);
                list0.add(file);
                String t01 = Tools.pathFormat(file.getParentFile().getPath()) + file.getName();
                String t02 = Tools.pathFormat(thisPath);
                t01 = t01.replace(t02, "");
                String data = base_data + "&hotFileVersionId=" + hotFileVersionId + "&paths=" + t01 + "&";
                httpV3.request(url, "POST", data, null, files, 10000L);
                Map<String, Object> res = Tools.jsonToMap(httpV3.asString());
                if (res != null && res.get("code") != null && res.get("code").toString().equals("1")) {
                    //System.out.println("上传成功:"+file.getPath());
                    num++;
                } else {
                    System.err.println("上传失败:" + file.getPath() + "\n" + res);
                }
            }

        }
        System.out.println("上传成功:" + num);
        return num;
    }

}
