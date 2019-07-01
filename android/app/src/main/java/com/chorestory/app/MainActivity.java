package com.chorestory.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chorestory.R;

import java.util.Arrays;

public class MainActivity extends ChoreStoryActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_main);

        Button parentGuardianButton = findViewById(R.id.parent_guardian_button);
        Button childButton = findViewById(R.id.child_button);
        buttons = Arrays.asList(parentGuardianButton, childButton);

        enableButtons();

        parentGuardianButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                navigateTo(ParentLoginSignUpActivity.class);
            }
        });

        childButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                // TODO: open camera to scan QR code

                // if QR code is for child sign up
                navigateTo(ChildJoinClanActivity.class);

                // if QR code is for child login
//                navigateTo(ChildHomeActivity.class);
            }
        });
    }
}
