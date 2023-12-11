package com.Toffee.Wallet.fragment;

import static com.Toffee.Wallet.utils.Fun.js;
import static com.Toffee.Wallet.utils.Fun.msgSuccess;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import com.ads.androidsdk.sdk.util.Tool;
import com.Toffee.Wallet.utils.AdsConfig;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.BannerResp;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.Responsemodel.HomeModal;
import com.Toffee.Wallet.Responsemodel.HomeResp;
import com.Toffee.Wallet.activities.NotificationActivity;
import com.Toffee.Wallet.activities.Splash;
import com.Toffee.Wallet.activities.StoreList;
import com.Toffee.Wallet.adapters.ImageSliderAdepter;
import com.Toffee.Wallet.adapters.MainRecyclerAdapter;
import com.Toffee.Wallet.databinding.FragmentHomeBinding;
import com.Toffee.Wallet.databinding.LayoutAlertBinding;
import com.Toffee.Wallet.databinding.LayoutPopupBinding;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.ClickAction;
import com.Toffee.Wallet.utils.DatabaseHandler;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment  {
    FragmentHomeBinding bind;
    Activity activity;
    Pref pref;
    AlertDialog banner_dialog,alert;
    LayoutAlertBinding lytAlert;
    private final Handler sliderHandler = new Handler();
    MainRecyclerAdapter homeAdapter;
    DatabaseHandler db;
    List<HomeModal> homeModalArrayList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bind=FragmentHomeBinding.inflate(getLayoutInflater());
        activity = getActivity();
        pref = new Pref(activity);

        lytAlert = LayoutAlertBinding.inflate(getLayoutInflater());
        alert = Fun.Alerts(activity,lytAlert);

        bind.name.setText(pref.getString(pref.NAME));

        if (pref.getString(pref.PROFILE) != null) {
            if (pref.getString(pref.PROFILE).equals("userpro.png")) {
                bind.profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));
            } else if (pref.getString(pref.PROFILE).startsWith("http")) {
                Picasso.get().load(pref.getString(pref.PROFILE)).error(R.drawable.ic_user).fit().into(bind.profile);
            } else {
                Picasso.get().load(WebApi.Api.USERTHUMB + pref.getString(pref.PROFILE)).error(R.drawable.ic_user).fit().into(bind.profile);
            }
        } else {
            bind.profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));
        }

        db=new DatabaseHandler(activity);
        if (pref.getString(AdsConfig.homeMsg) != null && !pref.getString(AdsConfig.homeMsg).equals("")) {
            bind.lytAlert.setVisibility(View.VISIBLE);
            bind.alertMsg.setText(pref.getString(AdsConfig.homeMsg));
            bind.alertMsg.setSelected(true);
        }

        bind.alertMsg.setOnClickListener(v -> {
             showAlert();
        });

        bind.lytNoti.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), NotificationActivity.class));
        });

        initRv();
        promotionBanner();
        slideBanner();

        bind.layoutCoin.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), StoreList.class));
        });
        bind.refresh.setOnClickListener(v -> {
            bind.refresh.setEnabled(false);
            Glide.with(requireActivity()).asGif().load(R.drawable.loading).into(bind.refresh);
            reload_coin();
        });

        return  bind.getRoot();
    }


    private void reload_coin() {
       try {
           Call<DefResp> call = Objects.requireNonNull(ApiClient.restAdapter(getActivity())).create(ApiInterface.class).api(js(activity,"refreshCoin","","","",""));
           call.enqueue(new Callback<DefResp>() {
               @Override
               public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                   if(response.isSuccessful() && response.body().getCode().equals("201")){
                       pref.setIntData(pref.WALLET,response.body().getBalance());
                       bind.coins.setText(Fun.coolNumberFormat(Long.parseLong(String.valueOf(pref.getBalance()))));
                       msgSuccess(activity, "Balance Updated");
                       new Handler().postDelayed(() -> bind.refresh.setEnabled(true),5000);
                       bind.refresh.setImageResource(R.drawable.ic_baseline_autorenew_24);
                   }
               }

               @Override
               public void onFailure(Call<DefResp> call, Throwable t) {

               }
           });
       }catch (Exception e){}
    }

    private void initRv() {
       List<HomeResp> resp;
       resp=db.getCat();

        for(int i=0; i<resp.size(); i++){
            try {
                homeModalArrayList.add(new HomeModal(resp,db.getHomeList(resp.get(i).getId()),null));
            }catch (Exception e){}
        }

        bind.rvTop.setLayoutManager(new LinearLayoutManager(activity));
        homeAdapter=new MainRecyclerAdapter(activity,homeModalArrayList);
        bind.rvTop.setAdapter(homeAdapter);

    }

    @Override
    public void onResume() {
        try {
            bind.coins.setText(Tool.coolNumberFormat(Long.parseLong(String.valueOf(pref.getBalance()))));
            bind.noticount.setText(String.valueOf(pref.getInt(pref.NOTI)));
        } catch (Exception e) {
        }
        super.onResume();
    }

    private void showAlert() {
        alert.show();
        lytAlert.title.setText(getString(R.string.announcement));
        lytAlert.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_follow));
        lytAlert.msg.setText(pref.getString(AdsConfig.homeMsg));
        lytAlert.negative.setOnClickListener(v -> {
            alert.dismiss();
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void promotionBanner() {
        if (pref.getBoolean(Pref.ENABLE_Banner)) {
            inAppReview();
            LayoutPopupBinding bannerBind;
            bannerBind = LayoutPopupBinding.inflate(getLayoutInflater());
            banner_dialog = new AlertDialog.Builder(activity).setView(bannerBind.getRoot()).create();
            banner_dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            banner_dialog.getWindow().setWindowAnimations(R.style.Dialoganimation);
            banner_dialog.setCancelable(true);

            Call<BannerResp> call = ApiClient.restAdapter(activity).create(ApiInterface.class).PromoBanner();
            call.enqueue(new Callback<BannerResp>() {
                @Override
                public void onResponse(Call<BannerResp> call, Response<BannerResp> response) {
                    pref.setBoolean(Pref.ENABLE_Banner, false);
                    if (response.isSuccessful() && response.body().getData().size() != 0) {
                        banner_dialog.show();

                        Picasso.get().load(WebApi.Api.IMAGES + response.body().getData().get(0).getBanner()).fit().into(bannerBind.banner);
                        bannerBind.close.setOnClickListener(v -> {
                            banner_dialog.dismiss();
                        });

                        bannerBind.banner.setOnClickListener(v -> {
                            banner_dialog.dismiss();
                            ClickAction.response(response.body().getData().get(0).getOnclick(),response.body().getData().get(0).getLink(),activity);
                        });
                    }
                }

                @Override
                public void onFailure(Call<BannerResp> call, Throwable t) {
                }
            });
        }
    }

    private void slideBanner() {

        bind.ViewPagerImageSlider.setClipToPadding(false);
        bind.ViewPagerImageSlider.setClipChildren(false);
        bind.ViewPagerImageSlider.setOffscreenPageLimit(3);
        bind.ViewPagerImageSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.20f);
            page.setScaleX(0.90f + r * 0.20f);
        });

        bind.ViewPagerImageSlider.setPageTransformer(compositePageTransformer);

        bind.ViewPagerImageSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
        Call<BannerResp> call = ApiClient.restAdapter(activity).create(ApiInterface.class).promoBanner(js(activity,"","","","",""));
        call.enqueue(new Callback<BannerResp>() {
            @Override
            public void onResponse(Call<BannerResp> call, Response<BannerResp> response) {
                if (response.isSuccessful() && response.body().getData().size() > 0) {
                    bind.ViewPagerImageSlider.setAdapter(new ImageSliderAdepter(response.body().getData(), bind.ViewPagerImageSlider));
                } else {
                    bind.ViewPagerImageSlider.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<BannerResp> call, Throwable t) {
                bind.ViewPagerImageSlider.setVisibility(View.GONE);
            }
        });
    }

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            bind.ViewPagerImageSlider.setCurrentItem(bind.ViewPagerImageSlider.getCurrentItem() + 1);
        }
    };

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        pref.setData(pref.SELECTED_LANGUAGE,lang);
        startActivity(new Intent(activity, Splash.class));
    }

    void inAppReview(){
       /* reviewManager = ReviewManagerFactory.create(activity);
        com.google.android.play.core.tasks.Task<ReviewInfo> request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Getting the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();
                com.google.android.play.core.tasks.Task<Void> flow = reviewManager.launchReviewFlow(activity, reviewInfo);
                flow.addOnCompleteListener(task1 -> {  });
            }
        });*/
    }

}
