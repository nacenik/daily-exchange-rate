package net.oleksin.converter.impl;

import net.oleksin.model.jsonmodel.Observation;
import net.oleksin.model.jsonmodel.ObservationsBySeriesData;
import net.oleksin.model.jsonmodel.Rate;
import net.oleksin.model.jsonmodel.SeriesDetail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ObservationBySeriesDataToCsvRowsConverterTest {
    private static final String EXPECTED_SERIES_NUMBER = "FXCADUSD";
    private static final BigDecimal EXPECTED_RATE = BigDecimal.valueOf(1.35);
    private static final String EXPECTED_DESCRIPTION = "Canadian Dollar/US Dollar";
    private static final String EXPECTED_LABEL = "CAD/USD";
    private static ObservationBySeriesDataToCsvRowsConverter converter;
    private ObservationsBySeriesData observationsBySeriesData;
    private LocalDate expectedDate;

    @BeforeAll
    static void beforeAll() {
        converter = new ObservationBySeriesDataToCsvRowsConverter();
    }

    @BeforeEach
    void setUp() {
        expectedDate = LocalDate.now();
        observationsBySeriesData = ObservationsBySeriesData.builder()
                .seriesDetail(Map.of(EXPECTED_SERIES_NUMBER, SeriesDetail.builder()
                        .label(EXPECTED_LABEL)
                        .description(EXPECTED_DESCRIPTION)
                        .build()))
                .observations(List.of(Observation
                        .builder()
                        .date(expectedDate)
                        .value(Map.of(EXPECTED_SERIES_NUMBER, Rate.builder()
                                .currentRate(EXPECTED_RATE)
                                .build()))
                        .build()))
                .build();
    }

    @Test
    void convert_observationsBySeriesData_ok() {
        final var csvRows = converter.convert(observationsBySeriesData);
        assertEquals(1, csvRows.size());
        final var csvRow = csvRows.get(0);
        assertEquals(EXPECTED_SERIES_NUMBER, csvRow.getSeriesName());
        assertEquals(Map.of(expectedDate, EXPECTED_RATE), csvRow.getDailyExchangeRate());
        assertEquals(EXPECTED_DESCRIPTION, csvRow.getDescription());
        assertEquals(EXPECTED_LABEL, csvRow.getLabel());
    }

    @Test
    void convert_observationsBySeriesDataWithoutObservations_ok() {
        observationsBySeriesData.setObservations(List.of());
        final var csvRows = converter.convert(observationsBySeriesData);
        assertEquals(1, csvRows.size());
        final var csvRow = csvRows.get(0);
        assertEquals(EXPECTED_SERIES_NUMBER, csvRow.getSeriesName());
        assertEquals(EXPECTED_DESCRIPTION, csvRow.getDescription());
        assertEquals(EXPECTED_LABEL, csvRow.getLabel());
    }

    @Test
    void convert_emptyObservationsBySeriesData_ok() {
        observationsBySeriesData.setObservations(List.of());
        observationsBySeriesData.setSeriesDetail(Map.of());
        final var csvRows = converter.convert(observationsBySeriesData);
        assertEquals(0, csvRows.size());
    }

    @Test
    void convert_nullObservationsBySeriesData_throwIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert(null));
    }
}