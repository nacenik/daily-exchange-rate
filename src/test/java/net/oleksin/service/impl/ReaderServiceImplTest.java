package net.oleksin.service.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReaderServiceImplTest {
    private static ReaderServiceImpl readerService;

    @BeforeAll
    static void beforeAll() {
        readerService = new ReaderServiceImpl();
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