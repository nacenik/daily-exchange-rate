package net.oleksin.service;

import lombok.RequiredArgsConstructor;
import net.oleksin.converter.Converter;
import net.oleksin.model.CsvRow;
import net.oleksin.model.jsonmodel.ObservationsBySeriesData;
import net.oleksin.service.AggregatorService;
import net.oleksin.service.DataFusionService;

import java.util.List;

@RequiredArgsConstructor
public class ApiAndFileDataAggregatorService
        implements AggregatorService<List<String>, ObservationsBySeriesData, List<CsvRow>> {
    private final Converter<ObservationsBySeriesData, List<CsvRow>> observationsBySeriesDataConverter;
    private final Converter<List<String>, List<CsvRow>> csvRowConverter;
    private final DataFusionService<List<CsvRow>> fusionService;

    @Override
    public List<CsvRow> aggregate(List<String> strings, ObservationsBySeriesData observationsBySeriesData) {
        final var observationsBySeriesDataCsvRows
                = observationsBySeriesDataConverter.convert(observationsBySeriesData);
        if (strings == null || strings.size() <= 1) {
            return observationsBySeriesDataCsvRows;
        }
        final var csvRows = csvRowConverter.convert(strings);
        return fusionService.fuseData(csvRows, observationsBySeriesDataCsvRows);
    }
}
