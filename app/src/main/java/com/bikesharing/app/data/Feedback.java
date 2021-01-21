package com.bikesharing.app.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Feedback implements Serializable {

    @SerializedName("id")
    private Integer id;

    @SerializedName("userEmail")
    private String userEmail;

    @SerializedName("rentalId")
    private Integer rentalId;

    @SerializedName("message")
    private String message;

    @SerializedName("rating")
    private Integer rating;

    public Feedback(String userEmail, Integer rentalId, String message, Integer rating) {
        this.userEmail = userEmail;
        this.rentalId = rentalId;
        this.message = message;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getRentalId() {
        return rentalId;
    }

    public void setRentalId(Integer rentalId) {
        this.rentalId = rentalId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
