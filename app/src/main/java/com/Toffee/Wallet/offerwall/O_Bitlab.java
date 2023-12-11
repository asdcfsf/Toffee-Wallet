package com.Toffee.Wallet.offerwall;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;

import java.util.HashMap;
import java.util.Map;

import ai.bitlabs.sdk.BitLabs;


public class O_Bitlab extends Activity{
    private ProgressDialog dialog;
    Activity activity;
    public String TAG= "Bitlab-offer";


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        activity=this;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();

        String userID=Pref.User_id(activity);
        BitLabs.INSTANCE.init(getIntent().getStringExtra("appid"),userID);
        Map<String, Object> tags = new HashMap<>();
        tags.put("user_id",userID);
        BitLabs.INSTANCE.setTags(tags);
        BitLabs.INSTANCE.launchOfferWall(activity);
        BitLabs.INSTANCE.checkSurveys(hasSurveys -> {
            if(hasSurveys == null) {
                Fun.log(TAG+" onCreate: "+"Error in HTTP, Check BitLabs Logs" );
                return;
            }
            if ((hasSurveys)) {
                Fun.log(TAG + " Has Surveys");
            } else {
                Fun.log(TAG + " No Surveys Available");
            }
            if(dialog.isShowing()){dialog.dismiss();}
            finish();
        });

        BitLabs.INSTANCE.getSurveys(surveys -> {
            if (surveys == null) {
                uiToast(getString(R.string.survey_not_available));
                Fun.log(TAG + "NULL -  Check BitLabs Logs");
                if(dialog.isShowing()){dialog.dismiss();}
                finish();
            }else {
                Fun.log(TAG + "Surveys: " + surveys);
                surveys.get(0).open(this);
            }
        });
    }

    private void uiToast(final String toast) {
        runOnUiThread(() -> Toast.makeText(this, toast, Toast.LENGTH_LONG).show());
    }

    @Override
    public void onBackPressed() {
        if(dialog.isShowing()){dialog.dismiss();}
        finish();
        super.onBackPressed();
    }
}
