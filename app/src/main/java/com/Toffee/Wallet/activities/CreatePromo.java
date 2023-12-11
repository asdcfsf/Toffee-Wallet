package com.Toffee.Wallet.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.databinding.ActivityCreatePromoBinding;
import com.Toffee.Wallet.databinding.LayoutAlertBinding;
import com.Toffee.Wallet.fragment.FaqBottomDialogFragment;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePromo extends AppCompatActivity {
    ActivityCreatePromoBinding bind;
    Activity activity;
    boolean web, video;
    String type;
    int usercoin, maxpromotion, coin;
    String thumb, url, title;
    boolean search;
    EditText etinstall;
    Pref pref;
    AlertDialog  loadingDialog, alert;
    LayoutAlertBinding lytAlert;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityCreatePromoBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());

        bind.tool.title.setText(getString(R.string.create_promotion));

        activity = this;
        loadingDialog = Fun.loading(activity);
        lytAlert = LayoutAlertBinding.inflate(getLayoutInflater());
        alert = Fun.Alerts(activity, lytAlert);
        pref = new Pref(activity);

        bind.coins.setText(String.valueOf(pref.getInt(pref.PROMO_WALLET)));
        usercoin = pref.getInt(pref.PROMO_WALLET);
        maxpromotion = Integer.parseInt(Const.MAX_PROMOTE);
        etinstall = findViewById(R.id.etinstall);

        bind.layoutCoin.setOnClickListener(v -> {
            startActivity(new Intent(activity, StoreList.class));
        });

        bind.coins.setText(String.valueOf(pref.getInt(pref.PROMO_WALLET)));

        type = getIntent().getStringExtra("type");
        if (type.equals("web")) {
            web = true;
        } else if (type.equals("video")) {
            video = true;
        }

        if (video) {
            coin = Integer.parseInt(Const.VIDEO_PROMO_COIN);
            bind.postTitle.setHint(getString(R.string.youtube_videourl));
            bind.etlink.setHint(getString(R.string.example_ytlink));
            bind.maxinstall.setText("Max Limit : " + Const.MAX_PROMOTE);
            bind.installcoin.setText(getString(R.string.per_install_coin) + " : " + Const.VIDEO_PROMO_COIN);
        }else{
            web = true;
            coin = Integer.parseInt(Const.VIDEO_PROMO_COIN);
            bind.maxinstall.setText("Max Limit : " + Const.MAX_PROMOTE);
            bind.installcoin.setText(getString(R.string.per_install_coin) + " : " + Const.VIDEO_PROMO_COIN);
        }

        bind.create.setOnClickListener(view -> {
            int install = Integer.parseInt(etinstall.getText().toString().trim());
            if (!bind.etlink.getText().toString().isEmpty() && !etinstall.getText().toString().isEmpty() && !bind.postTitle.getText().toString().isEmpty() && !bind.postThumb.getText().toString().isEmpty()) {
                if (install <= maxpromotion) {
                    if ((install * coin) <= usercoin) {
                        create();
                    }else if ((install * coin) <= (pref.getBalance()+usercoin)) {
                        create();
                    } else {
                        showAlert(getString(R.string.not_enough_coin), 1);
                    }
                } else {
                    showAlert(getString(R.string.max_promomtion_limit) + Const.MAX_PROMOTE, 0);
                }
            } else {
                showAlert(getString(R.string.please_fill_details), 0);
            }
        });

        bind.tool.back.setOnClickListener(v ->{
            onBackPressed();
        });

        bind.tool.faq.setOnClickListener(view -> {
            Const.FAQ_TYPE="promo";
            FaqBottomDialogFragment fragment =  FaqBottomDialogFragment.newInstance();
            fragment.show(getSupportFragmentManager(),FaqBottomDialogFragment.TAG);
        });
    }

    void showLoading() {
        loadingDialog.show();
    }

    void dismisLoading() {
        if (loadingDialog.isShowing())
            loadingDialog.dismiss();
    }

    void showAlert(String msg, int error) {
        alert.show();
        if (error==0) {
            lytAlert.negative.setText(getString(R.string.close));
            lytAlert.title.setText(getString(R.string.oops));
            lytAlert.msg.setText(msg);
            lytAlert.negative.setOnClickListener(v -> {
                alert.dismiss();
            });
        }else if (error==1) {
            lytAlert.negative.setText(getString(R.string.buy_coin));
            lytAlert.title.setText(getString(R.string.oops));
            lytAlert.msg.setText(msg);
            lytAlert.negative.setOnClickListener(v -> {
                alert.dismiss();
                startActivity(new Intent(activity,StoreList.class));
            });
        }
        else {
            lytAlert.title.setText(getString(R.string.congratulations));
            lytAlert.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
            lytAlert.msg.setText(msg);
            lytAlert.negative.setVisibility(View.GONE);
            lytAlert.positive.setVisibility(View.VISIBLE);
            lytAlert.positive.setText(getString(R.string.close));
            lytAlert.positive.setOnClickListener(v -> {
                bind.postTitle.setText("");
                bind.etlink.setText("");
                etinstall.setText("0");
                alert.dismiss();
            });
        }
    }

    private void create() {
        showLoading();
        Call<DefResp> call = ApiClient.restAdapter(activity).create(ApiInterface.class)
                .createPromo(pref.User_id(), type, bind.postTitle.getText().toString(),
                        bind.etlink.getText().toString(), bind.postThumb.getText().toString().trim(), etinstall.getText().toString().trim());
        call.enqueue(new Callback<DefResp>() {
            @Override
            public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                dismisLoading();
                if (response.isSuccessful() && response.body().getCode().equals("201")) {
                    pref.UpdateWallet(response.body().getBalance());
                    showAlert(response.body().getMsg(), 2);
                } else if(response.body().getCode().equals("400")){
                    showAlert(response.body().getMsg(), 1);
                } else {
                    showAlert(response.body().getMsg(), 0);
                }
            }

            @Override
            public void onFailure(Call<DefResp> call, Throwable t) {
                dismisLoading();
                showAlert(t.getMessage(), 0);
            }
        });
    }
}