package net.oleksin.service.impl;

import net.oleksin.converter.Converter;
import net.oleksin.model.CsvRow;
import net.oleksin.model.jsonmodel.ObservationsBySeriesData;
import net.oleksin.service.DataPreparationService;

import java.util.List;

public class CsvRowsDataPreparationService implements DataPreparationService<ObservationsBySeriesData> {
    private static final String FIRST_LINE = "Date,Series Name,Label,Description,Value";
    private final Converter<List<CsvRow>, String> csvRowConverter;
    private final Converter<ObservationsBySeriesData, List<CsvRow>> observationsBySeriesDataConverter;

    public CsvRowsDataPreparationService(Converter<List<CsvRow>, String> csvRowConverter,
                                         Converter<ObservationsBySeriesData, List<CsvRow>> observationsBySeriesDataConverter) {
        this.csvRowConverter = csvRowConverter;
        this.observationsBySeriesDataConverter = observationsBySeriesDataConverter;
    }

    @Override
    public String prepareData(ObservationsBySeriesData data) {
        final var csvRows = observationsBySeriesDataConverter.convert(data);
        if (!csvRows.isEmpty()) {
            return FIRST_LINE + csvRowConverter.convert(csvRows);
        }
        throw new IllegalArgumentException("Data is null");
    }
}
