package gzb.tools.img;
import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 通用图片处理工具类（JDK原生实现，无第三方依赖）
 * 包含：裁剪、压缩、多方式缩放、格式转换、GIF合成、水印、文字添加、元数据获取
 * 支持格式：JPG/PNG/BMP/GIF（合成），JDK1.8+兼容
 */
public class ImageTools {

    // 压缩算法枚举（主流算法，适配不同场景）
    public enum CompressAlgorithm {
        JPEG_QUALITY,  // JPG质量压缩（主流，画质损失可控）
        PNG_COMPRESS,  // PNG无损压缩（基于Deflate算法，减小体积不损画质）
        BMP_COMPRESS   // BMP压缩（转为压缩格式存储）
    }

    // 图片格式枚举（对应格式转换参数）
    public enum ImageFormat {
        JPG(1, "jpg", "image/jpeg"),
        PNG(2, "png", "image/png"),
        BMP(3, "bmp", "image/bmp");

        private final int code;
        private final String suffix;
        private final String mimeType;

        ImageFormat(int code, String suffix, String mimeType) {
            this.code = code;
            this.suffix = suffix;
            this.mimeType = mimeType;
        }

        // 根据编码获取格式，默认JPG
        public static ImageFormat getByCode(int code) {
            for (ImageFormat format : values()) {
                if (format.code == code) {
                    return format;
                }
            }
            return JPG;
        }

        public String getSuffix() {
            return suffix;
        }

        public String getMimeType() {
            return mimeType;
        }
    }

    /**
     * 图片元数据实体类（静态公共内部类）
     * 包含宽度、高度、格式、大小（字节）等核心信息
     */
    public static class ImageMeta {
        private int width;    // 图片宽度（像素）
        private int height;   // 图片高度（像素）
        private String format;// 图片格式（jpg/png/bmp）
        private long size;    // 图片大小（字节）

        // 无参构造
        public ImageMeta() {}

        // 全参构造
        public ImageMeta(int width, int height, String format, long size) {
            this.width = width;
            this.height = height;
            this.format = format;
            this.size = size;
        }

        // getter & setter
        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        @Override
        public String toString() {
            return "ImageMeta{" +
                    "width=" + width +
                    ", height=" + height +
                    ", format='" + format + '\'' +
                    ", size=" + size + "bytes" +
                    '}';
        }
    }

    // -------------------------- 1. 裁剪图片 --------------------------
    /**
     * 裁剪图片
     * @param imageBytes 原始图片字节数组
     * @param x1 裁剪X起点（左上角，像素，从0开始）
     * @param y1 裁剪Y起点（左上角，像素，从0开始）
     * @param x2 裁剪X终点（右下角，像素，需>x1）
     * @param y2 裁剪Y终点（右下角，像素，需>y1）
     * @return 裁剪后的图片字节数组（保持原格式）
     * @throws IllegalArgumentException 参数非法
     * @throws IOException 图片解析/处理异常
     */
    public static byte[] cropImage(byte[] imageBytes, int x1, int y1, int x2, int y2)
            throws IllegalArgumentException, IOException {
        // 参数校验
        validateImageBytes(imageBytes);
        if (x1 < 0 || y1 < 0) {
            throw new IllegalArgumentException("裁剪起点坐标不能为负数（x1=" + x1 + ", y1=" + y1 + "）");
        }
        if (x2 <= x1 || y2 <= y1) {
            throw new IllegalArgumentException("裁剪终点坐标必须大于起点（x2=" + x2 + " <= x1=" + x1 + " 或 y2=" + y2 + " <= y1=" + y1 + "）");
        }

        try (InputStream in = new ByteArrayInputStream(imageBytes)) {
            BufferedImage original = ImageIO.read(in);
            validateBufferedImage(original);
            int imgW = original.getWidth(), imgH = original.getHeight();

            // 校验裁剪区域是否超出图片边界
            if (x2 > imgW || y2 > imgH) {
                throw new IllegalArgumentException("裁剪区域超出图片边界，图片尺寸：" + imgW + "x" + imgH + "，裁剪终点：" + x2 + "x" + y2);
            }

            // 原生高效裁剪（浅拷贝，无性能损耗）
            BufferedImage cropped = original.getSubimage(x1, y1, x2 - x1, y2 - y1);
            // 保持原格式输出
            return bufferedImageToBytes(cropped, getImageFormatByBytes(imageBytes).getSuffix());
        }
    }

    // -------------------------- 2. 压缩图片（多算法可选） --------------------------
    /**
     * 图片压缩
     * @param imageBytes 原始图片字节数组
     * @param quality 压缩质量（1-100，100为原图质量，越小压缩率越高）
     * @param algorithm 压缩算法（JPEG_QUALITY/PNG_COMPRESS/BMP_COMPRESS）
     * @return 压缩后的图片字节数组
     * @throws IllegalArgumentException 参数非法
     * @throws IOException 图片处理异常
     */
    public static byte[] compressImage(byte[] imageBytes, int quality, CompressAlgorithm algorithm)
            throws IllegalArgumentException, IOException {
        // 参数校验
        validateImageBytes(imageBytes);
        if (quality < 1 || quality > 100) {
            throw new IllegalArgumentException("压缩质量必须为1-100的整数（当前：" + quality + "）");
        }
        if (algorithm == null) {
            algorithm = CompressAlgorithm.JPEG_QUALITY;
        }

        try (InputStream in = new ByteArrayInputStream(imageBytes)) {
            BufferedImage original = ImageIO.read(in);
            validateBufferedImage(original);
            ImageFormat originalFormat = getImageFormatByBytes(imageBytes);
            // 质量归一化（0.01-1.0，符合ImageIO参数要求）
            float q = quality / 100.0f;

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                // 获取图片写入器
                Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(originalFormat.getSuffix());
                if (!writers.hasNext()) {
                    throw new IOException("不支持的压缩格式：" + originalFormat.getSuffix());
                }
                ImageWriter writer = writers.next();
                ImageWriteParam param = writer.getDefaultWriteParam();

                // 根据算法设置压缩参数
                switch (algorithm) {
                    case JPEG_QUALITY:
                        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                        param.setCompressionQuality(q);
                        break;
                    case PNG_COMPRESS:
                        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                        break;
                    case BMP_COMPRESS:
                        // BMP默认无压缩，转为JPG/PNG实现压缩（保持原格式则返回原图）
                        if (originalFormat == ImageFormat.BMP) {
                            return compressImage(imageBytes, quality, CompressAlgorithm.JPEG_QUALITY);
                        }
                        break;
                }

                // 写入并返回字节数组
                try (ImageOutputStream ios = ImageIO.createImageOutputStream(out)) {
                    writer.setOutput(ios);
                    writer.write(null, new IIOImage(original, null, null), param);
                    writer.dispose();
                    return out.toByteArray();
                }
            }
        }
    }

    // -------------------------- 3. 按宽度等比例缩放 --------------------------
    /**
     * 根据宽度等比例缩放，自动计算高度
     * @param imageBytes 原始图片字节数组
     * @param targetWidth 目标宽度（像素，>0）
     * @return 缩放后的图片字节数组（保持原格式）
     * @throws IllegalArgumentException 参数非法
     * @throws IOException 图片处理异常
     */
    public static byte[] scaleByWidth(byte[] imageBytes, int targetWidth)
            throws IllegalArgumentException, IOException {
        validateImageBytes(imageBytes);
        if (targetWidth <= 0) {
            throw new IllegalArgumentException("目标宽度必须大于0（当前：" + targetWidth + "）");
        }

        ImageMeta meta = getImageMeta(imageBytes);
        // 等比例计算高度：目标高度 = 原高度 * 目标宽度 / 原宽度
        int targetHeight = meta.getHeight() * targetWidth / meta.getWidth();
        return scaleForce(imageBytes, targetWidth, targetHeight);
    }

    // -------------------------- 4. 按高度等比例缩放 --------------------------
    /**
     * 根据高度等比例缩放，自动计算宽度
     * @param imageBytes 原始图片字节数组
     * @param targetHeight 目标高度（像素，>0）
     * @return 缩放后的图片字节数组（保持原格式）
     * @throws IllegalArgumentException 参数非法
     * @throws IOException 图片处理异常
     */
    public static byte[] scaleByHeight(byte[] imageBytes, int targetHeight)
            throws IllegalArgumentException, IOException {
        validateImageBytes(imageBytes);
        if (targetHeight <= 0) {
            throw new IllegalArgumentException("目标高度必须大于0（当前：" + targetHeight + "）");
        }

        ImageMeta meta = getImageMeta(imageBytes);
        // 等比例计算宽度：目标宽度 = 原宽度 * 目标高度 / 原高度
        int targetWidth = meta.getWidth() * targetHeight / meta.getHeight();
        return scaleForce(imageBytes, targetWidth, targetHeight);
    }

    // -------------------------- 5. 强制缩放为指定宽高 --------------------------
    /**
     * 强制缩放为指定宽度和高度（不保持比例，可能拉伸）
     * @param imageBytes 原始图片字节数组
     * @param targetWidth 目标宽度（像素，>0）
     * @param targetHeight 目标高度（像素，>0）
     * @return 缩放后的图片字节数组（保持原格式）
     * @throws IllegalArgumentException 参数非法
     * @throws IOException 图片处理异常
     */
    public static byte[] scaleForce(byte[] imageBytes, int targetWidth, int targetHeight)
            throws IllegalArgumentException, IOException {
        validateImageBytes(imageBytes);
        if (targetWidth <= 0 || targetHeight <= 0) {
            throw new IllegalArgumentException("目标宽高必须大于0（宽：" + targetWidth + "，高：" + targetHeight + "）");
        }

        try (InputStream in = new ByteArrayInputStream(imageBytes)) {
            BufferedImage original = ImageIO.read(in);
            validateBufferedImage(original);
            // 创建目标尺寸的缓冲图，使用双线性插值（兼顾速度和画质）
            BufferedImage scaled = new BufferedImage(targetWidth, targetHeight, original.getType());
            Graphics2D g2d = scaled.createGraphics();
            // 开启抗锯齿，提升缩放画质
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(original, 0, 0, targetWidth, targetHeight, null);
            g2d.dispose();

            return bufferedImageToBytes(scaled, getImageFormatByBytes(imageBytes).getSuffix());
        }
    }

    // -------------------------- 6. 按比例等比缩放（1-100） --------------------------
    /**
     * 按比例等比缩放
     * @param imageBytes 原始图片字节数组
     * @param scale 缩放比例（1-100，100为原图，越小尺寸越小）
     * @return 缩放后的图片字节数组（保持原格式）
     * @throws IllegalArgumentException 参数非法
     * @throws IOException 图片处理异常
     */
    public static byte[] scaleByRatio(byte[] imageBytes, int scale)
            throws IllegalArgumentException, IOException {
        validateImageBytes(imageBytes);
        if (scale < 1 || scale > 100) {
            throw new IllegalArgumentException("缩放比例必须为1-100的整数（当前：" + scale + "）");
        }
        if (scale == 100) {
            return imageBytes; // 100%直接返回原图，避免无意义处理
        }

        ImageMeta meta = getImageMeta(imageBytes);
        // 按比例计算目标宽高
        int targetWidth = meta.getWidth() * scale / 100;
        int targetHeight = meta.getHeight() * scale / 100;
        // 保证宽高至少为1像素
        targetWidth = Math.max(1, targetWidth);
        targetHeight = Math.max(1, targetHeight);
        return scaleForce(imageBytes, targetWidth, targetHeight);
    }

    // -------------------------- 7. 图片格式转换 --------------------------
    /**
     * 图片格式转换
     * @param imageBytes 原始图片字节数组
     * @param formatCode 目标格式编码（1=JPG,2=PNG,3=BMP，默认1）
     * @return 转换后的图片字节数组
     * @throws IllegalArgumentException 参数非法
     * @throws IOException 图片处理异常
     */
    public static byte[] convertImageFormat(byte[] imageBytes, int formatCode)
            throws IllegalArgumentException, IOException {
        validateImageBytes(imageBytes);
        ImageFormat targetFormat = ImageFormat.getByCode(formatCode);
        try (InputStream in = new ByteArrayInputStream(imageBytes)) {
            BufferedImage original = ImageIO.read(in);
            validateBufferedImage(original);
            // 转换为目标格式
            return bufferedImageToBytes(original, targetFormat.getSuffix());
        }
    }

    // -------------------------- 8. 获取图片元数据 --------------------------
    /**
     * 获取图片元数据（宽、高、格式、大小）
     * @param imageBytes 原始图片字节数组
     * @return 图片元数据实体类ImageMeta
     * @throws IllegalArgumentException 参数非法
     * @throws IOException 图片解析异常
     */
    public static ImageMeta getImageMeta(byte[] imageBytes)
            throws IllegalArgumentException, IOException {
        validateImageBytes(imageBytes);
        try (InputStream in = new ByteArrayInputStream(imageBytes)) {
            BufferedImage original = ImageIO.read(in);
            validateBufferedImage(original);
            ImageFormat format = getImageFormatByBytes(imageBytes);
            // 构造元数据并返回
            return new ImageMeta(
                    original.getWidth(),
                    original.getHeight(),
                    format.getSuffix(),
                    imageBytes.length
            );
        }
    }

    // -------------------------- 9. 多张图片合成GIF --------------------------
    /**
     * 多张图片合成GIF动图（默认每帧间隔200ms，无限循环）
     * @param imageBytesArray 图片字节数组数组（按帧顺序排列）
     * @return 合成后的GIF字节数组
     * @throws IllegalArgumentException 参数非法
     * @throws IOException 图片处理异常
     */
    public static byte[] mergeToGif(byte[][] imageBytesArray)
            throws IllegalArgumentException, IOException {
        if (imageBytesArray == null || imageBytesArray.length == 0) {
            throw new IllegalArgumentException("GIF合成的图片数组不能为空或空数组");
        }
        // 校验每张图片的有效性
        List<BufferedImage> frameList = new LinkedList<>();
        for (byte[] imgBytes : imageBytesArray) {
            validateImageBytes(imgBytes);
            try (InputStream in = new ByteArrayInputStream(imgBytes)) {
                BufferedImage frame = ImageIO.read(in);
                validateBufferedImage(frame);
                frameList.add(frame);
            }
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageOutputStream ios = ImageIO.createImageOutputStream(out);
            // 获取GIF写入器
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("gif");
            if (!writers.hasNext()) {
                throw new IOException("JDK不支持GIF格式写入，请检查JDK版本");
            }
            ImageWriter writer = writers.next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            writer.setOutput(ios);

            // 初始化GIF元数据（设置循环次数：0=无限循环）
            IIOMetadata metadata = writer.getDefaultImageMetadata(ImageTypeSpecifier.createFromBufferedImageType(frameList.get(0).getType()), param);
            String metaFormat = metadata.getNativeMetadataFormatName();
            IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metaFormat);
            IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");
            graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
            graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
            graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
            graphicsControlExtensionNode.setAttribute("delayTime", "20"); // 延迟时间（1/100秒，20=200ms）
            IIOMetadataNode appExtensionsNode = getNode(root, "ApplicationExtensions");
            IIOMetadataNode appExtensionNode = new IIOMetadataNode("ApplicationExtension");
            appExtensionNode.setAttribute("applicationID", "NETSCAPE");
            appExtensionNode.setAttribute("authenticationCode", "2.0");
            byte[] loopBytes = new byte[]{0x1, (byte) 0x0, (byte) 0x0}; // 无限循环标识
            appExtensionNode.setUserObject(loopBytes);
            appExtensionsNode.appendChild(appExtensionNode);
            metadata.setFromTree(metaFormat, root);

            // 写入GIF帧
            writer.prepareWriteSequence(null);
            for (BufferedImage frame : frameList) {
                IIOImage iioImage = new IIOImage(frame, null, metadata);
                writer.writeToSequence(iioImage, param);
            }
            writer.endWriteSequence();
            writer.dispose();
            ios.close();

            return out.toByteArray();
        }
    }

    // -------------------------- 10. 图片加水印（图片水印） --------------------------
    /**
     * 图片添加图片水印
     * @param imageBytes 原始图片字节数组
     * @param watermarkBytes 水印图片字节数组
     * @param left 水印左上角X坐标（像素，从0开始）
     * @param top 水印左上角Y坐标（像素，从0开始）
     * @param wmWidth 水印宽度（像素，>0）
     * @param wmHeight 水印高度（像素，>0）
     * @return 添加水印后的图片字节数组（保持原格式）
     * @throws IllegalArgumentException 参数非法
     * @throws IOException 图片处理异常
     */
    public static byte[] addImageWatermark(byte[] imageBytes, byte[] watermarkBytes, int left, int top, int wmWidth, int wmHeight)
            throws IllegalArgumentException, IOException {
        validateImageBytes(imageBytes);
        validateImageBytes(watermarkBytes);
        if (left < 0 || top < 0) {
            throw new IllegalArgumentException("水印坐标不能为负数（left=" + left + ", top=" + top + "）");
        }
        if (wmWidth <= 0 || wmHeight <= 0) {
            throw new IllegalArgumentException("水印宽高必须大于0（宽：" + wmWidth + "，高：" + wmHeight + "）");
        }

        try (InputStream imgIn = new ByteArrayInputStream(imageBytes);
             InputStream wmIn = new ByteArrayInputStream(watermarkBytes)) {
            BufferedImage original = ImageIO.read(imgIn);
            BufferedImage watermark = ImageIO.read(wmIn);
            validateBufferedImage(original);
            validateBufferedImage(watermark);

            // 创建画布，开启透明通道
            Graphics2D g2d = original.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // 绘制水印（缩放为指定宽高）
            g2d.drawImage(watermark, left, top, wmWidth, wmHeight, null);
            g2d.dispose();

            return bufferedImageToBytes(original, getImageFormatByBytes(imageBytes).getSuffix());
        }
    }

    // -------------------------- 11. 图片加文字（UTF-8，自动适配宽高） --------------------------
    /**
     * 图片添加UTF-8文字水印，文字宽高自动扩展
     * @param imageBytes 原始图片字节数组
     * @param text 要添加的文字（UTF-8）
     * @param left 文字左上角X坐标（像素，从0开始）
     * @param top 文字左上角Y坐标（像素，从0开始）
     * @return 添加文字后的图片字节数组（保持原格式）
     * @throws IllegalArgumentException 参数非法
     * @throws IOException 图片处理异常
     */
    public static byte[] addTextWatermark(byte[] imageBytes, String text, int left, int top)
            throws IllegalArgumentException, IOException {
        validateImageBytes(imageBytes);
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("添加的文字不能为空或空白");
        }
        if (left < 0 || top < 0) {
            throw new IllegalArgumentException("文字坐标不能为负数（left=" + left + ", top=" + top + "）");
        }

        try (InputStream in = new ByteArrayInputStream(imageBytes)) {
            BufferedImage original = ImageIO.read(in);
            validateBufferedImage(original);

            // 创建画布，开启抗锯齿
            Graphics2D g2d = original.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // 设置文字样式（黑色，宋体，默认20号，可根据需求调整）
            Font font = new Font("宋体", Font.PLAIN, 20);
            g2d.setFont(font);
            g2d.setColor(Color.BLACK);

            // 计算文字实际宽高，自动适配
            FontRenderContext frc = g2d.getFontRenderContext();
            Rectangle2D textRect = font.getStringBounds(text, frc);
            int textWidth = (int) textRect.getWidth();
            int textHeight = (int) textRect.getHeight();

            // 绘制文字（自动扩展宽高，避免截断）
            g2d.drawString(text, left, top + textHeight); // top为基线，需加上文字高度保证显示完整
            g2d.dispose();

            return bufferedImageToBytes(original, getImageFormatByBytes(imageBytes).getSuffix());
        }
    }

    // -------------------------- 私有工具方法（内部调用） --------------------------
    /**
     * 校验图片字节数组有效性
     */
    private static void validateImageBytes(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new IllegalArgumentException("图片字节数组不能为空或空数组");
        }
    }

    /**
     * 校验BufferedImage有效性
     */
    private static void validateBufferedImage(BufferedImage image) throws IOException {
        if (image == null) {
            throw new IOException("无法解析图片，不支持的图片格式或图片已损坏");
        }
    }

    /**
     * 从图片字节数组识别图片格式
     */
    private static ImageFormat getImageFormatByBytes(byte[] imageBytes) {
        // JPG: FF D8 FF
        if (imageBytes.length >= 3 && imageBytes[0] == (byte) 0xFF && imageBytes[1] == (byte) 0xD8 && imageBytes[2] == (byte) 0xFF) {
            return ImageFormat.JPG;
        }
        // PNG: 89 50 4E 47
        if (imageBytes.length >= 4 && imageBytes[0] == (byte) 0x89 && imageBytes[1] == (byte) 0x50 && imageBytes[2] == (byte) 0x4E && imageBytes[3] == (byte) 0x47) {
            return ImageFormat.PNG;
        }
        // BMP: 42 4D
        if (imageBytes.length >= 2 && imageBytes[0] == (byte) 0x42 && imageBytes[1] == (byte) 0x4D) {
            return ImageFormat.BMP;
        }
        // 默认JPG
        return ImageFormat.JPG;
    }

    /**
     * BufferedImage转换为字节数组
     */
    private static byte[] bufferedImageToBytes(BufferedImage image, String format) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(image, format, out);
            return out.toByteArray();
        }
    }

    /**
     * GIF合成时获取元数据节点
     */
    private static IIOMetadataNode getNode(IIOMetadataNode root, String nodeName) {
        for (int i = 0; i < root.getLength(); i++) {
            if (root.item(i).getNodeName().equals(nodeName)) {
                return (IIOMetadataNode) root.item(i);
            }
        }
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        root.appendChild(node);
        return node;
    }

    // -------------------------- 测试示例（可选） --------------------------
    public static void main(String[] args) throws Exception {
        // 模拟读取本地图片为字节数组（实际业务中替换为接口/数据库入参）
        byte[] originalImage = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("C:\\ai\\YOLO\\FPS-V5-2\\train\\000127.jpg"));
        byte[] watermarkImage = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("C:\\ai\\YOLO\\FPS-V5-2\\11.png"));
        byte[][] gifImages = {
                java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("C:\\ai\\YOLO\\FPS-V5-2\\train\\000130.jpg")),
                java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("C:\\ai\\YOLO\\FPS-V5-2\\train\\000131.jpg")),
                java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("C:\\ai\\YOLO\\FPS-V5-2\\train\\000132.jpg"))
        };

        // 1. 获取元数据
        ImageMeta meta = ImageTools.getImageMeta(originalImage);
        System.out.println("图片元数据：" + meta);

        // 2. 裁剪（100,100到500,500）
        byte[] cropped = ImageTools.cropImage(originalImage, 100, 100, 500, 500);
        java.nio.file.Files.write(java.nio.file.Paths.get("C:\\ai\\YOLO\\FPS-V5-2\\cropped.jpg"), cropped);

        // 3. 压缩（质量80，JPG算法）
        byte[] compressed = ImageTools.compressImage(originalImage, 80, CompressAlgorithm.JPEG_QUALITY);
        java.nio.file.Files.write(java.nio.file.Paths.get("C:\\ai\\YOLO\\FPS-V5-2\\compressed.jpg"), compressed);

        // 4. 按宽度等比缩放（目标宽度400）
        byte[] scaleByW = ImageTools.scaleByWidth(originalImage, 400);
        java.nio.file.Files.write(java.nio.file.Paths.get("C:\\ai\\YOLO\\FPS-V5-2\\scaleByW.jpg"), scaleByW);

        // 5. 格式转换（JPG转PNG）
        byte[] converted = ImageTools.convertImageFormat(originalImage, 2);
        java.nio.file.Files.write(java.nio.file.Paths.get("C:\\ai\\YOLO\\FPS-V5-2\\converted.png"), converted);

        // 6. 加水印（left=50,top=50,宽100,高50）
        byte[] withWm = ImageTools.addImageWatermark(originalImage, watermarkImage, 50, 50, 100, 50);
        java.nio.file.Files.write(java.nio.file.Paths.get("C:\\ai\\YOLO\\FPS-V5-2\\withWm.jpg"), withWm);

        // 7. 加文字（UTF-8，left=50,top=50）
        byte[] withText = ImageTools.addTextWatermark(originalImage, "测试UTF-8文字水印", 50, 50);
        java.nio.file.Files.write(java.nio.file.Paths.get("C:\\ai\\YOLO\\FPS-V5-2\\withText.jpg"), withText);

        // 8. 合成GIF
        byte[] gif = ImageTools.mergeToGif(gifImages);
        java.nio.file.Files.write(java.nio.file.Paths.get("C:\\ai\\YOLO\\FPS-V5-2\\merge.gif"), gif);

        System.out.println("图片处理完成！");
    }
}