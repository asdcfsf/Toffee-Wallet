package com.Toffee.Wallet.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefListResp;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;


public class UniversalAdapter extends RecyclerView.Adapter {
    Context ctx;
    List<DefListResp> list;
    LayoutInflater inflater;
    RecyclerView.ViewHolder viewHolder;
    OnItemClickListener clickListener;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public UniversalAdapter(Context c, List<DefListResp> dataItems) {
        this.inflater = LayoutInflater.from(c);
        this.list = dataItems;
        this.ctx = c;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewHolder = null;
        switch (viewType) {
            case 0:
                View v = inflater.inflate(R.layout.item_social_links, parent, false);
                viewHolder = new MyViewHolder(v);
                break;

        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder_parent, int position) {
        if (getItemViewType(position) == 0) {
            ((MyViewHolder) holder_parent).bindData(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        RoundedImageView imageView;
        LinearLayout layout;
        ImageView select;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.image);
            select = itemView.findViewById(R.id.SelectedIcon);
            layout = itemView.findViewById(R.id.layout);
        }

        public void bindData(int posi) {
            viewHolder.setIsRecyclable(false);
            final DefListResp resp = list.get(posi);

            tvTitle.setText(resp.getTitle());
            Picasso.get().load(WebApi.Api.IMAGES +list.get(posi).getImage()).fit().into(imageView);
            if(resp.getLang_code()!=null){
                if(Pref.getString(ctx,Pref.SELECTED_LANGUAGE).equals(resp.getLang_code())){
                    select.setVisibility(View.VISIBLE);
                    layout.setBackground(ctx.getResources().getDrawable(R.drawable.border_green));
                }else {
                    if(resp.getId().equals("1")){
                        select.setVisibility(View.VISIBLE);
                        layout.setBackground(ctx.getResources().getDrawable(R.drawable.border_green));
                    }
                }
            }

            itemView.setOnClickListener(v -> {
               if(resp.getConfig()!=null){
                   clickListener.onClick(v,getAdapterPosition());
               }else if(resp.getLang_code()!=null){
                   clickListener.onClick(v,getAdapterPosition());
               }else {
                   String url=list.get(posi).getUrl();
                   if(url!=null){
                       if(url.contains("@gmail.com")){
                           Pref pref=new Pref(itemView.getContext());
                           Intent emailIntent = new Intent(Intent.ACTION_VIEW);
                           emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{pref.getString(pref.EMAIL)});
                           emailIntent.setData(Uri.parse("mailto:"));
                           ctx.startActivity(emailIntent);
                       }else{
                           loadurl(url, itemView.getContext());
                       }
                   }
               }
            });

        }

        private void loadurl(String url,Context context) {
            try {
                if (!url.startsWith("http://") || !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            } catch (Exception e) {
                Fun.msgError((Activity) ctx,"URL BROKEN !!");
            }
        }

    }
}
