package gzb.tools;

import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JSONResult {

    public static String codeText;
    public static String messageText;
    public static String dataText;
    public static String timeText;
    public static String jumpText;
    public static String pageText;
    public static String sizeText;
    public static String totalText;
    public static Integer codeSuccess;
    public static Integer codeFail;
    public static Integer codeError;
    public static Integer codeJump;

    public Result json = new ResultImpl();

    public static void main(String[] args) {
        JSONResult jsonResult = new JSONResult();
        System.out.println(jsonResult.success("true", new ArrayList<Object>()));
        System.out.println(String.class.getName());
    }

    public JSONResult() {
        json.set("code", "0");
    }

    static {
        //stateText = Config.get("json.code", "code");

        codeText = Config.get("json.code", "code");
        messageText = Config.get("json.message", "message");
        dataText = Config.get("json.data", "data");
        timeText = Config.get("json.time", "time");
        jumpText = Config.get("json.jump", "path");
        pageText = Config.get("json.page", "page");
        sizeText = Config.get("json.size", "size");
        totalText = Config.get("json.total", "total");
        codeSuccess = Config.getInteger("json.code.success", 1);
        codeFail = Config.getInteger("json.code.fail", 2);
        codeError = Config.getInteger("json.code.error", 3);
        codeJump = Config.getInteger("json.code.jump", 4);
    }

    public static String successSave() {
        return success();
    }

    public static String failSave() {
        return fail();
    }

    public static String successDelete() {
        return success();
    }

    public static String failDelete() {
        return fail();
    }

    public static String successUpdate() {
        return success();
    }

    public static String failUpdate() {
        return fail();
    }

    public static String success() {
        return new JSONResult()._success()._message("执行成功").toString();
    }

    public static String fail() {
        return new JSONResult()._fail()._message("执行失败").toString();
    }

    public static String err() {
        return new JSONResult()._err()._message("执行错误").toString();
    }

    public static String jump() {
        return new JSONResult()._success()._message("需要跳转").toString();
    }

    @Override
    public String toString() {
        return json.toString();
    }

    public JSONResult initJson(Integer state, String message, String stringData, List<?> data, String jump, Integer page, Integer size, Integer total) {
        json.set(codeText, state);
        json.set(messageText, message);
        if (stringData == null) {
            json.set(dataText, data);
        } else {
            json.set(dataText, stringData);
        }
        json.set(jumpText, jump);
        json.set(pageText, page);
        json.set(sizeText, size);
        json.set(totalText, total);
        json.set(timeText, System.currentTimeMillis());
        return this;
    }

    public <T> List<T> getList() {
        return json.getObject(dataText);
    }

    public Map<String, Object> getMap() {
        return json.getObject(dataText);
    }

    public <T> T get(T t) {
        return (T) json.get(dataText);
    }

    public Object get(String key) {
        return json.get(key);
    }

    public <T> T get(String key, T t) {
        return (T) json.get(key);
    }

    public JSONResult _success() {
        json.set(codeText, codeSuccess);
        return this;
    }

    public JSONResult _fail() {
        json.set(codeText, codeFail);
        return this;
    }

    public JSONResult _err() {
        json.set(codeText, codeError);
        return this;
    }

    public JSONResult _jump() {
        json.set(codeText, codeJump);
        return this;
    }

    public JSONResult _jump(String url) {
        json.set(codeText, codeJump);
        json.set(jumpText, url);
        return this;
    }

    public JSONResult _message(String message) {
        json.set(messageText, message);
        return this;
    }

    public JSONResult _data(String data) {
        json.set(dataText, data);
        return this;
    }

    public JSONResult _dataList(List<?> data) {
        json.set(dataText, data);
        return this;
    }

    public JSONResult _dataMap(Map<?, ?> data) {
        json.set(dataText, data);
        return this;
    }

    public JSONResult _data(String key, String data) {
        json.set(key, data);
        return this;
    }

    public JSONResult _dataList(String key, List<?> data) {
        json.set(key, data);
        return this;
    }

    public JSONResult _dataMap(String key, Map<?, ?> data) {
        json.set(key, data);
        return this;
    }

    public JSONResult _dataObject(Object data) {
        json.set(dataText, data);
        return this;
    }

    public JSONResult put(String key, Map<?, ?> data) {
        json.set(key, data);
        return this;
    }

    public JSONResult put(String key, List<?> data) {
        json.set(key, data);
        return this;
    }

    public JSONResult put(String key, String data) {
        json.set(key, data);
        return this;
    }

    public JSONResult paging(List<?> list, Integer page, Integer size, Integer maxPage, Integer maxSize) {
        int start = 0;
        if (page == null || page < 0) {
            page = 1;
        }
        if (size == null || size < 0) {
            size = 10;
        }
        if (maxPage > 0 && page > maxPage) {
            page = maxPage;
        }
        if (maxSize > 0 && size > maxSize) {
            size = maxSize;
        }
        if (page > 1) {
            start = (page - 1) * size;
        }
        List<Object> listThis = new ArrayList<>();
        for (int i = start; i < list.size(); i++) {
            listThis.add(list.get(i));
            if (size > 0 && listThis.size() == size) {
                break;
            }
        }
        return initJson(codeSuccess, "分页查询成功", null, listThis, null, page, size, list.size());
    }

    public JSONResult paging(List<?> list, Integer page, Integer size) {
        return paging(list, page, size, 0, 0);
    }

    public JSONResult paging(List<?> list, Integer page, Integer size, int total) {
        return initJson(codeSuccess, "分页查询成功", null, list, null, page, size, total);
    }

    public JSONResult success(String message) {
        return _success()._message(message);
    }

    public JSONResult success(String message, String data) {
        return _success()._message(message)._data(data);
    }

    public JSONResult success(String message, List<?> data) {
        return _success()._message(message)._dataList(data);
    }

    public JSONResult success(String message, Map<?, ?> data) {
        return _success()._message(message)._dataMap(data);
    }

    public JSONResult successObject(String message, Object data) {
        return _success()._message(message)._dataObject(data);
    }


    public JSONResult fail(String message) {
        return _fail()._message(message);
    }

    public JSONResult fail(String message, String data) {
        return _fail()._message(message)._data(data);
    }

    public JSONResult fail(String message, List<Object> data) {
        return _fail()._message(message)._dataList(data);
    }

    public JSONResult fail(String message, Map<?, ?> data) {
        return _fail()._message(message)._dataMap(data);
    }


    public JSONResult err(String message) {
        return _err()._message(message);
    }

    public JSONResult err(String message, String data) {
        return _err()._message(message)._data(data);
    }

    public JSONResult err(String message, List<Object> data) {
        return _err()._message(message)._dataList(data);
    }

    public JSONResult err(String message, Map<?, ?> data) {
        return _err()._message(message)._dataMap(data);
    }


    public JSONResult jump(String message, String url) {
        return _jump(url)._message(message);
    }

    public JSONResult jump(String message, String url, List<Object> data) {
        return _jump(url)._message(message)._dataList(data);
    }

    public JSONResult jump(String message, String url, String data) {
        return _jump(url)._message(message)._data(data);
    }

    public JSONResult jump(String message, String url, Map<?, ?> data) {
        return _jump(url)._message(message)._dataMap(data);
    }

}
