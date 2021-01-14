package com.bikesharing.app.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Dock implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("location")
    private String location;

    @SerializedName("bikeId")
    private int bikeId;

    public Dock(int id, long latitude, long longitude, String location, int bikeId) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.bikeId = bikeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getBikeId() {
        return bikeId;
    }

    public void setBikeId(int bikeId) {
        this.bikeId = bikeId;
    }

    //TODO getDistance
    public double getDistance() {
        return latitude;
    }

    //TODO getDistanceTime
    public double getDistanceTime() {
        return latitude;
    }

    //Todo numberOfBikes
    public int getNumberOfBikes() {
        return bikeId;
    }
}
