package gzb.frame.server.http.action;

import gzb.entity.FileUploadEntity;
import gzb.frame.annotation.*;
import gzb.tools.Config;
import gzb.tools.FileTools;
import gzb.tools.json.GzbJson;

import java.io.File;

/// 不提供版本切换 切换应该由调用者保证 本加载可确保编译不成功不会导致老版本丢失 但是编译成功后会覆盖老版本 需要调用者做好备份
public class SystemClassLoadApi {
    /// 加载类  需要确保加密秘钥和配置文件匹配 以及上传的文件是合法的类文件 以及类文件的命名和包路径正确 以免出现类冲突和安全问题 之所以这里不提供aes加密相关信息
    /// 是为了防止数据嗅探和中间人攻击 以及简化接口 让调用者自己做好安全措施 以免出现安全问题
    /// 本接口不对外提供服务 请手动创建控制器继承调用 或自己实现
    /// 需要确保 gzb.system.code.file.dir 是有效目录
    /// 需要确保 gzb.system.hot.update.token 和 传入token 一致
    /// 如需取消某个类的服务请传入一个不被容器扫描的类文件 以覆盖原有类文件 以达到删除的效果
    @PostMapping("hot/update/upload")
    @Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")})
    public Object loadJavaClass(String token, FileUploadEntity[] files, GzbJson gzbJson) {
        String server_token = Config.get("gzb.system.hot.update.token");
        String server_code_dir = Config.get("gzb.system.code.file.dir");
        if (server_token == null || server_token.isEmpty()) {
            return gzbJson.fail("服务未启用");
        }
        if (token == null || token.isEmpty() || !token.equals(server_token)) {
            return gzbJson.fail("未经授权的访问");
        }
        if (files == null || files.length < 1) {
            return gzbJson.fail("未发现上传文件");
        }
        for (FileUploadEntity file : files) {
            FileTools.toNewFile(file.getFile(), new File(server_code_dir, file.getFile().getName()));
        }
        return gzbJson.success("类已就绪,后台线程正在加载");
    }
}
