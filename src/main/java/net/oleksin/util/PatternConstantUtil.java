package net.oleksin.util;

import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class PatternConstantUtil {
    public static final Pattern SERIAL_GROUP_FIRST_PATTERN
            = Pattern.compile("^(?i)fx_(?i)rates_(?i)(daily|monthly)$");
    public static final Pattern SERIAL_GROUP_SECOND_PATTERN
            = Pattern.compile("^(?i)[a-z]{3}-\\d{4}-\\d$");
    public static final Pattern SERIAL_NAME_PATTERN = Pattern.compile("^(?i)fx[a-z]{6}$");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
}
