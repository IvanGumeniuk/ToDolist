package com.example.student_3.todolist;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Student_3 on 02/11/2017.
 */

public class Task extends TaskObject {

    private String name;
    private Date expireDate;
    //private ArrayList<SubTask> subTasksList;

    public Task(){
        expireDate = new Date();
        //subTasksList = new ArrayList<>();
    }

    public String getExpireDateString(){
        return Constants.DATE_FORMAT.format(expireDate);
    }

    public boolean isExpired(){
        return expireDate.compareTo(new Date()) < 1;
    }

    public String getLeftTime(){
        String result = "expired";
        long difference = expireDate.getTime() - System.currentTimeMillis();
        long values = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
        boolean resultFound = false;

        if(values > 0){
            result = formatDate(values, "day");
            resultFound = true;
        } else {
            values = TimeUnit.HOURS.convert(difference, TimeUnit.MILLISECONDS);
        }
        if (values > 0 && !resultFound) {
            result = formatDate(values, "hour");
            resultFound = true;
        } else {
            values = TimeUnit.MINUTES.convert(difference, TimeUnit.MILLISECONDS);
        }
        if(values > 0 && !resultFound){
            result = formatDate(values, "minute");
            resultFound = true;
        } else {
            values = TimeUnit.SECONDS.convert(difference, TimeUnit.MILLISECONDS);
        }
        if(values > 0 && !resultFound){
            result = formatDate(values, "second");
        }
        return result;
    }

    private String formatDate(long value, String type){
        String result = String.format(Locale.getDefault(), "%d " + type, value);
        return value > 1 ? result + "s" : result;
    }

    public String getName() {
        return name;
    }

    public Date getExpireDate() {
        return expireDate;
    }

//    public ArrayList<SubTask> getSubTasksList() {
//        return subTasksList;
//    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

//    public void setSubTasksList(ArrayList<SubTask> subTasksList) {
//        this.subTasksList = subTasksList;
//    }
}
