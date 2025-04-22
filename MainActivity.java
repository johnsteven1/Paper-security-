package com.example.lockscreenapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;

public class MainActivity extends Activity {
    WebView webView;
    SharedPreferences prefs;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("lockPrefs", MODE_PRIVATE);
        webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(), "Android");
        setContentView(webView);
        webView.loadUrl("file:///android_asset/lock.html");
    }

    private class WebAppInterface {
        @JavascriptInterface
        public void playClickSound() {
            // Optional: Implement sound logic
        }

        @JavascriptInterface
        public void unlockDevice() {
            prefs.edit().putBoolean("unlocked", true).apply();
            finish();
        }

        @JavascriptInterface
        public void lockPermanently() {
            prefs.edit().putBoolean("unlocked", false).apply();
        }
    }
}
