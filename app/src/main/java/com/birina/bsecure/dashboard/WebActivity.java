package com.birina.bsecure.dashboard;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.birina.bsecure.Base.BaseActivity;
import com.birina.bsecure.Base.BirinaActivity;
import com.birina.bsecure.R;
import com.birina.bsecure.util.Constant;


public class WebActivity extends BirinaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkExpireStatus();
        setContentView(R.layout.activity_web);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String url ="";
        Intent intent = getIntent();
        if(null != intent){
             url = setTitleAndUrl(intent.getStringExtra(Constant.WEB_INTENT_KEY));
        }

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebActivity.this, description, Toast.LENGTH_SHORT).show();
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        webView .loadUrl(url);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkExpireStatus();
    }

    private String setTitleAndUrl(String nav){

        String title = "";
        String url = "";
        switch (nav) {
            case Constant.HELP:
                title = Constant.HELP;
                url = Constant.URL_HELP;
                break;
            case Constant.FEEDBACK:
                title = Constant.FEEDBACK;
                url = Constant.URL_FEEDBACK;
                break;
            case Constant.ABOUT:
                title = Constant.ABOUT;
                url = Constant.URL_ABOUT;
                break;
            case Constant.URL_SUPPORT:
                title = Constant.SUPPORT;
                url = Constant.URL_SUPPORT;
                break;
                default:
                    title = getResources().getString(R.string.app_name);
        }

        getSupportActionBar().setTitle(title);

        return url;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
