package com.example.student_3.todolist.models;

import android.os.Parcel;

import com.example.student_3.todolist.Constants;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Student_3 on 02/11/2017.
 */

public class Task extends TaskObject {

    private String name;
    private Date expireDate;

    public Task(){
        expireDate = new Date();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeLong(this.expireDate != null ? this.expireDate.getTime() : -1);
    }

    protected Task(Parcel in){
        super(in);
        this.name = in.readString();
        long tmpExpireDate = in.readLong();
        this.expireDate = tmpExpireDate == -1 ? null : new Date(tmpExpireDate);
    }

    public static final Creator<Task> CREATOR = new Creator<Task>(){
        @Override
        public Task createFromParcel(Parcel source){
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[0];
        }

    };

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

    public void setName(String name) {
        this.name = name;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

}
