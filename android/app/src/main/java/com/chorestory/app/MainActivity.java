package com.chorestory.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chorestory.R;

public class MainActivity extends AppCompatActivity {

    private Button parentGuardianButton;
    private Button childButton;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_main);

        parentGuardianButton = findViewById(R.id.parent_guardian_button);
        childButton = findViewById(R.id.child_button);

        enableButtons();

        parentGuardianButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                navigateToParentSignUp();
            }
        });

        childButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: finish childButton's onClickListener
                //disableButtons();
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
        parentGuardianButton.setEnabled(false);
        childButton.setEnabled(false);
    }

    private void enableButtons() {
        parentGuardianButton.setEnabled(true);
        childButton.setEnabled(true);
    }

    private void navigateToParentSignUp() {
        intent = new Intent(this, ParentLoginSignUpActivity.class);
        startActivity(intent);
    }
}
