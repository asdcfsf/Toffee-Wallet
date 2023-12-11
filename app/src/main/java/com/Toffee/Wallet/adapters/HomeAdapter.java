package com.Toffee.Wallet.adapters;

import static com.Toffee.Wallet.utils.ClickAction.response;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.HomeResp;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter implements Constant {

    private Context context;
    private List<HomeResp> list;
    RecyclerView.ViewHolder viewHolder;
    LayoutInflater inflater;
    int CatTheme = 0,layoutMode;

    public HomeAdapter(Context context, int catTheme, List<HomeResp> categoryItemList,int layoutMode) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = categoryItemList;
        this.CatTheme = catTheme;
        this.layoutMode = layoutMode;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewHolder = null;
        switch (viewType) {
            case 0:
                View v = inflater.inflate(R.layout.item_home_top, parent, false);
                viewHolder = new HomeTopHolder1(v);
                break;
            case 1:
                View v2 = inflater.inflate(R.layout.item_home_top2, parent, false);
                viewHolder = new HomeTopHolder2(v2);
                break;
            case 2:
                View v3 = inflater.inflate(R.layout.item_home_top3, parent, false);
                viewHolder = new HomeTopHolder3(v3);
                break;

            case 3:
                View v4 = inflater.inflate(R.layout.item_home_top4, parent, false);
                viewHolder = new HomeTopHolder4(v4);
                break;

            case 4:
                View v5 = inflater.inflate(R.layout.item_home_top5, parent, false);
                viewHolder = new HomeTopHolder5(v5);
                break;

            case 5:
                View v6 = inflater.inflate(R.layout.item_home_top6, parent, false);
                viewHolder = new HomeTopHolder6(v6);
                break;

            case 6:
                View v7 = inflater.inflate(R.layout.item_home_top7, parent, false);
                viewHolder = new HomeTopHolder7(v7);
                break;

            case 7:
                View v8 = inflater.inflate(R.layout.item_home_top8, parent, false);
                viewHolder = new HomeTopHolder8(v8);
                break;

            case 8:
                View v9 = inflater.inflate(R.layout.item_home_top9, parent, false);
                viewHolder = new HomeTopHolder9(v9);
                break;


        }

        assert viewHolder != null;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                ((HomeTopHolder1) holder).bindData(position);
                break;
            case 1:
                ((HomeTopHolder2) holder).bindData(position);
                break;
            case 2:
                ((HomeTopHolder3) holder).bindData(position);
                break;
            case 3:
                ((HomeTopHolder4) holder).bindData(position);
                break;
            case 4:
                ((HomeTopHolder5) holder).bindData(position);
                break;
            case 5:
                ((HomeTopHolder6) holder).bindData(position);
                break;
            case 6:
                ((HomeTopHolder7) holder).bindData(position);
                break;
            case 7:
                ((HomeTopHolder8) holder).bindData(position);
                break;
            case 8:
                ((HomeTopHolder9) holder).bindData(position);
                break;

        }
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    @Override
    public int getItemViewType(int position) {
        return CatTheme;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class HomeTopHolder1 extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imageView;
        RelativeLayout layout,mainLyt;

        public HomeTopHolder1(View view) {
            super(view);
            tvTitle = itemView.findViewById(R.id.tvtitle);
            layout = itemView.findViewById(R.id.layout);
            imageView = itemView.findViewById(R.id.icon);
            mainLyt = itemView.findViewById(R.id.mainLyt);
        }

        public void bindData(int posi) {
            switch (layoutMode){
                case 0:
                    final float scale = context.getResources().getDisplayMetrics().density;
                    int pixels = (int) (110 * scale + 0.5f);
                    RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(pixels, ViewGroup.LayoutParams.WRAP_CONTENT);
                    mainLyt.setLayoutParams(rel_btn);
                    break;

                case 2:
                    mainLyt.setMinimumWidth(getScreenWidth()/2);
                    break;

                case 3:
                    mainLyt.setMinimumWidth(getScreenWidth()/3);
                    break;
            }


            int back = list.get(posi).getBtn_background();
            if (back == 0) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.top_vector1));
            } else if (back == 1) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.top_vector2));
            } else if (back == 2) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.top_vector3));
            } else if (back == 3) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.top_vector4));
            } else if (back == 4) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.top_vector5));
            } else if (back == 5) {
                if (list.get(posi).getBackground_color() != null) {
                    layout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(list.get(posi).getBackground_color())));
                }
            }
            tvTitle.setText(list.get(posi).getTitle());

            Picasso.get().load(WebApi.Api.IMAGES + list.get(posi).getIcon()).fit().into(imageView);


            itemView.setOnClickListener(v -> {
                Const.TOOLBAR_TITLE = list.get(posi).getTitle();
                response(list.get(posi).getBtn_action(), list.get(posi).getUrl(),context);
            });
        }
    }

    private class HomeTopHolder2 extends RecyclerView.ViewHolder {
        TextView tvTitle, subtitle, btnName;
        ImageView imageView;
        RelativeLayout layout, start,mainLyt;

        public HomeTopHolder2(View view) {
            super(view);
            tvTitle = itemView.findViewById(R.id.tvtitle);
            subtitle = itemView.findViewById(R.id.subtitle);
            layout = itemView.findViewById(R.id.background);
            start = itemView.findViewById(R.id.start);
            imageView = itemView.findViewById(R.id.icon);
            btnName = itemView.findViewById(R.id.btnName);
            mainLyt = itemView.findViewById(R.id.mainLyt);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public void bindData(int posi) {

            switch (layoutMode){
                case 0:
                    final float scale = context.getResources().getDisplayMetrics().density;
                    int pixels = (int) (130 * scale + 0.5f);
                    RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(pixels, ViewGroup.LayoutParams.WRAP_CONTENT);
                    mainLyt.setLayoutParams(rel_btn);
                    break;

                case 2:
                    mainLyt.setMinimumWidth(getScreenWidth()/2);
                    break;

                case 3:
                    mainLyt.setMinimumWidth(getScreenWidth()/3);
                    break;
            }


            if (list.get(posi).getBackground_color() != null) {
                layout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(list.get(posi).getBackground_color())));
            }
            if (list.get(posi).getBtn_color() != null) {
                start.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(list.get(posi).getBtn_color())));
            }

            tvTitle.setText(list.get(posi).getTitle());

            if (list.get(posi).getSubtitle() != null) {
                subtitle.setText(list.get(posi).getSubtitle());
            }
            if (list.get(posi).getBtn_name() != null) {
                btnName.setText(list.get(posi).getBtn_name());
            }

            Picasso.get().load(WebApi.Api.IMAGES + list.get(posi).getIcon()).fit().into(imageView);

            itemView.setOnClickListener(v -> {
                Const.TOOLBAR_TITLE = list.get(posi).getTitle();
                response(list.get(posi).getBtn_action(), list.get(posi).getUrl(),context);
            });
        }
    }

    private class HomeTopHolder3 extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imageView;
        LinearLayout layout;
        RelativeLayout mainLyt;

        public HomeTopHolder3(View view) {
            super(view);
            tvTitle = itemView.findViewById(R.id.tvtitle);
            layout = itemView.findViewById(R.id.background);
            imageView = itemView.findViewById(R.id.icon);
            mainLyt = itemView.findViewById(R.id.mainLyt);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public void bindData(int posi) {
            switch (layoutMode){

                case 2:
                    final float scale = context.getResources().getDisplayMetrics().density;
                    int pixels = (int) (100 * scale + 0.5f);
                    int wipixels = (int) (160 * scale + 0.5f);
                    CardView.LayoutParams rel_btn = new CardView.LayoutParams(wipixels,pixels);
                    mainLyt.setLayoutParams(rel_btn);
                    break;

            }

            int back = list.get(posi).getBtn_background();
            if (back == 0) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg1));
            } else if (back == 1) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg2));
            } else if (back == 2) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg3));
            } else if (back == 3) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg4));
            } else if (back == 4) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg5));
            } else if (back == 5) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg6));
            } else if (back == 6) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg7));
            } else if (back == 7) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg8));
            } else if (back == 8) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg9));
            } else if (back == 9) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg10));
            } else if (back == 10) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg11));
            } else {
                if (list.get(posi).getBackground_color() != null) {
                    layout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(list.get(posi).getBackground_color())));
                }
            }

            tvTitle.setText(list.get(posi).getTitle());

            Picasso.get().load(WebApi.Api.IMAGES + list.get(posi).getIcon()).fit().into(imageView);


            itemView.setOnClickListener(v -> {
                Const.TOOLBAR_TITLE = list.get(posi).getTitle();
                response(list.get(posi).getBtn_action(), list.get(posi).getUrl(),context);
            });
        }
    }

    private class HomeTopHolder4 extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imageView;
        RelativeLayout layout;

        public HomeTopHolder4(View view) {
            super(view);
            tvTitle = itemView.findViewById(R.id.tvtitle);
            layout = itemView.findViewById(R.id.background);
            imageView = itemView.findViewById(R.id.icon);
        }

        public void bindData(int posi) {
            tvTitle.setText(list.get(posi).getTitle());
            Picasso.get().load(WebApi.Api.IMAGES + list.get(posi).getIcon()).fit().into(imageView);

            itemView.setOnClickListener(v -> {
                Const.TOOLBAR_TITLE = list.get(posi).getTitle();
                response(list.get(posi).getBtn_action(), list.get(posi).getUrl(),context);
            });
        }
    }

    private class HomeTopHolder5 extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imageView;
        RelativeLayout layout,mainLyt;
        CardView cv;

        public HomeTopHolder5(View view) {
            super(view);
            tvTitle = itemView.findViewById(R.id.tvtitle);
            layout = itemView.findViewById(R.id.background);
            imageView = itemView.findViewById(R.id.icon);
            cv = itemView.findViewById(R.id.cv);
            mainLyt = itemView.findViewById(R.id.mainLyt);

        }

        public void bindData(int posi) {
            switch (layoutMode){
                case 0:
                    final float scale = context.getResources().getDisplayMetrics().density;
                    int pixels = (int) (140 * scale + 0.5f);
                    int wipixels = (int) (160 * scale + 0.5f);
                    CardView.LayoutParams rel_btn = new CardView.LayoutParams(wipixels,pixels);
                    mainLyt.setLayoutParams(rel_btn);
                    break;

            }
            tvTitle.setText(list.get(posi).getTitle());
            Picasso.get().load(WebApi.Api.IMAGES + list.get(posi).getIcon()).fit().into(imageView);

            itemView.setOnClickListener(v -> {
                Const.TOOLBAR_TITLE = list.get(posi).getTitle();
                response(list.get(posi).getBtn_action(), list.get(posi).getUrl(),context);
            });
        }
    }

    private class HomeTopHolder6 extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imageView;
        RelativeLayout layout;
        CardView cv;

        public HomeTopHolder6(View view) {
            super(view);
            tvTitle = itemView.findViewById(R.id.tvtitle);
            layout = itemView.findViewById(R.id.background);
            imageView = itemView.findViewById(R.id.icon);
            cv = itemView.findViewById(R.id.cv);
        }

        public void bindData(int posi) {
//            cv.setLayoutParams(new CardView.LayoutParams(getScreenWidth() / 2, 120));
            if (list.get(posi).getBackground_color() != null) {
                layout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(list.get(posi).getBackground_color())));
            }
            tvTitle.setText(list.get(posi).getTitle());
            Picasso.get().load(WebApi.Api.IMAGES + list.get(posi).getIcon()).fit().into(imageView);

            itemView.setOnClickListener(v -> {
                Const.TOOLBAR_TITLE = list.get(posi).getTitle();
                response(list.get(posi).getBtn_action(), list.get(posi).getUrl(),context);
            });
        }
    }

    private class HomeTopHolder7 extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imageView;
        RelativeLayout layout;
        CardView cv;

        public HomeTopHolder7(View view) {
            super(view);
            tvTitle = itemView.findViewById(R.id.tvtitle);
            layout = itemView.findViewById(R.id.background);
            imageView = itemView.findViewById(R.id.icon);
            cv = itemView.findViewById(R.id.cv);
        }

        public void bindData(int posi) {
//            cv.setLayoutParams(new CardView.LayoutParams(getScreenWidth() / 2, 120));
            if (list.get(posi).getBackground_color() != null) {
                layout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(list.get(posi).getBackground_color())));
            }
            tvTitle.setText(list.get(posi).getTitle());
            Picasso.get().load(WebApi.Api.IMAGES + list.get(posi).getIcon()).fit().into(imageView);

            itemView.setOnClickListener(v -> {
                Const.TOOLBAR_TITLE = list.get(posi).getTitle();
                response(list.get(posi).getBtn_action(), list.get(posi).getUrl(),context);
            });
        }
    }

    private class HomeTopHolder8 extends RecyclerView.ViewHolder {
        TextView tvTitle, subtitle, start;
        ImageView imageView;
        LinearLayout layout;

        public HomeTopHolder8(View view) {
            super(view);
            tvTitle = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            layout = itemView.findViewById(R.id.background);
            start = itemView.findViewById(R.id.start);
            imageView = itemView.findViewById(R.id.icon);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public void bindData(int posi) {
            int back = list.get(posi).getBtn_background();
            if (back == 0) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg1));
            } else if (back == 1) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg2));
            } else if (back == 2) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg3));
            } else if (back == 3) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg4));
            } else if (back == 4) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg5));
            } else if (back == 5) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg6));
            } else if (back == 6) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg7));
            } else if (back == 7) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg8));
            } else if (back == 8) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg9));
            } else if (back == 9) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg10));
            } else if (back == 10) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg11));
            } else {
                if (list.get(posi).getBackground_color() != null) {
                    layout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(list.get(posi).getBackground_color())));
                }
                if (list.get(posi).getBtn_color() != null) {
                    start.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(list.get(posi).getBackground_color())));
                }
            }

            tvTitle.setText(list.get(posi).getTitle());

            if (list.get(posi).getSubtitle() != null) {
                subtitle.setText(list.get(posi).getSubtitle());
            }
            if (list.get(posi).getBtn_name() != null) {
                start.setText(list.get(posi).getBtn_name());
            }

            Picasso.get().load(WebApi.Api.IMAGES + list.get(posi).getIcon()).fit().into(imageView);


            itemView.setOnClickListener(v -> {
                Const.TOOLBAR_TITLE = list.get(posi).getTitle();
                response(list.get(posi).getBtn_action(), list.get(posi).getUrl(),context);
            });
        }
    }

    private class HomeTopHolder9 extends RecyclerView.ViewHolder {
        TextView tvTitle, subtitle, start;
        ImageView imageView;
        ConstraintLayout layout;

        public HomeTopHolder9(View view) {
            super(view);
            tvTitle = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            layout = itemView.findViewById(R.id.background);
            start = itemView.findViewById(R.id.start);
            imageView = itemView.findViewById(R.id.icon);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public void bindData(int posi) {
            int back = list.get(posi).getBtn_background();
            if (back == 0) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg1));
            } else if (back == 1) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg2));
            } else if (back == 2) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg3));
            } else if (back == 3) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg4));
            } else if (back == 4) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg5));
            } else if (back == 5) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg6));
            } else if (back == 6) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg7));
            } else if (back == 7) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg8));
            } else if (back == 8) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg9));
            } else if (back == 9) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.bg10));
            } else if (back == 10) {
                layout.setBackground(context.getDrawable(R.drawable.bg11));
            } else {
                if (list.get(posi).getBackground_color() != null) {
                    layout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(list.get(posi).getBackground_color())));
                }
            }

            tvTitle.setText(list.get(posi).getTitle());

            if (list.get(posi).getSubtitle() != null) {
                subtitle.setText(list.get(posi).getSubtitle());
            }
            if (list.get(posi).getBtn_name() != null) {
                start.setText(list.get(posi).getBtn_name());
            }

            Picasso.get().load(WebApi.Api.IMAGES + list.get(posi).getIcon()).fit().into(imageView);


            itemView.setOnClickListener(v -> {
                Const.TOOLBAR_TITLE = list.get(posi).getTitle();
                response(list.get(posi).getBtn_action(), list.get(posi).getUrl(),context);
            });
        }
    }



    int getWidth(double flot){
       return (int) (getScreenWidth()/flot);
    }

}
