package gzb.tools;

import com.google.gson.Gson;

import java.util.*;


public class JSON {
    String[] arr1 = new String[]{"\r", "\n", "\t", "\""};
    String[] arr2 = new String[]{"\\\\r", "\\\\n", "\\\\t", "\\\\\""};
    public Map<String, Object> map = new HashMap<>();
//Long[] skuId, Integer[] num, Long[] couponId, Long usersAddressId, Integer payType, String deliveryTime
    public static void main(String[] args) {
        JSON json = new JSON();

        List goods = new ArrayList();
        for (int i = 0; i < 3; i++) {
            Map<String,Object> sku=new HashMap<>();
            sku.put("skuId","10001");
            sku.put("num","1");
            List codes = new ArrayList();
            for (int i1 = 0; i1 < 7; i1++) {
                Map<String,Object> code=new HashMap<>();
                code.put("code","4000"+i1);
                codes.add(code);
            }
            sku.put("codes",codes);
            sku.put("name","刘先生");
            sku.put("phone","18888888888");
            sku.put("address","XX村XX号");
            goods.add(sku);
        }

        json.put("couponId","20001");
        json.put("usersAddressId","30001");
        json.put("payType","1");
        json.put("deliveryTime","2023-06-17 00:02:11");
        json.put("goods",goods);
        System.out.println(json);
        JSON json2 = new JSON();
        json2.loadMap(json.toJson());
        Map<String,Object> sku=(Map<String,Object>)json2.getList("goods").get(0);

        System.out.println(sku.get("name"));
        System.out.println(sku.get("phone"));
        System.out.println(sku.get("address"));
        List codes =(List) sku.get("codes");
        for (int i = 0; i < codes.size(); i++) {
            Map<String,Object> code=(Map<String,Object>)codes.get(i);
            System.out.println(code);
        }
    }

    public JSON loadMap(String jsonString) {
        Gson gson = new Gson();
        this.map = gson.fromJson(jsonString, Map.class);
        return this;
    }
    public JSON loadMap(Map map) {
        this.map = map;
        return this;
    }

    public Object get(String key) {
        Object object = map.get(key);
        if (object == null) {
            return null;
        }
        return object;
    }
    public Map<String,Object> getMap(String key) {
        Object object = map.get(key);
        if (object == null) {
            return null;
        }
        return (Map<String,Object>)object;
    }
    public GzbMap getGzbMap(String key) {
        return new GzbMap().setMap(getMap(key));
    }
    public <T>List<T> getList(String key) {
        Object object = map.get(key);
        if (object == null) {
            return null;
        }
        return (List<T>)object;
    }

    public String getString(String key) {
        Object object = map.get(key);
        if (object == null || object.toString().length() == 0) {
            return null;
        }
        return String.valueOf(object.toString());
    }

    public Integer getInteger(String key) {
        Object object = map.get(key);
        if (object == null || object.toString().length() == 0) {
            return null;
        }
        return Integer.valueOf(object.toString());
    }

    public Boolean getBoolean(String key) {
        Object object = map.get(key);
        if (object == null || object.toString().length() == 0) {
            return null;
        }
        return Boolean.valueOf(object.toString());
    }

    public Double getDouble(String key) {
        Object object = map.get(key);
        if (object == null || object.toString().length() == 0) {
            return null;
        }
        return Double.valueOf(object.toString());
    }

    public Long getLong(String key) {
        Object object = map.get(key);
        if (object == null || object.toString().length() == 0) {
            return null;
        }
        return Long.valueOf(object.toString());
    }

    public JSON put(String key, String val) {
        map.put(key, val);
        return this;
    }

    public JSON put(String key, Integer val) {
        if (val == null) {
            return this;
        }
        return put(key, val.toString());
    }

    public JSON put(String key, Long val) {
        if (val == null) {
            return this;
        }
        return put(key, val.toString());
    }

    public JSON put(String key, Boolean val) {
        if (val == null) {
            return this;
        }
        return put(key, val.toString());
    }

    public JSON put(String key, Double val) {
        if (val == null) {
            return this;
        }
        return put(key, val.toString());
    }

    public JSON put(String key, Map<?, ?> val) {
        map.put(key, val);
        return this;
    }

    public JSON put(String key, List<?> val) {
        map.put(key, val);
        return this;
    }
    public JSON put(Map<String, Object> map) {
        for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> en = it.next();
            this.map.put(en.getKey(), en.getValue());
        }
        return this;
    }
    public JSON putObject(String key, Object val) {
        map.put(key, val);
        return this;
    }

    public JSON delete(String key) {
        map.remove(key);
        return this;
    }


    @Override
    public String toString() {
        return toJson();
    }

    public String toJson() {
        return toJsonMap(map);
    }

    public String toJsonList(List<?> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (int i = 0; i < list.size(); i++) {
            Class[] class1 = list.get(i).getClass().getInterfaces();
            Class[] class2 = list.get(i).getClass().getSuperclass().getInterfaces();
            //内部元素依赖本身tostring  不做解析
            if ((class1 != null && class1.length > 0 && class1[0].getClass().equals("java.util.List")) || (class2 != null && class2.length > 0 && class2[0].getName().equals("java.util.List"))) {
                stringBuilder.append(toJsonList((List<?>) list.get(i)));
                stringBuilder.append(",");
            } else if ((class1 != null && class1.length > 0 && class1[0].getName().equals("java.util.Map")) || (class2 != null && class2.length > 0 && class2[0].getName().equals("java.util.Map"))) {
                stringBuilder.append(toJsonMap((Map<String, Object>) list.get(i)));
                stringBuilder.append(",");
            }else if (list.get(i).getClass().getName().equals("java.lang.String")) {
                stringHandle(stringBuilder, list.get(i).toString());
            } else {
                stringBuilder.append(replaceAll(list.get(i).toString()));
                stringBuilder.append(",");
            }
        }
        if (stringBuilder.substring(stringBuilder.length() - 1, stringBuilder.length()).equals(",")) {
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    /*private void stringHandle(StringBuilder stringBuilder, String data) {
        if (data.length() < 1){
            stringBuilder.append("\"");
            stringBuilder.append("\"");
        }else{
            String q=data.substring(0,1);
            String h=data.substring(data.length()-1);
            if ((q.equals("{") && h.equals("}")) || (q.equals("[") && h.equals("]"))){
                stringBuilder.append(data);
            }else{
                stringBuilder.append("\"");
                stringBuilder.append(replaceAll(data, arr1, arr2));
                stringBuilder.append("\"");
            }
        }
        stringBuilder.append(",");
    }*/

    private void stringHandle(StringBuilder stringBuilder, String data) {
        String q=data.substring(0,1);
        String h=data.substring(data.length()-1);
        if ((q.equals("{") && h.equals("}")) || (q.equals("[") && h.equals("]"))){
            stringBuilder.append(data);
        }else{
            stringBuilder.append("\"");

            stringBuilder.append(Tools.escapeJsonString(data));
            stringBuilder.append("\"");
        }
        stringBuilder.append(",");
    }
    public String toJsonMap(Map<String, Object> map) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> en = it.next();
            if (en.getKey() == null || en.getKey().toString().length() == 0) {//空的 直接忽略
                continue;
            }
            if (en.getValue() == null || en.getValue().toString().length() == 0) {
                continue;
            }
            stringBuilder.append("\"");
            stringBuilder.append(en.getKey());
            stringBuilder.append("\":");
            Class[] class1 = en.getValue().getClass().getInterfaces();
            Class[] class2 = en.getValue().getClass().getSuperclass().getInterfaces();
            //内部元素依赖本身tostring  不做解析
            if ((class1 != null && class1.length > 0 && class1[0].getClass().equals("java.util.List")) || (class2 != null && class2.length > 0 && class2[0].getName().equals("java.util.List"))) {
                stringBuilder.append(toJsonList((List<?>) en.getValue()));
                stringBuilder.append(",");
            } else if ((class1 != null && class1.length > 0 && class1[0].getName().equals("java.util.Map")) || (class2 != null && class2.length > 0 && class2[0].getName().equals("java.util.Map"))) {
                stringBuilder.append(toJsonMap((Map<String, Object>) en.getValue()));
                stringBuilder.append(",");
            } else if (en.getValue().getClass().getName().equals("java.lang.String")) {
                stringHandle(stringBuilder, en.getValue().toString());
            } else {
                stringBuilder.append(replaceAll(en.getValue().toString()));
                stringBuilder.append(",");
            }
        }
        if (stringBuilder.substring(stringBuilder.length() - 1, stringBuilder.length()).equals(",")) {
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static final String replaceAll(String str) {
        String[] arr1 = new String[]{"\r", "\n", "\t"};
        String[] arr2 = new String[]{"\\\\r", "\\\\n", "\\\\t"};
        return replaceAll0(str, arr1, arr2);
    }

    public static final String replaceAll0(String str, String[] arr1, String[] arr2) {
        for (int i = 0; i < arr1.length; i++) {
            str = str.replaceAll(arr1[i], arr2[i]);
        }
        return str;
    }

}
