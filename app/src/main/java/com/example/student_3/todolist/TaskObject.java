package com.example.student_3.todolist;

/**
 * Created by Student_3 on 02/11/2017.
 */

public class TaskObject {
    public enum TaskStatus{
        NEW,
        DONE
    }

    private String description;
    private TaskStatus status;

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
}


