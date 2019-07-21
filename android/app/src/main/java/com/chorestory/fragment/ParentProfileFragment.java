package com.chorestory.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chorestory.Interface.RetrofitInterface;
import com.chorestory.R;
import com.chorestory.app.App;
import com.chorestory.app.QrCodeActivity;
import com.chorestory.helpers.Toaster;
import com.chorestory.helpers.TokenHandler;
import com.chorestory.templates.AccountResponse;
import com.chorestory.templates.SingleResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentProfileFragment extends ChoreStoryFragment {

    private ImageView parentImageView;
    private TextView parentNameTextView;
    private TextView clanNameTextView;
    private Button addParentGuardianButton;
    private Button addChildButton;
    private Spinner childSpinner;
    private Button logInChildButton;
    private TextView parentEmailTextView;
    private Button logoutButton;

    @Inject
    RetrofitInterface retrofitInterface;
    @Inject
    TokenHandler tokenHandler;

    private int selectedChildId;
    private String token;
    private List<AccountResponse.Data.Child> childList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_profile, container, false);
        App.getAppComponent().inject(this);

        selectedChildId = -1;
        token = tokenHandler.getToken(getContext());

        parentImageView = view.findViewById(R.id.parent_image_view);
        parentNameTextView = view.findViewById(R.id.parent_name_text_view);
        clanNameTextView = view.findViewById(R.id.clan_name_text_view);
        addParentGuardianButton = view.findViewById(R.id.add_parent_guardian_button);
        addChildButton = view.findViewById(R.id.add_child_button);
        childSpinner = view.findViewById(R.id.child_spinner);
        logInChildButton = view.findViewById(R.id.log_in_child_button);
        parentEmailTextView = view.findViewById(R.id.parent_email_text_view);
        logoutButton = view.findViewById(R.id.logout_parent_button);

        views = Arrays.asList(addParentGuardianButton, addChildButton, logInChildButton, logoutButton);
        enableViews();

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
                navigateToQRContext(getContext(),
                        QrCodeActivity.class,
                        getString(R.string.qr_code),
                        getString(R.string.child),
                        "");
            }
        });

        logInChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViews();
                if (selectedChildId != -1) {

                    // Get the childs login token to be passed to the QR activity
                    String parentToken = tokenHandler.getToken(getContext());
                    if (parentToken != null && tokenHandler.isParentToken(parentToken)) {
                        Call<SingleResponse<String>> childTokenRequest = retrofitInterface.get_child_login_token(parentToken, String.valueOf(selectedChildId));

                        childTokenRequest.enqueue(new Callback<SingleResponse<String>>() {
                            @Override
                            public void onResponse(Call<SingleResponse<String>> call, Response<SingleResponse<String>> response) {
                                if (response.isSuccessful() && response.body() != null && response.body().hasResponse()) {
                                    String childToken = response.body().getData();

                                    navigateToQRContext(getContext(),
                                            QrCodeActivity.class,
                                            getString(R.string.qr_code),
                                            getString(R.string.child),
                                            childToken);
                                }
                            }

                            @Override
                            public void onFailure(Call<SingleResponse<String>> call, Throwable t) {
                                Toaster.showToast(getContext(), "Internal error occurred.");
                                enableViews();
                            }
                        });
                    } else {
                        deleteTokenNavigateMain(getContext());
                    }
                } else {
                    enableViews();
                    Toaster.showToast(getContext(), "Please select a child!");
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTokenNavigateMain(getContext());
            }
        });
        return view;
    }

    private void populateChildrenList() {
        if (token != null && tokenHandler.isParentToken(token)) {
            Call<AccountResponse> accountQuery = retrofitInterface.me(token);
            accountQuery.enqueue(new Callback<AccountResponse>() {
                @Override
                public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                    if (response.isSuccessful() &&
                            response.body() != null &&
                            response.body().hasResponse()) {

                        AccountResponse.Data respData = response.body().getData();
                        childList = respData.getChildren();

                        List<String> childNameList = new ArrayList<>();
                        for (AccountResponse.Data.Child child : childList) {
                            childNameList.add(child.getName());
                        }

                        ArrayAdapter<String> childSpinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, childNameList);
                        childSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        childSpinner.setAdapter(childSpinnerAdapter);

                        childSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position < childList.size()) {
                                    selectedChildId = childList.get(position).getId();
                                } else {
                                    Log.d("BUG", "Our child list is inconsistent somehow.");
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                selectedChildId = -1;
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<AccountResponse> call, Throwable t) {
                    Toaster.showToast(getContext(), "Internal error occurred.");
                    deleteTokenNavigateMain(getContext());
                }
            });
        } else {
            deleteTokenNavigateMain(getContext());
        }
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
                            deleteTokenNavigateMain(getContext());
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountResponse> call, Throwable t) {
                        Toaster.showToast(getContext(), "Internal error occurred.");
                        deleteTokenNavigateMain(getContext());
                    }
                });
            } else {
                deleteTokenNavigateMain(getContext());
            }
        }

        populateChildrenList();

        // TODO: show/hide child log in
//        if (childList != null && !childList.isEmpty()) {
//            childSpinner.setVisibility(View.VISIBLE);
//            logInChildButton.setVisibility(View.VISIBLE);
//        } else {
//            childSpinner.setVisibility(View.GONE);
//            logInChildButton.setVisibility(View.GONE);
//        }
    }
}
