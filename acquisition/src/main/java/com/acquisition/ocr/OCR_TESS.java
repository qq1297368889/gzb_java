package com.acquisition.ocr;

import gzb.tools.Tools;
import net.sourceforge.tess4j.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OCR_TESS {
    /**
     * 主方法，用于演示如何使用ImageOcrProcessor进行OCR识别。
     */
    public static void main(String[] args) throws Exception {
        String tessdataPath =  "C:/work/Tesseract-OCR/tessdata";
        byte[] imageBytes = Tools.fileReadByte("E:/00/0/0/orc-2.jpg");
        if (imageBytes == null) {
            System.err.println("图片字节数组为空，无法进行OCR识别。");
            return;
        }
        System.out.println("图片加载成功，大小: " + imageBytes.length + " 字节");

        try {
            // 创建OCR处理器实例
            OCR_TESS ocrTess = new OCR_TESS(tessdataPath);
            for (int i = 0; i < 10; i++) {
                long start= System.currentTimeMillis();
                List<RecognitionResult> chineseResults = ocrTess.ocr(imageBytes, "chi_sim");
                long end= System.currentTimeMillis();
                System.out.println(i+"  "+(end-start)+" "+chineseResults);
            }
        } catch (TesseractException e) {
            System.err.println("OCR识别错误: " + e.getMessage());
            e.printStackTrace();
            System.err.println("请检查Tesseract数据文件路径 (" + tessdataPath + ") 是否正确，以及所需的语言文件 (.traineddata) 是否已下载并放置在正确的位置。");
        } catch (IOException e) {
            System.err.println("图片处理错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public ITesseract tesseract; // Tesseract OCR引擎实例
    public String tessdataPath;  // Tesseract语言数据文件（.traineddata）所在的目录路径

    /**
     * 构造函数，初始化OCR引擎。
     * @param tessdataPath Tesseract语言数据文件（.traineddata）所在的目录路径。
     * 例如：如果chi_sim.traineddata在D:/tessdata/，则传入"D:/tessdata"。
     * 请确保该路径正确，且包含所需的语言数据文件。
     */
    public OCR_TESS(String tessdataPath) {
        this.tesseract = new Tesseract();
        this.tessdataPath = tessdataPath;
        tesseract.setDatapath(tessdataPath); // 设置Tesseract数据文件路径
        // 默认语言设置为中文，如果需要其他语言，可以在ocr方法中指定
        tesseract.setLanguage("chi_sim");
        // 可以根据需要添加其他Tesseract配置，例如OCR引擎模式、页面分割模式等

        tesseract.setOcrEngineMode(1); // OCR引擎模式，例如ITesseract.OcrEngineMode.LSTM_ONLY
        // tesseract.setPageSegMode(ITesseract.DEFAULT_PAGE_SEG_MODE); // 页面分割模式
    }

    /**
     * 对图片进行OCR识别，返回识别结果列表（包含文本和坐标）。
     * 待识别的图片都是屏幕截图，以字节数组形式传入。
     * @param imageBytes 待识别图片的字节数组（例如屏幕截图的字节数据）。
     * @param langType 语言类型，例如"chi_sim"表示简体中文，"eng"表示英文。
     * 如果为null或空字符串，则使用构造函数中设置的默认语言（chi_sim）。
     * 请确保对应的语言数据文件已放置在tessdataPath目录下。
     * @return 识别结果列表，每个结果包含识别出的文本及其在图片中的坐标。
     * @throws IOException 如果图片字节数组无法读取或处理。
     * @throws TesseractException 如果OCR识别过程中发生错误。
     */
    public List<RecognitionResult> ocr(byte[] imageBytes, String langType) throws IOException, TesseractException {
        // 将字节数组转换为BufferedImage对象，Tesseract引擎需要这种格式的图片
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

        if (bufferedImage == null) {
            throw new IOException("无法从提供的字节数组读取图片。请检查图片数据是否有效或格式受支持。");
        }

        // 设置当前OCR语言
        if (langType != null && !langType.isEmpty()) {
            tesseract.setLanguage(langType);
        } else {
            // 如果未指定语言，则使用构造函数中设置的默认语言
            tesseract.setLanguage("chi_sim"); // 确保默认语言设置
        }

        // 使用getWords方法获取每个单词的识别结果，包括文本和边界框坐标
        // ITesseract.RIL_WORD 表示按单词级别获取结果（更细粒度）
        // 您也可以尝试 ITesseract.RIL_TEXTLINE 按行获取

        List<Word> words = tesseract.getWords(bufferedImage, ITessAPI.TessPageIteratorLevel.RIL_TEXTLINE);
        List<RecognitionResult> results = new ArrayList<>();

        for (Word word : words) {
            // 获取单词的边界框信息
            Rectangle boundingBox = word.getBoundingBox();
            results.add(new RecognitionResult(
                    word.getText(),
                    boundingBox.x,
                    boundingBox.y,
                    boundingBox.width,
                    boundingBox.height
            ));
        }
        return results;
    }

}