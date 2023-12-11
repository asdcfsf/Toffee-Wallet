package com.Toffee.Wallet.offerwall;

import static com.Toffee.Wallet.utils.Constant.*;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import com.adgatemedia.sdk.classes.AdGateMedia;
import com.adgatemedia.sdk.model.Conversion;
import com.adgatemedia.sdk.network.OnConversionsReceived;
import com.adgatemedia.sdk.network.OnOfferWallLoadFailed;
import com.Toffee.Wallet.utils.Fun;

import java.util.HashMap;
import java.util.List;

public class O_AdGateMedia extends Activity {
    Activity activity;
    public String TAG= "AdGateMedia-offer";
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        activity= this;

        dialog= Fun.loadingProgress(activity);
        dialog.show();

        Bundle data=getIntent().getExtras();

        final HashMap<String, String> subids = new HashMap<String, String>();
        subids.put("s2",data.getString(User_ID));
        AdGateMedia adGateMedia = AdGateMedia.getInstance();
        AdGateMedia.getInstance().setDebugMode(data.getBoolean(TestMode));
        adGateMedia.loadOfferWall(activity,
                data.getString("appid"),
                data.getString(User_ID),
                subids,
                () -> AdGateMedia.getInstance().showOfferWall(activity,
                        new AdGateMedia.OnOfferWallClosed() {
                            @Override
                            public void onOfferWallClosed() {
                                if(dialog.isShowing()){dialog.dismiss();}
                                finish();

                            }
                        }),
                new OnOfferWallLoadFailed() {
                    @Override
                    public void onOfferWallLoadFailed(String reason) {
                        Fun.msgError(activity,reason);
                        if(dialog.isShowing()){dialog.dismiss();}
                        finish();
                    }
                });

        adGateMedia.getConversions(data.getString("appid"),data.getString(User_ID), subids, new OnConversionsReceived() {
            @Override
            public void onSuccess(List<Conversion> conversions) {
                for (Conversion conversion : conversions)  {

                }
            }

            @Override
            public void onError(String message) {
                // Fired when any error occurs
            }
        });


    }

    @Override
    protected void onResume() {

        super.onResume();
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
}


