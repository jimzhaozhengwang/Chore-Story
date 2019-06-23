package com.chorestory.module;


import com.chorestory.Interface.RetrofitInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {

    private static final String BASE_URL = "https://chore.cloudbox.markkeller.dev/api/";

    @Provides
    @Singleton
    OkHttpClient provideOKHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    RetrofitInterface provideRetrofit() {
        Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        return retrofitInterface;
    }
}
