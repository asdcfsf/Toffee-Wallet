package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.AdsConfig.ADMOB;
import static com.Toffee.Wallet.utils.AdsConfig.CUSTOM;
import static com.Toffee.Wallet.utils.AdsConfig.FB;
import static com.Toffee.Wallet.utils.AdsConfig.STARTAPP;
import static com.Toffee.Wallet.utils.AdsConfig.bannerID;
import static com.Toffee.Wallet.utils.AdsConfig.banner_type;
import static com.Toffee.Wallet.utils.AdsConfig.nativeType;
import static com.Toffee.Wallet.utils.AdsConfig.native_count;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.TaskResp;
import com.Toffee.Wallet.adapters.TaskAdapter;
import com.Toffee.Wallet.ads.AdManager;
import com.Toffee.Wallet.databinding.ActivityDailyOfferBinding;
import com.Toffee.Wallet.databinding.LayoutAlertBinding;
import com.Toffee.Wallet.fragment.FaqBottomDialogFragment;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyOffer extends AppCompatActivity implements OnItemClickListener {
    ActivityDailyOfferBinding bind;
    DailyOffer activity;
    TaskAdapter adapter;
    List<TaskResp> list;
    int item;
    public static int posi;
    public static boolean removeItem=false;
    AdManager adManager;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityDailyOfferBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());
        activity = this;

        bind.tool.title.setText(Const.TOOLBAR_TITLE);

        pref=new Pref(activity);
        adManager = new AdManager(activity);
        adManager.loadBannerAd(bind.BANNER,pref.getString(banner_type),pref.getString(bannerID));

        list=new ArrayList<>();
        bind.rv.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new TaskAdapter(activity,list,2);
        adapter.setClickListener(this::onClick);
        bind.rv.setAdapter(adapter);

        getdata();

        bind.tool.faq.setOnClickListener(v -> {
            faq();
        });

        bind.Lyt1.setOnClickListener(v -> {
            faq();
        });

        bind.tool.back.setOnClickListener(v -> {
            onBackPressed();
        });


    }

    private void faq() {
        Const.FAQ_TYPE="dailyoffer";
        FaqBottomDialogFragment fragment =  FaqBottomDialogFragment.newInstance();
        fragment.show(getSupportFragmentManager(),FaqBottomDialogFragment.TAG);
    }


    private void getdata() {
        Call<List<TaskResp>> call = ApiClient.restAdapter(activity).create(ApiInterface.class).getDailyoffer();
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
    protected void onResume() {
        super.onResume();
        if(removeItem){
            removeItem=false;
            removeItem(posi);
        }
    }

    private void removeItem(int posi){
        list.remove(posi);
        adapter.notifyDataSetChanged();
        if(list.size()<5){
            list.clear();
            getdata();
        }
    }

    @Override
    public void onClick(View view, int position) {
        posi=position;
        Intent intent = new Intent(activity,CompleteDailyOfferActivity.class);
        intent.putExtra("title",list.get(position).getTitle());
        intent.putExtra("coin",list.get(position).getCoin());
        intent.putExtra("description",list.get(position).getDescription());
        intent.putExtra("id",list.get(position).getId());
        intent.putExtra("image",list.get(position).getImage());
        intent.putExtra("url",list.get(position).getLink());
        startActivity(intent);
        Animatoo.animateSlideUp(activity);

    }

    private void showdialog(){


    }



}