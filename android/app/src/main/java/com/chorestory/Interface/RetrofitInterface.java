package com.chorestory.Interface;

import android.accounts.Account;

import com.chorestory.templates.LoginRequest;
import com.chorestory.templates.LoginResponse;
import com.chorestory.templates.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @Headers("Content-Type: application/json")
    @POST("register")
    Call<LoginResponse> register(
            @Body RegisterRequest registerRequest
    );

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<LoginResponse> login(
            @Body LoginRequest loginRequest
    );

    @Headers("Content-Type: application/json")
    @GET("me")
    Call<Account> me(
            @Header("Authorization") String auth
    );

}
