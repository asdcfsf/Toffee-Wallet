package com.Toffee.Wallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.androidsdk.sdk.util.AnimatedProgressBar;
import com.Toffee.Wallet.utils.AdsConfig;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.TaskResp;
import com.Toffee.Wallet.ads.AdmobNativeHolder;
import com.Toffee.Wallet.ads.FacebookNativeHolder;
import com.Toffee.Wallet.ads.StartioHolder;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.utils.Pref;
import com.squareup.picasso.Picasso;

import java.util.List;


public class PromoTaskAdapter extends RecyclerView.Adapter {
    Context ctx;
    List<TaskResp> list;
    LayoutInflater inflater;
    RecyclerView.ViewHolder viewHolder;
    OnItemClickListener clickListener;
    Pref pref;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public PromoTaskAdapter(Context c, List<TaskResp> dataItems) {
        this.inflater = LayoutInflater.from(c);
        this.list = dataItems;
        this.ctx = c;
        this.pref=new Pref(c);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewHolder = null;
        switch (viewType) {
            case 0:
                break;
            case 1:
                View v1 = inflater.inflate(R.layout.item_promotion, parent, false);
                viewHolder = new MyViewHolder(v1);
                break;

            case 2:
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
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder_parent, int position) {
        if (getItemViewType(position) == 1) {
            ((MyViewHolder) holder_parent).bindData(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int x = 1;
        if (list.get(position).getViewType() == 0) {
            if (list.get(position).getThumb() == null) {
                x = 1;
            } else if (list.get(position).getLink() == null) {
                x = 1;
            }
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
        TextView title,status,install,percent;
        ImageView imageView;
        RelativeLayout layout_status;
        AnimatedProgressBar progress_limit;

        public MyViewHolder(View view) {
            super(view);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.image);
            status = itemView.findViewById(R.id.status);
            install = itemView.findViewById(R.id.install);
            percent = itemView.findViewById(R.id.percent);
            layout_status = itemView.findViewById(R.id.layout_status);
            progress_limit = itemView.findViewById(R.id.progressBar);
        }

        public void bindData(int posi) {
          try {
              viewHolder.setIsRecyclable(false);
              final TaskResp resp = list.get(posi);
              title.setText(resp.getTitle());
              int count=resp.getViews();
              int per=(count*100)/resp.getTask_limit();
              install.setText(count+"/"+resp.getTask_limit());
              progress_limit.makeProgress(per);
              percent.setText(per +"%");

              if(resp.getThumb()!=null){
                  Picasso.get().load(resp.getThumb()).fit().into(imageView);
              }else{
                  imageView.setImageDrawable(itemView.getContext().getResources().getDrawable(R.drawable.web));
              }

              int st=resp.getStatus();
              if(st==0){
                  status.setText(itemView.getContext().getString(R.string.active));
                  layout_status.setBackground(itemView.getResources().getDrawable(R.drawable.promo_status_active));
              }else if(st==1){
                  status.setText(itemView.getContext().getString(R.string.completed));
                  layout_status.setBackground(itemView.getResources().getDrawable(R.drawable.promo_status_complete));
              }
          }catch (Exception e){}

        }
    }


}
