package net.oleksin.parser;

import net.oleksin.exception.ParserException;
import net.oleksin.model.CommandLineArgs;
import net.oleksin.parser.Parser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

import static net.oleksin.util.PatternConstantUtil.DATE_PATTERN;
import static net.oleksin.util.PatternConstantUtil.DATE_TIME_FORMATTER;
import static net.oleksin.util.PatternConstantUtil.SERIAL_NAME_PATTERN;

public class SeriesNameCommandLineArgsParser implements Parser<String[], CommandLineArgs> {
    private static final int MAX_SERIAL_NUMBER_SIZE = 4;
    private static final int MAX_DATES_SIZE = 2;

    @Override
    public CommandLineArgs parse(String[] args) {
        if (args.length > MAX_DATES_SIZE + MAX_SERIAL_NUMBER_SIZE) {
            throw new IllegalArgumentException(String.format("Too many arguments. Max %d arguments",
                    MAX_DATES_SIZE + MAX_SERIAL_NUMBER_SIZE));
        }
        final var dates = new ArrayList<LocalDate>();
        final var serialNames = new LinkedList<String>();
        for (String arg : args) {
            if(SERIAL_NAME_PATTERN.matcher(arg).matches()) {
                serialNames.add(arg);
            } else if (DATE_PATTERN.matcher(arg).matches()) {
                dates.add(LocalDate.parse(arg, DATE_TIME_FORMATTER));
            } else {
                throw new ParserException(String.format("Argument: \"%s\" is not correct. "
                        + "Date format: \"yyyy-MM-dd\""
                        + " or serial names format: \"FXxxxxxx\"", arg));
            }
        }
        if (dates.size() > MAX_DATES_SIZE) {
            throw new ParserException(String.format("Too many dates. Max %d dates",
                    MAX_DATES_SIZE));
        }
        if (serialNames.size() > MAX_SERIAL_NUMBER_SIZE) {
            throw new ParserException(String.format("Too many serial numbers. Max %d serial numbers",
                    MAX_SERIAL_NUMBER_SIZE));
        }
        dates.sort(Comparator.naturalOrder());
        final var firstDate = dates.isEmpty() ? null : dates.get(0);
        final var secondDate = dates.size() < 2 ? null : dates.get(1);
        return new CommandLineArgs(firstDate, secondDate, serialNames);
    }
}
