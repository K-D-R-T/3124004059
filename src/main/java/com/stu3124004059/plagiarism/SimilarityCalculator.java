package com.stu3124004059.plagiarism;

import java.util.HashMap;
import java.util.Map;

/**
 * 论文查重核心算法：
 * 剥离 HTML 标签 -> 文本预处理 -> 字符 2-gram -> 余弦相似度 -> 百分制。
 */
public final class SimilarityCalculator {

    private static final int DEFAULT_N = 2;

    private SimilarityCalculator() {
    }

    public static double cosineSimilarity(String text1, String text2) {
        return cosineSimilarity(text1, text2, DEFAULT_N);
    }

    public static double cosineSimilarity(String text1, String text2, int n) {
        String clean1 = stripHtml(text1);
        String clean2 = stripHtml(text2);

        String normalized1 = normalize(clean1);
        String normalized2 = normalize(clean2);

        if (normalized1.isEmpty() || normalized2.isEmpty()) {
            return 0.0;
        }

        Map<String, Integer> grams1 = buildNGrams(normalized1, n);
        Map<String, Integer> grams2 = buildNGrams(normalized2, n);

        if (grams1.isEmpty() || grams2.isEmpty()) {
            return 0.0;
        }

        Map<String, Integer> smaller = grams1.size() <= grams2.size() ? grams1 : grams2;
        Map<String, Integer> larger = smaller == grams1 ? grams2 : grams1;

        long dotProduct = 0L;
        for (Map.Entry<String, Integer> entry : smaller.entrySet()) {
            Integer count = larger.get(entry.getKey());
            if (count != null) {
                dotProduct += (long) entry.getValue() * count;
            }
        }

        double norm1 = 0.0;
        for (int count : grams1.values()) {
            norm1 += (double) count * count;
        }

        double norm2 = 0.0;
        for (int count : grams2.values()) {
            norm2 += (double) count * count;
        }

        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }

        double similarity = dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
        similarity = Math.max(0.0, Math.min(1.0, similarity));

        return Math.round(similarity * 10000.0) / 100.0;
    }

    static String stripHtml(String text) {
        if (text == null) {
            return "";
        }
        // 去掉 <head>...</head> 整个部分（包含标题、样式、脚本等，它们不是论文正文）
        String result = text.replaceAll("(?is)<head[^>]*>.*?</head>", " ");
        // 去掉 <script>...</script> 块
        result = result.replaceAll("(?is)<script[^>]*>.*?</script>", " ");
        // 去掉 <style>...</style> 块
        result = result.replaceAll("(?is)<style[^>]*>.*?</style>", " ");
        // 去掉所有 <...> 标签
        result = result.replaceAll("<[^>]*>", " ");
        // 把 HTML 实体还原为普通字符
        result = result.replace("&nbsp;", " ")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .replace("&quot;", "\"");
        return result;
    }

    static String normalize(String text) {
        if (text == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isLetterOrDigit(ch)) {
                builder.append(Character.toLowerCase(ch));
            }
        }
        return builder.toString();
    }

    static Map<String, Integer> buildNGrams(String text, int n) {
        Map<String, Integer> gramMap = new HashMap<>();

        if (text == null || text.isEmpty()) {
            return gramMap;
        }

        int gramSize = n <= 0 ? 1 : n;

        if (text.length() < gramSize) {
            gramMap.put(text, 1);
            return gramMap;
        }

        for (int i = 0; i <= text.length() - gramSize; i++) {
            String gram = text.substring(i, i + gramSize);
            gramMap.merge(gram, 1, Integer::sum);
        }

        return gramMap;
    }
}