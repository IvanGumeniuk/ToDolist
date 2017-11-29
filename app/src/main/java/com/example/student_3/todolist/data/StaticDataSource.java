package com.example.student_3.todolist.data;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.student_3.todolist.models.Category;
import com.example.student_3.todolist.models.Task;
import com.example.student_3.todolist.models.User;

import java.util.ArrayList;

/**
 * Created by Student_3 on 14/11/2017.
 */

public class StaticDataSource implements IDataSource {

    private static final ArrayList<Task> tasks = new ArrayList<>();
    private static final ArrayList<Category> categories = new ArrayList<>();

    @Override
    public User getCurrentUser() {
        return null;
    }

    @Override
    public ArrayList<User> getUserList() {
        return null;
    }

    @Override
    public ArrayList<Task> getTaskList() {
        return tasks;
    }

    @Override
    public ArrayList<Category> getCategoryList() {
        return categories;
    }

    @Override
    public boolean setCurrentUser(@NonNull User user) {
        return false;
    }

    @Override
    public boolean createTask(@NonNull Task task) {
        return tasks.add(task);
    }

    @Override
    public boolean createCategory(@NonNull Category category) {
        return categories.add(category);
    }

    @Override
    public boolean addUser(@NonNull User user) {
        return false;
    }

    @Override
    public boolean updateTask(@NonNull Task task, @IntRange(from = 0, to = Integer.MAX_VALUE) int index) {
        boolean result = false;
        if (index >= 0 && index < tasks.size()){
            tasks.set(index, task);
            result = true;
        }
        return result;
    }

    @Override
    public int getIdForCategory() {
        return 0;
    }

    @Nullable
    @Override
    public Category getCategoryById(int id) {
        return null;
    }

    @Override
    public boolean isNameFreeForCategory(String name) {
        return false;
    }
}
