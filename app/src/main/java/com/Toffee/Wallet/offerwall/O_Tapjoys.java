package com.Toffee.Wallet.offerwall;

import static com.Toffee.Wallet.utils.Constant.Sdk_key;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.databinding.LayoutProgressLoadingBinding;
import com.Toffee.Wallet.utils.Constant;
import com.Toffee.Wallet.utils.Fun;
import com.tapjoy.TJActionRequest;
import com.tapjoy.TJConnectListener;
import com.tapjoy.TJError;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.Tapjoy;
import com.tapjoy.TapjoyConnectFlag;

import java.util.Hashtable;

public class O_Tapjoys extends Activity implements TJPlacementListener {
    Activity activity;
    public String TAG= "Tapjoy-offer";
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        activity= O_Tapjoys.this;
        dialog=Fun.loadingProgress(activity);

        Bundle bundle1= getIntent().getExtras();

        Hashtable<String, Object> connectFlags = new Hashtable<String, Object>();
        connectFlags.put(TapjoyConnectFlag.ENABLE_LOGGING,bundle1.getBoolean(Constant.TestMode)); // Disable this in production builds
        connectFlags.put(TapjoyConnectFlag.USER_ID,Constant.User_ID);
        Tapjoy.connect(getApplicationContext(), bundle1.getString(Sdk_key), connectFlags, new TJConnectListener() {
            @Override
            public void onConnectSuccess() {
                Fun.log(TAG+ " onConnectSuccess: " );
            }

            @Override
            public void onConnectFailure() {
                Fun.log(TAG+ " onConnectFailure: " );
            }
        });

        TJPlacementListener placementListener = this;
        TJPlacement p = Tapjoy.getPlacement(bundle1.getString("placement"), placementListener);
        Tapjoy.setDebugEnabled(true);

        if(Tapjoy.isConnected()) {
            p.requestContent();
        } else {
            Fun.log(TAG+ " Tapjoy SDK must finish connecting before requesting content.: " );
        }

        if(p.isContentReady()) {
            if(dialog.isShowing()){dialog.dismiss();}
             p.showContent();
        }
        else {
            p.requestContent();
            if(dialog.isShowing()){dialog.dismiss();}
            Toast.makeText(activity, getString(R.string.no_offer_available), Toast.LENGTH_SHORT).show();
            finish();
        }

        Fun.log(TAG+ " check__: "+p.isContentAvailable()  );
    }


    @Override
    protected void onStart() {
        Tapjoy.onActivityStart(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        Tapjoy.onActivityStop(this);
        if(dialog.isShowing()){dialog.dismiss();}
        finish();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(dialog.isShowing()){dialog.dismiss();}
        finish();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Tapjoy.endSession();
        if(dialog.isShowing()){dialog.dismiss();}
        finish();
        super.onBackPressed();
    }

    @Override
    public void onRequestSuccess(TJPlacement tjPlacement) {
        tjPlacement.showContent();
    }

    @Override
    public void onRequestFailure(TJPlacement tjPlacement, TJError tjError) {

    }

    @Override
    public void onContentReady(TJPlacement tjPlacement) {

    }

    @Override
    public void onContentShow(TJPlacement tjPlacement) {
        tjPlacement.showContent();
    }

    @Override
    public void onContentDismiss(TJPlacement tjPlacement) {

    }

    @Override
    public void onPurchaseRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s) {

    }

    @Override
    public void onRewardRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s, int i) {

    }

    @Override
    public void onClick(TJPlacement tjPlacement) {

    }
}


