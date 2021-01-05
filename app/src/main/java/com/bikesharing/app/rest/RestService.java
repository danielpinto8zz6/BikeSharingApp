package com.bikesharing.app.rest;

import com.bikesharing.app.data.Dock;
import com.bikesharing.app.data.Token;
import com.bikesharing.app.data.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestService {

    @POST("/account/user")
    Call<Void> addUser(@Body User user);

    @POST("/auth/auth")
    Call<Token> authenticator(@Body User user);

    @POST("/account/password")
    Call<String> forgetPassword(@Body User user);

    @GET("/account/user/{email}")
    Call<User> getUserByEmail(@Path("email") String email, @Header("Authorization") String authHeader);

    @GET("/dock/dock")
    Call<List<Dock>> getAllDocks(@Query("page") int nPage, @Query("size") int nSizePage, @Query("onlyWithBikes") boolean nOnlyBikes, @Header("Authorization") String authHeader);
}
