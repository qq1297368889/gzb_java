package com.acquisition.api;

import com.acquisition.entity.RoomUser;
import gzb.frame.annotation.*;
import gzb.tools.Config;
import gzb.tools.JSONResult;
import gzb.tools.LockFactory;
import gzb.tools.Tools;
import gzb.tools.cache.Cache;
import gzb.tools.img.PicUtils;
import gzb.tools.log.Log;
import gzb.tools.ocr.OCR_TESS;
import gzb.tools.ocr.RecognitionResult;

import java.io.File;
import java.util.List;
import java.util.concurrent.locks.Lock;

@Controller
@RequestMapping(value = "/ocr", header = "content-type:application/json;charset=UTF-8")
public class OCRAction {
    @Resource(value = "gzb.tools.log.LogImpl")
    Log log;
    OCR_TESS ocrTess=null;

    @DecoratorOpen //开启装饰器
    @GetMapping(value = "test")//映射 get /ocr/test
    @Limitation(1) //限制并发 1
    public Object test(JSONResult result) throws Exception {
        Tools.sleep(1000*5);
        return result.success("ok");
    }
    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 file 的参数
     * /ocr/find/text?file=
     * */
    @DecoratorOpen //开启装饰器
    @PostMapping(value = "find/text")//映射 post /ocr/find/text
    @Limitation(2) //限制并发 2
    public Object find(JSONResult result, File file,String langType) throws Exception {
        if (file==null) {
            return result.fail("未检测到文件-1000");
        }
        String dbPath = Config.get("ocr.tess.database.dir");
        Object object = Cache.gzbMap.get(dbPath);
        if (object==null) {
            Lock lock= LockFactory.getLock("ocr/find/text");
            lock.lock();
            try {
                object = Cache.gzbMap.get(dbPath);
                if (object==null) {
                    object = new OCR_TESS(dbPath);
                    Cache.gzbMap.put(dbPath, object);
                }
            }catch (Exception e){
                log.e(e);
                return result.fail("模型初始化失败-1001");
            }finally {
                lock.unlock();
            }
        }
        if (object instanceof OCR_TESS) {
            ocrTess=(OCR_TESS)object;
        }
        if (ocrTess==null) {
            return result.fail("模型初始化失败-1002");
        }
        byte[]bytes = Tools.fileReadByte(file);
        long start= System.currentTimeMillis();
        List<RecognitionResult> list=ocrTess.ocr(bytes,langType);
        long end= System.currentTimeMillis();
        return result.success("识别完成",list)
                ._data("consumingTime",(end-start)+"")
                ._data("time",System.currentTimeMillis()+"");
    }

}
