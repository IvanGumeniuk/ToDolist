package com.example.student_3.todolist.activities;

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

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categories;
    private IDataSource dataSource;
    private MenuItem searchItem;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        dataSource = new SharedPreferenceDataSource(this);
        categories = dataSource.getCategoryList();
        initCategoryRecycler();
    }

    private void initCategoryRecycler() {
        categoryRecyclerView = (RecyclerView) findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        categoryRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        categoryAdapter = new CategoryAdapter(categories, this);
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.category_activity_menu,menu);
        searchItem = menu.findItem(R.id.search_category);
        searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
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
        fragment.show(getSupportFragmentManager(), "addCategory");
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        categoryAdapter.filter(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        categoryAdapter.filter(s);
        return true;
    }

    public boolean saveCategory(Category category){
        boolean result = dataSource.createCategory(category);
        if(result){
            categoryAdapter.filter("");
            searchView.setQuery("", false);
            searchView.setIconified(true);
            searchItem.collapseActionView();
        }
        return result;
    }

    @Override
    public void onClick(Category category) {
        if(getIntent().getStringExtra("key")!= null &&
                getIntent().getStringExtra("key").equals(ActivityRequest.GET_CATEGORY.name())) {
            //Toast.makeText(this, category.getName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("category", category.getName());
            setResult(RESULT_OK, intent);
            finish();
        }

        //Toast.makeText(this, category.getName(), Toast.LENGTH_SHORT).show();
    }
}
