package gzb.frame.netty.action;

import gzb.frame.annotation.*;
import gzb.tools.Config;
import gzb.tools.FileTools;
import gzb.tools.Tools;
import gzb.tools.json.GzbJson;

import java.io.File;
import java.util.Objects;
/// 继承本类 即可支持网络热更新
/// 不提供版本切换 切换应该由调用者保证 本加载可确保编译不成功不会导致老版本丢失 但是编译成功后会覆盖老版本 需要调用者做好备份
public class SystemClassLoadApi {
    /// 加载类  需要确保加密秘钥和配置文件匹配 以及上传的文件是合法的类文件 以及类文件的命名和包路径正确 以免出现类冲突和安全问题 之所以这里不提供aes加密相关信息
    /// 是为了防止数据嗅探和中间人攻击 以及简化接口 让调用者自己做好安全措施 以免出现安全问题
    /// 本接口不对外提供服务 请手动创建控制器继承调用 或自己实现
    /// 需要确保 gzb.system.code.file.dir 是有效目录
    /// 需要确保 gzb.system.hot.update.token 和 传入token 一致
    /// 如需取消某个类的服务请传入一个不被容器扫描的类文件 以覆盖原有类文件 以达到删除的效果
    /// name 文件名 请传入类全名 不含 .java后缀
    /// file 文件内容的base64形式
    /// sign file+aes_pwd+aes_iv  pwd 和 iv 可以为空
    @PostMapping("hot/update/upload/v1")
    @Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")})
    public Object loadJavaClassV1(String name,String file,String sign, GzbJson gzbJson) {
        if (name==null) {
            return gzbJson.fail("文件名不允许为空");
        }
        if (file==null) {
            return gzbJson.fail("文件内容不允许为空");
        }
        if (sign==null) {
            return gzbJson.fail("签名不允许为空");
        }
        String pwd=Config.codePwd;
        String iv=Config.codeIv;
        String sign_server= Tools.textToMd5(file+   (pwd == null ? "" : pwd) + (iv == null ? "" : iv));
        if (!Objects.equals(sign_server, sign)) {
            return gzbJson.fail("签名不匹配");
        }
        String path=Config.codeDir.split(",")[0].trim();
        String newPath=path+"/"+(name.replaceAll("\\.","/"))+".java";
        newPath= Tools.pathFormat(newPath);
        File file1=new File(newPath);
        if (!file1.exists()) {
            FileTools.createFile(file1);
        }
        FileTools.save(file1,Tools.textBase64DecoderByte(file));
        return gzbJson.success("类已就绪,后台线程正在加载");
    }
}
