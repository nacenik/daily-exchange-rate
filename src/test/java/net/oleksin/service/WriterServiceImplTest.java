package net.oleksin.service;

import net.oleksin.service.FileWriterService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WriterServiceImplTest {
    private static final String OUTPUT_FILE_PATH = "src/test/resources/output.txt";
    private static FileWriterService writerService;

    @BeforeAll
    static void beforeAll() {
        writerService = new FileWriterService();
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
    void writeToFile_validPath_throwIllegalArgumentException() {
        final var validPath = "src/test/resources/";
        assertThrows(IllegalArgumentException.class, () ->
                writerService.writeToFile("test", validPath));
    }

    @Test
    void writeToFile_validPathNullData_throwRuntimeException() {
        assertThrows(RuntimeException.class, () ->
                writerService.writeToFile(null, OUTPUT_FILE_PATH));
    }
}