package com.Toffee.Wallet.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.MissionResponse;
import com.Toffee.Wallet.adapters.MissionAdapter;
import com.Toffee.Wallet.databinding.FragmentMissionBinding;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissionFragment extends Fragment {
    FragmentMissionBinding bind;
    MissionAdapter adapter;
    List<MissionResponse.Data> mission=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bind=FragmentMissionBinding.inflate(getLayoutInflater());

        ChipNavigationBar chipNavigationBar= getActivity().findViewById(R.id.navigation);
        chipNavigationBar.setItemSelected(R.id.navigation_mission,true);

        bind.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new MissionAdapter(getActivity(),mission);
        bind.rv.setAdapter(adapter);

        fetchMission();

        return bind.getRoot();
    }

    private void fetchMission() {
       try {
           ApiClient.restAdapter(getActivity()).create(ApiInterface.class).dailyMission(Pref.User_id(getActivity())).enqueue(new Callback<MissionResponse>() {
               @Override
               public void onResponse(Call<MissionResponse> call, Response<MissionResponse> response) {
                   if(response.isSuccessful()){
                       bind.progressBar.setMax(response.body().getTotal());
                       bind.progressBar.makeProgress(response.body().getLeft());
                       bind.tvProgress.setText(response.body().getLeft()+"/"+response.body().getTotal());
                       mission.addAll(response.body().getData());
                       bind.shimmerView.setVisibility(View.GONE);
                       bind.rv.setVisibility(View.VISIBLE);
                       adapter.notifyDataSetChanged();
                   }
               }

               @Override
               public void onFailure(Call<MissionResponse> call, Throwable t) {
                   Fun.log("mision_expection_onFailure"+t.getMessage());

               }
           });
       }catch (Exception e){
           Fun.log("mision_expection_"+e.getMessage());
       }
    }
}