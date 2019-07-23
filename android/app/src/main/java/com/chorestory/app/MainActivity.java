package com.chorestory.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.chorestory.R;
import com.chorestory.helpers.Toaster;
import com.chorestory.helpers.TokenHandler;
import com.chorestory.services.NotificationService;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;

import javax.inject.Inject;

public class MainActivity extends ChoreStoryActivity {

    final int REQUEST_CODE = 100;

    @Inject
    TokenHandler tokenHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);

        setContentView(R.layout.activity_main);

        Button parentGuardianButton = findViewById(R.id.parent_guardian_button);
        Button childButton = findViewById(R.id.child_button);
        Button creditsButton = findViewById(R.id.credits_button);
        buttons = Arrays.asList(parentGuardianButton, childButton, creditsButton);

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

        creditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                navigateTo(CreditsActivity.class);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE &&
                resultCode == CommonStatusCodes.SUCCESS &&
                data != null) {

            Barcode barCode = data.getParcelableExtra(getString(R.string.qr_code));

            if (barCode != null && !barCode.displayValue.isEmpty()) {

                if (barCode.displayValue.endsWith(getString(R.string.child_login_identifier))) {

                    // The token can be used by the child to login
                    tokenHandler.setChildToken(
                            barCode.displayValue.replace(getString(R.string.child_login_identifier), ""),
                            this);
                    navigateTo(ChildHomeActivity.class);

                } else if (barCode.displayValue.endsWith(getString(R.string.child_register_identifier))) {

                    // The token will be used to register a child

                    // Send the child to join the clan that the token belongs to
                    tokenHandler.setChildCreationToken(
                            barCode.displayValue.replace(getString(R.string.child_register_identifier), ""));
                    navigateTo(ChildJoinClanActivity.class);

                }

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
