package com.chorestory.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chorestory.R;
import com.chorestory.helpers.Toaster;

import java.util.Collections;

public class ParentJoinClanActivity extends ChoreStoryActivity {

    private String email;
    private String name;
    private String password;
    private int picture;

    private TextView welcomeTextView;
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
        setContentView(R.layout.activity_parent_join_clan);

        welcomeTextView = findViewById(R.id.welcome_text_view);
        String welcomeText = getString(R.string.welcome_to_the) +
                " " + "CS449" + " " +  // TODO: fetch clan name
                getString(R.string.clan) + "!";
        welcomeTextView.setText(welcomeText);

        emailEditText = findViewById(R.id.email_edit_text);

        clanNameEditText = findViewById(R.id.clan_name_edit_text);
        clanNameEditText.setVisibility(View.GONE);

        usernameEditText = findViewById(R.id.username_edit_text);
        usernameEditText.setVisibility(View.GONE);

        nameEditText = findViewById(R.id.name_edit_text);

        passwordEditText = findViewById(R.id.password_edit_text);

        signUpButton = findViewById(R.id.sign_up_button);

        buttons = Collections.singletonList(signUpButton);

        enableButtons();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                disableButtons();
                email = emailEditText.getText().toString();
                name = nameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                picture = R.drawable.king_color; // TODO: Let the user select this when the option is created

                if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
                    enableButtons();
                    Toaster.showToast(getApplicationContext(), "Missing sign up information!");
                } else {
                    // TODO cristian: make request, refer to ParentCreateClanActivity

                    // TODO cristian: make sure ParentHome shows the clan name
                    navigateTo(ParentHomeActivity.class);
                }
            }
        });
    }
}
