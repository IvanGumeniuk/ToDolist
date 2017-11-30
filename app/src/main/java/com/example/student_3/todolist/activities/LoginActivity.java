package com.example.student_3.todolist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.student_3.todolist.BundleKey;
import com.example.student_3.todolist.R;
import com.example.student_3.todolist.data.FilesDataSource;
import com.example.student_3.todolist.data.IDataSource;
import com.example.student_3.todolist.data.SharedPreferenceDataSource;
import com.example.student_3.todolist.fragments.LoginFragment;
import com.example.student_3.todolist.fragments.UserEmailFragment;
import com.example.student_3.todolist.fragments.UserNameFragment;
import com.example.student_3.todolist.fragments.UserPinFragment;
import com.example.student_3.todolist.fragments.UserWelcomeFragment;
import com.example.student_3.todolist.models.User;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements LoginFragment.NeedRegistrationListener,
        UserNameFragment.GetNameFromFragment, UserEmailFragment.GetEmailFromFragment, UserPinFragment.GetPinFromFragment,
        UserWelcomeFragment.RunMainActivity {

    private static final String CURRENT = "current";
    public static boolean NEED_REFRESH_PIN = true;

    private User currentUser;
    private IDataSource dataSource;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        intent = getIntent();

        if (savedInstanceState == null && !intent.getBooleanExtra(BundleKey.NEED_CHECK_PASSWORD.name(), false)) {
            LoginFragment loginFragment = new LoginFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userFragmentContainer, loginFragment)
                    .commit();
            NEED_REFRESH_PIN = true;
        }

        if (intent.getBooleanExtra(BundleKey.NEED_CHECK_PASSWORD.name(), false)&& NEED_REFRESH_PIN) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_rigth,R.anim.slide_in_left, R.anim.slide_out_rigth)
                    .replace(R.id.userFragmentContainer, new UserPinFragment())
                    .commit();
            NEED_REFRESH_PIN = false;
        }

        dataSource = new SharedPreferenceDataSource(this);
        dataSource.setCurrentUser(new User());
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT, getCurrentUser());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentUser = savedInstanceState.getParcelable(CURRENT);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public ArrayList<User> getUsersFromData() {
        return dataSource.getUserList();
    }

    @Override
    public void needRegistration(boolean needRegistration, int userCount) {
        if (needRegistration) {
            currentUser = new User();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_rigth,
                            R.anim.slide_in_left, R.anim.slide_out_rigth)
                    .replace(R.id.userFragmentContainer, new UserNameFragment())
                    .addToBackStack("UserName")
                    .commit();
        } else {
            dataSource.setCurrentUser(getUsersFromData().get(userCount));

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void getName(String name) {
        currentUser.setName(name);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_rigth,
                        R.anim.slide_in_left, R.anim.slide_out_rigth)
                .replace(R.id.userFragmentContainer, new UserEmailFragment())
                .addToBackStack("UserMail")
                .commit();
    }

    @Override
    public void getEmail(String email) {
        currentUser.setEmail(email);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_rigth)
                .replace(R.id.userFragmentContainer, new UserPinFragment())
                .addToBackStack("UserPin")
                .commit();
    }

    @Override
    public void getPin(String pin) {
        if (intent.getBooleanExtra(BundleKey.NEED_CHECK_PASSWORD.name(), false)) {
            if (pin.equals(dataSource.getCurrentUser().getPin())) {
                setResult(Activity.RESULT_OK);
                NEED_REFRESH_PIN = true;
                finish();
            } else {
                Toast.makeText(this, "Wrong pin", Toast.LENGTH_SHORT).show();
            }
        } else {
            currentUser.setPin(pin);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_rigth)
                    .replace(R.id.userFragmentContainer, new UserWelcomeFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void runMainActivity() {
        dataSource.addUser(currentUser);
        dataSource.setCurrentUser(currentUser);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
