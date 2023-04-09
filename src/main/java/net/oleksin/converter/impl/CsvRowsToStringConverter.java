package net.oleksin.converter.impl;

import net.oleksin.converter.Converter;
import net.oleksin.model.CsvRow;

import java.util.List;

public class CsvRowsToStringConverter implements Converter<List<CsvRow>, String> {
    private static final String COMMA_SEPARATOR = ",";

    @Override
    public String convert(List<CsvRow> csvRows) {
        if (csvRows == null) {
            throw new IllegalArgumentException("CsvRows is null");
        }
        final var stringBuilder = new StringBuilder();
        csvRows.forEach(csvRow -> csvRow.getDailyExchangeRate()
                .forEach((key, value) -> stringBuilder.append(System.lineSeparator())
                        .append(key).append(COMMA_SEPARATOR)
                        .append(csvRow.getSeriesName()).append(COMMA_SEPARATOR)
                        .append(csvRow.getLabel()).append(COMMA_SEPARATOR)
                        .append(csvRow.getDescription()).append(COMMA_SEPARATOR)
                        .append(value).append(COMMA_SEPARATOR)
                        .append(csvRow.getDailyChange().get(key))));
        return stringBuilder.toString();
    }
}
