package com.chorestory.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chorestory.R;
import com.chorestory.services.NotificationService;

import java.util.Arrays;

public class MainActivity extends ChoreStoryActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_main);

        // Register Notifications Channel
        NotificationService notificationService = new NotificationService();
        notificationService.createNotificationChannel(getBaseContext(), getString(R.string.notification_channel_id));

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
                // TODO: finish childButton's onClickListener
                //disableButtons();
            }
        });
    }
}
