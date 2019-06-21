package com.chorestory.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chorestory.R;

public class ParentLoginActivity extends AppCompatActivity {

    private String username;
    private String password;
    private Button logInButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Toast toast;
    private Intent intent;

    InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_parent_login);

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);

        logInButton = findViewById(R.id.log_in_button);
        logInButton.setEnabled(true);

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                logInButton.setEnabled(false);
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

    @Override
    protected void onResume() {
        super.onResume();
        logInButton.setEnabled(true);
    }

    private void hideKeyBoard() {
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
