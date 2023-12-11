package com.Toffee.Wallet.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.FaqResp;
import com.Toffee.Wallet.adapters.FaqAdapter;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Const;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaqBottomDialogFragment extends BottomSheetDialogFragment {
    public static final String TAG = "FaqBottomDialog";

    public static FaqBottomDialogFragment newInstance() {
        return new FaqBottomDialogFragment();
    }
    Call<List<FaqResp>> call;
    List<FaqResp> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_faq, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout shimmerView=view.findViewById(R.id.shimmer_view);
        RelativeLayout noResult =view.findViewById(R.id.layout_no_result);
        RecyclerView rv =view.findViewById(R.id.rv);

        FaqAdapter adapter;
        list=new ArrayList<>();

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new FaqAdapter(getContext(),list);
        rv.setAdapter(adapter);

        call = ApiClient.restAdapter(getContext()).create(ApiInterface.class).Faqs(Const.FAQ_TYPE);
        call.enqueue(new Callback<List<FaqResp>>() {
            @Override
            public void onResponse(Call<List<FaqResp>> call, Response<List<FaqResp>> response) {
                shimmerView.setVisibility(View.GONE);
                if(response.isSuccessful() && response.body().size()!=0){
                    rv.setVisibility(View.VISIBLE);
                    list.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    noResult.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<List<FaqResp>> call, Throwable t) {
            }
        });


    }

    @Override public int getTheme() {
        return R.style.CustomBottomSheetDialog;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        list=null;
        call.cancel();
    }

}
