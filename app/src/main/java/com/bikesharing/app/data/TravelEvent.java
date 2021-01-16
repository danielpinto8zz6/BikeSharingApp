package com.bikesharing.app.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TravelEvent {

    @SerializedName("rentalId")
    private Integer rentalId;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("timestamp")
    private Date timestamp;

    public TravelEvent(Integer rentalId, double latitude, double longitude, Date timestamp) {
        this.rentalId = rentalId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public Integer getRentalId() {
        return rentalId;
    }

    public void setRentalId(Integer rentalId) {
        this.rentalId = rentalId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
