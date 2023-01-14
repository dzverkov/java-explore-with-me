package ru.practicum.server.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EwmDateTimeFormatter {

    private final String formatString = "yyyy-MM-dd HH:mm:ss";
    private DateTimeFormatter formatter;

    public EwmDateTimeFormatter() {
        formatter = DateTimeFormatter.ofPattern(formatString);
    }

    public LocalDateTime toDateTime(String dateTimeStr) {

        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    public String toString(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }
}
