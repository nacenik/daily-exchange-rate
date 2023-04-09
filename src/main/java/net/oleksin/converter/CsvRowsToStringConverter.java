package net.oleksin.converter;

import net.oleksin.converter.Converter;
import net.oleksin.model.CsvRow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CsvRowsToStringConverter implements Converter<List<CsvRow>, String> {
    private static final String COMMA_SEPARATOR = ",";

    @Override
    public String convert(List<CsvRow> csvRows) {
        isNull(csvRows);
        final var stringBuilder = new StringBuilder();
        csvRows.forEach(csvRow -> csvRow.getDailyExchangeRate()
                .forEach((key, value) -> appendAllData(stringBuilder, csvRow, key, value)));
        return stringBuilder.toString();
    }

    private StringBuilder appendAllData(StringBuilder stringBuilder, CsvRow csvRow, LocalDate key, BigDecimal value) {
        return stringBuilder.append(System.lineSeparator())
                .append(key).append(COMMA_SEPARATOR)
                .append(csvRow.getSeriesName()).append(COMMA_SEPARATOR)
                .append(csvRow.getLabel()).append(COMMA_SEPARATOR)
                .append(csvRow.getDescription()).append(COMMA_SEPARATOR)
                .append(value).append(COMMA_SEPARATOR)
                .append(csvRow.getDailyChange().get(key));
    }

    private void isNull(List<CsvRow> csvRows) {
        if (csvRows == null) {
            throw new IllegalArgumentException("Cannot convert to string cause csvRows variable is null");
        }
    }
}
