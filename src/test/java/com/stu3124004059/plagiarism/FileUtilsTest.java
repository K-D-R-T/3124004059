package com.stu3124004059.plagiarism;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileUtilsTest {

    @TempDir
    Path tempDir;

    @Test
    void readAndWriteText() throws IOException {
        Path file = tempDir.resolve("a.txt");
        FileUtils.writeText(file.toString(), "测试内容");
        assertEquals("测试内容", FileUtils.readText(file.toString()));
    }

    @Test
    void readNotExistShouldThrow() {
        assertThrows(IOException.class,
                () -> FileUtils.readText(tempDir.resolve("none.txt").toString()));
    }
}