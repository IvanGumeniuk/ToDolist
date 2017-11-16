package com.example.student_3.todolist.data;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.example.student_3.todolist.models.Task;

import java.util.ArrayList;

/**
 * Created by Student_3 on 14/11/2017.
 */

public interface IDataSource {
    ArrayList<Task> getTaskList();
    boolean create(@NonNull Task task);
    boolean updateTask(@NonNull Task task, @IntRange(from = 0, to = Integer.MAX_VALUE) int index);
}
