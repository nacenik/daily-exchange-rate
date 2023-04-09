package net.oleksin.service;

import net.oleksin.converter.Converter;
import net.oleksin.converter.CsvRowsToStringConverter;
import net.oleksin.model.CsvRow;
import net.oleksin.service.ChangeCounterService;
import net.oleksin.service.CsvRowsDataPreparationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvRowsDataPreparationServiceTest {
    private static CsvRowsDataPreparationService service;
    private static Converter<List<CsvRow>, String> csvRowConverter;
    private static ChangeCounterService changeCounterService;

    @BeforeAll
    static void beforeAll() {
        csvRowConverter = Mockito.mock(CsvRowsToStringConverter.class);
        changeCounterService = Mockito.mock(ChangeCounterService.class);
        service = new CsvRowsDataPreparationService(csvRowConverter, changeCounterService);
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(csvRowConverter, changeCounterService);
    }

    @Test
    void prepareData_validInputData_ok() {
        final var expected = "Date,Series Name,Label,Description,Value,Change"
                + System.lineSeparator()
                + "test";
        Mockito.when(csvRowConverter.convert(Mockito.any())).thenReturn(System.lineSeparator() + "test");

        final var actual = service.prepareData(List.of(CsvRow.builder()
                .label("test").build()));

        Mockito.verify(csvRowConverter).convert(Mockito.any());
        assertEquals(expected, actual);
    }

    @Test
    void prepareData_emptyString_throwIllegalArgumentException() {
        Mockito.when(csvRowConverter.convert(Mockito.any())).thenReturn("");

        assertThrows(IllegalArgumentException.class,
                () -> service.prepareData(List.of()));

        Mockito.verify(csvRowConverter, Mockito.never()).convert(Mockito.any());
    }
}