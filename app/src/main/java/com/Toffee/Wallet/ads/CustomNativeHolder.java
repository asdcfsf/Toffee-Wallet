package com.Toffee.Wallet.ads;

import static com.Toffee.Wallet.utils.Const.nativeList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.Fun;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.Random;


public class CustomNativeHolder extends RecyclerView.ViewHolder {
    private final String TAG = "Custom_NATIVE";
    Context ctx;
    TextView title,body;
    RoundedImageView icon;
    ImageView bigPicture;
    Button btn;
    LinearLayout custom_nativeLyt;

    public CustomNativeHolder(View view) {
        super(view);
        this.ctx=view.getContext();

        title=view.findViewById(R.id.native_ad_title);
        body=view.findViewById(R.id.native_ad_body);
        icon=view.findViewById(R.id.native_ad_icon);
        bigPicture=view.findViewById(R.id.native_ad_media);
        btn=view.findViewById(R.id.native_ad_call_to_action);
        custom_nativeLyt=view.findViewById(R.id.custom_nativeLyt);

    }

    public void bindData(int posi){
        if(!nativeList.isEmpty()) {
            Fun.log("native_list_status "+ "Not empty");
            Random rand = new Random();
            // Get Random Index from the List
            int randomIndex = rand.nextInt(nativeList.size()-1);

            title.setText(nativeList.get(randomIndex).getTitle());
            body.setText(nativeList.get(randomIndex).getDescription());
            btn.setText(nativeList.get(randomIndex).getBtn_name());

            Glide.with(ctx).load(WebApi.Api.IMAGES + nativeList.get(randomIndex).getImage()).into(bigPicture);
            Picasso.get().load(WebApi.Api.IMAGES + nativeList.get(randomIndex).getIcon()).error(R.drawable.placeholder).fit().into(icon);

            btn.setOnClickListener(v -> {
               try {
                   if(nativeList.get(randomIndex).getUrl().startsWith("http")){
                       ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(nativeList.get(randomIndex).getUrl())));
                   }
               }catch (Exception e){}
            });
        }else {
            Fun.log("native_list_status "+ "empty");
            custom_nativeLyt.setVisibility(View.GONE);
        }
    }

}
