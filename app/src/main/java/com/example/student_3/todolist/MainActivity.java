package com.example.student_3.todolist;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton createTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCreateTaskButton();
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
                }
                break;
        }
    }
}

@Override
public void writeToParcel(Parcel dest, int flags){
    super.writeToParcel(dest, flags);
    dest.writeString(this.name);
    dest.writeLong(this.expireDate != null ? this.expireDate.getTime() : -1);
    dest.writeTypedList(this.subTasksList);
}

protected Task(Parcel in){
    super(in);
    this.name = in.readString();
    long tmpExpireDate = in.readLong();
    this.expireDate = tmpExpireDate == -1 ? null : new Date(tmpExpireDate);
    this.subTasksList = in.createTypedArrayList(SubTask.CREATOR);
}

public static final Creator<Task> CREATOR = new Creator<Task>(){
    @Override
    public Task createFromParcel(Parcel source){
        return new Task(source);
    }

    @Override
    public Task[] new Array(int size){
        return new Task[size]
    }
}
