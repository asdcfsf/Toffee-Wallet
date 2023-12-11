package com.Toffee.Wallet.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.databinding.ActivityBrowseBinding;

import im.delight.android.webview.AdvancedWebView;

public class BrowseActivity extends AppCompatActivity implements AdvancedWebView.Listener{
    ActivityBrowseBinding bind;
    Activity activity;
    String url,title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivityBrowseBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        activity=this;
        url=getIntent().getStringExtra("url");
        title=getIntent().getStringExtra("title");

        bind.webview.setListener(this, this);
        bind.webview.setMixedContentAllowed(false);
        bind.webview.loadUrl(url);



    }

    @Override
    protected void onResume() {
        super.onResume();
        bind.webview.onResume();
    }

    @Override
    protected void onPause() {
        bind.webview.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        bind.webview.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        bind.webview.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onBackPressed() {
        if (!bind.webview.onBackPressed()) { return; }
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}