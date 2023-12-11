package com.Toffee.Wallet.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Toffee.Wallet.ads.CustomNativeHolder;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.AdsConfig;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.HistoryResp;
import com.Toffee.Wallet.ads.AdmobNativeHolder;
import com.Toffee.Wallet.ads.FacebookNativeHolder;
import com.Toffee.Wallet.ads.StartioHolder;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.squareup.picasso.Picasso;

import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter {
    Context ctx;
    List<HistoryResp> list;
    LayoutInflater inflater;
    RecyclerView.ViewHolder viewHolder;
    OnItemClickListener clickListener;
    Pref pref;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public HistoryAdapter(Context c, List<HistoryResp> dataItems) {
        this.inflater = LayoutInflater.from(c);
        this.list = dataItems;
        this.ctx = c;
        this.pref = new Pref(c);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         viewHolder=null;
        switch (viewType){
            case 0:
                View v = inflater.inflate(R.layout.item_history, parent, false);
                viewHolder= new MyViewHolder(v);
                break;
            case 1:
                View v1 = inflater.inflate(R.layout.item_reward_history, parent, false);
                viewHolder= new RewardHistoryHolder(v1);
                break;

            case 3:
                View v3 = inflater.inflate(R.layout.item_facebook_ads, parent, false);
                viewHolder= new FacebookNativeHolder(v3, ctx,pref.getString(AdsConfig.nativeID),String.format("#%06X", (0xFFFFFF & ctx.getResources().getColor(R.color.colorAccent))),v3.findViewById(R.id.native_ad_container));
                break;

            case 4:
                View v4 = inflater.inflate(R.layout.item_admob_native_ads, parent, false);
                viewHolder= new AdmobNativeHolder(v4, ctx,pref.getString(AdsConfig.nativeID),String.format("#%06X", (0xFFFFFF & ctx.getResources().getColor(R.color.colorAccent))),v4.findViewById(R.id.fl_adplaceholder));
                break;
            case 5:
                View v5 = inflater.inflate(com.ads.androidsdk.R.layout.startapp_native_ad_layout, parent, false);
                viewHolder= new StartioHolder(v5, ctx,String.format("#%06X", (0xFFFFFF & ctx.getResources().getColor(R.color.colorAccent))));
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
        switch (getItemViewType(position)){
            case 0:
                ((MyViewHolder)holder_parent).bindData(position);
                break;
            case 1:
                ((RewardHistoryHolder)holder_parent).bindData(position);
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        int x = 0;
        if(list.get(position).getViewType()==0){
            if(list.get(position).getRequestId()!=null){
                x= 1;
            }else {
                x=  0;
            } 
        }
        else {
            x=   list.get(position).getViewType();
        }
        return x;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmt,tvRemarks, tvDate;

        public MyViewHolder(View view) {
            super(view);
            tvAmt = itemView.findViewById(R.id.amount);
            tvDate =  itemView.findViewById(R.id.date);
            tvRemarks = itemView.findViewById(R.id.remark);

        }

        public void bindData(int posi){
            viewHolder.setIsRecyclable(false);
            final HistoryResp resp = list.get(posi);
            tvRemarks.setText(resp.getRemarks());
            tvDate.setText(resp.getType()+" | "+ Fun.getFormatedDateSimple(resp.getInsertedAt()));

            int colorGreen = Color.parseColor("#2ABF88");
            int colorRed = Color.parseColor("#ff0000");

            String mtype=resp.getTranType();
            if(mtype.equals("credit")){
                tvAmt.setTextColor(colorGreen);
                tvAmt.setText("+"+resp.getAmount());
            }else if(mtype.equals("debit")){
                tvAmt.setTextColor(colorRed);
                tvAmt.setText("-"+resp.getAmount());
            }

        }
    }

    public class RewardHistoryHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,coinUsed,date,remark,status;
        ImageView image;
        RelativeLayout lytStatus;

        public RewardHistoryHolder(View v2) {
            super(v2);
            tvTitle= itemView.findViewById(R.id.tvTitle);
            coinUsed= itemView.findViewById(R.id.coinUsed);
            date= itemView.findViewById(R.id.date);
            remark= itemView.findViewById(R.id.remark);
            status= itemView.findViewById(R.id.status);
            image= itemView.findViewById(R.id.image);
            lytStatus= itemView.findViewById(R.id.lytStatus);

        }

        @SuppressLint("SetTextI18n")
        public void bindData(int posi){
            viewHolder.setIsRecyclable(false);
            final HistoryResp resp = list.get(posi);

            Picasso.get().load(WebApi.Api.IMAGES+resp.getImage());
            tvTitle.setText(resp.getType());
            coinUsed.setText(" -"+resp.getAmount());
            date.setText(Fun.getFormatedDateSimple(resp.getDate()));

            if(resp.getStatus().equals("Success")){
                remark.setText(resp.getRemarks());
                status.setText(ctx.getString(R.string.completed));
                lytStatus.setBackgroundColor(ctx.getResources().getColor(R.color.green));
            }else if(resp.getStatus().equals("Reject")){
                remark.setText(resp.getRemarks());
                status.setText(ctx.getString(R.string.reject));
                lytStatus.setBackgroundColor(ctx.getResources().getColor(R.color.red));
            }else {
                remark.setText(ctx.getString(R.string.pending));
            }


        }
    }

}
