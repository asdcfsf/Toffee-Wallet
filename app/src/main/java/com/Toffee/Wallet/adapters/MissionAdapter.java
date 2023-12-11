package com.Toffee.Wallet.adapters;

import static com.Toffee.Wallet.utils.AdsConfig.AD_LOADING_INTERVAL;
import static com.Toffee.Wallet.utils.Const.Currency;
import static com.Toffee.Wallet.utils.Fun.addToWallet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.Responsemodel.MissionResponse;
import com.Toffee.Wallet.ads.RewardAds;
import com.Toffee.Wallet.databinding.LayoutCollectBonusBinding;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.ClickAction;
import com.Toffee.Wallet.utils.Fun;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MissionAdapter extends RecyclerView.Adapter {
    Context ctx;
    List<MissionResponse.Data> list;
    LayoutInflater inflater;
    RecyclerView.ViewHolder viewHolder;
    OnItemClickListener clickListener;
    AlertDialog addWallet, loading;
    LayoutCollectBonusBinding collectBonusBinding;
    boolean closeDialog;
    RewardAds.Builder rewardAds;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public MissionAdapter(Context c, List<MissionResponse.Data> dataItems) {
        this.inflater = LayoutInflater.from(c);
        this.list = dataItems;
        this.ctx = c;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewHolder = null;
        if (viewType == 0) {
            View v1 = inflater.inflate(R.layout.item_mission, parent, false);
            viewHolder = new MissionHolder(v1);
        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder_parent, int position) {
        if (getItemViewType(position) == 0) {
            ((MissionHolder) holder_parent).bindData(position);
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

    private class MissionHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, coins, promo_coins, limit, tvClaim;
        ImageView imageView;
        LinearLayout lytPromocoin, claim;

        public MissionHolder(View view) {
            super(view);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imageView = itemView.findViewById(R.id.image);
            coins = itemView.findViewById(R.id.coins);
            promo_coins = itemView.findViewById(R.id.promo_coins);
            limit = itemView.findViewById(R.id.limit);
            tvClaim = itemView.findViewById(R.id.tvClaim);
            claim = itemView.findViewById(R.id.claim);
            lytPromocoin = itemView.findViewById(R.id.lytPromocoin);
        }

        @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
        public void bindData(int posi) {
            final MissionResponse.Data resp = list.get(posi);

            tvTitle.setText(resp.getTitle());
            limit.setText(resp.getCount() + "/" + resp.getLimit());
            coins.setText("" + resp.getCoin());
            promo_coins.setText("" + resp.getPromo_coin());

            if (resp.getStatus().equals("0")) {
                tvClaim.setText(resp.getBtn_name());

                if (resp.getCount() != null && Integer.parseInt(resp.getCount()) >= Integer.parseInt(resp.getLimit())) {
                    tvClaim.setText(ctx.getString(R.string.collect));
                    claim.setBackground(ctx.getResources().getDrawable(R.drawable.bg_pending));
                }
            }
            Picasso.get().load(WebApi.Api.IMAGES+resp.getImage()).into(imageView);
            if (!resp.getStatus().equals("0")) {
                lytPromocoin.setVisibility(View.VISIBLE);
            }

            if (resp.getStatus().equals("1")) {
                claim.setEnabled(false);
                tvClaim.setText(ctx.getString(R.string.collected));
                claim.setBackground(ctx.getResources().getDrawable(R.drawable.bg_success));
            }

            claim.setOnClickListener(view -> {
                if (resp.getStatus().equals("0")) {
                    if (resp.getCount() != null && Integer.parseInt(resp.getCount()) >= Integer.parseInt(resp.getLimit())) {
                        try {
                            rewardAds = new RewardAds.Builder((Activity) ctx);
                            rewardAds.buildAd(1);
                            loading = Fun.loading(ctx);

                            collectBonusBinding = LayoutCollectBonusBinding.inflate(((Activity) ctx).getLayoutInflater());
                            addWallet = addToWallet(ctx, collectBonusBinding);

                            collectBonusBinding.msg.setText(ctx.getString(R.string.you_ve_get) + " " + list.get(posi).getCoin() + "" + Currency);
                            addWallet.show();
                            collectBonusBinding.showAd.setOnClickListener(v -> {
                                collectBonusBinding.showAd.setEnabled(false);
                                collectBonusBinding.showAd.setAlpha(0.7f);
                                if (rewardAds.isAdLoaded()) {
                                    rewardAds.showReward();
                                } else {
                                    rewardAds.buildAd(1);
                                    new CountDownTimer(AD_LOADING_INTERVAL, 1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            ((Activity) ctx).runOnUiThread(() -> collectBonusBinding.showAd.setText("Loading Ad " + (millisUntilFinished / 1000)));
                                        }

                                        @Override
                                        public void onFinish() {
                                            if (rewardAds.isAdLoaded()) {
                                                rewardAds.showReward();
                                            }
                                        }
                                    }.start();
                                }
                                credit(list.get(posi).getId());
                            });

                            collectBonusBinding.close.setOnClickListener(view1 -> {
                                claim.setEnabled(false);
                                tvClaim.setText(ctx.getString(R.string.collected));
                                claim.setBackground(ctx.getResources().getDrawable(R.drawable.bg_success));
                                addWallet.dismiss();
                            });

                        } catch (Exception e) {
                        }
                    } else {
                        ClickAction.response(resp.getBtn_action(), null, ctx);
                    }
                }
            });
        }

        void credit(String id) {
            loading.show();
            Objects.requireNonNull(ApiClient.restAdapter(ctx)).create(ApiInterface.class).api(Fun.js((Activity) ctx,"missionValidation", "", "", "", id)).enqueue(new Callback<DefResp>() {
                @Override
                public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                    collectBonusBinding.showAd.setVisibility(View.GONE);
                    collectBonusBinding.close.setVisibility(View.VISIBLE);
                    loading.dismiss();
                    closeDialog = true;
                    if (response.isSuccessful() && response.body().getCode().equals("201")) {
                        collectBonusBinding.animationView.setImageAssetsFolder("raw/");
                        collectBonusBinding.animationView.setAnimation(R.raw.success);
                        collectBonusBinding.animationView.setSpeed(1f);
                        collectBonusBinding.animationView.playAnimation();
                        collectBonusBinding.congrts.setText(ctx.getString(R.string.congratulations));
                        collectBonusBinding.msg.setText(ctx.getString(R.string.coin_added_successfull).replace("coin", Currency));
                    } else {
                        collectBonusBinding.animationView.setImageAssetsFolder("raw/");
                        collectBonusBinding.animationView.setAnimation(R.raw.notice);
                        collectBonusBinding.animationView.setSpeed(1f);
                        collectBonusBinding.animationView.playAnimation();
                        collectBonusBinding.watchAd.setVisibility(View.GONE);
                        collectBonusBinding.close.setVisibility(View.VISIBLE);
                        collectBonusBinding.congrts.setText(ctx.getString(R.string.oops));
                        collectBonusBinding.congrts.setTextColor(ctx.getResources().getColor(R.color.red));
                        collectBonusBinding.msg.setText(response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<DefResp> call, Throwable t) {
                    loading.dismiss();
                }
            });
        }

    }

}
