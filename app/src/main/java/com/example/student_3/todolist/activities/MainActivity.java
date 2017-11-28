package com.example.student_3.todolist.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student_3.todolist.ActivityRequest;
import com.example.student_3.todolist.BundleKey;
import com.example.student_3.todolist.R;
import com.example.student_3.todolist.adapters.TaskAdapterWithStyles;
import com.example.student_3.todolist.listeners.OnTaskClickListener;
import com.example.student_3.todolist.models.Task;
import com.example.student_3.todolist.data.IDataSource;
import com.example.student_3.todolist.data.SharedPreferenceDataSource;
import com.example.student_3.todolist.decorators.GridSpacingItemDecoration;

public class MainActivity extends AppCompatActivity implements OnTaskClickListener{

    private final static String GRID_LAYOUT = "grid layout";

    private FloatingActionButton createTaskButton;
    private RecyclerView taskRecyclerView;
    private TaskAdapterWithStyles taskAdapter;
    private IDataSource dataSource;
    private boolean gridLayout = true;
    private DividerItemDecoration dividerItemDecoration;
    private GridSpacingItemDecoration gridSpacingItemDecoration;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null){
            gridLayout = savedInstanceState.getBoolean(GRID_LAYOUT, true);
        }
        initCreateTaskButton();
        initTaskRecycler();
        dataSource = new SharedPreferenceDataSource(this);
        initTaskAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initTaskRecycler(){
        taskRecyclerView = (RecyclerView) findViewById(R.id.taskRecyclerView);
        setLayoutForRecyclerView();
    }

    private void initTaskAdapter(){
        taskAdapter = new TaskAdapterWithStyles(dataSource.getTaskList(), this);
        taskRecyclerView.setAdapter(taskAdapter);
    }

    private void setLayoutForRecyclerView(){
        if(!gridLayout){
            if(linearLayoutManager == null){
                linearLayoutManager =
                        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            }
            dividerItemDecoration = new DividerItemDecoration(taskRecyclerView.getContext(),
                    linearLayoutManager.getOrientation());
            taskRecyclerView.removeItemDecoration(gridSpacingItemDecoration);
            taskRecyclerView.addItemDecoration(dividerItemDecoration);
            taskRecyclerView.setLayoutManager(linearLayoutManager);
        } else {
            if(gridLayoutManager == null){
                int amountOfColumns = getResources().getInteger(R.integer.grid_columns_amount);
                gridLayoutManager = new GridLayoutManager(this, amountOfColumns);
                gridSpacingItemDecoration = new GridSpacingItemDecoration(getResources()
                        .getInteger(R.integer.grid_columns_amount),
                        getResources().getDimensionPixelSize(R.dimen.grid_spacing_horizontal),
                        getResources().getDimensionPixelSize(R.dimen.grid_spacing_vertical),
                        getResources().getDimensionPixelSize(R.dimen.grid_spacing_edge));
            }
            taskRecyclerView.removeItemDecoration(dividerItemDecoration);
            taskRecyclerView.addItemDecoration(gridSpacingItemDecoration);
            taskRecyclerView.setLayoutManager(gridLayoutManager);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_change_layout:
                gridLayout  = !gridLayout;
                item.setTitle(gridLayout ? R.string.linear_layout : R.string.grid_layout);
                setLayoutForRecyclerView();
                break;
            case R.id.go_to_category_activity:
                startActivity(new Intent(this, CategoryActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(GRID_LAYOUT, gridLayout);
    }

    @Override
    protected void onResume(){
        super.onResume();
        int size = dataSource.getTaskList().size();
        Toast.makeText(this, String.format("%d task%s", size, size>0?"s":""), Toast.LENGTH_SHORT).show();
    }

    private void initCreateTaskButton(){
        createTaskButton = (FloatingActionButton) findViewById(R.id.createTaskButton);
        createTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Task task = new Task();
                Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);
                intent.putExtra(BundleKey.TASK.name(), task);
                startActivityForResult(intent, ActivityRequest.CREATE_TASK.ordinal());
            }
        });
    }

    @Override
    public void onTaskClick(Task task, View view) {
        View nameTextView = view.findViewById(R.id.nameTextView);
        View descriptionTextView = view.findViewById(R.id.descriptionTextView);
        View categoryTextView = view.findViewById(R.id.categoryTextView);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                new Pair<>(nameTextView, ViewCompat.getTransitionName(nameTextView)),
                new Pair<>(descriptionTextView, ViewCompat.getTransitionName(descriptionTextView)),
                new Pair<>(categoryTextView, ViewCompat.getTransitionName(categoryTextView)));
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(BundleKey.TASK.name(), task);
        intent.putExtra(BundleKey.NAME_TRANSITION.name(), ViewCompat.getTransitionName(nameTextView));
        intent.putExtra(BundleKey.DESCRIPTION_TRANSITION.name(), ViewCompat.getTransitionName(descriptionTextView));
        intent.putExtra(BundleKey.CATEGORY_TRANSITION.name(), ViewCompat.getTransitionName(categoryTextView));
        ActivityCompat.startActivity(this, intent, activityOptionsCompat.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (ActivityRequest.values()[requestCode]){
            case CREATE_TASK:
                if(resultCode == Activity.RESULT_OK){
                    Task task = data.getParcelableExtra(BundleKey.TASK.name());
                    if(task != null){
                        dataSource.createTask(task);
                        taskAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }
}

