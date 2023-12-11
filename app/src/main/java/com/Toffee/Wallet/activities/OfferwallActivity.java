package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.Constant.*;
import static com.Toffee.Wallet.utils.Constant.AdgateMedia;
import static com.Toffee.Wallet.utils.Constant.Adjoe;
import static com.Toffee.Wallet.utils.Constant.ApiKey;
import static com.Toffee.Wallet.utils.Constant.AppID;
import static com.Toffee.Wallet.utils.Constant.Bitlabs;
import static com.Toffee.Wallet.utils.Constant.Fyber;
import static com.Toffee.Wallet.utils.Constant.Inbrainai;
import static com.Toffee.Wallet.utils.Constant.IronSource;
import static com.Toffee.Wallet.utils.Constant.Monlix;
import static com.Toffee.Wallet.utils.Constant.OfferToro;
import static com.Toffee.Wallet.utils.Constant.Offerwall_url;
import static com.Toffee.Wallet.utils.Constant.Placement;
import static com.Toffee.Wallet.utils.Constant.Pollfish;
import static com.Toffee.Wallet.utils.Constant.SDK;
import static com.Toffee.Wallet.utils.Constant.Sdk_key;
import static com.Toffee.Wallet.utils.Constant.SecureKey;
import static com.Toffee.Wallet.utils.Constant.TapJoy;
import static com.Toffee.Wallet.utils.Constant.TestMode;
import static com.Toffee.Wallet.utils.Constant.User_ID;
import static com.Toffee.Wallet.utils.Constant.Value;
import static com.Toffee.Wallet.utils.Constant.WEB;
import static com.Toffee.Wallet.utils.Fun.launchCustomTabs;
import static com.Toffee.Wallet.utils.Fun.msgError;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.OfferwallResp;
import com.Toffee.Wallet.adapters.OfferwallAdapter;
import com.Toffee.Wallet.databinding.ActivityOfferwallBinding;
import com.Toffee.Wallet.fragment.FaqBottomDialogFragment;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.offerwall.Ironsources;
import com.Toffee.Wallet.offerwall.O_AdGateMedia;
import com.Toffee.Wallet.offerwall.O_AdjoeOfr;
import com.Toffee.Wallet.offerwall.O_Bitlab;
import com.Toffee.Wallet.offerwall.O_Offertoro;
import com.Toffee.Wallet.offerwall.O_Pollfish;
import com.Toffee.Wallet.offerwall.O_Tapjoys;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.fyber.ads.AdFormat;
import com.fyber.requesters.OfferWallRequester;
import com.fyber.requesters.RequestCallback;
import com.fyber.requesters.RequestError;
import com.monlixv2.MonlixOffers;
import com.wannads.sdk.WanGender;
import com.wannads.sdk.WannadsSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import theoremreach.com.theoremreach.TheoremReach;
import theoremreach.com.theoremreach.TheoremReachRewardListener;

public class OfferwallActivity extends AppCompatActivity implements OnItemClickListener, TheoremReachRewardListener {
    ActivityOfferwallBinding bind;
    Activity activity;
    Pref pref;
    List<OfferwallResp.DataItem> list;
    OfferwallAdapter adapter;
    AlertDialog loading;
    Intent fyberOfferWallIntent;
    boolean theoremReach=false;
    //private CPXResearch cpxResearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivityOfferwallBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());

        bind.tool.title.setText(Const.TOOLBAR_TITLE);

        activity=OfferwallActivity.this;
        pref=new Pref(activity);
        loading=Fun.loadingProgress(activity);

        list=new ArrayList<>();
        bind.rv.setLayoutManager(new GridLayoutManager(activity,2));
        adapter=new OfferwallAdapter(activity,list);
        adapter.setClickListener(this);
        bind.rv.setAdapter(adapter);

        if(getIntent().getStringExtra("type")!=null){
            getdata(getIntent().getStringExtra("type"));
        }else {
            getdata("all");
        }

        bind.tool.faq.setOnClickListener(v -> {
            Const.FAQ_TYPE="offerwall";
            FaqBottomDialogFragment fragment =  FaqBottomDialogFragment.newInstance();
            fragment.show(getSupportFragmentManager(),FaqBottomDialogFragment.TAG);
        });


        bind.tool.back.setOnClickListener(v -> {
            onBackPressed();
        });


    }


    private void getdata(String type){
        Call<OfferwallResp> call = ApiClient.restAdapter(activity).create(ApiInterface.class).Fetch_offerwalls(type);
        call.enqueue(new Callback<OfferwallResp>() {
            @Override
            public void onResponse(Call<OfferwallResp> call, Response<OfferwallResp> response) {
                if(response.isSuccessful() && response.body().getData().size()!=0){
                    bindData(response);
                }else{
                    noResult();
                }
            }

            @Override
            public void onFailure(Call<OfferwallResp> call, Throwable t) {
                noResult();
            }
        });
    }

    private void bindData(Response<OfferwallResp> response) {
        list.addAll(response.body().getData());
        bind.shimmerView.setVisibility(View.GONE);
        bind.rv.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    private void noResult() {
        bind.shimmerView.setVisibility(View.GONE);
        bind.layoutNoResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view, int position) {
        theoremReach=false;
        OfferwallResp.DataItem resp= list.get(position);
        try {
            if(resp.getType().equals(SDK)){
                JSONArray jsonArray = new JSONArray(resp.getData());
                if(resp.getOffer_slug().equals(TapJoy)){
                    Intent intent=new Intent(activity, O_Tapjoys.class);
                    intent.putExtra(Sdk_key,jsonArray.getJSONObject(0).getString(Value));
                    intent.putExtra(Placement,jsonArray.getJSONObject(1).getString(Value));
                    intent.putExtra(User_ID,pref.User_id());
                    intent.putExtra(TestMode,resp.isTest_mode());
                    startActivity(intent);
                }
                else if(resp.getOffer_slug().equals(IronSource)){
                    Intent intent=new Intent(activity, Ironsources.class);
                    intent.putExtra(AppID,jsonArray.getJSONObject(0).getString(Value));
                    intent.putExtra(Placement,jsonArray.getJSONObject(1).getString(Value));
                    intent.putExtra(User_ID,pref.User_id());
                    intent.putExtra(TestMode,resp.isTest_mode());
                    startActivity(intent);
                }
                else if(resp.getOffer_slug().equals(Bitlabs)){
                    Intent intent=new Intent(activity, O_Bitlab.class);
                    intent.putExtra(AppID,jsonArray.getJSONObject(0).getString(Value));
                    intent.putExtra(User_ID,pref.User_id());
                    intent.putExtra(TestMode,resp.isTest_mode());
                    startActivity(intent);
                }
                else if(resp.getOffer_slug().equals(Pollfish)){
                    Intent intent=new Intent(activity, O_Pollfish.class);
                    intent.putExtra(ApiKey,jsonArray.getJSONObject(0).getString(Value));
                    intent.putExtra(User_ID,pref.User_id());
                    intent.putExtra(TestMode,resp.isTest_mode());
                    startActivity(intent);
                }
                else if(resp.getOffer_slug().equals(OfferToro)){
                    Intent intent=new Intent(activity, O_Offertoro.class);
                    intent.putExtra(AppID,jsonArray.getJSONObject(0).getString(Value));
                    intent.putExtra(SecureKey,jsonArray.getJSONObject(1).getString(Value));
                    intent.putExtra(User_ID,pref.User_id());
                    intent.putExtra(TestMode,resp.isTest_mode());
                    startActivity(intent);
                }else if(resp.getOffer_slug().equals(Monlix)){
                    new MonlixOffers.Builder()
                            .setAppId(jsonArray.getJSONObject(0).getString(Value))
                            .setUserId(pref.User_id())
                            .build(activity);
                    MonlixOffers.INSTANCE.showWall(activity,jsonArray.getJSONObject(1).getString(Value));
                }else if(resp.getOffer_slug().equals(Fyber)){
                    loading.show();
                    loadFyber(jsonArray.getJSONObject(0).getString(Value),jsonArray.getJSONObject(1).getString(Value));
                }else if(resp.getOffer_slug().equals(Inbrainai)){
                    loading.show();
                    loadFyber(jsonArray.getJSONObject(0).getString(Value),jsonArray.getJSONObject(1).getString(Value));
                }else if(resp.getOffer_slug().equals(Adjoe)){
                    Intent intent=new Intent(activity, O_AdjoeOfr.class);
                    intent.putExtra(Placement,jsonArray.getJSONObject(0).getString(Value));
                    intent.putExtra(User_ID,pref.User_id());
                    intent.putExtra(TestMode,resp.isTest_mode());
                    startActivity(intent);

                }else if(resp.getOffer_slug().equals(AdgateMedia)){
                    Intent intent=new Intent(activity, O_AdGateMedia.class);
                    intent.putExtra(AppID,jsonArray.getJSONObject(0).getString(Value));
                    intent.putExtra(User_ID,pref.User_id());
                    intent.putExtra(TestMode,resp.isTest_mode());
                    startActivity(intent);
                }else if(resp.getOffer_slug().equals(TheoremReachOffer)){
                    theoremReach=true;
                    TheoremReach.initWithApiKeyAndUserIdAndActivityContext(jsonArray.getJSONObject(0).getString(Value),pref.User_id(),activity);
                    TheoremReach.getInstance().setTheoremReachRewardListener(this);
                    TheoremReach.getInstance().showRewardCenter();
                }else if(resp.getOffer_slug().equals(Wannads)){
                    //app id , app token
                    launchWanaAds(jsonArray.getJSONObject(0).getString(Value),jsonArray.getJSONObject(1).getString(Value));
                }
            }
            else if(resp.getType().equals(WEB)){
                JSONObject obj= new JSONObject(resp.getData().replace("[","").replace("]",""));
                String link=obj.getString(Offerwall_url);
                String replace="";
                String rep="";
                try {
                    String ext_user_id=list.get(position).getU_tag();
                    String[] arrOfStr = ext_user_id.split("=", 2);
                    replace=list.get(position).getU_tag();
                    rep=arrOfStr[0]+"="+ Pref.User_id(activity);
                    link=link.replace(replace,rep);
                    launchCustomTabs(activity,link);
                }catch (Exception e){
                    msgError(activity, "Url Broken");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadFyber(String appid,String token) {
        RequestCallback requestCallback = new RequestCallback() {
            @Override
            public void onAdAvailable(Intent intent) {
                Fun.log("Fyber_offer  "+ "Offers are available");
            }
            @Override
            public void onAdNotAvailable(AdFormat adFormat) {
                Fun.log("Fyber_offer  "+ "No ad available");
                fyberOfferWallIntent = null;
            }
            @Override
            public void onRequestError(RequestError requestError) {
                Fun.log("Fyber_offer  "+"Something went wrong with the request: " + requestError.getDescription());
                fyberOfferWallIntent= null;
            }
        };
        com.fyber.Fyber.with(appid, activity)
                .withUserId(pref.User_id())
                .withSecurityToken(token)
                .start();
        OfferWallRequester.create(requestCallback)
                .request(activity);
        new Handler().postDelayed(() -> {
            loading.dismiss();
            if(fyberOfferWallIntent!=null){
                startActivity(fyberOfferWallIntent);
            }else {
                Fun.msgError(activity, getString(R.string.no_offer_available));
            }
        },3000);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(theoremReach){
            TheoremReach.getInstance().onResume(activity);
        }
    }

    private void launchWanaAds(String appid, String placement) {
       WannadsSdk.getInstance().init(activity, appid, placement, pref.User_id());
        WannadsSdk.getInstance().setAge(20);
        WannadsSdk.getInstance().setGender(WanGender.MALE);
        WannadsSdk.getInstance().setPrimaryColor(R.color.colorPrimaryDark);
        WannadsSdk.getInstance().setConfirmButtonColor(R.color.colorAccent);
        WannadsSdk.getInstance().showOfferWall();

    }

    @Override
    public void onPause() {
        super.onPause();
        if(theoremReach){
            TheoremReach.getInstance().onPause();
        }
    }

    @Override
    public void onReward(int quantity) {

    }
}