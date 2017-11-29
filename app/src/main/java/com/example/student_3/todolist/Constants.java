package com.example.student_3.todolist;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Student_3 on 02/11/2017.
 */

public final class Constants {

    private Constants(){}

    private static final String DATE_FORMAT_STRING = "dd MMMM yyyy";
    public static final SimpleDateFormat DATE_FORMAT;
    public static final long TimeForCheckPassword = TimeUnit.SECONDS.toMillis(30);

    static {
        DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STRING, Locale.getDefault());
    }
}
