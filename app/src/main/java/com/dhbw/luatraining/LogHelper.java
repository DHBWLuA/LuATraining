package com.dhbw.luatraining;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LogHelper
{
    private static StringBuilder internalLog;

    private LogHelper()
    {
        // private constructor means static class
    }

    public static void addLogLine(String logText)
    {
        if (internalLog == null)
            internalLog = new StringBuilder();
        String newLogLine = System.getProperty("line.separator") + getDateTimeString() + " - " + logText;
        internalLog.append(newLogLine);
    }

    private static String getDateTimeString()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN);
        return dateFormat.format(new Date());
    }

    public static StringBuilder getLogLines()
    {
        return internalLog;
    }
}
