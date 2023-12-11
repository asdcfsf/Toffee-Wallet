package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.AdsConfig.ADMOB;
import static com.Toffee.Wallet.utils.AdsConfig.FB;
import static com.Toffee.Wallet.utils.AdsConfig.STARTAPP;
import static com.Toffee.Wallet.utils.AdsConfig.nativeType;
import static com.Toffee.Wallet.utils.AdsConfig.native_count;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.Toffee.Wallet.Responsemodel.HistoryResp;
import com.Toffee.Wallet.adapters.HistoryAdapter;
import com.Toffee.Wallet.databinding.FragmentRewardHistoryBinding;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RewardHistory extends Fragment {
    FragmentRewardHistoryBinding bind;
    HistoryAdapter adapter;
    Context context;
    List<HistoryResp> list ;
    int item;
    Pref pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bind =FragmentRewardHistoryBinding.inflate(getLayoutInflater());
        context = getActivity();
        pref = new Pref(context);

        list=new ArrayList<>();
        bind.recyclerView.setHasFixedSize(true);
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HistoryAdapter(getActivity(),list);
        bind.recyclerView.setAdapter(adapter);

        if (Fun.isConnected(context)){
            getdata();
        }

        return bind.getRoot();
    }

    private void getdata(){
        ApiClient.restAdapter(getActivity()).create(ApiInterface.class).RewardHistory(pref.User_id()).enqueue(new Callback<List<HistoryResp>>() {
            @Override
            public void onResponse(Call<List<HistoryResp>> call, Response<List<HistoryResp>> response) {
                if(response.isSuccessful() && response.body().size()!=0){
                    bindDate(response);
                }else{
                    noResult();
                }
            }

            @Override
            public void onFailure(Call<List<HistoryResp>> call, Throwable t) {
                noResult();
            }
        });

    }

    private void bindDate(Response<List<HistoryResp>> response) {
        for(int i=0; i<response.body().size(); i++) {
            list.add(response.body().get(i));
            item++;
            if (item == pref.getInt(native_count)) {
                item = 0;
                if (pref.getString(nativeType).equals(FB)) {
                    list.add(new HistoryResp().setViewType(3));
                } else if (pref.getString(nativeType).equals(ADMOB)) {
                    list.add(new HistoryResp().setViewType(4));
                } else if (pref.getString(nativeType).equals(STARTAPP)) {
                    list.add(new HistoryResp().setViewType(5));
                }
//               else if (pref.getString(nativeType).equals(CUSTOM)) {
//                    list.add(new HistoryResp().setViewType(6));
//                }
            }
        }

        bind.shimmerView.setVisibility(View.GONE);
        bind.recyclerView.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    private void noResult() {
        bind.shimmerView.setVisibility(View.GONE);
        bind.layoutNoResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}