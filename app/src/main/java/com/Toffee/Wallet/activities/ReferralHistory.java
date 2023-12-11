package com.Toffee.Wallet.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.Toffee.Wallet.Responsemodel.LeaderboardResp;
import com.Toffee.Wallet.adapters.RefHistoryAdapter;
import com.Toffee.Wallet.databinding.ActivityReferralHistoryBinding;
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

public class ReferralHistory extends AppCompatActivity {
    ActivityReferralHistoryBinding bind;
    Activity activity;
    RefHistoryAdapter adapter;
    List<LeaderboardResp.DataItem> list;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivityReferralHistoryBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());

        activity=this;
        pref=new Pref(activity);
        list=new ArrayList<>();

        bind.totalRef.setText("Total Refe : "+pref.getInt(pref.successRef) );
        bind.pendingRef.setText("Pending Refer : "+pref.getInt(pref.pendingRef) );

        bind.rv.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new RefHistoryAdapter(activity,list);
        bind.rv.setAdapter(adapter);

        getRef();

        bind.back.setOnClickListener(v -> {
            onBackPressed();
        });

        bind.faq.setOnClickListener(view -> {
            Const.FAQ_TYPE="invite";
            FaqBottomDialogFragment fragment =  FaqBottomDialogFragment.newInstance();
            fragment.show(getSupportFragmentManager(),FaqBottomDialogFragment.TAG);
        });

    }

    private void getRef() {
        ApiClient.restAdapter(activity).create(ApiInterface.class).getReferralHistory().enqueue(new Callback<LeaderboardResp>() {
            @Override
            public void onResponse(Call<LeaderboardResp> call, Response<LeaderboardResp> response) {
                try {
                    if(response.isSuccessful() && response.body().getData().size()!=0){
                        bind.rv.setVisibility(View.VISIBLE);
                        list.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    }else{
                        bind.layoutNoResult.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    bind.layoutNoResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<LeaderboardResp> call, Throwable t) {

            }
        });
    }
}

