package com.Toffee.Wallet.activities;

import static com.android.billingclient.api.BillingClient.SkuType.INAPP;
import static com.Toffee.Wallet.utils.Fun.encrypt_code;
import static com.Toffee.Wallet.utils.Fun.msgError;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ads.androidsdk.sdk.util.InputValidation;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetailsParams;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefListResp;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.adapters.UniversalAdapter;
import com.Toffee.Wallet.databinding.ActivityPaymentBinding;
import com.Toffee.Wallet.databinding.LayoutAlertBinding;
import com.Toffee.Wallet.fragment.FaqBottomDialogFragment;
import com.Toffee.Wallet.listener.OnItemClickListener;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.restApi.WebApi;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
import com.Toffee.Wallet.utils.Security;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class PaymentActivity extends AppCompatActivity implements PurchasesUpdatedListener, OnItemClickListener , PaymentResultListener {
    ActivityPaymentBinding bind;
    Pref pref;
    PaymentActivity activity;
    AlertDialog loading,alert;
    LayoutAlertBinding lytAlert;
    String productID;
    private BillingClient billingClient;
    String amount,currency,model_type,google_key,payment_mode, model_id,orderID,payment_status="Success";
    Bundle bundle;
    List<DefListResp> listResps=new ArrayList<>();
    UniversalAdapter adapter;
    private PayPalConfiguration configpaypal;
    int posi;
    public static final int PAYPAL_REQUEST_CODE = 113;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivityPaymentBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());

        bind.tool.title.setText(getString(R.string.choose_payment_method));

        activity=this;
        pref=new Pref(activity);
        loading= Fun.loading(activity);
        lytAlert= LayoutAlertBinding.inflate(getLayoutInflater());
        alert=Fun.Alerts(activity,lytAlert);

        bundle=getIntent().getExtras();

        model_type=bundle.getString("type");
        model_id =bundle.getString("id");
        amount=bundle.getString("amount");
        currency=bundle.getString("currency");
        productID=getIntent().getStringExtra("productID");
        if(model_type.equalsIgnoreCase("sub")){
            bind.planName.setText(bundle.getString("title"));
            bind.validity.setText("Validity : "+ bundle.getString("validity")+" month");
            if(bundle.getString("currency_posi").equals("0")){
                bind.amount.setText(bundle.getString("currency")+" "+bundle.getString("amount"));
            }else {
                bind.amount.setText(bundle.getString("amount")+" "+bundle.getString("currency"));
            }


            Picasso.get().load(WebApi.Api.IMAGES +bundle.getString("image")).fit().into(bind.icon);
        }
        else {
            bind.planName.setText(bundle.getString("title"));
            bind.validity.setText(Fun.formatNumber(bundle.getString("coin"))+" Coin");
            if(bundle.getString("currency_posi").equals("0")){
                bind.amount.setText(bundle.getString("currency")+" "+bundle.getString("amount"));
            }else {
                bind.amount.setText(bundle.getString("amount")+" "+bundle.getString("currency"));
            }

            bind.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_coin_coinstore));
        }

        bind.rv.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new UniversalAdapter(activity,listResps);
        adapter.setClickListener(this);
        bind.rv.setAdapter(adapter);
        fetchPaymentGateway();

        bind.tool.faq.setOnClickListener(v -> {
            Const.FAQ_TYPE="payment";
            FaqBottomDialogFragment fragment =  FaqBottomDialogFragment.newInstance();
            fragment.show(getSupportFragmentManager(),FaqBottomDialogFragment.TAG);
        });

        bind.tool.back.setOnClickListener(v -> {
            onBackPressed();
        });
    }


    @Override
    public void onClick(View view, int position) {
        this.posi=position;
        try {
            JSONArray jsonArray = new JSONArray(listResps.get(position).getConfig());
            payment_mode=listResps.get(position).getType();
            switch (payment_mode){
                case "flutter_wave":
                    initFlutterWave(amount,jsonArray.getJSONObject(0).getString("value"),jsonArray.getJSONObject(0).getString("value"));
                    break;

                case "paypal":
                    initPaypal(position,jsonArray.getJSONObject(0).getString("value"));
                    getPaypal(amount);
                    break;

                case "razor_pay":
                    Checkout.preload(activity);
                    initRazorPay(position,jsonArray.getJSONObject(0).getString("value"));
                    break;

                case "google_pay":
                    billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
                    google_key=jsonArray.getJSONObject(0).getString("value");
                    BillingAssign();
                    break;

                case "qr":
                    initQr(position,jsonArray);
                    break;

                case "bank":
                    initBank(position,jsonArray);
                    break;
            }
        }catch (Exception e){
            Fun.log("payment_activity_on_click "+e.getMessage());
            msgError(activity,"Something went wrong ");
        }

    }

    private void initFlutterWave(String amount,String publicKey,String encKey) {
        orderID= pref.User_id()+UUID.randomUUID().toString();
        String formattedAmount = amount;
        formattedAmount = new StringBuilder(formattedAmount).insert(formattedAmount.length()-2, ".").toString();

        double finalamount = Double.parseDouble(formattedAmount);

        new RaveUiManager(this).setAmount(finalamount)
                .setCurrency(currency)
                .setEmail(pref.getString(pref.EMAIL))
                .setfName(pref.getString(pref.NAME))
                .setlName("")
                .setNarration("Topup")
                .setPublicKey(publicKey)
                .setEncryptionKey(encKey)
                .setTxRef(orderID)
//                .setPhoneNumber(user.getPhone_number(), true)
                .acceptAccountPayments(true)
                .acceptCardPayments(true)
                .acceptMpesaPayments(true)
                .acceptAchPayments(true)
                .acceptGHMobileMoneyPayments(true)
                .acceptUgMobileMoneyPayments(true)
                .acceptZmMobileMoneyPayments(true)
                .acceptRwfMobileMoneyPayments(true)
                .acceptSaBankPayments(true)
                .acceptUkPayments(true)
                .acceptBankTransferPayments(true)
                .acceptUssdPayments(true)
                .acceptBarterPayments(true)
                .allowSaveCardFeature(true)
                .onStagingEnv(listResps.get(posi).isTest_mode())
                .withTheme(R.style.FlutterwaveTheme)
                .isPreAuth(true)
                .shouldDisplayFee(true)
                .showStagingLabel(true)
                .initialize();
    }

    private void initPaypal(int position,String key) {
        configpaypal = new PayPalConfiguration();
        if (listResps.get(position).isTest_mode()) {
            configpaypal.environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK);
        } else {
            configpaypal.environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION);
        }
        configpaypal.clientId(key);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configpaypal);
        startService(intent);
    }

    private void getPaypal(String price) {
        double Amount = Double.parseDouble(convertAngka(price.replace(currency, "")));
        PayPalPayment payment = new PayPalPayment(new BigDecimal(Amount),
                    currency, "CoinStore " + getString(R.string.app_name),
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, com.paypal.android.sdk.payments.PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configpaypal);
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Fun.msgSuccess(activity,"Payment Successfully.");
                        orderID=confirm.getProofOfPayment().getPaymentId();
                        payment_status=confirm.getProofOfPayment().getState();
                        verifyPay();

                    } catch (Exception e) {
                        Fun.msgError(activity,"Payment failure.");
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Fun.log("PAYPAL PAYMENT The user canceled.");
                Fun.msgError(activity,"Payment Canceled.");

            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Fun.log("PAYPAL PAYMENT An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
                Fun.msgError(activity,"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }

        else if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Fun.msgSuccess(activity,"Payment Successfully.");
                verifyPay();
            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Timber.tag("FLUTTERWAVEERROR").e(message);
                Fun.msgError(activity,message);
            }
        }
    }

    public String convertAngka(String value) {
        return (((((value + "")
                .replaceAll(currency, ""))
                .replaceAll(" ", ""))
                .replaceAll(",", ""))
                .replaceAll("[$.]", ""));
    }

    //    razor
    private void initRazorPay(int amount,String key) {
        Checkout checkout = new Checkout();
        checkout.setKeyID(key);
        checkout.setImage(R.mipmap.ic_launcher);

        try {
            JSONObject options = new JSONObject();
            options.put("name",pref.getString(pref.NAME));
            options.put("description", "Charge Of Plan");
            options.put("theme.color", "#f59614");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            options.put("currency","INR");
            options.put("amount", (float) amount * 100);//pass amount in currency subunits
            options.put("prefill.email",pref.getString(pref.EMAIL));
            checkout.open(activity, options);

        } catch (Exception e) {
            Fun.msgError(activity, "Error in starting Razorpay Checkout");
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Fun.msgSuccess(activity,"Payment Successful");
        System.out.println("reazor_pay__onPaymentSuccess "+s);

    }

    @Override
    public void onPaymentError(int i, String s) {
        System.out.println("reazor_pay__onPaymentError "+s);
        String message = "";
        if (i == Checkout.PAYMENT_CANCELED) {
            message = "The user canceled the payment.";
        } else if (i == Checkout.NETWORK_ERROR) {
            message = "There was a network error, for iqueen, loss of internet connectivity.";
        } else if (i == Checkout.INVALID_OPTIONS) {
            message = "An issue with options passed in checkout.open .";
        } else if (i == Checkout.TLS_ERROR) {
            message = "The device does not support TLS v1.1 or TLS v1.2.";
        } else {
            message = "Unknown Error";
        }

        Fun.msgError(activity,message);
    }

    // end razor
    private void initBank(int position,JSONArray jsonArray){
        try {
            Intent intent= new Intent(activity,OfflinePayment.class);
            intent.putExtra("id",model_id);
            intent.putExtra("payment_id",listResps.get(position).getId());
            intent.putExtra("title",listResps.get(position).getTitle());
            intent.putExtra("purchase_type",model_type);
            intent.putExtra("type","bank");
            intent.putExtra("bank_name",jsonArray.getJSONObject(0).getString("value"));
            intent.putExtra("account_name",jsonArray.getJSONObject(1).getString("value"));
            intent.putExtra("account_number",jsonArray.getJSONObject(2).getString("value"));
            intent.putExtra("account_ifsc",jsonArray.getJSONObject(3).getString("value"));
            intent.putExtra("description",jsonArray.getJSONObject(4).getString("value"));
            startActivity(intent);
        }catch (Exception e){
            Fun.msgError(activity,e.getMessage());
        }
    }

    private void initQr(int position,JSONArray jsonArray){
       try {
           Intent intent= new Intent(activity,OfflinePayment.class);
           intent.putExtra("title",listResps.get(position).getTitle());
           intent.putExtra("id",model_id);
           intent.putExtra("payment_id",listResps.get(position).getId());
           intent.putExtra("type","qr");
           intent.putExtra("purchase_type",model_type);
           intent.putExtra("qr_code",jsonArray.getJSONObject(0).getString("value"));
           intent.putExtra("qrmethod",jsonArray.getJSONObject(1).getString("value"));
           intent.putExtra("pay_id",jsonArray.getJSONObject(2).getString("value"));
           intent.putExtra("description",jsonArray.getJSONObject(3).getString("value"));
           startActivity(intent);
       }catch (Exception e){
           Fun.log("qr_error_ "+e.getMessage());
           Fun.msgError(activity,e.getMessage());
       }
    }
    private void fetchPaymentGateway() {
        ApiClient.restAdapter(activity).create(ApiInterface.class).getPaymentGateway().enqueue(new Callback<List<DefListResp>>() {
            @Override
            public void onResponse(Call<List<DefListResp>> call, Response<List<DefListResp>> response) {
                bind.shimmerView.setVisibility(View.GONE);
                if(response.isSuccessful() && response.body().size()>0){
                    bind.shimmerView.setVisibility(View.GONE);
                    bind.rv.setVisibility(View.VISIBLE);
                    listResps.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }else{
                    bind.layoutNoResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<DefListResp>> call, Throwable t) {
                bind.shimmerView.setVisibility(View.GONE);
                bind.layoutNoResult.setVisibility(View.VISIBLE);
            }
        });
    }

    void showAlert(String msg, boolean error) {
        alert.show();
        if (error) {
            lytAlert.negative.setText(getString(R.string.close));
            lytAlert.title.setText(getString(R.string.oops));
            lytAlert.msg.setText(msg);
            lytAlert.negative.setOnClickListener(v -> {
                alert.dismiss();
            });
        }
        else {
            lytAlert.title.setText(getString(R.string.congratulations));
            lytAlert.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
            lytAlert.msg.setText(msg);
            lytAlert.negative.setVisibility(View.GONE);
            lytAlert.positive.setVisibility(View.VISIBLE);
            lytAlert.positive.setText(getString(R.string.close));
            lytAlert.positive.setOnClickListener(v -> {
                alert.dismiss();
            });
        }
    }

//  In APP
    public void BillingAssign() {
        if (billingClient.isReady()) {
            initiatePurchase(productID);
        }
        //else reconnect service
        else {
            billingClient = BillingClient.newBuilder(activity).enablePendingPurchases().setListener(this).build();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase(productID);
                    } else {
                        Fun.msgError(activity,"Error " + billingResult.getDebugMessage());
                    }
                }

                @Override
                public void onBillingServiceDisconnected() {
                }
            });
        }

    }
    private void initiatePurchase(final String PRODUCT_ID) {
        List<String> skuList = new ArrayList<>();
        skuList.add(PRODUCT_ID);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(INAPP);
        billingClient.querySkuDetailsAsync(params.build(),
                (billingResult, skuDetailsList) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                    .setSkuDetails(skuDetailsList.get(0))
                                    .build();
                            billingClient.launchBillingFlow(activity, flowParams);
                        } else {
                            //try to add item/product id "c1" "c2" "c3" inside managed product in google play console
                            Toast.makeText(getApplicationContext(), "Purchase Item " + PRODUCT_ID + " not Found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), " Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {

        //if item newly purchased
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases);
        }
        //if item already purchased then check and reflect changes
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {

//            billingClient.queryPurchasesAsync(INAPP, (billingResult1, list) -> handlePurchases(list));

        }
        //if purchase cancelled
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(getApplicationContext(), "Purchase Canceled", Toast.LENGTH_SHORT).show();
        }
        // Handle any other error messages
        else {
            Toast.makeText(getApplicationContext(), "Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    void handlePurchases(List<Purchase> purchases) {

        for (Purchase purchase : purchases) {
            //if item is purchased
            if (purchase.getSkus().contains(productID) && purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
                    // Invalid purchase
                    // show error to user
                    Fun.msgError(activity, "Error : Invalid Purchase");
                    return;
                }else {
                    Fun.msgSuccess(activity, "Error : Invalid Purchase");
                    System.out.print("in_app_purchase___"+purchase.getOriginalJson());
                 //   verifyPay(purchase.getOriginalJson());
                }

                // else purchase is valid
                //if item is purchased and not consumed
                if (!purchase.isAcknowledged()) {
                    ConsumeParams consumeParams = ConsumeParams.newBuilder()
                            .setPurchaseToken(purchase.getPurchaseToken())
                            .build();

                    billingClient.consumeAsync(consumeParams, consumeListener);
                }
            }
            //if purchase is pending
            else if (purchase.getSkus().contains(productID) && purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
                Toast.makeText(getApplicationContext(),
                        "Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT).show();
            }
            //if purchase is refunded or unknown
            else if (purchase.getSkus().contains(productID) && purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                Fun.msgSuccess(activity, "Purchase Status Unknown\"");
            }
        }
    }

    ConsumeResponseListener consumeListener = (billingResult, purchaseToken) -> {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
        }
    };

    /**
     * Verifies that the purchase was signed correctly for this developer's public key.
     * &lt;p>Note: It's strongly recommended to perform such check on your backend since hackers can
     * replace this method with "constant true" if they decompile/rebuild your app.
     * &lt;/p>
     */
    private boolean verifyValidSignature(String signedData, String signature) {
        try {
            //for old playconsole
            // To get key go to Developer Console > Select your app > Development Tools > Services & APIs.
            //for new play console
            //To get key go to Developer Console > Select your app > Monetize > Monetization setup

            String base64Key = google_key;
            return Security.verifyPurchase(base64Key, signedData, signature);
        } catch (IOException e) {
            return false;
        }
    }

    void showLoading() {
        loading.show();
    }

    void dismisLoading() {
        if (loading.isShowing())
            loading.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(billingClient!=null) {
            billingClient.endConnection();
        }
    }


    //verify
    private void verifyPay() {
        showLoading();
        Call<DefResp> call= ApiClient.restAdapter(activity).create(ApiInterface.class).verifyPay(d(activity));
        call.enqueue(new Callback<DefResp>() {
            @Override
            public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                dismisLoading();
                if(response.isSuccessful() && response.body().getCode().equals("201")){
                    pref.setIntData(pref.PROMO_WALLET,response.body().getBalance());
                    showAlert(response.body().getMsg(),false);
                }else{
                    showAlert(response.body().getMsg(),true);
                }
            }

            @Override
            public void onFailure(Call<DefResp> call, Throwable t) {
                dismisLoading();
                Toast.makeText(PaymentActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public  String d(Activity a){
        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new JsonObject());
        jsObj.addProperty("paywith",payment_mode);
        jsObj.addProperty("orderid",orderID);
        jsObj.addProperty("model_type",model_type);
        jsObj.addProperty("model_id", model_id);
        jsObj.addProperty("status",payment_status);
        try {
            return InputValidation.parse(a,encrypt_code(jsObj.toString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}