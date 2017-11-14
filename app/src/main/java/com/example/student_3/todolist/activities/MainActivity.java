package com.example.student_3.todolist.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.student_3.todolist.ActivityRequest;
import com.example.student_3.todolist.BundleKey;
import com.example.student_3.todolist.R;
import com.example.student_3.todolist.Task;
import com.example.student_3.todolist.adapters.TaskAdapter;
import com.example.student_3.todolist.data.IDataSource;
import com.example.student_3.todolist.data.SharedPreferenceDataSource;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton createTaskButton;
    private RecyclerView taskRecyclerView;
    private TaskAdapter taskAdapter;
    private IDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCreateTaskButton();
        dataSource = new SharedPreferenceDataSource(this);
        initTaskRecycler();
    }

    private void initTaskRecycler(){
        taskRecyclerView = (RecyclerView) findViewById(R.id.taskRecyclerView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        taskAdapter = new TaskAdapter(dataSource.getTaskList());
        taskRecyclerView.setAdapter(taskAdapter);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (ActivityRequest.values()[requestCode]){
            case CREATE_TASK:
                if(resultCode == Activity.RESULT_OK){
                    Task task = data.getParcelableExtra(BundleKey.TASK.name());
                    if(task != null){
                        dataSource.create(task);
                        taskAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }
}

