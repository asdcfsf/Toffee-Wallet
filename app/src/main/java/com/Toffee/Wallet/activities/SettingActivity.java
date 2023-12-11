package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.Const.BU;
import static com.Toffee.Wallet.utils.Fun.js;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.databinding.ActivitySettingBinding;
import com.Toffee.Wallet.databinding.LayoutAlertBinding;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding bind;
    Activity activity;
    ProgressDialog pb;
    AlertDialog alert;
    LayoutAlertBinding lytAlert;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivitySettingBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());

        activity=this;
        bind.tool.title.setText(getString(R.string.setting));
        bind.tool.faq.setVisibility(View.GONE);
        pref=new Pref(activity);

        lytAlert=LayoutAlertBinding.inflate(getLayoutInflater());
        alert= Fun.Alerts(activity,lytAlert);

        pb = new ProgressDialog(activity);
        pb.setMessage(getString(R.string.delete_account_loading));
        pb.setCancelable(false);


        bind.cvLogout.setOnClickListener(v -> {
            logout();
        });

        bind.cvAbout.setOnClickListener(v -> {
            startActivity(new Intent(activity, AboutusActivity.class));
        });

        bind.cvPrivacy.setOnClickListener(v -> {
            Fun.launchCustomTabs(activity,BU+"privacy-policy");
        });

        bind.cvLang.setOnClickListener(v -> {
            startActivity(new Intent(activity, LanguageActivity.class));
        });

        bind.tool.back.setOnClickListener(v -> {
            onBackPressed();
        });

    }


    private void logout() {
        alert.show();
        lytAlert.title.setText(getString(R.string.are_you_sure));
        lytAlert.msg.setText(getString(R.string.are_you_sure_you_want_to_logout));
        lytAlert.positive.setVisibility(View.VISIBLE);
        lytAlert.negative.setText(getString(R.string.no));
        lytAlert.positive.setOnClickListener(v -> {
            pref.Logout();

            startActivity(new Intent(activity,LoginMainActivity.class));
        });

        lytAlert.negative.setOnClickListener(v -> {
            alert.dismiss();
        });
    }

    private void deleteAccount() {
        alert.show();
        lytAlert.title.setText(getString(R.string.are_you_sure));
        lytAlert.msg.setText(getString(R.string.delete_account_msg));
        lytAlert.positive.setVisibility(View.VISIBLE);
        lytAlert.positive.setText(getString(R.string.no));
        lytAlert.negative.setText(getString(R.string.yes));
        lytAlert.negative.setOnClickListener(v -> {
            deleteProcess();
        });

        lytAlert.positive.setOnClickListener(v -> {
            alert.dismiss();
        });
    }

    private void deleteProcess() {
        pb.show();
        ApiClient.restAdapter(activity).create(ApiInterface.class).api(js(activity,"deleteAc", pref.User_id(), "","","")).enqueue(new Callback<DefResp>() {
            @Override
            public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                pb.dismiss();
                if (response.isSuccessful() && response.body().getCode().equals("201")) {
                    Toast.makeText(activity, ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    pref.Logout();
                    startActivity(new Intent(activity,LoginMainActivity.class));
                }else{
                    Toast.makeText(activity, ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DefResp> call, Throwable t) {
                pb.dismiss();
            }
        });
    }

}