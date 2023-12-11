package com.Toffee.Wallet.ads;

import android.app.Activity;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.Toffee.Wallet.utils.AdsConfig;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.chartboost.sdk.ads.Rewarded;
import com.chartboost.sdk.callbacks.RewardedCallback;
import com.chartboost.sdk.events.CacheError;
import com.chartboost.sdk.events.CacheEvent;
import com.chartboost.sdk.events.ClickError;
import com.chartboost.sdk.events.ClickEvent;
import com.chartboost.sdk.events.DismissEvent;
import com.chartboost.sdk.events.ImpressionEvent;
import com.chartboost.sdk.events.RewardEvent;
import com.chartboost.sdk.events.ShowError;
import com.chartboost.sdk.events.ShowEvent;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;
import com.vungle.warren.InitCallback;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.error.VungleException;

public class RewardAds {
    public static class Builder implements AdsConfig {
        private static final String TAG = "AdNetwork__Rewarded";
        Pref pref;
        boolean unityadLoaded;
        public boolean isCompleted, rewardMode;
        Activity a;
        RewardedAd admobRewarded;
        RewardedVideoAd facebookReward;
        MaxRewardedAd applovinMaxReward;
        StartAppAd startAppAd;
        boolean isAdLoaded=false;
        Rewarded chartboostRewarded;
        private AdColonyInterstitial adColonyInt;
        private AdColonyInterstitialListener listener;
        int adType;

        public Builder(Activity act) {
            this.a = act;
            this.pref = new Pref(act);
        }

        public boolean isAdLoaded() {
            return isAdLoaded;
        }

        public void setAdLoaded(boolean adLoaded) {
            isAdLoaded = adLoaded;
        }

        public boolean isUnity_reward() {
            return pref.getBoolean(isUnity);
        }

        public boolean isAdcolony_reward() {
            return pref.getBoolean(isAdcolony);
        }

        public boolean isFb() {
            return pref.getBoolean(isFb);
        }

        public boolean isApplovin_reward() {
            return pref.getBoolean(isApplovin);
        }

        public boolean isStatartapp_reward() {
            return pref.getBoolean(isStart);
        }

        public Activity getA() {
            return a;
        }

        public Builder buildAd(int AdType) {
            this.adType = AdType;
            loadReward();
            return this;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public void setCompleted(boolean completed) {
            isCompleted = completed;
        }

        public void setA(Activity a) {
            this.a = a;
        }

        public boolean isAdmob_reward() {
            return pref.getBoolean(isAdmob);
        }

        public boolean isChartboost() {
            return pref.getBoolean(isChartboost);
        }

        public boolean isVungle() {
            return pref.getBoolean(isVungle);
        }

        public boolean isUnityadLoaded() {
            return unityadLoaded;
        }

        public void setUnityadLoaded(boolean unityadLoaded) {
            this.unityadLoaded = unityadLoaded;
        }


        private void loadReward() {
            if (adType == 1) {
                loadAllAD();
            } else {
                loadSoloAd();
            }
        }

        private void loadSoloAd() {
            switch (adType) {
                case 2:
                    if (pref.getString(auApplovin) != null) {
                        applovinMaxReward = MaxRewardedAd.getInstance(pref.getString(auApplovin), a);
                        applovinMaxReward.setListener(new MaxRewardedAdListener() {
                            @Override
                            public void onRewardedVideoStarted(MaxAd ad) {
                            }

                            @Override
                            public void onRewardedVideoCompleted(MaxAd ad) {
                            }

                            @Override
                            public void onUserRewarded(MaxAd ad, MaxReward reward) {
                                Fun.log("applovin_on_user rewarded");
                                setCompleted(true);
                            }

                            @Override
                            public void onAdLoaded(MaxAd ad) {
                                setAdLoaded(true);
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
                                Fun.log("MaxRewardedAd Ad  onAdLoadFailed: " + error);
                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                            }
                        });
                        applovinMaxReward.loadAd();
                    } else {
                        Fun.msgError(a, "Placement Id is Null");
                    }
                    break;

                case 3:
                    if (pref.getString(auUnity) != null) {
                        UnityAds.load(pref.getString(auUnity), new IUnityAdsLoadListener() {
                            @Override
                            public void onUnityAdsAdLoaded(String placementId) {
                                setAdLoaded(true);
                                setUnityadLoaded(true);
                                Fun.log("Unity Reward onUnityAdsAdLoaded");
                            }

                            @Override
                            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                                Fun.log("Unity Reward Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] \" + message");
                            }
                        });
                    } else {
                        Fun.msgError(a, "Placement Id is Null");
                    }
                    break;

                case 4:
                    if (pref.getString(auAdmob) != null) {
                        AdRequest adRequest = new AdRequest.Builder().build();
                        RewardedAd.load(a, pref.getString(auAdmob),
                                adRequest, new RewardedAdLoadCallback() {
                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                        Fun.log("AdmobReward onAdFailedToLoad " + loadAdError.toString());
                                    }

                                    @Override
                                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                                        setAdLoaded(true);
                                        admobRewarded = rewardedAd;
                                        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                            @Override
                                            public void onAdClicked() {
                                            }

                                            @Override
                                            public void onAdDismissedFullScreenContent() {
                                            }

                                            @Override
                                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                                Fun.log("AdmobReward onAdFailedToShowFullScreenContent ");

                                            }

                                            @Override
                                            public void onAdImpression() {
                                            }

                                            @Override
                                            public void onAdShowedFullScreenContent() {
                                                Fun.log("AdmobReward onAdShowedFullScreenContent ");
                                            }
                                        });
                                        Fun.log("AdmobReward Ad was loaded. ");
                                    }
                                });
                    } else {
                        Fun.msgError(a, "Placement Id is Null");
                    }
                    break;

                case 5:
                    if (pref.getString(adcolonyApp) != null && pref.getString(adcolonyZone) != null) {
                        AdColonyAppOptions appOptions = new AdColonyAppOptions().setGDPRConsentString("1")
                                .setKeepScreenOn(true).setGDPRRequired(false);

                        AdColony.configure(a, appOptions, pref.getString(adcolonyApp), pref.getString(adcolonyZone));
                        AdColonyAdOptions options = new AdColonyAdOptions().enableConfirmationDialog(false).enableResultsDialog(false);
                        AdColony.setRewardListener(adColonyReward -> {
                            setCompleted(true);
                            Fun.log("Adcolony Reward Credit");
                        });

                        listener = new AdColonyInterstitialListener() {
                            @Override
                            public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
                                setAdLoaded(true);
                                adColonyInt = adColonyInterstitial;
                            }

                            @Override
                            public void onRequestNotFilled(AdColonyZone zone) {
                                Fun.log("Adcolony  onRequestNotFilled: " + zone);
                            }

                            @Override
                            public void onClosed(AdColonyInterstitial ad) {
                            }
                        };
                        AdColony.requestInterstitial(adcolonyZone, listener, options);
                    } else {
                        Fun.msgError(a, "Placement Id is Null");
                    }
                    break;

                case 6:
                    if (pref.getString(auVungle) != null && pref.getString(vungleAppid) != null) {
                        Vungle.loadAd(this.auVungle, new LoadAdCallback() {
                            public void onAdLoad(String placementReferenceId) {
                                setAdLoaded(true);
                                Fun.log(" " + "Vungle onAdLoad: ");
                            }

                            public void onError(String placementReferenceId, VungleException exception) {
                                Fun.log(" " + "Vungle : " + "Vungle onError: " + exception.getMessage());
                            }
                        });
                    } else {
                        Fun.msgError(a, "Placement Id is Null");
                    }
                    break;

                case 7:
                    if (pref.getString(auCharboost) != null && pref.getString(chartboostSignature) != null) {
                        chartboostRewarded = new Rewarded(pref.getString(auCharboost), new RewardedCallback() {
                            @Override
                            public void onRewardEarned(@NonNull RewardEvent rewardEvent) {
                                Fun.log("chartboostRewarded onRewardEarned");
                                setCompleted(true);
                            }

                            @Override
                            public void onAdDismiss(@NonNull DismissEvent dismissEvent) {

                            }

                            @Override
                            public void onAdLoaded(@NonNull CacheEvent cacheEvent, @Nullable CacheError cacheError) {
                                setAdLoaded(true);
                                Fun.log("chartboostRewarded onAdLoaded");
                            }

                            @Override
                            public void onAdRequestedToShow(@NonNull ShowEvent showEvent) {

                            }

                            @Override
                            public void onAdShown(@NonNull ShowEvent showEvent, @Nullable ShowError showError) {

                            }

                            @Override
                            public void onAdClicked(@NonNull ClickEvent clickEvent, @Nullable ClickError clickError) {

                            }

                            @Override
                            public void onImpressionRecorded(@NonNull ImpressionEvent impressionEvent) {

                            }
                        }, null);
                    } else {
                        Fun.msgError(a, "Placement Id is Null");
                    }
                    break;

                case 8:
                    if (pref.getString(auFb) != null) {
                        facebookReward = new RewardedVideoAd(a, pref.getString(auFb));
                        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
                            @Override
                            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {
                                Fun.log("Facebook Ad Error " + adError.getErrorMessage());
                            }

                            @Override
                            public void onAdLoaded(com.facebook.ads.Ad ad) {
                                setAdLoaded(true);
                                Fun.log("Facebook onAdLoaded");

                            }

                            @Override
                            public void onAdClicked(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(com.facebook.ads.Ad ad) {

                            }


                            @Override
                            public void onRewardedVideoCompleted() {
                                setCompleted(true);
                                Fun.log("Fb Rewarded video completed!");
                            }

                            @Override
                            public void onRewardedVideoClosed() {
                                Fun.log("Fb Rewarded video ad closed!");
                            }
                        };
                        facebookReward.loadAd(
                                facebookReward.buildLoadAdConfig()
                                        .withAdListener(rewardedVideoAdListener)
                                        .build());
                    } else {
                        Fun.msgError(a, "Placement Id is Null");
                    }
                    break;

                case 9:
                    if (pref.getString(startapp_appid) != null) {
                        startAppAd = new StartAppAd(a);
                        startAppAd.setVideoListener(() -> {
                            setCompleted(true);
                        });
                        startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
                            @Override
                            public void onReceiveAd(Ad ad) {
                                setAdLoaded(true);
                                Fun.log("StartApp Rewarded Ad Loaded");
                            }

                            @Override
                            public void onFailedToReceiveAd(Ad ad) {
                                Fun.log("StartApp Rewarded Ad onFailedToReceiveAd " + ad.errorMessage);
                            }
                        });
                    }
                    break;
            }
        }

        private void loadAllAD() {
            if (!isAdLoaded() && isUnity_reward() && pref.getString(auUnity) != null) {
                UnityAds.load(pref.getString(auUnity), new IUnityAdsLoadListener() {
                    @Override
                    public void onUnityAdsAdLoaded(String placementId) {
                        setAdLoaded(true);
                        setUnityadLoaded(true);
                        Fun.log("Unity Reward onUnityAdsAdLoaded");
                    }

                    @Override
                    public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                        Fun.log("Unity Reward Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] \" + message");
                    }
                });
            }

            if (!isAdLoaded && isChartboost() && pref.getString(auCharboost) != null && pref.getString(chartboostSignature) != null) {
                chartboostRewarded = new Rewarded(pref.getString(auCharboost), new RewardedCallback() {
                    @Override
                    public void onRewardEarned(@NonNull RewardEvent rewardEvent) {
                        Fun.log("chartboostRewarded onRewardEarned");
                        setCompleted(true);
                    }

                    @Override
                    public void onAdDismiss(@NonNull DismissEvent dismissEvent) {

                    }

                    @Override
                    public void onAdLoaded(@NonNull CacheEvent cacheEvent, @Nullable CacheError cacheError) {
                        Fun.log("chartboostRewarded onAdLoaded cache error "+cacheError.toString());

                        setAdLoaded(true);
                    }

                    @Override
                    public void onAdRequestedToShow(@NonNull ShowEvent showEvent) {

                    }

                    @Override
                    public void onAdShown(@NonNull ShowEvent showEvent, @Nullable ShowError showError) {

                    }

                    @Override
                    public void onAdClicked(@NonNull ClickEvent clickEvent, @Nullable ClickError clickError) {

                    }

                    @Override
                    public void onImpressionRecorded(@NonNull ImpressionEvent impressionEvent) {

                    }

                }, null);

            }

            if (!isAdLoaded && isVungle() && pref.getString(auVungle) != null) {
                if (!Vungle.isInitialized()) {
                    Vungle.init(pref.getString(vungleAppid), a, new InitCallback() {
                        @Override
                        public void onSuccess() {
                            Fun.log("Vungle SDK Init Successful ");
                        }

                        @Override
                        public void onError(VungleException exception) {
                            Fun.log("Vungle  SDK onError " + exception.toString());
                        }

                        @Override
                        public void onAutoCacheAdAvailable(String pid) {
                        }
                    });
                }
                Vungle.loadAd(auVungle, new LoadAdCallback() {
                    public void onAdLoad(String placementReferenceId) {
                        Fun.log(" " + "Vungle onAdLoad: ");
                        setAdLoaded(true);
                    }

                    public void onError(String placementReferenceId, VungleException exception) {
                        Fun.log(" " + "Vungle : " + "Vungle onError: " + exception.getLocalizedMessage());
                    }
                });
            }

            if (!isAdLoaded && isAdmob_reward() && pref.getString(auAdmob) != null) {
                AdRequest adRequest = new AdRequest.Builder().build();
                RewardedAd.load(a, pref.getString(auAdmob),
                        adRequest, new RewardedAdLoadCallback() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Fun.log("AdmobReward onAdFailedToLoad " + loadAdError.toString());
                            }

                            @Override
                            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                                setAdLoaded(true);
                                admobRewarded = rewardedAd;
                                rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdClicked() {
                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        Fun.log("AdmobReward onAdFailedToShowFullScreenContent ");

                                    }

                                    @Override
                                    public void onAdImpression() {
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        Fun.log("AdmobReward onAdShowedFullScreenContent ");
                                    }
                                });
                                Fun.log("AdmobReward Ad was loaded. ");
                            }
                        });
            }

            if (!isAdLoaded && isApplovin_reward() && pref.getString(auApplovin) != null) {
                applovinMaxReward = MaxRewardedAd.getInstance(pref.getString(auApplovin), a);
                applovinMaxReward.setListener(new MaxRewardedAdListener() {
                    @Override
                    public void onRewardedVideoStarted(MaxAd ad) {
                    }

                    @Override
                    public void onRewardedVideoCompleted(MaxAd ad) {
                    }

                    @Override
                    public void onUserRewarded(MaxAd ad, MaxReward reward) {
                        setCompleted(true);
                        Fun.log("applovin_on_user rewarded");
                    }

                    @Override
                    public void onAdLoaded(MaxAd ad) {
                        setAdLoaded(true);
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
                        Fun.log("Applovin Ad  onAdLoadFailed: " + error);
                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                    }
                });
                applovinMaxReward.loadAd();
            }

            if (!isAdLoaded && isFb() && pref.getString(auFb) != null) {
                facebookReward = new RewardedVideoAd(a, pref.getString(auFb));
                RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
                    @Override
                    public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {
                        Fun.log("Facebook Ad Error " + adError.getErrorMessage());
                    }

                    @Override
                    public void onAdLoaded(com.facebook.ads.Ad ad) {
                        setAdLoaded(true);
                        Fun.log("Facebook onAdLoaded");

                    }

                    @Override
                    public void onAdClicked(com.facebook.ads.Ad ad) {

                    }

                    @Override
                    public void onLoggingImpression(com.facebook.ads.Ad ad) {

                    }


                    @Override
                    public void onRewardedVideoCompleted() {
                        setCompleted(true);
                        Fun.log("Fb Rewarded video completed!");
                    }

                    @Override
                    public void onRewardedVideoClosed() {
                        Fun.log("Fb Rewarded video ad closed!");
                    }
                };
                facebookReward.loadAd(
                        facebookReward.buildLoadAdConfig()
                                .withAdListener(rewardedVideoAdListener)
                                .build());
            }

            if (!isAdLoaded && isAdcolony_reward() && pref.getString(adcolonyApp) != null && pref.getString(adcolonyZone) != null) {
                AdColonyAppOptions appOptions = new AdColonyAppOptions().setGDPRConsentString("1")
                        .setKeepScreenOn(true).setGDPRRequired(false);

                AdColony.configure(a, appOptions, pref.getString(adcolonyApp), pref.getString(adcolonyZone));
                AdColonyAdOptions options = new AdColonyAdOptions().enableConfirmationDialog(false).enableResultsDialog(false);
                AdColony.setRewardListener(adColonyReward -> {
                    setCompleted(true);
                    Fun.log("Adcolony Reward Credit");
                });

                listener = new AdColonyInterstitialListener() {
                    @Override
                    public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
                        setAdLoaded(true);
                        adColonyInt = adColonyInterstitial;
                    }

                    @Override
                    public void onRequestNotFilled(AdColonyZone zone) {
                        Fun.log("Adcolony  onRequestNotFilled: " + zone);
                    }

                    @Override
                    public void onClosed(AdColonyInterstitial ad) {
                    }
                };
                AdColony.requestInterstitial(adcolonyZone, listener, options);
            }

            if (!isAdLoaded && isStatartapp_reward() && pref.getString(startapp_appid) != null) {
                startAppAd = new StartAppAd(a);
                startAppAd.setVideoListener(() -> {
                    setCompleted(true);
                });
                startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
                    @Override
                    public void onReceiveAd(Ad ad) {
                        setAdLoaded(true);
                        Fun.log("StartApp Rewarded Ad Loaded");
                    }

                    @Override
                    public void onFailedToReceiveAd(Ad ad) {
                        Fun.log("StartApp Rewarded Ad onFailedToReceiveAd " + ad.errorMessage);
                    }
                });
            }
        }

        public void showReward() {
            if (adType == 1) {
                showAllReward();
            } else {
                showSoloReward();
            }
        }

        private void showAllReward() {
            if (isUnity_reward() && isAdLoaded && isUnityadLoaded() && pref.getString(auUnity) != null) {
                Fun.log("ad_come_tag come in unity");
                UnityAds.show(a, pref.getString(auUnity), new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                    @Override
                    public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                    }

                    @Override
                    public void onUnityAdsShowStart(String placementId) {
                        setUnityadLoaded(false);
                        new Handler().postDelayed(() -> setCompleted(true),5000);
                        Fun.log("unity ads showing start");
                    }

                    @Override
                    public void onUnityAdsShowClick(String placementId) {
                    }

                    @Override
                    public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                        Fun.log("unity ads showing onUnityAdsShowComplete");
                    }
                });

            }

            else if (isChartboost() && pref.getString(auCharboost) != null && chartboostRewarded != null && chartboostRewarded.isCached()) {
                Fun.log("ad_come_tag come in chartboost "+chartboostRewarded.isCached());
                chartboostRewarded.show();
            } else if (isVungle() && pref.getString(auVungle) != null && Vungle.canPlayAd(pref.getString(auVungle))) {
                Fun.log("ad_come_tag come in Vungle");
                Vungle.playAd(pref.getString(auVungle), null, new PlayAdCallback() {
                    @Override
                    public void creativeId(String creativeId) {

                    }

                    @Override
                    public void onAdStart(String placementId) {

                    }

                    @Override
                    public void onAdEnd(String placementId, boolean completed, boolean isCTAClicked) {
                        if (completed) {
                            setCompleted(true);
                        }
                    }

                    @Override
                    public void onAdEnd(String placementId) {

                    }

                    @Override
                    public void onAdClick(String placementId) {

                    }

                    @Override
                    public void onAdRewarded(String placementId) {
                    }

                    @Override
                    public void onAdLeftApplication(String placementId) {

                    }

                    @Override
                    public void onError(String placementId, VungleException exception) {
                        Fun.log("Vungle Ad Error " + exception.getMessage());
                    }

                    @Override
                    public void onAdViewed(String placementId) {

                    }
                });
            } else if (isAdmob_reward() && admobRewarded != null && pref.getString(auAdmob) != null) {
                Fun.log("ad_come_tag come in admob");
                admobRewarded.show(a, rewardItem -> {
                    setCompleted(true);
                });
            } else if (isApplovin_reward() && applovinMaxReward.isReady() && pref.getString(auApplovin) != null) {
                Fun.log("ad_come_tag come in applovin");
                applovinMaxReward.showAd(pref.getString(auApplovin));
            } else if (isFb() && pref.getString(auFb) != null && facebookReward.isAdLoaded()) {
                Fun.log("ad_come_tag come in facebook ads");
                facebookReward.show();
            } else if (isAdcolony_reward() && adColonyInt != null && pref.getString(adcolonyZone) != null && pref.getString(adcolonyApp) != null) {
                Fun.log("ad_come_tag come in adcolony");
                adColonyInt.show();
            } else if (isStatartapp_reward() && startAppAd.isReady()) {
                Fun.log("ad_come_tag come in startapp");
                startAppAd.showAd();
            } else {
                setCompleted(false);
                Fun.msgError(a, "No Ads Available Try Again !!");
            }
        }

        private void showSoloReward() {
            switch (adType) {
                case 2:
                    if (applovinMaxReward.isReady() && pref.getString(auApplovin) != null) {
                        applovinMaxReward.showAd(pref.getString(auApplovin));
                    }
                    break;

                case 3:
                    if (isUnityadLoaded() && pref.getString(auUnity) != null) {
                        UnityAds.show(a, pref.getString(auUnity), new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                            @Override
                            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                            }

                            @Override
                            public void onUnityAdsShowStart(String placementId) {
                                setUnityadLoaded(false);
                                new Handler().postDelayed(() -> setCompleted(true),5000);
                            }

                            @Override
                            public void onUnityAdsShowClick(String placementId) {
                            }

                            @Override
                            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                            }
                        });
                    }
                    break;

                case 4:
                    if (admobRewarded != null && pref.getString(auAdmob) != null) {
                        admobRewarded.show(a, rewardItem -> {
                            setCompleted(true);
                        });
                    }
                    break;

                case 5:
                    if (adColonyInt != null && pref.getString(adcolonyZone) != null && pref.getString(adcolonyApp) != null) {
                        adColonyInt.show();
                    }
                    break;

                case 6:
                    if (pref.getString(auVungle) != null && Vungle.canPlayAd(pref.getString(auVungle))) {
                        Vungle.playAd(pref.getString(auVungle), null, new PlayAdCallback() {
                            @Override
                            public void creativeId(String creativeId) {

                            }

                            @Override
                            public void onAdStart(String placementId) {

                            }

                            @Override
                            public void onAdEnd(String placementId, boolean completed, boolean isCTAClicked) {

                            }

                            @Override
                            public void onAdEnd(String placementId) {

                            }

                            @Override
                            public void onAdClick(String placementId) {

                            }

                            @Override
                            public void onAdRewarded(String placementId) {
                                setCompleted(true);
                            }

                            @Override
                            public void onAdLeftApplication(String placementId) {

                            }

                            @Override
                            public void onError(String placementId, VungleException exception) {
                                Fun.log("Vungle Ad Error " + exception.getMessage());
                            }

                            @Override
                            public void onAdViewed(String placementId) {

                            }
                        });
                    }
                    break;

                case 7:
                    if (pref.getString(auCharboost) != null && chartboostRewarded != null  && chartboostRewarded.isCached()) {
                        chartboostRewarded.show();
                    }
                    break;

                case 8:
                    if (pref.getString(auFb) != null && facebookReward.isAdLoaded()) {
                        facebookReward.show();
                    }
                    break;

                case 9:
                    if (isStatartapp_reward() && startAppAd.isReady()) {
                        startAppAd.showAd();
                    }
                    break;
            }
        }

    }
}
