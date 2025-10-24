package gzb.frame.db;

import gzb.exception.GzbException0;
import gzb.frame.PublicData;
import gzb.frame.annotation.*;
import gzb.entity.DataBaseEventEntity;
import gzb.frame.factory.ClassTools;
import gzb.frame.factory.GzbOneInterface;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;
import gzb.tools.json.GzbJson;
import gzb.tools.log.Log;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventFactoryImpl implements EventFactory {
    //日志接口
    private Log log = null;
    //service 对象工厂
    private Map<String, Object> mapObject = null;
    //某个实体类数据库 事件  查询 新增 修改 删除   需要传递 被操作的实体类
    //1查询 2插入 3删除 4修改
    private Map<String, List<DataBaseEventEntity>> map_mapping = new ConcurrentHashMap<>();
    //注册事件大于0 则自动开启
    private boolean open = false;
    //添加事件到队列   感觉无需异步 因此取消  因为dao 内部 有异步方法 可以在执行的时候选择 异步执行 某一个sql  同时  同步执行某个sql  也可全部同步 全部异步
    //ConcurrentLinkedQueue<String> eventList=new ConcurrentLinkedQueue<>();
    //为了兼容性 调用代码 为空 会报错 懒得改了
    private final Map<String, List<Object>> requestMap = new ConcurrentHashMap<>();

    public EventFactoryImpl(Map<String, Object> mapObject, Log log) {
        this.mapObject = mapObject;
        this.log = log;
    }


    //事件触发 方法 硬编码版本  主要供 框架调度
    public boolean eventSelect(Object entity_obj, boolean before) throws Exception {
        return event(entity_obj, 1, before);
    }

    public boolean eventSave(Object entity_obj, boolean before) throws Exception {
        return event(entity_obj, 2, before);
    }

    public boolean eventDelete(Object entity_obj, boolean before) throws Exception {
        return event(entity_obj, 3, before);
    }

    public boolean eventUpdate(Object entity_obj, boolean before) throws Exception {
        return event(entity_obj, 4, before);
    }

    //触发事件  无注册事件则跳过
    public boolean event(Object entity_obj, int type, boolean before) throws Exception {
        if (!open) {
            return true;
        }
        Class<?> aClass = entity_obj.getClass(); //entity_obj 不允许为空  为空说明是预料外的情况
        List<DataBaseEventEntity> list = map_mapping.get(aClass.getName());
        if (list == null) {
            return true;
        }
        for (DataBaseEventEntity dataBaseEventEntity : list) {
            //检查事件类型是否匹配
            if (dataBaseEventEntity.type == type) {
                //检查 主操作 执行之前 或 执行之后 是否匹配
                if (dataBaseEventEntity.before == before) {
                    Integer dep = PublicData.depth.get();
                    PublicData.depth.set((dep == null ? 1 : dep) + 1);//深度+1
                    if (dep != null && dep > dataBaseEventEntity.depth) {
                        throw new GzbException0(
                                "数据库事件传播层数异常",
                                "超过预设深度",
                                dataBaseEventEntity.depth,
                                "当前深度",
                                dep);
                    }
                    Object[] objects = PublicData.context.get();
                    Object[] newArray = Arrays.copyOf(objects, objects.length + 1);
                    newArray[newArray.length - 1] = entity_obj;
                    //无返回值 错误将会中断后续流程,因为不中断的话 可能掩盖问题 或者出现不可预知问题
                    Object object = dataBaseEventEntity.run._gzb_call_x01(
                            dataBaseEventEntity.met_id,//方法调用标识
                            mapObject,//公共 service 单例对象引用
                            (Request) objects[3],
                            (Response) objects[4],
                            (Map<String, List<Object>>) objects[5],
                            (GzbJson) objects[1],
                            (Log) objects[2],
                            newArray//私有对象
                    );
                    //如果返回 false 中断
                    if (object instanceof Boolean && !(Boolean) object) {
                        return false;
                    }

                    if (dep == null) {
                        PublicData.depth.remove();
                    } else {
                        PublicData.depth.set(dep);//深度-1
                    }

                }

            }
        }
        return true;
    }

    //注册事件
    //method 方法对象
    //code 方法所在类完整代码
    public void register(Class<?> aClass, String code) throws Exception {
        Method[] methods = aClass.getDeclaredMethods();
        GzbOneInterface obj = null;
        for (Method method : methods) {
            log.t("register method ", method);
            DataBaseEventSelect dataBaseEventSelect = method.getAnnotation(DataBaseEventSelect.class);
            DataBaseEventSave dataBaseEventSave = method.getAnnotation(DataBaseEventSave.class);
            DataBaseEventDelete dataBaseEventDelete = method.getAnnotation(DataBaseEventDelete.class);
            DataBaseEventUpdate dataBaseEventUpdate = method.getAnnotation(DataBaseEventUpdate.class);
            Transaction transaction = method.getAnnotation(Transaction.class);
            boolean isOpen = false; //这里设置没用而是 代码生成决定
            Class<?> declaringClass = method.getDeclaringClass();
            String sign = ClassTools.getSing(method, declaringClass);
            int id = ClassTools.getSingInt(method, declaringClass);
            if (dataBaseEventSelect == null && dataBaseEventSave == null && dataBaseEventDelete == null && dataBaseEventUpdate == null) {
                continue;
            }
            if (obj == null) {
               /* obj = (GzbOneInterface) mapObject.get(declaringClass.getName());*/
                obj = (GzbOneInterface) ClassTools.gen_call_code_v4_object(declaringClass, code);
                mapObject.put(declaringClass.getName(), obj);
            }
            //完成动态编译 并注册
            if (dataBaseEventSelect != null) {
                Class<?>[] types = method.getParameterTypes();
                String[] names = ClassTools.getParameterNamesByAsm(method, types).toArray(new String[types.length]);
                putEvent(new DataBaseEventEntity(declaringClass, method, types, names, id, sign, obj, dataBaseEventSelect.entity(), 1,
                        dataBaseEventSelect.executionBefore(), isOpen, dataBaseEventSelect.depth()));
            } else if (dataBaseEventSave != null) {
                Class<?>[] types = method.getParameterTypes();
                String[] names = ClassTools.getParameterNamesByAsm(method, types).toArray(new String[types.length]);
                putEvent(new DataBaseEventEntity(declaringClass, method, types, names, id, sign, obj, dataBaseEventSave.entity(), 2,
                        dataBaseEventSave.executionBefore(), isOpen, dataBaseEventSave.depth()));
            } else if (dataBaseEventDelete != null) {
                Class<?>[] types = method.getParameterTypes();
                String[] names = ClassTools.getParameterNamesByAsm(method, types).toArray(new String[types.length]);
                putEvent(new DataBaseEventEntity(declaringClass, method, types, names, id, sign, obj, dataBaseEventDelete.entity(), 3,
                        dataBaseEventDelete.executionBefore(), isOpen, dataBaseEventDelete.depth()));
            } else {
                Class<?>[] types = method.getParameterTypes();
                String[] names = ClassTools.getParameterNamesByAsm(method, types).toArray(new String[types.length]);
                putEvent(new DataBaseEventEntity(declaringClass, method, types, names, id, sign, obj, dataBaseEventUpdate.entity(), 4,
                        dataBaseEventUpdate.executionBefore(), isOpen, dataBaseEventUpdate.depth()));
            }
        }

    }

    private void putEvent(DataBaseEventEntity dataBaseEventEntity) throws Exception {
        if (dataBaseEventEntity.depth < 1) {
            return;
        }
        log.t("注册数据库事件", dataBaseEventEntity.entityClass, dataBaseEventEntity.type, dataBaseEventEntity.before);
        open = true;
        List<DataBaseEventEntity> list = map_mapping.get(dataBaseEventEntity.entityClass.getName());
        if (list == null) {
            list = new ArrayList<>();
            map_mapping.put(dataBaseEventEntity.entityClass.getName(), list);
        }
        for (int i = 0; i < list.size(); i++) {
            DataBaseEventEntity dataBaseEventEntity1 = list.get(i);
            if (Objects.equals(dataBaseEventEntity.met_sign, dataBaseEventEntity1.met_sign)) {
                list.set(i, dataBaseEventEntity);
                return;
            }
        }
        list.add(dataBaseEventEntity);
    }
}
