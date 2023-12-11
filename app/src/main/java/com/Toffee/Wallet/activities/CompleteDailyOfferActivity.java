package com.Toffee.Wallet.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.databinding.ActivityCompleteDailyOfferBinding;
import com.Toffee.Wallet.databinding.LayoutAlertBinding;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Fun;
import com.Toffee.Wallet.utils.Pref;
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
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteDailyOfferActivity extends AppCompatActivity {
    ActivityCompleteDailyOfferBinding bind;
    Pref pref;
    Activity activity;
    Bundle bundle;
    BottomSheetDialog uploadSheet;
    private static final int SELECT_PICTURE = 0;
    private Bitmap bitmap;
    String filepath,Id,url;
    AlertDialog alert, loading;
    LayoutAlertBinding lytAlert;
    Uri image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivityCompleteDailyOfferBinding.inflate(getLayoutInflater());
        Fun.statusbar(this);
        setContentView(bind.getRoot());

        activity=this;
        pref=new Pref(activity);
        bundle=getIntent().getExtras();
        loading=Fun.loading(activity);

        lytAlert=LayoutAlertBinding.inflate(getLayoutInflater());
        alert=Fun.Alerts(activity,lytAlert);

        Id=bundle.getString("id");
        url=bundle.getString("url");

        bind.tvTitle.setText(bundle.getString("title"));
        bind.coins.setText(bundle.getString("coin"));
        bind.desc.setText(Html.fromHtml(bundle.getString("description")));
        Picasso.get().load(bundle.getString("image")).fit().into(bind.images);

        bind.filloffer.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
                Dexter.withContext(this)
                        .withPermissions(Manifest.permission.READ_MEDIA_IMAGES).
                        withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                UploadBottomDialog();
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
                                UploadBottomDialog();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                Fun.msgError(activity, "Permission not Granted");
                            }
                        }).check();
            }
        });

        bind.startoffer.setOnClickListener(view -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));
            }catch (Exception e){
                Fun.msgError(activity, "Url Broken");
            }
        });

    }

    private void UploadBottomDialog() {
        uploadSheet = new BottomSheetDialog(activity, R.style.BottomSheetDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_upload_dailyoffer_ss, findViewById(R.id.uploadLayouts), false);
        uploadSheet.setContentView(view);
        uploadSheet.setCancelable(true);

        EditText link = view.findViewById(R.id.link);
        Button submit = view.findViewById(R.id.submit);

        view.findViewById(R.id.uploadImage).setOnClickListener(v -> {
            chooseImage();
        });

        view.findViewById(R.id.uploadImage1).setOnClickListener(v -> {
            chooseImage();
        });


        submit.setOnClickListener(v -> {
            if(image==null && link.getText().toString().isEmpty()){
                showbonus(getString(R.string.select_screenshot_link),true);
            }else{
                submitDetail(link.getText().toString().trim());
            }
        });

        if (!activity.isFinishing()) {
            uploadSheet.show();
        }
    }


    public void submitDetail(String link){
        showProgress();
        Call<DefResp> call;
        if(image==null){
            call= Objects.requireNonNull(ApiClient.restAdapter(activity)).create(ApiInterface.class).submit(link,Id);
        }else{
            File file = new File(filepath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part parts = MultipartBody.Part.createFormData("newimage", file.getName(), requestBody);
            call= Objects.requireNonNull(ApiClient.restAdapter(activity)).create(ApiInterface.class).submitWithAttach(parts,link,Id);
        }
        call.enqueue(new Callback<DefResp>() {
            @Override
            public void onResponse(Call<DefResp> call, Response<DefResp> response) {
//                try {
                    dismissProgress();
                    if(response.isSuccessful() && response.body().getCode().equals("201")){
                        uploadSheet.dismiss();
                        DailyOffer.removeItem=true;
                        showbonus(response.body().getMsg(),false);
                    }else{
                        showbonus(Objects.requireNonNull(response.body()).getMsg(),true);
                    }
//                }catch (Exception e){}
            }
            @Override
            public void onFailure(Call<DefResp> call, Throwable t) {
                Toast.makeText(activity, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });

    }

    private void chooseImage() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
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

    @Override
    public void onBackPressed() {
        finish();
    }
}