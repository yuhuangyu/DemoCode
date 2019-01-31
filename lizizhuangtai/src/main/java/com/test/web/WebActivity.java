package com.test.web;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.test.lizizhuangtai.R;

/**
 * Created by fj on 2018/10/22.
 */

public class WebActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity);
        String adsenseBannerUrl = "https://play.google.com/store/apps/details?id=com.hwgg.tcsdzz";
        WebView webView = new WebView(this);
        webView.loadUrl(adsenseBannerUrl);



    }

}
