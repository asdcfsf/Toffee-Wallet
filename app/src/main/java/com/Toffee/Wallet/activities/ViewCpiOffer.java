package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.adapters.AppsAdapter.AppPosi;
import static com.Toffee.Wallet.adapters.MainRecyclerAdapter.appadapter;
import static com.Toffee.Wallet.adapters.MainRecyclerAdapter.appsResps;
import static com.Toffee.Wallet.utils.Fun.js;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.Toast;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.AppsResp;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.adapters.MainRecyclerAdapter;
import com.Toffee.Wallet.databinding.ActivityViewCpiOfferBinding;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCpiOffer extends AppCompatActivity {
    ActivityViewCpiOfferBinding bind;
    Activity activity;
    Pref pref;
    Bundle bundle;
    String id, appId, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityViewCpiOfferBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());

        activity = this;
        pref = new Pref(activity);
        bundle = getIntent().getExtras();

        if (pref.getString("gaid").isEmpty()) {
            AsyncTask.execute(() -> {
                try {
                    AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(this);
                    String gaid = adInfo.getId();
                    pref.setData("gaid", gaid);
                } catch (Exception ignored) {
                }
            });
        }

        id = bundle.getString("id");
        appId = bundle.getString("appid");
        url = bundle.getString("url");
        bind.tvTitle.setText(bundle.getString("title"));
        bind.coins.setText(bundle.getString("coin"));
        bind.desc.setText(Html.fromHtml(bundle.getString("detail")));

        Picasso.get().load(bundle.getString("image")).error(R.drawable.placeholder).fit().into(bind.images);

        bind.startoffer.setOnClickListener(view -> {
            try {
                if (appId.equals("0")) {
                    appId = "&subid2=" + id + "&";
                } else if (appId.equals("1")) {
                    appId = "&offerid=" + id + "&";
                } else if (appId.equals("2")) {
                    appId = "&appid=" + id + "&";
                } else if (appId.equals("3")) {
                    if(bundle.getString("offer_type").equals("offer_toro")){
                        appId = "";
                    }else {
                        appId = "&offer_id=" + id + "&";
                    }
                }
                String add;
                if(bundle.getString("offer_type").equals("offer_toro")){
                    add = url.replace("[USER_ID]",pref.User_id()) + "&"  + appId + "gaid=" + pref.getString("gaid");
                }else {
                    add = url + "&" + bundle.getString("P_userid") + pref.User_id() + appId + "gaid=" + pref.getString("gaid");
                }
                Fun.log("clicked_url__"+add);
                Fun.log("clicked_id"+id);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(add)));
                moveTaskPending();
            } catch (Exception e) {
                Toast.makeText(activity, "Url Broken", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void moveTaskPending() {
        new Handler().postDelayed(() -> move(), 3000);
    }

    private void move() {
        try {
            ApiClient.restAdapter(activity).create(ApiInterface.class).api(js(activity,"moveCpi", pref.User_id(), "", "", id)).enqueue(new Callback<DefResp>() {
                @Override
                public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                    if (response.isSuccessful() && response.body().getCode().equals("201")) {
                        appsResps.remove(AppPosi);
                        appadapter.notifyDataSetChanged();
                        if (appsResps.size() < 3) {
                            loadNewData();
                        }
                        Fun.log("CPI OFFER :" + "onResponse: " + response.body().toString());
                    } else {
                        Fun.log("CPI OFFER :" + "onResponse: " + response.body().toString());
                    }
                }

                @Override
                public void onFailure(Call<DefResp> call, Throwable t) {
                    Fun.log("CPI OFFER :" + "onFailure: " + t.getMessage());
                }
            });
        } catch (Exception e) {

        }
    }

    private void loadNewData() {
        try {
            Call<List<AppsResp>> call = Objects.requireNonNull(ApiClient.restAdapter(activity)).create(ApiInterface.class).cpaoffers();
            call.enqueue(new Callback<List<AppsResp>>() {
                @Override
                public void onResponse(Call<List<AppsResp>> call, Response<List<AppsResp>> response) {
                    if (response.isSuccessful() && response.body().size() != 0) {
                        appsResps.clear();
                        appsResps.addAll(response.body());
                        MainRecyclerAdapter.appadapter.notifyDataSetChanged();
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<List<AppsResp>> call, Throwable t) {
                }
            });
        } catch (Exception e) {
        }
    }
}