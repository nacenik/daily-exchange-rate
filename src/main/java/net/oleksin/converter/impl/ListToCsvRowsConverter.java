package net.oleksin.converter.impl;

import net.oleksin.converter.Converter;
import net.oleksin.model.CsvRow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ListToCsvRowsConverter implements Converter<List<String>, List<CsvRow>> {
    private static final int DATE_INDEX = 0;
    private static final int SERIAL_NAME_INDEX = 1;
    private static final int LABEL_INDEX = 2;
    private static final int DESCRIPTION_INDEX = 3;
    private static final int DAILY_EXCHANGE_RATE_INDEX = 4;
    private static final int DAILY_CHANGE_INDEX = 5;


    @Override
    public List<CsvRow> convert(List<String> strings) {
        if (strings == null) {
            throw new IllegalArgumentException("Strings from csv file is null");
        }
        final var collect = strings.stream()
                .skip(1)
                .map(s -> s.split(","))
                .toList();
        var dailyExchangeRate =
                new TreeMap<LocalDate, BigDecimal>(Comparator.reverseOrder());
        var dailyChange =
                new TreeMap<LocalDate, BigDecimal>(Comparator.reverseOrder());
        List<CsvRow> csvRows = new ArrayList<>();
        var row = new CsvRow();
        for (String[] values : collect) {
            if (values.length == 6) {
                if (row.getSeriesName() == null || !row.getSeriesName().equals(values[SERIAL_NAME_INDEX])) {
                    row = new CsvRow();
                    dailyExchangeRate = new TreeMap<>(Comparator.reverseOrder());
                    dailyChange = new TreeMap<>(Comparator.reverseOrder());
                    row.setSeriesName(values[SERIAL_NAME_INDEX]);
                    row.setLabel(values[LABEL_INDEX]);
                    row.setDescription(values[DESCRIPTION_INDEX]);
                    csvRows.add(row);
                    row.setDailyExchangeRate(dailyExchangeRate);
                    row.setDailyChange(dailyChange);
                }
                dailyExchangeRate.put(getDate(values), getBigDecimal(values, DAILY_EXCHANGE_RATE_INDEX));
                dailyChange.put(getDate(values), getBigDecimal(values, DAILY_CHANGE_INDEX));
            }

        }
        return csvRows;
    }

    private LocalDate getDate(String[] strings) {
        return LocalDate.parse(strings[DATE_INDEX]);
    }

    private BigDecimal getBigDecimal(String[] strings, int index) {
        return new BigDecimal(strings[index]);
    }
}
