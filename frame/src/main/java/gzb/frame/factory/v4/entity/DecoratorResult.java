package gzb.frame.factory.v4.entity;

public class DecoratorResult {
    //true 拦截 false 不拦截
    public boolean intercept = false;
    //http状态码 可修改
    public int responseCode = 200;
    //http响应 后处理器可修改可修改 前处理器修改没意义
    public Object responseData = null;
    //注入数据 后方所有处理器 都会拥有该数据
    public Object injectDataObject = null;

    //拦截 终止于当前处理器
    public void intercept(int responseCode,Object responseData) {
        this.responseCode = responseCode;
        this.responseData = responseData;
        intercept = true;
    }

    //放行 进入下一个处理器
    public void release() {
        intercept = false;
    }

    //注入数据给后方处理器
    public void injectData(Object injectDataObject) {
        this.injectDataObject = injectDataObject;
    }

}
