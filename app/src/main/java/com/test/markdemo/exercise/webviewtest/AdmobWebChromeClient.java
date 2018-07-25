package com.test.markdemo.exercise.webviewtest;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

/**
 * Created by ASUS on 2018/5/16.
 */

public class AdmobWebChromeClient extends WebChromeClient {
    private WebChromeClient mWebChromeClient;
    public AdmobWebChromeClient(WebChromeClient mWebChromeClient) {
        this.mWebChromeClient = mWebChromeClient;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        mWebChromeClient.onProgressChanged(view, newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        mWebChromeClient.onReceivedTitle(view, title);
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        mWebChromeClient.onReceivedIcon(view, icon);
    }

    @Override
    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        mWebChromeClient.onReceivedTouchIconUrl(view, url, precomposed);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        mWebChromeClient.onShowCustomView(view, callback);
    }

    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        mWebChromeClient.onShowCustomView(view, requestedOrientation, callback);
    }

    @Override
    public void onHideCustomView() {
        mWebChromeClient.onHideCustomView();
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        return mWebChromeClient.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    @Override
    public void onRequestFocus(WebView view) {
        mWebChromeClient.onRequestFocus(view);
    }

    @Override
    public void onCloseWindow(WebView window) {
        mWebChromeClient.onCloseWindow(window);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return mWebChromeClient.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return mWebChromeClient.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return mWebChromeClient.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        return mWebChromeClient.onJsBeforeUnload(view, url, message, result);
    }

    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
        mWebChromeClient.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
    }

    @Override
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
        mWebChromeClient.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        mWebChromeClient.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        mWebChromeClient.onGeolocationPermissionsHidePrompt();
    }

    @Override
    public void onPermissionRequest(PermissionRequest request) {
        mWebChromeClient.onPermissionRequest(request);
    }

    @Override
    public void onPermissionRequestCanceled(PermissionRequest request) {
        mWebChromeClient.onPermissionRequestCanceled(request);
    }

    @Override
    public boolean onJsTimeout() {
        return mWebChromeClient.onJsTimeout();
    }

    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        mWebChromeClient.onConsoleMessage(message, lineNumber, sourceID);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return mWebChromeClient.onConsoleMessage(consoleMessage);
    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        return mWebChromeClient.getDefaultVideoPoster();
    }

    @Override
    public View getVideoLoadingProgressView() {
        return mWebChromeClient.getVideoLoadingProgressView();
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        mWebChromeClient.getVisitedHistory(callback);
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        return mWebChromeClient.onShowFileChooser(webView, filePathCallback, fileChooserParams);
    }
}
