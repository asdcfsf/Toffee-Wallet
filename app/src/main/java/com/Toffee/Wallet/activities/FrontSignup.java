package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.Const.BU;
import static com.Toffee.Wallet.utils.Const.randomCode;
import static com.Toffee.Wallet.utils.Fun.d;
import static com.Toffee.Wallet.utils.Fun.generateRandomString;
import static com.Toffee.Wallet.utils.Fun.sec;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.Toffee.Wallet.Responsemodel.LoginResp;
import com.Toffee.Wallet.databinding.LayoutAlertBinding;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.Toffee.Wallet.databinding.ActivitySignupBinding;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FrontSignup extends AppCompatActivity {
    ActivitySignupBinding binding;
    Activity activity;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    AlertDialog loadingDialog, alert, privacyDialog;
    Pref pref;
    LayoutAlertBinding lytAlert;
    private static final int RC_SIGN_IN = 100;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        pref = new Pref(activity);

        loadingDialog = Fun.loading(activity);
        lytAlert = LayoutAlertBinding.inflate(getLayoutInflater());
        alert = Fun.Alerts(activity, lytAlert);

        binding.terms.setOnClickListener(v -> {
            Fun.launchCustomTabs(activity,BU+"privacy-policy");
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


        binding.loginGoogle.setOnClickListener(v -> {
            signIn();
        });

        binding.signin.setOnClickListener(v -> {
            Signup();
        });

        binding.tvSignup.setOnClickListener(view -> {
            login();
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

    void showLoading() {
        loadingDialog.show();
    }

    void dismisLoading() {
        if (loadingDialog.isShowing())
            loadingDialog.dismiss();
    }

    private boolean validateData() {
        boolean valid = true;

        if (TextUtils.isEmpty(binding.username.getText().toString().trim())) {
            binding.username.setError(getResources().getString(R.string.error_field_required));
            valid = false;
        }
        if (TextUtils.isEmpty(binding.email.getText().toString().trim())) {
            binding.email.setError(getResources().getString(R.string.error_field_required));
            valid = false;
        } else if (!binding.email.getText().toString().trim().matches(emailPattern)) {
            binding.email.setError(getResources().getString(R.string.error_invalid_email));
            valid = false;
        }

        if (TextUtils.isEmpty(binding.password.getText().toString().trim())) {
            binding.password.setError(getResources().getString(R.string.error_invalid_password));
            valid = false;
        }

        return valid;
    }

    public void Signup() {
        if (!Fun.isConnected(activity)) {
            Fun.msgError(activity, getString(R.string.no_internet));
        }
        if (!validateData()) {
            return;
        } else {
            reqSignup(binding.username.getText().toString().trim(), binding.email.getText().toString().trim(), binding.password.getText().toString().trim());
        }
    }

    private void LoginUserGoogle(String displayName, String email, String photoUrl, String id) {
        OSDeviceState device = OneSignal.getDeviceState();
        showLoading();
        String data = d(activity,displayName, email, null, id, "google", device.getUserId());
        String secure = sec(activity, photoUrl, Const.checkAppStatus);

        if (data == null || secure == null) {
            dismisLoading();
            Fun.msgError(activity, "Something went wrong 00");
        }

        Call<LoginResp> call = ApiClient.restAdapter(activity).create(ApiInterface.class).Login(data, secure);
        call.enqueue(new Callback<LoginResp>() {
            @Override
            public void onResponse(Call<LoginResp> call, Response<LoginResp> response) {
                dismisLoading();
                try {
                    if (response.isSuccessful() && response.body().getCode().equals("201")) {
                        randomCode = generateRandomString(response.body().getResp(), response.body().getUser().getRefferalId());
                        pref.saveUserData(response.body(), "", "google");
                        startActivity(new Intent(activity, MainActivity.class));
                    } else {
                        showAlert(response.body().getMessage(), false);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<LoginResp> call, Throwable t) {
                dismisLoading();
            }
        });
    }

    private void reqSignup(String name, String email, String password) {
        OSDeviceState device = OneSignal.getDeviceState();
        showLoading();
        Call<LoginResp> call = ApiClient.restAdapter(activity).create(ApiInterface.class).Signup(d(activity,name, email, password, null, "email", device.getUserId()), sec(activity, null, Const.checkAppStatus));
        call.enqueue(new Callback<LoginResp>() {
            @Override
            public void onResponse(Call<LoginResp> call, Response<LoginResp> response) {
                dismisLoading();
                try {
                    if (response.isSuccessful() && Objects.requireNonNull(response.body()).getCode().equals("201")) {
                        pref.setBoolean(pref.LOGIN,true);
                        pref.setData(pref.EMAIL,email);
                        pref.setData(pref.PASSWORD,password);
                        pref.setData(pref.ATYPE,"email");
                        showAlert(response.body().getMessage(), true);
                    } else {
                        assert response.body() != null;
                        showAlert(response.body().getMessage(), false);
                    }
                } catch (Exception e) {
                    System.out.println("login_froN_signup___ "+e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginResp> call, Throwable t) {
                dismisLoading();
            }
        });
    }

    public void login() {
        startActivity(new Intent(activity, FrontLogin.class));
        Animatoo.animateSlideLeft(activity);
    }

    void showAlert(String msg, boolean success) {
        alert.show();
        if (success) {
            lytAlert.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
            lytAlert.title.setText(getString(R.string.account_created));
            lytAlert.msg.setText(msg);
            lytAlert.negative.setVisibility(View.GONE);
            lytAlert.positive.setVisibility(View.VISIBLE);
            lytAlert.positive.setText(getString(R.string.login));
            lytAlert.positive.setOnClickListener(view -> {
                alert.dismiss();
                login();
            });
        } else {
            lytAlert.negative.setText(getString(R.string.close));
            lytAlert.title.setText(getString(R.string.signup) + " " + getString(R.string.error));
            lytAlert.msg.setText(msg);
            lytAlert.negative.setOnClickListener(v -> {
                alert.dismiss();
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateInAndOut(activity);
    }

}
