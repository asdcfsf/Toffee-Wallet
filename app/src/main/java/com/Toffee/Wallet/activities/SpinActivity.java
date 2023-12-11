package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.AdsConfig.*;
import static com.Toffee.Wallet.utils.Cnt.runTimer;
import static com.Toffee.Wallet.utils.Const.Currency;
import static com.Toffee.Wallet.utils.Const.OFF;
import static com.Toffee.Wallet.utils.Const.SPIN_LIMIT;
import static com.Toffee.Wallet.utils.Fun.ClaimBonus;
import static com.Toffee.Wallet.utils.Fun.addToWallet;
import static com.Toffee.Wallet.utils.Fun.js;
import static com.Toffee.Wallet.utils.Fun.loading;
import static com.Toffee.Wallet.utils.Fun.msgError;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.Responsemodel.SettingResp;
import com.Toffee.Wallet.ads.AdManager;
import com.Toffee.Wallet.ads.RewardAds;
import com.Toffee.Wallet.databinding.ActivitySpinBinding;
import com.Toffee.Wallet.databinding.LayoutCollectBonusBinding;
import com.Toffee.Wallet.databinding.LayoutProgressLoadingBinding;
import com.Toffee.Wallet.fragment.FaqBottomDialogFragment;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.DatabaseHandler;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.WheelItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpinActivity extends AppCompatActivity {
    ActivitySpinBinding bind;
    SpinActivity activity;
    Pref pref;
    List<WheelItem> wheelItems = new ArrayList<>();
    LuckyWheel luckyWheel;
    int counts;
    String points;
    private final String TAG = "SpinActivity";
    boolean isPlaying, isComplete, spinPlay,closeDialog,addCoin;
    CountDownTimer countDownTimer;
    AdManager adManager;
    AlertDialog loading, progressLoading,addWallet;
    DatabaseHandler db;
    List<SettingResp.SpinItem> spinItems = new ArrayList<>();
    RewardAds.Builder rewardAds;
    LayoutCollectBonusBinding collectBonusBinding;

    public int spinAdType =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivitySpinBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);

        setContentView(bind.getRoot());
        activity = this;

        db = new DatabaseHandler(activity);
        rewardAds = new RewardAds.Builder(activity);


        pref = new Pref(activity);
        loading = loading(activity);
        luckyWheel = findViewById(R.id.luckyWheel);

        progressLoading = Fun.loadingProgress(activity);
        collectBonusBinding = LayoutCollectBonusBinding.inflate(getLayoutInflater());
        addWallet = addToWallet(activity, collectBonusBinding);

        getlimit();
        adManager = new AdManager(this);
        adManager.loadBannerAd(null,pref.getString(banner_type),pref.getString(bannerID));
        prepareWheel();

        bind.play.setOnClickListener(view -> {
            if (Fun.isConnected(activity)) {
                preparead();
                bind.play.setEnabled(false);
                isPlaying = true;
                Random random = new Random();
                String str = "1-8";
                String[] parts = str.split("-", 2);
                int low = Integer.parseInt(parts[0]);
                int high = Integer.parseInt(parts[1]);
                int result = random.nextInt(high - low) + low;
                points = String.valueOf(result);
                if (points.equals("0")) {
                    points = String.valueOf(1);
                }
                luckyWheel.rotateWheelTo(Integer.parseInt(points));
            } else {
                Fun.msgError(activity, "Please Check your Internet Connection");
            }
        });

        luckyWheel.setLuckyWheelReachTheTarget(() -> {
            try {
                String poi = spinItems.get(Integer.parseInt(points) - 1).getCoin();
                points = poi;
                Fun.log("TAG" + "onReachTarget: " + points);
                if (spinAdType == OFF) {
                    Fun.log("spin______  if" );
                    Fun.log("TAG" + "spinAdType: adoff" );
                    cr();
                } else if (pref.getAdInterval("spin") == -1 || pref.getAdCount("spin") >= pref.getAdInterval("spin")) {
                    Fun.log("spin______   else if" );
                    Fun.log("TAG" + "getAdInterval: else if " +pref.getAdInterval("spin"));
                    prepareClaimDialog();
                } else {
                    Fun.log("spin______  else" );
                    Fun.log("TAG" + "else part: " +pref.getAdInterval("spin"));
                    pref.setAdCount("spin", 1);
                    cr();
                }
            } catch (Exception e) {
                Fun.log(TAG + " onReachTarget: " + e.getMessage());
            }
        });

        bind.faq.setOnClickListener(v -> {
            Const.FAQ_TYPE="spin";
            FaqBottomDialogFragment fragment =  FaqBottomDialogFragment.newInstance();
            fragment.show(getSupportFragmentManager(),FaqBottomDialogFragment.TAG);
        });

        bind.back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void prepareClaimDialog() {

        collectBonusBinding.showAd.setEnabled(true);
        collectBonusBinding.showAd.setAlpha(1f);
        collectBonusBinding.close.setVisibility(View.GONE);
        collectBonusBinding.watchAd.setText(getString(R.string.watch_ad_to_add));
        collectBonusBinding.showAd.setVisibility(View.VISIBLE);
        collectBonusBinding.effect.setImageAssetsFolder("raw/");
        collectBonusBinding.effect.setAnimation(R.raw.coin_effect);
        collectBonusBinding.effect.playAnimation();
        collectBonusBinding.effect.loop(true);

        collectBonusBinding.congrts.setText(getString(R.string.congratulations));
        collectBonusBinding.msg.setText(getString(R.string.you_ve_won) + " " + points + " " + Currency);
        collectBonusBinding.showAd.setText(getString(R.string.watch_ad));
        collectBonusBinding.animationView.setImageAssetsFolder("raw/");
        collectBonusBinding.animationView.setAnimation(R.raw.coin_claim);
        collectBonusBinding.animationView.playAnimation();

        collectBonusBinding.effect.setImageAssetsFolder("raw/");
        collectBonusBinding.effect.setAnimation(R.raw.adeffect);
        collectBonusBinding.effect.playAnimation();

        collectBonusBinding.showAd.setOnClickListener(v -> {
            collectBonusBinding.showAd.setEnabled(false);
            collectBonusBinding.showAd.setAlpha(0.7f);
            if (rewardAds.isAdLoaded()) {
                isComplete=true;
                rewardAds.showReward();
            }
            else {
                preparead();
                new CountDownTimer(AD_LOADING_INTERVAL, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        collectBonusBinding.showAd.setText("Loading Ad " + (millisUntilFinished / 1000));
                    }

                    @Override
                    public void onFinish() {
                        if (rewardAds.isAdLoaded()) {
                            isComplete = true;
                            rewardAds.showReward();
                        } else {
                            cr();
                        }
                    }
                }.start();
            }
        });

        addWallet.show();
    }
    private void upgradeAccount() {
        bind.spinnerRing.setVisibility(View.GONE);
        bind.play.setVisibility(View.GONE);
        bind.relativeLayout5.setVisibility(View.GONE);
        bind.layoutUpgrade.setVisibility(View.VISIBLE);
        bind.layoutUpgrade.findViewById(R.id.upgrade).setOnClickListener(v -> {
            startActivity(new Intent(activity, SubscriptionActivity.class));
        });
    }
    private void preparead() {
        if(!rewardAds.isAdLoaded()){
            rewardAds.buildAd(spinAdType);
        }
    }

    private void prepareWheel() {
        spinItems = db.getSpin();
        for (int i = 0; i < spinItems.size(); i++) {
            wheelItems.add(new WheelItem(Color.parseColor(spinItems.get(i).getColor()), BitmapFactory.decodeResource(getResources(), R.drawable.small_coins), spinItems.get(i).getCoin()));
        }
        luckyWheel.addWheelItems(wheelItems);
    }

    void cr() {
        Fun.log(TAG + " complete_section_cr: " + points);
        try {
            showProgress();
            Objects.requireNonNull(ApiClient.restAdapter(activity)).create(ApiInterface.class).api(js(activity,"s-credit", pref.User_id(), points, "spin", "")).
                    enqueue(new Callback<DefResp>() {
                        @Override
                        public void onResponse(@NonNull Call<DefResp> call, @NonNull Response<DefResp> response) {
                            dismissProgress();
                            isPlaying = false;
                            if (response.isSuccessful() && Objects.requireNonNull(response.body()).getCode().equals("201")) {
                                pref.UpdateWallet(response.body().getBalance());
                                SPIN_LIMIT = response.body().getLimit();
                                bind.limit.setText(String.valueOf(SPIN_LIMIT));
                                spinAdType = response.body().getAdType();
                                pref.setAdInterval("spin", response.body().getAd_interval());
                                ClaimBonus(activity, response.body().getMsg());
                                startCount(response.body().getInterval());
                                showConfirmationDialog(true,response.body().getMsg());
                            } else {
                                showConfirmationDialog(false,response.body().getMsg());
                                msgError(activity, response.body().getMsg());
                                isPlaying = false;
                            }
                        }

                        @Override
                        public void onFailure(Call<DefResp> call, @NonNull Throwable t) {
                            msgError(activity, "Something went wrong!");
                            dismissProgress();
                            enableSpin();
                        }
                    });
        }catch(Exception e){}
    }

    void  showConfirmationDialog(boolean success,String msg){
        if(!addWallet.isShowing()){
            addWallet.show();
        }
        collectBonusBinding.close.setVisibility(View.VISIBLE);
        if(success){
            collectBonusBinding.effect.setImageAssetsFolder("raw/");
            collectBonusBinding.effect.setAnimation(R.raw.coin_effect);
            collectBonusBinding.effect.playAnimation();
            collectBonusBinding.effect.loop(true);

            collectBonusBinding.animationView.setImageAssetsFolder("raw/");
            collectBonusBinding.animationView.setAnimation(R.raw.success);
            collectBonusBinding.animationView.playAnimation();

            collectBonusBinding.congrts.setText(getString(R.string.congratulations));
            collectBonusBinding.msg.setText(msg);
            collectBonusBinding.watchAd.setText(getString(R.string.coin_added_successfull));
            collectBonusBinding.showAd.setVisibility(View.GONE);
        }else {
            collectBonusBinding.animationView.setImageAssetsFolder("raw/");
            collectBonusBinding.animationView.setAnimation(R.raw.notice);
            collectBonusBinding.animationView.setSpeed(1f);
            collectBonusBinding.animationView.playAnimation();
            collectBonusBinding.watchAd.setVisibility(View.GONE);
            collectBonusBinding.congrts.setText(getString(R.string.oops));
            collectBonusBinding.congrts.setTextColor(getResources().getColor(R.color.red));
            collectBonusBinding.msg.setText(msg);
        }

        collectBonusBinding.close.setOnClickListener(view -> {
            addWallet.dismiss();
        });
    }

    void getlimit() {
        if (SPIN_LIMIT > 0) {
            bind.limit.setText(String.valueOf(SPIN_LIMIT));
        } else {
            try {
                showProgress();
                ApiClient.restAdapter(activity).create(ApiInterface.class).api(js(activity,"s-limit", pref.User_id(), "", "spin", "")).enqueue(new Callback<DefResp>() {
                    @Override
                    public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                        dismissProgress();
                        if (response.isSuccessful() && response.body().getCode().equals("201")) {
                            SPIN_LIMIT = response.body().getLimit();
                            pref.setAdInterval("spin", response.body().getAd_interval());
                            bind.limit.setText(String.valueOf(SPIN_LIMIT));
                            spinAdType = response.body().getAdType();
                            preparead();
                            isPlaying = false;
                            if (response.body().getLimit() <= 0) {
                                bind.play.setEnabled(false);
                                bind.play.setAlpha(0.7f);
                                upgradeAccount();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DefResp> call, Throwable t) {
                        dismissProgress();
                        Toast.makeText(activity, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception ignored) {

            }
        }
    }

    void startCount(int times) {
        spinPlay = false;
        bind.play.setAlpha(0.7f);
        bind.play.setEnabled(false);
        countDownTimer = new CountDownTimer(times * 1000L, 1000) {
            public void onTick(long millisUntilFinished) {
                counts = (int) (millisUntilFinished / 1000);
                bind.tvPlay.setText(Fun.milliSecondsToTimer(millisUntilFinished));
                isPlaying = true;
            }

            public void onFinish() {
                isPlaying = false;
                if (SPIN_LIMIT <= 0) {
                    bind.play.setEnabled(false);
                    bind.play.setAlpha(0.1f);
                    upgradeAccount();
                } else {
                    bind.limit.setText(String.valueOf(SPIN_LIMIT));
                    preparead();
                    enableSpin();
                }
            }
        }.start();
    }

    private void enableSpin() {
        bind.tvPlay.setText(getString(R.string.spin));
        bind.play.setVisibility(View.VISIBLE);
        bind.play.setEnabled(true);
        bind.play.setAlpha(1f);
        isPlaying = false;
    }

    @Override
    public void onBackPressed() {
        try {
            if (spinPlay) {
                msgError(activity, "Please wait for Spin End");
            } else {
                if (isPlaying) {
                    pref.setIntData(pref.SPIN_COUNTS, counts);
                    countDownTimer.cancel();
                    countDownTimer = null;
                    runTimer(activity, "spin");
                }else {
                    if(countDownTimer!=null){
                        pref.setIntData(pref.SPIN_COUNTS, counts);
                        countDownTimer.cancel();
                    }
                }
                finish();
            }
        } catch (Exception e) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (countDownTimer == null) {
            int time = pref.getInt(pref.SPIN_COUNTS);
            if (time > 0) {
                startCount(time);
            }
        }
        if (isComplete && rewardAds.isCompleted()) {
            Fun.log("spin_ads_completed_true");
            isComplete=false;
            pref.setAdCount("spin", 0);
            rewardAds.setCompleted(false);
            rewardAds.setAdLoaded(false);
            cr();
        }
    }


    void showProgress() {
        if (!loading.isShowing()) {
            loading.show();
        }
    }

    void dismissProgress() {
        if (loading.isShowing()) {
            loading.dismiss();
        }
    }
}