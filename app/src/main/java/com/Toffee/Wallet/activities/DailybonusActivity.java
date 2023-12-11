package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.AdsConfig.AD_LOADING_INTERVAL;
import static com.Toffee.Wallet.utils.Fun.addToWallet;
import static com.Toffee.Wallet.utils.Fun.js;
import static com.Toffee.Wallet.utils.Fun.loading;
import static com.Toffee.Wallet.utils.Fun.msgError;
import static com.Toffee.Wallet.utils.Fun.msgSuccess;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.ads.RewardAds;
import com.Toffee.Wallet.databinding.LayoutCollectBonusBinding;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailybonusActivity extends AppCompatActivity {
    AlertDialog addWallet, loading, progressLoading;
    boolean closeDialog, isComplete;
    RewardAds.Builder rewardAds;
    Activity activity;
    Pref pref;
    com.Toffee.Wallet.databinding.LayoutCollectBonusBinding collectBonusBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailybonus);
        activity = this;
        pref = new Pref(activity);
        rewardAds = new RewardAds.Builder(activity);
        rewardAds.buildAd(1);
        loading = loading(activity);
        collectBonusBinding = LayoutCollectBonusBinding.inflate(getLayoutInflater());
        addWallet = addToWallet(activity, collectBonusBinding);
        prepareClaimDialog();

    }

    private void crdaily() {
        loading.show();
        ApiClient.restAdapter(activity).create(ApiInterface.class).api(js(activity,"dailycheck", pref.User_id(), "", "", "")).enqueue(new Callback<DefResp>() {
            @Override
            public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                loading.dismiss();
                if (response.isSuccessful() && response.body().getCode().equals("201")) {
                    msgSuccess(activity, response.body().getMsg());
                    isComplete = false;
                    pref.UpdateWallet(response.body().getBalance());
                    closeDialog = true;
                    showConfirmationDialog(true,response.body().getMsg());
                } else {
                    showConfirmationDialog(false,response.body().getMsg());
                    msgError(activity, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<DefResp> call, Throwable t) {
                loading.dismiss();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (rewardAds.isCompleted()) {
            rewardAds.setCompleted(false);
            rewardAds.setAdLoaded(false);
            crdaily();
        }
    }

    private void prepareClaimDialog() {
        collectBonusBinding.congrts.setText(getString(R.string.watch_ad));
        collectBonusBinding.msg.setText(getString(R.string.watch_ad_to_unlock));

        collectBonusBinding.showAd.setOnClickListener(v -> {
            collectBonusBinding.showAd.setEnabled(false);
            collectBonusBinding.showAd.setAlpha(0.7f);
            if (rewardAds.isAdLoaded()) {
                Toast.makeText(activity, "rewarded ads is loaded", Toast.LENGTH_SHORT).show();
                rewardAds.showReward();
            } else {
                rewardAds.buildAd(1);
                new CountDownTimer(AD_LOADING_INTERVAL, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        Fun.log("timer_running_daily_bonus "+(millisUntilFinished/1000));
                        collectBonusBinding.showAd.setText("Loading Ad " + (millisUntilFinished / 1000));
                    }

                    @Override
                    public void onFinish() {
                        if (rewardAds.isAdLoaded()) {
                            rewardAds.showReward();
                        } else {
                            Toast.makeText(activity, "Credit", Toast.LENGTH_SHORT).show();
                            crdaily();
                        }
                    }
                }.start();
            }
        });

        collectBonusBinding.close.setOnClickListener(view -> {
            addWallet.dismiss();
            onBackPressed();
        });

        addWallet.show();
    }

    void  showConfirmationDialog(boolean success,String msg){
        collectBonusBinding.close.setVisibility(View.VISIBLE);
        if(success){
            collectBonusBinding.animationView.setImageAssetsFolder("raw/");
            collectBonusBinding.animationView.setAnimation(R.raw.success);
            collectBonusBinding.animationView.setSpeed(1f);
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
    }

}