package net.oleksin.factory;

import net.oleksin.service.impl.SeriesGroupCommandLineQueryBuilderService;
import net.oleksin.service.impl.SeriesNameCommandLineQueryBuilderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeriesGroupQueryBuilderServiceFactoryTest {
    private static SeriesGroupQueryBuilderServiceFactory seriesGroupQueryBuilderServiceFactory;

    @BeforeAll
    static void beforeAll() {
        seriesGroupQueryBuilderServiceFactory = new SeriesGroupQueryBuilderServiceFactory();
    }

    @Test
    void getCommandLineBuilder_validData_ok() {
        final var commandLineBuilder = seriesGroupQueryBuilderServiceFactory.getCommandLineBuilder(
                new String[]{});
        assertNotNull(commandLineBuilder);
        assertEquals(SeriesGroupCommandLineQueryBuilderService.class, commandLineBuilder.getClass());
    }
}