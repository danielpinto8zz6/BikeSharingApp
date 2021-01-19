package com.bikesharing.app.data.payment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class PaymentHistory implements Serializable {

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

    @SerializedName("invoiceVo")
    private Invoice invoiceVo;

    public PaymentHistory(Integer id, Integer rentalId, Double value, Integer status, Integer method, Date timestamp, String userEmail, Invoice invoiceVo) {
        this.id = id;
        this.rentalId = rentalId;
        this.value = value;
        this.status = status;
        this.method = method;
        this.timestamp = timestamp;
        this.userEmail = userEmail;
        this.invoiceVo = invoiceVo;
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

    public Invoice getInvoiceVo() {
        return invoiceVo;
    }

    public void setInvoiceVo(Invoice invoiceVo) {
        this.invoiceVo = invoiceVo;
    }
}
