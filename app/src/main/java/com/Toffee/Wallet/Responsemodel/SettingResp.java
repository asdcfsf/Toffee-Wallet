package com.Toffee.Wallet.Responsemodel;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SettingResp {

    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("spin")
    private List<SpinItem> spin;

    @SerializedName("home")
    private List<HomeResp> home;

    @SerializedName("cat")
    private List<HomeResp> cat;

    public List<HomeResp> getCat() {
        return cat;
    }

	@SerializedName("onboarding")
	private List<OnboardingItem> onboardingItems;

    @SerializedName("code")
    private String code;

    @SerializedName("msg")
    private String msg;

    public List<HomeResp> getHome() {
        return home;
    }

    public List<OnboardingItem> getOnboardingItems() {
        return onboardingItems;
    }

    public List<SpinItem> getSpin() {
        return spin;
    }

    public List<DataItem> getData() {
        return data;
    }

    public List<OnboardingItem> getOnboarding() {
        return onboardingItems;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public class DataItem {

        @SerializedName("up_version")
        private int up_version;

        @SerializedName("up_mode")
        private int up_mode;

        @SerializedName("up_msg")
        private String up_msg;

        @SerializedName("up_link")
        private String up_link;

        @SerializedName("up_btn")
        private boolean up_btn;

        @SerializedName("up_status")
        private boolean up_status;

        @SerializedName("privacy_policy")
        private String privacy_policy;

        @SerializedName("app_author")
        private String app_author;

        @SerializedName("app_email")
        private String app_email;

        @SerializedName("app_website")
        private String app_website;

        @SerializedName("app_description")
        private String app_description;

        @SerializedName("app_icon")
        private String app_icon;

        @SerializedName("insta")
        private String insta;

        @SerializedName("telegram")
        private String telegram;

        @SerializedName("youtube")
        private String youtube;

        @SerializedName("banner_type")
        private String banner_type;

        @SerializedName("bannerID")
        private String bannerID;

        @SerializedName("homeMsg")
        private String homeMsg;

        @SerializedName("interstitalID")
        private String interstitalID;

        @SerializedName("interstital_count")
        private int interstital_count;

        @SerializedName("interstital_type")
        private String interstital_type;

        @SerializedName("refer_msg")
        private String refer_msg;

        @SerializedName("task")
        private String task;

        @SerializedName("startapp_appid")
        private String startapp_appid;

        @SerializedName("unity_game")
        private String unity_game;

        @SerializedName("adcolonyApp")
        private String adcolonyApp;

        @SerializedName("adcolony_zone")
        private String adcolony_zone;

        @SerializedName("nativeID")
        private String nativeID;

        @SerializedName("native_count")
        private int native_count;

        @SerializedName("nativeType")
        private String nativeType;

        @SerializedName("interstital_adunit")
        private String interstital_adunit;

        public String getInterstital_adunit() {
            return interstital_adunit;
        }

        public String getHomeMsg() {
            return homeMsg;
        }

        public String getUp_msg() {
            return up_msg;
        }

        public int getUp_version() {
            return up_version;
        }

        public int getUp_mode() {
            return up_mode;
        }

        public String getUp_link() {
            return up_link;
        }

        public boolean isUp_btn() {
            return up_btn;
        }

        public boolean isUp_status() {
            return up_status;
        }

        public String getPrivacy_policy() {
            return privacy_policy;
        }

        public String getApp_author() {
            return app_author;
        }

        public String getApp_email() {
            return app_email;
        }

        public String getApp_website() {
            return app_website;
        }

        public String getApp_description() {
            return app_description;
        }

        public String getApp_icon() {
            return app_icon;
        }

        public String getInsta() {
            return insta;
        }

        public String getTelegram() {
            return telegram;
        }

        public String getYoutube() {
            return youtube;
        }

        public String getBanner_type() {
            return banner_type;
        }

        public String getBannerID() {
            return bannerID;
        }

        public String getInterstitalID() {
            return interstitalID;
        }

        public int getInterstital_count() {
            return interstital_count;
        }

        public String getInterstital_type() {
            return interstital_type;
        }

        public String getRefer_msg() {
            return refer_msg;
        }

        public String getTask() {
            return task;
        }

        public String getStartapp_appid() {
            return startapp_appid;
        }

        public String getUnity_game() {
            return unity_game;
        }

        public String getAdcolonyApp() {
            return adcolonyApp;
        }

        public String getAdcolony_zone() {
            return adcolony_zone;
        }

        public String getNativeID() {
            return nativeID;
        }

        public int getNative_count() {
            return native_count;
        }

        public String getNativeType() {
            return nativeType;
        }
    }

    public class OnboardingItem {

        @SerializedName("image")
        private String image;

        @SerializedName("title")
        private String title;

        @SerializedName("subtitle")
        private String subtitle;

        public String getImage() {
            return image;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }
    }

    public static class SpinItem {

        @SerializedName("id")
        private String id;

        @SerializedName("coin")
        private String coin;

        @SerializedName("color")
        private String color;

        public String getId() {
            return id;
        }

        public String getCoin() {
            return coin;
        }

        public String getColor() {
            return color;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }


}