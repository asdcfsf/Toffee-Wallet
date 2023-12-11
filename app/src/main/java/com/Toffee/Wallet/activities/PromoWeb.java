package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.AdsConfig.ADMOB;
import static com.Toffee.Wallet.utils.AdsConfig.CUSTOM;
import static com.Toffee.Wallet.utils.AdsConfig.FB;
import static com.Toffee.Wallet.utils.AdsConfig.STARTAPP;
import static com.Toffee.Wallet.utils.AdsConfig.interstitalCount;
import static com.Toffee.Wallet.utils.AdsConfig.interstital_adunit;
import static com.Toffee.Wallet.utils.AdsConfig.nativeType;
import static com.Toffee.Wallet.utils.AdsConfig.native_count;
import static com.Toffee.Wallet.utils.Fun.js;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.Toffee.Wallet.Responsemodel.TaskResp;
import com.Toffee.Wallet.adapters.PromoTaskAdapter;
import com.Toffee.Wallet.ads.AdManager;
import com.Toffee.Wallet.databinding.ActivityPromoWebBinding;
import com.Toffee.Wallet.fragment.FaqBottomDialogFragment;
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

public class PromoWeb extends AppCompatActivity  {
    ActivityPromoWebBinding bind;
    Activity activity;
    Pref pref;
    List<TaskResp> list;
    PromoTaskAdapter adapter;
    int item;
    AdManager adManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind =ActivityPromoWebBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());
        activity=this;
        pref =new Pref(activity);

        adManager = new AdManager(activity);

        list=new ArrayList<>();
        bind.rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new PromoTaskAdapter(activity,list);
        bind.rv.setAdapter(adapter);

        bind.add.setOnClickListener(view -> {
            Intent intent = new Intent(this,CreatePromo.class);
            intent.putExtra("type","web");
            startActivity(intent);
        });

        getData();

        bind.tool.back.setOnClickListener(v -> {
            onBackPressed();
        });

        bind.buy.setOnClickListener(v -> {
            startActivity(new Intent(activity, StoreList.class));
        });

        bind.tool.faq.setOnClickListener(view -> {
            Const.FAQ_TYPE="promo";
            FaqBottomDialogFragment fragment =  FaqBottomDialogFragment.newInstance();
            fragment.show(getSupportFragmentManager(),FaqBottomDialogFragment.TAG);
        });
    }


    private void getData() {
        ApiClient.restAdapter(activity).create(ApiInterface.class).apiPromo(js(activity,"PromoData", pref.User_id(), "","web","")).enqueue(new Callback<List<TaskResp>>() {
            @Override
            public void onResponse(Call<List<TaskResp>> call, Response<List<TaskResp>> response) {
                if(response.isSuccessful() && response.body().size()>0){
                    bindData(response);
                }else{
                    noResult();
                }
            }

            @Override
            public void onFailure(Call<List<TaskResp>> call, Throwable t) {
                noResult();
            }
        });
    }

    private void bindData(Response<List<TaskResp>> response) {
        for(int i=0; i<response.body().size(); i++){
            list.add(response.body().get(i));
            item++;
            if (item == pref.getInt(native_count)) {
                item = 0;
                if (pref.getString(nativeType).equals(FB)) {
                    list.add(new TaskResp().setViewType(3));
                } else if (pref.getString(nativeType).equals(ADMOB)) {
                    list.add(new TaskResp().setViewType(4));
                } else if (pref.getString(nativeType).equals(STARTAPP)) {
                    list.add(new TaskResp().setViewType(5));
                } else if (pref.getString(nativeType).equals(CUSTOM)) {
                    list.add(new TaskResp().setViewType(6));
                }
            }
        }
        bind.shimmerView.setVisibility(View.GONE);
        bind.rv.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    private void noResult() {
        bind.shimmerView.setVisibility(View.GONE);
        bind.layoutNoResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        adManager.OnBackInterstitalAd(pref.getInt(interstitalCount),pref.getString(interstital_adunit));
        super.onBackPressed();
    }
}