

# 2025-10-02
### 功能新增
* 新增
* 可以指定事件传播深度 防止循环传播

### 性能优化
* 1.优化实体类装在逻辑
* 2.优化web请求参数解析逻辑
* 3.更改动态编译的部分代码效率
* 4.数据库事件的一些链路精简

### 修复已知BUG

* 1.一些BUG小修复
* 2.数据库事件的BUG修复



# 2025-09-30

### 性能优化

* 1.性能提升，0微秒比例上升至60-80%

```java

@DecoratorOpen
@Limitation(10)
@Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")})
@GetMapping("/test1")
public Object test1(String sysUsersAcc, GzbJson gzbJson) {
    return gzbJson.success(sysUsersAcc);
}
```

### 修复已知BUG

* 1.一些BUG修复

### 功能新增

* 1.支持注册 数据库事件 类似触发器 但是更加灵活  可以给每个实体类绑定 select  sava update delete 事件

# 2025-09-27

### 性能优化

* 1.一些性能优化

## 一个基础请求耗时小于1微秒 比例达到 99.99% (框架耗时30% 业务代码70% ，底层耗时没有计算)

```java

@DecoratorOpen
@Limitation(10)
@Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")})
@GetMapping("/test1")
public Object test1(String sysUsersAcc, GzbJson gzbJson) {
    return gzbJson.success(sysUsersAcc);
}
```

### 修复已知BUG

* 1.一些BUG修复

# 2025-09-26

### 修复已知BUG

* 1.上传文件未及时删除，可能泄露
* 2.修复 FileUploadEntity 参数接收不到文件的情况
* 3.多装饰器的调用顺序通过 sort指定 防止调用顺序和预期不符 @DecoratorStart(sort = 0) @DecoratorEnd(sort = 0) sort越小越先执行
* 4.修复多装饰器时，部分装饰器失效的问题
* 5.修复 gzb.system.server.http.post.size 配置项不生效

### 功能改进

* 1.当检测到 数据库异步线程被设置为0的时候，弹出警告，防止出现难以排查问题
* 2.配置文件中配置的所有 classpath:/xx,this:/xx 目录，不存在则 自动创建，防止出现意外错误
* 3.增加更详细的错误输出
