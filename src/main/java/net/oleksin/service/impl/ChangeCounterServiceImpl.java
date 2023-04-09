package net.oleksin.service.impl;

import net.oleksin.model.CsvRow;
import net.oleksin.service.ChangeCounterService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class ChangeCounterServiceImpl implements ChangeCounterService {
    private static final int SCALE = 6;
    private static final int PERCENT = 100;

    @Override
    public void countChange(List<CsvRow> csvRows) {
        csvRows.forEach(csvRow -> {
            if (csvRow.getDailyChange() == null) {
                csvRow.setDailyChange(new HashMap<>());
            }
            if (csvRow.getDailyExchangeRate().size() != csvRow.getDailyChange().size()) {
                final var descendingMap = new TreeMap<>(csvRow.getDailyExchangeRate())
                        .descendingMap();
                descendingMap.forEach((k, v) -> {
                    final var nextKey = descendingMap.higherKey(k);
                    if (nextKey != null) {
                        final var nextValue = descendingMap.get(nextKey);
                        final var changeInPercent = v.subtract(nextValue)
                                .divide(v, SCALE, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(PERCENT))
                                .stripTrailingZeros();
                        csvRow.getDailyChange().put(k, changeInPercent);
                    } else {
                        csvRow.getDailyChange().put(k, BigDecimal.ZERO);
                    }
                });
            }
        });
    }

}

