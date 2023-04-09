package net.oleksin.service;

import net.oleksin.model.CsvRow;
import net.oleksin.service.DataFusionService;

import java.util.List;
import java.util.Optional;

public class ApiAndFileDataFusionService implements DataFusionService<List<CsvRow>> {
    @Override
    public List<CsvRow> fuseData(List<CsvRow> firstData, List<CsvRow> secondData) {
        return isAnyDataNull(firstData, secondData)
                .orElseGet(() -> fuseDataIfBothDataIsNotNull(firstData, secondData));
    }

    private List<CsvRow> fuseDataIfBothDataIsNotNull(List<CsvRow> firstData, List<CsvRow> secondData) {
        for (final CsvRow csvRow : secondData) {
            if(firstData.contains(csvRow)) {
                putIfPresentByIndex(firstData, csvRow, firstData.indexOf(csvRow));
            } else {
                firstData.add(csvRow);
            }
        }
        return firstData;
    }

    private Optional<List<CsvRow>> isAnyDataNull(List<CsvRow> firstData, List<CsvRow> secondData) {
        if (firstData == null && secondData == null) {
            throw new IllegalArgumentException("Cannot fuse data, cause both data is null");
        } else if (firstData == null) {
            return Optional.of(secondData);
        } else if (secondData == null) {
            return Optional.of(firstData);
        }
        return Optional.empty();
    }

    private void putIfPresentByIndex(List<CsvRow> firstData, CsvRow csvRow, int indexOf) {
        if (csvRow.getDailyExchangeRate() != null) {
            firstData.get(indexOf).getDailyExchangeRate()
                    .putAll(csvRow.getDailyExchangeRate());
        }
        if (csvRow.getDailyChange() != null) {
            firstData.get(indexOf).getDailyChange()
                    .putAll(csvRow.getDailyChange());
        }
    }
}
