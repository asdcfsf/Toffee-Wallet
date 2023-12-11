package com.Toffee.Wallet;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class AppLifecycleTracker implements Application.ActivityLifecycleCallbacks{

    private int numStarted = 0;
    public static boolean GAMEAcT=false;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (numStarted == 0) {
        }
        numStarted++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
//        System.out.println("Actvitt_status=>"+"onActivityResumed");

    }

    @Override
    public void onActivityPaused(Activity activity) {
//        System.out.println("Actvitt_status=>"+"onActivityPaused");

    }

    @Override
    public void onActivityStopped(Activity activity) {
//        System.out.println("Actvitt_status=>"+"onActivityStopped");
        numStarted--;
        if (numStarted == 0) {
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//        System.out.println("Actvitt_status=>"+"onActivitySaveInstanceState");

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
//        System.out.println("Actvitt_status=>"+"onActivityDestroyed");
    }
}
