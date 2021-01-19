package com.bikesharing.app.data.payment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentRequest implements Serializable {

    @SerializedName("paymentId")
    private Integer paymentId;

    @SerializedName("name")
    private String name;

    @SerializedName("taxNumber")
    private Integer taxNumber;

    @SerializedName("company")
    private String company;

    @SerializedName("method")
    private Integer method;

    @SerializedName("creditCardNumber")
    private String creditCardNumber;

    @SerializedName("creditCardExpirationDate")
    private String creditCardExpirationDate;

    @SerializedName("creditCardCode")
    private String creditCardCode;

    public PaymentRequest(Integer paymentId, String name, Integer taxNumber, String company, Integer method, String creditCardNumber, String creditCardExpirationDate, String creditCardCode) {
        this.paymentId = paymentId;
        this.name = name;
        this.taxNumber = taxNumber;
        this.company = company;
        this.method = method;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpirationDate = creditCardExpirationDate;
        this.creditCardCode = creditCardCode;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
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

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardExpirationDate() {
        return creditCardExpirationDate;
    }

    public void setCreditCardExpirationDate(String creditCardExpirationDate) {
        this.creditCardExpirationDate = creditCardExpirationDate;
    }

    public String getCreditCardCode() {
        return creditCardCode;
    }

    public void setCreditCardCode(String creditCardCode) {
        this.creditCardCode = creditCardCode;
    }
}
