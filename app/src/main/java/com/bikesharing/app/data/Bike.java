package com.bikesharing.app.data;

import com.google.gson.annotations.SerializedName;

public class Bike {

    @SerializedName("id")
    private Integer id;

    @SerializedName("chargeLevel")
    private Integer chargeLevel;

    @SerializedName("serviceHours")
    private Long serviceHours;

    @SerializedName("code")
    private String code;

    @SerializedName("brand")
    private String brand;

    @SerializedName("model")
    private String model;

    public Bike(Integer id, Integer chargeLevel, Long serviceHours, String code, String brand, String model) {
        this.id = id;
        this.chargeLevel = chargeLevel;
        this.serviceHours = serviceHours;
        this.code = code;
        this.brand = brand;
        this.model = model;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChargeLevel() {
        return chargeLevel;
    }

    public void setChargeLevel(Integer chargeLevel) {
        this.chargeLevel = chargeLevel;
    }

    public Long getServiceHours() {
        return serviceHours;
    }

    public void setServiceHours(Long serviceHours) {
        this.serviceHours = serviceHours;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
