package net.oleksin.service;

import net.oleksin.model.CsvRow;
import net.oleksin.service.ApiAndFileDataFusionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApiAndFileDataFusionServiceTest {
    private static ApiAndFileDataFusionService dataFusionService;

    @BeforeAll
    static void beforeAll() {
        dataFusionService = new ApiAndFileDataFusionService();
    }

    @Test
    void fuseData_validDataWithoutChange_ok() {
        final var test = new TreeMap<LocalDate, BigDecimal>();
        final var actual = dataFusionService.fuseData(List.of(CsvRow.builder()
                        .dailyExchangeRate(test)
                        .build()),
                List.of(CsvRow.builder()
                        .dailyExchangeRate(test)
                        .build()));

        assertFalse(actual.isEmpty());
        assertEquals(1, actual.size());
    }

    @Test
    void fuseData_validDataWithChange_ok() {
        final var test = new TreeMap<LocalDate, BigDecimal>();
        final var actual = dataFusionService.fuseData(List.of(CsvRow.builder()
                        .dailyExchangeRate(test)
                        .dailyChange(test)
                        .build()),
                List.of(CsvRow.builder()
                        .dailyExchangeRate(test)
                        .dailyChange(test)
                        .build()));

        assertFalse(actual.isEmpty());
        assertEquals(1, actual.size());
    }

    @Test
    void fuseData_missData_ok() {
        final var test = new TreeMap<LocalDate, BigDecimal>();
        final var actual = dataFusionService.fuseData(List.of(CsvRow.builder()
                        .dailyExchangeRate(test)
                        .build()),
                List.of(CsvRow.builder()
                        .build()));

        assertFalse(actual.isEmpty());
        assertEquals(1, actual.size());
    }

    @Test
    void fuseData_firstDataIsNull_ok() {
        final var actual = dataFusionService.fuseData(null,
                List.of(CsvRow.builder().build()));

        assertFalse(actual.isEmpty());
        assertEquals(1, actual.size());
    }

    @Test
    void fuseData_differentRow_ok() {
        final var csvRows = new ArrayList<CsvRow>();
        csvRows.add(CsvRow.builder()
                .seriesName("test")
                .build());
        final var actual = dataFusionService.fuseData(csvRows,
                List.of(CsvRow.builder()
                        .seriesName("test2")
                        .build()));

        assertFalse(actual.isEmpty());
        assertEquals(2, actual.size());
    }

    @Test
    void fuseData_secondDataIsNull_ok() {
        final var actual = dataFusionService.fuseData(List.of(CsvRow.builder().build()),
                null);

        assertFalse(actual.isEmpty());
        assertEquals(1, actual.size());
    }

    @Test
    void fuseData_bothDataAreNull_throwIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> dataFusionService.fuseData(null, null));
    }
}