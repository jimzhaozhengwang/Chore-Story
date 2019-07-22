package com.chorestory.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.chorestory.R;
import com.chorestory.helpers.Toaster;
import com.chorestory.helpers.TokenHandler;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.Arrays;

import javax.inject.Inject;

public class ParentSignUpActivity extends ChoreStoryActivity {

    private final int REQUEST_CODE = 100;

    private Button createNewClanButton;
    private Button joinExistingClanButton;

    @Inject
    TokenHandler tokenHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_parent_sign_up);

        createNewClanButton = findViewById(R.id.create_new_clan_button);
        joinExistingClanButton = findViewById(R.id.join_existing_clan_button);
        buttons = Arrays.asList(createNewClanButton, joinExistingClanButton);

        enableButtons();

        createNewClanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                navigateTo(ParentCreateClanActivity.class);
            }
        });

        joinExistingClanButton.setOnClickListener(new View.OnClickListener() {
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
//        if (requestCode == REQUEST_CODE &&
//                resultCode == CommonStatusCodes.SUCCESS &&
//                data != null) {
//
//            Barcode barCode = data.getParcelableExtra(getString(R.string.qr_code));
//
//            if (barCode != null && !barCode.displayValue.isEmpty()) {
//
//                if (barCode.displayValue.endsWith(getString(R.string.parent_register_identifier))) {

                    // The token can be used by the child to login
                    tokenHandler.setParentRegistrationToken(
                            "31f056a8-d760-491d-abd1-31b3e87dd90e:parentRegister".replace(getString(R.string.parent_register_identifier), ""));
                    navigateTo(ParentJoinClanActivity.class);

//                }
//
//            } else {
//                Toaster.showToast(this, this.getString(R.string.unable_to_detect_qr_code));
//            }
//        } else {
//            Toaster.showToast(this, this.getString(R.string.unable_to_detect_qr_code));
//        }
    }
}
