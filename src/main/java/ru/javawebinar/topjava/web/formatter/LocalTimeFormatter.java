package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalTimeFormatter implements Formatter<LocalTime> {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        if (text.isEmpty()) {
            return  null;
        }
        return LocalTime.parse(text);
    }

    @Override
    public String print(LocalTime localTime, Locale locale) {
        return localTime.format(TIME_FORMATTER);
    }

}
