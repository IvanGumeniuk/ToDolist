package com.example.student_3.todolist.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.student_3.todolist.R;

public class CategoryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    RecyclerView categoryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initCategoryRecycler();
    }

    private void initCategoryRecycler() {
        categoryRecyclerView = (RecyclerView) findViewById(R.id.categoryRecyclerView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.category_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.add_category:
                //fragment with add category
                break;
            case R.id.search_category:
                //search in category list
                break;
            default:
                 super.onOptionsItemSelected(item);
                 break;
        }

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
