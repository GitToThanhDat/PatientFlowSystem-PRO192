package util;

import exception.InvalidInputException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;

public final class DateUtils {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_PARSER =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private DateUtils() {
    }

    public static String format(LocalDateTime time) {
        return time == null ? "-" : time.format(DATE_TIME_FORMAT);
    }

    public static String format(LocalDate date) {
        return date == null ? "-" : date.format(DATE_FORMAT);
    }

    public static LocalDate parseDate(String text) throws InvalidInputException {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(text.trim(), DATE_PARSER);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Invalid date '" + text.trim()
                    + "'. Please use dd/MM/yyyy, for example 25/12/1990.");
        }
    }

    public static String toStorage(LocalDate date) {
        return date == null ? "" : date.toString();
    }

    public static String toStorage(LocalDateTime time) {
        return time == null ? "" : time.truncatedTo(ChronoUnit.SECONDS).toString();
    }

    public static LocalDate dateFromStorage(String text) {
        return (text == null || text.isEmpty()) ? null : LocalDate.parse(text);
    }

    public static LocalDateTime timeFromStorage(String text) {
        return (text == null || text.isEmpty()) ? null : LocalDateTime.parse(text);
    }
}
