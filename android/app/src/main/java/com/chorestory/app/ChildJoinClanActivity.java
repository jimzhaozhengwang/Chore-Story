package com.chorestory.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chorestory.R;
import com.chorestory.helpers.Toaster;

import java.util.Collections;

public class ChildJoinClanActivity extends ChoreStoryActivity {

    private TextView welcomeTextView;

    private String username;
    private String name;

    private EditText emailEditText;
    private EditText clanNameEditText;
    private EditText usernameEditText;
    private EditText nameEditText;
    private EditText passwordEditText;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_child_join_clan);

        welcomeTextView = findViewById(R.id.welcome_text_view);
        welcomeTextView.setText("Welcome to the CS449 Clan!"); // TODO: fetch clan name

        emailEditText = findViewById(R.id.email_edit_text);
        emailEditText.setVisibility(View.GONE);

        clanNameEditText = findViewById(R.id.clan_name_edit_text);
        clanNameEditText.setVisibility(View.GONE);

        usernameEditText = findViewById(R.id.username_edit_text);

        nameEditText = findViewById(R.id.name_edit_text);

        passwordEditText = findViewById(R.id.password_edit_text);
        passwordEditText.setVisibility(TextView.GONE);

        signUpButton = findViewById(R.id.sign_up_button);
        buttons = Collections.singletonList(signUpButton);
        enableButtons();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                disableButtons();
                username = usernameEditText.getText().toString();
                name = nameEditText.getText().toString();

                if (username.isEmpty() || name.isEmpty()) {
                    enableButtons();
                    // TODO: think of message
                    Toaster.showToast(getApplicationContext(), "Missing sign up information!");
                } else {
                    // TODO: create child account
                    navigateTo(ChildHomeActivity.class);
                }
            }
        });
    }
}
