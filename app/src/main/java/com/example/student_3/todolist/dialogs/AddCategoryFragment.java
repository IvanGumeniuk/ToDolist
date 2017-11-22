package com.example.student_3.todolist.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.student_3.todolist.R;

/**
 * Created by gromi on 11/22/2017.
 */

public class AddCategoryFragment extends DialogFragment {

    private TextInputLayout textInputLayout;
    private EditText categoryNameEditText;
    private Button categoryCreateButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_dialog_create_category, container, false);
        textInputLayout = view.findViewById(R.id.categoryNameWrapper);
        categoryNameEditText = view.findViewById(R.id.categoryNameEditText);
        categoryCreateButton = view.findViewById(R.id.categoryCreateButton);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
