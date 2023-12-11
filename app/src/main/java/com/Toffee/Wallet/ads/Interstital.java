package com.Toffee.Wallet.ads;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.Toffee.Wallet.utils.AdsConfig;
import com.Toffee.Wallet.utils.Pref;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

public class Interstital implements AdsConfig {
    public static class Builder {
        private static final String TAG = "AdNetwork__Interstital";
        private String au;
        private String iT;
        Activity a;
        InterstitialAd fi;
        com.google.android.gms.ads.interstitial.InterstitialAd ai;
        MaxInterstitialAd mi;
        StartAppAd si;
        private String AdcolonyAppid,AdcolonyZone;
        private AdColonyInterstitial adcolonyInt;
        private AdColonyInterstitialListener adColonyInterstitialListener;
        public Builder(Activity act) {
            this.a = act;
            pref=new Pref(a);
        }
        Pref pref;


        public void loadInterstital() {
            au=pref.getString(interstital_adunit);
            if (pref.getString(interstital_adunit) != null) {
                switch (pref.getString(interstitalType)) {
                    case "off":
                        Log.e(TAG, "Ad disabled: ");
                        break;
                    case ADMOB:
                        com.google.android.gms.ads.interstitial.InterstitialAd.load(a, au, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                                ai=interstitialAd;
                                ai.show(a);
                                ai.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {

                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                        Log.d(TAG, "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        ai = null;
                                        Log.d(TAG, "The ad was shown.");
                                    }
                                });
                                Log.i(TAG, "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.i(TAG, " AdMob : " + loadAdError.getMessage());
                                ai = null;
                            }
                        });
                        break;

                    case FB:
                        fi = new InterstitialAd(a, au);
                        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(Ad ad) {
                            }

                            @Override
                            public void onInterstitialDismissed(Ad ad) {
                            }

                            @Override
                            public void onError(Ad ad, AdError adError) {
                                fi.loadAd();
                                Log.e(TAG, "onError: " + adError.getErrorMessage());
                            }

                            @Override
                            public void onAdLoaded(Ad ad) {
                                fi.show();
                            }

                            @Override
                            public void onAdClicked(Ad ad) {
                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {
                                Log.d("TAG", "Interstitial ad impression logged!");
                            }
                        };
                        fi.loadAd(
                                fi.buildLoadAdConfig()
                                        .withAdListener(interstitialAdListener)
                                        .build());
                        break;

                    case APPLOVIN:
                        mi = new MaxInterstitialAd(au, a);
                        mi.setListener(new MaxAdListener() {
                            @Override
                            public void onAdLoaded(MaxAd ad) {
                                Log.e(TAG, "onAdLoaded: ");
                                mi.showAd();
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

                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                                mi.loadAd();
                            }
                        });
                        mi.loadAd();
                        break;

                    case STARTAPP:
                        si = new StartAppAd(a);
                        si.loadAd(new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull com.startapp.sdk.adsbase.Ad ad) {
                                Log.e(TAG, "onReceiveAd: startapp");
                                si.showAd();
                            }

                            @Override
                            public void onFailedToReceiveAd(@Nullable com.startapp.sdk.adsbase.Ad ad) {
                                Log.e(TAG, "onFailedToReceiveAd: " + ad.errorMessage);
                                loadInterstital();
                            }
                        });
                        break;

                    case UNITY:
                        UnityAds.load(au, new IUnityAdsLoadListener() {
                            @Override
                            public void onUnityAdsAdLoaded(String placementId) {
                                Log.d(TAG, "unity interstitial ad loaded");
                                UnityAds.show(a, au, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                                    @Override
                                    public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                                        Log.e(TAG, "onUnityAdsShowFailure: unity failder");
                                    }

                                    @Override
                                    public void onUnityAdsShowStart(String placementId) {

                                    }

                                    @Override
                                    public void onUnityAdsShowClick(String placementId) {

                                    }

                                    @Override
                                    public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {

                                    }
                                });
                            }

                            @Override
                            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                                Log.e(TAG, "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
                                loadInterstital();
                            }
                        });
                        break;

                    case ad_Adcolony:
                        AdcolonyAppid=pref.getString(adcolonyApp);
                        AdcolonyZone=pref.getString(adcolonyZone);
                        if (AdcolonyAppid != null && AdcolonyZone != null) {
                            AdColonyAppOptions appOptions = new AdColonyAppOptions().setGDPRConsentString("1")
                                    .setKeepScreenOn(true).setGDPRRequired(true);
                            AdColony.configure(a, appOptions, AdcolonyAppid, AdcolonyZone);
                            AdColonyAdOptions options = new AdColonyAdOptions().enableConfirmationDialog(false).enableResultsDialog(false);
                            adColonyInterstitialListener = new AdColonyInterstitialListener() {
                                @Override
                                public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
                                    adColonyInterstitial.show();
                                }
                                @Override
                                public void onRequestNotFilled(AdColonyZone zone) {
                                    Log.e(TAG, "onRequestNotFilled: " + zone);
                                }
                                @Override
                                public void onClosed(AdColonyInterstitial ad) {
                                }
                            };
                            AdColony.requestInterstitial(AdcolonyZone, adColonyInterstitialListener, options);
                        }
                        break;
                }
            } else {
                if (iT.equalsIgnoreCase(STARTAPP)) {
                    si = new StartAppAd(a);
                    si.loadAd(new AdEventListener() {
                        @Override
                        public void onReceiveAd(@NonNull com.startapp.sdk.adsbase.Ad ad) {
                            si.showAd();
                            Log.e(TAG, "onReceiveAd: startapp");
                        }
                        @Override
                        public void onFailedToReceiveAd(@Nullable com.startapp.sdk.adsbase.Ad ad) {
                            Log.e(TAG, "onFailedToReceiveAd: " + ad.errorMessage);
                            loadInterstital();
                        }
                    });
                }
            }
        }
    }
}
