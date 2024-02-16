package com.example.audiowork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class AiChatActivity extends AppCompatActivity {
    private BaseWebView customWebView;

    private void checkAudioPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("onRequestPermissionsResult request code", String.valueOf(requestCode));
        Log.d("onRequestPermissionsResult grantResults", String.valueOf(grantResults));
        switch (requestCode) {
            case 102: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("权限成功", "reader permisson success");
                }
                break;
            }
            default: {
                Log.d("null权限成功", "check null");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chat);
        customWebView = new BaseWebView(this);

        LinearLayout layout = findViewById(R.id.chatai_layout);
        layout.addView(customWebView);
        customWebView.clearCache(true);
        customWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        customWebView.getSettings().setJavaScriptEnabled(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        customWebView.setLayoutParams(params);
        customWebView.loadUrl("http://66.42.71.56");
        this.checkAudioPermission();

    }
}