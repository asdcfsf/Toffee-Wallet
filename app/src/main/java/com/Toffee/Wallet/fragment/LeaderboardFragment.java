package com.Toffee.Wallet.fragment;

import static com.Toffee.Wallet.utils.AdsConfig.bannerID;
import static com.Toffee.Wallet.utils.AdsConfig.banner_type;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.LeaderboardResp;
import com.Toffee.Wallet.adapters.LeaderboardAdapter;
import com.Toffee.Wallet.ads.AdManager;
import com.Toffee.Wallet.databinding.FragmentLeaderboardBinding;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardFragment extends Fragment {
    FragmentLeaderboardBinding bind;
    Context activity;
    AdManager adManager;
    LeaderboardAdapter adapter;
    List<LeaderboardResp.DataItem> list;
    Pref pref;
    String type="0";
    public LeaderboardFragment(String s) {
        this.type=s;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind=FragmentLeaderboardBinding.inflate(getLayoutInflater());
        activity=getActivity();

        pref=new Pref(activity);
        adManager = new AdManager((Activity) activity);
        adManager.loadBannerAd(bind.BANNER,pref.getString(banner_type),pref.getString(bannerID));

        list=new ArrayList<>();
        bind.rv.setHasFixedSize(true);
        bind.rv.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new LeaderboardAdapter(list,activity);
        bind.rv.setAdapter(adapter);

        api();


        return bind.getRoot();
    }

    private void api() {
        ApiClient.restAdapter(activity).create(ApiInterface.class).getLeaderbord(type).enqueue(new Callback<LeaderboardResp>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<LeaderboardResp> call, Response<LeaderboardResp> response) {
                if(response.isSuccessful() && response.body().getCode().equals("201") && response.body().getData().size()!=0){
                    try {

                        if(type.equals("2")){
                            bind.winnerLyt.setVisibility(View.GONE);
                        }else {
                            bind.winner.setText(response.body().getMsg());
                        }

                        bind.player1name.setText(response.body().getData().get(0).getName());
                        bind.player1coin.setText(" "+ Fun.coolNumberFormat(Long.parseLong(String.valueOf(response.body().getData().get(0).getBalance()))));

                        bind.player2name.setText(response.body().getData().get(1).getName());
                        bind.player2coin.setText(" "+ Fun.coolNumberFormat(Long.parseLong(String.valueOf(response.body().getData().get(1).getBalance()))));

                        bind.player3name.setText(response.body().getData().get(2).getName());
                        bind.player3coin.setText(" "+ Fun.coolNumberFormat(Long.parseLong(String.valueOf(response.body().getData().get(2).getBalance()))));

                        String url1 =response.body().getData().get(0).getProfile();
                        String url2 =response.body().getData().get(1).getProfile();
                        String url3 =response.body().getData().get(2).getProfile();

                        if (url1 != null) {
                            if (url1.equals("userpro.png")) {
                                bind.player1img.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));
                            } else if (url1.startsWith("http")) {
                                Picasso.get().load(url1).error(R.drawable.ic_user).fit().into(bind.player1img);
                            } else {
                                Picasso.get().load(WebApi.Api.USERTHUMB+url1).error(R.drawable.ic_user).fit().into(bind.player1img);
                            }
                        } else {
                            bind.player1img.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));
                        }

                        if (url2 != null) {
                            if (url2.equals("userpro.png")) {
                                bind.player2img.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));
                            } else if (url2.startsWith("http")) {
                                Picasso.get().load(url2).error(R.drawable.ic_user).fit().into(bind.player2img);
                            } else {
                                Picasso.get().load(WebApi.Api.USERTHUMB+url2).error(R.drawable.ic_user).fit().into(bind.player2img);
                            }
                        } else {
                            bind.player2img.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));
                        }

                        if (url3 != null) {
                            if (url3.equals("userpro.png")) {
                                bind.player3img.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));
                            } else if (url3.startsWith("http")) {
                                Picasso.get().load(url3).error(R.drawable.ic_user).fit().into(bind.player3img);
                            } else {
                                Picasso.get().load(WebApi.Api.USERTHUMB+url3).error(R.drawable.ic_user).fit().into(bind.player3img);
                            }
                        } else {
                            bind.player3img.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));
                        }

                    }catch (Exception e){}

                    for (int i=0; i<response.body().getData().size(); i++){
                        if(i>2){
                            list.add(response.body().getData().get(i));
                        }
                    }
                    bind.loader.setVisibility(View.GONE);
                    bind.rv.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }else{
                    bind.loader.setVisibility(View.GONE);
                    bind.layoutNoResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<LeaderboardResp> call, Throwable t) {
                bind.loader.setVisibility(View.GONE);
                bind.layoutNoResult.setVisibility(View.VISIBLE);
            }
        });
    }
}