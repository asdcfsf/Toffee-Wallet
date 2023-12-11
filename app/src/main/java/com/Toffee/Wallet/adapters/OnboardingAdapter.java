package com.Toffee.Wallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.SettingResp;

import java.util.List;

public class OnboardingAdapter extends PagerAdapter {

    private Context context;
    private List<SettingResp.OnboardingItem> introModels;

    public OnboardingAdapter(Context context, List<SettingResp.OnboardingItem> introModels) {
        this.context = context;
        this.introModels = introModels;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.item_onboarding, null);

        TextView headLines = layoutScreen.findViewById(R.id.title);
        TextView descriptions = layoutScreen.findViewById(R.id.desc);
        headLines.setText(introModels.get(position).getTitle());
        descriptions.setText(introModels.get(position).getSubtitle());

        LottieAnimationView Image=layoutScreen.findViewById(R.id.Image);

        Image.setAnimationFromUrl(introModels.get(position).getImage());
        Image.playAnimation();

        container.addView(layoutScreen);
        return layoutScreen;
    }

    @Override
    public int getCount() {
        return introModels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
