package net.oleksin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public final class CommandLineArgs {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<String> seriesNames;
}
