package net.oleksin.service.impl;

import net.oleksin.model.CommandLineArgs;
import net.oleksin.parser.Parser;
import net.oleksin.parser.impl.CommandLineArgsParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandLineQueryBuilderServiceTest {

    private static final String FXCADUSD = "FXCADUSD";
    private static final String FXAUDCAD = "FXAUDCAD";
    private static final String START_DATE = "start_date=";
    private static final String END_DATE = "end_date=";
    private static LocalDate now;
    private static CommandLineQueryBuilderService service;
    private static Parser<String[], CommandLineArgs> mockParser;


    @BeforeAll
    static void beforeAll() {
        mockParser = Mockito.mock(CommandLineArgsParser.class);
        service = new CommandLineQueryBuilderService(mockParser);
        now = LocalDate.now();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockParser);
    }

    @Test
    void buildQuery_defaultQuery_ok() {
        final var strings = new String[]{};
        Mockito.when(mockParser.parse(strings))
                .thenReturn(CommandLineArgs.builder()
                        .seriesNames(List.of())
                        .build());

        final var query = service.buildQuery(strings);

        shouldVerifyParseMethodAndCheckDefaultSeriesNames(query);
        assertTrue(query.contains(now.toString()));
        assertTrue(query.contains(now.minusWeeks(1).toString()));
    }

    @Test
    void buildQuery_customSerialNumber_ok() {
        final var serialNumber = "FXUSDEUR";
        Mockito.when(mockParser.parse(Mockito.any()))
                .thenReturn(CommandLineArgs.builder()
                        .seriesNames(List.of(serialNumber))
                        .build());

        final var query = service.buildQuery(new String[]{serialNumber});

        Mockito.verify(mockParser).parse(Mockito.any());
        assertTrue(query.contains(now.toString()));
        assertTrue(query.contains(now.minusWeeks(1).toString()));
        assertTrue(query.contains(serialNumber));
    }

    @Test
    void buildQuery_customSerialNumbers_ok() {
        final var serialNumbers = List.of("FXAUDCAD", "FXUSDEUR", "fxcadeur");
        Mockito.when(mockParser.parse(Mockito.any()))
                .thenReturn(CommandLineArgs.builder()
                        .seriesNames(serialNumbers)
                        .build());

        final var query = service.buildQuery(serialNumbers
                .toArray(String[]::new));

        Mockito.verify(mockParser).parse(Mockito.any());
        assertTrue(query.contains(now.toString()));
        assertTrue(query.contains(now.minusWeeks(1).toString()));
        serialNumbers.forEach(serialNumber -> assertTrue(query.contains(serialNumber)));
    }


    @Test
    void buildQuery_oneStartDate_ok() {
        final var oneDate = List.of(now.toString());
        Mockito.when(mockParser.parse(Mockito.any()))
                .thenReturn(CommandLineArgs.builder()
                        .seriesNames(List.of())
                        .startDate(now)
                        .build());

        final var query = service.buildQuery(oneDate
                .toArray(String[]::new));

        shouldVerifyParseMethodAndCheckDefaultSeriesNames(query);
        oneDate.forEach(serialNumber -> assertTrue(query.contains(START_DATE + serialNumber)));
    }


    @Test
    void buildQuery_oneDateThatMoreThanCurrentDate_ok() {
        now = now.plusMonths(1);
        final var oneDate = List.of(now.toString());
        Mockito.when(mockParser.parse(Mockito.any()))
                .thenReturn(CommandLineArgs.builder()
                        .seriesNames(List.of())
                        .startDate(now)
                        .build());

        final var query = service.buildQuery(oneDate
                .toArray(String[]::new));

        shouldVerifyParseMethodAndCheckDefaultSeriesNames(query);
        oneDate.forEach(serialNumber -> assertTrue(query.contains(END_DATE + serialNumber)));
    }

    @Test
    void buildQuery_twoDates_ok() {
        LocalDate startDate = now.minusMonths(1);
        final var oneDate = List.of(now.toString(), startDate.toString());
        Mockito.when(mockParser.parse(Mockito.any()))
                .thenReturn(CommandLineArgs.builder()
                        .seriesNames(List.of())
                        .startDate(startDate)
                        .endDate(now)
                        .build());

        final var query = service.buildQuery(oneDate
                .toArray(String[]::new));

        shouldVerifyParseMethodAndCheckDefaultSeriesNames(query);
        assertTrue(query.contains(START_DATE + startDate));
        assertTrue(query.contains(END_DATE + now));
    }

    private static void shouldVerifyParseMethodAndCheckDefaultSeriesNames(String query) {
        Mockito.verify(mockParser).parse(Mockito.any());
        assertTrue(query.contains(FXAUDCAD));
        assertTrue(query.contains(FXCADUSD));
    }


}