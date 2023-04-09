package net.oleksin.parser;

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

class SeriesGroupCommandLineArgsParserTest {
    private static LocalDate now;
    private static SeriesGroupCommandLineArgsParser seriesGroupCommandLineArgsParser;
    private List<String> listWithTestData;

    @BeforeAll
    static void beforeAll() {
        seriesGroupCommandLineArgsParser = new SeriesGroupCommandLineArgsParser();
        now = LocalDate.now();
    }

    @BeforeEach
    void setUp() {
        listWithTestData = new ArrayList<>(List.of("Fx_rates_daily"));
    }

    @Test
    void parse_cmdArgsWithOneSeriesGroupAndWithoutDates_ok() {
        final var expected = new CommandLineArgs(null, null,
                listWithTestData);
        String[] args = listWithTestData.toArray(String[]::new);
        final var commandLineArgs = assertDoesNotThrow(() -> seriesGroupCommandLineArgsParser.parse(args));
        assertEquals(expected, commandLineArgs);
    }

    @Test
    void parse_cmdArgsWitOneDateAndOneSeriesGroup_ok() {
        listWithTestData.add(now.toString());
        final var expected = new CommandLineArgs(now, null,
                listWithTestData.subList(0, listWithTestData.size() - 1));
        String[] args = listWithTestData.toArray(String[]::new);
        final var commandLineArgs = assertDoesNotThrow(() -> seriesGroupCommandLineArgsParser.parse(args));
        assertEquals(expected, commandLineArgs);
    }

    @Test
    void parse_cmdArgsWithTwoDatesAndOneSeriesGroup_ok() {
        listWithTestData.add(now.toString());
        listWithTestData.add(now.toString());
        final var expected = new CommandLineArgs(now, now,
                listWithTestData.subList(0, listWithTestData.size() - 2));
        String[] args = listWithTestData.toArray(String[]::new);
        final var commandLineArgs = assertDoesNotThrow(() -> seriesGroupCommandLineArgsParser.parse(args));
        assertEquals(expected, commandLineArgs);
    }

    @Test
    void parse_cmdArgsWithTwoDatesAndOneSeriesGroupMixedOrder_ok() {
        listWithTestData.add(0, now.toString());
        listWithTestData.add(now.toString());
        final var expected = new CommandLineArgs(now, now,
                listWithTestData.subList(1, listWithTestData.size() - 1));
        String[] args = listWithTestData.toArray(String[]::new);
        final var commandLineArgs = assertDoesNotThrow(() -> seriesGroupCommandLineArgsParser.parse(args));
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
        final var commandLineArgs = assertDoesNotThrow(() -> seriesGroupCommandLineArgsParser.parse(args));
        assertEquals(expected, commandLineArgs);
    }

    @Test
    void parse_sevenArgs_throwIllegalArgumentException() {
        String[] args = {"-s", "USD", "-b", "PLN", "-f", "2019-01-01", "-t", "2019-01-01", "-o", "csv"};
        assertThrows(IllegalArgumentException.class, () -> seriesGroupCommandLineArgsParser.parse(args));
    }

    @Test
    void parse_threeDates_throwParserException() {
        String[] args = {"2019-01-01", "2019-01-01", "2019-01-01"};
        assertThrows(ParserException.class, () -> seriesGroupCommandLineArgsParser.parse(args));
    }

    @Test
    void parse_twoSerialGroups_throwParserException() {
        String[] args = {"Fx_rates_daily", "Fx_rates_daily"};
        assertThrows(ParserException.class, () -> seriesGroupCommandLineArgsParser.parse(args));
    }

    @Test
    void parse_dateIllegalFormat_throwParserException() {
        assertThrows(ParserException.class, () -> seriesGroupCommandLineArgsParser.parse(new String[]{"2019.01.01"}));
    }

    @Test
    void parse_serialGroupIllegalFormatRate_throwParserException() {
        assertThrows(ParserException.class, () -> seriesGroupCommandLineArgsParser.parse(new String[]{"fx_dasda_daily"}));
    }

    @Test
    void parse_serialGroupIllegalFormat_throwParserException() {
        assertThrows(ParserException.class, () -> seriesGroupCommandLineArgsParser.parse(new String[]{"sds_20002_8"}));
    }
}