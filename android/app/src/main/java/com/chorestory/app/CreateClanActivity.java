package com.chorestory.app;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chorestory.Interface.RetrofitInterface;
import com.chorestory.R;
import com.chorestory.helpers.TokenHandler;
import com.chorestory.templates.LoginRequest;
import com.chorestory.templates.LoginResponse;
import com.chorestory.templates.RegisterRequest;

import java.util.Collections;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateClanActivity extends ChoreStoryActivity {

    @Inject
    RetrofitInterface retrofitInterface;
    @Inject
    TokenHandler tokenHandler;

    private String clanName;
    private String username;
    private String password;

    private EditText clanNameEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button signUpButton;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_create_clan);

        clanNameEditText = findViewById(R.id.clan_name_edit_text);
        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);

        signUpButton = findViewById(R.id.sign_up_button);
        buttons = Collections.singletonList(signUpButton);

        enableButtons();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                disableButtons();
                clanName = clanNameEditText.getText().toString();
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();

                RegisterRequest registerRequest = new RegisterRequest(clanName, username, password);
                Call<LoginResponse> registerQuery = retrofitInterface.register(registerRequest);

                registerQuery.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().hasToken()) {
                            String token = response.body().getToken();

                            // TODO: Store the token properly here
                            tokenHandler.setParentToken(token);

                            // TODO: Navigate to profile page
                        } else {
                            // TODO: use Snackbar instead; move existing view up when Snackbar appears
                            toast = Toast.makeText(CreateClanActivity.this,
                                    "Something went wrong!",
                                    Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 400);
                            toast.show();
                            signUpButton.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        toast = Toast.makeText(CreateClanActivity.this,
                                "Something went wrong!",
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 400);
                        toast.show();
                        signUpButton.setEnabled(true);
                    }
                });
            }
        });
    }
}
