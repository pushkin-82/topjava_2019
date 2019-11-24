package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class StringToLocalDateFormatterFactory implements AnnotationFormatterFactory<DateTimeFormat> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(asList(LocalDate.class, LocalTime.class));
    }

    @Override
    public Printer<LocalDate> getPrinter(DateTimeFormat annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    @Override
    public Parser<LocalDate> getParser(DateTimeFormat annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    private Formatter<LocalDate> configureFormatterFrom(DateTimeFormat annotation, Class<?> fieldType) {
        return null;
    }
}
