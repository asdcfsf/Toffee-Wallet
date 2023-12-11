package com.Toffee.Wallet;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.Toffee.Wallet.utils.NotificationHandler;
import com.onesignal.OneSignal;

import timber.log.Timber;

public class App extends Application  {
    private static App instance;
    public static boolean isDebugMode =false;


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        registerActivityLifecycleCallbacks(new AppLifecycleTracker());
        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }

        OneSignal.initWithContext(this);
        OneSignal.setAppId(getString(R.string.ONE_SIGNAL_APPID));
        OneSignal.setNotificationOpenedHandler(new NotificationHandler(this));
    }

    public static App getInstance ()
    {
        return instance;
    }

    public static boolean hasNetwork ()
    {
        return instance.checkIfHasNetwork();
    }

    public boolean checkIfHasNetwork()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }



}
