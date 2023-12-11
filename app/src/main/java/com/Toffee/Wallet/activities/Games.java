package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.AdsConfig.bannerID;
import static com.Toffee.Wallet.utils.AdsConfig.banner_type;
import static com.Toffee.Wallet.utils.AdsConfig.interstitalCount;
import static com.Toffee.Wallet.utils.AdsConfig.interstital_adunit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.adapters.GameAdapter;
import com.Toffee.Wallet.ads.AdManager;
import com.Toffee.Wallet.databinding.ActivityGamesBinding;
import com.Toffee.Wallet.fragment.FaqBottomDialogFragment;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.Responsemodel.GameResp;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Pref;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Games extends AppCompatActivity implements OnItemClickListener {
    ActivityGamesBinding bind;
    Activity activity;
    GameAdapter adapter;
    private AlertDialog alert;
    List<GameResp.DataItem> list;
    AdManager adManager;
    Pref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind =ActivityGamesBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());
        activity=this;

        pref=new Pref(activity);
        adManager = new AdManager(activity);
        adManager.loadBannerAd(bind.BANNER,pref.getString(banner_type),pref.getString(bannerID));
        alert= Fun.Alerts(activity);

        list=new ArrayList<>();
        bind.rv.setLayoutManager(new GridLayoutManager(this, 2));
        adapter =new GameAdapter(activity,list,false);
        adapter.setClickListener(this);
        bind.rv.setAdapter(adapter);

        if (Fun.isConnected(this)){
            getdata();
        }else {
//            showAlert(getString(R.string.no_internet));
        }

        bind.tool.back.setOnClickListener(v -> {
            onBackPressed();
        });

        bind.tool.faq.setOnClickListener(v -> {
            Const.FAQ_TYPE="game";
            FaqBottomDialogFragment fragment =  FaqBottomDialogFragment.newInstance();
            fragment.show(getSupportFragmentManager(),FaqBottomDialogFragment.TAG);
        });


    }

    private void getdata(){
        ApiClient.restAdapter(activity).create(ApiInterface.class).funGame("0","20").enqueue(new Callback<GameResp>() {
            @Override
            public void onResponse(Call<GameResp> call, Response<GameResp> response) {
                if(response.isSuccessful() && response.body().getData().size()!=0){
                    Const.GAME_MINUTE=response.body().getGame_minute();
                    bindData(response);
                }else{
                    noResult();
                }
            }

            @Override
            public void onFailure(Call<GameResp> call, Throwable t) {
                noResult();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        adManager.OnBackInterstitalAd(pref.getInt(interstitalCount),pref.getString(interstital_adunit));
        super.onBackPressed();
    }

    private void bindData(Response<GameResp> response) {
        for(int i=0; i<response.body().getData().size(); i++){
            list.add(response.body().getData().get(i));
        }
        bind.loader.cancelAnimation();
        bind.loader.setVisibility(View.GONE);
        bind.rv.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    private void noResult() {
        bind.loader.cancelAnimation();
        bind.loader.setVisibility(View.GONE);
        bind.layoutNoResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(activity,PlayTime.class);
        intent.putExtra("type","game");
        intent.putExtra("id",list.get(position).getId());
        intent.putExtra("url",list.get(position).getLink());
        intent.putExtra("thumb", WebApi.Api.GAME_THUMB+list.get(position).getImage());
        intent.putExtra("title",list.get(position).getTitle());
        intent.putExtra("time",list.get(position).getTime());
        intent.putExtra("coin",list.get(position).getGame_coin());
        intent.putExtra("adType",list.get(position).getAd_type());
        intent.putExtra("action_type",list.get(position).getAction_type());
        startActivity(intent);
    }


}