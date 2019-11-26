package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.Formatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {
    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        if (text.isEmpty()) {
            return null;
        }
        return LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE.localizedBy(locale));
    }

    @Override
    public String print(LocalDate localDate, Locale locale) {
        return localDate.format(DateTimeFormatter.ISO_LOCAL_DATE.localizedBy(locale));
    }

//    protected DateFormat getDateFormat(Locale locale) {
//        DateFormat dateFormat = new SimpleDateFormat(String.valueOf(DateTimeFormatter.ISO_LOCAL_DATE), locale);
//        dateFormat.setLenient(false);
//        return dateFormat;
//    }
}
