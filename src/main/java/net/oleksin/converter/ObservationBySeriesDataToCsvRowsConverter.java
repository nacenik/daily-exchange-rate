package net.oleksin.converter;

import net.oleksin.converter.Converter;
import net.oleksin.model.CsvRow;
import net.oleksin.model.jsonmodel.Observation;
import net.oleksin.model.jsonmodel.ObservationsBySeriesData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ObservationBySeriesDataToCsvRowsConverter implements Converter<ObservationsBySeriesData, List<CsvRow>> {
    @Override
    public List<CsvRow> convert(ObservationsBySeriesData observationsBySeriesData) {
        isNull(observationsBySeriesData);
        final var csvRowModels = new ArrayList<CsvRow>();
        observationsBySeriesData.getSeriesDetail()
                .forEach((k,v) -> {
                    final var treeMap =
                            new TreeMap<LocalDate, BigDecimal>(Comparator.reverseOrder());
                    treeMap.putAll(groupDataByDate(observationsBySeriesData, k));
                    csvRowModels.add(new CsvRow(k, v.getLabel(), v.getDescription(), treeMap, null));
                });
        return csvRowModels;
    }

    private Map<LocalDate, BigDecimal> groupDataByDate(ObservationsBySeriesData observationsBySeriesData, String k) {
        return observationsBySeriesData.getObservations()
                .stream()
                .collect(Collectors.filtering(
                        observation -> observation.getValue().containsKey(k),
                        Collectors.toMap(
                                Observation::getDate,
                                value -> value.getValue().get(k).getCurrentRate()
                        )
                ));
    }

    private void isNull(ObservationsBySeriesData observationsBySeriesData) {
        if (observationsBySeriesData == null) {
            throw new IllegalArgumentException("ObservationsBySeriesData is null");
        }
    }
}
