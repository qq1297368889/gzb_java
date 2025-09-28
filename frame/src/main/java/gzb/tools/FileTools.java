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

package gzb.tools;

import java.io.File;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

public class FileTools {
    // 预定义的测试文件名
    private static final String TEST_FILENAME = "test_file.txt";
    private static final String NEW_FILENAME = "new_file.txt";
    private static final String COPY_FILENAME = "copy_file.txt";
    private static final String MOVE_FILENAME = "moved_file.txt";

    public static void main(String[] args) {
        // ----------------- 测试准备 -----------------
        File testFile = new File(TEST_FILENAME);
        File newFile = new File(NEW_FILENAME);
        File copyFile = new File(COPY_FILENAME);
        File moveFile = new File(MOVE_FILENAME);

        // 确保测试环境干净
        FileTools.delete(testFile);
        FileTools.delete(newFile);
        FileTools.delete(copyFile);
        FileTools.delete(moveFile);

        System.out.println("--- FileTools 功能测试开始 ---");
        System.out.println("测试文件路径: " + testFile.getAbsolutePath());

        // ----------------- 1. 测试 save(String) -----------------
        String initialData = "这是第一行数据。\nThis is the second line.";
        boolean saveResult = FileTools.save(testFile, initialData);
        System.out.print("1. save(String) - 保存初始数据: " + saveResult);
        System.out.println(saveResult ? " [成功]" : " [失败]");

        // ----------------- 2. 测试 readString() -----------------
        String readData = FileTools.readString(testFile);
        boolean readCorrect = initialData.equals(readData);
        System.out.print("2. readString() - 读取并对比: " + readCorrect);
        System.out.println(readCorrect ? " [成功]" : " [失败]");
        if (!readCorrect) {
            System.out.println("    期望: " + initialData);
            System.out.println("    实际: " + readData);
        }

        // ----------------- 3. 测试 readArray() (按行) -----------------
        String[] expectedLines = initialData.split("\n");
        String[] readLines = FileTools.readArray(testFile);
        boolean arrayCorrect = readLines != null && Arrays.equals(expectedLines, readLines);
        System.out.print("3. readArray() (按行) - 读取并对比: " + arrayCorrect);
        System.out.println(arrayCorrect ? " [成功]" : " [失败]");

        // ----------------- 4. 测试 readArray(split) -----------------
        String splitData = "A,B,C,,";
        FileTools.save(newFile, splitData); // 使用新文件保存分割数据
        String[] expectedSplit = splitData.split(",", -1);
        String[] readSplit = FileTools.readArray(newFile, ",");
        boolean splitCorrect = readSplit != null && Arrays.equals(expectedSplit, readSplit);
        System.out.print("4. readArray(split) - 读取并对比: " + splitCorrect);
        System.out.println(splitCorrect ? " [成功]" : " [失败]");
        FileTools.delete(newFile); // 用完即删

        // ----------------- 5. 测试 append(String) -----------------
        String appendData = "\n这是追加的数据。";
        boolean appendResult = FileTools.append(testFile, appendData);
        System.out.print("5. append(String) - 追加数据: " + appendResult);
        System.out.println(appendResult ? " [成功]" : " [失败]");

        // ----------------- 6. 验证 append 结果 -----------------
        String finalData = initialData + appendData;
        String verifyData = FileTools.readString(testFile);
        boolean appendVerify = finalData.equals(verifyData);
        System.out.print("6. 验证 append 结果: " + appendVerify);
        System.out.println(appendVerify ? " [成功]" : " [失败]");

        // ----------------- 7. 测试 copy() -----------------
        boolean copyResult = FileTools.copy(testFile, copyFile);
        boolean copyVerify = copyResult && FileTools.readString(copyFile).equals(finalData);
        System.out.print("7. copy() - 复制文件并验证内容: " + copyVerify);
        System.out.println(copyVerify ? " [成功]" : " [失败]");

        // ----------------- 8. 测试 toNewFile() (移动) -----------------
        boolean moveResult = FileTools.toNewFile(copyFile, moveFile);
        // 验证源文件不存在，目标文件存在且内容正确
        boolean moveVerify = moveResult && !copyFile.exists() && FileTools.readString(moveFile).equals(finalData);
        System.out.print("8. toNewFile() - 移动文件并验证: " + moveVerify);
        System.out.println(moveVerify ? " [成功]" : " [失败]");

        // ----------------- 9. 测试 delete() -----------------
        boolean deleteTest = FileTools.delete(testFile);
        boolean deleteMove = FileTools.delete(moveFile);
        boolean deleteVerify = deleteTest && deleteMove && !testFile.exists() && !moveFile.exists();
        System.out.print("9. delete() - 删除所有文件并验证: " + deleteVerify);
        System.out.println(deleteVerify ? " [成功]" : " [失败]");

        System.out.println("--- FileTools 功能测试结束 ---");
    }
    // 统一使用 UTF-8 编码，防止乱码
    private static final Charset ENCODING = Config.encoding;
    /**
     * 循环创建目录（包括所有不存在的父目录）。
     * 性能优化：使用 Files.createDirectories (JDK 7+)。
     * @param file 要创建的目录
     */
    public static void mkdir(File file) {
        if (file == null) {
            return;
        }
        // 检查是否已存在或已是目录，如果是则直接返回
        if (file.exists() && file.isDirectory()) {
            return;
        }
        try {
            // NIO API，高效地递归创建所有不存在的父目录
            Files.createDirectories(file.toPath());
        } catch (IOException e) {
            System.err.println("创建目录失败：" + file.toPath().toAbsolutePath());
            e.printStackTrace();
        }
    }
    // --- 读取操作 (Read Operations) ---

    /**
     * 读取文件所有字节。
     * 性能优化：使用 Files.readAllBytes，这是 NIO 优化的，兼容 JDK 7+。
     * @param file 要读取的文件
     * @return 文件的所有字节数组，失败返回 null。
     */
    public static byte[] readByte(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            System.err.println("读取字节失败：" + file.getAbsolutePath());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取文件所有内容为字符串。
     * 编码统一：使用 UTF-8。
     * 性能优化：使用 NIO readAllBytes + String 构造函数。
     * @param file 要读取的文件
     * @return 文件的所有内容字符串，失败返回 null。
     */
    public static String readString(File file) {
        byte[] bytes = readByte(file);
        if (bytes == null) {
            return null;
        }
        // JDK 8 兼容的字符串解码方式
        return new String(bytes, ENCODING);
    }

    /**
     * 读取文本内容，并根据换行符分割成数组返回。
     * 编码统一：使用 UTF-8。
     * 性能优化：使用 Files.readAllLines (JDK 7+)，高效且内存友好。
     * @param file 要读取的文件
     * @return 按行分割的字符串数组，失败返回 null。
     */
    public static String[] readArray(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        try {
            // Files.readAllLines 是 JDK 7+ 的高效 API
            List<String> lines = Files.readAllLines(file.toPath(), ENCODING);
            // List<T>.toArray(T[] a) 是 JDK 6+ 的标准用法
            return lines.toArray(new String[0]);
        } catch (IOException e) {
            System.err.println("读取行数组失败：" + file.getAbsolutePath());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取文本内容，并根据指定分隔符分割成数组返回。
     * 性能优化：先读取整个文件为字符串，然后进行分割。
     * @param file 要读取的文件
     * @param split 指定的分隔符
     * @return 分割后的字符串数组，失败或内容为空返回 null。
     */
    public static String[] readArray(File file, String split) {
        String content = readString(file);
        if (content == null || content.isEmpty()) {
            return null;
        }
        // 使用负数限制，确保末尾空白项也被保留
        return content.split(split, -1);
    }

    // --- 写入/保存操作 (Write/Save Operations) ---

    /**
     * 将字节数组数据写入文件，如果文件存在则覆盖。
     * 性能优化：使用 NIO Files.write (JDK 7+)，效率高。
     * @param file 目标文件
     * @param data 要写入的字节数组
     * @return 成功返回 true，失败返回 false。
     */
    public static boolean save(File file, byte[] data) {
        if (file == null || data == null) return false;
        try {
            // 使用 Files.write 简化代码，并利用 NIO 优化
            Files.write(file.toPath(), data);
            return true;
        } catch (IOException e) {
            System.err.println("保存字节数据失败：" + file.getAbsolutePath());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将字符串数据写入文件，如果文件存在则覆盖。
     * 编码统一：先转为 UTF-8 字节，然后写入。
     * @param file 目标文件
     * @param data 要写入的字符串
     * @return 成功返回 true，失败返回 false。
     */
    public static boolean save(File file, String data) {
        if (file == null || data == null) return false;
        // 编码转换：使用 UTF-8
        byte[] bytes = data.getBytes(ENCODING);
        return save(file, bytes);
    }

    /**
     * 将字节数组数据追加到文件末尾。
     * 性能优化：使用 FileOutputStream + true (append) + 缓冲流。
     * @param file 目标文件
     * @param data 要追加的字节数组
     * @return 成功返回 true，失败返回 false。
     */
    public static boolean append(File file, byte[] data) {
        if (file == null || data == null) return false;
        try (FileOutputStream fos = new FileOutputStream(file, true)) { // true 表示追加模式
            fos.write(data);
            return true;
        } catch (IOException e) {
            System.err.println("追加字节数据失败：" + file.getAbsolutePath());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 将字符串数据追加到文件末尾。
     * 编码统一：使用 UTF-8，性能优化：使用 BufferedWriter。
     * @param file 目标文件
     * @param data 要追加的字符串
     * @return 成功返回 true，失败返回 false。
     */
    public static boolean append(File file, String data) {
        if (file == null || data == null) return false;
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, true), ENCODING))) {
            writer.write(data);
            writer.flush();
            return true;
        } catch (IOException e) {
            System.err.println("追加字符串失败：" + file.getAbsolutePath());
            e.printStackTrace();
            return false;
        }
    }

    // --- 文件操作 (File Operations) ---

    /**
     * 删除文件。
     * @param file 要删除的文件
     * @return 成功返回 true，失败返回 false。
     */
    public static boolean delete(File file) {
        if (file == null || !file.exists()) return true; // 文件不存在也算成功
        try {
            // Files.delete(path) 在删除失败时会抛出异常，比 file.delete() 更可靠
            Files.delete(file.toPath());
            return true;
        } catch (IOException e) {
            System.err.println("删除文件失败：" + file.getAbsolutePath());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 复制文件 (保留源文件)。
     * 性能优化：使用 Files.copy，NIO 零拷贝优化。
     * @param file 源文件
     * @param newFile 目标文件
     * @return 成功返回 true，失败返回 false。
     */
    public static boolean copy(File file, File newFile) {
        if (file == null || newFile == null || !file.exists() || file.isDirectory()) return false;
        try {
            // REPLACE_EXISTING: 如果目标文件存在则覆盖
            Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("复制文件失败：" + file.getAbsolutePath() + " -> " + newFile.getAbsolutePath());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移动/重命名文件 (删除源文件)。
     * 性能优化：使用 Files.move，NIO 零拷贝优化。
     * @param file 源文件
     * @param newFile 目标文件
     * @return 成功返回 true，失败返回 false。
     */
    public static boolean toNewFile(File file, File newFile) {
        if (file == null || newFile == null || !file.exists() || file.isDirectory()) return false;
        try {
            // REPLACE_EXISTING: 如果目标文件存在则覆盖。
            // ATOMIC_MOVE: 尝试原子操作（某些文件系统支持），确保操作的完整性。
            Files.move(file.toPath(), newFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING); // ATOMIC_MOVE 并非所有系统都支持，这里简化
            return true;
        } catch (IOException e) {
            System.err.println("移动文件失败：" + file.getAbsolutePath() + " -> " + newFile.getAbsolutePath());
            e.printStackTrace();
            return false;
        }
    }
}