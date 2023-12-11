package com.Toffee.Wallet.Responsemodel;

public class ApiModel {
    String campid,title,description,icon,amount,link;

    public ApiModel(String campid, String title, String description, String icon, String amount, String link) {
        this.campid = campid;
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.amount = amount;
        this.link = link;
    }

    public String getCampid() {
        return campid;
    }

    public void setCampid(String campid) {
        this.campid = campid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
