package com.example.student_3.todolist.activities;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student_3.todolist.ActivityRequest;
import com.example.student_3.todolist.BundleKey;
import com.example.student_3.todolist.R;
import com.example.student_3.todolist.dialogs.DatePickerFragment;
import com.example.student_3.todolist.models.Category;
import com.example.student_3.todolist.models.Task;
import com.example.student_3.todolist.validators.Validator;

import java.util.Date;

public class CreateTaskActivity extends AppCompatActivity implements DatePickerFragment.OnDateSelectedListener {

    private Task task;
    private TextInputLayout nameWrapper;
    private EditText nameEditText;
    private TextInputLayout descriptionWrapper;
    private EditText descriptionEditText;
    private Validator stringValidator;
    private TextView dateTextView;
    private TextView categoryTextView;


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

    public void showDatePickerDialog(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void openCategoryActivity(View v){
        Intent intent = new Intent(CreateTaskActivity.this, CategoryActivity.class);
        intent.putExtra("key", ActivityRequest.GET_CATEGORY.name());
        startActivityForResult(intent, ActivityRequest.GET_CATEGORY.ordinal());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null) {
            Toast.makeText(this, "Wrong category", Toast.LENGTH_SHORT).show();
        }else{
            String categoryTitle = data.getStringExtra("category");
            categoryTextView.setText(categoryTitle);
        }
    }

    @Override
    public void onDateSelected(Date date) {
        task.setExpireDate(date);
        dateTextView.setText(task.getExpireDateString());
    }

    private void initUI(){
        nameWrapper = (TextInputLayout) findViewById(R.id.nameWrapper);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        descriptionWrapper = (TextInputLayout) findViewById(R.id.descriptionWrapper);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        categoryTextView = (TextView) findViewById(R.id.categoryTextView);
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
                if(validate(nameWrapper) && validate(descriptionWrapper)) {
                    saveTask();
                }
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
