package com.Toffee.Wallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.LeaderboardResp;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.Fun;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.MyViewHolder> {

    Context c;
    List<LeaderboardResp.DataItem> itemList;

    public LeaderboardAdapter(List<LeaderboardResp.DataItem> dataItems, Context c) {
        this.itemList = dataItems;
        this.c = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(c)
                .inflate(R.layout.item_leaderboard, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final LeaderboardResp.DataItem category = itemList.get(position);

        holder.title.setText(category.getName());
        holder.rank.setText("" + (4 + position));
        holder.point.setText(Fun.coolNumberFormat(Long.parseLong(String.valueOf(category.getBalance()))));


        if (category.getProfile() != null) {
            if (category.getProfile().equals("userpro.png")) {
                holder.image.setImageDrawable(c.getResources().getDrawable(R.drawable.ic_user));
            } else if (category.getProfile().startsWith("http")) {
                Picasso.get().load(category.getProfile()).error(R.drawable.ic_user).fit().into(holder.image);
            } else {
                Picasso.get().load(WebApi.Api.USERTHUMB + category.getProfile()).error(R.drawable.ic_user).fit().into(holder.image);
            }
        } else {
            holder.image.setImageDrawable(c.getResources().getDrawable(R.drawable.ic_user));
        }

    }

    public void filterList(ArrayList<LeaderboardResp.DataItem> filterdNames) {
        this.itemList = filterdNames;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, point, rank;
        public CircleImageView image;
        public RelativeLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvName);
            point = itemView.findViewById(R.id.coins);
            image = itemView.findViewById(R.id.profile_image);
            layout = itemView.findViewById(R.id.layout);
            rank = itemView.findViewById(R.id.rank);
        }
    }
}
