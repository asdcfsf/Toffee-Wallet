package com.Toffee.Wallet.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefListResp;
import com.Toffee.Wallet.adapters.UniversalAdapter;
import com.Toffee.Wallet.databinding.ActivityLanguageBinding;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LanguageActivity extends AppCompatActivity implements OnItemClickListener {
    ActivityLanguageBinding bind;
    Activity activity;
    Pref pref;
    UniversalAdapter adapter;
    List<DefListResp> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivityLanguageBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());

        bind.tool.title.setText(getText(R.string.choose_language));
        bind.tool.faq.setVisibility(View.GONE);

        activity=this;
        pref=new Pref(activity);

        bind.rv.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new UniversalAdapter(activity,list);
        adapter.setClickListener(this);
        bind.rv.setAdapter(adapter);

        getLang();


        bind.tool.back.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    private void getLang() {
        ApiClient.restAdapter(activity).create(ApiInterface.class).getLanguage().enqueue(new Callback<List<DefListResp>>() {
            @Override
            public void onResponse(Call<List<DefListResp>> call, Response<List<DefListResp>> response) {
                if(response.isSuccessful() && response.body().size()>0){
                    bind.shimmerView.setVisibility(View.GONE);
                    bind.rv.setVisibility(View.VISIBLE);
                    list.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }else {
                    bind.shimmerView.setVisibility(View.GONE);
                    bind.layoutNoResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<DefListResp>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view, int position) {
        updateLanguage(list.get(position).getLang_code());
    }

    public void updateLanguage(String langCode){
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        configuration.setLayoutDirection(locale);
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        pref.setData(Pref.SELECTED_LANGUAGE,langCode);

        activity.startActivity(new Intent(activity,MainActivity.class));
        activity.finish();
    }
}