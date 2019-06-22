package com.chorestory.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chorestory.R;

import java.util.Arrays;

public class ParentSignUpActivity extends ChoreStoryActivity {

    private Button createNewClanButton;
    private Button joinExistingClanButton;

    ParentSignUpActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_parent_sign_up);

        createNewClanButton = findViewById(R.id.create_new_clan_button);
        joinExistingClanButton = findViewById(R.id.join_existing_clan_button);
        this.buttons = Arrays.asList(createNewClanButton, joinExistingClanButton);

        enableButtons();

        createNewClanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                // TODO: navigate to create new clan activity
            }
        });

        joinExistingClanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                // TODO: navigate to join existing clan activity
            }
        });
    }
}
