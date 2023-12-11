package com.Toffee.Wallet.utils;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.Toffee.Wallet.App;
import com.Toffee.Wallet.activities.Splash;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

public class NotificationHandler implements OneSignal.OSNotificationOpenedHandler {
    App activity;
    String link= null,desc = null,type,img,id;
    public NotificationHandler(App activity) {
        this.activity=activity;
    }

    @Override
    public void notificationOpened(OSNotificationOpenedResult result) {
        String  title = result.getNotification().getTitle();
        String  bigPicture = result.getNotification().getBigPicture();
        try {
            link = result.getNotification().getAdditionalData().getString("link");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(link==null){
                Intent intent = new Intent(activity, Splash.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }else{
                Log.e("oneSignal_noti", "notificationOpened: "+link );
                Intent intent = new Intent(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
            }
        }catch (Exception e){
            Log.e("oneSignal_noti_error", "notificationOpened: "+e.getMessage());
        }
    }
}
