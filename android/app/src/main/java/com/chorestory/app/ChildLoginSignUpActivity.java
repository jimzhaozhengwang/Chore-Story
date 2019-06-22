package com.chorestory.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chorestory.R;

import java.util.Arrays;

public class ChildLoginSignUpActivity extends ChoreStoryActivity {

    private Button loginButton;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_child_login_sign_up);

        loginButton = findViewById(R.id.login_button);
        signUpButton = findViewById(R.id.sign_up_button);
        buttons = Arrays.asList(loginButton, signUpButton);

        enableButtons();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                // TODO: open camera
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                // TODO: open camera
            }
        });
    }
}
