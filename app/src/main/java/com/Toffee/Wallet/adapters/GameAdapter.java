package com.Toffee.Wallet.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.GameResp;
import com.Toffee.Wallet.activities.CustomGame;
import com.Toffee.Wallet.activities.PlayTime;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Fun;
import com.jackandphantom.circularimageview.RoundedImage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter {
    Context ctx;
    List<GameResp.DataItem> list;
    LayoutInflater inflater;
    RecyclerView.ViewHolder viewHolder;
    OnItemClickListener clickListener;
    boolean home=false;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public GameAdapter(Context c, List<GameResp.DataItem> dataItems,boolean home) {
        this.inflater = LayoutInflater.from(c);
        this.list = dataItems;
        this.ctx = c;
        this.home=home;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         viewHolder=null;
        switch (viewType){
            case 0:
                View v = inflater.inflate(R.layout.item_game, parent, false);
                viewHolder= new MyViewHolder(v);
                break;
            case 1:
                View v1 = inflater.inflate(R.layout.item_home_game, parent, false);
                viewHolder= new GameHomeHolder(v1);
                break;

            case 3:
                View v3 = inflater.inflate(R.layout.item_home_playzone, parent, false);
                viewHolder= new HomePlayzone(v3);
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
                ((GameHomeHolder)holder_parent).bindData(position);
                break;
            case 3:
                ((HomePlayzone)holder_parent).bindData(position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(home){
            return 3;
        }
        return list.get(position).getGame_type();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RoundedImage image;
        public MyViewHolder(View view) {
            super(view);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
        }

        public void bindData(int posi){
            title.setText(list.get(posi).getTitle());

            Picasso.get().load(WebApi.Api.GAME_THUMB +list.get(posi).getImage()).fit().into(image);
            itemView.setOnClickListener(v -> {
                clickListener.onClick(v,posi);
            });
        }
    }

    private class HomePlayzone extends RecyclerView.ViewHolder {
        TextView title,time,coin,description;
        ImageView image;
        public HomePlayzone(View view) {
            super(view);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.tvTitle);
            time = itemView.findViewById(R.id.time);
            coin = itemView.findViewById(R.id.coin);
            description = itemView.findViewById(R.id.description);
        }

        public void bindData(int posi){
            title.setText(list.get(posi).getTitle());
            time.setText(Fun.milliSecondsToTimer(list.get(posi).getTime()* 1000L));
            coin.setText(list.get(posi).getGame_coin());
            description.setText(list.get(posi).getDescription());

            Picasso.get().load(WebApi.Api.GAME_THUMB +list.get(posi).getImage()).fit().into(image);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(ctx, PlayTime.class);
                intent.putExtra("type","game");
                intent.putExtra("id",list.get(posi).getId());
                intent.putExtra("url",list.get(posi).getLink());
                intent.putExtra("thumb", WebApi.Api.GAME_THUMB+list.get(posi).getImage());
                intent.putExtra("title",list.get(posi).getTitle());
                intent.putExtra("time",list.get(posi).getTime());
                intent.putExtra("coin",list.get(posi).getGame_coin());
                Const.adType=list.get(posi).getAd_type();
                intent.putExtra("action_type",list.get(posi).getAction_type());
                ctx.startActivity(intent);
            });
        }
    }

    public class GameHomeHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imageView;
        LinearLayout layout;

        public GameHomeHolder(View v2) {
            super(v2);
            tvTitle= itemView.findViewById(R.id.title);
            layout= itemView.findViewById(R.id.layout);
            imageView= itemView.findViewById(R.id.image);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(ctx, CustomGame.class);
                intent.putExtra("title",list.get(getAdapterPosition()).getTitle());
                intent.putExtra("background",list.get(getAdapterPosition()).getBackground());
                intent.putExtra("id",list.get(getAdapterPosition()).getId());
                intent.putExtra("anim_off",list.get(getAdapterPosition()).getAnim_off());
                intent.putExtra("anim_play",list.get(getAdapterPosition()).getAnim_play());
                intent.putExtra("anim_off",list.get(getAdapterPosition()).getAnim_off());
                ctx.startActivity(intent);
            });
        }

        public void bindData(int posi){

            switch (list.get(posi).getCard_bg()){
                case 0:
                    layout.setBackground(ctx.getDrawable(R.drawable.bg4));
                    break;

                case 1:
                    layout.setBackground(ctx.getDrawable(R.drawable.bg5));
                    break;

                case 2:
                    layout.setBackground(ctx.getDrawable(R.drawable.bg6));
                    break;

                case 3:
                    layout.setBackground(ctx.getDrawable(R.drawable.bg7));
                    break;
            }

            tvTitle.setText(list.get(posi).getTitle());

            Picasso.get().load(WebApi.Api.GAME_THUMB +list.get(posi).getImage()).fit().into(imageView);


        }
    }

}
