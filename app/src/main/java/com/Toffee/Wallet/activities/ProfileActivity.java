package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.Fun.getFormatedDateWithoutTimestamp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.databinding.ActivityProfileBinding;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding bind;
    Pref pref;
    Activity activity;
    AlertDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivityProfileBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());
        activity=this;
        pref=new Pref(activity);

        loading=Fun.loading(activity);
        bind.username.setText(pref.getString(pref.NAME));
        bind.email.setText(pref.getString(pref.EMAIL));

        if (pref.getString(pref.PROFILE) != null) {
            if (pref.getString(pref.PROFILE).equals("userpro.png")) {
                bind.profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));
            } else if (pref.getString(pref.PROFILE).startsWith("http")) {
                Picasso.get().load(pref.getString(pref.PROFILE)).error(R.drawable.ic_user).fit().into(bind.profile);
            } else {
                Picasso.get().load(WebApi.Api.USERTHUMB + pref.getString(pref.PROFILE)).error(R.drawable.ic_user).fit().into(bind.profile);
            }
        } else {
            bind.profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));
        }

        fetchProfile();

        bind.lytUpgrade.setOnClickListener(view -> {
            startActivity(new Intent(activity,SubscriptionActivity.class));
        });

    }

    private void fetchProfile() {
        loading.show();
        ApiClient.restAdapter(activity).create(ApiInterface.class).api(Fun.js(activity,"profileInfo",pref.User_id(),"","","")).enqueue(new Callback<DefResp>() {
            @Override
            public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                loading.dismiss();
               try {
                   if(response.body().getCode().equals("201")){
                       Picasso.get().load(WebApi.Api.IMAGES+response.body().getImage()).into(bind.subImage);
                       bind.title.setText(response.body().getTitle());
                   }

                   pref.setIntData(pref.WALLET,response.body().getBalance());
                   bind.joined.setText(getFormatedDateWithoutTimestamp(response.body().getData2()));
                   bind.date.setText(getFormatedDateWithoutTimestamp(response.body().getData1()));
                   if(response.body().getId().equals("1")){
                       bind.lytUpgrade.setVisibility(View.VISIBLE);
                   }

                   bind.status.setText(response.body().getStatus());
                   if(response.body().getStatus().equals("Expired")){
                       bind.statusLyt.setBackground(getResources().getDrawable(R.drawable.bg_danger));
                   }
               }catch (Exception e){}
            }

            @Override
            public void onFailure(Call<DefResp> call, Throwable t) {
                loading.dismiss();
            }
        });
    }
}