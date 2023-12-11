package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.AdsConfig.AD_LOADING_INTERVAL;
import static com.Toffee.Wallet.utils.AdsConfig.bannerID;
import static com.Toffee.Wallet.utils.AdsConfig.banner_type;
import static com.Toffee.Wallet.utils.Cnt.runTimer;;
import static com.Toffee.Wallet.utils.Const.OFF;
import static com.Toffee.Wallet.utils.Const.QUIZ_LIMIT;
import static com.Toffee.Wallet.utils.Const.TOOLBAR_TITLE;
import static com.Toffee.Wallet.utils.Fun.ClaimBonus;
import static com.Toffee.Wallet.utils.Fun.addToWallet;
import static com.Toffee.Wallet.utils.Fun.js;
import static com.Toffee.Wallet.utils.Fun.loading;
import static com.Toffee.Wallet.utils.Fun.msgError;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.ads.AdManager;
import com.Toffee.Wallet.ads.RewardAds;
import com.Toffee.Wallet.databinding.ActivityMathQuizBinding;
import com.Toffee.Wallet.databinding.LayoutCollectBonusBinding;
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

public class MathQuiz extends AppCompatActivity {
    ActivityMathQuizBinding bind;
    MathQuiz activity;
    int num1, num2, total, num, counts,btnType,quizAdType=1;
    Pref pref;
    private final String TAG = "MathQUiz Activity";
    boolean isPlaying, isComplete;
    CountDownTimer countDownTimer;
    AdManager adManager;
    AlertDialog loading,progressLoading,addWallet;
    RewardAds.Builder rewardAds;
    LayoutCollectBonusBinding collectBonusBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityMathQuizBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());

        activity = this;
        loading = loading(activity);
        pref = new Pref(activity);
        rewardAds = new RewardAds.Builder(activity);

        bind.title.setText(TOOLBAR_TITLE);

        collectBonusBinding = LayoutCollectBonusBinding.inflate(getLayoutInflater());
        addWallet = addToWallet(activity, collectBonusBinding);

        getlimit();

        adManager = new AdManager(activity);
        adManager.loadBannerAd(bind.BANNER,pref.getString(banner_type),pref.getString(bannerID));

        bind.a.setOnClickListener(v -> {
            validateAns(1);
        });

        bind.b.setOnClickListener(v -> {
            validateAns(2);
        });

        bind.c.setOnClickListener(v -> {
            validateAns(3);
        });

        bind.d.setOnClickListener(v -> {
            validateAns(4);
        });


        bind.back.setOnClickListener(v -> {
            onBackPressed();
        });

        bind.faq.setOnClickListener(v -> {
            Const.FAQ_TYPE="quiz";
            FaqBottomDialogFragment fragment =  FaqBottomDialogFragment.newInstance();
            fragment.show(getSupportFragmentManager(),FaqBottomDialogFragment.TAG);
        });
    }

    void validateAns(int type) {
        if (type == num) {
            btnType=type;
            disableBtn();
            isPlaying = true;
            Fun.msgSuccess(activity,getString(R.string.right_answer));
            if (quizAdType == OFF) {
                cr();
            } else if ( pref.getAdInterval("quiz") == -1 || pref.getAdCount("quiz") >= pref.getAdInterval("quiz")) {
                prepareClaimDialog();
            } else {
                pref.setAdCount("quiz", 1);
                cr();
            }
        } else {
            btnType=type;
            wrongBtn();
            msgError(this, getString(R.string.wrong_answer));
            new Handler().postDelayed(() -> enableBtn(),1000);
            Que();
        }
    }

    private void preparead() {
        if(!rewardAds.isAdLoaded()){
            rewardAds.buildAd(quizAdType);
        }
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
        collectBonusBinding.msg.setText(getString(R.string.watch_ad_to_add));
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

    @SuppressLint("SetTextI18n")
    void Que() {
        bind.placeholder.setVisibility(View.GONE);
        bind.queLyt.setVisibility(View.VISIBLE);
        preparead();
        Random random = new Random();
        num1 = random.nextInt(10);
        num2 = random.nextInt(30);

        bind.no1.setText(String.valueOf(num1));
        bind.no2.setText(String.valueOf(num2));
        total = num1 + num2;

        num = getRandomNumber(1, 4);
        System.out.println("number_is__" + num);
        if (num == 1) {
            bind.a.setText(String.valueOf(total));
            bind.b.setText(String.valueOf(total + getRandomNumber(15, 10)));
            bind.c.setText(String.valueOf(total + getRandomNumber(5, 10)));
            bind.d.setText(String.valueOf(total + getRandomNumber(10, 30)));
        } else if (num == 2) {
            bind.b.setText(String.valueOf(total));
            bind.a.setText(String.valueOf(total + getRandomNumber(5, 25)));
            bind.c.setText(String.valueOf(total + getRandomNumber(6, 30)));
            bind.d.setText(String.valueOf(total + getRandomNumber(9, 30)));
        } else if (num == 3) {
            bind.c.setText(String.valueOf(total));
            bind.a.setText(String.valueOf(total + getRandomNumber(1, 39)));
            bind.b.setText(String.valueOf(total + getRandomNumber(7, 20)));
            bind.d.setText(String.valueOf(total + getRandomNumber(5, 30)));
        } else if (num == 4) {
            bind.d.setText(String.valueOf(total));
            bind.a.setText(String.valueOf(total + getRandomNumber(1, 19)));
            bind.b.setText(String.valueOf(total + getRandomNumber(7, 20)));
            bind.c.setText(String.valueOf(total + getRandomNumber(5, 10)));
        }
    }


    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    void startCount(int times) {
        bind.queLyt.setVisibility(View.GONE);
        bind.placeholder.setVisibility(View.VISIBLE);
        isPlaying = true;
        countDownTimer = new CountDownTimer(times * 1000L, 1000) {
            public void onTick(long millisUntilFinished) {
                counts = (int) (millisUntilFinished / 1000);
                bind.placeholder.setText(getString(R.string.loading_next_que) + " " + counts);
            }

            public void onFinish() {
                isPlaying = false;
                if (QUIZ_LIMIT <= 0) {
                    disableBtn();
                    upgradeAccount();
                } else {
                    enableBtn();
                    if(!rewardAds.isAdLoaded()){
                        preparead();
                    }
                    Que();
                }
            }
        }.start();
    }


    private void upgradeAccount() {
        bind.cvQuiz.setVisibility(View.GONE);
        bind.layoutUpgrade.setVisibility(View.VISIBLE);
        bind.layoutUpgrade.findViewById(R.id.upgrade).setOnClickListener(v -> {
            startActivity(new Intent(activity,SubscriptionActivity.class));
        });
    }



    private void disableBtn() {
        switch (btnType){
            case 1:
                bind.a.setBackground(getResources().getDrawable(R.drawable.bg_success));
                break;
            case 2:
                bind.b.setBackground(getResources().getDrawable(R.drawable.bg_success));
                break;
            case 3:
                bind.c.setBackground(getResources().getDrawable(R.drawable.bg_success));
                break;
            case 4:
                bind.d.setBackground(getResources().getDrawable(R.drawable.bg_success));
                break;
        }

        bind.a.setEnabled(false);
        bind.b.setEnabled(false);
        bind.c.setEnabled(false);
        bind.d.setEnabled(false);

        bind.a.setAlpha(0.5f);
        bind.b.setAlpha(0.5f);
        bind.c.setAlpha(0.5f);
        bind.d.setAlpha(0.5f);



    }

    private void enableBtn() {

        switch (btnType){
            case 1:
                bind.a.setBackground(getResources().getDrawable(R.drawable.border_white));
                break;
            case 2:
                bind.b.setBackground(getResources().getDrawable(R.drawable.border_white));
                break;
            case 3:
                bind.c.setBackground(getResources().getDrawable(R.drawable.border_white));
                break;
            case 4:
                bind.d.setBackground(getResources().getDrawable(R.drawable.border_white));
                break;
        }

        bind.a.setEnabled(true);
        bind.b.setEnabled(true);
        bind.c.setEnabled(true);
        bind.d.setEnabled(true);

        bind.a.setAlpha(1f);
        bind.b.setAlpha(1f);
        bind.c.setAlpha(1f);
        bind.d.setAlpha(1f);

    }

    private void wrongBtn(){
        switch (btnType){
            case 1:
                bind.a.setBackground(getResources().getDrawable(R.drawable.bg_danger));
                break;
            case 2:
                bind.b.setBackground(getResources().getDrawable(R.drawable.bg_danger));
                break;
            case 3:
                bind.c.setBackground(getResources().getDrawable(R.drawable.bg_danger));
                break;
            case 4:
                bind.d.setBackground(getResources().getDrawable(R.drawable.bg_danger));
                break;
        }
    }

    void getlimit() {
        if (QUIZ_LIMIT > 0) {
            bind.limit.setText(String.valueOf(QUIZ_LIMIT));
        } else {
            showProgress();
            ApiClient.restAdapter(activity).create(ApiInterface.class).api(js(activity,"s-limit", pref.User_id(), "", "quiz","")).enqueue(new Callback<DefResp>() {
                @Override
                public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                    dismissProgress();
                    if (response.isSuccessful() && response.body().getCode().equals("201")) {
                        QUIZ_LIMIT = response.body().getLimit();
                        bind.limit.setText(String.valueOf(QUIZ_LIMIT));
                        quizAdType=response.body().getAdType();
                        pref.setAdInterval("quiz", response.body().getAd_interval());
                        Que();
                    } else {
                        isPlaying = false;
                        if (response.body().getLimit() == 0) {
                            msgError(activity, getString(R.string.today_limit_completed));
                            disableBtn();
                            upgradeAccount();
                        }
                    }
                }

                @Override
                public void onFailure(Call<DefResp> call, Throwable t) {
                    dismissProgress();
                }
            });
        }
    }

    void cr() {
        try {
            showProgress();
            ApiClient.restAdapter(activity).create(ApiInterface.class).api(js(activity,"s-credit", pref.User_id(), "0", "quiz","")).enqueue(new Callback<DefResp>() {
                @Override
                public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                    dismissProgress();
                    if (response.isSuccessful() && response.body().getCode().equals("201")) {
                        isComplete = false;
                        pref.UpdateWallet(response.body().getBalance());
                        QUIZ_LIMIT = response.body().getLimit();
                        bind.limit.setText(String.valueOf(QUIZ_LIMIT));
                        ClaimBonus(activity, response.body().getMsg());
                        quizAdType=response.body().getAdType();
                        pref.setAdInterval("quiz", response.body().getAd_interval());
                        startCount(response.body().getInterval());
                        showConfirmationDialog(true,response.body().getMsg());
                    } else {
                        showConfirmationDialog(false,response.body().getMsg());
                        msgError(activity, response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<DefResp> call, Throwable t) {
                    dismissProgress();
                    enableBtn();
                }
            });
        }catch (Exception e){}
    }


    @Override
    public void onBackPressed() {
        try {
            if (isPlaying) {
                pref.setIntData(pref.Quiz_COUNTS, counts);
                countDownTimer.cancel();
                countDownTimer = null;
                runTimer(activity, "quiz");
            }
            finish();
        } catch (Exception e) {
            finish();
        }
    }

    void  showConfirmationDialog(boolean success,String msg){
        if(!addWallet.isShowing()){
            addWallet.show();
        }
        collectBonusBinding.close.setVisibility(View.VISIBLE);
        collectBonusBinding.watchAd.setVisibility(View.VISIBLE);
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


    @Override
    protected void onPostResume() {
        if (countDownTimer == null) {
            int time = pref.getInt(pref.Quiz_COUNTS);
            if (time > 0) {
                disableBtn();
                startCount(time);
            }else {
                if(QUIZ_LIMIT > 0)
                    Que();
            }
        }

        if (isComplete && rewardAds.isCompleted()) {
            pref.setAdCount("quiz", 0);
            rewardAds.setCompleted(false);
            rewardAds.setAdLoaded(false);
            cr();
        }
        super.onPostResume();
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