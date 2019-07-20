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
import com.chorestory.app.QrCodeActivity;
import com.chorestory.helpers.Toaster;
import com.chorestory.helpers.TokenHandler;
import com.chorestory.templates.AccountResponse;

import java.util.Arrays;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentProfileFragment extends ChoreStoryFragment {

    ImageView parentImageView;
    TextView parentNameTextView;
    TextView clanNameTextView;
    Button addParentGuardianButton;
    Button addChildButton;
    TextView parentEmailTextView;
    Button logoutButton;

    @Inject
    RetrofitInterface retrofitInterface;
    @Inject
    TokenHandler tokenHandler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_profile, container, false);
        App.getAppComponent().inject(this);

        parentImageView = view.findViewById(R.id.parent_image_view);
        parentNameTextView = view.findViewById(R.id.parent_name_text_view);
        clanNameTextView = view.findViewById(R.id.clan_name_text_view);
        addParentGuardianButton = view.findViewById(R.id.add_parent_guardian_button);
        addChildButton = view.findViewById(R.id.add_child_button);
        parentEmailTextView = view.findViewById(R.id.parent_email_text_view);
        logoutButton = view.findViewById(R.id.logout_parent_button);

        views = Arrays.asList(addParentGuardianButton, addChildButton);
        enableViews();

        // TODO: need to add a child selection dropdown somewhere here
        //          so we can generate the child login token for the qr code

        addParentGuardianButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViews();
                navigateTo(getContext(),
                        QrCodeActivity.class,
                        getString(R.string.qr_code),
                        getString(R.string.parent_guardian));
            }
        });

        addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViews();
                navigateTo(getContext(),
                        QrCodeActivity.class,
                        getString(R.string.qr_code),
                        getString(R.string.child));
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete the token we have stored and redirect the user to the login page
                tokenHandler.deleteStoredToken(getContext());
                navigateTo(getContext(), MainActivity.class);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        String token = tokenHandler.getToken(getContext());
        if (token != null) {
            if (tokenHandler.isParentToken(token)) {
                Call<AccountResponse> accountQuery = retrofitInterface.me(token);
                accountQuery.enqueue(new Callback<AccountResponse>() {
                    @Override
                    public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().hasResponse()) {

                            AccountResponse.Data respData = response.body().getData();

                            // Set data for page
                            parentNameTextView.setText(respData.getName());
                            clanNameTextView.setText(respData.getClanName());
                            parentEmailTextView.setText(respData.getEmail());
                            parentImageView.setImageResource(R.drawable.king_color); // TODO: get image id (currently not being set)
                        } else {
                            // delete the token we have stored and redirect the user to the login page
                            tokenHandler.deleteStoredToken(getContext());
                            navigateTo(getContext(), MainActivity.class);
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountResponse> call, Throwable t) {
                        Toaster.showToast(getContext(), "Internal error occurred.");

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
}
