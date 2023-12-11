package com.Toffee.Wallet.Responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DefResp {
    @SerializedName("data")
    private List<DefListResp> data;

    public List<DefListResp> getData() {
        return data;
    }

    @SerializedName("id")
    private String id;
    @SerializedName("msg")
    private String msg;

    @SerializedName("code")
    private String code;

    @SerializedName("data1")
    private String data1;

    @SerializedName("data2")
    private String data2;

    @SerializedName("coin")
    private String coin;

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String image;
    @SerializedName("balance")
    private int balance;

    @SerializedName("interval")
    private int interval;

    @SerializedName("limit")
    private int limit;

    @SerializedName("ad_interval")
    private int ad_interval;

    @SerializedName("adType")
    private int adType;

    public String getId() {
        return id;
    }

    @SerializedName("pendingRef")
    private int pendingRef;

    @SerializedName("successRef")
    private int successRef;

    @SerializedName("refer_point1")
    private String refer_point1;

    @SerializedName("refer_point2")
    private String refer_point2;

    @SerializedName("refer_mode")
    private String refer_mode;

    @SerializedName("refer_point3")
    private String refer_point3;

    public String getRefer_mode() {
        return refer_mode;
    }

    public String getRefer_point1() {
        return refer_point1;
    }

    public String getRefer_point2() {
        return refer_point2;
    }

    public String getRefer_point3() {
        return refer_point3;
    }

    @SerializedName("welcome_bonus")
    private String welcome_bonus;

    @SerializedName("refer_bonus")
    private String refer_bonus;

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getWelcome_bonus() {
        return welcome_bonus;
    }

    public String getRefer_bonus() {
        return refer_bonus;
    }

    public int getPendingRef() {
        return pendingRef;
    }

    public int getSuccessRef() {
        return successRef;
    }


    public int getAdType() {
        return adType;
    }

    public int getAd_interval() {
        return ad_interval;
    }

    public String getData1() {
        return data1;
    }

    public String getData2() {
        return data2;
    }

    public String getCoin() {
        return coin;
    }

    public int getInterval() {
        return interval;
    }

    public String getMsg() {
        return msg;
    }

    public int getBalance() {
        return balance;
    }

    public String getCode() {
        return code;
    }

    public int getLimit() {
        return limit;
    }
}
