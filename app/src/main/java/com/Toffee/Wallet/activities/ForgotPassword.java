package com.Toffee.Wallet.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.databinding.LayoutAlertBinding;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.databinding.ActivityForgotPasswordBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {
    ActivityForgotPasswordBinding binding;
    Activity activity;
    private boolean emailVerified = true;
    private boolean otpVerified = false;
    private boolean passwordUpdate = false;
    AlertDialog loadingDialog, alert;
    LayoutAlertBinding lytAlert;
    String OTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(binding.getRoot());

        activity = ForgotPassword.this;
        loadingDialog = Fun.loading(activity);
        lytAlert=LayoutAlertBinding.inflate(getLayoutInflater());
        alert = Fun.Alerts(activity,lytAlert);
        initView();

        binding.procced.setOnClickListener(view -> {
            proceed();
        });

    }

    private void initView() {
        if (!emailVerified) {
            binding.layoutOtp.setVisibility(View.VISIBLE);
        }

        if (passwordUpdate) {
            binding.layoutOtp.setVisibility(View.GONE);
            binding.layoutEmail.setVisibility(View.GONE);
            binding.layoutPassword.setVisibility(View.VISIBLE);
        }
    }

    public void proceed() {
        if (Fun.isConnected(this)) {
            if (emailVerified) {
                funVerifyEmail(binding.email.getText().toString().trim());
            }

            if (otpVerified) {
                if (binding.otp.getText().toString().trim().length() == 4) {
                    funOtpVerify(binding.email.getText().toString().trim(), binding.otp.getText().toString().trim());
                } else {
                    Fun.msgError(activity, "Invalid Otp");
                }
            }
            if (passwordUpdate) {
                if (TextUtils.isEmpty(binding.newPassword.getText().toString().trim()) || TextUtils.isEmpty(binding.confirmPassword.getText().toString().trim())) {
                    showAlert(getString(R.string.error_field_required),false);
                } else if (binding.newPassword.getText().toString().trim().equals(binding.confirmPassword.getText().toString().trim())) {
                    funUpdatePassword(binding.email.getText().toString().trim(), binding.newPassword.getText().toString().trim());
                } else {
                    showAlert(getString(R.string.new_password_not_match),false);
                }
            }
        } else {
            Fun.msgError(activity, getString(R.string.no_internet));
        }
    }

    void showLoading() {
        loadingDialog.show();
    }

    void dismisLoading() {
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    private void funOtpVerify(String email, String otp) {
        showLoading();
        Call<DefResp> call = ApiClient.restAdapter(activity).create(ApiInterface.class).Verify_OTP(otp, email);
        call.enqueue(new Callback<DefResp>() {
            @Override
            public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                dismisLoading();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getCode().equals("201")) {
                        OTP = otp;
                        passwordUpdate = true;
                        binding.procced.setText(getString(R.string.update_password));
                        initView();
                    } else {
                        binding.otp.setText(null);
                        Fun.msgError(activity, response.body().getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<DefResp> call, Throwable t) {
                dismisLoading();
            }
        });
    }


    private void funUpdatePassword(String email, String password) {
        showLoading();
        Call<DefResp> call = ApiClient.restAdapter(activity).create(ApiInterface.class).UpdatePass(email, password, OTP);
        call.enqueue(new Callback<DefResp>() {
            @Override
            public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                dismisLoading();
                if (response.body().getCode().equals("201")) {
                    showAlert(response.body().getMsg(),true);
                } else {
                    showAlert(response.body().getMsg(),false);
                }
            }

            @Override
            public void onFailure(Call<DefResp> call, Throwable t) {
                dismisLoading();
            }
        });
    }

    private void funVerifyEmail(String email) {
        showLoading();
        Call<DefResp> call = ApiClient.restAdapter(activity).create(ApiInterface.class).ResetPass(email,"reset");
        call.enqueue(new Callback<DefResp>() {
            @Override
            public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                dismisLoading();
                if (response.body().getCode().equals("201")) {
                    binding.email.setEnabled(false);
                    Fun.msgSuccess(activity, getString(R.string.otp_has_been_sended));
                    emailVerified = false;
                    otpVerified = true;
                    binding.procced.setText(getString(R.string.verify_otp));
                    initView();
                } else {
                    showAlert(response.body().getMsg(),false);
                }
            }

            @Override
            public void onFailure(Call<DefResp> call, Throwable t) {
                dismisLoading();
                Fun.msgError(activity, getString(R.string.try_again_later));
            }
        });
    }

    void showAlert(String msg,boolean success){
        alert.show();
        if(success){
            lytAlert.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
            lytAlert.title.setText(getString(R.string.password_reset));
            lytAlert.msg.setText(msg);
            lytAlert.negative.setVisibility(View.GONE);
            lytAlert.positive.setVisibility(View.VISIBLE);
            lytAlert.positive.setText(getString(R.string.login));
            lytAlert.positive.setOnClickListener(v -> {
                startActivity(new Intent(activity,FrontLogin.class));
                finish();
            });
        }else{
            lytAlert.negative.setText(getString(R.string.close));
            lytAlert.title.setText(getString(R.string.oops));
            lytAlert.msg.setText(msg);
            lytAlert.negative.setOnClickListener(v -> {
                alert.dismiss();
            });
        }

    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.txt_exit));
        builder.setMessage(getString(R.string.txt_confirm_exit));

        builder.setPositiveButton(getString(R.string.txt_yes), (dialog, which) -> {
            overridePendingTransition(R.anim.exit, R.anim.enter);
            finish();
        });

        builder.setNegativeButton(getString(R.string.txt_no), (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}