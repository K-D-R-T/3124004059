package com.stu3124004059.plagiarism;

public class PerformanceTest {
    public static void main(String[] args) {
        // 构造一个很大的测试文本，模拟大文件
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("今天是星期天，天气晴朗，今天晚上我要去看电影。");
        }
        String largeText = sb.toString();
        String copiedText = largeText + "我们一起去吧。";

        System.out.println("开始性能测试...");
        long start = System.currentTimeMillis();

        // 循环计算 100 次，放大耗时
        for (int i = 0; i < 100; i++) {
            SimilarityCalculator.cosineSimilarity(largeText, copiedText);
        }

        long end = System.currentTimeMillis();
        System.out.println("100 次算法计算总耗时: " + (end - start) + " 毫秒");
        System.out.println("单次平均耗时: " + (end - start) / 100.0 + " 毫秒");
    }
}