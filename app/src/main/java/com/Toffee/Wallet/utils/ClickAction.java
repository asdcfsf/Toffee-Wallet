package com.Toffee.Wallet.utils;

import static com.Toffee.Wallet.utils.Cnt.stopTimer;
import static com.Toffee.Wallet.utils.Fun.js;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.activities.ArticleActivity;
import com.Toffee.Wallet.activities.DailyOffer;
import com.Toffee.Wallet.activities.DailybonusActivity;
import com.Toffee.Wallet.activities.FaqActivity;
import com.Toffee.Wallet.activities.Games;
import com.Toffee.Wallet.activities.Leaderboard;
import com.Toffee.Wallet.activities.MathQuiz;
import com.Toffee.Wallet.activities.NotificationActivity;
import com.Toffee.Wallet.activities.OfferwallActivity;
import com.Toffee.Wallet.activities.Promote;
import com.Toffee.Wallet.activities.ScratchActivity;
import com.Toffee.Wallet.activities.SettingActivity;
import com.Toffee.Wallet.activities.SpinActivity;
import com.Toffee.Wallet.activities.StoreList;
import com.Toffee.Wallet.activities.SubscriptionActivity;
import com.Toffee.Wallet.activities.VideoActivity;
import com.Toffee.Wallet.activities.VideoWall;
import com.Toffee.Wallet.fragment.Invite;
import com.Toffee.Wallet.fragment.MissionFragment;
import com.Toffee.Wallet.fragment.RewardCatFragment;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClickAction implements Constant {
   public static void response(String type, String url, Context context) {
        switch (type) {
            case TYPE_DAILY_BONUS:
                context.startActivity(new Intent(context, DailybonusActivity.class));
                break;

            case TYPE_COINSTORE:
                context.startActivity(new Intent(context, StoreList.class));
                break;

            case TYPE_FAQ:
                context.startActivity(new Intent(context, FaqActivity.class));
                break;

            case TYPE_NOTIFICATION:
                context.startActivity(new Intent(context, NotificationActivity.class));
                break;

            case TYPE_PROMO:
                context.startActivity(new Intent(context, Promote.class));
                break;

            case TYPE_ARTICLE:
                context.startActivity(new Intent(context, ArticleActivity.class));
                break;

            case TYPE_DAILY_OFFER:
                context.startActivity(new Intent(context, DailyOffer.class));
                break;

            case TYPE_INVITE:
                loadFragment(new Invite(),context);
                break;

            case TYPE_DAILY_MISSION:
                loadFragment(new MissionFragment(),context);
                break;

            case TYPE_CPI:
                Toast.makeText(context, "CPI", Toast.LENGTH_SHORT).show();
                break;

            case TYPE_LEADERBOARD:
                context.startActivity(new Intent(context, Leaderboard.class));
                break;

            case TYPE_OFFERWALL:
                context.startActivity(new Intent(context, OfferwallActivity.class));
                break;

            case TYPE_OFFERWALL_SURVEY:
                context.startActivity(new Intent(context, OfferwallActivity.class).putExtra("type", "survey"));
                break;

            case TYPE_OFFERWALL_OFFERS:
                context.startActivity(new Intent(context, OfferwallActivity.class).putExtra("type", "offers"));
                break;

            case TYPE_OFFERWALL_PLAYZONE:
                context.startActivity(new Intent(context, OfferwallActivity.class).putExtra("type", "playzone"));
                break;

            case TYPE_QUIZ:
                stopTimer((Activity) context, "quiz");
                context.startActivity(new Intent(context, MathQuiz.class));
                break;

            case TYPE_REDEEM:
                loadFragment(new RewardCatFragment(),context);
                break;

            case TYPE_SCRATCH:
                stopTimer((Activity) context, "scratch");
                context.startActivity(new Intent(context, ScratchActivity.class));
                break;

            case TYPE_SETTING:
                context.startActivity(new Intent(context, SettingActivity.class));
                break;

            case TYPE_SPIN:
                stopTimer((Activity) context, "spin");
                context.startActivity(new Intent(context, SpinActivity.class));
                break;

            case TYPE_URL:
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (Exception e) {
                    Fun.msgError((Activity) context, "Url Broken");
                }
                break;

            case TYPE_URL_CUSTOM:
                try {
                    Fun.launchCustomTabs((Activity) context, url);
                } catch (Exception e) {
                    Fun.msgError((Activity) context, "Url Broken");
                }
                break;

            case TYPE_PLAYZONE:
                context.startActivity(new Intent(context, Games.class));
                break;

            case TYPE_VIDEOWALL:
                context.startActivity(new Intent(context, VideoWall.class));
                break;

            case TYPE_VIDEOZONE:
                context.startActivity(new Intent(context, VideoActivity.class));
                break;

            case TYPE_SUBSCRIPTION:
                context.startActivity(new Intent(context, SubscriptionActivity.class));
                break;
        }
    }
    private static void loadFragment(Fragment fragment, Context context) {
        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void updateGlobalStatus(Activity activity,String id){
        try {
            Objects.requireNonNull(ApiClient.restAdapter(activity)).create(ApiInterface.class).api(js(activity,"readGlobal","","","",id)).enqueue(new Callback<DefResp>() {
                @Override
                public void onResponse(@NonNull Call<DefResp> call, Response<DefResp> response) {
                    Fun.log("updateGlobalStatus_onResponse" +response.body().getMsg());
                }

                @Override
                public void onFailure(@NonNull Call<DefResp> call, Throwable t) {
                    Fun.log("updateGlobalStatus_onFailure" +t.getMessage());
                }
            });
        }catch (Exception e){}
    }

}
