package net.oleksin.converter.impl;

import net.oleksin.model.CsvRow;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ListToCsvRowsConverterTest {
    private static final String FIRST_LINE = "Date,Series Name,Label,Description,Value,Change";
    private static ListToCsvRowsConverter listToCsvRowsConverter;

    @BeforeAll
    static void beforeAll() {
        listToCsvRowsConverter = new ListToCsvRowsConverter();
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void convert_validData_ok() {
        final var now = LocalDate.now();
        final var test = "test";
        final var expected = List.of(CsvRow.builder()
                .label(test)
                .seriesName(test)
                .description(test)
                .dailyExchangeRate(Map.of(now, BigDecimal.TEN))
                .dailyChange(Map.of(now, BigDecimal.TEN))
                .build());
        final var strings = List.of(FIRST_LINE,
                now + ",test,test,test,10,10");
        final var actual = listToCsvRowsConverter.convert(strings);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.get(0).getDailyChange(), actual.get(0).getDailyChange());
        assertEquals(expected.get(0).getDailyExchangeRate(), actual.get(0).getDailyExchangeRate());
    }

    @Test
    void convert_listWithHeaders_ok() {
        final var expected = List.of();
        final var strings = List.of(FIRST_LINE);
        final var actual = listToCsvRowsConverter.convert(strings);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void convert_emptyList_ok() {
        final var expected = List.of();
        final List<String> strings = List.of();
        final var actual = listToCsvRowsConverter.convert(strings);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void convert_null_throwIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> listToCsvRowsConverter.convert(null));
    }
}