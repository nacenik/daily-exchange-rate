package net.oleksin.service.impl;

import net.oleksin.converter.Converter;
import net.oleksin.model.CsvRow;
import net.oleksin.model.jsonmodel.ObservationsBySeriesData;
import net.oleksin.service.ChangeCounterService;
import net.oleksin.service.DataPreparationService;

import java.util.List;

public class ObservationsBySeriesDataPreparationService implements DataPreparationService<ObservationsBySeriesData> {
    private static final String FIRST_LINE = "Date,Series Name,Label,Description,Value,Change";
    private final Converter<List<CsvRow>, String> csvRowConverter;
    private final Converter<ObservationsBySeriesData, List<CsvRow>> observationsBySeriesDataConverter;
    private final ChangeCounterService changeCounterService;

    public ObservationsBySeriesDataPreparationService(Converter<List<CsvRow>, String> csvRowConverter,
                                                      Converter<ObservationsBySeriesData, List<CsvRow>> observationsBySeriesDataConverter,
                                                      ChangeCounterService changeCounterService) {
        this.csvRowConverter = csvRowConverter;
        this.observationsBySeriesDataConverter = observationsBySeriesDataConverter;
        this.changeCounterService = changeCounterService;
    }

    @Override
    public String prepareData(ObservationsBySeriesData data) {
        final var csvRows = observationsBySeriesDataConverter.convert(data);
        if (!csvRows.isEmpty()) {
            changeCounterService.countChange(csvRows);
            return FIRST_LINE + csvRowConverter.convert(csvRows);
        }
        throw new IllegalArgumentException("Cannot prepare data cause "
                + "ObservationsBySeriesData is null");
    }
}
