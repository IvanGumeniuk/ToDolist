package com.example.student_3.todolist.data;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.example.student_3.todolist.models.Category;
import com.example.student_3.todolist.models.Task;
import com.example.student_3.todolist.models.User;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Student_3 on 14/11/2017.
 */

public interface IDataSource {
    User getCurrentUser();
    ArrayList<User> getUserList();
    ArrayList<Task> getTaskList();
    ArrayList<Category> getCategoryList();
    boolean setCurrentUser(@NonNull User user);
    boolean createTask(@NonNull Task task);
    boolean createCategory(@NonNull Category category);
    boolean addUser(@NonNull User user);
    boolean updateTask(@NonNull Task task, @IntRange(from = 0, to = Integer.MAX_VALUE) int index);
}
