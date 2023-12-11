package com.Toffee.Wallet.support;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.Responsemodel.SupportResp;
import com.Toffee.Wallet.activities.FaqActivity;
import com.Toffee.Wallet.adapters.SupportAdapter;
import com.Toffee.Wallet.databinding.ActivitySupportBinding;
import com.Toffee.Wallet.databinding.LayoutSupportBinding;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Fun;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportActivity extends AppCompatActivity {
    ActivitySupportBinding bind;
    SupportActivity activity;
    LayoutSupportBinding lytSupport;
    AlertDialog supportDialog;
    List<SupportResp> list;
    SupportAdapter adapter;
    AlertDialog pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivitySupportBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());
        activity=this;

        bind.tool.title.setText(getString(R.string.support));

        pb=Fun.loadingProgress(activity);
        lytSupport=LayoutSupportBinding.inflate(getLayoutInflater());
        supportDialog = new AlertDialog.Builder(activity).setView(lytSupport.getRoot()).create();
        supportDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        supportDialog.getWindow().setWindowAnimations(R.style.Dialoganimation);
        supportDialog.setCanceledOnTouchOutside(false);
        supportDialog.setCancelable(true);

        list=new ArrayList<>();
        bind.rv.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new SupportAdapter(activity,list);
        bind.rv.setAdapter(adapter);

        getTicket();

        bind.createTicket.setOnClickListener(v -> {
            showSupportDialog();
        });

        bind.tool.back.setOnClickListener(view -> {
            onBackPressed();
        });

    }

    private void showSupportDialog() {
        supportDialog.show();
        lytSupport.positive.setOnClickListener(v -> {
            if(lytSupport.email.getText().toString().isEmpty()){
                Toast.makeText(activity, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show();
            }else if(lytSupport.subject.getText().toString().isEmpty()){
                Toast.makeText(activity, getString(R.string.please_fill_details), Toast.LENGTH_SHORT).show();
            }else if(lytSupport.message.getText().toString().isEmpty()){
                Toast.makeText(activity, getString(R.string.please_fill_details), Toast.LENGTH_SHORT).show();
            }else{
                submitQuery(lytSupport.email.getText().toString(),lytSupport.subject.getText().toString(),lytSupport.message.getText().toString());
            }
        });

        lytSupport.negative.setOnClickListener(v -> {
            supportDialog.dismiss();
        });
    }

    public void faqSupport(View view) {
        startActivity(new Intent(activity, FaqActivity.class).putExtra("type","support"));
    }

    void submitQuery(String email,String subject,String message){
        pb.show();
        ApiClient.restAdapter(activity).create(ApiInterface.class).createSupportTicket(email,subject,message).enqueue(new Callback<DefResp>() {
            @Override
            public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                pb.dismiss();
                if(response.isSuccessful() && response.body().getCode().equals("201")){
                    supportDialog.dismiss();
                    Fun.msgSuccess(activity, ""+response.body().getMsg());
                    getTicket();
                }else{
                    Fun.msgError(activity, ""+response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<DefResp> call, Throwable t) {
                pb.dismiss();
            }
        });
    }

    private void getTicket() {
        ApiClient.restAdapter(activity).create(ApiInterface.class).supportTicket().enqueue(new Callback<List<SupportResp>>() {
            @Override
            public void onResponse(Call<List<SupportResp>> call, Response<List<SupportResp>> response) {
                if (response.isSuccessful() && response.body().size() != 0) {
                    list.clear();
                    bindData(response);
                } else {
                    noResult();
                }
            }

            @Override
            public void onFailure(Call<List<SupportResp>> call, Throwable t) {

            }
        });
    }

    private void bindData(Response<List<SupportResp>> response) {
        list.addAll(response.body());
        bind.layoutNoResult.setVisibility(View.GONE);
        bind.shimmerView.setVisibility(View.GONE);
        bind.rv.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    private void noResult() {
        bind.shimmerView.setVisibility(View.GONE);
        bind.layoutNoResult.setVisibility(View.VISIBLE);

    }
}