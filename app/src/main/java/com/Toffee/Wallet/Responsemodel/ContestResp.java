package com.Toffee.Wallet.Responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContestResp {
    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("code")
    private int code;



    public int getCode() {
        return code;
    }


    public List<DataItem> getData() {
        return data;
    }


    public class DataItem {

        @SerializedName("id")
        private String id;

        @SerializedName("title")
        private String title;

        @SerializedName("image")
        private String image;

        @SerializedName("task_require")
        private String task_require;

        @SerializedName("coin")
        private String coin;

        @SerializedName("validity")
        private String validity;

        @SerializedName("start_from")
        private String start_from;

        @SerializedName("expired_at")
        private String expired_at;

        @SerializedName("refer")
        private int refer;

        public int getRefer() {
            return refer;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getTask_require() {
            return task_require;
        }

        public String getValidity() {
            return validity;
        }

        public String getStart_from() {
            return start_from;
        }

        public String getExpired_at() {
            return expired_at;
        }

        public String getCoin() {
            return coin;
        }

        public String getImage() {
            return image;
        }
    }

}
