package net.oleksin.converter.impl;

import net.oleksin.converter.Converter;
import net.oleksin.model.CsvRow;
import net.oleksin.model.jsonmodel.Observation;
import net.oleksin.model.jsonmodel.ObservationsBySeriesData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ObservationBySeriesDataToCsvRowsConverter implements Converter<ObservationsBySeriesData, List<CsvRow>> {

    @Override
    public List<CsvRow> convert(ObservationsBySeriesData observationsBySeriesData) {
        if (observationsBySeriesData == null) {
            throw new IllegalArgumentException("ObservationsBySeriesData is null");
        }
        final var csvRowModels = new ArrayList<CsvRow>();
        observationsBySeriesData.getSeriesDetail()
                .forEach((k,v) -> {
                    final var csvRowModel = new CsvRow();
                    csvRowModel.setSeriesName(k);
                    csvRowModel.setLabel(v.getLabel());
                    csvRowModel.setDescription(v.getDescription());
                    final var treeMap =
                            new TreeMap<LocalDate, BigDecimal>(Comparator.reverseOrder());
                    treeMap.putAll(
                            observationsBySeriesData.getObservations()
                            .stream()
                            .collect(
                                    Collectors.filtering(
                                            observation -> observation.getValue().containsKey(k),
                                            Collectors.toMap(
                                                    Observation::getDate,
                                                    value -> value.getValue().get(k).getCurrentRate()
                                            )
                                    )
                            )
                    );
                    csvRowModel.setElements(treeMap);
                    csvRowModels.add(csvRowModel);
                });
        return csvRowModels;
    }
}
