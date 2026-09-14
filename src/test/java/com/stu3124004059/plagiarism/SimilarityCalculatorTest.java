package com.stu3124004059.plagiarism;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimilarityCalculatorTest {

    @Test
    void identicalTextShouldReturnOneHundred() {
        assertEquals(100.00,
                SimilarityCalculator.cosineSimilarity("今天是星期天", "今天是星期天"),
                0.01);
    }

    @Test
    void completelyDifferentShouldReturnZero() {
        assertEquals(0.00,
                SimilarityCalculator.cosineSimilarity("abc", "xyz"),
                0.01);
    }

    @Test
    void sampleShouldBeReasonable() {
        double similarity = SimilarityCalculator.cosineSimilarity(
                "今天是星期天，天气晴，今天晚上我要去看电影。",
                "今天是周天，天气晴朗，我晚上要去看电影。"
        );
        assertTrue(similarity > 50 && similarity < 70, "样例相似度应在合理范围，实际为 " + similarity);
    }

    @Test
    void emptyOriginalShouldReturnZero() {
        assertEquals(0.00,
                SimilarityCalculator.cosineSimilarity("", "abc"),
                0.01);
    }

    @Test
    void emptyCopyShouldReturnZero() {
        assertEquals(0.00,
                SimilarityCalculator.cosineSimilarity("abc", ""),
                0.01);
    }

    @Test
    void punctuationAndSpaceIgnored() {
        assertEquals(100.00,
                SimilarityCalculator.cosineSimilarity("今天，天气晴。", "今天 天气晴"),
                0.01);
    }

    @Test
    void englishCaseIgnored() {
        assertEquals(100.00,
                SimilarityCalculator.cosineSimilarity("Hello World", "hello world"),
                0.01);
    }

    @Test
    void singleCharSame() {
        assertEquals(100.00,
                SimilarityCalculator.cosineSimilarity("中", "中"),
                0.01);
    }

    @Test
    void singleCharDifferent() {
        assertEquals(0.00,
                SimilarityCalculator.cosineSimilarity("中", "文"),
                0.01);
    }

    @Test
    void partialAdditionShouldKeepHighSimilarity() {
        double similarity = SimilarityCalculator.cosineSimilarity(
                "今天天气很好",
                "今天天气很好我们一起出去玩"
        );
        assertTrue(similarity > 50, "部分增加后相似度应较高，实际为 " + similarity);
    }

    @Test
    void nullShouldBeTreatedAsEmpty() {
        assertEquals(0.00,
                SimilarityCalculator.cosineSimilarity(null, "abc"),
                0.01);
    }

    @Test
    void htmlTagsShouldBeStripped() {
        String html = "<html><head><title>测试</title></head><body><p>今天是星期天</p></body></html>";
        String plain = "今天是星期天";
        assertEquals(100.00,
                SimilarityCalculator.cosineSimilarity(html, plain),
                0.01);
    }
}