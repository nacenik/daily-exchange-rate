package net.oleksin.service;

import lombok.RequiredArgsConstructor;
import net.oleksin.converter.Converter;
import net.oleksin.model.CsvRow;
import net.oleksin.service.ChangeCounterService;
import net.oleksin.service.DataPreparationService;

import java.util.List;

@RequiredArgsConstructor
public class CsvRowsDataPreparationService implements DataPreparationService<List<CsvRow>> {
    private static final String FIRST_LINE = "Date,Series Name,Label,Description,Value,Change";
    private final Converter<List<CsvRow>, String> csvRowConverter;
    private final ChangeCounterService changeCounterService;

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
