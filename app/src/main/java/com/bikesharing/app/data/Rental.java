package com.bikesharing.app.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Rental {

    @SerializedName("id")
    private Integer id;

    @SerializedName("dockId")
    private Integer dockId;

    @SerializedName("bikeId")
    private Integer bikeId;

    @SerializedName("userEmail")
    private String userEmail;

    @SerializedName("startDate")
    private Date startDate;

    @SerializedName("endDate")
    private Date endDate;


    public Rental(Integer dockId, Integer bikeId, String userEmail) {
        this.dockId = dockId;
        this.bikeId = bikeId;
        this.userEmail = userEmail;
    }

    public Rental(Integer id, Integer dockId, Integer bikeId, String userEmail, Date startDate, Date endDate) {
        this.id = id;
        this.dockId = dockId;
        this.bikeId = bikeId;
        this.userEmail = userEmail;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDockId() {
        return dockId;
    }

    public void setDockId(Integer dockId) {
        this.dockId = dockId;
    }

    public Integer getBikeId() {
        return bikeId;
    }

    public void setBikeId(Integer bikeId) {
        this.bikeId = bikeId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
