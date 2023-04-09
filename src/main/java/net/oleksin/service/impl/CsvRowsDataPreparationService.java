package net.oleksin.service.impl;

import net.oleksin.converter.Converter;
import net.oleksin.model.CsvRow;
import net.oleksin.model.jsonmodel.ObservationsBySeriesData;
import net.oleksin.service.ChangeCounterService;
import net.oleksin.service.DataPreparationService;

import java.util.List;

public class CsvRowsDataPreparationService implements DataPreparationService<List<CsvRow>> {
    private static final String FIRST_LINE = "Date,Series Name,Label,Description,Value,Change";
    private final Converter<List<CsvRow>, String> csvRowConverter;
    private final ChangeCounterService changeCounterService;

    public CsvRowsDataPreparationService(Converter<List<CsvRow>, String> csvRowConverter,
                                                      ChangeCounterService changeCounterService) {
        this.csvRowConverter = csvRowConverter;
        this.changeCounterService = changeCounterService;
    }

    @Override
    public String prepareData(List<CsvRow> data) {
        if (!data.isEmpty()) {
            changeCounterService.countChange(data);
            return FIRST_LINE + csvRowConverter.convert(data);
        }
        throw new IllegalArgumentException("Cannot prepare data cause "
                + "CsvRow is null");
    }
}
