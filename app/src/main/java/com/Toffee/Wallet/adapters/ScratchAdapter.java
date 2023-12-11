package com.Toffee.Wallet.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefListResp;
import com.Toffee.Wallet.listener.OnScratchClickListener;

import java.util.List;

public class ScratchAdapter extends RecyclerView.Adapter<ScratchAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<DefListResp> dataItems;
    Context contx;
    OnScratchClickListener clickListener;

    public ScratchAdapter(Context ctx, List<DefListResp> dataItems) {
        this.inflater = LayoutInflater.from(ctx);
        this.dataItems = dataItems;
        this.contx=ctx;
    }



    public void setClickListener(OnScratchClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_scratch, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e("coupon_item", "onBindViewHolder: "+dataItems.get(position).getCoin());
        if(dataItems.get(position).getStatus()==1){
            holder.imageView.setVisibility(View.GONE);
            holder.coupon.setText(String.valueOf(dataItems.get(position).getCoin()));
        }
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,coupon;
        ImageView imageView;
        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            coupon = itemView.findViewById(R.id.coupon);
            imageView = itemView.findViewById(R.id.thumbnail);

            itemView.setOnClickListener(v -> {
                if(dataItems.get(getAdapterPosition()).getStatus()==0) {
                    clickListener.onScratchClick(v, getAdapterPosition());
                }
            });
        }
    }

}
