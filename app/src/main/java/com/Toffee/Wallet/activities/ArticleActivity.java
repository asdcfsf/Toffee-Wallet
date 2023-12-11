package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.AdsConfig.ADMOB;
import static com.Toffee.Wallet.utils.AdsConfig.CUSTOM;
import static com.Toffee.Wallet.utils.AdsConfig.FB;
import static com.Toffee.Wallet.utils.AdsConfig.STARTAPP;
import static com.Toffee.Wallet.utils.AdsConfig.bannerID;
import static com.Toffee.Wallet.utils.AdsConfig.banner_type;
import static com.Toffee.Wallet.utils.AdsConfig.interstitalCount;
import static com.Toffee.Wallet.utils.AdsConfig.interstital_adunit;
import static com.Toffee.Wallet.utils.AdsConfig.nativeType;
import static com.Toffee.Wallet.utils.AdsConfig.native_count;
import static com.Toffee.Wallet.utils.Const.TOOLBAR_TITLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.TaskResp;
import com.Toffee.Wallet.adapters.TaskAdapter;
import com.Toffee.Wallet.ads.AdManager;
import com.Toffee.Wallet.databinding.ActivityArticleBinding;
import com.Toffee.Wallet.fragment.FaqBottomDialogFragment;
import com.Toffee.Wallet.listener.OnItemClickListener;
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

public class ArticleActivity extends AppCompatActivity implements OnItemClickListener {
    ActivityArticleBinding bind;
    Activity activity;
    TaskAdapter adapter;
    List<TaskResp> list;
    int item,posi;
    String id;
    AdManager adManager;
    Pref pref;
    public static boolean removeItem=false;
    public static int itemID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityArticleBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());
        activity = this;

        bind.tool.title.setText(TOOLBAR_TITLE);
        pref=new Pref(activity);
        adManager = new AdManager(activity);
        adManager.loadBannerAd(bind.BANNER,pref.getString(banner_type),pref.getString(bannerID));

        list=new ArrayList<>();
        bind.rv.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new TaskAdapter(activity,list,1);
        adapter.setClickListener(this::onClick);
        bind.rv.setAdapter(adapter);

        apiweb();

        bind.tool.back.setOnClickListener(v -> {
            onBackPressed();
        });

        bind.tool.faq.setOnClickListener(v -> {
            Const.FAQ_TYPE="article";
            FaqBottomDialogFragment fragment =  FaqBottomDialogFragment.newInstance();
            fragment.show(getSupportFragmentManager(),FaqBottomDialogFragment.TAG);
        });

    }

    private void apiweb() {
        Call<List<TaskResp>> call = ApiClient.restAdapter(activity).create(ApiInterface.class).getWebLink();
        call.enqueue(new Callback<List<TaskResp>>() {
            @Override
            public void onResponse(Call<List<TaskResp>> call, Response<List<TaskResp>> response) {
                if (response.isSuccessful() && response.body().size() != 0) {
                    list.clear();
                    bindData(response);
                } else {
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
    public void onClick(View view, int position) {
        this.posi=position;
        this.id=list.get(position).getId();
        itemID=position;
        if(list.get(position).getUrl().startsWith("http")){
            Intent intent = new Intent(activity,PlayTime.class);
            intent.putExtra("type","web");
            intent.putExtra("id",list.get(position).getId());
            intent.putExtra("url",list.get(position).getUrl());
            intent.putExtra("title",list.get(position).getTitle());
            intent.putExtra("time",list.get(position).getTimer());
            intent.putExtra("coin",list.get(position).getPoint());
            Const.adType=list.get(position).getAd_type();
            intent.putExtra("action_type",list.get(position).getAction_type());
            startActivity(intent);
        }else{
            Toast.makeText(activity, "Url Broken", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(removeItem){
            removeItem(itemID);
        }
    }

    @Override
    public void onBackPressed() {
        adManager.OnBackInterstitalAd(pref.getInt(interstitalCount),pref.getString(interstital_adunit));
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        System.out.println("on pause");
        super.onPause();
    }

    private void removeItem(int posi){
        list.remove(posi);
        adapter.notifyDataSetChanged();
        removeItem=false;
        if(list.size()<5){
            list.clear();
            apiweb();
        }
    }
}