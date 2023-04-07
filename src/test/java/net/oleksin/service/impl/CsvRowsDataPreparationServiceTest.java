package net.oleksin.service.impl;

import net.oleksin.converter.Converter;
import net.oleksin.converter.impl.CsvRowsToStringConverter;
import net.oleksin.converter.impl.ObservationBySeriesDataToCsvRowsConverter;
import net.oleksin.model.CsvRow;
import net.oleksin.model.jsonmodel.ObservationsBySeriesData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvRowsDataPreparationServiceTest {
    private static CsvRowsDataPreparationService service;
    private static Converter<List<CsvRow>, String> csvRowConverter;
    private static Converter<ObservationsBySeriesData, List<CsvRow>> observationsBySeriesDataConverter;

    @BeforeAll
    static void beforeAll() {
        csvRowConverter = Mockito.mock(CsvRowsToStringConverter.class);
        observationsBySeriesDataConverter = Mockito.mock(ObservationBySeriesDataToCsvRowsConverter.class);
        service = new CsvRowsDataPreparationService(csvRowConverter, observationsBySeriesDataConverter);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(csvRowConverter, observationsBySeriesDataConverter);
    }

    @Test
    void prepareData_validInputData_ok() {
        final var expected = "Date,Series Name,Label,Description,Value"
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