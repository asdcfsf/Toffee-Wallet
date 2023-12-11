package com.Toffee.Wallet.adapters;

import static com.Toffee.Wallet.utils.Fun.getFormatedDateWithoutTimestamp;
import static com.Toffee.Wallet.utils.Fun.js;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.ContestResp;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContestAdapter extends RecyclerView.Adapter<ContestAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<ContestResp.DataItem> list;
    Context contx;
    Pref pref;
    OnItemClickListener clickListener;

    public ContestAdapter(Context ctx, List<ContestResp.DataItem> ItemList) {
        this.inflater = LayoutInflater.from(ctx);
        this.list = ItemList;
        this.contx = ctx;
        this.pref = new Pref(ctx);
    }


    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_contest, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        Picasso.get().load(WebApi.Api.IMAGES + list.get(position).getImage()).fit().into(holder.icon);
        holder.coin.setText(list.get(position).getCoin() + "");

        if (list.get(position).getRefer() >= Integer.parseInt(list.get(position).getTask_require())) {
            holder.tvInvite.setText(list.get(position).getRefer() + "/" + list.get(position).getTask_require() + " Invited");
        } else {
            holder.tvInvite.setText(list.get(position).getRefer() + "/" + list.get(position).getTask_require() + " Invited");
        }

        holder.eventPeriod.setText("Period  : " + getFormatedDateWithoutTimestamp(list.get(position).getStart_from()) + " To " + getFormatedDateWithoutTimestamp(list.get(position).getExpired_at()));
        holder.pb.setMax(Integer.parseInt(list.get(position).getTask_require()));
        holder.pb.setProgress(list.get(position).getRefer());

        try {
            if (list.get(position).getRefer() >= Integer.parseInt(list.get(position).getTask_require())) {
                holder.lytInfo.setVisibility(View.GONE);
                holder.btnComplete.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
        }

        holder.btnComplete.setOnClickListener(v -> {
            AlertDialog loading=Fun.loading(contx);
            loading.show();
            ApiClient.restAdapter(contx).create(ApiInterface.class).api(js((Activity) contx,"validateContest","","","",list.get(position).getId())).enqueue(new Callback<DefResp>() {
                @Override
                public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                    loading.dismiss();
                    if(response.isSuccessful() && response.body().getCode().equals("201")){
                        list.remove(position);
                        holder.lytInfo.setVisibility(View.VISIBLE);
                        holder.btnComplete.setVisibility(View.GONE);
                        notifyDataSetChanged();
                        pref.UpdateWallet(response.body().getBalance());
                        Fun.msgSuccess((Activity) contx,response.body().getMsg());
                    }else {
                        Fun.msgError((Activity) contx,response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<DefResp> call, Throwable t) {
                    loading.dismiss();
                }
            });
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, tvInvite, coin, btnComplete, eventPeriod, endDate;
        ImageView icon;
        ProgressBar pb;
        RelativeLayout lytInfo;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.tvTitle);
            tvInvite = itemView.findViewById(R.id.tvInvite);
            coin = itemView.findViewById(R.id.coin);
            btnComplete = itemView.findViewById(R.id.complete);
            pb = itemView.findViewById(R.id.progressBar);
            eventPeriod = itemView.findViewById(R.id.eventPeriod);
            lytInfo = itemView.findViewById(R.id.lytInfo);

        }
    }
}

