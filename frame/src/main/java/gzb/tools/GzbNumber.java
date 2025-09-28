package gzb.tools;

import gzb.exception.GzbException0;

public class GzbNumber {

    public static Long parseLong(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            if (obj instanceof Long) {
                return (Long) obj;
            }
            return Long.parseLong(obj.toString());
        } catch (NumberFormatException e) {
            String message = "无法将输入值转换为 Long: " + obj.toString();
            throw new GzbException0(message);
        }
    }

    public static Integer parseInteger(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            if (obj instanceof Integer) {
                return (Integer) obj;
            }
            return Integer.parseInt(obj.toString());
        } catch (NumberFormatException e) {
            String message = "无法将输入值转换为 Integer: " + obj.toString();
            throw new GzbException0(message);
        }
    }

    public static Short parseShort(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            if (obj instanceof Short) {
                return (Short) obj;
            }
            return Short.parseShort(obj.toString());
        } catch (NumberFormatException e) {
            String message = "无法将输入值转换为 Short: " + obj.toString();
            throw new GzbException0(message);
        }
    }

    public static Float parseFloat(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            if (obj instanceof Float) {
                return (Float) obj;
            }
            return Float.parseFloat(obj.toString());
        } catch (NumberFormatException e) {
            String message = "无法将输入值转换为 Float: " + obj.toString();
            throw new GzbException0(message);
        }
    }

    public static Double parseDouble(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            if (obj instanceof Double) {
                return (Double) obj;
            }
            return Double.parseDouble(obj.toString());
        } catch (NumberFormatException e) {
            String message = "无法将输入值转换为 Double: " + obj.toString();
            throw new GzbException0(message);
        }
    }

    public static Byte parseByte(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            if (obj instanceof Byte) {
                return (Byte) obj;
            }
            return Byte.parseByte(obj.toString());
        } catch (NumberFormatException e) {
            String message = "无法将输入值转换为 Byte: " + obj.toString();
            throw new GzbException0(message);
        }
    }
    public static Boolean parseBoolean(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            return Boolean.parseBoolean(obj.toString());
        } catch (NumberFormatException e) {
            String message = "无法将输入值转换为 Byte: " + obj.toString();
            throw new GzbException0(message);
        }
    }


}
