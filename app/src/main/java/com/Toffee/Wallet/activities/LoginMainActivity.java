package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.Const.BU;
import static com.Toffee.Wallet.utils.Const.randomCode;
import static com.Toffee.Wallet.utils.Fun.d;
import static com.Toffee.Wallet.utils.Fun.generateRandomString;
import static com.Toffee.Wallet.utils.Fun.sec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.LoginResp;
import com.Toffee.Wallet.databinding.ActivityLoginMainBinding;
import com.Toffee.Wallet.databinding.LayoutAlertBinding;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginMainActivity extends AppCompatActivity {
    ActivityLoginMainBinding bind;
    Activity activity;
    GoogleSignInClient mGoogleSignInClient;
    LayoutAlertBinding lytAlert;
    AlertDialog loadingDialog, alert;
    private static final int RC_SIGN_IN = 100;
    boolean isPrivacyConfirm = false;
    String referlink;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bind = ActivityLoginMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        activity = this;
        OneSignal.promptForPushNotifications();

        pref=new Pref(activity);
        lytAlert = LayoutAlertBinding.inflate(getLayoutInflater());
        alert = Fun.Alerts(activity, lytAlert);
        loadingDialog = Fun.loading(activity);

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    // Get deep link from result (may be null if no link is found)
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                        Fun.log("deeplink__"+ "onCreate: "+pendingDynamicLinkData.toString() );
                        try {
                            referlink=deepLink.toString();
                            Const.REFER_CODE=deepLink.getQueryParameter("ref");

                        }catch (Exception e){}
                    }

                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Fun.log("firebase_dynamicLink "+ "getDynamicLink:onFailure"+ e);
                    }
                });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


        bind.loginGoogle.setOnClickListener(v -> {
            signIn();
        });

        bind.getStarted.setOnClickListener(v -> {
            startActivity(new Intent(activity,FrontLogin.class));
        });

        bind.terms.setOnClickListener(v -> {
            Fun.launchCustomTabs(activity,BU+"privacy-policy");
        });
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);
            LoginUserGoogle(acct.getDisplayName(), acct.getEmail(), String.valueOf(acct.getPhotoUrl()), acct.getId());

        } catch (ApiException e) {
            Fun.log("Login Activtiy signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void LoginUserGoogle(String displayName, String email, String photoUrl, String id) {
        OSDeviceState device = OneSignal.getDeviceState();
        showLoading();
        String data=d(activity,displayName,email,null,id,"google",device.getUserId());
        String secure=sec(activity,photoUrl,Const.checkAppStatus);

        if(data==null || secure==null){
            dismisLoading();
            Fun.msgError(activity,"Something went wrong 00");
        }

        Call<LoginResp> call = ApiClient.restAdapter(activity).create(ApiInterface.class).Signup(data,secure);
        call.enqueue(new Callback<LoginResp>() {
            @Override
            public void onResponse(Call<LoginResp> call, Response<LoginResp> response) {
                dismisLoading();
                try {
                    if (response.isSuccessful() && response.body().getCode().equals("201")) {
                        randomCode = generateRandomString(response.body().getResp(),response.body().getUser().getRefferalId());
                        pref.saveUserData(response.body(),"","google");
                        startActivity(new Intent(activity, MainActivity.class));
                    }else{
                        showAlert(response.body().getMessage());
                    }
                }catch (Exception e){

                }
            }
            @Override
            public void onFailure(Call<LoginResp> call, Throwable t) {
                dismisLoading();
            }
        });
    }

    void showAlert(String msg) {
        alert.show();
        lytAlert.negative.setText(getString(R.string.close));
        lytAlert.title.setText(getString(R.string.signup) + " " + getString(R.string.error));
        lytAlert.msg.setText(msg);
        lytAlert.negative.setOnClickListener(v -> {
            alert.dismiss();
        });
    }

    void showLoading(){
        loadingDialog.show();
    }

    void dismisLoading(){
        if(loadingDialog.isShowing())
            loadingDialog.dismiss();
    }

    @Override
    public void onBackPressed() {

    }
}