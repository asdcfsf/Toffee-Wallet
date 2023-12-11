package com.Toffee.Wallet.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.androidsdk.sdk.util.AnimatedProgressBar;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Pref;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.RewardCatResp;
import com.Toffee.Wallet.activities.Rewards;
import com.Toffee.Wallet.restApi.WebApi;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RewardCatAdapter extends RecyclerView.Adapter<RewardCatAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<RewardCatResp> rewardCatModelList;
    Pref pref;

    public RewardCatAdapter(Context ctx, List<RewardCatResp> rewardCatModelList) {
        this.inflater = LayoutInflater.from(ctx);
        this.rewardCatModelList = rewardCatModelList;
        this.pref=new Pref(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_reward_cat, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(rewardCatModelList.get(position).getName());
        holder.tvdesc.setText(holder.itemView.getContext().getString(R.string.required_coin) + " " + rewardCatModelList.get(position).getMin_coin());

        int requiredCoin = rewardCatModelList.get(position).getMin_coin();
        if (pref.getBalance() >= requiredCoin) {
            holder.tvdesc.setText("Completed");
            holder.pb.setMax(rewardCatModelList.get(position).getMin_coin());
            holder.pb.makeProgress(rewardCatModelList.get(position).getMin_coin());
            holder.redeemStatus.setImageResource(R.drawable.ic_unlock);
        } else {
            int required =rewardCatModelList.get(position).getMin_coin() - pref.getBalance();
            holder.tvdesc.setText(required + " coins need to Redeem");
            holder.pb.setMax(rewardCatModelList.get(position).getMin_coin());
            holder.pb.makeProgress(pref.getBalance());
        }

        Picasso.get().load(WebApi.Api.IMAGES + rewardCatModelList.get(position).getImage()).fit().into(holder.image);

    }


    @Override
    public int getItemCount() {
        return rewardCatModelList.size();
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,tvdesc;
        ImageView image;
        ImageView redeemStatus;
        AnimatedProgressBar pb;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvTitle);
            image = itemView.findViewById(R.id.image);
            redeemStatus = itemView.findViewById(R.id.redeemStatus);
            pb = itemView.findViewById(R.id.progressBar);
            tvdesc = itemView.findViewById(R.id.tvdesc);

            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                Intent go = new Intent(itemView.getContext(), Rewards.class);
                go.putExtra("id",String.valueOf(rewardCatModelList.get(pos).getId()));
                go.putExtra("image",rewardCatModelList.get(pos).getImage());
                go.putExtra("description",rewardCatModelList.get(pos).getDescription());
                Const.REWARD_CAT_IMG=rewardCatModelList.get(pos).getImage();
                Const.REWARD_CAT_TYPE=rewardCatModelList.get(pos).getName();
                go.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                itemView.getContext().startActivity(go);
            });


        }
    }
}
