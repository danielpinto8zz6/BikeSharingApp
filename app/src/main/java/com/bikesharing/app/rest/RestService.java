package com.bikesharing.app.rest;

import com.bikesharing.app.data.Dock;
import com.bikesharing.app.data.Bike;
import com.bikesharing.app.data.EmailToken;
import com.bikesharing.app.data.Feedback;
import com.bikesharing.app.data.Page.Page;
import com.bikesharing.app.data.payment.Payment;
import com.bikesharing.app.data.payment.PaymentRequest;
import com.bikesharing.app.data.Rental;
import com.bikesharing.app.data.Token;
import com.bikesharing.app.data.TravelEvent;
import com.bikesharing.app.data.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestService {

    @POST("/account/user")
    Call<Void> addUser(@Body User user);

    @POST("/auth/auth")
    Call<Token> authenticator(@Body User user);

    @PUT("/account/user")
    Call<Void> updateUser(@Body User user, @Header("Authorization") String authHeader);

    @PUT("/account/user/password")
    Call<Void> updatePassword(@Body User user, @Header("Authorization") String authHeader);

    @GET("/account/user/{email}")
    Call<User> getUserByEmail(@Path("email") String email, @Header("Authorization") String authHeader);

    @GET("/dock/dock")
    Call<Page<Dock>> getAllDocks(@Query("page") int nPage, @Query("size") int nSizePage, @Query("onlyWithBikes") boolean nOnlyBikes, @Header("Authorization") String authHeader);

    @GET("/rental/rental")
    Call<Page<Rental>> getAllBikeHistory(@Query("page") int nPage, @Query("size") int nSizePage, @Header("Authorization") String authHeader);

    @GET("/payment/payment")
    Call<Page<Payment>> getAllPaymentHistory(@Query("page") int nPage, @Query("size") int nSizePage, @Header("Authorization") String authHeader);

    @GET("/payment/payment/rental/{rentalId}")
    Call<Payment> getPaymentByRentalId(@Path("rentalId") int rentalId, @Header("Authorization") String authHeader);

    @GET("/travel-history/travel/{rentalId}")
    Call<List<TravelEvent>> getAllLocationHistory(@Path("rentalId") int nRentalId, @Header("Authorization") String authHeader);

    @POST("/rental/rental")
    Call<Rental> newRental(@Body Rental myRental, @Header("Authorization") String authHeader);

    @POST("/travel-history-receiver/travel")
    Call<Void> sendLocation(@Body TravelEvent myLocation, @Header("Authorization") String authHeader);

    @POST("/payment/payment")
    Call<Void> donePayment(@Body PaymentRequest myPaymentRequest, @Header("Authorization") String authHeader);

    @POST("/token-manager/token")
    Call<Void> saveToken(@Body EmailToken myToken, @Header("Authorization") String authHeader);

    @POST("/feedback/feedback")
    Call<Void> sendfeedback(@Body Feedback myFeedback, @Header("Authorization") String authHeader);

    @GET("/bike-management/bike/{bikeId}")
    Call<Bike> getBikeDetails(@Path("bikeId") Integer bikeId, @Header("Authorization") String authHeader);
}
