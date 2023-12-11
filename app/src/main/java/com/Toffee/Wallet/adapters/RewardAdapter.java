package com.Toffee.Wallet.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.Responsemodel.RewardResp;
import com.Toffee.Wallet.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<RewardResp.DataItem> redeemModelList;
    Context ctx;
    OnItemClickListener clickListener;

    public RewardAdapter(Context ctx, List<RewardResp.DataItem> redeemModelList) {
        this.inflater = LayoutInflater.from(ctx);
        this.redeemModelList = redeemModelList;
        this.ctx=ctx;
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_rewards, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RewardResp.DataItem  resp=redeemModelList.get(position);
        holder.title.setText(resp.getTitle());
        holder.coin.setText(resp.getPoints());

        if(resp.getQuantity()!=null) {
            try {
                if (resp.getQuantity().equals("00")) {
                    holder.stock.setText("IN Stock");
                    holder.stockLyt.setBackgroundColor(ctx.getResources().getColor(R.color.green));
                } else if (Integer.parseInt(resp.getQuantity())<=0) {
                    holder.stock.setText("Out of Stock");
                    holder.stockLyt.setBackgroundColor(ctx.getResources().getColor(R.color.red));
                }else {
                    holder.stock.setText("IN Stock");
                    holder.stockLyt.setBackgroundColor(ctx.getResources().getColor(R.color.green));
                }
            }catch (Exception e){}
        }
      /*  else {
            holder.stock.setText("Out of Stock");
            holder.stockLyt.setBackgroundColor(ctx.getResources().getColor(R.color.red));
        }*/

        Picasso.get().load(WebApi.Api.IMAGES + Const.REWARD_CAT_IMG).resize(100,100).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return redeemModelList.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, coin,stock;
        RoundedImageView image;
        LinearLayout stockLyt;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            image = itemView.findViewById(R.id.image);
            coin = itemView.findViewById(R.id.coin);
            stock = itemView.findViewById(R.id.stock);
            stockLyt = itemView.findViewById(R.id.stockLyt);

            itemView.setOnClickListener(v -> {
                clickListener.onClick(v, getAdapterPosition());
            });
        }

    }

}




