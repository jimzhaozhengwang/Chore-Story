package com.chorestory.app;

import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chorestory.helpers.TokenHandler;
import com.chorestory.helpers.Toaster;
import com.chorestory.templates.AccountResponse;
import com.chorestory.templates.LoginRequest;
import com.chorestory.templates.SingleResponse;
import com.chorestory.Interface.RetrofitInterface;
import com.chorestory.R;

import java.util.Collections;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentLoginActivity extends ChoreStoryActivity {

    @Inject
    RetrofitInterface retrofitInterface;
    @Inject
    TokenHandler tokenHandler;

    private String email;
    private String password;
    
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_parent_login);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        logInButton = findViewById(R.id.log_in_button);
        buttons = Collections.singletonList(logInButton);

        enableButtons();

        AccountManager accountManager = AccountManager.get(this);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                disableButtons();
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();

                LoginRequest loginRequest = new LoginRequest(email, password);
                Call<SingleResponse<String>> loginQuery = retrofitInterface.login(loginRequest);

                loginQuery.enqueue(new Callback<SingleResponse<String>>() {
                    @Override
                    public void onResponse(Call<SingleResponse<String>> call, Response<SingleResponse<String>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().hasResponse()) {
                            String token = response.body().getData();

                            Context ctx = getApplicationContext();

                            tokenHandler.setParentToken(token, ctx);

                            Call<AccountResponse> accountQuery = retrofitInterface.me(tokenHandler.getToken(ctx));

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
                                    Toaster.showToast(ParentLoginActivity.this, "Sign-in with token failed.");
                                    enableButtons();
                                }
                            });

                        } else {
                            // TODO: use Snackbar instead; move existing view up when Snackbar appears
                            Toaster.showToast(ParentLoginActivity.this, "Invalid email or password");
                            enableButtons();
                        }
                    }

                    @Override
                    public void onFailure(Call<SingleResponse<String>> call, Throwable t) {
                        Toaster.showToast(ParentLoginActivity.this, "Something went wrong!");
                        enableButtons();
                    }
                });
            }
        });
    }
}
