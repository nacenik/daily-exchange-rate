package net.oleksin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class CsvRow {
    private String seriesName;
    private String label;
    private String description;
    private Map<LocalDate, BigDecimal> dailyExchangeRate;
    private Map<LocalDate, BigDecimal> dailyChange;
}
