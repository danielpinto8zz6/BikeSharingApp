package com.bikesharing.app.data;

import com.google.gson.annotations.SerializedName;

public class Dock {

    @SerializedName("latitude")
    private long latitude;

    @SerializedName("longitude")
    private long longitude;

    @SerializedName("location")
    private String location;

    @SerializedName("bikeid")
    private int bikeId;

    public Dock(long latitude, long longitude, String location, int bikeId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.bikeId = bikeId;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
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
}
