package com.bikesharing.app.data.payment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Payment implements Serializable {

    public static final Integer AWAITING_PAYMENT = 0;
    public static final Integer VALIDATING_PAYMENT = 1;
    public static final Integer PAID = 2;
    public static final Integer PAYMENT_FAILED = 3;

    @SerializedName("id")
    private Integer id;

    @SerializedName("rentalId")
    private Integer rentalId;

    @SerializedName("value")
    private Double value;

    @SerializedName("status")
    private Integer status;

    @SerializedName("method")
    private Integer method;

    @SerializedName("timestamp")
    private Date timestamp;

    @SerializedName("userEmail")
    private String userEmail;

    @SerializedName("name")
    private String name;

    @SerializedName("taxNumber")
    private Integer taxNumber;

    @SerializedName("company")
    private String company;

    public Payment(Integer id, Integer rentalId, Double value, Integer status, Integer method, Date timestamp, String userEmail, String name, Integer taxNumber, String company) {
        this.id = id;
        this.rentalId = rentalId;
        this.value = value;
        this.status = status;
        this.method = method;
        this.timestamp = timestamp;
        this.userEmail = userEmail;
        this.name = name;
        this.taxNumber = taxNumber;
        this.company = company;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRentalId() {
        return rentalId;
    }

    public void setRentalId(Integer rentalId) {
        this.rentalId = rentalId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    public String getStatusString() {

        switch (this.status) {
            case 0:
                return "Awaiting payment";
            case 1:
                return "Validating payment";
            case 2:
                return "Paid";
            case 3:
                return "Payment failed";
        }

        return "UNKNOWN";
    }
}
