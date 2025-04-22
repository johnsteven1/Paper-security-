
package com.example.lockscreen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.media.AudioManager;
import android.media.ToneGenerator;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("lockData", MODE_PRIVATE);
        boolean unlocked = prefs.getBoolean("unlocked", false);

        if (unlocked) {
            finish(); // Exit if already unlocked
            return;
        }

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        webView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        webView.addJavascriptInterface(new WebAppInterface(), "Android");
        webView.loadUrl("file:///android_asset/lock.html");
    }

    public class WebAppInterface {
        @android.webkit.JavascriptInterface
        public void unlockDevice() {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("unlocked", true);
            editor.apply();
            runOnUiThread(() -> webView.loadUrl("file:///android_asset/success.html"));
        }

        @android.webkit.JavascriptInterface
        public void playClickSound() {
            ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            toneGen.startTone(ToneGenerator.TONE_PROP_BEEP);
        }

        @android.webkit.JavascriptInterface
        public void wrongPin() {
            // Optional: You can show a native toast or log
        }
    }
}
