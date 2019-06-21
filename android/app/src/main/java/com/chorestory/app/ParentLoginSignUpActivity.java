package com.chorestory.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chorestory.R;

public class ParentLoginSignUpActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signUpButton;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login_sign_up);
        App.getAppComponent().inject(this);

        loginButton = findViewById(R.id.login_button);
        signUpButton = findViewById(R.id.sign_up_button);

        enableButtons();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                navigateToParentLogin();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                navigateToParentSignUp();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableButtons();
    }

    // prevent users from spam clicking buttons
    private void disableButtons() {
        loginButton.setEnabled(false);
        signUpButton.setEnabled(false);
    }

    private void enableButtons() {
        loginButton.setEnabled(true);
        signUpButton.setEnabled(true);
    }

    private void navigateToParentLogin() {
        intent = new Intent(this, ParentLoginActivity.class);
        startActivity(intent);
    }

    private void navigateToParentSignUp(){
        intent = new Intent(this, ParentSignUpActivity.class);
        startActivity(intent);
    }
}
