package com.bikesharing.app.rest;

import com.bikesharing.app.data.Dock;
import com.bikesharing.app.data.Page.Page;
import com.bikesharing.app.data.Rental;
import com.bikesharing.app.data.Token;
import com.bikesharing.app.data.TravelEvent;
import com.bikesharing.app.data.User;
import com.bikesharing.app.travel.TravelActivity;

import retrofit2.Call;
import retrofit2.http.Body;
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

    //TODO forgetPassword
    @POST("/account/password")
    Call<String> forgetPassword(@Body User user);

    //TODO recoverPassword
    @POST("/account/newPassword")
    Call<User> newPassword(@Body User user, @Header("Authorization") String authHeader);

    //TODO newName
    @POST("/account/newName")
    Call<User> newUserName(@Body User user, @Header("Authorization") String authHeader);

    @GET("/account/user/{email}")
    Call<User> getUserByEmail(@Path("email") String email, @Header("Authorization") String authHeader);

    @GET("/dock/dock")
    Call<Page<Dock>> getAllDocks(@Query("page") int nPage, @Query("size") int nSizePage, @Query("onlyWithBikes") boolean nOnlyBikes, @Header("Authorization") String authHeader);

    @POST("/rental/rental")
    Call<Rental> newRental(@Body Rental myRental, @Header("Authorization") String authHeader);

    @POST("/travel-history-receiver/travel")
    Call<Void> sendLocation(@Body TravelEvent myLocation, @Header("Authorization") String authHeader);
}
