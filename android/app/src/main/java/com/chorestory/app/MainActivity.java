package com.chorestory.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chorestory.Interface.RetrofitInterface;
import com.chorestory.R;
import com.chorestory.helpers.Toaster;
import com.chorestory.helpers.TokenHandler;
import com.chorestory.templates.AccountResponse;

import java.util.Arrays;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends ChoreStoryActivity {

    @Inject
    RetrofitInterface retrofitInterface;
    @Inject
    TokenHandler tokenHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);

        // TODO: Move this to a splash screen to be more seamless
        String token = tokenHandler.getToken(getApplicationContext());
        if (token != null) {
            if (tokenHandler.isParentToken(token)) {
                Call<AccountResponse> accountQuery = retrofitInterface.me(token);
                accountQuery.enqueue(new Callback<AccountResponse>() {
                    @Override
                    public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                        // TODO: Navigate to profile page
                        navigateTo(ParentHomeActivity.class,
                                getResources().getString(R.string.clan_name),
                                response.body().getData().getClanName());
                    }

                    @Override
                    public void onFailure(Call<AccountResponse> call, Throwable t) {
                        Toaster.showToast(MainActivity.this, "Sign-in with token failed.");
                        enableButtons();
                    }
                });
            } else {
                // TODO: redirect to child homepage
            }
        }

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
                // TODO: finish childButton's onClickListener
                //disableButtons();
            }
        });
    }
}
