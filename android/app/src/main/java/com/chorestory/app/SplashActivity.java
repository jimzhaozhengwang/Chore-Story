package com.chorestory.app;

import android.os.Bundle;

import com.chorestory.Interface.RetrofitInterface;
import com.chorestory.R;
import com.chorestory.helpers.TokenHandler;
import com.chorestory.templates.AccountResponse;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends ChoreStoryActivity {

    @Inject
    RetrofitInterface retrofitInterface;
    @Inject
    TokenHandler tokenHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);

        setContentView(R.layout.activity_splash);

        String token = tokenHandler.getToken(getApplicationContext());
        if (tokenHandler.isParentToken(token)) {
            Call<AccountResponse> accountQuery = retrofitInterface.me(token);
            accountQuery.enqueue(new Callback<AccountResponse>() {
                @Override
                public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                    navigateTo(ParentHomeActivity.class,
                            getResources().getString(R.string.clan_name),
                            response.body().getData().getClanName());
                }

                @Override
                public void onFailure(Call<AccountResponse> call, Throwable t) {
                    navigateTo(MainActivity.class);
                }
            });
        } else if (tokenHandler.isChildToken(token)) {
            // There's no need to verify the child token here since if the childHome request
            //      fails then it'll redirect back to main activity after deleting the token
            navigateTo(ChildHomeActivity.class);
        } else {
            navigateTo(MainActivity.class);
        }
    }
}
