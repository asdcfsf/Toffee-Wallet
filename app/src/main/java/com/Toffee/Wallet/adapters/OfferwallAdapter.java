package com.Toffee.Wallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.OfferwallResp;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.restApi.WebApi;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OfferwallAdapter extends RecyclerView.Adapter<OfferwallAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<OfferwallResp.DataItem> dataItems;
    Context ctx;
    OnItemClickListener clickListener;

    public OfferwallAdapter(Context ctx, List<OfferwallResp.DataItem> dataItems) {
        this.inflater = LayoutInflater.from(ctx);
        this.dataItems = dataItems;
        this.ctx=ctx;
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_offerwall, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(dataItems.get(position).getTitle());
        holder.desc.setText(dataItems.get(position).getDescription());
//        Glide.with(holder.itemView.getContext()).load(WebApi.Api.IMAGES + dataItems.get(position).getThumb()).placeholder(R.drawable.placeholder).into(holder.image);
        Picasso.get().load(WebApi.Api.IMAGES +  dataItems.get(position).getThumb()).into(holder.image);

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

        TextView title,desc;
        ImageView image;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            desc = itemView.findViewById(R.id.desc);

            itemView.setOnClickListener(v->{
                clickListener.onClick(v,getAdapterPosition());
            });
        }
    }
}
