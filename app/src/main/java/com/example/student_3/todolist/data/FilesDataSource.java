package com.example.student_3.todolist.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.student_3.todolist.models.Category;
import com.example.student_3.todolist.models.DefaultCategory;
import com.example.student_3.todolist.models.Task;
import com.example.student_3.todolist.models.User;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by 1996a on 30.11.2017.
 */

public class FilesDataSource implements IDataSource {

    private final static String MAIN_FILE = "USERS";
    private final static String TEMPORARY_FILE = "CURRENT";
    private final static String MAX_ID_CATEGORY = "max_id_for_category";

    private final static String CATEGORIES = "categories";
    private final static String USERS = "users";
    private final static String CURRENT = "current";


    private FileOutputStream outputStream;
    private FileInputStream inputStream;
    private Gson gson;
    private Context context;

    private User currentUser;
    private ArrayList<User> users;
    private ArrayList<Task> tasks;
    private Integer maxIdForCategory;

    public FilesDataSource(Context context) {
        this.context = context;

        readFromFile(MAIN_FILE);
        if(users == null) {
            users = new ArrayList<>();
        }

        readFromFile(TEMPORARY_FILE);
        if (currentUser != null) {
            if (currentUser.getCategories() == null) {
                currentUser.setCategories(new ArrayList<Category>());

                createCategory(new Category(DefaultCategory.defaultCategoryFirst, getIdForCategory()));
                createCategory(new Category(DefaultCategory.defaultCategorySecond, getIdForCategory()));
                createCategory(new Category(DefaultCategory.defaultCategoryThird, getIdForCategory()));
                createCategory(new Category(DefaultCategory.defaultCategoryFourth, getIdForCategory()));
                createCategory(new Category(DefaultCategory.defaultCategoryFifth, getIdForCategory()));
            }

            if (currentUser.getTasks() == null) {
                currentUser.setTasks(new ArrayList<Task>());
            } else {
                for (Task task : currentUser.getTasks()) {
                    checkAndUpdateCategory(task);
                }
                currentUser.setTasks(tasks);
            }
        }
    }

    private void writeToFile(String fileName) {
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            switch (fileName){
                case MAIN_FILE:
                    objectOutputStream.writeObject(users);
                    break;
                case TEMPORARY_FILE:
                    objectOutputStream.writeObject(currentUser);
                    break;
                case MAX_ID_CATEGORY:
                    objectOutputStream.writeObject(maxIdForCategory);
                    break;
            }


            objectOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFromFile(String fileName) {
        try {
            inputStream = context.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream streamReader = new ObjectInputStream(inputStream);

                switch (fileName){
                    case MAIN_FILE:
                        users = (ArrayList<User>) streamReader.readObject();
                        break;
                    case TEMPORARY_FILE:
                        currentUser = (User) streamReader.readObject();
                        break;
                    case MAX_ID_CATEGORY:
                        maxIdForCategory = (Integer) streamReader.readObject();
                        break;
                }

                streamReader.close();
                inputStream.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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
    public ArrayList<Task> getTaskList() {
        return currentUser.getTasks();
    }

    @Override
    public ArrayList<Category> getCategoryList() {
        return currentUser.getCategories();
    }

    @Override
    public boolean setCurrentUser(@NonNull User user) {
        currentUser = user;
        writeToFile(TEMPORARY_FILE);
        return true;
    }

    @Override
    public boolean createTask(@NonNull Task task) {
        currentUser.getTasks().add(task);
        writeToFile(TEMPORARY_FILE);
        return true;
    }

    @Override
    public boolean createCategory(@NonNull Category category) {
        for (Category existingCategory : getCurrentUser().getCategories()) {
            if (category.getName().toLowerCase().equals(existingCategory.getName().toLowerCase())) {
                return false;
            }
        }
        currentUser.getCategories().add(category);
        return true;
    }

    @Override
    public boolean addUser(@NonNull User user) {
        for (User existingUser : users) {
            if (existingUser.getEmail().equals(user.getEmail())) {
                return false;
            }
        }
        users.add(user);
        writeToFile(MAIN_FILE);
        return true;
    }

    @Override
    public boolean updateTask(@NonNull Task task, int index) {
        boolean result = false;
        if (index >= 0 && index < currentUser.getTasks().size()) {
            currentUser.getTasks().set(index, task);
            writeToFile(TEMPORARY_FILE);
            result = true;
        }
        return result;
    }

    private void checkAndUpdateCategory(Task task) {
        Category category = getCategoryById(task.getCategory().getId());
        if (!task.getCategory().equals(category)) {
            task.setCategory(category);
        }
    }

    @Override
    public int getIdForCategory() {
       readFromFile(MAX_ID_CATEGORY);
       if(maxIdForCategory == null){
           maxIdForCategory = 0;
       } else {
           maxIdForCategory++;
       }
       writeToFile(MAX_ID_CATEGORY);
       return maxIdForCategory;
    }

    @Override
    public void saveCurrentUser() {
        int userNumber = -1;
        for (int i = 0; i < users.size(); i++) {
            if (currentUser.getEmail().equals(users.get(i).getEmail())) {
                userNumber = i;
            }
        }
        users.set(userNumber, currentUser);
        writeToFile(MAIN_FILE);
    }

    @Nullable
    @Override
    public Category getCategoryById(int id) {
        for (Category category : getCurrentUser().getCategories()){
            if(category.getId() == id){
                return category;
            }
        }
        return null;
    }

    @Override
    public boolean isNameFreeForCategory(String name) {
        for(Category existingCategory : getCurrentUser().getCategories()){
            if(name.equalsIgnoreCase(existingCategory.getName())){
                return false;
            }
        }
        return true;
    }


}
