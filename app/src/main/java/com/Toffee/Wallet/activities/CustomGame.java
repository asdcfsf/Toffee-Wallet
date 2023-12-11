package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.Fun.loading;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.Toffee.Wallet.databinding.LayoutProgressLoadingBinding;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.Toffee.Wallet.databinding.ActivityCustomGameBinding;

public class CustomGame extends AppCompatActivity {
    ActivityCustomGameBinding bind;
    Activity activity;
    Pref pref;
    Bundle bundle;
    AlertDialog loading, progressLoading;
    String viewType, offAnim, playAnim;
    float animSpeed = 0.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind = ActivityCustomGameBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());
        activity = this;

        progressLoading = Fun.loadingProgress(activity);
        progressLoading.show();
        loading = loading(activity);

        bundle = getIntent().getExtras();
        bind.tool.title.setText(bundle.getString("title"));
        pref = new Pref(activity);
        offAnim = bundle.getString("anim_off");
        playAnim =bundle.getString("anim_play");
        viewType = "0";

        try {
            Glide.with(this).load(WebApi.Api.GAME_THUMB + bundle.getString("background")).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    progressLoading.dismiss();
                    bind.mainLyt.setBackground(resource);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                    progressLoading.dismiss();
                }
            });
        } catch (Exception e) {
            progressLoading.dismiss();
        }

        prepareLayout();
        PrepareAnimation();
        onClickEvent();


        bind.tool.back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void prepareLayout() {
        switch (viewType) {
            case "0":
                bind.grid9.setVisibility(View.VISIBLE);
                break;

            case "1":
                bind.grid4.setVisibility(View.VISIBLE);
                break;

            case "2":
                bind.grid1.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void onClickEvent() {

        bind.i1.setOnClickListener(v -> {
            playAnim(1);
        });
        bind.i2.setOnClickListener(v -> {
            playAnim(2);
        });
        bind.i3.setOnClickListener(v -> {
            playAnim(3);
        });
        bind.i4.setOnClickListener(v -> {
            playAnim(4);
        });
        bind.i5.setOnClickListener(v -> {
            playAnim(5);
        });
        bind.i6.setOnClickListener(v -> {
            playAnim(6);
        });
        bind.i7.setOnClickListener(v -> {
            playAnim(7);
        });
        bind.i8.setOnClickListener(v -> {
            playAnim(8);
        });
        bind.i9.setOnClickListener(v -> {
            playAnim(9);
        });

        bind.i10.setOnClickListener(v -> {
            playAnim(10);
        });
        bind.i11.setOnClickListener(v -> {
            playAnim(11);
        });

        bind.i12.setOnClickListener(v -> {
            playAnim(12);
        });

        bind.i13.setOnClickListener(v -> {
            playAnim(13);
        });

        bind.i14.setOnClickListener(v -> {
            playAnim(14);
        });
    }

    void playAnim(int type) {
        btnStatus(false);
        switch (type) {
            case 1:
                bind.i1.setAnimationFromUrl(playAnim);
                bind.i1.setSpeed(animSpeed);
                bind.i1.playAnimation();
                break;

            case 2:
                bind.i2.setAnimationFromUrl(playAnim);
                bind.i2.setSpeed(animSpeed);
                bind.i2.playAnimation();
                break;

            case 3:
                bind.i3.setAnimationFromUrl(playAnim);
                bind.i3.setSpeed(animSpeed);
                bind.i3.playAnimation();
                break;

            case 4:
                bind.i4.setAnimationFromUrl(playAnim);
                bind.i4.setSpeed(animSpeed);
                bind.i4.playAnimation();
                break;

            case 5:
                bind.i5.setAnimationFromUrl(playAnim);
                bind.i5.setSpeed(animSpeed);
                bind.i5.playAnimation();
                break;

            case 6:
                bind.i6.setAnimationFromUrl(playAnim);
                bind.i6.setSpeed(animSpeed);
                bind.i6.playAnimation();
                break;

            case 7:
                bind.i7.setAnimationFromUrl(playAnim);
                bind.i7.setSpeed(animSpeed);
                bind.i7.playAnimation();
                break;

            case 8:
                bind.i8.setAnimationFromUrl(playAnim);
                bind.i8.setSpeed(animSpeed);
                bind.i8.playAnimation();
                break;

            case 9:
                bind.i9.setAnimationFromUrl(playAnim);
                bind.i9.setSpeed(animSpeed);
                bind.i9.playAnimation();
                break;

            case 10:
                bind.i10.setAnimationFromUrl(playAnim);
                bind.i10.setSpeed(animSpeed);
                bind.i10.playAnimation();
                break;

            case 11:
                bind.i11.setAnimationFromUrl(playAnim);
                bind.i11.setSpeed(animSpeed);
                bind.i11.playAnimation();
                break;

            case 12:
                bind.i12.setAnimationFromUrl(playAnim);
                bind.i12.setSpeed(animSpeed);
                bind.i12.playAnimation();
                break;

            case 13:
                bind.i13.setAnimationFromUrl(playAnim);
                bind.i13.setSpeed(animSpeed);
                bind.i13.playAnimation();
                break;

            case 14:
                bind.i14.setAnimationFromUrl(playAnim);
                bind.i14.setSpeed(animSpeed);
                bind.i14.playAnimation();
                break;
        }
    }

    private void btnStatus(boolean Active) {
        switch (viewType) {
            case "0":
                bind.i1.setEnabled(Active);
                bind.i2.setEnabled(Active);
                bind.i3.setEnabled(Active);
                bind.i4.setEnabled(Active);
                bind.i5.setEnabled(Active);
                bind.i6.setEnabled(Active);
                bind.i7.setEnabled(Active);
                bind.i8.setEnabled(Active);
                bind.i9.setEnabled(Active);
                break;

            case "1":
                bind.i10.setEnabled(Active);
                bind.i11.setEnabled(Active);
                bind.i12.setEnabled(Active);
                bind.i13.setEnabled(Active);
                break;

            case "2":
                bind.i14.setEnabled(Active);
                break;
        }
    }

    private void PrepareAnimation() {
        switch (viewType) {
            case "0":
                bind.i1.setAnimationFromUrl(offAnim);
                bind.i1.playAnimation();

                bind.i2.setAnimationFromUrl(offAnim);
                bind.i2.playAnimation();

                bind.i3.setAnimationFromUrl(offAnim);
                bind.i3.playAnimation();

                bind.i4.setAnimationFromUrl(offAnim);
                bind.i4.playAnimation();

                bind.i5.setAnimationFromUrl(offAnim);
                bind.i5.playAnimation();

                bind.i6.setAnimationFromUrl(offAnim);
                bind.i6.playAnimation();

                bind.i7.setAnimationFromUrl(offAnim);
                bind.i7.playAnimation();

                bind.i8.setAnimationFromUrl(offAnim);
                bind.i8.playAnimation();

                bind.i9.setAnimationFromUrl(offAnim);
                bind.i9.playAnimation();

                break;

            case "1":
                bind.i10.setAnimationFromUrl(offAnim);
                bind.i10.playAnimation();

                bind.i11.setAnimationFromUrl(offAnim);
                bind.i11.playAnimation();

                bind.i12.setAnimationFromUrl(offAnim);
                bind.i12.playAnimation();

                bind.i13.setAnimationFromUrl(offAnim);
                bind.i13.playAnimation();
                break;

            case "2":
                bind.i14.setAnimationFromUrl(offAnim);
                bind.i14.playAnimation();
                break;
        }
    }

}