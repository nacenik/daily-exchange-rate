package net.oleksin.parser.impl;

import net.oleksin.exception.ParserException;
import net.oleksin.model.CommandLineArgs;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandLineArgsParserTest {
    private static LocalDate now;
    private static CommandLineArgsParser commandLineArgsParser;
    private List<String> listWithTestData;

    @BeforeAll
    static void beforeAll() {
        commandLineArgsParser = new CommandLineArgsParser();
        now = LocalDate.now();
    }

    @BeforeEach
    void setUp() {
        listWithTestData = new ArrayList<>(List.of("FXCADUSD", "FXGBPNOK", "FXUSDCAD", "FXUSDEUR"));
    }

    @Test
    void parse_cmdArgsWithFourSeriesNamesAndWithoutDates_ok() {
        final var expected = new CommandLineArgs(null, null,
                listWithTestData);
        String[] args = listWithTestData.toArray(String[]::new);
        final var commandLineArgs = assertDoesNotThrow(() -> commandLineArgsParser.parse(args));
        assertEquals(expected, commandLineArgs);
    }

    @Test
    void parse_cmdArgsWitOneDateAndFourSeriesNames_ok() {
        listWithTestData.add(now.toString());
        final var expected = new CommandLineArgs(now, null,
                listWithTestData.subList(0, listWithTestData.size() - 1));
        String[] args = listWithTestData.toArray(String[]::new);
        final var commandLineArgs = assertDoesNotThrow(() -> commandLineArgsParser.parse(args));
        assertEquals(expected, commandLineArgs);
    }

    @Test
    void parse_cmdArgsWithTwoDatesAndFourSeriesNames_ok() {
        listWithTestData.add(now.toString());
        listWithTestData.add(now.toString());
        final var expected = new CommandLineArgs(now, now,
                listWithTestData.subList(0, listWithTestData.size() - 2));
        String[] args = listWithTestData.toArray(String[]::new);
        final var commandLineArgs = assertDoesNotThrow(() -> commandLineArgsParser.parse(args));
        assertEquals(expected, commandLineArgs);
    }

    @Test
    void parse_cmdArgsWithTwoDatesAndFourSeriesNamesMixedOrder_ok() {
        listWithTestData.add(0, now.toString());
        listWithTestData.add(now.toString());
        final var expected = new CommandLineArgs(now, now,
                listWithTestData.subList(1, listWithTestData.size() - 1));
        String[] args = listWithTestData.toArray(String[]::new);
        final var commandLineArgs = assertDoesNotThrow(() -> commandLineArgsParser.parse(args));
        assertEquals(expected, commandLineArgs);
    }

    @Test
    void parse_cmdArgsWithLowerCaseSeriesNumber_ok() {
        listWithTestData = listWithTestData.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        final var expected = new CommandLineArgs(null, null,
                listWithTestData);
        String[] args = listWithTestData.toArray(String[]::new);
        final var commandLineArgs = assertDoesNotThrow(() -> commandLineArgsParser.parse(args));
        assertEquals(expected, commandLineArgs);
    }

    @Test
    void parse_sevenArgs_throwIllegalArgumentException() {
        String[] args = {"-s", "USD", "-b", "PLN", "-f", "2019-01-01", "-t", "2019-01-01", "-o", "csv"};
        assertThrows(IllegalArgumentException.class, () -> commandLineArgsParser.parse(args));
    }

    @Test
    void parse_threeDates_throwParserException() {
        String[] args = {"2019-01-01", "2019-01-01", "2019-01-01"};
        assertThrows(ParserException.class, () -> commandLineArgsParser.parse(args));
    }

    @Test
    void parse_fiveSerialNumbers_throwParserException() {
        String[] args = {"FXCADUSD", "FXCADUSD", "FXCADUSD", "FXCADUSD", "FXCADUSD"};
        assertThrows(ParserException.class, () -> commandLineArgsParser.parse(args));
    }

    @Test
    void parse_dateIllegalFormat_throwParserException() {
        assertThrows(ParserException.class, () -> commandLineArgsParser.parse(new String[]{"2019.01.01"}));
    }

    @Test
    void parse_serialNameIllegalFormat_throwParserException() {
        assertThrows(ParserException.class, () -> commandLineArgsParser.parse(new String[]{"FX123ASD"}));
    }
}