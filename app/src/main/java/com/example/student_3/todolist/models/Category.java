package com.example.student_3.todolist.models;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

/**
 * Created by gromi on 11/22/2017.
 */

public class Category implements Parcelable, DefaultCategory{

    private String name;
    private int color;

    public Category(String name){
        this.name = name;
        Random random = new Random();
        this.color = Color.argb(255, random.nextInt(256), random.nextInt(256),
                random.nextInt(256));
    }

    protected Category(Parcel in){
        this.name = in.readString();
        this.color = in.readInt();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeInt(this.color);
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }
}
