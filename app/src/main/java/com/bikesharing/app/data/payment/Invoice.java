package com.bikesharing.app.data.payment;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Invoice {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("taxNumber")
    private Integer taxNumber;

    @SerializedName("company")
    private String company;

    @SerializedName("timestamp")
    private Date timestamp;

    public Invoice(Integer id, String name, Integer taxNumber, String company, Date timestamp) {
        this.id = id;
        this.name = name;
        this.taxNumber = taxNumber;
        this.company = company;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(Integer taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
