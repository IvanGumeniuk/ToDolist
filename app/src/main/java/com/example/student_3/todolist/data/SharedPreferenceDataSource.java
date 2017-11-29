package com.example.student_3.todolist.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.student_3.todolist.models.Category;
import com.example.student_3.todolist.models.DefaultCategory;
import com.example.student_3.todolist.models.Task;
import com.example.student_3.todolist.models.User;
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
    private final static String USERS = "users";
    private final static String CURRENT = "current";
    private final static String MAX_ID_CATEGORY = "max_id_for_category";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private User currentUser;
    private ArrayList<User> users;
    private ArrayList<Task> tasks;
    private ArrayList<Category> categories;

    public SharedPreferenceDataSource(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        gson = new Gson();

        String jsonUsers = sharedPreferences.getString(USERS, null);
        if (!TextUtils.isEmpty(jsonUsers)) {
            Type typeUser = new TypeToken<ArrayList<User>>() {
            }.getType();
            users = gson.fromJson(jsonUsers, typeUser);
        } else {
            users = new ArrayList<>();
        }

        String jsonCurrentUser = sharedPreferences.getString(CURRENT, null);
        if (!TextUtils.isEmpty(jsonCurrentUser)) {
            currentUser = gson.fromJson(jsonCurrentUser, User.class);

            String jsonTasks = sharedPreferences.getString(currentUser.getEmail(), null);
            if (!TextUtils.isEmpty(jsonTasks)) {
                Type typeTask = new TypeToken<ArrayList<Task>>() {
                }.getType();
                tasks = gson.fromJson(jsonTasks, typeTask);
                for (Task task : tasks) {
                    checkAndUpdateCategory(task);
                }
                currentUser.setTasks(tasks);
            } else {
                currentUser.setTasks(new ArrayList<Task>());
            }

            String jsonCategories = sharedPreferences.getString(currentUser.getEmail() + CATEGORIES, null);
            if (!TextUtils.isEmpty(jsonCategories)) {
                Type typeCategories = new TypeToken<ArrayList<Category>>() {
                }.getType();
                categories = gson.fromJson(jsonCategories, typeCategories);
                currentUser.setCategories(categories);
            } else {
                categories = new ArrayList<>();
                createCategory(new Category(DefaultCategory.defaultCategoryFirst, getIdForCategory()));
                createCategory(new Category(DefaultCategory.defaultCategorySecond, getIdForCategory()));
                createCategory(new Category(DefaultCategory.defaultCategoryThird, getIdForCategory()));
                createCategory(new Category(DefaultCategory.defaultCategoryFourth, getIdForCategory()));
                createCategory(new Category(DefaultCategory.defaultCategoryFifth, getIdForCategory()));

                currentUser.setCategories(categories);
            }
        }
    }

    private void checkAndUpdateCategory(Task task){
        Category category = getCategoryById(task.getCategory().getId());
        if(!task.getCategory().equals(category)){
            task.setCategory(category);
        }
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public ArrayList<User> getUserList() {
        return users;
    }

    @Override
    public boolean isNameFreeForCategory(String name) {
        for(Category existingCategory : categories){
            if(name.equalsIgnoreCase(existingCategory.getName())){
                return false;
            }
        }
        return true;
    }

    @Nullable
    @Override
    public Category getCategoryById(int id) {
        for (Category category : categories){
            if(category.getId() == id){
                return category;
            }
        }
        return null;
    }

    @Override
    public int getIdForCategory() {
        int id = sharedPreferences.getInt(MAX_ID_CATEGORY, 0);
        sharedPreferences.edit().putInt(MAX_ID_CATEGORY, ++id).commit();
        return id;
    }

    @Override
    public ArrayList<Task> getTaskList() {
        return currentUser.getTasks();
    }

    @Override
    public ArrayList<Category> getCategoryList() {
        return currentUser.getCategories();
    }

    @Override
    public boolean setCurrentUser(@NonNull User user) {
        editor = sharedPreferences.edit();
        editor.putString(CURRENT, gson.toJson(user));
        return editor.commit();
    }

    @Override
    public boolean createTask(@NonNull Task task) {
        currentUser.getTasks().add(task);
        editor = sharedPreferences.edit();
        editor.putString(currentUser.getEmail(), gson.toJson(currentUser.getTasks()));
        return editor.commit();
    }

    @Override
    public boolean createCategory(@NonNull Category category) {
        for (Category existingCategory : categories) {
            if (category.getName().toLowerCase().equals(existingCategory.getName().toLowerCase())) {
                return false;
            }
        }
        currentUser.getCategories().add(category);
        editor = sharedPreferences.edit();
        editor.putString(currentUser.getEmail()+CATEGORIES, gson.toJson(currentUser.getCategories()));
        return editor.commit();
    }

    @Override
    public boolean addUser(@NonNull User user) {
        for (User existingUser : users) {
            if (existingUser.getEmail().equals(user.getEmail())) {
                return false;
            }
        }
        users.add(user);
        editor = sharedPreferences.edit();
        editor.putString(USERS, gson.toJson(users));
        return editor.commit();
    }

    @Override
    public boolean updateTask(@NonNull Task task, @IntRange(from = 0, to = Integer.MAX_VALUE) int index) {
        boolean result = false;
        if (index >= 0 && index < tasks.size()) {
            currentUser.getTasks().set(index, task);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(currentUser.getEmail(), gson.toJson(currentUser.getTasks()));
            result = editor.commit();
        }
        return result;
    }

}
