package com.chorestory.module;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Module
public class NetModule {

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
    Retrofit provideRetrofit() {
        return null;// TODO: return new Retrofit as needed
    }
}
