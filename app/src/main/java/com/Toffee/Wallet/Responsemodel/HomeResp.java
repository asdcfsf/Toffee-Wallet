package com.Toffee.Wallet.Responsemodel;

import com.google.gson.annotations.SerializedName;

public class HomeResp {

    @SerializedName("id")
    private int id;

    @SerializedName("cat_theme")
    private int cat_theme;
    @SerializedName("icon")
    private String icon;

    @SerializedName("title")
    private String title;

    @SerializedName("subtitle")
    private String subtitle;

    @SerializedName("btn_name")
    private String btn_name;

    @SerializedName("btn_action")
    private String btn_action;

    @SerializedName("background_color")
    private String background_color;

    @SerializedName("btn_theme")
    private int btn_theme;

    @SerializedName("btn_color")
    private String btn_color;

    @SerializedName("btn_background")
    private int btn_background;

    public String getBackground_color() {
        return background_color;
    }

    public int getBtn_theme() {
        return btn_theme;
    }

    public String getBtn_color() {
        return btn_color;
    }

    public int getBtn_background() {
        return btn_background;
    }

    @SerializedName("url")
    private String url;

    @SerializedName("cat")
    private int cat;

    @SerializedName("view_mode")
    private int view_mode;

    public int getView_mode() {
        return view_mode;
    }

    public void setView_mode(int view_mode) {
        this.view_mode = view_mode;
    }

    public int getCat() {
        return cat;
    }

    public void setCat(int cat) {
        this.cat = cat;
    }

    public int getCat_theme() {
        return cat_theme;
    }

    public void setCat_theme(int cat_theme) {
        this.cat_theme = cat_theme;
    }

    @SerializedName("type")
    private String type;

    @SerializedName("cat_type")
    private String cat_type;

    public String getCat_type() {
        return cat_type;
    }

    public void setCat_type(String cat_type) {
        this.cat_type = cat_type;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getBtn_name() {
        return btn_name;
    }

    public String getBtn_action() {
        return btn_action;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setBtn_name(String btn_name) {
        this.btn_name = btn_name;
    }

    public void setBtn_action(String btn_action) {
        this.btn_action = btn_action;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    public void setBtn_theme(int btn_theme) {
        this.btn_theme = btn_theme;
    }

    public void setBtn_color(String btn_color) {
        this.btn_color = btn_color;
    }

    public void setBtn_background(int btn_background) {
        this.btn_background = btn_background;
    }
}
