package net.oleksin.service.impl;

import net.oleksin.model.CsvRow;
import net.oleksin.service.DataFusionService;

import java.util.List;

public class DataFusionServiceImpl implements DataFusionService<List<CsvRow>> {
    @Override
    public List<CsvRow> fuseData(List<CsvRow> firstData, List<CsvRow> secondData) {
        if (firstData == null && secondData == null) {
            throw new IllegalArgumentException("Cannot fuse data, cause both data is null");
        } else if (firstData == null) {
            return secondData;
        } else if (secondData == null) {
            return firstData;
        }
        for (final CsvRow csvRow : secondData) {
            if(firstData.contains(csvRow)) {
                final var indexOf = firstData.indexOf(csvRow);
                if (csvRow.getDailyExchangeRate() != null) {
                    firstData.get(indexOf).getDailyExchangeRate()
                            .putAll(csvRow.getDailyExchangeRate());
                }
                if (csvRow.getDailyChange() != null) {
                    firstData.get(indexOf).getDailyChange()
                            .putAll(csvRow.getDailyChange());
                }
            } else {
                firstData.add(csvRow);
            }
        }
        return firstData;
    }
}
