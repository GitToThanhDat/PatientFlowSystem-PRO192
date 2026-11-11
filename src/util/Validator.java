package util;

import exception.InvalidInputException;
import java.time.LocalDate;

public final class Validator {

    private static final int MIN_YEAR = 1900;

    private Validator() {
    }

    public static String requireText(String value, String label) throws InvalidInputException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(label + " must not be empty.");
        }
        String text = value.trim();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 32 || c > 126) {
                throw new InvalidInputException(label + " must be typed without"
                        + " accents (letters a-z, digits and standard symbols only).");
            }
        }
        return text;
    }

    public static String requireName(String value, String label) throws InvalidInputException {
        String text = requireText(value, label);
        if (text.contains("?") || text.equals("0")) {
            throw new InvalidInputException(label + " must not contain '?' and must not be '0'.");
        }
        return text;
    }

    public static LocalDate requirePastDate(LocalDate date, String label) throws InvalidInputException {
        if (date == null) {
            throw new InvalidInputException(label + " is required.");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new InvalidInputException(label + " must not be in the future.");
        }
        if (date.getYear() < MIN_YEAR) {
            throw new InvalidInputException(label + " must not be before " + MIN_YEAR + ".");
        }
        return date;
    }
}
