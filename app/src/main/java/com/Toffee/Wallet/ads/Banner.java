package com.Toffee.Wallet.ads;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.ads.androidsdk.R;
import com.Toffee.Wallet.utils.AdsConfig;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.startapp.sdk.ads.banner.BannerListener;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

public class Banner implements AdsConfig {
    public static class Builder {
        private static final String TAG = "AdNetwork__Banner";
        private String au;
        private String bT;
        AdView aAdview;
        Activity a;
        View v;
        MaxAdView maxAdView;
        FrameLayout ac;
        RelativeLayout fc;

        public Builder(Activity act) {
            this.a = act;
        }

        public Builder buildAd() {
            loadBanner();
            return this;
        }

        public Builder setAu(String au) {
            this.au = au;
            return this;

        }

        public Builder setA(Activity a) {
            this.a = a;
            return this;
        }

        public View getV() {
            return v;
        }

        public void setV(View v) {
            this.v = v;
        }

        public String getbT() {
            return bT;
        }

        public void setbT(String bT) {
            this.bT = bT;
        }

        private void loadBanner() {

            switch (bT) {
                case "off":
                    break;
                case ADMOB:
                    if (au == null) break;
                    if (getV() == null) {
                        ac = a.findViewById(R.id.frame_container);
                    } else {
                        ac = getV().findViewById(R.id.frame_container);
                    }
                    aAdview = new AdView(a);
                    aAdview.setAdUnitId(au);
                    ac.removeAllViews();
                    ac.addView(aAdview);
                    aAdview.setAdSize(AdSize.BANNER);
                    aAdview.loadAd(new AdRequest.Builder().build());
                    aAdview.setAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            ac.setVisibility(View.GONE);
                            Log.e(TAG, "ADMOB_BANNER: " + loadAdError);
                        }

                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            ac.setVisibility(View.VISIBLE);
                        }
                    });
                    break;

                case FB:
                    if (au == null) break;
                    if (getV() == null) {
                        fc = a.findViewById(R.id.rel_container);
                    } else {
                        fc = getV().findViewById(R.id.rel_container);
                    }
                    com.facebook.ads.AdView adView = new com.facebook.ads.AdView(a, au, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
                    fc.addView(adView);
                    adView.loadAd(adView.buildLoadAdConfig().withAdListener(new com.facebook.ads.AdListener() {
                        @Override
                        public void onError(Ad ad, AdError adError) {
                            fc.setVisibility(View.GONE);
                            Log.e(TAG, "FB_BANNER: " + adError);
                        }

                        @Override
                        public void onAdLoaded(Ad ad) {
                            fc.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAdClicked(Ad ad) {
                        }

                        @Override
                        public void onLoggingImpression(Ad ad) {
                        }
                    }).build());
                    break;

                case APPLOVIN:
                    if (au == null) break;
                    if (getV() == null) {
                        fc = a.findViewById(R.id.rel_container);
                    } else {
                        fc = getV().findViewById(R.id.rel_container);
                    }
                    maxAdView = new MaxAdView(au, a);
                    maxAdView.setListener(new MaxAdViewAdListener() {
                        @Override
                        public void onAdExpanded(MaxAd ad) {
                        }

                        @Override
                        public void onAdCollapsed(MaxAd ad) {
                        }

                        @Override
                        public void onAdLoaded(MaxAd ad) {
                            fc.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAdDisplayed(MaxAd ad) {
                        }

                        @Override
                        public void onAdHidden(MaxAd ad) {
                        }

                        @Override
                        public void onAdClicked(MaxAd ad) {
                        }

                        @Override
                        public void onAdLoadFailed(String adUnitId, MaxError error) {
                            fc.setVisibility(View.GONE);
                            Log.e(TAG, "APPLOVIN_BANNER: " + error.getMessage());
                        }

                        @Override
                        public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                        }
                    });
                    maxAdView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, a.getResources().getDimensionPixelSize(R.dimen.banner_height)));
                    fc.addView(maxAdView);
                    maxAdView.loadAd();
                    break;

                case STARTAPP:
                    if (getV() == null) {
                        fc = a.findViewById(R.id.rel_container);
                    } else {
                        fc = getV().findViewById(R.id.rel_container);
                    }
                    com.startapp.sdk.ads.banner.Banner banner = new com.startapp.sdk.ads.banner.Banner(a, new BannerListener() {
                        @Override
                        public void onReceiveAd(View view) {
                            fc.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailedToReceiveAd(View view) {
                            Log.e(TAG, "STARTAPP_BANNER: " + view);
                            fc.setVisibility(View.GONE);
                        }

                        @Override
                        public void onImpression(View view) {

                        }

                        @Override
                        public void onClick(View view) {

                        }
                    });
                    fc.addView(banner);
                    break;

                case UNITY:
                    if (au == null) break;
                    if (getV() == null) {
                        fc = a.findViewById(R.id.rel_container);
                    } else {
                        fc = getV().findViewById(R.id.rel_container);
                    }
                    BannerView ubanner = new BannerView(a, au, new UnityBannerSize(320, 50));
                    ubanner.setListener(new BannerView.Listener() {
                        @Override
                        public void onBannerLoaded(BannerView bannerAdView) {
                            super.onBannerLoaded(bannerAdView);
                            fc.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onBannerFailedToLoad(BannerView bannerAdView, BannerErrorInfo errorInfo) {
                            super.onBannerFailedToLoad(bannerAdView, errorInfo);
                            Log.e(TAG, "STARTAPP_BANNER: " + errorInfo);
                            fc.setVisibility(View.GONE);
                        }

                        @Override
                        public void onBannerClick(BannerView bannerAdView) {
                            super.onBannerClick(bannerAdView);
                        }

                        @Override
                        public void onBannerLeftApplication(BannerView bannerAdView) {
                            super.onBannerLeftApplication(bannerAdView);
                        }
                    });
                    fc.addView(ubanner);
                    ubanner.load();
                    break;
            }

        }
    }
}
