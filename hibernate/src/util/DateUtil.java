package util;

import java.sql.Timestamp;

public class DateUtil {
    public static int getYear(Timestamp date){
        return date.toLocalDateTime().getYear();
    }

    public static int getMonth(Timestamp date){
        return date.toLocalDateTime().getMonthValue();
    }


    public static int getDay(Timestamp date){
        return date.toLocalDateTime().getDayOfMonth();
    }
}
