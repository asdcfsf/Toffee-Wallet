package com.Toffee.Wallet.activities;

import static com.ads.androidsdk.sdk.util.Api.getBaseUrl;
import static com.Toffee.Wallet.activities.OnboardingActivity.list;
import static com.Toffee.Wallet.utils.Const.BU;
import static com.Toffee.Wallet.utils.Fun.checkApp;
import static com.Toffee.Wallet.utils.Fun.d;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAppOptions;
import com.ads.androidsdk.sdk.util.InputValidation;
import com.Toffee.Wallet.utils.AdsConfig;
import com.Toffee.Wallet.Responsemodel.LoginResp;
import com.Toffee.Wallet.Responsemodel.SettingResp;
import com.Toffee.Wallet.databinding.LayoutAlertBinding;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Const;

import static com.Toffee.Wallet.utils.Const.randomCode;
import static com.Toffee.Wallet.utils.Fun.encrypt_code;
import static com.Toffee.Wallet.utils.Fun.generateRandomString;
import static com.Toffee.Wallet.utils.Fun.log;
import static com.Toffee.Wallet.utils.Fun.msgError;
import static com.Toffee.Wallet.utils.Fun.sec;
import com.Toffee.Wallet.utils.DatabaseHandler;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.BuildConfig;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.utils.Pref;
import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinPrivacySettings;
import com.applovin.sdk.AppLovinSdk;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.chartboost.sdk.Chartboost;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;
import com.vungle.warren.InitCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.error.VungleException;
import org.json.JSONObject;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Splash extends AppCompatActivity implements AdsConfig {
    Pref pref;
    private AlertDialog dialog, no_connection, updateAlert;
    LayoutAlertBinding updateBind;
    DatabaseHandler db;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        BU =getBaseUrl(this,BuildConfig.API_URL);
        pref = new Pref(this);
        db=new DatabaseHandler(this);
        activity=this;

        Const.checkAppStatus=checkApp(getFilesDir().getPath());
        updatelang();

        updateBind = LayoutAlertBinding.inflate(getLayoutInflater());
        updateAlert = Fun.Alerts(this, updateBind);

        no_connection = new AlertDialog.Builder(this).setView(LayoutInflater.from(this).inflate(R.layout.layout_nointernet, null)).create();
        no_connection.getWindow().setBackgroundDrawableResource(R.color.transparent);
        no_connection.getWindow().setWindowAnimations(R.style.Dialoganimation);
        no_connection.setCanceledOnTouchOutside(false);
        no_connection.setCancelable(false);

        if (Fun.isConnected(this)) {
           loadSetting();
        } else {
            no_connection.show();
            no_connection.findViewById(R.id.btntry).setOnClickListener(v -> {
                no_connection.dismiss();
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            });

            no_connection.findViewById(R.id.btntry).setOnClickListener(v -> {
                if (Fun.isConnected(this)) {
                    no_connection.dismiss();
                    loadSetting();
                } else {
                    no_connection.dismiss();
                    no_connection.show();
                }
            });
        }
    }


    private void loadSetting() {
       try {
           Call<SettingResp> call = ApiClient.restAdapter(Splash.this).create(ApiInterface.class).getSetting(js());
           call.enqueue(new Callback<SettingResp>() {
               @Override
               public void onResponse(Call<SettingResp> call, Response<SettingResp> response) {
                   try {
                       if(response.isSuccessful() && response.body().getCode().equals("201")){
                           assert response.body() != null;
                           db.removeData();
                           db.insertSpin(response.body().getSpin());
                           db.insert(response.body().getHome(),response.body().getCat());
                           if(!pref.isUserLogin()){
                               list=response.body().getOnboarding();
                           }

                           parseData(response.body().getData().get(0));
                           pref.setBoolean(Pref.ENABLE_Banner, true);

                           if (response.body().getData().get(0).isUp_status()) {
                               if (response.body().getData().get(0).getUp_mode() == 0) {
                                   if (BuildConfig.VERSION_CODE < response.body().getData().get(0).getUp_version()) {
                                       show(response.body().getData().get(0).getUp_msg(), "update", response.body().getData().get(0).getUp_link(), response.body().getData().get(0).isUp_btn());
                                   } else {
                                       doTask();
                                   }
                               } else {
                                   show(response.body().getData().get(0).getUp_msg(), Const.MAINTENANCE, "", false);
                               }
                           } else {
                               doTask();
                           }
                       }else{
                           msgError(activity,"Server Error");
                       }
                   }catch (Exception e){
                       Fun.log("splash_setting "+e.getMessage());
                       msgError(activity,e.getMessage());
                   }
               }

               @Override
               public void onFailure(Call<SettingResp> call, Throwable t) {
                   msgError(activity,"Please Contact To ADMIN");
               }
           });
       }catch (Exception e){}
    }

    private void updatelang() {
        setLocale(pref.getString(Pref.SELECTED_LANGUAGE));
    }

    private void parseData(SettingResp.DataItem o) {
        try {
            JSONObject obj = new JSONObject(o.getInterstitalID().replace("[", "").replace("]", ""));
            pref.setData(auAdmob,obj.getString(auAdmob));
            pref.setData(auFb,obj.getString(auFb));
            pref.setData(auApplovin,obj.getString(auApplovin));
            pref.setData(auUnity,obj.getString(auUnity));
            pref.setData(auVungle,obj.getString(auVungle));
            pref.setData(auCharboost,obj.getString(auCharboost));
            pref.setData(vungleAppid,obj.getString(vungleAppid));
            pref.setData(chartboostAppid,obj.getString(chartboostAppid));
            pref.setData(chartboostSignature,obj.getString(chartboostSignature));

            pref.setData(unity_game,o.getUnity_game());
            pref.setData(startapp_appid,o.getStartapp_appid());

            pref.setData(interstital_adunit,o.getInterstital_adunit());
            pref.setData(adcolonyApp,o.getAdcolonyApp());
            pref.setData(adcolonyZone,o.getAdcolony_zone());
            pref.setData(nativeID,o.getNativeID());
            pref.setData(nativeType,o.getNativeType());
            pref.setIntData(native_count,o.getNative_count());
            pref.setData(bannerID,o.getBannerID());
            pref.setData(banner_type,o.getBanner_type());
            pref.setIntData(interstitalCount,o.getInterstital_count());
            pref.setData(interstitalType,o.getInterstital_type());
            pref.setData(homeMsg,o.getHomeMsg());
            pref.setData(referMsg,o.getRefer_msg());
            pref.setData(appAuthor,o.getApp_author());
            pref.setData(appDescription,o.getApp_description());

            pref.setBoolean(isApplovin,obj.getBoolean(isApplovin));
            pref.setBoolean(isAdcolony,obj.getBoolean(isAdcolony));
            pref.setBoolean(isAdmob,obj.getBoolean(isAdmob));
            pref.setBoolean(isStart,obj.getBoolean(isStart));
            pref.setBoolean(isUnity,obj.getBoolean(isUnity));
            pref.setBoolean(isChartboost,obj.getBoolean(isChartboost));
            pref.setBoolean(isVungle,obj.getBoolean(isVungle));
            pref.setBoolean(isFb,obj.getBoolean(isFb));

        } catch (Exception e) {
            msgError(this,"Please Contact To Administrator");
        }

        initAd();
    }

    private void initAd() {
        if(!getString(R.string.ADMOB_APPID).equals("ca-app-pub-3940256099942544~3347511713")){
            MobileAds.initialize(activity, initializationStatus -> {
                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {
                    AdapterStatus adapterStatus = statusMap.get(adapterClass);
                    assert adapterStatus != null;
                    log("Admob initialize "+String.format("Adapter name: %s, Description: %s, Latency: %d", adapterClass, adapterStatus.getDescription(), adapterStatus.getLatency()));
                }
            });
        }

        if(pref.getString(startapp_appid)!=null){
            StartAppSDK.init(activity,pref.getString(startapp_appid), false);
            StartAppAd.disableSplash();
            StartAppAd.disableAutoInterstitial();
        }

        if(getString(R.string.APPLOVIN_SDK_KEY)!=null){
            AppLovinPrivacySettings.setIsAgeRestrictedUser( false,activity);
            AppLovinPrivacySettings.setDoNotSell( true,activity);
            AppLovinSdk.getInstance(activity).setMediationProvider(AppLovinMediationProvider.MAX);
            AppLovinSdk.getInstance(activity).initializeSdk(config -> {
               Fun.log("initAd AppLovinSdk");
            });
        }

        if(pref.getString(unity_game)!=null){
            UnityAds.initialize(activity.getApplicationContext(),pref.getString(unity_game), false, new IUnityAdsInitializationListener() {
                @Override
                public void onInitializationComplete() {
                    Fun.log("Unity Ads Initialization Complete with ID : " +  pref.getString(unity_game));
                }
                @Override
                public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                    Fun.log("Unity Ads Initialization Failed: [" + error + "] " + message);
                }
            });
        }

        if(pref.getString(adcolonyApp)!=null){
            AdColonyAppOptions appOptions =  new AdColonyAppOptions().setGDPRConsentString("1")
                    .setKeepScreenOn(true).setGDPRRequired(false);
            AdColony.configure(activity, appOptions,pref.getString(adcolonyApp));
        }

        if(pref.getString(auFb)!=null){
            AudienceNetworkAds.initialize(activity);
        }

        if (pref.getString(vungleAppid)!=null) {
            Fun.log("Vungle_APP_ID "+pref.getString(vungleAppid));
            Vungle.init(pref.getString(vungleAppid),activity.getApplicationContext(), new InitCallback() {
                @Override
                public void onSuccess() {
                    Fun.log("Vungle SDK Init Successful "+pref.getString(vungleAppid));
                }

                @Override
                public void onError(VungleException exception) {
                    Fun.log("Vungle "+pref.getString(vungleAppid)+" SDK onError "+exception.toString());
                }

                @Override
                public void onAutoCacheAdAvailable(String pid) {
                }
            });
        }

        if(pref.getString(chartboostAppid)!=null && pref.getString(chartboostSignature)!=null){
            Chartboost.startWithAppId(getApplicationContext(),pref.getString(chartboostAppid),pref.getString(chartboostSignature), startError -> {
                if (startError == null) {
                    Fun.log("Chartboost SDK is initialized");
                } else {
                    Fun.log("Chartboost SDK initialized with error: "+startError.getCode().name());
                }
            });
        }
    }

    private void LoginUser(String email, String password, String type, String personid) {
        Call<LoginResp> call = ApiClient.restAdapter(this).create(ApiInterface.class).Login(d(activity,"", email, password, personid,type, ""),sec(activity,null,Const.checkAppStatus));
        call.enqueue(new Callback<LoginResp>() {
            @Override
            public void onResponse(Call<LoginResp> call, Response<LoginResp> response) {

                try {
                    if (response.isSuccessful() && response.body().getCode().equals("201")) {
                        pref.saveUserData(response.body(),password,type);
                        randomCode = generateRandomString(response.body().getResp(),response.body().getUser().getRefferalId());
                        finish();
                        startActivity(new Intent(Splash.this, MainActivity.class));
                    } else if(response.body().getCode().equals("401")){
                        Fun.alert2(activity,"",response.body().getMessage());
                    } else {
                        finish();
                        startActivity(new Intent(Splash.this, LoginMainActivity.class));
                        Animatoo.animateSlideUp(Splash.this);
                    }
                } catch (Exception e) {
                    Toast.makeText(Splash.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResp> call, Throwable t) {
            }
        });
    }

    private void doTask() {

        if (pref.isUserLogin()) {
            if (pref.getString(pref.ATYPE).equals("google")) {
                LoginUser(pref.getString(pref.EMAIL), pref.getString(pref.PASSWORD), pref.getString(pref.ATYPE),pref.getString(pref.PERSON_ID));
            } else {
                LoginUser(pref.getString(pref.EMAIL), pref.getString(pref.PASSWORD), pref.getString(pref.ATYPE),null);
            }
        } else {
            finish();
            startActivity(new Intent(Splash.this,OnboardingActivity.class));
        }
    }
    private void openUrl(String url) {
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } catch (Exception e) {
        }
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }


    void show(String msg, String type, String url, boolean skip) {
        updateAlert.show();
        updateBind.msg.setText(msg);
        if (type.equalsIgnoreCase("maintenance")) {
            updateBind.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_maintenance));
            updateBind.title.setText(getString(R.string.maintenance));
            updateBind.negative.setText(getString(R.string.close));
        } else {
            updateBind.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_update));
            updateBind.title.setText(getString(R.string.new_update));
            updateBind.positive.setText(getString(R.string.update));
            updateBind.positive.setVisibility(View.VISIBLE);
        }

        if (!skip) {
            updateBind.negative.setVisibility(View.GONE);
        }

        updateBind.positive.setOnClickListener(v -> {
            if (type.equals(Const.MAINTENANCE)) {
                updateAlert.dismiss();
            } else {
                openUrl(url);
            }
        });

        updateBind.negative.setOnClickListener(v -> {
             updateAlert.dismiss();
             doTask();
        });

    }

    public String js(){
        try {
            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new JsonObject());
            jsObj.addProperty("onboarding",pref.isUserLogin());
            return InputValidation.parse(activity,encrypt_code(jsObj.toString()));
        }catch (Exception e){
            return  null;
        }
    }

}