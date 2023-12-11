package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.Fun.addToWallet;
import static com.Toffee.Wallet.utils.Fun.js;
import static com.Toffee.Wallet.utils.Fun.loading;
import static com.Toffee.Wallet.utils.Fun.msgError;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.View;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.databinding.ActivityCollectRewardsBinding;
import com.Toffee.Wallet.databinding.LayoutAlertBinding;
import com.Toffee.Wallet.databinding.LayoutCollectBonusBinding;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectRewardActivity extends AppCompatActivity {
    ActivityCollectRewardsBinding bind;
    Activity activity;
    Pref pref;
    Bundle bundle;
    String id, title, task, inputType;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    AlertDialog loading, alert, addWallet;
    LayoutCollectBonusBinding collectBonusBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityCollectRewardsBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());

        activity = this;
        pref = new Pref(activity);
        bundle = getIntent().getExtras();
        loading = loading(activity);
        collectBonusBinding = LayoutCollectBonusBinding.inflate(getLayoutInflater());
        addWallet = addToWallet(activity, collectBonusBinding);

        id = bundle.getString("id");
        title = bundle.getString("title");
        task = bundle.getString("task");
        inputType = bundle.getString("inputType");

        bind.tvTitle.setText(title);
        bind.coin.setText(bundle.getString("coin"));


        if(bundle.getString("stock")!=null) {
            try {
                if (bundle.getString("stock").equals("00")) {
                    bind.stock.setText("IN Stock");
                    bind.stock.setTextColor(getResources().getColor(R.color.green));
                } else if (Integer.parseInt(bundle.getString("stock"))<=0) {
                    bind.stock.setText("Out of Stock");
                    bind.stock.setTextColor(getResources().getColor(R.color.red));
                }else {
                    bind.stock.setText("IN Stock");
                    bind.stock.setTextColor(getResources().getColor(R.color.green));
                }
            }catch (Exception e){}
        }

        if (getIntent().getStringExtra("description") != null) {
            bind.desc.setText(Html.fromHtml(getIntent().getStringExtra("description")));
        }

        bind.redeem.setHint(bundle.getString("placeholder"));
        bind.hint.setText(bundle.getString("placeholder"));

        switch (inputType) {
            case "text":
                bind.redeem.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case "number":
                bind.redeem.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case "email":
                bind.redeem.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
        }

        switch (task) {
            case "1":
                bind.layoutTask.setVisibility(View.VISIBLE);
                bind.lytSpin.setVisibility(View.VISIBLE);
                bind.lytScratch.setVisibility(View.VISIBLE);
                break;

            case "2":
                bind.layoutTask.setVisibility(View.VISIBLE);
                bind.lytSpin.setVisibility(View.VISIBLE);
                bind.lytScratch.setVisibility(View.VISIBLE);
                bind.lytQuiz.setVisibility(View.VISIBLE);
                break;

            case "3":
                bind.layoutTask.setVisibility(View.VISIBLE);
                bind.lytSpin.setVisibility(View.VISIBLE);
                bind.lytScratch.setVisibility(View.VISIBLE);
                bind.lytArticle.setVisibility(View.VISIBLE);
                break;

            case "4":
                bind.layoutTask.setVisibility(View.VISIBLE);
                bind.lytSpin.setVisibility(View.VISIBLE);
                bind.lytScratch.setVisibility(View.VISIBLE);
                bind.lytArticle.setVisibility(View.VISIBLE);
                bind.lytVideo.setVisibility(View.VISIBLE);
                break;
        }

        Picasso.get().load(WebApi.Api.IMAGES + Const.REWARD_CAT_IMG).fit().into(bind.image);

        bind.submitRedeem.setOnClickListener(view -> {
            bind.submitRedeem.setEnabled(false);
            validateData();
        });

    }

    private void validateData() {
        if (Objects.requireNonNull(bind.redeem.getText()).toString().isEmpty()) {
            Fun.msgError(activity, "Please Enter Valid Detail");
            bind.submitRedeem.setEnabled(true);
        } else {
            if (inputType.equals("email")) {
                if (bind.redeem.getText().toString().trim().matches(emailPattern)) {
                    if (pref.getBalance() >= Integer.parseInt(bundle.getString("coin"))) {
                        Redeem();
                    } else {
                        bind.submitRedeem.setEnabled(true);
                        showbonus(getString(R.string.not_enough_coin), true);
                    }
                } else {
                    msgError(activity, getString(R.string.enter_valid_email));
                }
            } else if (inputType.equals("text") || inputType.equals("number")) {
                if (pref.getBalance() >= Integer.parseInt(bundle.getString("coin"))) {
                    Redeem();
                } else {
                    bind.submitRedeem.setEnabled(true);
                    showbonus(getString(R.string.not_enough_coin), true);
                }
            }
        }
    }

    private void Redeem() {
        bind.submitRedeem.setEnabled(false);
        showProgress();
        Objects.requireNonNull(ApiClient.restAdapter(activity)).create(ApiInterface.class).api(js(activity,"redeem", pref.User_id(), "", bind.redeem.getText().toString().trim(), id)).enqueue(new Callback<DefResp>() {
            @Override
            public void onResponse(@NonNull Call<DefResp> call, @NonNull Response<DefResp> response) {
                dismissProgress();
                if (response.isSuccessful() && Objects.requireNonNull(response.body()).getCode().equals("201")) {
                    pref.UpdateWallet(response.body().getBalance());
                    showbonus(response.body().getMsg(), false);
                } else {
                    showbonus(Objects.requireNonNull(response.body()).getMsg(), true);
                }
            }

            @Override
            public void onFailure(Call<DefResp> call, Throwable t) {
                msgError(activity, t.getMessage());
                dismissProgress();
            }
        });
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

    void showbonus(String msg, boolean error) {
        collectBonusBinding.showAd.setVisibility(View.GONE);
        collectBonusBinding.watchAd.setVisibility(View.GONE);
        if (error) {
            collectBonusBinding.effect.setVisibility(View.GONE);
            collectBonusBinding.animationView.setImageAssetsFolder("raw/");
            collectBonusBinding.animationView.setAnimation(R.raw.notice);
            collectBonusBinding.animationView.setSpeed(1f);
            collectBonusBinding.animationView.playAnimation();
            collectBonusBinding.close.setVisibility(View.VISIBLE);
            collectBonusBinding.congrts.setText(getString(R.string.oops));
            collectBonusBinding.congrts.setTextColor(getResources().getColor(R.color.red));
            collectBonusBinding.msg.setText(msg);

        } else {
            collectBonusBinding.effect.setVisibility(View.VISIBLE);
            collectBonusBinding.animationView.setImageAssetsFolder("raw/");
            collectBonusBinding.animationView.setAnimation(R.raw.monyesent);
            collectBonusBinding.animationView.setSpeed(1f);
            collectBonusBinding.animationView.playAnimation();

            collectBonusBinding.effect.setImageAssetsFolder("raw/");
            collectBonusBinding.effect.setAnimation(R.raw.dollereffect);
            collectBonusBinding.effect.setSpeed(1f);
            collectBonusBinding.effect.playAnimation();

            collectBonusBinding.congrts.setText(getString(R.string.congratulations));
            collectBonusBinding.congrts.setTextColor(getResources().getColor(R.color.green));
            collectBonusBinding.close.setVisibility(View.VISIBLE);
            collectBonusBinding.msg.setText(msg);

        }

        collectBonusBinding.close.setOnClickListener(view -> {
            bind.submitRedeem.setEnabled(true);
            addWallet.dismiss();
        });

        addWallet.show();
    }

}