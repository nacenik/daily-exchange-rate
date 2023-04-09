package net.oleksin.service;

import net.oleksin.service.FileReaderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileReaderServiceTest {
    private static FileReaderService readerService;

    @BeforeAll
    static void beforeAll() {
        readerService = new FileReaderService();
    }

    @Test
    void readFromFile_validPath_ok() {
        final var actual = readerService.readFromFile("src/test/resources/test.json");

        assertNotNull(actual);
        assertEquals(61, actual.size());
    }

    @Test
    void readFromFile_directoryPath_emptyData() {
        final var actual = readerService.readFromFile("src/test/resources/empty");

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    void readFromFile_directoryPath_throwIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                readerService.readFromFile("src/test/resources/"));
    }
}