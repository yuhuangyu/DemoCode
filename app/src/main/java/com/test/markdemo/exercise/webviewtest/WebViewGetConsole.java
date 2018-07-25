package com.test.markdemo.exercise.webviewtest;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.test.markdemo.utils.ReflectAccess;

/**
 * Created by fj on 2018/7/18.
 */

public class WebViewGetConsole {


    //检测当前view中的webView
    public static void checkWebView(View parentView) {

        if (parentView instanceof ViewGroup) {
            if (parentView instanceof WebView) {
                Log.i("sdk","webView TTTTTTTTTTTT urlww = " + ((WebView) parentView).getUrl());
                Log.i("sdk","webView TTTTTTTTTTTT urlww = " + ((WebView) parentView).getContentDescription());
                WebView webView = (WebView) parentView;
                doWebview(webView);
                return;
            }
            int childCount = ((ViewGroup) parentView).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = ((ViewGroup) parentView).getChildAt(i);
                if (child instanceof WebView) {
                    WebView webView = (WebView) child;
                    doWebview(webView);

                } else if (child instanceof ViewGroup) {
                    checkWebView(child);
                }
            }
        }
    }

    public static void doWebview(WebView webView) {
        // 反射获取 执行js的响应
        getJsRespond(webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.requestFocus();
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);

        String jsCode = "console.log('point=269.2215496785939,486.0285707858391');";
        webView.loadUrl("javascript:" +jsCode);
        Log.e("sdk","===== loadUrl");
    }

    public static void getJsRespond(WebView webView) {
        //com.android.webview.chromium.WebViewContentsClientAdapter 4.4,6.0,7.0 WebView.mProvider.mContentsClientAdapter.mWebViewClient|mWebChromeClient
        //android.webkit.WebViewClassic 4.1,4.2 WebView.mProvider.mCallbackProxy.mWebViewClient|mWebChromeClient
        //android.webkit.CallbackProxy 4.0.3 WebView.mCallbackProxy.mWebViewClient|mWebChromeClient
        try {
            Object mWebChromeClient = getWebChromeClientObject(webView);
            if (mWebChromeClient != null) {
                WebChromeClient mmWebChromeClient = (WebChromeClient) mWebChromeClient;
                webView.setWebChromeClient(new AdmobWebChromeClient(mmWebChromeClient){
                    @Override
                    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                        Log.e("sdk","11====="+consoleMessage.toString());
                        if (consoleMessage != null && consoleMessage.message() != null) {
                            if (consoleMessage.message().contains("point")) {
//                                resultAdmob = consoleMessage.message();
                                Log.e("sdk","11===== webView consoleMessage "+consoleMessage.message());
                            }
                        }
                        return super.onConsoleMessage(consoleMessage);
                    }
                });
            }else {
                webView.setWebChromeClient(new WebChromeClient(){
                    @Override
                    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                        Log.e("sdk","22====="+consoleMessage.toString());
                        if (consoleMessage != null && consoleMessage.message() != null) {
                            if (consoleMessage.message().contains("point")) {
//                                resultAdmob = consoleMessage.message();
                                Log.e("sdk","22===== webView consoleMessage "+consoleMessage.message());
                            }
                        }
                        return super.onConsoleMessage(consoleMessage);
                    }
                });
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static Object getWebChromeClientObject(WebView webView) {
        //com.android.webview.chromium.WebViewContentsClientAdapter 4.4,6.0,7.0 WebView.mProvider.mContentsClientAdapter.mWebViewClient|mWebChromeClient
        //android.webkit.WebViewClassic 4.1,4.2 WebView.mProvider.mCallbackProxy.mWebViewClient|mWebChromeClient
        //android.webkit.CallbackProxy 4.0.3 WebView.mCallbackProxy.mWebViewClient|mWebChromeClient

        Object mWebChromeClient = null;
        try {
            if (Build.VERSION.SDK_INT  >= 18) {
                Object mProvider = ReflectAccess.getValue(webView, "mProvider");
                Object mContentsClientAdapter = ReflectAccess.getValue(mProvider, "mContentsClientAdapter");
                if (mContentsClientAdapter == null) {
                    mContentsClientAdapter = ReflectAccess.getValue(mProvider, "X");
                }
                mWebChromeClient = ReflectAccess.getValue(mContentsClientAdapter, "mWebChromeClient");
                if (mWebChromeClient == null) {
                    mWebChromeClient = ReflectAccess.getValue(mContentsClientAdapter, "D");
                }
            } else if (Build.VERSION.SDK_INT  == 16 || Build.VERSION.SDK_INT  == 17) {
                Object mProvider = ReflectAccess.getValue(webView, "mProvider");
                Object mCallbackProxy = ReflectAccess.getValue(mProvider, "mCallbackProxy");
                mWebChromeClient = ReflectAccess.getValue(mCallbackProxy, "mWebChromeClient");
            } else if (Build.VERSION.SDK_INT  <= 15) {
                Object mCallbackProxy = ReflectAccess.getValue(webView, "mCallbackProxy");
                mWebChromeClient = ReflectAccess.getValue(mCallbackProxy, "mWebChromeClient");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return mWebChromeClient;
    }
}
