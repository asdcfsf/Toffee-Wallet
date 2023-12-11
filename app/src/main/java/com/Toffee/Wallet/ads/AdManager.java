package com.Toffee.Wallet.ads;

import static com.Toffee.Wallet.utils.Const.ADCOUNT;

import android.app.Activity;
import android.view.View;

import com.Toffee.Wallet.utils.AdsConfig;

public class AdManager implements AdsConfig {
     Activity activity;
    Banner.Builder bannerAd;
    Interstital.Builder interstitalAd;

    public AdManager(Activity activity){
        this.activity=activity;
        bannerAd=new Banner.Builder(activity);
        interstitalAd=new Interstital.Builder(activity);
    }

    public void loadBannerAd(View v,String bannerType,String bannerID){
        bannerAd.setAu(bannerID);
        bannerAd.setbT(bannerType);
        bannerAd.setV(v);
        bannerAd.buildAd();
    }


    public void OnBackInterstitalAd(int count,String type){
        if(ADCOUNT>=count){
            interstitalAd.loadInterstital();
        }else{
            ADCOUNT++;
        }
    }

}
