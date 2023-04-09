package net.oleksin.service;

import net.oleksin.converter.Converter;
import net.oleksin.model.CsvRow;
import net.oleksin.model.jsonmodel.ObservationsBySeriesData;
import net.oleksin.service.ApiAndFileDataAggregatorService;
import net.oleksin.service.DataFusionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApiAndFileDataAggregatorServiceTest {
    private static ApiAndFileDataAggregatorService aggregatorService;
    private static Converter<ObservationsBySeriesData, List<CsvRow>> observationsBySeriesDataConverter;
    private static Converter<List<String>, List<CsvRow>> csvRowConverter;
    private static DataFusionService<List<CsvRow>> fusionService;

    @BeforeEach
    void setUp() {
        observationsBySeriesDataConverter = Mockito.mock(Converter.class);
        csvRowConverter = Mockito.mock(Converter.class);
        fusionService = Mockito.mock(DataFusionService.class);
        aggregatorService = new ApiAndFileDataAggregatorService(observationsBySeriesDataConverter,
                csvRowConverter, fusionService);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(observationsBySeriesDataConverter, csvRowConverter, fusionService);
    }

    @Test
    void aggregate_validInputData_ok() {
        Mockito.when(observationsBySeriesDataConverter.convert(Mockito.any()))
                .thenReturn(List.of(CsvRow.builder().build()));
        Mockito.when(csvRowConverter.convert(Mockito.any()))
                .thenReturn(List.of(CsvRow.builder().build()));
        Mockito.when(fusionService.fuseData(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(CsvRow.builder().build()));

        final var aggregate = aggregatorService.aggregate(List.of("test", "test"),
                Mockito.mock(ObservationsBySeriesData.class));

        Mockito.verify(observationsBySeriesDataConverter).convert(Mockito.any());
        Mockito.verify(csvRowConverter).convert(Mockito.any());
        Mockito.verify(fusionService).fuseData(Mockito.any(), Mockito.any());
        assertFalse(aggregate.isEmpty());
        assertEquals(1, aggregate.size());
    }

    @Test
    void aggregate_listWithOneParameter_ok() {
        Mockito.when(observationsBySeriesDataConverter.convert(Mockito.any()))
                .thenReturn(List.of(CsvRow.builder().build()));

        final var aggregate = aggregatorService.aggregate(List.of("test"),
                Mockito.mock(ObservationsBySeriesData.class));

        checkAggregateMethod(aggregate);
    }

    @Test
    void aggregate_emptyList_ok() {
        Mockito.when(observationsBySeriesDataConverter.convert(Mockito.any()))
                .thenReturn(List.of(CsvRow.builder().build()));

        final var aggregate = aggregatorService.aggregate(List.of(),
                Mockito.mock(ObservationsBySeriesData.class));

        checkAggregateMethod(aggregate);
    }

    @Test
    void aggregate_nullList_ok() {
        Mockito.when(observationsBySeriesDataConverter.convert(Mockito.any()))
                .thenReturn(List.of(CsvRow.builder().build()));

        final var aggregate = aggregatorService.aggregate(null,
                Mockito.mock(ObservationsBySeriesData.class));

        checkAggregateMethod(aggregate);
    }

    @Test
    void aggregate_nullObjectFromJson_throwIllegalArgumentException() {
        Mockito.when(observationsBySeriesDataConverter.convert(Mockito.any()))
                .thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> aggregatorService.aggregate(List.of("test", "test"), null));

        Mockito.verify(observationsBySeriesDataConverter).convert(Mockito.any());
        Mockito.verify(csvRowConverter, Mockito.never()).convert(Mockito.any());
        Mockito.verify(fusionService, Mockito.never()).fuseData(Mockito.any(), Mockito.any());
    }

    private static void checkAggregateMethod(List<CsvRow> aggregate) {
        Mockito.verify(observationsBySeriesDataConverter).convert(Mockito.any());
        Mockito.verify(csvRowConverter, Mockito.never()).convert(Mockito.any());
        Mockito.verify(fusionService, Mockito.never()).fuseData(Mockito.any(), Mockito.any());
        assertFalse(aggregate.isEmpty());
        assertEquals(1, aggregate.size());
    }
}