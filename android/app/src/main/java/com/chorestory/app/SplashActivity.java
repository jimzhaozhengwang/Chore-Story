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

        // TODO: Move this to a splash screen to be more seamless
        String token = tokenHandler.getToken(getApplicationContext());
        if (tokenHandler.isParentToken(token)) {
            Call<AccountResponse> accountQuery = retrofitInterface.me(token);
            accountQuery.enqueue(new Callback<AccountResponse>() {
                @Override
                public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                    // TODO: Navigate to profile page
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
            // TODO: retrofit
            // onResponse navigate to childHome
            // onfailure: navigate to MainActivity

            navigateTo(MainActivity.class);
        } else {
            navigateTo(MainActivity.class);
        }
    }
}
