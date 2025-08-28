package com.acquisition.ocr;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class SparseTextGeneratorV2 {

    /**
     * 在背景图上绘制稀疏文字。文字的稀疏度由颜色密度决定。
     * 字体大小会根据预期的缩放效果进行调整，以确保缩放后文字清晰。
     * 文字会进行居中（水平和垂直）处理。
     * 函数返回的是未经缩放的、带有稀疏文字的图片字节数组。
     *
     * @param backgroundImageBytes 背景图的字节数组。
     * @param text                 要写入的文字，支持换行符'\n'。
     * @param outputUnscaledWidth  最终返回的未缩放图片的宽度。
     * @param outputUnscaledHeight 最终返回的未缩放图片的高度。
     * @param expectedScaledWidth  预期图片被平台缩放到的宽度（用于计算字体大小）。
     * @param expectedScaledHeight 预期图片被平台缩放到的高度（用于计算字体大小）。
     * @param baseFontSizeAtScaled  在预期缩放后的图片尺寸下，希望文字达到的基础字体大小（例如24）。
     * 这个值决定了缩放后文字的清晰度。
     * @param colorDensityPercent   颜色密度百分比 (1%-100%)。值越小，文字越稀疏。
     * @param textColor            文字的颜色。
     * @return 包含稀疏文字的未缩放图片的字节数组。
     * @throws IOException 如果图片读取或写入失败。
     * @throws IllegalArgumentException 如果参数无效。
     */
    public static byte[] generateSparseTextUnscaled(byte[] backgroundImageBytes, String text,
                                                    int outputUnscaledWidth, int outputUnscaledHeight,
                                                    int expectedScaledWidth, int expectedScaledHeight,
                                                    int baseFontSizeAtScaled,
                                                    int colorDensityPercent, Color textColor) throws IOException {

        if (colorDensityPercent < 1 || colorDensityPercent > 100) {
            throw new IllegalArgumentException("颜色密度百分比必须在 1 到 100 之间。");
        }
        if (outputUnscaledWidth <= 0 || outputUnscaledHeight <= 0 || expectedScaledWidth <= 0 || expectedScaledHeight <= 0) {
            throw new IllegalArgumentException("所有尺寸参数必须大于 0。");
        }
        if (baseFontSizeAtScaled <= 0) {
            throw new IllegalArgumentException("基础字体大小必须大于 0。");
        }

        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(backgroundImageBytes));
        if (originalImage == null) {
            throw new IOException("无法读取背景图片。请检查图片格式或内容。");
        }

        BufferedImage imageWithSparseText = new BufferedImage(outputUnscaledWidth, outputUnscaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imageWithSparseText.createGraphics();

        g2d.drawImage(originalImage, 0, 0, outputUnscaledWidth, outputUnscaledHeight, null);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        double scaleFactorWidth = (double) outputUnscaledWidth / expectedScaledWidth;
        double scaleFactorHeight = (double) outputUnscaledHeight / expectedScaledHeight;
        double overallScaleFactor = Math.min(scaleFactorWidth, scaleFactorHeight);

        int fontSize = (int) (baseFontSizeAtScaled * overallScaleFactor);
        if (fontSize < 1) {
            fontSize = 1;
        }

        Font font = new Font("SimSun", Font.PLAIN, fontSize);
        g2d.setFont(font);
        g2d.setColor(textColor);

        FontMetrics fontMetrics = g2d.getFontMetrics(font);
        int lineHeight = fontMetrics.getHeight();
        String[] lines = text.split("\n");
        Random random = new Random();
        FontRenderContext frc = g2d.getFontRenderContext();

        // --- 核心修改：计算文本块的整体尺寸以进行居中 ---
        int totalTextWidth = 0;
        for (String line : lines) {
            int lineWidth = fontMetrics.stringWidth(line);
            if (lineWidth > totalTextWidth) {
                totalTextWidth = lineWidth; // 找到最长行的宽度
            }
        }
        int totalTextHeight = lines.length * lineHeight; // 总文本高度

        // 计算居中起始点
        int initialX = (outputUnscaledWidth - totalTextWidth) / 2;
        // 计算Y轴起始点：图片中心 - 文本高度一半 + ascent (因为drawstring的Y是基线)
        // 这里的 ascent 是为了弥补文字实际绘制区域比lineHeight高一点点（从顶部到基线）
        int initialY = (outputUnscaledHeight - totalTextHeight) / 2 + fontMetrics.getAscent();

        // 确保文字不会超出顶部边界 (如果计算出来initialY太小)
        if (initialY < fontMetrics.getAscent()) { // 至少要让第一行的基线在 ascent 以上
            initialY = fontMetrics.getAscent();
        }
        // ----------------------------------------------------

        int currentY = initialY;

        for (String line : lines) {
            if (line.isEmpty() || line.trim().isEmpty()) {
                currentY += lineHeight;
                continue;
            }

            // 重新计算每行文字的起始X坐标以实现每行的居中 (而非整个文本块左对齐)
            // 如果希望所有行都左对齐并以最长行为准居中，则使用 initialX
            // 如果希望每行独立居中，则使用以下代码:
            int lineStartX = initialX + (totalTextWidth - fontMetrics.stringWidth(line)) / 2; // 每行单独居中

            for (char c : line.toCharArray()) {
                if (Character.isWhitespace(c)) {
                    lineStartX += fontMetrics.charWidth(c);
                    continue;
                }

                String charStr = String.valueOf(c);
                GlyphVector gv = font.createGlyphVector(frc, charStr);
                Shape charShape = gv.getOutline();

                Rectangle charBounds = charShape.getBounds();

                int tempCharWidth = charBounds.width + 2;
                int tempCharHeight = charBounds.height + 2;
                if (tempCharWidth <= 0) tempCharWidth = 1;
                if (tempCharHeight <= 0) tempCharHeight = 1;

                BufferedImage tempCharImage = new BufferedImage(tempCharWidth, tempCharHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D tempG2d = tempCharImage.createGraphics();
                tempG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                tempG2d.setColor(textColor);
                tempG2d.translate(-charBounds.x, -charBounds.y);
                tempG2d.fill(charShape);
                tempG2d.dispose();

                for (int ty = 0; ty < tempCharImage.getHeight(); ty++) {
                    for (int tx = 0; tx < tempCharImage.getWidth(); tx++) {
                        int pixelColor = tempCharImage.getRGB(tx, ty);
                        if ((pixelColor >>> 24) != 0) { // 检查Alpha通道是否非零
                            if (random.nextInt(100) < colorDensityPercent) {
                                // 最终像素在图片上的位置
                                int finalX = lineStartX + tx + charBounds.x;
                                // currentY 是基线，charBounds.y 是字符相对于其(0,0)的Y偏移，fontMetrics.getAscent()是基线到文字顶部的距离
                                int finalY = currentY + ty + charBounds.y - fontMetrics.getAscent();

                                if (finalX >= 0 && finalX < outputUnscaledWidth &&
                                        finalY >= 0 && finalY < outputUnscaledHeight) {
                                    imageWithSparseText.setRGB(finalX, finalY, textColor.getRGB());
                                }
                            }
                        }
                    }
                }

                lineStartX += fontMetrics.charWidth(c);
            }
            currentY += lineHeight;
        }

        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(imageWithSparseText, "png", baos);
        return baos.toByteArray();
    }

    private static BufferedImage scaleImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        if (targetWidth <= 0 || targetHeight <= 0) {
            throw new IllegalArgumentException("目标缩放尺寸必须大于 0。");
        }
        BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return scaledImage;
    }


    public static void main(String[] args) {
        try {
            String text = "实 时 I D 群 \n" +
                    "自 动 私 信 脚 本 \n" +
                    "打 开 网 址 添 加 微\n" +
                    "P P . 0 5 0 5 . F U N \n"; // 增加多行文本测试
            // --- 1. 定义你的输入参数 ---
            byte[] backgroundImageBytes = Files.readAllBytes(Paths.get("E:\\00\\0\\0\\新建文件夹\\背景.png"));

            // --- 2. 定义未缩放输出图片的尺寸 (这是函数返回的图片尺寸) ---
            int outputUnscaledWidth = 800;
            int outputUnscaledHeight = 800;

            // --- 3. 定义预期图片被平台缩放到的尺寸 (用于字体大小计算，仅作为参考) ---
            int expectedScaledWidth = 400;
            int expectedScaledHeight = 400;

            // --- 4. 定义在【缩放后】图片上，你希望文字达到的基础字体大小 ---
            // 例如，如果你希望500x500的图片上文字看起来是24pt那么大，就设为24。
            int baseFontSizeAtScaled = 35; // 稍微增大一点，通常居中后效果更好

            // --- 5. 定义颜色密度 (1-100%) ---
            int colorDensity = 13; // 8% 的密度 (你需要根据测试效果调整此值)

            // --- 6. 定义文字颜色 ---
            Color textColor = Color.CYAN;

            // --- 7. 调用主函数，生成未缩放的稀疏文字图片 ---
            System.out.println("正在生成未缩放的稀疏文字图片...");
            byte[] unscaledImageBytes = generateSparseTextUnscaled(
                    backgroundImageBytes, text,
                    outputUnscaledWidth, outputUnscaledHeight,
                    expectedScaledWidth, expectedScaledHeight,
                    baseFontSizeAtScaled,
                    colorDensity, textColor
            );

            // --- 8. 保存未缩放的图片 ---
            String unscaledOutputPath = "E:\\00\\0\\0\\新建文件夹\\稀疏文字-未缩放-居中.png";
            Files.write(Paths.get(unscaledOutputPath), unscaledImageBytes);
            System.out.println("未缩放图片已成功保存到: " + unscaledOutputPath);

            // --- 9. (仅用于测试) 模拟平台缩放后的效果 ---
            System.out.println("\n正在生成模拟缩放后的图片 (仅用于测试目的)...");
            BufferedImage unscaledImageBuffer = ImageIO.read(new ByteArrayInputStream(unscaledImageBytes));
            BufferedImage scaledTestImage = scaleImage(unscaledImageBuffer, expectedScaledWidth, expectedScaledHeight);

            // 将模拟缩放后的图片保存到文件
            String scaledTestOutputPath = "E:\\00\\0\\0\\新建文件夹\\稀疏文字-缩放后测试-居中.png";
            ByteArrayOutputStream scaledBaos = new ByteArrayOutputStream();
            ImageIO.write(scaledTestImage, "png", scaledBaos);
            Files.write(Paths.get(scaledTestOutputPath), scaledBaos.toByteArray());
            System.out.println("模拟缩放后的测试图片已成功保存到: " + scaledTestOutputPath);

            System.out.println("\n请对比 '" + unscaledOutputPath + "' (大图) 和 '" + scaledTestOutputPath + "' (小图)，观察稀疏文字在缩放后的可见性变化。");
            System.out.println("然后将 '" + unscaledOutputPath + "' 上传到目标平台进行实际测试。");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("图片处理失败: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("参数错误: " + e.getMessage());
        }
    }
}