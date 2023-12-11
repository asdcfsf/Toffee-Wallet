package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.AdsConfig.AD_LOADING_INTERVAL;
import static com.Toffee.Wallet.utils.AdsConfig.bannerID;
import static com.Toffee.Wallet.utils.AdsConfig.banner_type;
import static com.Toffee.Wallet.utils.Cnt.runTimer;
import static com.Toffee.Wallet.utils.Const.Currency;
import static com.Toffee.Wallet.utils.Const.MAX_NUMBER;
import static com.Toffee.Wallet.utils.Const.MIN_NUMBER;
import static com.Toffee.Wallet.utils.Const.OFF;
import static com.Toffee.Wallet.utils.Const.SCRATCH_LIMIT;
import static com.Toffee.Wallet.utils.Const.TOOLBAR_TITLE;
import static com.Toffee.Wallet.utils.Fun.ClaimBonus;
import static com.Toffee.Wallet.utils.Fun.addToWallet;
import static com.Toffee.Wallet.utils.Fun.js;
import static com.Toffee.Wallet.utils.Fun.loading;
import static com.Toffee.Wallet.utils.Fun.msgError;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import com.anupkumarpanwar.scratchview.ScratchView;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.ads.AdManager;
import com.Toffee.Wallet.ads.RewardAds;
import com.Toffee.Wallet.databinding.ActivityScratchBinding;
import com.Toffee.Wallet.databinding.LayoutCollectBonusBinding;
import com.Toffee.Wallet.databinding.LayoutProgressLoadingBinding;
import com.Toffee.Wallet.fragment.FaqBottomDialogFragment;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;

import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScratchActivity extends AppCompatActivity {
    ActivityScratchBinding bind;
    ScratchActivity activity;
    Pref pref;
    private final String TAG = "ScratchActivity : ";
    boolean isPlaying, isComplete;
    CountDownTimer countDownTimer;
    AdManager adManager;
    AlertDialog loading,progressLoading,addWallet;
    private String points;
    int counts,scratchAdType=1;
    RewardAds.Builder rewardAds;
    LayoutCollectBonusBinding collectBonusBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityScratchBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());
        activity = this;
        pref = new Pref(activity);
        loading = loading(activity);

        bind.title.setText(TOOLBAR_TITLE);

        progressLoading = Fun.loadingProgress(activity);
        rewardAds = new RewardAds.Builder(activity);
        collectBonusBinding = LayoutCollectBonusBinding.inflate(getLayoutInflater());
        addWallet = addToWallet(activity, collectBonusBinding);

        getlimit();

        adManager = new AdManager(activity);
        adManager.loadBannerAd(bind.BANNER,pref.getString(banner_type),pref.getString(bannerID));

        bind.play.setOnClickListener(v -> {
           if(bind.Scratchcard.isRevealed()){
               preparead();
               bind.pointsShow.setVisibility(View.GONE);
               
               bind.Scratchcard.mask();
           }else {
               Fun.msgError(activity,"Please Reveal Existing Card");
           }
        });

        bind.faq.setOnClickListener(v -> {
            Const.FAQ_TYPE="scratch";
            FaqBottomDialogFragment fragment =  FaqBottomDialogFragment.newInstance();
            fragment.show(getSupportFragmentManager(),FaqBottomDialogFragment.TAG);
        });

        bind.back.setOnClickListener(v -> {
            onBackPressed();
        });

        bind.Scratchcard.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
                if(isPlaying){
                    bind.Scratchcard.clear();
                    bind.Scratchcard.mask();
                    Fun.msgError(activity,"Please wait For Time End");
                }else {
                    bind.Scratchcard.clear();
                    points = generateCodePart(MIN_NUMBER, MAX_NUMBER);
                    bind.pointsShow.setVisibility(View.VISIBLE);
                    bind.pointsShow.setText(String.valueOf(points));
                    if (scratchAdType == OFF) {
                        cr();
                    } else if ( pref.getAdInterval("scratch") == -1 || pref.getAdCount("scratch") >= pref.getAdInterval("scratch")  || pref.getAdInterval("scratch")==-1) {
                        prepareClaimDialog();
                    } else {
                        pref.setAdCount("scratch", 1);
                        cr();
                    }
                }

            }
            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {

            }
        });

    }
     Random random = new Random();

    private  String generateCodePart(int min, int max) {
        return String.valueOf((random.nextInt((max - min) + 1) + min));
    }

    private void preparead() {
       if(!rewardAds.isAdLoaded()){
           rewardAds.buildAd(scratchAdType);
       }else {
           Fun.log("prepare_ad_else_part Scratch Activty");
       }
    }
    void getlimit() {
        if (SCRATCH_LIMIT > 0) {
            bind.limit.setText(String.valueOf(SCRATCH_LIMIT));
        } else {
            try {
                ApiClient.restAdapter(activity).create(ApiInterface.class).api(js(activity,"s-limit", pref.User_id(), "","scratch","")).enqueue(new Callback<DefResp>() {
                    @Override
                    public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                        if (response.isSuccessful() && response.body().getCode().equals("201")) {
                            SCRATCH_LIMIT = response.body().getLimit();
                            bind.limit.setText(String.valueOf(SCRATCH_LIMIT));
                            Const.MIN_AMOUNT = Integer.parseInt(response.body().getCoin().substring(0, response.body().getCoin().indexOf("-")));
                            Const.MAX_AMOUNT = Integer.parseInt(response.body().getCoin().replace(Const.MIN_AMOUNT + "-", ""));
                            pref.setAdInterval("scratch", response.body().getAd_interval());
                            scratchAdType=response.body().getAdType();

                            if (response.body().getLimit() <= 0) {
                                bind.play.setEnabled(false);
                                bind.play.setAlpha(0.7f);
                                upgradeAccount();
                            }
                        }else{
                            msgError(activity,response.body().getMsg());
                            upgradeAccount();
                        }
                    }

                    @Override
                    public void onFailure(Call<DefResp> call, Throwable t) {
                    }
                });
            }catch ( Exception e){}
        }
    }

    private void upgradeAccount() {
        bind.play.setVisibility(View.GONE);
        bind.cvScratch.setVisibility(View.GONE);
        bind.lytLimit.setVisibility(View.GONE);
        bind.layoutUpgrade.setVisibility(View.VISIBLE);
        bind.layoutUpgrade.findViewById(R.id.upgrade).setOnClickListener(v -> {
            startActivity(new Intent(activity, SubscriptionActivity.class));
        });
    }

    void startCount(int times) {
        isPlaying = true;
        bind.play.setEnabled(false);
        bind.Scratchcard.setEnabled(false);
        if (bind.play.getVisibility() == View.GONE) {
            bind.play.setVisibility(View.VISIBLE);
        }
        bind.play.setAlpha(0.7f);
        countDownTimer = new CountDownTimer(times * 1000L, 1000) {
            public void onTick(long millisUntilFinished) {
                counts =(int) millisUntilFinished / 1000;
                bind.play.setText(Fun.milliSecondsToTimer(millisUntilFinished));
            }

            public void onFinish() {
                isPlaying = false;
                if (SCRATCH_LIMIT <= 0) {
                    bind.play.setEnabled(false);
                    bind.play.setAlpha(1f);
                    upgradeAccount();
                } else {
                    
                    enableSpin();
                }
            }
        }.start();
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

    private void enableSpin() {
        bind.play.setText(getString(R.string.scratch_again));
        bind.play.setEnabled(true);
        bind.play.setAlpha(1f);
        isPlaying=false;
    }

    @Override
    public void onBackPressed() {
        try {
            if (isPlaying) {
                pref.setIntData(pref.SCRATCH_COUNTS, counts);
                countDownTimer.cancel();
                countDownTimer = null;
                runTimer(activity,"scratch");
            }else {
                if(countDownTimer!=null){
                    pref.setIntData(pref.SCRATCH_COUNTS, counts);
                    countDownTimer.cancel();
                }
            }
            finish();
        } catch (Exception e) {
            finish();
        }
    }

    @Override
    protected void onPostResume() {
        if (countDownTimer == null) {
            int time = pref.getInt(pref.SCRATCH_COUNTS);
            if (time > 0) {
                startCount(time);
            }
        }
        if (isComplete && rewardAds.isCompleted()) {
            isComplete=false;
            pref.setAdCount("scratch", 0);
            rewardAds.setCompleted(false);
            rewardAds.setAdLoaded(false);
            cr();
        }
        super.onPostResume();
    }

    void cr() {
        Log.e(TAG, "cr: " + points);
       try {
           showProgress();
           ApiClient.restAdapter(activity).create(ApiInterface.class).api(js(activity,"s-credit", pref.User_id(),points,"scratch","")).enqueue(new Callback<DefResp>() {
               @Override
               public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                   dismissProgress();
                   if (response.isSuccessful() && response.body().getCode().equals("201")) {
                       pref.UpdateWallet(response.body().getBalance());
                       SCRATCH_LIMIT = response.body().getLimit();
                       bind.limit.setText(String.valueOf(SCRATCH_LIMIT));
                       ClaimBonus(activity,response.body().getMsg());
                       pref.setAdInterval("scratch", response.body().getAd_interval());
                       scratchAdType=response.body().getAdType();
                       startCount(response.body().getInterval());
                       showConfirmationDialog(true,response.body().getMsg());
                   }else{
                       showConfirmationDialog(false,response.body().getMsg());
                       msgError(activity,response.body().getMsg());
                       isPlaying=false;
                   }
               }

               @Override
               public void onFailure(Call<DefResp> call, Throwable t) {
                   dismissProgress();
                   enableSpin();
               }
           });
       }catch (Exception e){}
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