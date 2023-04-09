package net.oleksin.factory;

import net.oleksin.service.impl.SeriesNameCommandLineQueryBuilderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeriesNameQueryBuilderServiceFactoryTest {
    private static SeriesNameQueryBuilderServiceFactory seriesNameQueryBuilderServiceFactory;

    @BeforeAll
    static void beforeAll() {
        seriesNameQueryBuilderServiceFactory = new SeriesNameQueryBuilderServiceFactory();
    }

    @Test
    void getCommandLineBuilder() {
        final var commandLineBuilder = seriesNameQueryBuilderServiceFactory.getCommandLineBuilder(
                new String[]{});
        assertNotNull(commandLineBuilder);
        assertEquals(SeriesNameCommandLineQueryBuilderService.class, commandLineBuilder.getClass());
    }
}