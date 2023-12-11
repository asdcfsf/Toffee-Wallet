package com.Toffee.Wallet.Responsemodel;

import com.google.gson.annotations.SerializedName;

public class RewardCatResp {

    @SerializedName("image")
    private String image;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("name")
    private String name;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("id")
    private int id;

    @SerializedName("min_coin")
    private int min_coin;

    @SerializedName("description")
    private String description;

    public int getMin_coin() {
        return min_coin;
    }

    public String getDescription() {
        return description;
    }

    @SerializedName("status")
    private int status;

    public String getImage() {
        return image;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

}