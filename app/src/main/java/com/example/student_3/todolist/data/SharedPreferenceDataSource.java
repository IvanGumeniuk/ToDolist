package com.example.student_3.todolist.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.student_3.todolist.models.Category;
import com.example.student_3.todolist.models.DefaultCategory;
import com.example.student_3.todolist.models.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Student_3 on 14/11/2017.
 */

public class SharedPreferenceDataSource implements IDataSource {

    private final static String TASKS = "tasks";
    private final static String CATEGORIES = "categories";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private ArrayList<Task> tasks;
    private ArrayList<Category> categories;

    public SharedPreferenceDataSource(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        gson = new Gson();
        String jsonTasks = sharedPreferences.getString(TASKS, null);
        String jsonCategories = sharedPreferences.getString(CATEGORIES, null);
        if(!TextUtils.isEmpty(jsonTasks)) {
            Type typeTask = new TypeToken<ArrayList<Task>>() {}.getType();
            tasks = gson.fromJson(jsonTasks, typeTask);
        } else {
            tasks = new ArrayList<>();
        }

        if(!TextUtils.isEmpty(jsonCategories)) {
            Type typeCategories = new TypeToken<ArrayList<Category>>() {}.getType();
            categories = gson.fromJson(jsonTasks, typeCategories);
        } else {
            categories = new ArrayList<>();
            createCategory(new Category(DefaultCategory.defaultCategoryFirst));
            createCategory(new Category(DefaultCategory.defaultCategorySecond));
            createCategory(new Category(DefaultCategory.defaultCategoryThird));
            createCategory(new Category(DefaultCategory.defaultCategoryFourth));
            createCategory(new Category(DefaultCategory.defaultCategoryFifth));
        }
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
    public boolean createTask(@NonNull Task task) {
        tasks.add(task);
        editor = sharedPreferences.edit();
        editor.putString(TASKS, gson.toJson(tasks));
        return editor.commit();
    }

    @Override
    public boolean createCategory(@NonNull Category category) {
        categories.add(category);
        editor = sharedPreferences.edit();
        editor.putString(CATEGORIES, gson.toJson(categories));
        return editor.commit();
    }

    @Override
    public boolean updateTask(@NonNull Task task, @IntRange(from = 0, to = Integer.MAX_VALUE) int index) {
        boolean result = false;
        if (index >= 0 && index < tasks.size()){
            tasks.set(index, task);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(TASKS, gson.toJson(tasks));
            result = editor.commit();
        }
        return result;
    }

}
