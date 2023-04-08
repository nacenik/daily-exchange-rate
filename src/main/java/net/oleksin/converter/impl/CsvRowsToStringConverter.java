package net.oleksin.converter.impl;

import net.oleksin.converter.Converter;
import net.oleksin.model.CsvRow;

import java.util.List;

public class CsvRowsToStringConverter implements Converter<List<CsvRow>, String> {
    @Override
    public String convert(List<CsvRow> csvRows) {
        if (csvRows == null) {
            throw new IllegalArgumentException("CsvRows is null");
        }
        final var stringBuilder = new StringBuilder();
        csvRows.forEach(csvRow -> csvRow.getDailyExchangeRate()
                .forEach((key, value) -> {
                    stringBuilder.append(System.lineSeparator());
                    stringBuilder.append(key).append(",");
                    stringBuilder.append(csvRow.getSeriesName()).append(",");
                    stringBuilder.append(csvRow.getLabel()).append(",");
                    stringBuilder.append(csvRow.getDescription()).append(",");
                    stringBuilder.append(value).append(",");
                    stringBuilder.append(csvRow.getDailyChange().get(key));
                }));
        return stringBuilder.toString();
    }
}
