package com.example.student_3.todolist.data;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.student_3.todolist.BundleKey;
import com.example.student_3.todolist.listeners.OnDataChangedListener;
import com.example.student_3.todolist.models.Category;
import com.example.student_3.todolist.models.DefaultCategories;
import com.example.student_3.todolist.models.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by gromi on 11/29/2017.
 */

public class FileDataSource implements IDataSource, LoaderManager.LoaderCallbacks<Bundle>{

    private static final String TAG = "FILE_DATA_SOURCE";
    private static final String FILE_NAME = "datasource";
    private static final int TASKS_JSON_CODE = 52937843;
    private static final int CATEGORIES_JSON_CODE = 52937844;
    private static final int CATEGORY_MAX_ID_CODE = 52937845;
    private static final int JSON_START_OFFSET = 8;
    private static final int LOADER_ID = 555555;

    private enum Mode{
        GET_ALL_DATA,
        UPDATE_TASK,
        UPDATE_CATEGORY,
        UPDATE_MAXID_CATEGORY,
        CREATE_TASK,
        CREATE_CATEGORY
    }

    private ArrayList<Task> tasks;
    private ArrayList<Category> categories;
    private int categoryMaxId;
    private AppCompatActivity context;
    private OnDataChangedListener dataListener;
    private boolean needLoadEverything;

    public FileDataSource(AppCompatActivity context, OnDataChangedListener dataListener){
        tasks = new ArrayList<>();
        categories = new ArrayList<>();
        this.context = context;
        this.dataListener = dataListener;
        needLoadEverything = true;
        doInBackground(Mode.GET_ALL_DATA, null);
    }

    private void doInBackground(Mode mode,@Nullable Bundle args){
        if(args == null){
            args = new Bundle();
        }
        args.putInt(BundleKey.MODE.name(), mode.ordinal());
        if(this.context.getSupportLoaderManager().getLoader(LOADER_ID) != null){
            this.context.getSupportLoaderManager().restartLoader(LOADER_ID, args, this);
        } else {
            this.context.getSupportLoaderManager().initLoader(LOADER_ID, args, this);
        }
    }

    @Override
    public void onLoadFinished(Loader<Bundle> loader, Bundle data) {
        if(needLoadEverything) {
            this.tasks.clear();
            this.tasks.addAll(data.getParcelableArrayList(BundleKey.TASK.name()));
            this.categories.clear();
            this.categories.addAll(data.getParcelableArrayList(BundleKey.CATEGORY.name()));
            this.categoryMaxId = data.getInt(BundleKey.CATEGORY_ID.name());
            if(dataListener != null){
                dataListener.notifyDataChanged();
            }
            needLoadEverything = false;
        }
    }

    @Override
    public Loader<Bundle> onCreateLoader(int i, Bundle bundle) {
        return new DataLoader(context, bundle);
    }

    @Override
    public void onLoaderReset(Loader<Bundle> loader) {

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
        Bundle taskBundle = new Bundle();
        taskBundle.putParcelable(BundleKey.TASK.name(), task);
        doInBackground(Mode.CREATE_TASK, taskBundle);
        return tasks.add(task);
    }

    @Override
    public boolean createCategory(@NonNull Category category) {
        Bundle categoryBundle = new Bundle();
        categoryBundle.putParcelable(BundleKey.CATEGORY.name(), category);
        doInBackground(Mode.CREATE_CATEGORY, categoryBundle);
        return categories.add(category);
    }

    @Override
    public boolean updateTask(@NonNull Task task, int index) {
        boolean result = false;
        if(index >= 0 && tasks.size() > index) {
            tasks.set(index, task);
            Bundle taskBundle = new Bundle();
            taskBundle.putParcelable(BundleKey.TASK.name(), task);
            taskBundle.putInt(BundleKey.INDEX.name(), index);
            doInBackground(Mode.UPDATE_TASK, taskBundle);
            result = true;
        }
        return result;
    }

    @Override
    public int getIdForCategory() {
        doInBackground(Mode.UPDATE_MAXID_CATEGORY, null);
        return categoryMaxId++;
    }

    @Nullable
    @Override
    public Category getCategoryById(int id) {
        Category returnCategory = null;
        for(Category category : categories){
            if(category.getId() == id){
                returnCategory = category;
                break;
            }
        }
        return returnCategory;
    }

    @Override
    public boolean isNameFreeForCategory(String name) {
        boolean result = true;
        for (Category category : categories){
            if (category.getName().equalsIgnoreCase(name)){
                result = false;
                break;
            }
        }
        return result;
    }

    protected static class DataLoader extends AsyncTaskLoader<Bundle>{
        private File file;
        private ArrayList<Category> categories;
        private ArrayList<Task> tasks;
        private Gson gson;
        private Integer categoryMaxId;
        private Mode mode;
        private Bundle args;

        DataLoader(Context context, Bundle args) {
            super(context);
            gson = new Gson();
            this.tasks = new ArrayList<>();
            this.categories = new ArrayList<>();
            this.args = args;
            this.mode = Mode.values()[this.args.getInt(BundleKey.MODE.name())];
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

        @Override
        public Bundle loadInBackground() {
            Bundle result = null;
            openDataSource();
            switch (mode) {
                case GET_ALL_DATA:
                    result = getAllData();
                    break;
                case CREATE_TASK:
                    createTask();
                    break;
                case UPDATE_TASK:
                    updateTask();
                    break;
                case CREATE_CATEGORY:
                    createCategory();
                    break;
                case UPDATE_MAXID_CATEGORY:
                    updateCategoryMaxId();
                    break;
                default:
                    break;
            }
            return result;
        }

        private void openDataSource(){
            file = new File(getContext().getFilesDir(), FILE_NAME);
            if(file.exists()){
                openFileAndLoadData();
            } else {
                createDefaultDataAndFile();
            }
        }

        private void openFileAndLoadData(){
            try(FileReader fileReader = new FileReader(file);
                BufferedReader reader = new BufferedReader(fileReader)){
                String line = reader.readLine();
                while (line != null){
                    transformDataFromFile(line);
                    line = reader.readLine();
                }
            } catch (IOException exception){
                Log.e(TAG, "openFileAndLoadData: ", exception);
            }
        }

        private void createDefaultDataAndFile(){
            for(DefaultCategories categoryName : DefaultCategories.values()){
                categories.add(new Category(categoryName.name(), categoryMaxId++));
            }
            for (int i = 0; i < 60; i++){
                Task task = new Task();
                task.setName("bla"+i*300);
                task.setDescription("gokoprthijrthiojrithojrtoihjrtoihjroithjroithjroithjrit");
                task.setCategory(categories.get(new Random().nextInt(categories.size())));
                task.setExpireDate(new Date());
                tasks.add(task);
            }
            saveEverythingToFile();
        }

        private void saveEverythingToFile() {
            try(FileWriter fileWriter = new FileWriter(file);
                BufferedWriter writer = new BufferedWriter(fileWriter)){
                writeToFile(writer, TASKS_JSON_CODE, gson.toJson(tasks));
                writeToFile(writer, CATEGORIES_JSON_CODE, gson.toJson(categories));
                writeToFile(writer, CATEGORY_MAX_ID_CODE, gson.toJson(categoryMaxId));
            } catch (IOException exception){
                Log.e(TAG, "createDefaultDataAndFile: ", exception);
            }
        }

        private void writeToFile(BufferedWriter writer, int code, String json) throws IOException{
            writer.write(String.format("%s%s", code, json));
            writer.newLine();
        }

        private void transformDataFromFile(String data){
            int code = Integer.valueOf(data.substring(0, JSON_START_OFFSET));
            String json = data.substring(JSON_START_OFFSET, data.length());
            switch (code){
                case TASKS_JSON_CODE:
                    tasks.clear();
                    Type typeTask = new TypeToken<ArrayList<Task>>() {}.getType();
                    tasks.addAll(gson.fromJson(json, typeTask));
                    break;
                case CATEGORIES_JSON_CODE:
                    categories.clear();
                    Type typeCategories = new TypeToken<ArrayList<Category>>() {}.getType();
                    categories.addAll(categories = gson.fromJson(json, typeCategories));
                    break;
                case CATEGORY_MAX_ID_CODE:
                    Type typeMaxId = new TypeToken<Integer>() {}.getType();
                    categoryMaxId = gson.fromJson(json, typeMaxId);
                    break;
                default:
                    break;
            }
        }

        private Bundle getAllData(){
            Bundle result = new Bundle();
            result.putParcelableArrayList(BundleKey.CATEGORY.name(), categories);
            result.putParcelableArrayList(BundleKey.TASK.name(), tasks);
            result.putInt(BundleKey.CATEGORY_ID.name(), categoryMaxId);
            result.putInt(BundleKey.MODE.name(), Mode.GET_ALL_DATA.ordinal());
            return result;
        }

        private void createCategory(){
            categories.add(args.getParcelable(BundleKey.CATEGORY.name()));
            saveEverythingToFile();
        }

        private void updateTask(){
            tasks.set(args.getInt(BundleKey.INDEX.name()), args.getParcelable(BundleKey.TASK.name()));
            saveEverythingToFile();
        }

        private void createTask(){
            tasks.add(args.getParcelable(BundleKey.TASK.name()));
            saveEverythingToFile();
        }

        private void updateCategoryMaxId(){
            categoryMaxId++;
            saveEverythingToFile();
        }
    }
}
