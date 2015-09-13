package dina.library.libs;

import java.util.*;
import dina.library.*;
import dina.runtime.*;

public class DateLibrary extends Library {

    public static Calendar calendar;
    public static Date date;

    @Override
    public void initLibrary() {
        calendar = Calendar.getInstance();
        date = new Date();
    }

    @Override
    public void invoke(int functionID) {
        switch (functionID) {
            case FunctionsID.GET_YEAR: {
                date.setTime(System.currentTimeMillis());
                calendar.setTime(date);
                DinaVM.operands[++DinaVM.pointer] = new int[]{calendar.get(Calendar.YEAR)};
                return;
            }
            case FunctionsID.GET_MONTH: {
                date.setTime(System.currentTimeMillis());
                calendar.setTime(date);
                DinaVM.operands[++DinaVM.pointer] = new int[]{calendar.get(Calendar.MONTH)};
                return;
            }
            case FunctionsID.GET_DAY: {
                date.setTime(System.currentTimeMillis());
                calendar.setTime(date);
                DinaVM.operands[++DinaVM.pointer] = new int[]{calendar.get(Calendar.DAY_OF_MONTH)};
                return;
            }
            case FunctionsID.GET_HOUR: {
                date.setTime(System.currentTimeMillis());
                calendar.setTime(date);
                DinaVM.operands[++DinaVM.pointer] = new int[]{calendar.get(Calendar.HOUR_OF_DAY)};
                return;
            }
            case FunctionsID.GET_MINUTE: {
                date.setTime(System.currentTimeMillis());
                calendar.setTime(date);
                DinaVM.operands[++DinaVM.pointer] = new int[]{calendar.get(Calendar.MINUTE)};
                return;
            }
            case FunctionsID.GET_SECOND: {
                date.setTime(System.currentTimeMillis());
                calendar.setTime(date);
                DinaVM.operands[++DinaVM.pointer] = new int[]{calendar.get(Calendar.SECOND)};
                return;
            }
            case FunctionsID.GET_MILLISECOND: {
                date.setTime(System.currentTimeMillis());
                calendar.setTime(date);
                DinaVM.operands[++DinaVM.pointer] = new int[]{calendar.get(Calendar.MILLISECOND)};
                return;
            }
        }
    }

    public String getLibratyName() {
        return "date";
    }
}
