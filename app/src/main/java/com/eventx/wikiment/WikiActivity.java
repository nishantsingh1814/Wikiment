package com.eventx.wikiment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.eventx.wikiment.util.AppConstants;

public class WikiActivity extends AppCompatActivity {

    private WebView myWebView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);
        long pageId = getIntent().getLongExtra(AppConstants.PAGE_ID,-1);
        myWebView = findViewById(R.id.webview);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setIndeterminate(false);

        myWebView.loadUrl("https://en.wikipedia.org/?curid="+pageId);
        WebSettings webSettings = myWebView.getSettings();
        myWebView.setWebChromeClient(new MyWebChromeClient());
        myWebView.setWebViewClient(new MyWebViewClient());
        webSettings.setJavaScriptEnabled(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(myWebView, true);
    }
    private class MyWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            Log.i("progresstest", "onProgressChanged: "+progress);

            progressBar.setProgress(progress);
            setProgress(progress * 100);

            if (progress == 100)
                progressBar.setVisibility(View.GONE);
            else {
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
