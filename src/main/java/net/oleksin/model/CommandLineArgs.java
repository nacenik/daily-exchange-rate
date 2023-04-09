package net.oleksin.model;

import java.time.LocalDate;
import java.util.List;

public record CommandLineArgs(LocalDate startDate,
                              LocalDate endDate,
                              List<String> series) {
}
