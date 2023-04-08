package net.oleksin.converter.impl;

import net.oleksin.model.CsvRow;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvRowsToStringConverterTest {
    private static final String EXPECTED_SERIES_NUMBER = "FXCADUSD";
    private static final BigDecimal EXPECTED_RATE = BigDecimal.valueOf(1.35);
    private static final String EXPECTED_DESCRIPTION = "Canadian Dollar/US Dollar";
    private static final String EXPECTED_LABEL = "CAD/USD";
    private static final LocalDate EXPECTED_DATE = LocalDate.now();
    private static CsvRowsToStringConverter converter;
    private List<CsvRow> csvRows;

    @BeforeAll
    static void beforeAll() {
        converter = new CsvRowsToStringConverter();
    }

    @BeforeEach
    void setUp() {
        csvRows = new ArrayList<>();
        csvRows.add(new CsvRow(EXPECTED_SERIES_NUMBER, EXPECTED_DESCRIPTION,
                EXPECTED_LABEL, Map.of(EXPECTED_DATE, EXPECTED_RATE), Map.of()));
    }

    @Test
    void convert_csvRows_ok() {
        final var result = converter.convert(csvRows);
        assertTrue(result.startsWith(System.lineSeparator()));
        assertTrue(result.contains(EXPECTED_SERIES_NUMBER));
        assertTrue(result.contains(EXPECTED_DESCRIPTION));
        assertTrue(result.contains(EXPECTED_LABEL));
        assertTrue(result.contains(EXPECTED_RATE.toString()));
        assertTrue(result.contains(EXPECTED_DATE.toString()));
    }

    @Test
    void convert_csvRowsWithoutElements_ok() {
        csvRows.get(0).setDailyExchangeRate(Map.of());
        final var result = converter.convert(csvRows);
        assertEquals("", result);
    }

    @Test
    void convert_emptyCsvRows_ok() {
        final var result = converter.convert(List.of());
        assertEquals("", result);
    }

    @Test
    void convert_nullCsvRows_ok() {
       assertThrows(IllegalArgumentException.class, () -> converter.convert(null));
    }
}