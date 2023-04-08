package net.oleksin.service.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WriterServiceImplTest {
    private static final String OUTPUT_FILE_PATH = "src/test/resources/output.txt";
    private static WriterServiceImpl writerService;

    @BeforeAll
    static void beforeAll() {
        writerService = new WriterServiceImpl();
    }

    @AfterAll
    static void afterAll() {
        final var path = Path.of(OUTPUT_FILE_PATH);
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void writeToFile_validPath_ok() {
        writerService.writeToFile("test", OUTPUT_FILE_PATH);
        assertTrue(Files.exists(Path.of(OUTPUT_FILE_PATH)));
    }

    @Test
    void writeToFile_validPath_throwException() {
        final var validPath = "src/test/resources/";
        assertThrows(RuntimeException.class, () -> writerService.writeToFile("test", validPath));
        assertTrue(Files.isDirectory(Path.of(validPath)));
    }
}