package com.example.student_3.todolist.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.student_3.todolist.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Student_3 on 14/11/2017.
 */

public class SharedPreferenceDataSource implements IDataSource {

    private final static String TASKS = "tasks";

    private SharedPreferences sharedPreferences;
    private Gson gson;
    private ArrayList<Task> tasks;

    public SharedPreferenceDataSource(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        gson = new Gson();
        String jsonTasks = sharedPreferences.getString(TASKS, null);
        if(!TextUtils.isEmpty(jsonTasks)) {
            Type type = new TypeToken<ArrayList<Task>>() {}.getType();
            tasks = gson.fromJson(jsonTasks, type);
        } else {
            tasks = new ArrayList<>();
        }
    }

    @Override
    public ArrayList<Task> getTaskList() {
        return tasks;
    }

    @Override
    public boolean create(@NonNull Task task) {
        tasks.add(task);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TASKS, gson.toJson(tasks));
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
