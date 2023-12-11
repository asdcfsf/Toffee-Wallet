package com.Toffee.Wallet.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.activities.FaqActivity;
import com.Toffee.Wallet.adapters.ViewpagerAdapter;
import com.Toffee.Wallet.databinding.FragmentCpaOfferBinding;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.tabs.TabLayout;

public class CpaOffer extends Fragment {
    FragmentCpaOfferBinding bind;
    Activity activity;
    private ViewpagerAdapter catadapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind=FragmentCpaOfferBinding.inflate(getLayoutInflater());
        activity=getActivity();

        viewPager = bind.catviewpager;
        tabLayout= bind.tablayout;
        catadapter= new ViewpagerAdapter(getChildFragmentManager());
        catadapter.AddFragment(new CpiProgress(),"test");
        viewPager.setAdapter(catadapter);
        viewPager.setOffscreenPageLimit(1);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(getString(R.string.top_offers));
        tabLayout.getTabAt(1).setText(getString(R.string.on_progress));


        bind.faq.setOnClickListener(v -> {
            startActivity(new Intent(activity, FaqActivity.class).putExtra("type","cpi"));
            Animatoo.animateFade(activity);
        });

        return  bind.getRoot();
    }
}