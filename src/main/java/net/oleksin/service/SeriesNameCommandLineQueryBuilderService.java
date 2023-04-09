package net.oleksin.service;

import lombok.RequiredArgsConstructor;
import net.oleksin.model.CommandLineArgs;
import net.oleksin.parser.Parser;
import net.oleksin.service.QueryBuilderService;

import java.time.LocalDate;

@RequiredArgsConstructor
public class SeriesNameCommandLineQueryBuilderService implements QueryBuilderService {
    private static final String QUERY = "https://www.bankofcanada.ca/valet/observations/%s/json";
    private static final String PARAMS = "?start_date=%s&end_date=%s";
    private static final String DEFAULT_SERIAL_NUMBER = "FXCADUSD,FXAUDCAD";
    private static final int DEFAULT_WEEKS_TO_SUBTRACT = 1;
    private final Parser<String[], CommandLineArgs> commandLineArgsParser;

    @Override
    public String buildQuery(String[] args) {
        final var stringBuilder = new StringBuilder();
        final var commandLineArgs = commandLineArgsParser.parse(args);
        stringBuilder.append(String.format(QUERY, commandLineArgs.series().isEmpty()
                ? DEFAULT_SERIAL_NUMBER
                : String.join(",", commandLineArgs.series())));
        final var now = LocalDate.now();
        setDate(stringBuilder, commandLineArgs, now);
        return stringBuilder.toString();
    }

    private static void setDate(StringBuilder stringBuilder, CommandLineArgs commandLineArgs, LocalDate now) {
        if (commandLineArgs.startDate() == null) {
            stringBuilder.append(String.format(PARAMS,
                    now.minusWeeks(DEFAULT_WEEKS_TO_SUBTRACT), now));
        } else if (commandLineArgs.endDate() == null) {
            stringBuilder.append(commandLineArgs.startDate().isAfter(now)
                    ? String.format(PARAMS, now.minusWeeks(DEFAULT_WEEKS_TO_SUBTRACT), commandLineArgs.startDate())
                    : String.format(PARAMS, commandLineArgs.startDate(), now));
        } else {
            stringBuilder.append(String.format(PARAMS,
                    commandLineArgs.startDate(), commandLineArgs.endDate()));
        }
    }
}
