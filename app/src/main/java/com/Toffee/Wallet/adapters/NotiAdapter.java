package com.Toffee.Wallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Toffee.Wallet.ads.CustomNativeHolder;
import com.Toffee.Wallet.utils.AdsConfig;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefListResp;
import com.Toffee.Wallet.ads.AdmobNativeHolder;
import com.Toffee.Wallet.ads.FacebookNativeHolder;
import com.Toffee.Wallet.ads.StartioHolder;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.utils.Pref;

import java.util.List;


public class NotiAdapter extends RecyclerView.Adapter {
    Context ctx;
    List<DefListResp> list;
    LayoutInflater inflater;
    RecyclerView.ViewHolder viewHolder;
    OnItemClickListener clickListener;
    Pref pref;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public NotiAdapter(Context c, List<DefListResp> dataItems) {
        this.inflater = LayoutInflater.from(c);
        this.list = dataItems;
        this.ctx = c;
        this.pref = new Pref(c);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewHolder = null;
        switch (viewType) {
            case 0:
                View v = inflater.inflate(R.layout.item_notification, parent, false);
                viewHolder = new MyViewHolder(v);
                break;

            case 3:
                View v3 = inflater.inflate(R.layout.item_facebook_ads, parent, false);
                viewHolder = new FacebookNativeHolder(v3, ctx,pref.getString(AdsConfig.nativeID), String.format("#%06X", (0xFFFFFF & ctx.getResources().getColor(R.color.colorAccent))), v3.findViewById(R.id.native_ad_container));
                break;

            case 4:
                View v4 = inflater.inflate(R.layout.item_admob_native_ads, parent, false);
                viewHolder = new AdmobNativeHolder(v4, ctx,pref.getString(AdsConfig.nativeID), String.format("#%06X", (0xFFFFFF & ctx.getResources().getColor(R.color.colorAccent))), v4.findViewById(R.id.fl_adplaceholder));
                break;
            case 5:
                View v5 = inflater.inflate(com.ads.androidsdk.R.layout.startapp_native_ad_layout, parent, false);
                viewHolder = new StartioHolder(v5, ctx, String.format("#%06X", (0xFFFFFF & ctx.getResources().getColor(R.color.colorAccent))));
                break;
            case 6:
                View v6 = inflater.inflate(R.layout.layout_custom_native, parent, false);
                viewHolder= new CustomNativeHolder(v6);
                break;

        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder_parent, int position) {
        if (getItemViewType(position) == 0) {
            ((MyViewHolder) holder_parent).bindData(position);
        }else if(getItemViewType(position) == 6) {
            ((CustomNativeHolder) holder_parent).bindData(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int x = 0;
        if (list.get(position).getViewType() == 0) {
                x = 0;
        } else {
            x = list.get(position).getViewType();
        }
        return x;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvmsg, date;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvmsg = itemView.findViewById(R.id.tvmsg);
            date = itemView.findViewById(R.id.date);
        }

        public void bindData(int posi) {
            viewHolder.setIsRecyclable(false);
            final DefListResp resp = list.get(posi);

            tvTitle.setText(resp.getTitle());
            tvmsg.setText(resp.getMsg());
            date.setText(resp.getCreated_at());
        }
    }
}
