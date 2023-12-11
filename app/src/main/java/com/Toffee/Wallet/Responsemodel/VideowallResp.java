package com.Toffee.Wallet.Responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideowallResp {
    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("code")
    private String code;

    public List<DataItem> getData(){
        return data;
    }

    public String getCode(){
        return code;
    }

    public class DataItem{
        @SerializedName("id")
        private String id;

        @SerializedName("title")
        private String title;

        @SerializedName("subtitle")
        private String subtitle;

        @SerializedName("image")
        private String image;

        @SerializedName("coin")
        private String coin;

        @SerializedName("adID")
        private String adID;

        @SerializedName("type")
        private String type;

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public String getImage() {
            return image;
        }

        public String getCoin() {
            return coin;
        }

        public String getAdID() {
            return adID;
        }
    }

}