package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.AdsConfig.AD_LOADING_INTERVAL;
import static com.Toffee.Wallet.utils.Const.Currency;
import static com.Toffee.Wallet.utils.Fun.ClaimBonus;
import static com.Toffee.Wallet.utils.Fun.addToWallet;
import static com.Toffee.Wallet.utils.Fun.js;
import static com.Toffee.Wallet.utils.Fun.loading;
import static com.Toffee.Wallet.utils.Fun.log;
import static com.Toffee.Wallet.utils.Fun.msgError;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.ads.RewardAds;
import com.Toffee.Wallet.databinding.LayoutCollectBonusBinding;
import com.Toffee.Wallet.databinding.LayoutProgressLoadingBinding;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Fun;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.view.View;

import androidx.browser.customtabs.CustomTabsIntent;

import com.Toffee.Wallet.databinding.ActivityPlayTimeBinding;
import com.Toffee.Wallet.utils.Pref;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

    public class PlayTime extends AppCompatActivity {
        ActivityPlayTimeBinding bind;
        Activity activity;
        CustomTabsIntent customTab;
        String url, thumb, title, reward, id, type, creditType, action_type;
        CountDownTimer countDownTimer;
        int totalTime = 120, timeCount, timeLeft, progress;
        boolean isPlaying = true, isCompleted, isComplete = true, isCredit, closeDialog,isSuccess;
        private static final int CHROME_CUSTOM_TAB_REQUEST_CODE = 100;
        private static final int BROWSER_REQUEST_CODE = 200;
        RewardAds.Builder rewardAds;
        AlertDialog loading, progressLoading, addWallet;
        Pref pref;
        LayoutCollectBonusBinding collectBonusBinding;

        @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Fun.statusbar(this);

            bind = ActivityPlayTimeBinding.inflate(getLayoutInflater());
            setContentView(bind.getRoot());

            activity = this;
            pref = new Pref(activity);
            loading = loading(activity);

            rewardAds = new RewardAds.Builder(activity);
            progressLoading = Fun.loadingProgress(activity);

            Intent data = getIntent();
            id = data.getStringExtra("id");
            type = data.getStringExtra("type");
            url = data.getStringExtra("url");
            title = data.getStringExtra("title");
            reward = data.getStringExtra("coin");
            action_type = data.getStringExtra("action_type");

            if (type.equals("web")) {
                creditType = "web";
                bind.thumb.setImageDrawable(getResources().getDrawable(R.drawable.ic_article));
                bind.desc.setText(getString(R.string.read_artical_description));
            } else if (type.equals("videozone")) {
                creditType = "video";
                thumb = data.getStringExtra("thumb");
                bind.desc.setText(getString(R.string.watch_tutorial_learn));
                Picasso.get().load(thumb).fit().into(bind.thumb);
            } else {
                creditType = "playzone";
                thumb = data.getStringExtra("thumb");
                Picasso.get().load(thumb).fit().into(bind.thumb);
            }

            totalTime = getIntent().getIntExtra("time", 0);
            Fun.log("total_task_time " + totalTime);
            bind.progressBar.setMax(totalTime);

            if (action_type.equals("browser")) {
                startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse(url)), BROWSER_REQUEST_CODE);
            } else if (action_type.equals("custom")) {
                launchTab();
            }
            bind.animationView.setVisibility(View.GONE);
            bind.playTime.setVisibility(View.VISIBLE);
            startCount(totalTime);
            preparead();

            bind.title.setText(title);
            bind.tbcoin.setText(reward);
            bind.tbcoin1.setText(reward);
            try {
                bind.gameInfo.setText(getString(R.string.you_can_earn_maximum_coin_coins_by_completing_this_task).replace("{coin}", reward).replace("{currency}",Currency));
            }catch (Exception e){}

            bind.close.setOnClickListener(v -> {
                if (rewardAds.isAdLoaded()) {
                    rewardAds.showReward();
                } else {
                    onBackPressed();
                }
            });

            bind.close1.setOnClickListener(v -> {
                if (rewardAds.isAdLoaded()) {
                    rewardAds.showReward();
                } else {
                    onBackPressed();
                }
            });

            bind.play.setOnClickListener(view -> {
                startCount(timeLeft);
                if (action_type.equals("browser")) {
                    startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse(url)), BROWSER_REQUEST_CODE);
                } else if (action_type.equals("custom")) {
                    launchTab();
                }
            });

            bind.claim.setOnClickListener(view -> {
                bind.claim.setEnabled(false);
                bind.claim.setVisibility(View.GONE);
                collectBonusBinding = LayoutCollectBonusBinding.inflate(getLayoutInflater());
                addWallet = addToWallet(activity, collectBonusBinding);
                prepareClaimDialog();
            });


        }


        private void prepareClaimDialog() {
            collectBonusBinding.msg.setText(getString(R.string.you_ve_get) + " " + reward + "" + Currency);
            addWallet.show();
            collectBonusBinding.showAd.setOnClickListener(v -> {
                collectBonusBinding.showAd.setEnabled(false);
                collectBonusBinding.showAd.setAlpha(0.7f);
                isCredit = true;
                if (rewardAds.isAdLoaded()) {
                    rewardAds.showReward();
                } else {
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
                                progressLoading.dismiss();
                                cr();
                            }
                        }
                    }.start();
                }
            });
            collectBonusBinding.close.setOnClickListener(view -> {
                addWallet.dismiss();
            });
        }

        private void preparead() {
            Fun.log("preparead Type in Playgame " + Const.adType);
            rewardAds.buildAd(Const.adType);
        }

        private void launchTab() {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder().setShowTitle(false).setUrlBarHidingEnabled(true);
            customTab = builder.build();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.intent.setData(Uri.parse(url));
            startActivityForResult(customTabsIntent.intent, CHROME_CUSTOM_TAB_REQUEST_CODE);
        }

        @Override
        protected void onPause() {
            super.onPause();
            Fun.log("play_time on pause");

        }

        void startCount(int timer) {
            Fun.log("PlayTime Timer Started ");
            countDownTimer = new CountDownTimer(timer * 1000L, 1000) {
                public void onTick(long millisUntilFinished) {
                    timeLeft = (int) (millisUntilFinished / 1000);
                    timeCount = (int) (millisUntilFinished / 1000);
                    log("PlayTime Timer Running " + timeCount + " time_left " + timeLeft);
                    isPlaying = true;
                    progress += 1;
                    bind.progressBar.makeProgress(progress);
    //                bind.timeleft.setText(Fun.milliSecondsToTimer((totalTime*1000L)-millisUntilFinished));
                    bind.timeleft.setText(Fun.milliSecondsToTimer(millisUntilFinished));
                }

                public void onFinish() {
                    isPlaying = false;
                    isCompleted = true;
                    log("Timer Completed");
                }
            }.start();
        }

        @Override
        protected void onResume() {
            super.onResume();
            if (rewardAds.isCompleted()) {
                rewardAds.setCompleted(false);
                rewardAds.setAdLoaded(false);
                if (isCredit) {
                    cr();
                }else {
                    onBackPressed();
                }
            }
        }

        void showLoading() {
            if (!loading.isShowing()) {
                loading.show();
            }
        }

        void dismissLoading() {
            if (loading.isShowing()) {
                loading.dismiss();
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            Fun.log("onActivityResult__ " + requestCode + " resultCode " + resultCode);
            if (requestCode == CHROME_CUSTOM_TAB_REQUEST_CODE) {
                Fun.log("playtime on custom tab closed");
                stopTimer();
            } else if (requestCode == BROWSER_REQUEST_CODE) {
                Fun.log("playtime on browser closed");
                stopTimer();
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

        private void stopTimer() {
            Fun.log("playtime timer stopped");
            if (isPlaying && countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
            } else if (isCompleted && !isPlaying) {
                bind.progressBar.makeProgress(totalTime);
                bind.taskStatus.setText(getString(R.string.completed));
                bind.claim.setVisibility(View.VISIBLE);
                bind.play.setVisibility(View.GONE);
                bind.close1.setVisibility(View.GONE);
            } else {
                Fun.log("playtime timer is null");
            }
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
        }

        @Override
        public void onBackPressed() {
            if(isSuccess){
                if(creditType.equals("web")){
                    ArticleActivity.removeItem=true;
                }else if(creditType.equals("video")){
                    VideoActivity.removeItem=true;
                }
            }
            super.onBackPressed();
        }

        void cr() {
            try {
                showLoading();
                ApiClient.restAdapter(activity).create(ApiInterface.class).api(js(activity,creditType, pref.User_id(), reward, "", id)).
                        enqueue(new Callback<DefResp>() {
                            @Override
                            public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                                dismissLoading();
                                if (response.isSuccessful() && response.body().getCode().equals("201")) {
                                    pref.UpdateWallet(response.body().getBalance());
                                    ClaimBonus(activity, response.body().getMsg());
                                    isSuccess=true;
                                    collectDialog(response.body().getMsg(), false);
                                } else {
                                    collectDialog(response.body().getMsg(), true);
                                    msgError(activity, response.body().getMsg());
                                    isPlaying = false;
                                }
                            }

                            @Override
                            public void onFailure(Call<DefResp> call, Throwable t) {
                                msgError(activity, "Something went wrong!");
                                dismissLoading();
                            }
                        });
            } catch (Exception e) {
            }
        }

        private void collectDialog(String msg, boolean error) {
            closeDialog = true;
            collectBonusBinding.showAd.setVisibility(View.GONE);
            if (error) {
                collectBonusBinding.animationView.setImageAssetsFolder("raw/");
                collectBonusBinding.animationView.setAnimation(R.raw.notice);
                collectBonusBinding.animationView.setSpeed(1f);
                collectBonusBinding.animationView.playAnimation();
                collectBonusBinding.watchAd.setVisibility(View.GONE);
                collectBonusBinding.close.setVisibility(View.VISIBLE);
                collectBonusBinding.congrts.setText(getString(R.string.oops));
                collectBonusBinding.congrts.setTextColor(getResources().getColor(R.color.red));
                collectBonusBinding.msg.setText(msg);
            } else {
                collectBonusBinding.animationView.setImageAssetsFolder("raw/");
                collectBonusBinding.animationView.setAnimation(R.raw.success);
                collectBonusBinding.animationView.setSpeed(1f);
                collectBonusBinding.animationView.playAnimation();
                collectBonusBinding.msg.setVisibility(View.GONE);
                collectBonusBinding.watchAd.setVisibility(View.GONE);
                collectBonusBinding.close.setVisibility(View.VISIBLE);
                collectBonusBinding.congrts.setText(getString(R.string.coin_added_successfull).replace("coin", Currency));

            }

            collectBonusBinding.close.setOnClickListener(
                    view -> {
                        onBackPressed();
                    }
            );

        }

}