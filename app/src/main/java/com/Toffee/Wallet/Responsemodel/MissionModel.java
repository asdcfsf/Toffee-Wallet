package com.Toffee.Wallet.Responsemodel;

public class MissionModel {

    private String title,info,type,btn,win;
    int icon,icon2;

    public MissionModel(String title, String win, String info, String type, String btn, int icon, int icon2) {
        this.title = title;
        this.win = win;
        this.info = info;
        this.type = type;
        this.btn = btn;
        this.icon = icon;
        this.icon2 = icon2;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getBtn() {
        return btn;
    }

    public void setBtn(String btn) {
        this.btn = btn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon2() {
        return icon2;
    }

    public void setIcon2(int icon2) {
        this.icon2 = icon2;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
