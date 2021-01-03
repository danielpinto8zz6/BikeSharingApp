package com.bikesharing.app.rest;

import com.bikesharing.app.data.Token;
import com.bikesharing.app.data.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestService {

    @POST("/account/user")
    Call<Void> addUser(@Body User user);

    @POST("/auth/auth")
    Call<Token> authenticator(@Body User user);

    @POST("/account/password")
    Call<String> forgetPassword(@Body User user);
}
