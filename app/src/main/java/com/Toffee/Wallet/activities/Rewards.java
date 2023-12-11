package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.AdsConfig.bannerID;
import static com.Toffee.Wallet.utils.AdsConfig.banner_type;
import static com.Toffee.Wallet.utils.AdsConfig.interstitalCount;
import static com.Toffee.Wallet.utils.AdsConfig.interstital_adunit;
import static com.Toffee.Wallet.utils.Fun.loading;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.RewardResp;
import com.Toffee.Wallet.ads.AdManager;
import com.Toffee.Wallet.databinding.ActivityRewardsBinding;
import com.Toffee.Wallet.databinding.LayoutAlertBinding;
import com.Toffee.Wallet.databinding.LayoutProgressLoadingBinding;
import com.Toffee.Wallet.fragment.FaqBottomDialogFragment;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.Toffee.Wallet.adapters.RewardAdapter;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rewards extends AppCompatActivity implements OnItemClickListener {
    ActivityRewardsBinding bind;
    RewardAdapter adapter;
    Pref pref;
    Rewards activity;
    AlertDialog alert;
    LayoutAlertBinding lytAlert;
    List<RewardResp.DataItem> list;
    AdManager adManager;
    AlertDialog loading,progressDialog;
    int posi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityRewardsBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());
        activity=this;
        pref = new Pref(activity);

        adManager = new AdManager(activity);
        adManager.loadBannerAd(bind.BANNER,pref.getString(banner_type),pref.getString(bannerID));
        loading = loading(activity);

        progressDialog=Fun.loadingProgress(activity);

        list=new ArrayList<>();
        lytAlert=LayoutAlertBinding.inflate(getLayoutInflater());
        alert= Fun.Alerts(activity,lytAlert);

        bind.coins.setText(""+pref.getBalance());

        if(getIntent().getStringExtra("description")!=null){
            bind.desc.setText(Html.fromHtml(getIntent().getStringExtra("description")));
        }

        list=new ArrayList<>();
        bind.rv.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new RewardAdapter(activity,list);
        adapter.setClickListener(this::onClick);
        bind.rv.setAdapter(adapter);

        getdata();

        bind.back.setOnClickListener(v -> {
            onBackPressed();
        });

        bind.faq.setOnClickListener(view -> {
            Const.FAQ_TYPE="redeem";
            FaqBottomDialogFragment fragment =  FaqBottomDialogFragment.newInstance();
            fragment.show(getSupportFragmentManager(),FaqBottomDialogFragment.TAG);
        });

    }

    private void getdata() {
        Call<RewardResp> call = ApiClient.restAdapter(activity).create(ApiInterface.class).getRewardbyID(getIntent().getStringExtra("id"));
        call.enqueue(new Callback<RewardResp>() {
            @Override
            public void onResponse(Call<RewardResp> call, Response<RewardResp> response) {
                if (response.isSuccessful() && response.body().getCode().equals("201")) {
                    bindData(response);
                } else {
                    noResult();
                }
            }
            @Override
            public void onFailure(Call<RewardResp> call, Throwable t) {
                    noResult();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void bindData(Response<RewardResp> response) {
        list.addAll(response.body().getData());
        bind.shimmerView.setVisibility(View.GONE);
        bind.rv.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    private void noResult() {
        bind.shimmerView.setVisibility(View.GONE);
        bind.layoutNoResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view, int position) {
        this.posi=position;
        Intent intent=new Intent(activity, CollectRewardActivity.class);
        intent.putExtra("id",list.get(position).getId());
        intent.putExtra("title",list.get(position).getTitle());
        intent.putExtra("coin",list.get(position).getPoints());
        intent.putExtra("category",list.get(position).getCategory());
        intent.putExtra("refer",list.get(position).getRefer());
        intent.putExtra("task",list.get(position).getTask());
        intent.putExtra("placeholder",list.get(position).getPlaceholder());
        intent.putExtra("inputType",list.get(position).getInput_type());
        intent.putExtra("stock",list.get(position).getQuantity());
        intent.putExtra("description",getIntent().getStringExtra("description"));
        startActivity(intent);
        Animatoo.animateZoom(activity);
    }

    @Override
    public void onBackPressed() {
        adManager.OnBackInterstitalAd(pref.getInt(interstitalCount),pref.getString(interstital_adunit));
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            bind.coins.setText(""+pref.getBalance());
        }catch (Exception e){}
    }
}
