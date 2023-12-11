package com.Toffee.Wallet.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.CoinStoreResponse;
import com.Toffee.Wallet.activities.PaymentActivity;
import com.Toffee.Wallet.utils.Fun;

import java.util.List;

public class CoinstoreAdapter extends RecyclerView.Adapter<CoinstoreAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<CoinStoreResponse.DataItem> dataItemList;
    Activity ctx;

    public CoinstoreAdapter(Context ctx, List<CoinStoreResponse.DataItem> dataItemList) {
        this.inflater = LayoutInflater.from(ctx);
        this.dataItemList = dataItemList;
}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_coinstore, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoinStoreResponse.DataItem posi=dataItemList.get(position);

        holder.title.setText(posi.getTitle());
        holder.coin.setText(" "+ Fun.formatNumber(posi.getCoin()));

        if(posi.getCurrency_posi().equals("0")){
            holder.buy.setText(posi.getCurrency()+" "+dataItemList.get(position).getAmount());
        }else {
            holder.buy.setText(dataItemList.get(position).getAmount()+ " "+posi.getCurrency());
        }
    }


    @Override
    public int getItemCount() {
        return dataItemList.size();
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,coin;
        Button buy;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            buy = itemView.findViewById(R.id.buy);
            coin = itemView.findViewById(R.id.tbcoin);

            buy.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), PaymentActivity.class);
                intent.putExtra("type","coinstore");
                intent.putExtra("coin",dataItemList.get(getAdapterPosition()).getCoin());
                intent.putExtra("amount",dataItemList.get(getAdapterPosition()).getAmount());
                intent.putExtra("currency",dataItemList.get(getAdapterPosition()).getCurrency());
                intent.putExtra("id",dataItemList.get(getAdapterPosition()).getId());
                intent.putExtra("productID",dataItemList.get(getAdapterPosition()).getProductID());
                intent.putExtra("currency_posi",dataItemList.get(getAdapterPosition()).getCurrency_posi());
                intent.putExtra("title",dataItemList.get(getAdapterPosition()).getTitle());
                itemView.getContext().startActivity(intent);
            });

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), PaymentActivity.class);
                intent.putExtra("type","coinstore");
                intent.putExtra("coin",dataItemList.get(getAdapterPosition()).getCoin());
                intent.putExtra("amount",dataItemList.get(getAdapterPosition()).getAmount());
                intent.putExtra("currency",dataItemList.get(getAdapterPosition()).getCurrency());
                intent.putExtra("id",dataItemList.get(getAdapterPosition()).getId());
                intent.putExtra("productID",dataItemList.get(getAdapterPosition()).getProductID());
                intent.putExtra("currency_posi",dataItemList.get(getAdapterPosition()).getCurrency_posi());
                intent.putExtra("title",dataItemList.get(getAdapterPosition()).getTitle());
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
