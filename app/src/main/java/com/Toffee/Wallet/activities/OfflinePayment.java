package com.Toffee.Wallet.activities;

import static com.Toffee.Wallet.utils.Fun.loading;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.databinding.ActivityOfflinePaymentBinding;
import com.Toffee.Wallet.databinding.LayoutAlertBinding;
import com.Toffee.Wallet.fragment.FaqBottomDialogFragment;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Fun;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfflinePayment extends AppCompatActivity {
    ActivityOfflinePaymentBinding bind;
    Activity activity;
    Bundle bundle;
    String type, id, acNumber, acName, acIfsc, payment_id, purchase_type;
    BottomSheetDialog uploadPaymentSheet;
    private static final int SELECT_PICTURE = 0;
    private Bitmap bitmap;
    String filepath, encodedimage;
    Uri image;
    AlertDialog alert, loading;
    LayoutAlertBinding lytAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityOfflinePaymentBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());

        activity = this;
        lytAlert = LayoutAlertBinding.inflate(getLayoutInflater());
        alert = Fun.Alerts(activity, lytAlert);
        loading = loading(activity);

        bundle = getIntent().getExtras();

        bind.tool.title.setText(bundle.getString("title"));

        type = bundle.getString("type");
        id = bundle.getString("id");
        payment_id = bundle.getString("payment_id");
        purchase_type = bundle.getString("purchase_type");

        if (type.equalsIgnoreCase("bank")) {
            acNumber = bundle.getString("account_number");
            acName = bundle.getString("account_name");
            acIfsc = bundle.getString("account_ifsc");

            bind.bankLyt.setVisibility(View.VISIBLE);
            bind.bankName.setText(bundle.getString("bank_name"));
            bind.bankdescription.setText(bundle.getString("description"));
            bind.acName.setText(acName);
            bind.acNumber.setText(acNumber);
            bind.acIfsc.setText(acIfsc);

            bind.accopy.setOnClickListener(v -> {
                copy(acName);
            });

            bind.acIfsccopy.setOnClickListener(v -> {
                copy(acIfsc);
            });

            bind.acNumbercopy.setOnClickListener(v -> {
                copy(acNumber);
            });
        }
        else if (type.equalsIgnoreCase("qr")) {
            bind.qrLyt.setVisibility(View.VISIBLE);
            bind.qrmethod.setText(bundle.getString("qrmethod"));

            Picasso.get().load(bundle.getString("qr_code")).fit().into(bind.qrcode);
            bind.tvqrid.setText(bundle.getString("pay_id"));

            bind.copy.setOnClickListener(v -> {
                copy(bundle.getString("pay_id"));
            });

            bind.description.setText(bundle.getString("description"));
        }

        bind.upload.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
                    Dexter.withContext(this)
                    .withPermissions(Manifest.permission.READ_MEDIA_IMAGES).
                            withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            PaymentBottomDialog();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            Fun.msgError(activity, "Permission not Granted");
                        }
                    }).check();
            }
            else {
                Dexter.withContext(this)
                        .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE).
                        withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                PaymentBottomDialog();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                Fun.msgError(activity, "Permission not Granted");
                            }
                        }).check();
            }
        });

        bind.tool.back.setOnClickListener(v -> {
            onBackPressed();
        });

        bind.tool.faq.setOnClickListener(view -> {
            Const.FAQ_TYPE="payment";
            FaqBottomDialogFragment fragment =  FaqBottomDialogFragment.newInstance();
            fragment.show(getSupportFragmentManager(),FaqBottomDialogFragment.TAG);
        });
    }

    void copy(String text) {
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("link", text);
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
        Fun.msgSuccess(activity, "Copied!");
    }

    private void PaymentBottomDialog() {
        uploadPaymentSheet = new BottomSheetDialog(activity, R.style.BottomSheetDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_upload_payment_ss, findViewById(R.id.uploadLayout), false);
        uploadPaymentSheet.setContentView(view);
        uploadPaymentSheet.setCancelable(true);

        EditText transactionid = view.findViewById(R.id.transactionID);
        Button submit = view.findViewById(R.id.submit);

        view.findViewById(R.id.uploadImage).setOnClickListener(v -> {
            chooseImage();
        });

        view.findViewById(R.id.uploadImage1).setOnClickListener(v -> {
            chooseImage();
        });


        submit.setOnClickListener(v -> {
            if (image == null || transactionid.getText().toString() == null) {
                Toast.makeText(activity, getString(R.string.select_screenshot_payment), Toast.LENGTH_SHORT).show();
            } else {
                submitDetail(transactionid.getText().toString());
            }
        });

        if (!activity.isFinishing()) {
            uploadPaymentSheet.show();
        }
    }

    private void chooseImage() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void submitDetail(String transactionid) {
        showProgress();
        Call<DefResp> call;
        File file = new File(filepath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part parts = MultipartBody.Part.createFormData("newimage", file.getName(), requestBody);
        call = ApiClient.restAdapter(activity).create(ApiInterface.class).submitPayment(parts, id, purchase_type, transactionid,bundle.getString("title"),payment_id);
        call.enqueue(new Callback<DefResp>() {
            @Override
            public void onResponse(Call<DefResp> call, Response<DefResp> response) {
                try {
                    dismissProgress();
                    if (response.isSuccessful() && response.body().getCode().equals("201")) {
                        uploadPaymentSheet.dismiss();
                        showbonus(response.body().getMsg(), false);
                    } else {
                        showbonus(response.body().getMsg(), true);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<DefResp> call, Throwable t) {
                dismissProgress();
            }
        });
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            image = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(image);
                bitmap = BitmapFactory.decodeStream(inputStream);
                Bitmap.createScaledBitmap(bitmap, 350, 600, false);

                filepath = getRealPathFromURI(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    void showbonus(String msg, boolean error) {
        alert.show();
        if (error) {
            lytAlert.title.setText(getString(R.string.oops));
            lytAlert.msg.setText(msg);
            lytAlert.negative.setOnClickListener(v -> {
                alert.dismiss();
            });
        } else {
            lytAlert.title.setText(getString(R.string.congratulations));
            lytAlert.msg.setText(msg);
            lytAlert.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
            lytAlert.negative.setVisibility(View.GONE);
            lytAlert.positive.setVisibility(View.VISIBLE);
            lytAlert.positive.setText(getString(R.string.close));
            lytAlert.positive.setOnClickListener(v -> {
                alert.dismiss();
                onBackPressed();
            });
        }

    }

    void showProgress() {
        if (!loading.isShowing()) {
            loading.show();
        }
    }

    void dismissProgress() {
        if (loading.isShowing()) {
            loading.dismiss();
        }
    }
}