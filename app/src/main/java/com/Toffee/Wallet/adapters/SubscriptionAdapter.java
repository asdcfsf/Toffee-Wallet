package com.Toffee.Wallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.SubscriptionResp;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.restApi.WebApi;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {

    private List<SubscriptionResp.DataItem> subscriptionRespList;
    LayoutInflater inflater;
    Context context;
    OnItemClickListener clickListener;

    public SubscriptionAdapter(Context context,List<SubscriptionResp.DataItem> ItemList) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.subscriptionRespList = ItemList;
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_subscription, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubscriptionResp.DataItem sub = subscriptionRespList.get(position);

        Picasso.get().load(WebApi.Api.IMAGES + sub.getImage()).fit().into(holder.imageView);


        if(sub.getAmount().equalsIgnoreCase("0")){
            holder.valid.setText("FREE");
            holder.buy.setText("Free Plan");
            holder.buy.setEnabled(false);
            holder.buy.setAlpha(0.7f);
        }else {
            holder.valid.setText("Validity : "+sub.getValid_for()+" Month");
            if(sub.getCurrency_posi().equals("0")){
                holder.buy.setText(sub.getCurrency()+" "+sub.getAmount());
            }else {
                holder.buy.setText(sub.getAmount()+" "+sub.getCurrency());
            }
        }

        holder.tvTitle.setText(sub.getTitle());
        holder.tvDesc.setText(sub.getDescription());
        holder.tvPromoCoin.setText("upto " + sub.getPromo_coin() + " Promo Coin");
        holder.tvSpin.setText(sub.getTask_spin() + " spins daily");
        holder.tvScratchh.setText(sub.getTask_scratch() + " scratch daily");
        holder.tvVideowall.setText(sub.getTask_videozone() + " rewarded video daily");
        holder.tvVideo.setText(sub.getTask_video() + " video task daily");
        holder.tvApp.setText(sub.getTask_daily_offer() + "  Daily Offer ");
        holder.tvQuiz.setText(sub.getTask_quiz() + " quizzes daily");
        holder.tvArticle.setText(sub.getTask_web() + " article daily");

        try {
            String[] ref=sub.getRefer_bonus().split("-");
            holder.tvRefer.setText("upto " +ref[1] + "+ refer bonus");
        }catch (Exception e){}
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTitle, tvDesc, tvPromoCoin, tvRefer, tvSpin, tvScratchh, tvQuiz, tvVideowall, tvArticle, tvVideo, tvApp,valid;
        Button buy;
        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            buy = itemView.findViewById(R.id.buy);
            valid = itemView.findViewById(R.id.valid);
            imageView = itemView.findViewById(R.id.image);
            tvTitle = itemView.findViewById(R.id.title);
            tvDesc = itemView.findViewById(R.id.desc);
            tvPromoCoin = itemView.findViewById(R.id.tvPromocoin);
            tvRefer = itemView.findViewById(R.id.tvRefer);
            tvSpin = itemView.findViewById(R.id.tvSpin);
            tvScratchh = itemView.findViewById(R.id.tvScratch);
            tvQuiz = itemView.findViewById(R.id.tvQuiz);
            tvVideowall = itemView.findViewById(R.id.tvVideowall);
            tvArticle = itemView.findViewById(R.id.tvArticle);
            tvVideo = itemView.findViewById(R.id.tvVideo);
            tvApp = itemView.findViewById(R.id.tvApp);

            buy.setOnClickListener(v -> {
                clickListener.onClick(v,getAdapterPosition());
            });
        }
    }

    @Override
    public int getItemCount() {
        return subscriptionRespList.size();
    }

}
