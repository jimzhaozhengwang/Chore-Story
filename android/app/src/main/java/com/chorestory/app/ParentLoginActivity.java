package com.chorestory.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.Toast;

import com.chorestory.R;

import java.util.Collections;

public class ParentLoginActivity extends ChoreStoryActivity {

    private String username;
    private String password;
    private EditText usernameEditText;
    private EditText passwordEditText;
//    private Toast toast;
    private Button logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_parent_login);

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        logInButton = findViewById(R.id.log_in_button);
        buttons = Collections.singletonList(logInButton);

        enableButtons();

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                disableButtons();
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();

                // TODO: Authenticate user

                // TODO: if authentication successful, navigate to app homepage

                // TODO: if authentication unsuccessful, enable loginButton, display toast
                // TODO: use Snackbar instead; move existing view up when Snackbar appears
//                toast = Toast.makeText(ParentLoginActivity.this,
//                        "Invalid username or password",
//                        Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 400);
//                toast.show();
//                logInButton.setEnabled(true);
            }
        });
    }
}
