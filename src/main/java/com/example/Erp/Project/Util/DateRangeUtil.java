package com.example.Erp.Project.Util;

import java.time.DayOfWeek;
import java.time.LocalDate;

import com.example.Erp.Project.Entity.DashboardFilter;

public class DateRangeUtil {

    public static LocalDate[] resolve(DashboardFilter filter) {

        LocalDate now = LocalDate.now();

        switch (filter) {

            case TODAY:
                return new LocalDate[]{ now, now };

            case THIS_WEEK:
                return new LocalDate[]{
                        now.with(DayOfWeek.MONDAY),
                        now
                };

            case LAST_MONTH:
                LocalDate lastMonth = now.minusMonths(1);
                return new LocalDate[]{
                        lastMonth.withDayOfMonth(1),
                        lastMonth.withDayOfMonth(lastMonth.lengthOfMonth())
                };

            case THIS_MONTH:
            default:
                return new LocalDate[]{
                        now.withDayOfMonth(1),
                        now
                };
        }
    }
}
