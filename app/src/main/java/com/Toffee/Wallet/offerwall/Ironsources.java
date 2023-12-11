package com.Toffee.Wallet.offerwall;

import static com.Toffee.Wallet.utils.Constant.TestMode;
import static com.Toffee.Wallet.utils.Constant.User_ID;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.databinding.LayoutProgressLoadingBinding;
import com.Toffee.Wallet.utils.Fun;
import com.ironsource.adapters.supersonicads.SupersonicConfig;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.OfferwallListener;

public class Ironsources extends Activity {
    Activity activity;
    public String TAG= "ironsources";
    AlertDialog loading;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        activity= Ironsources.this;

        loading=Fun.loadingProgress(activity);
        loading.show();

        Bundle data=getIntent().getExtras();
        /**
         *Ad Units should be in the type of IronSource.Ad_Unit.AdUnitName, example
         */
        SupersonicConfig.getConfigObj().setClientSideCallbacks(false);
        IronSource.init(this, data.getString("appid"), IronSource.AD_UNIT.OFFERWALL);
        IronSource.setUserId(User_ID);
        IronSource.setAdaptersDebug(data.getBoolean(TestMode));

        IronSource.setOfferwallListener(new OfferwallListener() {
            @Override
            public void onOfferwallAvailable(boolean isAvailable) {
                if(IronSource.isOfferwallAvailable()){
                    if(loading.isShowing()){loading.dismiss();}
                    IronSource.showOfferwall(data.getString("placement"));
                }
                else
                {
                    Fun.log("IronSource : on Offerwall Available: No Offerwall Available ");
                    Fun.msgError(activity,getString(R.string.no_offer_available));
                    IronSource.removeOfferwallListener();
                    if(loading.isShowing()){loading.dismiss();}
                    finish();
                }

            }

            @Override
            public void onOfferwallOpened() {
            }

            @Override
            public void onOfferwallShowFailed(IronSourceError error) {
                Fun.msgError(activity,getString(R.string.no_offer_available));
                if(loading.isShowing()){loading.dismiss();}
                finish();
            }

            @Override
            public boolean onOfferwallAdCredited(int credits, int totalCredits, boolean totalCreditsFlag) {
                return true;
            }

            @Override
            public void onGetOfferwallCreditsFailed(IronSourceError error) {
                Toast.makeText(activity, ""+error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOfferwallClosed() {
                IronSource.getOfferwallCredits();
                if(loading.isShowing()){loading.dismiss();}
                finish();
            }
        });


    }
    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }
    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        IronSource.removeOfferwallListener();
        if(loading.isShowing()){loading.dismiss();}
        finish();
        super.onDestroy();
    }
}


