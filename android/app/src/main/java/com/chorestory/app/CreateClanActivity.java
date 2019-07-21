package com.chorestory.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chorestory.Interface.RetrofitInterface;
import com.chorestory.R;
import com.chorestory.helpers.Toaster;
import com.chorestory.helpers.TokenHandler;
import com.chorestory.templates.RegisterRequest;
import com.chorestory.templates.SingleResponse;

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

    private String email;
    private String clanName;
    private String name;
    private String password;
    private int picture;

    private EditText emailEditText;
    private EditText clanNameEditText;
    private EditText usernameEditText;
    private EditText nameEditText;
    private EditText passwordEditText;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_create_clan);

        emailEditText = findViewById(R.id.email_edit_text);

        clanNameEditText = findViewById(R.id.clan_name_edit_text);

        usernameEditText = findViewById(R.id.username_edit_text);
        usernameEditText.setVisibility(View.GONE);

        nameEditText = findViewById(R.id.name_edit_text);

        passwordEditText = findViewById(R.id.password_edit_text);

        signUpButton = findViewById(R.id.sign_up_button);
        buttons = Collections.singletonList(signUpButton);

        enableButtons();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                disableButtons();
                email = emailEditText.getText().toString();
                clanName = clanNameEditText.getText().toString();
                name = nameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                picture = R.drawable.king_color; // TODO: Let the user select this when the option is created

                if (email.isEmpty() || clanName.isEmpty() || name.isEmpty() || password.isEmpty()) {
                    enableButtons();
                    Toaster.showToast(getApplicationContext(), "Missing sign up information!");
                } else {
                    RegisterRequest registerRequest = new RegisterRequest(email, clanName, name, password, picture);
                    Call<SingleResponse<String>> registerQuery = retrofitInterface.register(registerRequest);

                    registerQuery.enqueue(new Callback<SingleResponse<String>>() {
                        @Override
                        public void onResponse(Call<SingleResponse<String>> call, Response<SingleResponse<String>> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().hasResponse()) {
                                String token = response.body().getData();

                                tokenHandler.setParentToken(token, getApplicationContext());

                                // TODO: verification
                                //  if successful register new clan & user, else display snackbar/toast

                                navigateTo(ParentHomeActivity.class,
                                        getResources().getString(R.string.clan_name),
                                        clanName);
                            } else {
                                // TODO: use Snackbar instead; move existing view up when Snackbar appears
                                Toaster.showToast(getApplicationContext(), "Something went wrong!");
                                enableButtons();
                            }
                        }

                        @Override
                        public void onFailure(Call<SingleResponse<String>> call, Throwable t) {
                            Toaster.showToast(getApplicationContext(), "Something went wrong!");
                            enableButtons();
                        }
                    });
                }
            }
        });
    }
}
