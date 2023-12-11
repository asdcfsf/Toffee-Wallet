package com.Toffee.Wallet.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Toffee.Wallet.activities.ViewCpiOffer;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.AppsResp;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<AppsResp> appsItemList;
    Context contx;
    OnItemClickListener clickListener;
    public static int AppPosi;

    public AppsAdapter(Context ctx, List<AppsResp> appsItemList) {
        this.inflater = LayoutInflater.from(ctx);
        this.appsItemList = appsItemList;
        this.contx=ctx;
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_apps, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.title.setText(appsItemList.get(position).getAppName());
            try {
                holder.subtitle.setText(Html.fromHtml(appsItemList.get(position).getDetails()));
                holder.subtitle.setTextSize(13);
            }catch (Exception e){}
            holder.coins.setText(""+appsItemList.get(position).getPoints());
        Picasso.get().load(appsItemList.get(position).getImage()).fit().into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return appsItemList.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, coins,subtitle;
        RoundedImageView imageView;
        CardView layout;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvTitle);
            imageView = itemView.findViewById(R.id.image);
            coins = itemView.findViewById(R.id.coins);
            subtitle = itemView.findViewById(R.id.subtitle);
            layout = itemView.findViewById(R.id.cv);

            itemView.setOnClickListener(v->{
                AppPosi=getAdapterPosition();
                Intent intent = new Intent(contx, ViewCpiOffer.class);
                intent.putExtra("id",appsItemList.get(getAdapterPosition()).getId());
                intent.putExtra("title",appsItemList.get(getAdapterPosition()).getAppName());
                intent.putExtra("url",appsItemList.get(getAdapterPosition()).getAppurl());
                intent.putExtra("appid",appsItemList.get(getAdapterPosition()).getAppID());
                intent.putExtra("image",appsItemList.get(getAdapterPosition()).getImage());
                intent.putExtra("detail",appsItemList.get(getAdapterPosition()).getDetails());
                intent.putExtra("coin",appsItemList.get(getAdapterPosition()).getPoints());
                intent.putExtra("P_userid",appsItemList.get(getAdapterPosition()).getP_userid());
                intent.putExtra("offer_type",appsItemList.get(getAdapterPosition()).getOffer_type());
                intent.putExtra("offer_id",appsItemList.get(getAdapterPosition()).getOffer_id());
                contx.startActivity(intent);
            });

        }
    }


}
