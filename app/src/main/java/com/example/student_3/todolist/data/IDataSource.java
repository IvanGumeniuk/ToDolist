package com.example.student_3.todolist.data;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.student_3.todolist.models.Category;
import com.example.student_3.todolist.models.Task;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Student_3 on 14/11/2017.
 */

public interface IDataSource {
    int defaultMaxId = 0;

    ArrayList<Task> getTaskList();
    ArrayList<Category> getCategoryList();
    boolean createTask(@NonNull Task task);
    boolean createCategory(@NonNull Category category);
    boolean updateTask(@NonNull Task task, @IntRange(from = 0, to = Integer.MAX_VALUE) int index);
    int getIdForCategory();
    @Nullable
    Category getCategoryById(int id);
    boolean isNameFreeForCategory(String name);
}
