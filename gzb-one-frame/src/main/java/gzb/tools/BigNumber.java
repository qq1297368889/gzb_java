package gzb.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//不支持负数
public class BigNumber {
    Entity data = null;

    public static void main(String[] args) {
        BigNumber addResult = new BigNumber(100).addition(67.89);
        System.out.println("加法结果：" + addResult.toString() +"  "+(100.0+67.89));

        addResult.addition("1000.00001");
        System.out.println("加法结果：" + addResult.toString() +"  "+(100.0+67.89));
        System.out.println("加法结果：" + addResult.toString(3) +"  "+(100.0+67.89));
        BigNumber subResult = new BigNumber(100).subtraction(67.89);
        System.out.println("减法结果：" + subResult.toString() +"  "+(100.0-67.89));

        BigNumber mulResult = new BigNumber(100).multiplication(67.89);
        System.out.println("乘法结果：" + mulResult.toString() +"  "+(100.0*67.89));

        BigNumber divResult = new BigNumber(100).division(67.89, 6);
        System.out.println("除法结果：" + divResult.toString() +"  "+(100.0/67.89));
    }

    // 空构造器：默认初始化为0
    public BigNumber() {
        this.data = toCharArray("0", 0, 0);
    }

    // 重载构造器：支持Integer/Long/Double
    public BigNumber(Integer data) {
        this.data = toCharArray(data, 0, 0);
    }
    public BigNumber(Long data) {
        this.data = toCharArray(data, 0, 0);
    }
    public BigNumber(Double data) {
        this.data = toCharArray(data, 0, 0);
    }

    // ---------------------- 核心运算方法 ----------------------
    /**
     * 加法
     * @param number1 加数（支持Integer/Long/Double/String）
     * @return 自身（链式调用）
     */
    public BigNumber addition(Object number1) {
        // 1. 格式化两个数为相同长度的整数+小数部分
        Entity num1Entity = this.data;
        Entity num2Entity = toCharArray(number1, 0, 0);
        align(num1Entity, num2Entity);

        // 2. 从右往左逐位相加（先算小数部分，再算整数部分）
        List<Character> result = new ArrayList<>();
        int carry = 0; // 进位
        int totalLen = num1Entity.data.size();
        int pointIndex = totalLen - num1Entity.point - 1; // 小数点位置

        // 从最后一位开始计算（反向遍历）
        for (int i = totalLen - 1; i >= 0; i--) {
            if (i == pointIndex) { // 跳过小数点
                result.add('.');
                continue;
            }
            // 转数字计算
            int n1 = num1Entity.data.get(i) - '0';
            int n2 = num2Entity.data.get(i) - '0';
            int sum = n1 + n2 + carry;
            result.add((char) (sum % 10 + '0'));
            carry = sum / 10; // 更新进位
        }
        // 处理最终进位
        if (carry > 0) {
            result.add((char) (carry + '0'));
        }
        // 反转结果（因为是反向计算的）
        Collections.reverse(result);

        // 3. 更新自身数据
        this.data.data = result;
        this.data.point = num1Entity.point;
        return this;
    }

    /**
     * 减法（当前数 - 传入数）
     * @param number1 减数
     * @return 自身
     */
    public BigNumber subtraction(Object number1) {
        Entity num1Entity = this.data;
        Entity num2Entity = toCharArray(number1, 0, 0);
        align(num1Entity, num2Entity);

        List<Character> result = new ArrayList<>();
        int borrow = 0; // 借位
        int totalLen = num1Entity.data.size();
        int pointIndex = totalLen - num1Entity.point - 1;

        // 反向遍历计算
        for (int i = totalLen - 1; i >= 0; i--) {
            if (i == pointIndex) {
                result.add('.');
                continue;
            }
            int n1 = num1Entity.data.get(i) - '0' - borrow;
            int n2 = num2Entity.data.get(i) - '0';
            borrow = 0;

            if (n1 < n2) { // 需要借位
                n1 += 10;
                borrow = 1;
            }
            int sub = n1 - n2;
            result.add((char) (sub + '0'));
        }
        // 反转结果
        Collections.reverse(result);
        // 去除前导0（保留最后一个0）
        trimLeadingZero(result);

        this.data.data = result;
        this.data.point = num1Entity.point;
        return this;
    }

    /**
     * 乘法
     * @param number1 乘数
     * @return 自身
     */
    public BigNumber multiplication(Object number1) {
        // 1. 转为纯数字字符串（去掉小数点）
        String num1Str = getPureNumber(this.data);
        String num2Str = getPureNumber(toCharArray(number1, 0, 0));

        // 2. 逐位相乘（小学乘法逻辑）
        int[] result = new int[num1Str.length() + num2Str.length()];
        for (int i = num1Str.length() - 1; i >= 0; i--) {
            int n1 = num1Str.charAt(i) - '0';
            for (int j = num2Str.length() - 1; j >= 0; j--) {
                int n2 = num2Str.charAt(j) - '0';
                int product = n1 * n2 + result[i + j + 1];
                result[i + j + 1] = product % 10;
                result[i + j] += product / 10;
            }
        }

        // 3. 转换为字符列表并插入小数点
        List<Character> charResult = new ArrayList<>();
        int decimalLen = this.data.point + toCharArray(number1, 0, 0).point; // 总小数位数
        int pureLen = num1Str.length() + num2Str.length();

        // 转字符
        for (int num : result) {
            charResult.add((char) (num + '0'));
        }
        // 去除前导0
        trimLeadingZero(charResult);
        // 插入小数点
        if (decimalLen > 0) {
            int pointPos = charResult.size() - decimalLen;
            if (pointPos <= 0) { // 小数点在最前面，补0
                charResult.add(0, '0');
                pointPos = 1;
            }
            charResult.add(pointPos, '.');
        }

        this.data.data = charResult;
        this.data.point = decimalLen;
        return this;
    }

    /**
     * 除法（保留指定小数位数，四舍五入）
     * @param number1 除数
     * @param scale 保留小数位数
     * @return 自身
     */
    public BigNumber division(Object number1, int scale) {
        // 1. 转为纯数字并统一小数位数
        Entity dividendEntity = this.data; // 被除数
        Entity divisorEntity = toCharArray(number1, 0, 0); // 除数

        String dividend = getPureNumber(dividendEntity);
        String divisor = getPureNumber(divisorEntity);
        int divisorInt = Integer.parseInt(divisor);
        if (divisorInt == 0) {
            throw new ArithmeticException("除数不能为0");
        }

        // 2. 逐位相除
        StringBuilder sb = new StringBuilder();
        int remainder = 0;
        int decimalCount = 0;
        boolean pointAdded = false;

        for (int i = 0; i < dividend.length() || remainder > 0; i++) {
            // 取当前位数字
            int digit = (i < dividend.length()) ? (dividend.charAt(i) - '0') : 0;
            remainder = remainder * 10 + digit;

            // 计算商
            int quotient = remainder / divisorInt;
            sb.append(quotient);
            remainder = remainder % divisorInt;

            // 处理小数位数
            if (!pointAdded && i > (dividendEntity.data.size() - dividendEntity.point - 1)) {
                sb.append('.');
                pointAdded = true;
            }
            if (pointAdded) {
                decimalCount++;
                if (decimalCount > scale) {
                    // 四舍五入
                    int lastDigit = sb.charAt(sb.length() - 1) - '0';
                    if (remainder * 10 / divisorInt >= 5) {
                        lastDigit += 1;
                        sb.setCharAt(sb.length() - 1, (char) (lastDigit + '0'));
                    }
                    break;
                }
            }
        }

        // 3. 转换为字符列表
        List<Character> result = new ArrayList<>();
        for (char c : sb.toString().toCharArray()) {
            result.add(c);
        }
        trimLeadingZero(result);

        this.data.data = result;
        this.data.point = scale;
        return this;
    }

    // ---------------------- 辅助方法 ----------------------
    /**
     * 对齐两个数的整数和小数长度（补0）
     */
    private void align(Entity e1, Entity e2) {
        // 1. 统一小数部分长度
        int maxDecimalLen = Math.max(e1.point, e2.point);
        while (e1.data.size() - (e1.data.indexOf('.') + 1) < maxDecimalLen) {
            e1.data.add('0');
        }
        while (e2.data.size() - (e2.data.indexOf('.') + 1) < maxDecimalLen) {
            e2.data.add('0');
        }

        // 2. 统一整数部分长度
        int e1IntLen = e1.data.indexOf('.');
        int e2IntLen = e2.data.indexOf('.');
        int maxIntLen = Math.max(e1IntLen, e2IntLen);

        while (e1.data.indexOf('.') < maxIntLen) {
            e1.data.add(0, '0');
        }
        while (e2.data.indexOf('.') < maxIntLen) {
            e2.data.add(0, '0');
        }

        // 更新小数位数
        e1.point = maxDecimalLen;
        e2.point = maxDecimalLen;
    }

    /**
     * 去除前导0（保留最后一个0）
     */
    private void trimLeadingZero(List<Character> list) {
        while (list.size() > 1 && list.get(0) == '0' && list.get(1) != '.') {
            list.remove(0);
        }
    }

    /**
     * 获取纯数字字符串（去掉小数点）
     */
    private String getPureNumber(Entity entity) {
        StringBuilder sb = new StringBuilder();
        for (char c : entity.data) {
            if (c != '.') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // ---------------------- 原有方法 ----------------------
    public Entity toCharArray(Object data, int len1, int len2) {
        String s1 = data.toString();
        List<Character> list = new ArrayList<>();
        char[] data0 = s1.toCharArray();
        List<Character> data1 = new ArrayList<>(); // 整数部分
        List<Character> data2 = new ArrayList<>(); // 小数部分
        List<Character> data3 = data1;

        for (int i = 0; i < data0.length; i++) {
            if (data0[i] == '.') {
                data3 = data2;
                continue;
            }
            // 过滤非数字字符
            if (Character.isDigit(data0[i])) {
                data3.add(data0[i]);
            }
        }

        // 补0逻辑（len1/len2为0时默认至少1位）
        if (len1 == 0) {
            len1 = 1;
        }
        while (data1.size() < len1) {
            data1.add(0, '0');
        }
        while (data2.size() < len2) {
            data2.add('0');
        }

        // 拼接结果
        list.addAll(data1);
        list.add('.');
        list.addAll(data2);

        Entity entity = new Entity();
        entity.data = list;
        entity.point = data2.size(); // 小数部分长度
        return entity;
    }

    // 重写toString：输出易读的数字字符串
    @Override
    public String toString() {
        return toString(this.data.data.size());
    }
    public String toString(int decimalLen) {
        StringBuilder sb = new StringBuilder();
        int size=0;
        boolean open=false;
        for (char c : this.data.data) {
            sb.append(c);
            if (c=='.') {
                open=true;
                size=0;
            } else{
                if (open) {
                    size++;
                    if (size>=decimalLen) {
                        break;
                    }
                }
            }

        }
        return sb.toString();
    }

    public static class Entity {
        public List<Character> data = null;
        public int point = -1; // 小数部分长度

        @Override
        public String toString() {
            return "{\"data\":" + data.toString() + ",\"point\":" + point + "}";
        }
    }
}