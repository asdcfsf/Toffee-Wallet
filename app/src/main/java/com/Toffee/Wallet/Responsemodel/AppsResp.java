package com.Toffee.Wallet.Responsemodel;

import com.google.gson.annotations.SerializedName;

public class AppsResp {
    @SerializedName("app_name")
    private String appName;

    @SerializedName("image")
    private String image;

    @SerializedName("details")
    private String details;

    @SerializedName("id")
    private String id;

    @SerializedName("appurl")
    private String appurl;

    @SerializedName("url")
    private String url;

    @SerializedName("appID")
    private String appID;

    @SerializedName("points")
    private String points;

    @SerializedName("p_userid")
    private String p_userid;

    @SerializedName("offer_type")
    private String offer_type;

    @SerializedName("offer_id")
    private String offer_id;

    @SerializedName("status")
    private int status;

    @SerializedName("task_limit")
    private int task_limit;

    public int getTask_limit() {
        return task_limit;
    }

    public String getUrl() {
        return url;
    }

    public String getOffer_type() {
        return offer_type;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public String getAppID() {
        return appID;
    }

    public String getAppName() {
        return appName;
    }

    public String getP_userid() {
        return p_userid;
    }

    public String getImage() {
        return image;
    }

    public String getDetails() {
        return details;
    }

    public String getId() {
        return id;
    }

    public String getAppurl() {
        return appurl;
    }

    public String getPoints() {
        return points;
    }

    public int getStatus() {
        return status;
    }

}