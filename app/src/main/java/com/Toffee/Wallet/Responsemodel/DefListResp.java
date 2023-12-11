package com.Toffee.Wallet.Responsemodel;

import com.google.gson.annotations.SerializedName;

public class DefListResp {

    @SerializedName("title")
    private String title;

    @SerializedName("id")
    private String id;

    @SerializedName("subtitle")
    private String subtitle;

    @SerializedName("image")
    private String image;

    @SerializedName("config")
    private String config;

    @SerializedName("msg")
    private String msg;

    @SerializedName("test_mode")
    private boolean test_mode;

    @SerializedName("error")
    private boolean error;

    @SerializedName("type")
    private String type;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("url")
    private String url;


    public int getUser_id() {
        return user_id;
    }

    public String getUrl() {
        return url;
    }

    @SerializedName("status")
    private int status;

    @SerializedName("coin")
    private int coin;

    @SerializedName("icon")
    private String icon;

    public String getIcon() {
        return icon;
    }

    public String getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public int getCoin() {
        return coin;
    }

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("description")
    private String description;

    @SerializedName("btn_name")
    private String btn_name;

    @SerializedName("btn_action")
    private String btn_action;

    @SerializedName("lang_code")
    private String lang_code;

    public String getLang_code() {
        return lang_code;
    }

    public boolean isError() {
        return error;
    }

    public String getBtn_action() {
        return btn_action;
    }

    public String getBtn_name() {
        return btn_name;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getMsg() {
        return msg;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getImage() {
        return image;
    }

    public String getConfig() {
        return config;
    }



    public boolean isTest_mode() {
        return test_mode;
    }

    private int viewType = 0;

    public int getViewType() {
        return viewType;
    }

    public DefListResp setViewType(int viewType) {
        this.viewType = viewType;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBtn_name(String btn_name) {
        this.btn_name = btn_name;
    }
}
