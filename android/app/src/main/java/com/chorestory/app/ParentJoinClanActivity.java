package com.chorestory.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import static java.security.AccessController.getContext;

public class ParentJoinClanActivity extends ChoreStoryActivity {

    private String email;
    private String name;
    private String password;
    private int picture;

    private TextView welcomeTextView;
    private EditText emailEditText;
    private EditText clanNameEditText;
    private EditText usernameEditText;
    private EditText nameEditText;
    private EditText passwordEditText;
    private Button signUpButton;

    @Inject
    TokenHandler tokenHandler;
    @Inject
    RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_parent_join_clan);

        welcomeTextView = findViewById(R.id.welcome_text_view);
        String welcomeText = getString(R.string.sign_up_to_join_your_clan);
        welcomeTextView.setText(welcomeText);

        emailEditText = findViewById(R.id.email_edit_text);

        clanNameEditText = findViewById(R.id.clan_name_edit_text);
        clanNameEditText.setVisibility(View.GONE);

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
                name = nameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                picture = R.drawable.king_color; // TODO: Let the user select this when the option is created

                if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
                    enableButtons();
                    Toaster.showToast(getApplicationContext(), "Missing sign up information!");
                } else {

                    if (tokenHandler.hasParentRegistrationToken()) {

                        RegisterRequest registerRequest = new RegisterRequest(
                                email,
                                "",
                                name,
                                password,
                                picture
                        );

                        String parentRegistrationToken = tokenHandler.getParentRegistrationToken();
                        Call<SingleResponse<String>> parentRegistrationQuery = retrofitInterface.register_co_parent(
                                parentRegistrationToken,
                                registerRequest);
                        parentRegistrationQuery.enqueue(new Callback<SingleResponse<String>>() {
                            @Override
                            public void onResponse(Call<SingleResponse<String>> call, Response<SingleResponse<String>> response) {
                                if(response.isSuccessful() && response.body() != null && response.body().hasResponse()) {
                                    String parentToken = response.body().getData();

                                    tokenHandler.setParentToken(parentToken, getApplicationContext());

                                    navigateTo(ParentHomeActivity.class);
                                }
                            }

                            @Override
                            public void onFailure(Call<SingleResponse<String>> call, Throwable t) {
                                Toaster.showToast(getApplicationContext(), "Internal error occurred.");
                            }
                        });

                    } else {
                        deleteTokenNavigateMain(getApplicationContext());
                    }
                }
            }
        });
    }
}
