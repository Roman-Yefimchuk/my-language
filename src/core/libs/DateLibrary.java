package core.libs;

import java.util.*;

public class DateLibrary implements AbstractLibrary {

    public static final int GET_YEAR = 1097005045;//"Date.getYear()I"
    public static final int GET_MONTH = 869563454;//"Date.getMonth()I"
    public static final int GET_DAY = -1815801054;//"Date.getDay()I"
    public static final int GET_HOUR = -800893714;//"Date.getHour()I"
    public static final int GET_MINUTE = -638737346;//"Date.getMinute()I"
    public static final int GET_SECOND = 1670179294;//"Date.getSecond()I"
    public static final int GET_MILLISECOND = -338467811;//"Date.getMillisecond()I"
    private static Calendar calendar;
    private static Date date;

    public void constructor() {
        calendar = Calendar.getInstance();
        date = new Date();
    }

    public Object invoke(int functionId, Object[] args) {
        switch (functionId) {
            case GET_YEAR: {
                date.setTime(System.currentTimeMillis());
                calendar.setTime(date);
                return new int[]{calendar.get(Calendar.YEAR)};
            }
            case GET_MONTH: {
                date.setTime(System.currentTimeMillis());
                calendar.setTime(date);
                return new int[]{calendar.get(Calendar.MONTH)};
            }
            case GET_DAY: {
                date.setTime(System.currentTimeMillis());
                calendar.setTime(date);
                return new int[]{calendar.get(Calendar.DAY_OF_MONTH)};
            }
            case GET_HOUR: {
                date.setTime(System.currentTimeMillis());
                calendar.setTime(date);
                return new int[]{calendar.get(Calendar.HOUR_OF_DAY)};
            }
            case GET_MINUTE: {
                date.setTime(System.currentTimeMillis());
                calendar.setTime(date);
                return new int[]{calendar.get(Calendar.MINUTE)};
            }
            case GET_SECOND: {
                date.setTime(System.currentTimeMillis());
                calendar.setTime(date);
                return new int[]{calendar.get(Calendar.SECOND)};
            }
            case GET_MILLISECOND: {
                date.setTime(System.currentTimeMillis());
                calendar.setTime(date);
                return new int[]{calendar.get(Calendar.MILLISECOND)};
            }
        }
        throw new RuntimeException(getName() + "[" + functionId + "]");
    }

    public String getName() {
        return "Date";
    }

    public void destructor() {
        calendar = null;
        date = null;
    }
}
