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

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.LeaderboardResp;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.Fun;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RefHistoryAdapter extends RecyclerView.Adapter<RefHistoryAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<LeaderboardResp.DataItem> list;
    Context contx;

    public RefHistoryAdapter(Context ctx, List<LeaderboardResp.DataItem> ItemList) {
        this.inflater = LayoutInflater.from(ctx);
        this.list = ItemList;
        this.contx = ctx;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_refuser, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getName());
        holder.date.setText(Fun.getFormatedDate(list.get(position).getInserted_at()));
        holder.coins.setText(String.valueOf(list.get(position).getBalance()));

        if (list.get(position).getCountry() != null) {
            Picasso.get().load("https://ipdata.co/flags/" + list.get(position).getCountry().toLowerCase() + ".png").fit().into(holder.country);
        }
        if (list.get(position).getImage() != null) {
            Picasso.get().load(WebApi.Api.IMAGES + list.get(position).getImage()).error(R.drawable.ic_mission_star).fit().into(holder.sub_image);
        }

        if (list.get(position).getRefer().equals("true")) {
            holder.refStatus.setText(contx.getString(R.string.success));
            holder.refLyt.setBackground(contx.getResources().getDrawable(R.drawable.btn_success));
        } else {
            holder.refStatus.setText(contx.getString(R.string.pending));
            holder.refLyt.setBackground(contx.getResources().getDrawable(R.drawable.bg_pending));
        }

        if (list.get(position).getProfile() != null) {
            if (list.get(position).getProfile().equals("userpro.png")) {
                holder.profile_image.setImageDrawable(contx.getResources().getDrawable(R.drawable.ic_user));
            } else if (list.get(position).getProfile().startsWith("http")) {
                Picasso.get().load(list.get(position).getProfile()).error(R.drawable.ic_user).fit().into(holder.profile_image);
            } else {
                Picasso.get().load(WebApi.Api.USERTHUMB + list.get(position).getProfile()).error(R.drawable.ic_user).fit().into(holder.profile_image);
            }
        } else {
            holder.profile_image.setImageDrawable(contx.getResources().getDrawable(R.drawable.ic_user));
        }
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
        TextView title, date, coins, refStatus;
        CircleImageView profile_image;
        LinearLayout refLyt;
        RoundedImageView country, sub_image;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            title = itemView.findViewById(R.id.tvName);
            date = itemView.findViewById(R.id.date);
            coins = itemView.findViewById(R.id.coins);
            refStatus = itemView.findViewById(R.id.refStatus);
            refLyt = itemView.findViewById(R.id.refLyt);
            country = itemView.findViewById(R.id.country);
            sub_image = itemView.findViewById(R.id.sub_image);


        }
    }
}

