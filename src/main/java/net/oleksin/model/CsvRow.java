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
    private Map<LocalDate, BigDecimal> elements;

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<LocalDate, BigDecimal> getElements() {
        return elements;
    }

    public void setElements(Map<LocalDate, BigDecimal> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        return "CsvRowModel{" +
                "seriesName='" + seriesName + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", elements=" + elements +
                '}';
    }
}
