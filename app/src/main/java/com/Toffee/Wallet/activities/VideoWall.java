package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.Fun.ClaimBonus;
import static com.Toffee.Wallet.utils.Fun.js;
import static com.Toffee.Wallet.utils.Fun.msgError;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.Toffee.Wallet.fragment.FaqBottomDialogFragment;
import com.Toffee.Wallet.utils.AdsConfig;
import com.Toffee.Wallet.BuildConfig;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.Responsemodel.FaqResp;
import com.Toffee.Wallet.Responsemodel.VideowallResp;
import com.Toffee.Wallet.adapters.FaqAdapter;
import com.Toffee.Wallet.databinding.ActivityVideoWallBinding;
import com.Toffee.Wallet.databinding.LayoutProgressLoadingBinding;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.chartboost.sdk.Chartboost;
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
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.LevelPlayRewardedVideoListener;
import com.squareup.picasso.Picasso;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
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

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoWall extends AppCompatActivity implements OnItemClickListener, AdsConfig {
    public String TAG = "VideoWall : ";
    ActivityVideoWallBinding bind;
    Activity activity;
    VideoWallAdapter adapter;
    List<VideowallResp.DataItem> videolist;
    MaxRewardedAd maxRewardedAd;
    String admobAdUnit, fbAdUnit, unityAdUnit, applovinAdunit, charBoostAppid, chartBoostSign, chartBoostLocation, vungleAppId, vunglePlacement, startAppid, adColonyZone, adColonyApp, irounSourceAppKey, ironSourcePlacement;
    RewardedAd admobReward;
    Rewarded chartboostRewarded;
    boolean unityLoaded = false, vungleLoaded, vungleSdk;
    AdColonyInterstitialListener adColonyInterstitialListener;
    AdColonyAdOptions adOptions;
    AdColonyInterstitial adcolonyInt;
    StartAppAd startAppAd;
    AlertDialog dialog;
    RewardedVideoAd facebookReward;
    BottomSheetDialog faqBottomSheetDialog;
    String id;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityVideoWallBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());
        activity = this;

        dialog = Fun.loadingProgress(activity);
        pref = new Pref(activity);

        bind.tool.title.setText(Const.TOOLBAR_TITLE);

        videolist = new ArrayList<>();
        bind.rv.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new VideoWallAdapter(activity, videolist);
        adapter.setClickListener(this);
        bind.rv.setAdapter(adapter);

        getVideos();

        bind.tool.faq.setOnClickListener(v -> {
            Const.FAQ_TYPE="videowall";
            FaqBottomDialogFragment fragment =  FaqBottomDialogFragment.newInstance();
            fragment.show(getSupportFragmentManager(),FaqBottomDialogFragment.TAG);
        });


        bind.tool.back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public void onBackPressed() {
        activity.finish();
        super.onBackPressed();
    }

    private void getVideos() {
        ApiClient.restAdapter(activity).create(ApiInterface.class).videowall().enqueue(new Callback<VideowallResp>() {
            @Override
            public void onResponse(Call<VideowallResp> call, Response<VideowallResp> response) {
                if (response.isSuccessful() && response.body().getCode().equals("201")) {
                    bind.rv.setVisibility(View.VISIBLE);
                    videolist.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    bind.shimmerView.setVisibility(View.GONE);
                } else if (response.isSuccessful() && response.body().getCode().equals("202")) {
                    upgradeAccount();
                } else {
                    bind.shimmerView.setVisibility(View.GONE);
                    bind.layoutNoResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<VideowallResp> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View view, int position) {
        id = videolist.get(position).getId();
        switch (videolist.get(position).getType()) {
            case ad_Applovin:
                initApplovin();
                break;

            case ad_Unity:
                initUnity();
                break;

            case ad_Chartboost:
                initChartBoost();
                break;

            case ad_Admob:
                initAdmob();
                break;

            case ad_Adcolony:
                initAdcolony();
                break;

            case ad_Vungle:
                initVungle();
                break;

            case ad_Ironsource:
                initIronSource();
                break;

            case ad_Startapp:
                initStartApp();
                break;

            case ad_Fb:
                initFb();
                break;

        }

    }

    private void initFb() {
        if (facebookReward.isAdLoaded()) {
            facebookReward.show();
        } else {
            showLoading();
            loadFb();
            new Handler().postDelayed(() -> {
                hideLoading();
                if (facebookReward.isAdLoaded()) {
                    facebookReward.show();
                } else {
                    adNotAvailable();
                }
            }, AD_LOADING_INTERVAL);
        }
    }

    void loadFb() {
        facebookReward = new RewardedVideoAd(activity, fbAdUnit);
        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {
                Fun.log("Facebook Ad Error " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {
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
                creditVideo();
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

    void loadStartApp() {
        StartAppSDK.init(activity, startAppid);
        startAppAd = new StartAppAd(activity);
        startAppAd.setVideoListener(this::creditVideo);
        startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {

            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
            }
        });
    }

    private void initStartApp() {
        if (startAppAd != null) {
            startAppAd.showAd();
            creditVideo();
        } else {
            showLoading();
            loadStartApp();
            new Handler().postDelayed(() -> {
                hideLoading();
                if (startAppAd != null) {
                    startAppAd.showAd();
                    creditVideo();
                } else {
                    adNotAvailable();
                }
            }, AD_LOADING_INTERVAL);
        }

    }

    private void loadIronSource() {
        IronSource.setLevelPlayRewardedVideoListener(new LevelPlayRewardedVideoListener() {
            @Override
            public void onAdAvailable(AdInfo adInfo) {
            }

            @Override
            public void onAdUnavailable() {
            }

            @Override
            public void onAdOpened(AdInfo adInfo) {
            }

            @Override
            public void onAdClosed(AdInfo adInfo) {
            }

            @Override
            public void onAdRewarded(Placement placement, AdInfo adInfo) {
                creditVideo();
            }

            @Override
            public void onAdShowFailed(IronSourceError error, AdInfo adInfo) {
            }

            @Override
            public void onAdClicked(Placement placement, AdInfo adInfo) {
            }
        });
        IronSource.setMetaData("is_child_directed", "false");
        IronSource.init(this, irounSourceAppKey, IronSource.AD_UNIT.REWARDED_VIDEO);
        IronSource.loadRewardedVideo();
        if (BuildConfig.DEBUG) {
            IntegrationHelper.validateIntegration(this);
        }

    }

    private void initIronSource() {
        if (IronSource.isRewardedVideoAvailable()) {
            IronSource.showRewardedVideo();
        } else {
            showLoading();
            loadIronSource();
            new Handler().postDelayed(() -> {
                hideLoading();
                if (IronSource.isRewardedVideoAvailable()) {
                    IronSource.showRewardedVideo();
                } else {
                    adNotAvailable();
                }
            }, AD_LOADING_INTERVAL);
        }
    }

    private void loadVungle() {
        Vungle.loadAd(vunglePlacement, new LoadAdCallback() {
            public void onAdLoad(String placementReferenceId) {
                Fun.log("Vungle Ads onAdLoad");
                vungleLoaded = true;
            }

            public void onError(String placementReferenceId, VungleException exception) {
                Fun.log("Vungle  Vungle SDK  onError: " + exception.getMessage());

            }
        });

    }

    private void initVungle() {
        if (Vungle.canPlayAd(vunglePlacement)) {
            Vungle.playAd(vunglePlacement, null, new PlayAdCallback() {
                public void creativeId(String creativeId) {
                }

                public void onAdStart(String placementReferenceId) {

                }

                public void onAdEnd(String placementReferenceId, boolean completed, boolean isCTAClicked) {
                    Fun.log("Vungle Ads onAdEnd1");
                    if (completed) {
                        creditVideo();
                    }
                }

                public void onAdEnd(String placementId) {
                    Fun.log("Vungle Ads onAdEnd2");
                }

                public void onAdClick(String placementId) {
                }

                public void onAdRewarded(String placementId) {

                }

                public void onAdLeftApplication(String placementId) {
                }

                public void onError(String placementReferenceId, VungleException exception) {
                    Fun.log("Vungle Ads PLay Error: " + exception.getMessage());
                }

                public void onAdViewed(String placementId) {
                    Fun.log("Vungle Ads Vungle Ads onAdViewed");
                }
            });
        } else {
            showLoading();
            loadVungle();
            new Handler().postDelayed(() -> {
                hideLoading();
                if (Vungle.canPlayAd(vunglePlacement)) {
                    Vungle.playAd(vunglePlacement, null, new PlayAdCallback() {
                        public void creativeId(String creativeId) {
                        }

                        public void onAdStart(String placementReferenceId) {

                        }

                        public void onAdEnd(String placementReferenceId, boolean completed, boolean isCTAClicked) {
                            Fun.log("Rewarded Ad Vungle Ads Vungle Ads onAdEnd1");

                        }

                        public void onAdEnd(String placementId) {
                            Fun.log("Rewarded Ad Vungle Ads onAdEnd2");

                        }

                        public void onAdClick(String placementId) {
                        }

                        public void onAdRewarded(String placementId) {
                            creditVideo();
                        }

                        public void onAdLeftApplication(String placementId) {
                        }

                        public void onError(String placementReferenceId, VungleException exception) {
                            Fun.log("Rewarded Ad  Vungle Ads PLay Error: " + exception.getMessage());
                        }

                        public void onAdViewed(String placementId) {
                            Fun.log("Rewarded Ad  Vungle Ads onAdViewed");
                        }
                    });
                } else {
                    adNotAvailable();
                }
            }, AD_LOADING_INTERVAL);
        }
    }

    private void loadAdcolony() {
        AdColonyAppOptions appOptions = (new AdColonyAppOptions()).setGDPRConsentString("1").setKeepScreenOn(true).setGDPRRequired(false);
        AdColony.configure(activity, appOptions, adColonyApp);
        adOptions = new AdColonyAdOptions();
        adColonyInterstitialListener = new AdColonyInterstitialListener() {
            public void onRequestFilled(AdColonyInterstitial ad) {
                Fun.log("Adcolony onRequestFilled  ");
                adcolonyInt = ad;
            }

            public void onRequestNotFilled(AdColonyZone zone) {
                Fun.log("Adcolony onRequestNotFilled  " + zone.getZoneID());

            }

            public void onOpened(AdColonyInterstitial ad) {
                creditVideo();
            }

            public void onExpiring(AdColonyInterstitial ad) {
                Fun.log("Adcolony onExpiring");

            }
        };
        AdColony.requestInterstitial(adColonyZone, adColonyInterstitialListener, adOptions);

    }

    private void initAdcolony() {
        if (adcolonyInt != null) {
            adcolonyInt.show();
        } else {
            showLoading();
            loadAdcolony();
            new Handler().postDelayed(() -> {
                hideLoading();
                if (adcolonyInt != null) {
                    adcolonyInt.show();
                } else {
                    adNotAvailable();
                }
            }, AD_LOADING_INTERVAL);
        }
    }

    private void initAdmob() {
        if (admobReward != null) {
            admobReward.show(activity, rewardItem -> {
                creditVideo();
            });
        } else {
            showLoading();
            loadAdmob();
            new Handler().postDelayed(() -> {
                hideLoading();
                if (admobReward != null) {
                    admobReward.show(activity, rewardItem -> {
                        creditVideo();
                    });
                } else {
                    adNotAvailable();
                }
            }, AD_LOADING_INTERVAL);
        }
    }

    void faqDialog(String type) {
        faqBottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_faq, findViewById(R.id.faqLayout), false);
        faqBottomSheetDialog.setContentView(view);
        faqBottomSheetDialog.setCancelable(true);

        LinearLayout shimmerView = view.findViewById(R.id.shimmer_view);
        RelativeLayout noResult = view.findViewById(R.id.layout_no_result);
        RecyclerView rv = view.findViewById(R.id.rv);

        FaqAdapter adapter;
        List<FaqResp> list = new ArrayList<>();

        rv.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new FaqAdapter(activity, list);
        rv.setAdapter(adapter);

        Call<List<FaqResp>> call = ApiClient.restAdapter(activity).create(ApiInterface.class).Faqs(type);
        call.enqueue(new Callback<List<FaqResp>>() {
            @Override
            public void onResponse(Call<List<FaqResp>> call, Response<List<FaqResp>> response) {
                if (response.isSuccessful() && response.body().size() != 0) {
                    rv.setVisibility(View.VISIBLE);
                    list.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    shimmerView.setVisibility(View.GONE);
                } else {
                    shimmerView.setVisibility(View.GONE);
                    noResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<FaqResp>> call, Throwable t) {
            }
        });

        if (!activity.isFinishing()) {
            faqBottomSheetDialog.show();
        }
    }


    private void loadAdmob() {
        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
        RewardedAd.load(this, admobAdUnit, adRequest, new RewardedAdLoadCallback() {
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Fun.log(TAG + " Admob : " + loadAdError.toString());
                admobReward = null;
            }

            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                Fun.log(TAG + "Admob Ad was loaded.");
                admobReward = rewardedAd;
                admobReward.setFullScreenContentCallback(new FullScreenContentCallback() {
                    public void onAdClicked() {
                    }

                    public void onAdDismissedFullScreenContent() {
                        admobReward = null;
                    }

                    public void onAdImpression() {
                    }

                    public void onAdShowedFullScreenContent() {
                    }
                });
            }
        });
    }

    private void initChartBoost() {
        if (chartboostRewarded != null) {
            chartboostRewarded.show();
        } else {
            showLoading();
            loadChartboost();
            new Handler().postDelayed(() -> {
                hideLoading();
                if (chartboostRewarded != null) {
                    chartboostRewarded.show();
                } else {
                    adNotAvailable();
                }
            }, AD_LOADING_INTERVAL);
        }
    }

    private void loadChartboost() {
        Chartboost.startWithAppId(getApplicationContext(), charBoostAppid, chartBoostSign, startError -> {
            if (startError != null) {
                Fun.log(TAG + "loadChartboost onStartCompleted: startError");
            }
        });
        chartboostRewarded = new Rewarded(chartBoostLocation, new RewardedCallback() {
            @Override
            public void onRewardEarned(@NonNull RewardEvent rewardEvent) {
                creditVideo();
            }

            @Override
            public void onAdDismiss(@NonNull DismissEvent dismissEvent) {
            }

            @Override
            public void onAdLoaded(@NonNull CacheEvent cacheEvent, @Nullable CacheError cacheError) {
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

    private void initUnity() {
        if (unityLoaded) {
            UnityAds.show(activity, unityAdUnit, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                @Override
                public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                }

                @Override
                public void onUnityAdsShowStart(String placementId) {
                    unityLoaded = false;
                }

                @Override
                public void onUnityAdsShowClick(String placementId) {
                }

                @Override
                public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                    creditVideo();
                }
            });
        } else {
            showLoading();
            loadUnity();
            new Handler().postDelayed(() -> {
                hideLoading();
                if (unityLoaded) {
                    UnityAds.show(activity, unityAdUnit, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                        @Override
                        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                        }

                        @Override
                        public void onUnityAdsShowStart(String placementId) {
                            unityLoaded = false;
                        }

                        @Override
                        public void onUnityAdsShowClick(String placementId) {
                        }

                        @Override
                        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                            creditVideo();
                        }
                    });
                } else {
                    adNotAvailable();
                }
            }, AD_LOADING_INTERVAL);
        }
    }

    private void loadUnity() {
        UnityAds.load(unityAdUnit, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                unityLoaded = true;
                Fun.log("unity Rewarded ad loaded");
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                Fun.log("Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
            }
        });
    }

    private void initApplovin() {
        if (maxRewardedAd.isReady()) {
            maxRewardedAd.showAd();
        } else {
            showLoading();
            new Handler().postDelayed(() -> {
                hideLoading();
                if (maxRewardedAd.isReady()) {
                    maxRewardedAd.showAd();
                } else {
                    adNotAvailable();
                }
            }, AD_LOADING_INTERVAL);
        }
    }

    private void showLoading() {
        dialog.show();
    }

    private void hideLoading() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }

    private void adNotAvailable() {
        Toast.makeText(activity, "Try Again Ads Not Loaded", Toast.LENGTH_SHORT).show();
    }

    public void loadApplovin() {
        maxRewardedAd = MaxRewardedAd.getInstance(applovinAdunit, this);
        maxRewardedAd.setListener(new MaxRewardedAdListener() {
            @Override
            public void onUserRewarded(MaxAd maxAd, MaxReward maxReward) {
                creditVideo();
            }

            @Override
            public void onRewardedVideoStarted(MaxAd maxAd) {

            }

            @Override
            public void onRewardedVideoCompleted(MaxAd maxAd) {

            }

            @Override
            public void onAdLoaded(MaxAd maxAd) {

            }

            @Override
            public void onAdDisplayed(MaxAd maxAd) {

            }

            @Override
            public void onAdHidden(MaxAd maxAd) {

            }

            @Override
            public void onAdClicked(MaxAd maxAd) {

            }

            @Override
            public void onAdLoadFailed(String s, MaxError maxError) {

            }

            @Override
            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {

            }
        });
        maxRewardedAd.loadAd();
    }

    private void creditVideo() {
        try {
            showLoading();
            ApiClient.restAdapter(activity).create(ApiInterface.class).api(js(activity,"videowall", pref.User_id(), "", "", id)).enqueue(new Callback<DefResp>() {
                @Override
                public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                    hideLoading();
                    if (response.isSuccessful() && response.body().getCode().equals("201")) {
                        pref.UpdateWallet(response.body().getBalance());
                        ClaimBonus(activity, response.body().getMsg());
                        if (response.body().getLimit() <= 0) {
                            upgradeAccount();
                        }
                    } else if (response.isSuccessful() && response.body().getCode().equals("202")) {
                        msgError(activity, response.body().getMsg());
                        upgradeAccount();
                    } else {
                        msgError(activity, response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<DefResp> call, Throwable t) {
                    hideLoading();
                }
            });
        } catch (Exception e) {
        }
    }

    private void upgradeAccount() {
        bind.shimmerView.setVisibility(View.GONE);
        bind.rv.setVisibility(View.GONE);
        bind.layoutUpgrade.setVisibility(View.VISIBLE);
        bind.layoutUpgrade.findViewById(R.id.upgrade).setOnClickListener(v -> {
            startActivity(new Intent(activity, SubscriptionActivity.class));
        });
    }

    class VideoWallAdapter extends RecyclerView.Adapter<VideoWallAdapter.ViewHolder> {
        LayoutInflater inflater;
        List<VideowallResp.DataItem> list;
        Context contx;
        OnItemClickListener clickListener;

        public VideoWallAdapter(Context ctx, List<VideowallResp.DataItem> appsItemList) {
            this.inflater = LayoutInflater.from(ctx);
            this.list = appsItemList;
            this.contx = ctx;
        }

        public void setClickListener(OnItemClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_videowall, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.title.setText(list.get(position).getTitle());
            holder.subtitle.setText(list.get(position).getSubtitle());
            holder.coins.setText(Fun.formatNumber(list.get(position).getCoin()));
            Picasso.get().load(WebApi.Api.IMAGES + list.get(position).getImage()).fit().into(holder.imageView);
            loadAd(list.get(position).getType(), position);
        }

        void loadAd(String type, int position) {
            try {
                JSONArray jsonArray = new JSONArray(list.get(position).getAdID());
                switch (type) {
                    case ad_Applovin:
                        applovinAdunit = jsonArray.getJSONObject(0).getString("value");
                        loadApplovin();
                        break;

                    case ad_Unity:
                        unityAdUnit = jsonArray.getJSONObject(0).getString("value");
                        loadUnity();
                        break;

                    case ad_Chartboost:
                        charBoostAppid = jsonArray.getJSONObject(0).getString("value");
                        chartBoostSign = jsonArray.getJSONObject(1).getString("value");
                        chartBoostLocation = jsonArray.getJSONObject(2).getString("value");
                        loadChartboost();
                        break;

                    case ad_Admob:
                        admobAdUnit = jsonArray.getJSONObject(0).getString("value");
                        loadAdmob();
                        break;

                    case ad_Adcolony:
                        adColonyApp = jsonArray.getJSONObject(0).getString("value");
                        adColonyZone = jsonArray.getJSONObject(1).getString("value");
                        loadAdcolony();
                        break;

                    case ad_Vungle:
                        vungleAppId = jsonArray.getJSONObject(0).getString("value");
                        vunglePlacement = jsonArray.getJSONObject(1).getString("value");

                        if (!Vungle.isInitialized()) {
                            Vungle.init(vungleAppId, activity.getApplicationContext(), new InitCallback() {
                                @Override
                                public void onSuccess() {
                                    Fun.log("Vungle SDK Init Successful " + vungleAppId);
                                }

                                @Override
                                public void onError(VungleException exception) {
                                    Fun.log("Vungle " + vungleAppId + " SDK onError " + exception.toString());
                                }

                                @Override
                                public void onAutoCacheAdAvailable(String pid) {
                                }
                            });
                        }
                        loadVungle();
                        break;

                    case ad_Ironsource:
                        irounSourceAppKey = jsonArray.getJSONObject(0).getString("value");
                        ironSourcePlacement = jsonArray.getJSONObject(1).getString("value");
                        loadIronSource();
                        break;

                    case ad_Startapp:
                        startAppid = jsonArray.getJSONObject(0).getString("value");
                        loadStartApp();
                        break;

                    case ad_Fb:
                        fbAdUnit = jsonArray.getJSONObject(0).getString("value");
                        loadFb();
                        break;
                }
            } catch (Exception e) {
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, coins, subtitle;
            ImageView imageView;

            ViewHolder(@NonNull final View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.tvTitle);
                imageView = itemView.findViewById(R.id.image);
                coins = itemView.findViewById(R.id.coins);
                subtitle = itemView.findViewById(R.id.subtitle);

                itemView.setOnClickListener(v -> {
                    clickListener.onClick(v, getAdapterPosition());
                });

            }
        }
    }

}




