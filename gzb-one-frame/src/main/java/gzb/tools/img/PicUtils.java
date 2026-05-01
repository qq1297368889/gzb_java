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

package gzb.tools.img;

import gzb.tools.FileTools;
import gzb.tools.Tools;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class PicUtils {
    public static byte[] stretchImage(byte[] src, int addWidth, int addHeight) {
        try {
            BufferedImage sourceImage = ImageIO.read(new ByteArrayInputStream(src));
            int originalWidth = sourceImage.getWidth();
            int originalHeight = sourceImage.getHeight();
            BufferedImage stretchedImage = new BufferedImage(originalWidth+addWidth, originalHeight+addHeight, sourceImage.getType());
            Graphics2D g2d = stretchedImage.createGraphics();
            g2d.drawImage(sourceImage, 0, 0, originalWidth+addWidth, originalHeight+addHeight, null);
            g2d.dispose();
            // 将 BufferedImage 转换回 byte 数组
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageWriteParam param = new JPEGImageWriteParam(Locale.getDefault());
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.99f);  // 调整这个值来控制质量，0 到 1 之间，1 为最高质量
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            writer.setOutput(ImageIO.createImageOutputStream(baos));
            writer.write(null, new IIOImage(stretchedImage, null, null), param);

            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static byte[] resizeImage(File file, int targetSize) throws Exception {
        return resizeImage(FileTools.readByte(file),targetSize);
    }
    public static byte[] resizeImage(byte[] imageData, int targetSize) {
        try {
            // 将 byte 数组转换为 BufferedImage
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));

            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();

            // 计算缩放比例
            double scale = Math.min((double) targetSize / originalWidth, (double) targetSize / originalHeight);

            int newWidth = (int) (originalWidth * scale);
            int newHeight = (int) (originalHeight * scale);

            // 创建新的 BufferedImage 用于绘制缩放后的图像
            BufferedImage resizedImage = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            // 绘制缩放后的图像 居中
            //g2d.drawImage(originalImage, (targetSize - newWidth) / 2, (targetSize - newHeight) / 2, newWidth, newHeight, null);
            // 绘制缩放后的图像（居左上）
            g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            // 填充黑色空白区域
            g2d.setColor(new Color(0xFFFFFF));
            g2d.fillRect(newWidth, 0, targetSize - newWidth, targetSize);
            g2d.fillRect(0, newHeight, targetSize, targetSize - newHeight);

            g2d.dispose();

            // 将 BufferedImage 转换回 byte 数组
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageWriteParam param = new JPEGImageWriteParam(Locale.getDefault());
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.99f);  // 调整这个值来控制质量，0 到 1 之间，1 为最高质量
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            writer.setOutput(ImageIO.createImageOutputStream(baos));
            writer.write(null, new IIOImage(resizedImage, null, null), param);

            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 强制压缩/放大图片到固定的大小
     *
     * @param w int 新宽度
     * @param h int 新高度
     * @throws IOException
     */
    public static final byte[] resize(File file, int w, int h) throws IOException {
        BufferedImage _image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Image img = ImageIO.read(file);
        _image.getGraphics().drawImage(img, 0, 0, w, h, null);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(_image, "jpg", os);
        return os.toByteArray();
    }

    public static final byte[] resize(Image img, int w, int h) throws IOException {
        BufferedImage _image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        _image.getGraphics().drawImage(img, 0, 0, w, h, null);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(_image, "jpg", os);
        return os.toByteArray();
    }

    /**
     * 按照固定的比例缩放图片
     *
     * @param t double 比例  0.1 0.2 ..... 1  1.1  1.2
     * @throws IOException
     */
    public static final byte[] resize(File file, double t) throws IOException {
        Image img = ImageIO.read(file);
        int width = img.getWidth(null); // 得到源图宽
        int height = img.getHeight(null); // 得到源图高
        int w = (int) (width * t);
        int h = (int) (height * t);
        return resize(img, w, h);
    }
    public static final byte[] resize(byte[]file, double t) throws IOException {
        Image img = ImageIO.read(new ByteArrayInputStream(file));
        int width = img.getWidth(null); // 得到源图宽
        int height = img.getHeight(null); // 得到源图高
        int w = (int) (width * t);
        int h = (int) (height * t);
        return resize(img, w, h);
    }

    /**
     * 以宽度为基准，等比例放缩图片
     *
     * @param w int 新宽度
     * @throws IOException
     */
    public static final byte[] resizeByWidth(byte[]file, int w) throws IOException {
        Image img = ImageIO.read(new ByteArrayInputStream(file));
        int width = img.getWidth(null); // 得到源图宽
        int height = img.getHeight(null); // 得到源图高
        int h = (int) (height * w / width);
        return resize(img, w, h);
    }

    /**
     * 以高度为基准，等比例缩放图片
     *
     * @param h int 新高度
     * @throws IOException
     */
    public static final byte[] resizeByHeight(byte[]file, int h) throws IOException {
        Image img = ImageIO.read(new ByteArrayInputStream(file));
        int width = img.getWidth(null); // 得到源图宽
        int height = img.getHeight(null); // 得到源图高
        int w = (int) (width * h / height);
        return resize(img, w, h);
    }
    public static final byte[] resizeByMin(File file, int px) throws IOException {
        Image img = ImageIO.read(file);
        int w = img.getWidth(null); // 得到源图宽
        int h = img.getHeight(null); // 得到源图高
        if (w > h){
            return resize(img, (w * px / h), px);
        }else{
            return resize(img, px, (h * px / w));
        }
    }


}