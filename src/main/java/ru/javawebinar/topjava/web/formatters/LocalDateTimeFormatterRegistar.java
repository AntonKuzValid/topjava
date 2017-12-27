package ru.javawebinar.topjava.web.formatters;

import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

import static java.time.format.DateTimeFormatter.ofPattern;

public class LocalDateTimeFormatterRegistar implements FormatterRegistrar {
    @Override
    public void registerFormatters(FormatterRegistry registry) {
        registry.addFormatter(new LocalDateFormatter("yyyy-MM-dd"));
        registry.addFormatter(new LocalTimeFormatter("HH:mm"));
    }

    public static class LocalDateFormatter implements Formatter<LocalDate> {
        private String pattern;

        public LocalDateFormatter(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public LocalDate parse(String text, Locale locale) throws ParseException {
            return StringUtils.isEmpty(text) ? null : LocalDate.parse(text, ofPattern(pattern, locale));
        }

        @Override
        public String print(LocalDate date, Locale locale) {
            return date.format(ofPattern(pattern, locale));
        }
    }

    public static class LocalTimeFormatter implements Formatter<LocalTime> {
        private String pattern;

        public LocalTimeFormatter(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public LocalTime parse(String text, Locale locale) throws ParseException {
            return StringUtils.isEmpty(text) ? null : LocalTime.parse(text, ofPattern(pattern, locale));
        }

        @Override
        public String print(LocalTime time, Locale locale) {
            return time.format(ofPattern(pattern,locale));
        }
    }
}
