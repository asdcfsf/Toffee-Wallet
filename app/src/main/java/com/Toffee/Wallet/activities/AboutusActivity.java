package com.Toffee.Wallet.activities;


import static com.Toffee.Wallet.utils.AdsConfig.*;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.Toffee.Wallet.fragment.FaqBottomDialogFragment;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefListResp;
import com.Toffee.Wallet.adapters.UniversalAdapter;
import com.Toffee.Wallet.ads.AdManager;
import com.Toffee.Wallet.databinding.ActivityAboutusBinding;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutusActivity extends AppCompatActivity {
    ActivityAboutusBinding bind;
    Activity activity;
    AdManager adManager;
    UniversalAdapter adapter;
    List<DefListResp> list=new ArrayList<>();
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind =ActivityAboutusBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());
        activity=this;
        pref=new Pref(activity);

        bind.tool.title.setText(getString(R.string.about_us));

        adManager = new AdManager(activity);
        adManager.loadBannerAd(bind.BANNER,pref.getString(banner_type),pref.getString(bannerID));

        bind.tvAboutus.setText(Html.fromHtml(pref.getString(appDescription)));

        bind.rv.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new UniversalAdapter(activity,list);
        bind.rv.setAdapter(adapter);

        getSocial();

        bind.tool.back.setOnClickListener(v -> {
            onBackPressed();
        });

        bind.tool.faq.setOnClickListener(v -> {
            Const.FAQ_TYPE="about";
            FaqBottomDialogFragment fragment =  FaqBottomDialogFragment.newInstance();
            fragment.show(getSupportFragmentManager(),FaqBottomDialogFragment.TAG);
        });

    }

    private void getSocial() {
        ApiClient.restAdapter(activity).create(ApiInterface.class).getSocialLink().enqueue(new Callback<List<DefListResp>>() {
            @Override
            public void onResponse(Call<List<DefListResp>> call, Response<List<DefListResp>> response) {
                bind.shimmerViewContainer.setVisibility(View.GONE);
                if(response.isSuccessful() && response.body().size()>0){
                    list.addAll(response.body());
                    bind.rv.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<DefListResp>> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        adManager.OnBackInterstitalAd(pref.getInt(interstitalCount),pref.getString(interstital_adunit));
        activity.finish();
        super.onBackPressed();
    }


}
