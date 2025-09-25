

# 2025-09-26
### 修复 已知BUG
* 1.上传文件未及时删除，可能泄露
* 2.修复 FileUploadEntity 参数接收不到文件的情况
* 3.多装饰器的调用顺序通过 sort指定 防止调用顺序和预期不符 @DecoratorStart(sort = 0) @DecoratorEnd(sort = 0) sort越小越先执行
* 4.修复多装饰器时，部分装饰器失效的问题
* 5.修复 gzb.system.server.http.post.size 配置项不生效
### 功能改进
* 1.当检测到 数据库异步线程被设置为0的时候，弹出警告，防止出现难以排查问题
* 2.配置文件中配置的所有 classpath:/xx,this:/xx 目录，不存在则 自动创建，防止出现意外错误
* 3.增加更详细的错误输出
