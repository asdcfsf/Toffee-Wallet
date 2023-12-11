package com.Toffee.Wallet.offerwall;

import static com.Toffee.Wallet.utils.Constant.TestMode;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.pollfish.Pollfish;
import com.pollfish.builder.Params;
import com.pollfish.callback.PollfishClosedListener;
import com.pollfish.callback.PollfishOpenedListener;
import com.pollfish.callback.PollfishSurveyNotAvailableListener;
import com.pollfish.callback.PollfishSurveyReceivedListener;
import com.pollfish.callback.SurveyInfo;

public class O_Pollfish extends Activity implements PollfishOpenedListener,
        PollfishClosedListener,
        PollfishSurveyNotAvailableListener,
        PollfishSurveyReceivedListener {
    Activity activity;
    public String TAG= "Pollfish-offer";
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        activity= O_Pollfish.this;

        dialog= Fun.loadingProgress(activity);

        Bundle data=getIntent().getExtras();
        String userId=Pref.User_id(activity);

        Params params = new Params.Builder(data.getString("apikey"))
                .requestUUID(userId)
                .clickId(userId)
                .rewardMode(true)
                .offerwallMode(true)
                .releaseMode(data.getBoolean(TestMode))
                .build();

        Pollfish.initWith(this, params);
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
        super.onDestroy();
    }

    @Override
    public void onPollfishClosed() {
        if(dialog.isShowing()){dialog.dismiss();}
        finish();
    }

    @Override
    public void onPollfishOpened() {
        if(dialog.isShowing()){dialog.dismiss();}
    }

    @Override
    public void onPollfishSurveyNotAvailable() {
        uiToast(getString(R.string.survey_not_available));
        if(dialog.isShowing()){dialog.dismiss();}
        finish();
    }

    @Override
    public void onPollfishSurveyReceived(@Nullable SurveyInfo surveyInfo) {
        if(dialog.isShowing()){dialog.dismiss();}
        Pollfish.show();
    }

    private void uiToast(final String toast) {
        runOnUiThread(() -> Toast.makeText(this, toast, Toast.LENGTH_LONG).show());
    }
}


