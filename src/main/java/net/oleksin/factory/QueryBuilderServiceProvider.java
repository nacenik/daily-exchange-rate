package net.oleksin.factory;

import java.util.Arrays;

import static net.oleksin.util.PatternConstantUtil.SERIAL_GROUP_FIRST_PATTERN;
import static net.oleksin.util.PatternConstantUtil.SERIAL_GROUP_SECOND_PATTERN;
import static net.oleksin.util.PatternConstantUtil.SERIAL_NAME_PATTERN;

public final class QueryBuilderServiceProvider {
    public QueryBuilderServiceFactory getQueryBuilderServiceFactory(String[] args) {
        if (isSerialGroup(args)) {
            return new SeriesGroupQueryBuilderServiceFactory();
        } else if (isSerialNames(args)) {
            return new SeriesNameQueryBuilderServiceFactory();
        } else {
            throw new IllegalArgumentException("Command line arguments are not valid." +
                    " Please, check your input.");
        }
    }
    private boolean isSerialGroup(String... args) {
        return Arrays.stream(args)
                .anyMatch(arg -> SERIAL_GROUP_FIRST_PATTERN.matcher(arg).matches()
                        || SERIAL_GROUP_SECOND_PATTERN.matcher(arg).matches())
                && Arrays.stream(args).noneMatch(arg ->
                SERIAL_NAME_PATTERN.matcher(arg).matches());
    }

    private boolean isSerialNames(String... args) {
        return (Arrays.stream(args)
                .anyMatch(arg -> SERIAL_NAME_PATTERN.matcher(arg).matches())
                || Arrays.stream(args).noneMatch(arg ->
                SERIAL_NAME_PATTERN.matcher(arg).matches()))
                && Arrays.stream(args).noneMatch(arg ->
                SERIAL_GROUP_FIRST_PATTERN.matcher(arg).matches()
                || SERIAL_GROUP_SECOND_PATTERN.matcher(arg).matches());
    }
}
