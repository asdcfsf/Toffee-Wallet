package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.ClickAction.response;
import static com.Toffee.Wallet.utils.ClickAction.updateGlobalStatus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefListResp;
import com.Toffee.Wallet.databinding.ActivityMainBinding;
import com.Toffee.Wallet.databinding.LayoutGlobalMsgBinding;
import com.Toffee.Wallet.fragment.Home;
import com.Toffee.Wallet.fragment.Invite;
import com.Toffee.Wallet.fragment.MissionFragment;
import com.Toffee.Wallet.fragment.Profile;
import com.Toffee.Wallet.fragment.RewardCatFragment;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding bind;
    boolean doubleBackToExitPressedOnce = false;
    AlertDialog globalDialog;
    LayoutGlobalMsgBinding globalMsgBinding;
    Pref pref;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fun.statusbar(this);
        super.onCreate(savedInstanceState);
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        pref=new Pref(this);

        globalMsgBinding= LayoutGlobalMsgBinding.inflate(getLayoutInflater());
        globalDialog=Fun.GlobalMsg(this,globalMsgBinding);
        initAct();


        loadFragment(new Home());
        bind.appbar.navigation.setItemSelected(R.id.navigation_home, true);
        bind.appbar.navigation.setOnItemSelectedListener(i -> {
                switch (i){
                    case R.id.navigation_home:
                        loadFragment(new Home());
                        break;
                    case R.id.navigation_mission:
                        loadFragment(new MissionFragment());
                        break;

                    case R.id.navigation_invite:
                        loadFragment(new Invite());
                        break;

                    case R.id.navigation_reward:
                        loadFragment(new RewardCatFragment());
                        break;

                    case R.id.navigation_profile:
                        loadFragment(new Profile());
                        break;
                }
            });
    }

    private void initAct() {

        try {
            Fun.loadNative(this);
            checkUser(this);
        }catch (Exception e){}
    }

    private void checkUser(Activity activity) {
        try {
            Objects.requireNonNull(ApiClient.restAdapter(activity)).create(ApiInterface.class).getGlobalMsg(Pref.User_id(activity)).enqueue(new Callback<List<DefListResp>>() {
                @Override
                public void onResponse(Call<List<DefListResp>> call, Response<List<DefListResp>> response) {
                    if(response.isSuccessful() && response.body().size()!=0){
                        DefListResp resp=response.body().get(0);
                        globalMsgBinding.title.setText(resp.getTitle());
                        globalMsgBinding.msg.setText(resp.getMsg());
                        globalMsgBinding.positive.setText(resp.getBtn_name());

                        if(!resp.getBtn_action().equals("close")){
                            globalMsgBinding.positive.setVisibility(View.VISIBLE);
                        }
                        if(resp.getImage()!=null){
                            globalMsgBinding.anim.setVisibility(View.GONE);
                            globalMsgBinding.cvImage.setVisibility(View.GONE);
                            Glide.with(activity).load(WebApi.Api.IMAGES+resp.getImage()).into(globalMsgBinding.image);
                        }else{
                            if(resp.isError()){
                                globalMsgBinding.title.setTextColor(getResources().getColor(R.color.red));
                                globalMsgBinding.anim.setImageAssetsFolder("raw/");
                                globalMsgBinding.anim.setAnimation(R.raw.notice);
                                globalMsgBinding.anim.setSpeed(1f);
                                globalMsgBinding.anim.playAnimation();
                            }else {
                                globalMsgBinding.title.setTextColor(getResources().getColor(R.color.green));
                                globalMsgBinding.anim.setImageAssetsFolder("raw/");
                                globalMsgBinding.anim.setAnimation(R.raw.success);
                                globalMsgBinding.anim.setSpeed(1f);
                                globalMsgBinding.anim.playAnimation();
                            }
                        }

                        globalMsgBinding.negative.setOnClickListener(v -> {
                            globalDialog.dismiss();
                            if(resp.getUser_id()>0){
                                updateGlobalStatus(activity,resp.getId());
                            }
                        });

                        globalMsgBinding.positive.setOnClickListener(v -> {
                            globalDialog.dismiss();
                            response(resp.getBtn_action(),resp.getUrl(),activity);
                            if(resp.getUser_id()>0){
                                updateGlobalStatus(activity,resp.getId());
                            }
                        });

                        if(!activity.isFinishing()) {
                            globalDialog.show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<DefListResp>> call, Throwable t) {

                }
            });
        }catch (Exception e){}
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }


}
