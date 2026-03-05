package com.nff.NextFirstFiltrex.util;

import java.time.*;
import java.time.temporal.WeekFields;

public record YearWeek(int year, int week) {

    private static final WeekFields ISO = WeekFields.ISO;

    public static YearWeek from(LocalDate date) {
        return new YearWeek(
            date.get(ISO.weekBasedYear()),
            date.get(ISO.weekOfWeekBasedYear())
        );
    }

    public LocalDate startDate() {
        return LocalDate
                .of(year, 1, 4)
                .with(ISO.weekOfWeekBasedYear(), week)
                .with(ISO.dayOfWeek(), 1);
    }

    public LocalDate endDate() {
        return startDate().plusDays(6);
    }

    @Override
    public String toString() {
        return year + "-W" + String.format("%02d", week);
    }
}
