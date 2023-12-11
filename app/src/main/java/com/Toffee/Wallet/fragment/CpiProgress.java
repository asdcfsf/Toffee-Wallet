package com.Toffee.Wallet.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.Toffee.Wallet.Responsemodel.AppsResp;
import com.Toffee.Wallet.adapters.AppsAdapter;
import com.Toffee.Wallet.databinding.FragmentCpiBinding;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Fun;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CpiProgress extends Fragment implements OnItemClickListener {
    FragmentCpiBinding bind;
    Context activity;
    AppsAdapter adapter;
    List<AppsResp> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind =FragmentCpiBinding.inflate(getLayoutInflater());
        activity=getActivity();

        list=new ArrayList<>();
        bind.rv.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new AppsAdapter(activity,list);
        adapter.setClickListener(this);
        bind.rv.setAdapter(adapter);

        if (Fun.isConnected(activity)){
            getdata();
        }
        return bind.getRoot();
    }

    private void getdata(){
        Call<List<AppsResp>> call = ApiClient.restAdapter(activity).create(ApiInterface.class).cpaprogress();
        call.enqueue(new Callback<List<AppsResp>>() {
            @Override
            public void onResponse(Call<List<AppsResp>> call, Response<List<AppsResp>> response) {
                if(response.isSuccessful() && response.body().size()!=0){
                    bindData(response);
                }else{
                    noResult();
                }
            }

            @Override
            public void onFailure(Call<List<AppsResp>> call, Throwable t) {
                noResult();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void bindData(Response<List<AppsResp>> response) {
        for(int i=0; i<response.body().size(); i++){
            list.add(response.body().get(i));
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
    }
}
