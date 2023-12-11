package com.Toffee.Wallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.BannerResp;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.ClickAction;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageSliderAdepter extends RecyclerView.Adapter<ImageSliderAdepter.SliderViewHolder> {

    private List<BannerResp.DataItem> slider_items;
    private ViewPager2 viewPager2;

    Context context;

    public ImageSliderAdepter(List<BannerResp.DataItem> slider_items, ViewPager2 viewPager2) {
        this.slider_items = slider_items;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item_container,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(slider_items.get(position));
        if (position == slider_items.size() - 2) {
            viewPager2.post(runnable);
        }

        holder.imageView.setOnClickListener(view -> {
            ClickAction.response(slider_items.get(position).getOnclick(),slider_items.get(position).getLink(),context);
        });
    }

    @Override
    public int getItemCount() {
        return slider_items.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageView;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ImageSlider);
        }

        void setImage(BannerResp.DataItem image_slider_item) {

            Picasso.get().load(WebApi.Api.IMAGES+image_slider_item.getBanner()).fit().into(imageView);

        }
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            slider_items.addAll(slider_items);
            notifyDataSetChanged();
        }
    };
}
