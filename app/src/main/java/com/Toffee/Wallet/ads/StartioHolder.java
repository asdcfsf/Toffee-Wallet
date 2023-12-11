package com.Toffee.Wallet.ads;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.androidsdk.R;
import com.startapp.sdk.ads.nativead.NativeAdDetails;
import com.startapp.sdk.ads.nativead.NativeAdPreferences;
import com.startapp.sdk.ads.nativead.StartAppNativeAd;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import java.util.ArrayList;


public class StartioHolder extends RecyclerView.ViewHolder {
    private final String TAG = "StartIo_NATIVE";
    Context ctx;
    ImageView icon;
    TextView title;
    TextView description;
    Button button;
    private CardView cv;
    View view;
    public StartioHolder(View view, Context context,String colorprimary) {
        super(view);
        this.ctx=context;
        this.view=view;
        icon = view.findViewById(R.id.icon);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        button = view.findViewById(R.id.button);
        cv = view.findViewById(R.id.cv);
        button.setOnClickListener(v -> itemView.performClick());
        button.setBackgroundColor(Color.parseColor(colorprimary));
        loadNativeAd();
    }

    private void loadNativeAd() {
        final StartAppNativeAd nativeAd = new StartAppNativeAd(ctx);
        nativeAd.loadAd(new NativeAdPreferences()
                .setAdsNumber(1)
                .setAutoBitmapDownload(true)
                .setPrimaryImageSize(2), new AdEventListener() {
            @Override
            public void onReceiveAd(Ad arg0) {
                Log.v(TAG, "onReceiveAd_ST_: loaded"); // Native Ad Received
                ArrayList<NativeAdDetails> ads= nativeAd.getNativeAds();
                if (ads != null) {
                    icon.setImageBitmap(ads.get(0).getImageBitmap());
                    title.setText(ads.get(0).getTitle());
                    description.setText(ads.get(0).getDescription());
                    button.setText(ads.get(0).isApp() ? "Install" : "Open");
                    cv.setVisibility(View.VISIBLE);
                    ads.get(0).registerViewForInteraction(itemView);
                }
                Log.v(TAG, "onReceiveAd_ST_: "+ads.get(0).getTitle()); // Native Ad Received

            }

            @Override
            public void onFailedToReceiveAd(Ad arg0) {
                // Native Ad failed to receive
            }
        });
    }


}
