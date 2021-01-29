package com.bikesharing.app.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Rental implements Serializable {

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

    @SerializedName("bikeCode")
    private String bikeCode;

    public Rental(Integer dockId, Integer bikeId, String userEmail, String bikeCode) {
        this.dockId = dockId;
        this.bikeId = bikeId;
        this.userEmail = userEmail;
        this.bikeCode = bikeCode;
    }

    public Rental(Integer id, Integer dockId, Integer bikeId, String userEmail, Date startDate, Date endDate, String bikeCode) {
        this.id = id;
        this.dockId = dockId;
        this.bikeId = bikeId;
        this.userEmail = userEmail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bikeCode = bikeCode;
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

    public String getBikeCode() {
        return bikeCode;
    }

    public void setBikeCode(String bikeCode) {
        this.bikeCode = bikeCode;
    }
}
