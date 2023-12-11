package com.Toffee.Wallet.Responsemodel;

import com.google.gson.annotations.SerializedName;

public class MissionResp {
    @SerializedName("app")
    private String app;

    @SerializedName("spin")
    private String spin;

    @SerializedName("web")
    private String web;

    @SerializedName("video")
    private String video;

    @SerializedName("videoZone")
    private String videoZone;

    @SerializedName("quiz")
    private String quiz;

    @SerializedName("scratch")
    private String scratch;

    @SerializedName("total")
    private String total;

    @SerializedName("left")
    private String left;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getSpin() {
        return spin;
    }

    public void setSpin(String spin) {
        this.spin = spin;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoZone() {
        return videoZone;
    }

    public void setVideoZone(String videoZone) {
        this.videoZone = videoZone;
    }

    public String getQuiz() {
        return quiz;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }

    public String getScratch() {
        return scratch;
    }

    public void setScratch(String scratch) {
        this.scratch = scratch;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }
}