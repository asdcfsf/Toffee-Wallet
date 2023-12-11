package com.Toffee.Wallet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.SettingResp;
import com.Toffee.Wallet.adapters.OnboardingAdapter;
import com.Toffee.Wallet.databinding.ActivityOnboardingBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {
    Activity activity;
    ActivityOnboardingBinding bind;
    private OnboardingAdapter adapter;
    public static List<SettingResp.OnboardingItem> list= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        activity=this;

        adapter = new OnboardingAdapter(activity,list);
        bind.vp4.setAdapter(adapter);
        bind.vp4.setPageTransformer(false, new DepthPagerTransformer());

        bind.tabLayout1.setupWithViewPager(bind.vp4);

        bind.goNext.setOnClickListener(v -> {
            int position = bind.vp4.getCurrentItem();
            if (position < list.size()-1) {
                position++;
                bind.ll7.setVisibility(View.VISIBLE);
                bind.ll8.setVisibility(View.GONE);
                bind.vp4.setCurrentItem(position);
            } else if (position == list.size() - 1) {
                doVisibilityOperation();
            }
        });

        bind.getStarted.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(activity, LoginMainActivity.class));
        });

        bind.tabLayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() ==list.size() - 1) {
                    doVisibilityOperation();
                } else {
                    bind.ll7.setVisibility(View.VISIBLE);
                    bind.ll8.setVisibility(View.GONE);
                    bind.getStarted.animate().setInterpolator(new BounceInterpolator()).scaleXBy(1).scaleX(0).scaleYBy(1).scaleY(0);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void doVisibilityOperation() {
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.bottom_anim);
        bind.ll8.setAnimation(animation);
        bind.ll8.setVisibility(View.VISIBLE);
        bind.ll7.setVisibility(View.GONE);
        bind.getStarted.animate()
                .setInterpolator(new BounceInterpolator())
                .setDuration(1000)
                .scaleXBy(0)
                .scaleX(1)
                .scaleYBy(0)
                .scaleY(1);
    }

    private class DepthPagerTransformer implements ViewPager.PageTransformer {
        public static final float MIN_SCALE = 0.75f;

        @Override
        public void transformPage(@NonNull View page, float position) {
            int pageWidth = page.getWidth();

            if (position < -1) {
                //left most
                page.setAlpha(0);
            } else if (position <= 0) {
                //from center to first left
                page.setAlpha(1);
                page.setTranslationX(0);
                page.setScaleY(1);
                page.setScaleX(1);

            } else if (position <= 1) {
                //at center to first right
                page.setAlpha(1 - position);
                page.setTranslationX(pageWidth * -position);

                float scaling = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                page.setScaleX(scaling);
                page.setScaleY(scaling);
                page.animate().setInterpolator(new AccelerateDecelerateInterpolator());
            } else {
                page.setAlpha(0);
            }

        }
    }

}

