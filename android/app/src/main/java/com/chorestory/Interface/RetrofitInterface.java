package com.chorestory.Interface;

import android.accounts.Account;

import com.chorestory.templates.ChildRequest;
import com.chorestory.templates.ChildResponse;
import com.chorestory.templates.FriendsResponse;
import com.chorestory.templates.LoginRequest;
import com.chorestory.templates.SingleStringResponse;
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
    Call<SingleStringResponse> register(
            @Body RegisterRequest registerRequest
    );

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<SingleStringResponse> login(
            @Body LoginRequest loginRequest
    );

    @GET("me")
    Call<Account> me(
            @Header("Authorization") String auth
    );

    @Headers("Content-Type: application/json")
    @POST("child")
    Call<ChildResponse> create_child(
            @Header("Authorization") String auth,
            @Body ChildRequest childRequest
            );

    @POST("friend")
    Call<FriendsResponse> list_friends(
            @Header("Authorization") String auth
    );
}
