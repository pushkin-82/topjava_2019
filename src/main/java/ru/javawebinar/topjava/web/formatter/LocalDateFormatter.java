package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        if (text.isEmpty()) {
            return null;
        }
        return LocalDate.parse(text);
    }

    @Override
    public String print(LocalDate localDate, Locale locale) {
        return localDate.format(DATE_FORMATTER);
    }
}
