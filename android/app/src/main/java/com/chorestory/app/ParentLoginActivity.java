package com.chorestory.app;

import android.accounts.AccountManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//import android.widget.Toast;

import com.chorestory.helpers.TokenHandler;
import com.chorestory.templates.LoginRequest;
import com.chorestory.templates.LoginResponse;
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

    private String username;
    private String password;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Toast toast;
    private Button logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_parent_login);

        usernameEditText = findViewById(R.id.username_edit_text);
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
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();

                LoginRequest loginRequest = new LoginRequest(username, password);
                Call<LoginResponse> loginQuery = retrofitInterface.login(loginRequest);

                loginQuery.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            String token = response.body().getToken();

                            // TODO: Store the token properly here
                            tokenHandler.setToken(token);

                            // TODO: Navigate to profile page
                        } else{
                            // TODO: use Snackbar instead; move existing view up when Snackbar appears
                            toast = Toast.makeText(ParentLoginActivity.this,
                                    "Invalid username or password",
                                    Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 400);
                            toast.show();
                            logInButton.setEnabled(true);
                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        toast.show();
                        logInButton.setEnabled(true);
                    }
                });
            }
        });
    }
}
