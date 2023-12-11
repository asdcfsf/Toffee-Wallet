package com.Toffee.Wallet.Responsemodel;

import java.util.List;

public class HomeModal {
    List<HomeResp> category;
    List<HomeResp> categoryItemList;
    List<GameResp.DataItem> gameResp;

    public HomeModal(List<HomeResp> category, List<HomeResp> categoryItemList, List<GameResp.DataItem> gameResp) {
        this.category = category;
        this.categoryItemList = categoryItemList;
        this.gameResp = gameResp;
    }

    public List<HomeResp> getCategory() {
        return category;
    }

    public void setCategory(List<HomeResp> category) {
        this.category = category;
    }

    public List<GameResp.DataItem> getGameResp() {
        return gameResp;
    }

    public void setGameResp(List<GameResp.DataItem> gameResp) {
        this.gameResp = gameResp;
    }



    public List<HomeResp> getCategoryItemList() {
        return categoryItemList;
    }

    public List<HomeResp> getHomeItemList() {
        return categoryItemList;
    }

    public void setCategoryItemList(List<HomeResp> categoryItemList) {
        this.categoryItemList = categoryItemList;
    }

}
