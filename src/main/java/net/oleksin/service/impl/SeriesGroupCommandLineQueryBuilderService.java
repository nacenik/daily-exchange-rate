package net.oleksin.service.impl;

import net.oleksin.model.CommandLineArgs;
import net.oleksin.parser.Parser;
import net.oleksin.service.QueryBuilderService;

public class SeriesGroupCommandLineQueryBuilderService implements QueryBuilderService {
    private static final String QUERY = "https://www.bankofcanada.ca/valet/observations/group/%s/json";
    private static final String START_DATE = "?start_date=%s";
    private static final String END_DATE = "&end_date=%s";
    private final Parser<String[], CommandLineArgs> commandLineArgsParser;

    public SeriesGroupCommandLineQueryBuilderService(Parser<String[], CommandLineArgs> commandLineArgsParser) {
        this.commandLineArgsParser = commandLineArgsParser;
    }
    @Override
    public String buildQuery(String[] args) {
        final var stringBuilder = new StringBuilder();
        final var commandLineArgs = commandLineArgsParser.parse(args);
        stringBuilder.append(String.format(QUERY, String.join("", commandLineArgs.getSeries())));
        setDate(stringBuilder, commandLineArgs);
        return stringBuilder.toString();
    }

    private static void setDate(StringBuilder stringBuilder, CommandLineArgs commandLineArgs) {
        if (commandLineArgs.getStartDate() != null) {
            stringBuilder.append(String.format(START_DATE,
                    commandLineArgs.getStartDate()));
        }
        if (commandLineArgs.getEndDate() != null) {
            stringBuilder.append(String.format(END_DATE,
                    commandLineArgs.getEndDate()));
        }
    }
}
