package com.chorestory.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chorestory.R;

public class ParentSignUpActivity extends AppCompatActivity {

    private Button createNewClanButton;
    private Button joinExistingClanButton;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_parent_sign_up);

        createNewClanButton = findViewById(R.id.create_new_clan_button);
        joinExistingClanButton = findViewById(R.id.join_existing_clan_button);

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

    @Override
    protected void onResume() {
        super.onResume();
        enableButtons();
    }

    // prevent users from spam clicking buttons
    private void disableButtons() {
        createNewClanButton.setEnabled(false);
        joinExistingClanButton.setEnabled(false);
    }

    private void enableButtons() {
        createNewClanButton.setEnabled(true);
        joinExistingClanButton.setEnabled(true);
    }
}
