package com.chorestory.fragment;

import android.accounts.Account;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chorestory.Interface.RetrofitInterface;
import com.chorestory.R;
import com.chorestory.app.App;
import com.chorestory.helpers.Toaster;
import com.chorestory.helpers.TokenHandler;
import com.chorestory.templates.AccountResponse;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ParentProfileFragment extends Fragment {

    ImageView parentImageView;
    TextView parentNameTextView;
    TextView clanNameTextView;
    Button addMemberButton;
    Button leaveClanButton;
    TextView parentEmailTextView;
    Button changePasswordButton;

    @Inject
    RetrofitInterface retrofitInference;
    @Inject
    TokenHandler tokenHandler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_profile, container, false);

        App.getAppComponent().inject(this);

        parentImageView = view.findViewById(R.id.parent_image_view);
        parentNameTextView = view.findViewById(R.id.parent_name_text_view);
        clanNameTextView = view.findViewById(R.id.clan_name_text_view);
        addMemberButton = view.findViewById(R.id.add_member_button);
        leaveClanButton = view.findViewById(R.id.leave_clan_button);
        parentEmailTextView = view.findViewById(R.id.parent_email_text_view);
        changePasswordButton = view.findViewById(R.id.change_password_button);

        enableButtons();

        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                // TODO: add a member, camera QR code?
            }
        });

        leaveClanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                // TODO: determine appropriate behaviour
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                // TODO: change user password
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
                Call<AccountResponse> accountQuery = retrofitInference.me(token);
                accountQuery.enqueue(new Callback<AccountResponse>() {
                    @Override
                    public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().hasResponse()) {

                            AccountResponse.Data respData = response.body().getData();

                            // Set data for page
                            parentNameTextView.setText(respData.getName());
                            clanNameTextView.setText(respData.getClanName());
                            parentEmailTextView.setText(respData.getEmail());

                            parentImageView.setImageResource(R.drawable.king_color); // TODO: get image id

                        }
                    }

                    @Override
                    public void onFailure(Call<AccountResponse> call, Throwable t) {
                        Toaster.showToast(getContext(), "Internal error occurred.");
                        // TODO: delete the token we have stored and redirect the user to the login page
                    }
                });
            } else {
                // TODO: redirect to login page
            }
        }

        enableButtons();
    }

    private void enableButtons() {
        addMemberButton.setEnabled(true);
        leaveClanButton.setEnabled(true);
        changePasswordButton.setEnabled(true);
    }

    private void disableButtons() {
        addMemberButton.setEnabled(false);
        leaveClanButton.setEnabled(false);
        changePasswordButton.setEnabled(false);
    }
}
