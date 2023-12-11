package com.Toffee.Wallet.fragment;

import static com.Toffee.Wallet.utils.Const.REFER_BONUS;
import static com.Toffee.Wallet.utils.Const.REFER_TITLE_1;
import static com.Toffee.Wallet.utils.Const.REFER_TITLE_2;
import static com.Toffee.Wallet.utils.Const.REFER_TITLE_3;
import static com.Toffee.Wallet.utils.Const.WELCOME_BONUS;
import static com.Toffee.Wallet.utils.Fun.js;
import static com.Toffee.Wallet.utils.Fun.msgError;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anupkumarpanwar.scratchview.ScratchView;
import com.Toffee.Wallet.BuildConfig;
import com.Toffee.Wallet.activities.ReferralHistory;
import com.Toffee.Wallet.utils.AdsConfig;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefListResp;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.adapters.ScratchAdapter;
import com.Toffee.Wallet.ads.AdManager;
import com.Toffee.Wallet.databinding.FragmentInviteBinding;
import com.Toffee.Wallet.databinding.LayoutAlertBinding;
import com.Toffee.Wallet.databinding.LayoutScratchBinding;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.listener.OnScratchClickListener;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Invite extends Fragment implements OnItemClickListener, OnScratchClickListener {
    FragmentInviteBinding bind;
    Pref pref;
    Activity activity;
    AdManager adManager;
    List<DefListResp> scratchList = new ArrayList<>();
    ScratchAdapter scratchAdapter;
    BottomSheetDialog referCodeSheet;
    LayoutAlertBinding lytAlert;
    private AlertDialog loading, alert, scratchDialog,progressLoading;
    LayoutScratchBinding scratchBinding;
    String REFLINK;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentInviteBinding.inflate(getLayoutInflater());

        activity = getActivity();
        pref = new Pref(activity);
        adManager = new AdManager(activity);
        ChipNavigationBar chipNavigationBar = requireActivity().findViewById(R.id.navigation);
        chipNavigationBar.setItemSelected(R.id.navigation_invite, true);

        progressLoading=Fun.loadingProgress(activity);
        REFLINK=pref.getString(pref.REFLINK);

        loading = Fun.loading(activity);
        lytAlert = LayoutAlertBinding.inflate(getLayoutInflater());
        alert = Fun.Alerts(activity, lytAlert);

        bind.rvScratch.setLayoutManager(new GridLayoutManager(activity,2));
        scratchAdapter = new ScratchAdapter(activity, scratchList);
        scratchAdapter.setClickListener(this::onScratchClick);
        bind.rvScratch.setAdapter(scratchAdapter);

        prepareLink(false);
        getScratch();

        bind.lytCopy.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("link", pref.getString(pref.REFER_ID));
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);
            Fun.msgSuccess(activity, "Code copied!");
        });

        bind.invite.setOnClickListener(view -> {
            if(REFLINK!=null){
                share();
            }else {
                prepareLink(true);
                share();
            }

        });

        bind.seeMoreRefertask.setOnClickListener(view -> {
            if(REFLINK!=null){
                share();
            }else {
                prepareLink(true);
                share();
            }
        });

        bind.refercode.setText(pref.getString(pref.REFER_ID));

        bind.faq.setOnClickListener(v -> {
            Const.FAQ_TYPE = "invite";
            FaqBottomDialogFragment fragment = FaqBottomDialogFragment.newInstance();
            fragment.show(getFragmentManager(), FaqBottomDialogFragment.TAG);
        });

        bind.claimbonus.setOnClickListener(v -> {
            referBottomSheet();
        });

        bind.referralHistory.setOnClickListener(view -> {
            startActivity(new Intent(activity, ReferralHistory.class));
        });

        update();


        return bind.getRoot();
    }

    private void prepareLink(boolean progress) {
        if(REFLINK==null || REFLINK.equals("")){
            Fun.log("Invite_FRAG _refLinkNull");
            try {
                if(progress){showloading();}
                DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse("https://play.google.com/store/apps/details?id="+ requireActivity().getPackageName()+"&ref=" + pref.getString(pref.REFER_ID)))
                        .setDomainUriPrefix(getString(R.string.firebase_short_link))
                        .setAndroidParameters(
                                new DynamicLink.AndroidParameters.Builder(requireActivity().getPackageName())
                                        .setMinimumVersion(BuildConfig.VERSION_CODE)
                                        .build())
                        // Open links with com.example.ios on iOS
                        .buildDynamicLink();
                Fun.log("Invite_FRAG "+ "long link " + dynamicLink.getUri());

                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        //.setLongLink(dynamicLink.getUri())
                        .setLongLink(dynamicLink.getUri())  // manually
                        .buildShortDynamicLink()
                        .addOnCompleteListener(getActivity(), task -> {
                            if (task.isSuccessful()) {
                                // Short link created

                                Uri shortLink = task.getResult().getShortLink();
                                Uri flowchartLink = task.getResult().getPreviewLink();
                                Fun.log("Invite_FRAG "+ "short link " + shortLink.toString());
                                Fun.log("Invite_FRAG "+ "flowchartLink link " + flowchartLink);
                                // share app dialog
                                REFLINK = String.valueOf(shortLink);
                                pref.setData(pref.REFLINK,REFLINK);
                                if(progress){hideloading();}
                            } else {
                                if(progress){hideloading();}
                                msgError(activity,"Internal Error");
                                Fun.log("Invite_FRAG "+ "error  " + task.getException());
                            }
                        });
            } catch (Exception e) {
                if(progress){hideloading();}
                Fun.log("Internal Error.");
            }
        }else{
            Fun.log("Invite_FRAG _refLink is not null "+REFLINK);
        }
    }

    private void share() {
        Intent sendInt = new Intent(Intent.ACTION_SEND);
        sendInt.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        sendInt.putExtra(Intent.EXTRA_TEXT,
                Html.fromHtml(pref.getString(AdsConfig.referMsg)) + "\n" +
                        "Use my referral code " + pref.getString(pref.REFER_ID) + " on signup.\n" +
                        "\n" +
                        "Download link:"+
                        "\n" +REFLINK );
        sendInt.setType("text/plain");
        startActivity(Intent.createChooser(sendInt, "Share"));
    }

    private void update() {
        bind.tvGetPoint.setText("Upto " + REFER_BONUS);
        try {

            if(REFER_TITLE_1.contains("{refer_bonus}")){
                REFER_TITLE_1=REFER_TITLE_1.replace("{refer_bonus}",REFER_BONUS);
            }else if(REFER_TITLE_1.contains("{welcome_bonus}")){
                REFER_TITLE_1=REFER_TITLE_1.replace("{welcome_bonus}",WELCOME_BONUS);
            }
            bind.referPoint1.setText(REFER_TITLE_1);

            if(REFER_TITLE_2.contains("{refer_bonus}")){
                REFER_TITLE_2=REFER_TITLE_2.replace("{refer_bonus}",REFER_BONUS);
            }else if(REFER_TITLE_2.contains("{welcome_bonus}")){
                REFER_TITLE_2=REFER_TITLE_2.replace("{welcome_bonus}",WELCOME_BONUS);
            }
            bind.referPoint2.setText(REFER_TITLE_2);


            if(REFER_TITLE_3.contains("{refer_bonus}")){
                REFER_TITLE_3=REFER_TITLE_3.replace("{refer_bonus}",REFER_BONUS);
            }else if(REFER_TITLE_3.contains("{welcome_bonus}")){
                REFER_TITLE_3=REFER_TITLE_3.replace("{welcome_bonus}",WELCOME_BONUS);
            }
            bind.referPoint3.setText(REFER_TITLE_3);

        }catch (Exception e){}
    }

    private void referBottomSheet() {
        referCodeSheet = new BottomSheetDialog(activity, R.style.BottomSheetDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.referdialog, getActivity().findViewById(R.id.referDialog), false);
        referCodeSheet.setContentView(view);
        referCodeSheet.setCancelable(true);

        EditText refer = view.findViewById(R.id.refer);
        Button submit = view.findViewById(R.id.submit);
        RelativeLayout close = view.findViewById(R.id.close);

        submit.setOnClickListener(v -> {
            claimBonus(refer.getText().toString().trim());
        });

        close.setOnClickListener(v -> {
            referCodeSheet.dismiss();
        });

        if (!activity.isFinishing()) {
            referCodeSheet.show();
        }
    }

    private void getScratch() {
       try {
           if(REFER_TITLE_1==null){
               progressLoading.show();
           }
           bind.shimmerViewContainer.setVisibility(View.VISIBLE);
           Objects.requireNonNull(ApiClient.restAdapter(activity)).create(ApiInterface.class).getReferScratch(pref.User_id(), 16).enqueue(new Callback<DefResp>() {
               @Override
               public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                   bind.shimmerViewContainer.setVisibility(View.GONE);
                   if(progressLoading.isShowing()){ progressLoading.dismiss();}
                   if (response.isSuccessful() && response.body().getCode().equals("201")) {
                       bind.lytScratcCard.setVisibility(View.VISIBLE);
                       bind.rvScratch.setVisibility(View.VISIBLE);
                       scratchList.addAll(response.body().getData());
                       updateData(response.body());
                       scratchAdapter.notifyDataSetChanged();
                   } else {
                       bind.lytScratcCard.setVisibility(View.GONE);
                   }
               }

               @Override
               public void onFailure(Call<DefResp> call, Throwable t) {
                   if(progressLoading.isShowing()){ progressLoading.dismiss();}
               }
           });
       }catch (Exception e){
           Toast.makeText(activity, "-"+e.getMessage(), Toast.LENGTH_SHORT).show();
       }
    }

    @SuppressLint("SetTextI18n")
    private void updateData(DefResp resp) {
        REFER_BONUS= String.valueOf(resp.getRefer_bonus());
        WELCOME_BONUS= String.valueOf(resp.getWelcome_bonus());
        REFER_TITLE_1=resp.getRefer_point1();
        REFER_TITLE_2=resp.getRefer_point2();
        REFER_TITLE_3=resp.getRefer_point3();

        bind.tvGetPoint.setText("Upto " + REFER_BONUS);
        try {

            if(REFER_TITLE_1.contains("{refer_bonus}")){
                REFER_TITLE_1=REFER_TITLE_1.replace("{refer_bonus}",REFER_BONUS);
            }else if(REFER_TITLE_1.contains("{welcome_bonus}")){
                REFER_TITLE_1=REFER_TITLE_1.replace("{welcome_bonus}",WELCOME_BONUS);
            }
            bind.referPoint1.setText(REFER_TITLE_1);

            if(REFER_TITLE_2.contains("{refer_bonus}")){
                REFER_TITLE_2=REFER_TITLE_2.replace("{refer_bonus}",REFER_BONUS);
            }else if(REFER_TITLE_2.contains("{welcome_bonus}")){
                REFER_TITLE_2=REFER_TITLE_2.replace("{welcome_bonus}",WELCOME_BONUS);
            }
            bind.referPoint2.setText(REFER_TITLE_2);


            if(REFER_TITLE_3.contains("{refer_bonus}")){
                REFER_TITLE_3=REFER_TITLE_3.replace("{refer_bonus}",REFER_BONUS);
            }else if(REFER_TITLE_3.contains("{welcome_bonus}")){
                REFER_TITLE_3=REFER_TITLE_3.replace("{welcome_bonus}",WELCOME_BONUS);
            }
            bind.referPoint3.setText(REFER_TITLE_3);

            pref.setIntData(pref.successRef, resp.getSuccessRef());
            pref.setIntData(pref.pendingRef, resp.getPendingRef());
        }catch (Exception e){}
    }

    @Override
    public void onClick(View view, int position) {

    }

    void claimBonus(String refer) {
        showloading();
        Call<DefResp> call = ApiClient.restAdapter(activity).create(ApiInterface.class).api(js(activity,"referBonus", pref.User_id(), refer, "", ""));
        call.enqueue(new Callback<DefResp>() {
            @Override
            public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                hideloading();
                try {
                    if (response.isSuccessful() && response.body().getCode().equals("201")) {
                        referCodeSheet.dismiss();
                        showAlert(getString(R.string.congratulations), response.body().getMsg(), true);
                    } else {
                        showAlert("", response.body().getMsg(), false);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<DefResp> call, Throwable t) {
                hideloading();
            }
        });

    }

    void showloading() {
        loading.show();
    }

    void hideloading() {
        if (loading.isShowing()) {
            loading.dismiss();
        }
    }

    void showAlert(String title, String msg, boolean success) {
        alert.show();
        if (success) {
            lytAlert.title.setText(title);
            lytAlert.msg.setText(msg);
            lytAlert.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
            lytAlert.negative.setVisibility(View.GONE);
            lytAlert.positive.setVisibility(View.VISIBLE);
            lytAlert.positive.setText(getString(R.string.close));
            lytAlert.positive.setOnClickListener(v -> {
                scratchList.clear();
                getScratch();
                alert.dismiss();
            });

        } else {
            lytAlert.title.setText(getString(R.string.oops));
            lytAlert.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_warning));
            lytAlert.msg.setText(msg);
            lytAlert.negative.setOnClickListener(v -> {
                alert.dismiss();
            });
        }
    }

    @Override
    public void onScratchClick(View view, int position) {
        scratchBinding = LayoutScratchBinding.inflate(getLayoutInflater());
        scratchDialog = new AlertDialog.Builder(activity).setView(scratchBinding.getRoot()).create();
        scratchDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        scratchDialog.getWindow().setWindowAnimations(R.style.Dialoganimation);
        scratchDialog.setCanceledOnTouchOutside(false);
        scratchDialog.setCancelable(true);

        final boolean[] scratch = {false};
        scratchDialog.show();

        scratchBinding.Scratchcard.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
                scratchView.clear();
                scratchBinding.lytCoin.setVisibility(View.VISIBLE);
                scratchBinding.coupon.setText("You Won " + scratchList.get(position).getCoin());
                scratch[0] = true;
                showloading();
                Objects.requireNonNull(ApiClient.restAdapter(activity)).create(ApiInterface.class).api(js(activity,"revealScratch","","","",scratchList.get(position).getId())).enqueue(new Callback<DefResp>() {
                    @Override
                    public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                        hideloading();
                        if(response.isSuccessful() && response.body().getCode().equals("201")){
                            scratchDialog.dismiss();
                            Fun.msgSuccess(activity,response.body().getMsg());
                            scratchList.clear();
                            getScratch();
                        }else{
                            Fun.msgError(activity,response.body().getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<DefResp> call, Throwable t) {
                        hideloading();
                    }
                });
            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {

            }
        });

        scratchBinding.addTowallet.setOnClickListener(v1 -> {
            if (scratch[0]) {
                showloading();
                Objects.requireNonNull(ApiClient.restAdapter(activity)).create(ApiInterface.class).api(js(activity,"revealScratch","","","",scratchList.get(position).getId())).enqueue(new Callback<DefResp>() {
                    @Override
                    public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                        if(response.isSuccessful() && response.body().getCode().equals("201")){
                            Fun.msgSuccess(activity,response.body().getMsg());
                            scratchList.clear();
                            getScratch();
                        }else{
                            Fun.msgError(activity,response.body().getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<DefResp> call, Throwable t) {

                    }
                });
            } else {
                msgError(activity, "Something went wrong!!");
            }
        });


    }

}