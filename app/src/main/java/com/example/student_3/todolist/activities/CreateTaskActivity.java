package com.example.student_3.todolist.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.student_3.todolist.BundleKey;
import com.example.student_3.todolist.R;
import com.example.student_3.todolist.Task;
import com.example.student_3.todolist.validators.Validator;

public class CreateTaskActivity extends AppCompatActivity {

    private Task task;
    private TextInputLayout nameWrapper;
    private EditText nameEditText;
    private TextInputLayout descriptionWrapper;
    private EditText descriptionEditText;
    private Validator stringValidator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey(BundleKey.TASK.name())){
            task = bundle.getParcelable(BundleKey.TASK.name());
            initUI();
            setData();
        } else{
            Toast.makeText(getApplicationContext(), "Task not found", Toast.LENGTH_LONG).show();
            finish();
        }
        stringValidator = new Validator.StringValidatorBuilder().setNotEmpty().build();
    }

    private void initUI(){
        nameWrapper = (TextInputLayout) findViewById(R.id.nameWrapper);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        descriptionWrapper = (TextInputLayout) findViewById(R.id.descriptionWrapper);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
    }

    private void setData(){
        nameEditText.setText(task.getName());
        descriptionEditText.setText(task.getDescription());
    }

    private void fillData(){
        task.setName(nameEditText.getText().toString());
        task.setDescription(descriptionEditText.getText().toString());
    }

    private boolean validate(TextInputLayout wrapper){
        wrapper.setErrorEnabled(false);
        boolean result = stringValidator.validate(wrapper.getEditText().getText().toString(), wrapper.getHint().toString());
        if (!result) {
            wrapper.setErrorEnabled(true);
            wrapper.setError(stringValidator.getLastMessage());
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_save:
                saveTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveTask(){
        fillData();
        Intent result = new Intent();
        result.putExtra(BundleKey.TASK.name(), task);
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
