package net.oleksin.service;

import net.oleksin.model.CommandLineArgs;
import net.oleksin.parser.Parser;
import net.oleksin.parser.SeriesGroupCommandLineArgsParser;
import net.oleksin.service.SeriesGroupCommandLineQueryBuilderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeriesGroupCommandLineQueryBuilderServiceTest {
    private static LocalDate now;
    private static final String FX_RATES_DAILY = "Fx_rates_daily";
    private static final String START_DATE = "start_date=";
    private static final String END_DATE = "end_date=";
    private static SeriesGroupCommandLineQueryBuilderService service;
    private static Parser<String[], CommandLineArgs> mockParser;

    @BeforeAll
    static void beforeAll() {
        mockParser = Mockito.mock(SeriesGroupCommandLineArgsParser.class);
        service = new SeriesGroupCommandLineQueryBuilderService(mockParser);
        now = LocalDate.now();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockParser);
    }

    @Test
    void buildQuery_withOneSeriesGroup_ok() {
        final var strings = new String[]{FX_RATES_DAILY};
        Mockito.when(mockParser.parse(strings))
                .thenReturn(new CommandLineArgs(null, null, List.of(strings[0])));

        final var query = service.buildQuery(strings);

        Mockito.verify(mockParser).parse(Mockito.any());
        assertTrue(query.contains(FX_RATES_DAILY));
        assertFalse(query.contains(START_DATE));
        assertFalse(query.contains(END_DATE));
    }

    @Test
    void buildQuery_withOneSeriesGroupAndStartDate_ok() {
        final var strings = new String[]{FX_RATES_DAILY, now.toString()};
        Mockito.when(mockParser.parse(strings))
                .thenReturn(new CommandLineArgs(now, null, List.of(strings[0])));

        final var query = service.buildQuery(strings);

        Mockito.verify(mockParser).parse(Mockito.any());
        assertTrue(query.contains(FX_RATES_DAILY));
        assertTrue(query.contains(START_DATE));
        assertTrue(query.contains(now.toString()));
        assertFalse(query.contains(END_DATE));
    }

    @Test
    void buildQuery_withOneSeriesGroupAndDates_ok() {
        final var endDate = now.plusDays(1);
        final var strings = new String[]{FX_RATES_DAILY, now.toString(), endDate.toString()};
        Mockito.when(mockParser.parse(strings))
                .thenReturn(new CommandLineArgs(now, endDate, List.of(strings[0])));

        final var query = service.buildQuery(strings);

        Mockito.verify(mockParser).parse(Mockito.any());
        assertTrue(query.contains(FX_RATES_DAILY));
        assertTrue(query.contains(START_DATE));
        assertTrue(query.contains(now.toString()));
        assertTrue(query.contains(END_DATE));
        assertTrue(query.contains(endDate.toString()));
    }
}