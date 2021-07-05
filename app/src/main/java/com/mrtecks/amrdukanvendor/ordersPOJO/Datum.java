package com.mrtecks.amrdukanvendor.ordersPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("txn")
    @Expose
    private String txn;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("pay_mode")
    @Expose
    private String pay_mode;
    @SerializedName("slot")
    @Expose
    private String slot;
    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("delivery_date")
    @Expose
    private String delivery_date;
    @SerializedName("prescription")
    @Expose
    private String prescription;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("current")
    @Expose
    private String current;
    @SerializedName("time_to_prepare")
    @Expose
    private String time_to_prepare;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTxn() {
        return txn;
    }

    public void setTxn(String txn) {
        this.txn = txn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getPay_mode() {
        return pay_mode;
    }

    public String getSlot() {
        return slot;
    }

    public void setPay_mode(String pay_mode) {
        this.pay_mode = pay_mode;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getTime_to_prepare() {
        return time_to_prepare;
    }

    public void setTime_to_prepare(String time_to_prepare) {
        this.time_to_prepare = time_to_prepare;
    }
}
