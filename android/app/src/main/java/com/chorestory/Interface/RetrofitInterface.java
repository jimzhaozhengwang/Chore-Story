package com.chorestory.Interface;

import com.chorestory.templates.AccountResponse;
import com.chorestory.templates.ChildRequest;
import com.chorestory.templates.ChildResponse;
import com.chorestory.templates.ClanChildrenResponse;
import com.chorestory.templates.ClanResponse;
import com.chorestory.templates.FriendsResponse;
import com.chorestory.templates.GetQuestsResponse;
import com.chorestory.templates.LoginRequest;
import com.chorestory.templates.QuestCreateRequest;
import com.chorestory.templates.QuestCreateResponse;
import com.chorestory.templates.RegisterRequest;
import com.chorestory.templates.SingleResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitInterface {

    @Headers("Content-Type: application/json")
    @POST("register")
    Call<SingleResponse<String>> register(
            @Body RegisterRequest registerRequest
    );

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<SingleResponse<String>> login(
            @Body LoginRequest loginRequest
    );

    @GET("me")
    Call<AccountResponse> me(
            @Header("Authorization") String auth
    );

    @GET("me")
    Call<ChildResponse> me_child(
            @Header("Authorization") String auth
    );

    @Headers("Content-Type: application/json")
    @POST("child")
    Call<SingleResponse<String>> get_child_clan_token(
            @Header("Authorization") String auth
    );

    @Headers("Content-Type: application/json")
    @POST("child/{cid}")
    Call<SingleResponse<String>> create_child(
            @Path("cid") String cid,
            @Body ChildRequest childRequest
    );

    @POST("friend")
    Call<FriendsResponse> list_friends(
            @Header("Authorization") String auth
    );

    @GET("clan")
    Call<ClanResponse> get_clan(
            @Header("Authorization") String auth
    );

    @GET("clan/children")
    Call<ClanChildrenResponse> get_clan_children(
            @Header("Authorization") String auth
    );

    @POST("child/{cid}/quest")
    Call<QuestCreateResponse> create_quest(
            @Header("Authorization") String auth,
            @Path("cid") Integer cid,
            @Body QuestCreateRequest questcreateRequest
    );

    @GET("child_quest/{start}/{lookahead}")
    Call<GetQuestsResponse> get_all_quests(
            @Header("Authorization") String auth,
            @Path("start") String start,
            @Path("lookahead") String lookahead
    );
}
