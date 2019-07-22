package com.chorestory.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chorestory.Interface.RetrofitInterface;
import com.chorestory.R;
import com.chorestory.helpers.Toaster;
import com.chorestory.helpers.TokenHandler;
import com.chorestory.templates.ChildRequest;
import com.chorestory.templates.SingleResponse;

import java.util.Collections;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildJoinClanActivity extends ChoreStoryActivity {

    @Inject
    TokenHandler tokenHandler;
    @Inject
    RetrofitInterface retrofitInterface;

    private TextView welcomeTextView;

    private String username;
    private String name;
    private int picture;

    private EditText usernameEditText;
    private EditText nameEditText;
    private ImageView jokerImageView;
    private ImageView kightImageView;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_child_join_clan);

        welcomeTextView = findViewById(R.id.welcome_text_view);
        welcomeTextView.setText(getString(R.string.sign_up_to_join_your_clan));

        usernameEditText = findViewById(R.id.username_edit_text);

        nameEditText = findViewById(R.id.name_edit_text);

        picture = R.drawable.joker_color;
        jokerImageView = findViewById(R.id.avatar_joker);
        kightImageView = findViewById(R.id.avatar_knight);
        jokerImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                jokerImageView.setImageResource(R.drawable.joker_color);
                kightImageView.setImageResource(R.drawable.knight_bw);
                picture = R.drawable.joker_color;
            }
        });
        kightImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                jokerImageView.setImageResource(R.drawable.joker_bw);
                kightImageView.setImageResource(R.drawable.knight_color);
                picture = R.drawable.knight_color;
            }
        });

        signUpButton = findViewById(R.id.sign_up_button);
        buttons = Collections.singletonList(signUpButton);
        enableButtons();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                disableButtons();
                username = usernameEditText.getText().toString();
                name = nameEditText.getText().toString();

                if (username.isEmpty() || name.isEmpty()) {
                    enableButtons();
                    // TODO: think of message
                    Toaster.showToast(ChildJoinClanActivity.this,
                            "Missing sign up information!");
                } else {
                    if (tokenHandler.hasChildCreationToken()) {

                        ChildRequest childRequest = new ChildRequest(name, username);

                        Call<SingleResponse<String>> childTokenQuery = retrofitInterface.create_child(
                                tokenHandler.getChildCreationToken(),
                                childRequest);
                        childTokenQuery.enqueue(new Callback<SingleResponse<String>>() {
                            @Override
                            public void onResponse(Call<SingleResponse<String>> call,
                                                   Response<SingleResponse<String>> response) {
                                if (response.isSuccessful() &&
                                        response.body() != null &&
                                        response.body().hasResponse()) {

                                    String childToken = response.body().getData();
                                    tokenHandler.setChildToken(childToken, getApplicationContext());

                                    navigateTo(ChildHomeActivity.class);
                                }
                            }

                            @Override
                            public void onFailure(Call<SingleResponse<String>> call, Throwable t) {
                                enableButtons();
                                Toaster.showToast(ChildJoinClanActivity.this,
                                        "Unable to create account!");
                            }
                        });
                    }
                    navigateTo(ChildHomeActivity.class);
                }
            }
        });
    }
}
