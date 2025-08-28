package com.acquisition.ocr;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class ImageTextWriter {

    /**
     * 在图片上写入指定文字并对外框和字体进行特殊处理以对抗OCR识别。
     * 空格会保持占位但不会绘制外框。
     *
     * @param backgroundImageBytes 背景图byte[]
     * @param text                 文本内容
     * @param fontColor            字体颜色 (java.awt.Color 对象)
     * @param fontSize             字体大小 (例如: 24)
     * @param isFontBold           字体是否为粗体
     * @param rotationAngle        旋转角度 (度数)
     * @param transparency         透明度 (0.0 - 1.0)
     * @param frameType            外圈类型: 0随机, 1圆形, 2菱形, 3正方形, 4长方形
     * @param frameStrokeThickness 外框描边粗细
     * @param frameColor           外框颜色 (java.awt.Color 对象)
     * @return 处理后的图片 byte[]
     * @throws IOException 如果图片读取或写入失败
     */
    public static byte[] writeTextOnImage(byte[] backgroundImageBytes, String text, Color fontColor, int fontSize,
                                          boolean isFontBold, double rotationAngle, float transparency, int frameType,
                                          float frameStrokeThickness, Color frameColor) throws IOException {

        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(backgroundImageBytes));
        if (originalImage == null) {
            throw new IOException("无法读取背景图片。请检查图片格式。");
        }

        BufferedImage image = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.drawImage(originalImage, 0, 0, null);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));

        // 根据 isFontBold 参数设置字体样式
        int fontStyle = isFontBold ? Font.BOLD : Font.PLAIN;
        Font font = new Font("宋体", fontStyle, fontSize);
        g2d.setFont(font);
        // 首次设置字体颜色，但在循环内会再次设置以确保正确性
        // g2d.setColor(fontColor);

        FontMetrics fontMetrics = g2d.getFontMetrics(font);
        int lineHeight = fontMetrics.getHeight();

        String[] lines = text.split("\n");
        int y = fontMetrics.getAscent() + 10;

        Random random = new Random();

        for (String line : lines) {
            if (line.isEmpty() && line.trim().isEmpty()) {
                y += lineHeight;
                continue;
            }

            int x = 10;
            for (char c : line.toCharArray()) {
                String charStr = String.valueOf(c);
                int charWidth = fontMetrics.charWidth(c);

                AffineTransform originalTransform = g2d.getTransform();

                double radians = Math.toRadians(rotationAngle);
                g2d.rotate(radians, x + charWidth / 2.0, y - lineHeight / 2.0 + fontMetrics.getAscent());

                // 只有非空格字符才绘制外框
                if (!Character.isWhitespace(c)) {
                    // **在绘制外框前，设置外框颜色**
                    g2d.setColor(frameColor);
                    drawAntiOcrFrame(g2d, x, y, charWidth, lineHeight, fontMetrics.getAscent(), fontMetrics.getDescent(),
                            frameType, frameStrokeThickness, random); // 移除frameColor参数，因为已直接设置
                }

                // **在绘制字符前，重新设置字体颜色**
                g2d.setColor(fontColor);
                g2d.drawString(charStr, x, y);

                g2d.setTransform(originalTransform);

                x += charWidth;
            }
            y += lineHeight;
        }

        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    /**
     * 绘制对抗OCR的外框，确保连接。
     *
     * @param g2d                   Graphics2D 对象 (其颜色已在外层方法中设置为frameColor)
     * @param x                     字符的X坐标
     * @param y                     字符的Y坐标
     * @param charWidth             字符宽度
     * @param lineHeight            行高
     * @param ascent                字体上升高度 (从基线到字体顶部的距离)
     * @param descent               字体下降高度 (从基线到字体底部的距离)
     * @param frameType             外框类型
     * @param frameStrokeThickness  外框描边粗细
     * @param random                随机数生成器
     */
    private static void drawAntiOcrFrame(Graphics2D g2d, int x, int y, int charWidth, int lineHeight, int ascent, int descent,
                                         int frameType, float frameStrokeThickness, Random random) {
        // g2d的颜色已经在外层方法中设置为frameColor，此处无需再设置
        g2d.setStroke(new BasicStroke(frameStrokeThickness));

        int framePadding = 2;
        int frameX = x - framePadding;
        int frameY = y - ascent - framePadding;
        int frameWidth = charWidth + 2 * framePadding;
        int frameHeight = lineHeight + 2 * framePadding;

        // 连接点：尝试在字符的底部中心连接
        int connectionPointX = x + charWidth / 2;
        // 调整连接点Y坐标，使其稍微进入字符下方，增强连接感
        int connectionPointY = y + descent - 1;

        int actualFrameType = frameType;
        if (frameType == 0) {
            actualFrameType = random.nextInt(4) + 1; // 1-4 之间
        }

        Path2D.Double frameShape = new Path2D.Double();
        switch (actualFrameType) {
            case 1: // 圆形
                frameShape.append(new Ellipse2D.Double(frameX, frameY, frameWidth, frameHeight), false);
                break;
            case 2: // 菱形
                frameShape.moveTo(frameX + frameWidth / 2, frameY);
                frameShape.lineTo(frameX + frameWidth, frameY + frameHeight / 2);
                frameShape.lineTo(frameX + frameWidth / 2, frameY + frameHeight);
                frameShape.lineTo(frameX, frameY + frameHeight / 2);
                frameShape.closePath();
                break;
            case 3: // 正方形
                frameShape.append(new Rectangle(frameX, frameY, frameWidth, frameHeight), false);
                break;
            case 4: // 长方形
                int rectWidth = frameWidth + random.nextInt(5) - 2;
                int rectHeight = frameHeight + random.nextInt(5) - 2;
                frameShape.append(new Rectangle(frameX, frameY, rectWidth, rectHeight), false);
                break;
        }

        // 确保外框与字体连接：从外框底部中心（或最近点）连接到字符底部中心
        double frameBottomCenterX = frameX + frameWidth / 2.0;
        double frameBottomY = frameY + frameHeight;

        if (!frameShape.getBounds2D().contains(connectionPointX, connectionPointY)) {
            frameShape.moveTo(frameBottomCenterX, frameBottomY);
            frameShape.lineTo(connectionPointX, connectionPointY);
        }

        g2d.draw(frameShape);
    }

    public static void main(String[] args) {
        try {
            String text = "实 时 I D 群 \n" +
                    "自 动 私 信 脚 本 \n" +
                    "打 开 网 址 添 加 微 信  \n" +
                    "W X . 0 5 0 5 . F U N \n"; // 增加多行文本测试
            byte[] backgroundImageBytes = Files.readAllBytes(Paths.get("E:\\00\\0\\0\\新建文件夹\\背景0.png"));

            Color textColor = Color.RED; // 字体颜色  RED  BLUE
            int fontSize = 52;
            boolean isFontBold = true;
            double rotation = 45.0;
            float opacity = 0.5f;
            int frame = 2;
            float frameThickness = 4.0f;
            Color frameColor = Color.RED; // 外框颜色
            /**
             * 在图片上写入指定文字并对外框和字体进行特殊处理以对抗OCR识别。
             * 空格会保持占位但不会绘制外框。
             *
             * @param backgroundImageBytes 背景图byte[]
             * @param text                 文本内容
             * @param fontColor            字体颜色 (java.awt.Color 对象)
             * @param fontSize             字体大小 (例如: 24)
             * @param isFontBold           字体是否为粗体
             * @param rotationAngle        旋转角度 (度数)
             * @param transparency         透明度 (0.0 - 1.0)
             * @param frameType            外圈类型: 0随机, 1圆形, 2菱形, 3正方形, 4长方形
             * @param frameStrokeThickness 外框描边粗细
             * @param frameColor           外框颜色 (java.awt.Color 对象)
             * @return 处理后的图片 byte[]
             * @throws IOException 如果图片读取或写入失败
             */
            byte[] processedImageBytes = ImageTextWriter.writeTextOnImage(
                    backgroundImageBytes, text, textColor, fontSize, isFontBold, rotation, opacity, frame, frameThickness, frameColor
            );

            String outputPath = "E:\\00\\0\\0\\新建文件夹\\生成-01-binary.png";
            Files.write(Paths.get(outputPath), processedImageBytes);
            System.out.println("图片已成功保存到: " + outputPath);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("图片处理失败: " + e.getMessage());
        }
    }
}