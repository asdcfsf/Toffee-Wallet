package com.Toffee.Wallet.adapters;

import static com.Toffee.Wallet.utils.AdsConfig.nativeID;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.TaskResp;
import com.Toffee.Wallet.ads.AdmobNativeHolder;
import com.Toffee.Wallet.ads.CustomNativeHolder;
import com.Toffee.Wallet.ads.FacebookNativeHolder;
import com.Toffee.Wallet.ads.StartioHolder;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.squareup.picasso.Picasso;

import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter {
    Context ctx;
    List<TaskResp> list;
    LayoutInflater inflater;
    RecyclerView.ViewHolder viewHolder;
    OnItemClickListener clickListener;
    Pref pref;
    int viewType;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public TaskAdapter(Context c,List<TaskResp> dataItems,int view) {
        this.inflater = LayoutInflater.from(c);
        this.list = dataItems;
        this.ctx = c;
        this.viewType = view;
        this.pref = new Pref(c);  }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         viewHolder=null;
        switch (viewType){
            case 0:
                View v = inflater.inflate(R.layout.item_video, parent, false);
                viewHolder= new MyViewHolder(v);
                break;
            case 1:
                View v1 = inflater.inflate(R.layout.item_article, parent, false);
                viewHolder= new WebHolder(v1);
                break;
            case 2:
                View v2 = inflater.inflate(R.layout.item_dailyoffer, parent, false);
                viewHolder= new DailyHolder(v2);
                break;
            case 3:
                View v3 = inflater.inflate(R.layout.item_facebook_ads, parent, false);
                viewHolder= new FacebookNativeHolder(v3, ctx,pref.getString(nativeID),String.format("#%06X", (0xFFFFFF & ctx.getResources().getColor(R.color.colorAccent))),v3.findViewById(R.id.native_ad_container));
                break;

            case 4:
                View v4 = inflater.inflate(R.layout.item_admob_native_ads, parent, false);
                viewHolder= new AdmobNativeHolder(v4, ctx,pref.getString(nativeID),String.format("#%06X", (0xFFFFFF & ctx.getResources().getColor(R.color.colorAccent))),v4.findViewById(R.id.fl_adplaceholder));
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
                ((WebHolder)holder_parent).bindData(position);
                break;
            case 2:
                ((DailyHolder)holder_parent).bindData(position);
                break;
            case 6:
                ((CustomNativeHolder)holder_parent).bindData(position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int x = 0;
        if(list.get(position).getViewType()==0){
            return viewType;
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
        TextView title, point,timer;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = itemView.findViewById(R.id.title);
            point = itemView.findViewById(R.id.coins);
            image = itemView.findViewById(R.id.image);
            timer = itemView.findViewById(R.id.timer);

            itemView.setOnClickListener(v->{
                clickListener.onClick(v,getAdapterPosition());
            });
        }

        public void bindData(int posi){
            viewHolder.setIsRecyclable(false);
            final TaskResp resp = list.get(posi);
            title.setText(resp.getTitle());
            point.setText(Fun.formatNumber(list.get(posi).getPoint()));
            timer.setText(Fun.milliSecondsToTimer(list.get(posi).getTimer()*1000L));

            Picasso.get().load(resp.getThumb()).error(R.drawable.placeholder).fit().into(image);
        }
    }

    public class WebHolder extends RecyclerView.ViewHolder {
        TextView title, point,timer;
        ImageView image;
        public WebHolder(View v2) {
            super(v2);
            title = itemView.findViewById(R.id.title);
            point = itemView.findViewById(R.id.coins);
            image = itemView.findViewById(R.id.image);
            timer = itemView.findViewById(R.id.timer);

            itemView.setOnClickListener(v->{
                clickListener.onClick(v, getAdapterPosition());
            });
        }

        public void bindData(int posi){
            viewHolder.setIsRecyclable(false);
            final TaskResp resp = list.get(posi);
            title.setText(resp.getTitle());
            point.setText(Fun.formatNumber(list.get(posi).getPoint()));
            timer.setText(Fun.milliSecondsToTimer(list.get(posi).getTimer()*1000L));
            Picasso.get().load(resp.getThumb()).error(R.drawable.placeholder).fit().into(image);

        }
    }

    public class DailyHolder extends RecyclerView.ViewHolder {
        TextView title, point,desc;
        ImageView image;
        public DailyHolder(View v2) {
            super(v2);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            point = itemView.findViewById(R.id.coins);
            image = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(v->{
                clickListener.onClick(v, getAdapterPosition());
            });
        }

        public void bindData(int posi){
            viewHolder.setIsRecyclable(false);
            final TaskResp resp = list.get(posi);
            title.setText(resp.getTitle());
            desc.setText(Html.fromHtml(resp.getDescription()));
            point.setText(Fun.formatNumber(list.get(posi).getCoin()));
            if(resp.getImage()!=null) {
                Picasso.get().load(resp.getImage()).resize(300,200).into(image);
            }else{
                image.setVisibility(View.GONE);
            }
        }
    }

}
