package com.example.student_3.todolist.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Student_3 on 02/11/2017.
 */

public class TaskObject implements Parcelable{
    public enum TaskStatus{
        NEW,
        DONE
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeParcelable(category, flags);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
    }

    protected TaskObject(Parcel in){
        this.description = in.readString();
        this.category = in.readParcelable(Category.class.getClassLoader());
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : TaskStatus.values()[tmpStatus];
    }

    private String description;
    private TaskStatus status;
    private Category category;

    public TaskObject(){
        status = TaskStatus.NEW;
    }

    public  boolean isDone(){
        return status == TaskStatus.DONE;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}


