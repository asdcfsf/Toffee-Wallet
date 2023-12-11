package com.Toffee.Wallet.offerwall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.utils.Constant;
import com.Toffee.Wallet.utils.Fun;

import io.adjoe.sdk.Adjoe;
import io.adjoe.sdk.AdjoeException;
import io.adjoe.sdk.AdjoeInitialisationListener;
import io.adjoe.sdk.AdjoeNotInitializedException;
import io.adjoe.sdk.AdjoeOfferwallListener;

public class O_AdjoeOfr extends Activity {
    Activity activity;
    public String TAG= "Adjoe-offer";
    private AlertDialog dialog;
    Bundle data;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        activity= this;

        dialog= Fun.loadingProgress(activity);
        dialog.show();

        data=getIntent().getExtras();

        if (Adjoe.isAdjoeProcess()) {
            // the method is executed on the adjoe process
            return;
        }

        try {
            Intent adjoeOfferwallIntent = Adjoe.getOfferwallIntent(activity);
            activity.startActivity(adjoeOfferwallIntent);
        } catch(AdjoeNotInitializedException notInitializedException) {
            if(dialog.isShowing()){dialog.dismiss();}
            Fun.log(TAG+ "onCreate: "+notInitializedException.getMessage());
            // you have not initialized the adjoe SDK
        } catch(AdjoeException exception) {
            if(dialog.isShowing()){dialog.dismiss();}
            Fun.msgError(activity, getString(R.string.no_offer_available));
            // the offerwall cannot be displayed for some other reason
        }

        Adjoe.setOfferwallListener(new AdjoeOfferwallListener() {

            @Override
            public void onOfferwallOpened(String type) {
                if(dialog.isShowing()){dialog.dismiss();}
                Fun.log(TAG+"Offerwall of type '" + type + "' was opened");
            }

            @Override
            public void onOfferwallClosed(String type) {
                if(dialog.isShowing()){dialog.dismiss();}
                finish();
                Fun.log(TAG+"Offerwall of type '" + type + "' was closed");
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!Adjoe.isInitialized()) {
            Adjoe.Options options = new Adjoe.Options()
                    .setUserId(data.getString(Constant.User_ID));
            Adjoe.init(this, getIntent().getStringExtra("placement"), options, new AdjoeInitialisationListener() {

                @Override
                public void onInitialisationFinished() {
                    Fun.log(TAG+"onInitialisationFinished: ");
                }

                @Override
                public void onInitialisationError(Exception exception) {
                    Fun.log(TAG+"onInitialisationError: " +exception.getMessage());
                    // an error occurred while initializing the adjoe SDK.
                    // note that exception might be null
                }
            });
        }

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
}


