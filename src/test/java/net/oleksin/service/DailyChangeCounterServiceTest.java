package net.oleksin.service;

import net.oleksin.model.CsvRow;
import net.oleksin.service.DailyChangeCounterService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DailyChangeCounterServiceTest {

    private static DailyChangeCounterService changeCounterService;

    @BeforeAll
    static void beforeAll() {
        changeCounterService = new DailyChangeCounterService();
    }

    @Test
    void countChange_validData_ok() {
        final var dailyExchange = Map.of(LocalDate.now(), BigDecimal.valueOf(1.2),
                LocalDate.now().plusDays(1), BigDecimal.valueOf(1.3),
                LocalDate.now().plusDays(2), BigDecimal.valueOf(1.4));
        final var csvRows = List.of(CsvRow.builder()
                .dailyExchangeRate(dailyExchange)
                .build());

        assertNull(csvRows.get(0).getDailyChange());
        changeCounterService.countChange(csvRows);
        assertNotNull(csvRows.get(0).getDailyChange());
        assertEquals(dailyExchange.size(), csvRows.get(0).getDailyChange().size());
        assertEquals(dailyExchange.keySet(), csvRows.get(0).getDailyChange().keySet());
        assertTrue(csvRows.get(0).getDailyChange().containsValue(BigDecimal.ZERO));
    }

    @Test
    void countChange_emptyData_ok() {
        final var csvRows = List.of(CsvRow.builder()
                .dailyExchangeRate(Map.of())
                .build());
        assertNull(csvRows.get(0).getDailyChange());
        changeCounterService.countChange(csvRows);
        assertNotNull(csvRows.get(0).getDailyChange());
        assertTrue(csvRows.get(0).getDailyChange().isEmpty());
    }
}