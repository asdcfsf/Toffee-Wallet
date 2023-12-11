package com.Toffee.Wallet.Responsemodel;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GameResp {

    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("data2")
    private List<DataItem> data2;

    @SerializedName("status")
    private int status;

    @SerializedName("game_minute")
    private int game_minute;

    @SerializedName("game_message")
    private String game_message;

    public List<DataItem> getData2() {
        return data2;
    }

    public int getGame_minute() {
        return game_minute;
    }

    public String getGame_message() {
        return game_message;
    }

    public List<DataItem> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public static class DataItem {
        @SerializedName("image")
        private String image;

        @SerializedName("link")
        private String link;

        @SerializedName("game_type")
        private int game_type;

        @SerializedName("ad_type")
        private int ad_type;

        @SerializedName("anim_play")
        private String anim_play;

        @SerializedName("anim_off")
        private String anim_off;

        @SerializedName("background")
        private String background;

        @SerializedName("card_bg")
        private int card_bg;

        @SerializedName("id")
        private String id;

        @SerializedName("description")
        private String description;

        public String getDescription() {
            return description;
        }

        @SerializedName("viewType")
        private int viewType;

        @SerializedName("time")
        private int time;

        public int getTime() {
            return time;
        }

        @SerializedName("game_coin")
        private String game_coin;

        public String getGame_coin() {
            return game_coin;
        }

        @SerializedName("title")
        private String title;

        @SerializedName("action_type")
        private String action_type;

        public String getImage() {
            return image;
        }

        public String getLink() {
            return link;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public int getGame_type() {
            return game_type;
        }

        public int getAd_type() {
            return ad_type;
        }

        public String getAction_type() {
            return action_type;
        }

        public String getAnim_play() {
            return anim_play;
        }

        public String getAnim_off() {
            return anim_off;
        }

        public String getBackground() {
            return background;
        }

        public int getViewType() {
            return viewType;
        }

        public int getCard_bg() {
            return card_bg;
        }
    }
}