package com.Toffee.Wallet.Responsemodel;

import com.google.gson.annotations.SerializedName;

public class TaskResp {
    @SerializedName("timer")
    private int timer;

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("subtitle")
    private String subtitle;

    @SerializedName("description")
    private String description;

    @SerializedName("icon")
    private String icon;

    @SerializedName("image")
    private String image;

    @SerializedName("btn_name")
    private String btn_name;

  @SerializedName("url")
    private String url;

    @SerializedName("inserted_at")
    private String insertedAt;

    @SerializedName("point")
    private String point;

    @SerializedName("adID")
    private String adID;

    @SerializedName("coin")
    private String coin;

    @SerializedName("link")
    private String link;

    @SerializedName("status")
    private int status;

    @SerializedName("views")
    private int views;

    @SerializedName("task_limit")
    private int task_limit;

    @SerializedName("thumb")
    private String thumb;

    @SerializedName("video_id")
    private String videoId;

    @SerializedName("ad_type")
    private int ad_type;

    @SerializedName("action_type")
    private String action_type;

    public String getDescription() {
        return description;
    }

    private int viewType = 0;

    public int getViewType() {
        return viewType;
    }

    public TaskResp setViewType(int viewType) {
        this.viewType = viewType;
        return this;
    }

    public int getAd_type() {
        return ad_type;
    }

    public String getAction_type() {
        return action_type;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getAdID() {
        return adID;
    }

    public int getViews() {
        return views;
    }

    public String getCoin() {
        return coin;
    }

    public String getImage() {
        return image;
    }

    public String getLink() {
        return link;
    }

    public String getThumb() {
        return thumb;
    }

    public int getTask_limit() {
        return task_limit;
    }

    public String getVideoId() {
        return videoId;
    }


    public int getTimer() {
        return timer;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getInsertedAt() {
        return insertedAt;
    }

    public String getUrl() {
        return url;
    }

    public String getPoint() {
        return point;
    }

    public int getStatus() {
        return status;
    }

    public String getIcon() {
        return icon;
    }

    public String getBtn_name() {
        return btn_name;
    }
}