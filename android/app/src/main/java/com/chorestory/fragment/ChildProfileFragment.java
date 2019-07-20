package com.chorestory.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chorestory.Interface.RetrofitInterface;
import com.chorestory.R;
import com.chorestory.app.App;
import com.chorestory.app.MainActivity;
import com.chorestory.helpers.TokenHandler;
import com.chorestory.templates.ChildResponse;

import java.util.Locale;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildProfileFragment extends ChoreStoryFragment {

    ImageView childImageView;
    TextView childUsernameTextView;
    TextView childNameTextView;
    TextView childLevelTextView;
    TextView childExpTextView;
    Button logoutButton;

    @Inject
    TokenHandler tokenHandler;
    @Inject
    RetrofitInterface retrofitInterface;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_profile, container, false);
        App.getAppComponent().inject(this);

        childImageView = view.findViewById(R.id.child_image_view);
        childUsernameTextView = view.findViewById(R.id.child_username_text_view);
        childNameTextView = view.findViewById(R.id.child_name_text_view);
        childLevelTextView = view.findViewById(R.id.child_level_text_view);
        childExpTextView = view.findViewById(R.id.child_exp_text_view);
        logoutButton = view.findViewById(R.id.logout_child_button);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete the token we have stored and redirect the user to the login page
                tokenHandler.deleteStoredToken(getContext());
                navigateTo(getContext(), MainActivity.class);
            }
        });

        populateProfile();

        return view;
    }

    public void populateProfile() {

        String token = tokenHandler.getToken(getContext());
        if (token != null && tokenHandler.isChildToken(token)) {
            Call<ChildResponse> accountQuery = retrofitInterface.me_child(token);
            accountQuery.enqueue(new Callback<ChildResponse>() {
                @Override
                public void onResponse(Call<ChildResponse> call, Response<ChildResponse> response) {

                    if (response.isSuccessful() && response.body() != null && response.body().hasResponse()) {
                        ChildResponse.Data respData = response.body().getData();

                        childImageView.setImageResource(R.drawable.king_color); // TODO: get actual id once it's set
                        childUsernameTextView.setText(respData.getUsername());
                        childNameTextView.setText(respData.getName());
                        childLevelTextView.setText(String.format(Locale.CANADA, "%d", respData.getLevel()));
                        childExpTextView.setText(String.format(Locale.CANADA, "%d", respData.getExp()));

                    } else {
                        // delete the token we have stored and redirect the user to the login page
                        tokenHandler.deleteStoredToken(getContext());
                        navigateTo(getContext(), MainActivity.class);
                    }
                }

                @Override
                public void onFailure(Call<ChildResponse> call, Throwable t) {
                    // delete the token we have stored and redirect the user to the login page
                    tokenHandler.deleteStoredToken(getContext());
                    navigateTo(getContext(), MainActivity.class);
                }
            });
        } else {
            // delete the token we have stored and redirect the user to the login page
            tokenHandler.deleteStoredToken(getContext());
            navigateTo(getContext(), MainActivity.class);
        }
    }
}
