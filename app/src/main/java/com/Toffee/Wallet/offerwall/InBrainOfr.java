package com.Toffee.Wallet.offerwall;

import static com.Toffee.Wallet.utils.Constant.User_ID;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.utils.Fun;
import com.inbrain.sdk.InBrain;
import com.inbrain.sdk.callback.InBrainCallback;
import com.inbrain.sdk.callback.StartSurveysCallback;
import com.inbrain.sdk.config.StatusBarConfig;
import com.inbrain.sdk.config.ToolBarConfig;
import com.inbrain.sdk.model.InBrainSurveyReward;
import com.inbrain.sdk.model.Reward;

import java.util.List;

public class InBrainOfr extends Activity {
    Activity activity;
    public String TAG= "Inbrain-offer";
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        activity= InBrainOfr.this;

        dialog= Fun.loadingProgress(InBrainOfr.this);
        dialog.show();

        Bundle data=getIntent().getExtras();

        InBrain.getInstance().setInBrain(activity, data.getString("placement"),  data.getString("appid"), true,data.getString(User_ID));
        InBrain.getInstance().addCallback(new InBrainCallback() {
            @Override
            public void surveysClosed(boolean b, List<InBrainSurveyReward> list) {
                if(dialog.isShowing()){dialog.dismiss();}
                finish();
            }

            @Override
            public boolean didReceiveInBrainRewards(List<Reward> list) {
                return false;
            }
        });

        ToolBarConfig toolBarConfig = new ToolBarConfig();
        toolBarConfig.setTitle(getString(R.string.app_name)); // set title

        boolean useResourceId = false;
        if (useResourceId) {
            toolBarConfig.setToolbarColorResId(R.color.colorPrimaryDark) // set toolbar color with status bar
                    .setBackButtonColorResId(android.R.color.white) // set icon color
                    .setTitleColorResId(android.R.color.white); //  set toolbar text
        } else {
            toolBarConfig.setToolbarColor(getResources().getColor(R.color.colorPrimaryDark))
                    .setBackButtonColor(getResources().getColor(android.R.color.white))
                    .setTitleColor(getResources().getColor(android.R.color.white));
        }
        toolBarConfig.setElevationEnabled(false);
        InBrain.getInstance().setToolbarConfig(toolBarConfig);

        StatusBarConfig statusBarConfig = new StatusBarConfig();
        if (useResourceId) {
            statusBarConfig.setStatusBarColorResId(android.R.color.white)
                    .setStatusBarIconsLight(false);
        } else {
            statusBarConfig.setStatusBarColor(getResources().getColor(android.R.color.white))
                    .setStatusBarIconsLight(false);
        }
        InBrain.getInstance().setStatusBarConfig(statusBarConfig);


        InBrain.getInstance().showSurveys(activity, new StartSurveysCallback() {
            @Override
            public void onSuccess() {
                if(dialog.isShowing()){dialog.dismiss();}

                // successfully opened the survey wall
            }

            @Override
            public void onFail(String message) {
                if(dialog.isShowing()){dialog.dismiss();}
                finish();
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    //session end
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(dialog.isShowing()){dialog.dismiss();}
        finish();
        super.onDestroy();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

}


