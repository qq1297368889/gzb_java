/*
 *
 *  * Copyright [2025] [GZB ONE]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package gzb.tools.json;

import gzb.tools.Tools;

import java.util.*;

public class JSONParse {

    // 将 String 替换为 char[]，用于高性能的字符访问
    private char[] chars;
    private int index;
    private StringBuilder stringBuilder;

    /**
     * 公共的解析方法入口。
     * @param input 要解析的字符串。
     * @return 解析后的对象（Map, List, 或 String），如果输入为空则返回 null。
     */
    public Object parse(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        // *** 核心优化点：在解析前一次性转换为 char 数组 ***
        this.chars = input.toCharArray();
        this.index = 0;
        return parseDataStructure();
    }

    // -----------------------------------------------------------------
    // 私有辅助方法
    // -----------------------------------------------------------------

    /**
     * 跳过当前位置的所有空白字符。
     */
    private void skipWhitespace() {
        // 使用 chars.length 代替 input.length()
        while (index < chars.length) {
            final char ch = chars[index];
            // 仅检查 JSON 规范定义的四种空白字符：空格(32), \t(9), \n(10), \r(13)
            if (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') {
                index++;
            } else {
                break;
            }
        }
    }

    /**
     * 主解析方法：根据当前字符决定调用哪个特定类型的解析函数。
     */
    private Object parseDataStructure() {
        skipWhitespace();

        if (index >= chars.length) {
            return null;
        }

        // 直接访问 char[]
        char current = chars[index];

        if (current == '{') {
            return parseMap();
        } else if (current == '[') {
            return parseList();
        } else if (current == '"') {
            return parseString(true); // 引号字符串
        } else {
            return parseString(false); // 裸露的字符串（数值、布尔、裸键等）
        }
    }

    /**
     * 解析字符串。
     * @param isQuoted 如果为 true，则寻找配对的双引号。
     */
    private String parseString(boolean isQuoted) {
        int startIndex = index;

        if (isQuoted) {
            // 1. 消耗起始双引号
            index++;
            startIndex = index; // 内容从下一个位置开始

            // 2. 查找结束双引号
            // 直接访问 char[]
            while (index < chars.length && chars[index] != '"') {
                index++;
            }

            int endIndex = index;
            index++; // 移过结束 '"'

            // 使用 String 构造函数从 char 数组中创建子字符串，比 substring(String) 更高效
            return new String(chars, startIndex, endIndex - startIndex);

        } else {
            // 非引号字符串（数值、布尔、裸键等，一律视为 String）

            // 3. 查找结束边界
            while (index < chars.length) {
                // 直接访问 char[]
                char ch = chars[index];

                if (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r' || ch == ',' || ch == ':' || ch == ']' || ch == '}') {
                    break;
                }
                index++;
            }

            // 使用 String 构造函数从 char 数组中创建子字符串
            // 这里仍需 trim() 以防裸露字符串周围有空白
            return new String(chars, startIndex, index - startIndex);
        }
    }


    /**
     * 解析列表（List）。
     */
    private List<Object> parseList() {
        List<Object> resultList = new ArrayList<>();

        // 1. 消耗起始方括号 '['
        index++;

        while (index < chars.length) {
            skipWhitespace();

            // 直接访问 char[]
            char current = chars[index];

            // 2. 检查结束方括号 ']'
            if (current == ']') {
                index++; // 移过 ']'
                return resultList;
            }

            // 3. 递归解析下一个元素
            Object element = parseDataStructure();
            if (element != null) {
                resultList.add(element);
            }

            skipWhitespace();

            // 4. 检查逗号分隔符 ','
            if (index < chars.length && chars[index] == ',') {
                index++; // 移过 ','
            }
        }

        return resultList;
    }


    /**
     * 解析键值对（Map）。
     */
    private Map<String, Object> parseMap() {
        Map<String, Object> resultMap = new HashMap<>();

        // 1. 消耗起始花括号 '{'
        index++;

        while (index < chars.length) {
            skipWhitespace();

            // 直接访问 char[]
            char current = chars[index];

            // 2. 检查结束花括号 '}'
            if (current == '}') {
                index++; // 移过 '}'
                return resultMap;
            }

            // --- 解析键（Key） ---
            Object keyObj = parseDataStructure();
            if (!(keyObj instanceof String)) {
                break;
            }
            String key = (String) keyObj;

            skipWhitespace();

            // 3. 检查冒号分隔符 ':'
            if (index < chars.length && chars[index] == ':') {
                index++; // 移过 ':'
            } else {
                // 格式错误，缺少冒号
                break;
            }

            // --- 解析值（Value） ---
            Object value = parseDataStructure(); // 递归解析值

            resultMap.put(key, value);

            skipWhitespace();

            // 4. 检查逗号分隔符 ','
            if (index < chars.length && chars[index] == ',') {
                index++; // 移过 ','
            }
        }

        return resultMap;
    }
}