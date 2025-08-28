package com.acquisition.ocr;

public class RecognitionResult {
    private String text; // 识别出的文本内容
    private int x;       // 文本框左上角的X坐标
    private int y;       // 文本框左上角的Y坐标
    private int width;   // 文本框的宽度
    private int height;  // 文本框的高度

    /**
     * 构造函数。
     * @param text 识别出的文本。
     * @param x 文本框左上角X坐标。
     * @param y 文本框左上角Y坐标。
     * @param width 文本框宽度。
     * @param height 文本框高度。
     */
    public RecognitionResult(String text, int x, int y, int width, int height) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Getter 方法
    public String getText() {
        return text;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return toJsonString();
    }

    /**
     * 将识别结果转换为JSON字符串。
     * 注意：手动构建JSON字符串容易出错，强烈推荐使用Jackson或Gson等JSON库进行序列化。
     * 此方法为手动转义示例，已处理常见的JSON特殊字符。
     * @return 识别结果的JSON字符串表示。
     */
    public String toJsonString() {
        // 步骤1: 对字面反斜杠进行转义 (必须最先处理，因为它是转义字符本身)
        String escapedText = text
                .trim()
                .replace("\\", "\\\\");

        // 步骤2: 对双引号进行转义
        escapedText = escapedText.replace("\"", "\\\"");

        // 步骤3: 对换行符进行转义
        escapedText = escapedText.replace("\n", "\\n");

        // 步骤4: 对回车符进行转义
        escapedText = escapedText.replace("\r", "\\r");

        // 可选：如果OCR结果可能包含制表符等其他控制字符，也需要进行转义
        escapedText = escapedText.replace("\t", "");
        escapedText = escapedText.replace(" ", "");
        // 可选：JSON规范允许不转义斜杠'/'，但为了避免某些HTML解析问题，有时也会转义
        escapedText = escapedText.replace("/", "\\/");

        return "{" +
                "\"text\":\"" + escapedText + "\"," +
                "\"sx\":" + x + "," + // 数值类型直接输出，不加引号更符合JSON规范
                "\"sy\":" + y + "," +
                "\"ex\":" + (x + width) + "," +
                "\"ey\":" + (y + height) +
                "}";
    }
}
