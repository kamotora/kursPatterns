package util;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {
    private int year = 2020;
    private int month = 12;
    private int day = 12;
    @Test
    void getYear() {
        Assert.assertEquals(year,DateUtil.getYear(Timestamp.valueOf(LocalDateTime.of(year,month,day,12,12))));
    }

    @Test
    void getMonth() {
        Assert.assertEquals(month,DateUtil.getMonth(Timestamp.valueOf(LocalDateTime.of(year,month,day,12,12))));
    }

    @Test
    void getDay() {
        Assert.assertEquals(day,DateUtil.getDay(Timestamp.valueOf(LocalDateTime.of(year,month,day,12,12))));
    }
}