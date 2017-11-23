package com.example.student_3.todolist.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.student_3.todolist.ActivityRequest;
import com.example.student_3.todolist.BundleKey;
import com.example.student_3.todolist.R;
import com.example.student_3.todolist.adapters.CategoryAdapter;
import com.example.student_3.todolist.data.IDataSource;
import com.example.student_3.todolist.data.SharedPreferenceDataSource;
import com.example.student_3.todolist.dialogs.AddCategoryFragment;
import com.example.student_3.todolist.listeners.OnCategoryClickListener;
import com.example.student_3.todolist.models.Category;

import java.util.List;

public class CategoryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        OnCategoryClickListener{

    public static Intent launchInEditMode(Context context){
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra(BundleKey.EDIT_MODE.name(), true);
        return intent;
    }

    public static Intent launch(Context context){
        Intent intent = new Intent(context, CategoryActivity.class);
        return intent;
    }

    private static final String CURRENT_FILTER = "currentFilter";

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categories;
    private IDataSource dataSource;
    private MenuItem searchItem;
    private SearchView searchView;
    private String currentFilterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        dataSource = new SharedPreferenceDataSource(this);
        categories = dataSource.getCategoryList();
        initCategoryAdapter();
        initCategoryRecycler();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            currentFilterText = savedInstanceState.getString(CURRENT_FILTER);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(currentFilterText != null){
            outState.putString(CURRENT_FILTER, currentFilterText);
        }
    }

    private void initCategoryRecycler() {
        categoryRecyclerView = (RecyclerView) findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        categoryRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    private void initCategoryAdapter(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getBoolean(BundleKey.EDIT_MODE.name(), false)){
                categoryAdapter = new CategoryAdapter(categories, this);
            } else {
                categoryAdapter = new CategoryAdapter(categories);
            }
        } else {
            categoryAdapter = new CategoryAdapter(categories);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.category_activity_menu,menu);
        searchItem = menu.findItem(R.id.search_category);
        searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        if(currentFilterText != null){
            searchItem.expandActionView();
            searchView.setIconified(false);
            searchView.setQuery(currentFilterText, true);
            searchView.clearFocus();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_category:
                addCategory();
                break;
            case R.id.search_category:
                break;
            default:
                 super.onOptionsItemSelected(item);
                 break;
        }
        return true;
    }

    private void addCategory(){
        AddCategoryFragment fragment = new AddCategoryFragment();
        Bundle arguments = new Bundle();
        arguments.putBoolean(BundleKey.CREATE_CATEGORY.name(), true);
        fragment.setArguments(arguments);
        fragment.show(getSupportFragmentManager(), "addCategory");
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        currentFilterText = s;
        categoryAdapter.filter(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        currentFilterText = s;
        categoryAdapter.filter(s);
        return true;
    }

    public boolean saveCategory(String categoryName){
        boolean result = dataSource.isNameFreeForCategory(categoryName);
        if(result){
            result = dataSource.createCategory(new Category(categoryName, dataSource.getIdForCategory()));
            categoryAdapter.updateFilter();
        }
        return result;
    }

    @Override
    public void onClick(Category category) {
        Intent intent = new Intent();
        intent.putExtra(BundleKey.CATEGORY.name(), category);
        setResult(RESULT_OK, intent);
        finish();
    }
}
