package net.oleksin.service.impl;

import net.oleksin.converter.Converter;
import net.oleksin.converter.impl.CsvRowsToStringConverter;
import net.oleksin.converter.impl.ObservationBySeriesDataToCsvRowsConverter;
import net.oleksin.model.CsvRow;
import net.oleksin.model.jsonmodel.ObservationsBySeriesData;
import net.oleksin.service.ChangeCounterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvRowsDataPreparationServiceTest {
    private static CsvRowsDataPreparationService service;
    private static Converter<List<CsvRow>, String> csvRowConverter;
    private static Converter<ObservationsBySeriesData, List<CsvRow>> observationsBySeriesDataConverter;
    private static ChangeCounterService changeCounterService;

    @BeforeAll
    static void beforeAll() {
        csvRowConverter = Mockito.mock(CsvRowsToStringConverter.class);
        observationsBySeriesDataConverter = Mockito.mock(ObservationBySeriesDataToCsvRowsConverter.class);
        changeCounterService = Mockito.mock(ChangeCounterService.class);
        service = new CsvRowsDataPreparationService(csvRowConverter, observationsBySeriesDataConverter, changeCounterService);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(csvRowConverter, observationsBySeriesDataConverter, changeCounterService);
    }

    @Test
    void prepareData_validInputData_ok() {
        final var expected = "Date,Series Name,Label,Description,Value,Change"
                + System.lineSeparator()
                + "test";
        Mockito.when(observationsBySeriesDataConverter.convert(Mockito.any()))
                .thenReturn(List.of(CsvRow.builder().build()));
        Mockito.when(csvRowConverter.convert(Mockito.any())).thenReturn(System.lineSeparator() + "test");

        final var actual = service.prepareData(Mockito.mock(ObservationsBySeriesData.class));

        Mockito.verify(observationsBySeriesDataConverter).convert(Mockito.any());
        Mockito.verify(csvRowConverter).convert(Mockito.any());
        assertEquals(expected, actual);
    }

    @Test
    void prepareData_emptyString_throwIllegalArgumentException() {
        Mockito.when(observationsBySeriesDataConverter.convert(Mockito.any()))
                .thenReturn(List.of());
        Mockito.when(csvRowConverter.convert(Mockito.any())).thenReturn("");

        assertThrows(IllegalArgumentException.class,
                () -> service.prepareData(Mockito.mock(ObservationsBySeriesData.class)));

        Mockito.verify(observationsBySeriesDataConverter).convert(Mockito.any());
        Mockito.verify(csvRowConverter, Mockito.never()).convert(Mockito.any());
    }
}