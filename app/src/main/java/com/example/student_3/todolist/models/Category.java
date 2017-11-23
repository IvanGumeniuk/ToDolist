package com.example.student_3.todolist.models;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

/**
 * Created by gromi on 11/22/2017.
 */

public class Category implements Parcelable{

    private String name;
    private int color;
    private int id;

    public Category(String name, int id){
        this.name = name;
        Random random = new Random();
        this.color = Color.argb(255, random.nextInt(256), random.nextInt(256),
                random.nextInt(256));
        this.id = id;
    }

    protected Category(Parcel in){
        this.name = in.readString();
        this.color = in.readInt();
        this.id = in.readInt();
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
        parcel.writeInt(this.id);
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if(obj instanceof Category){
            if(((Category) obj).getId() == this.id && ((Category) obj).getColor() == this.color
                    && ((Category) obj).getName().equals(this.name)){
                result = true;
            }
        }
        return result;
    }
}
