package net.oleksin.service.impl;

import net.oleksin.model.CsvRow;
import net.oleksin.service.ChangeCounterService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class ChangeCounterServiceImpl implements ChangeCounterService {
    @Override
    public void countChange(List<CsvRow> csvRows) {
        csvRows.forEach(csvRow -> {
            csvRow.setDailyChange(new HashMap<>());
            final var descendingMap = new TreeMap<>(csvRow.getDailyExchangeRate())
                    .descendingMap();
            descendingMap.forEach((k, v) -> {
                final var nextKey = descendingMap.higherKey(k);
                if (nextKey != null) {
                    final var nextValue = descendingMap.get(nextKey);
                    final var changeInPercent = v.subtract(nextValue)
                            .divide(v, 6, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .stripTrailingZeros();
                    csvRow.getDailyChange().put(k, changeInPercent);
                } else {
                    csvRow.getDailyChange().put(k, BigDecimal.ZERO);
                }
            });
        });
    }

}

