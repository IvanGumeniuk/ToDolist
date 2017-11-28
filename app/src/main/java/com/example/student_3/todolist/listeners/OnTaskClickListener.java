package com.example.student_3.todolist.listeners;

import android.view.View;

import com.example.student_3.todolist.models.Task;

/**
 * Created by gromi on 11/27/2017.
 */

public interface OnTaskClickListener {
    void onTaskClick(Task task, View view);
}
