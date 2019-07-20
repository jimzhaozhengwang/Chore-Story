package com.chorestory.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.chorestory.R;
import com.chorestory.helpers.Toaster;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.chorestory.services.NotificationService;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;

public class MainActivity extends ChoreStoryActivity {

    final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);

        setContentView(R.layout.activity_main);

        // Register Notification Channel
        NotificationService notificationService = new NotificationService();
        notificationService.createNotificationChannel(getBaseContext(), getString(R.string.notification_channel_id));
        String token = FirebaseInstanceId.getInstance().getToken();
        notificationService.sendRegistrationToServer(token);

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

                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE &&
                resultCode == CommonStatusCodes.SUCCESS &&
                data != null) {

            Barcode barCode = data.getParcelableExtra(getString(R.string.qr_code));

            if (barCode != null) {
                Toaster.showToast(this, barCode.displayValue); // TODO: remove this line

                // TODO: if QR code is for child sign up
//                navigateTo(ChildJoinClanActivity.class);

                // TODO: if QR code is for child login
//                navigateTo(ChildHomeActivity.class);
            } else {
                Toaster.showToast(this, this.getString(R.string.unable_to_detect_qr_code));
            }
        } else {
            Toaster.showToast(this, this.getString(R.string.unable_to_detect_qr_code));
        }
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
