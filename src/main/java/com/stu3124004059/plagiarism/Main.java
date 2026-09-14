package com.stu3124004059.plagiarism;

import java.io.IOException;
import java.util.Locale;

/**
 * 程序入口。
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("用法: java -jar main.jar <原文文件> <抄袭版论文文件> <答案文件>");
            System.exit(1);
            return;
        }

        try {
            String originalText = FileUtils.readText(args[0]);
            String copiedText = FileUtils.readText(args[1]);

            double similarity = SimilarityCalculator.cosineSimilarity(originalText, copiedText);

            // 输出百分制，保留两位小数
            String answer = String.format(Locale.ROOT, "%.2f", similarity);
            FileUtils.writeText(args[2], answer);
        } catch (IOException e) {
            System.err.println("文件读写失败: " + e.getMessage());
            System.exit(2);
        } catch (RuntimeException e) {
            System.err.println("程序运行异常: " + e.getMessage());
            System.exit(3);
        }
    }
}