package com.test.markdemo.exercise.webviewtest;

import android.content.Context;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;


/**
 * Created by ASUS on 2018/4/4.
 */

public class WebManager2 {
    private WebView _webView;
    private Context _context;
    private static Handler handler = new Handler(Looper.getMainLooper());
    private WebStateManager webStateManager;
    private FloatWindow _win;
    public static final int Close = 1;
    public static final int Completed = 2;
    public static final int Limit = 3;
    public WebManager2(Context context, WebView webView){
        _context = context;
        if (webView != null) {
            _webView = webView;
        } else
            _webView = new WebView(_context);

        float a = 0.5f;
        _webView.getSettings().setJavaScriptEnabled(true);
        _webView.getSettings().setAllowFileAccess(true);
        _webView.getSettings().setLoadsImagesAutomatically(true);
        _webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        _webView.getSettings().setDomStorageEnabled(true);

        _webView.getSettings().setAppCacheEnabled(true);
        _webView.getSettings().setAppCachePath(_context.getDir("localdata", Context.MODE_PRIVATE).getPath());

        _webView.setBackgroundColor(Color.BLACK);
        _webView.setAlpha(a);
//        _webView.addJavascriptInterface(new WebManager.Sdk(), "WebSdk");
//        _webView.addJavascriptInterface(new Controller(), "WebController");

        if (Build.VERSION.SDK_INT >= 21)
            _webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        /*_webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (!com.omg.net.NetUtil.hasWifiConn(context))
                    return;
                if (url != null && (url.startsWith("http") || url.startsWith("ftp"))) {
                    DownloadManager manager = (DownloadManager) _context.getSystemService(Context.DOWNLOAD_SERVICE);

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                    request.setMimeType(mimetype);
                    request.setDescription("");

                    manager.enqueue(request);
                }
            }
        });*/

        _win = new FloatWindow(context);
        _win.setView(_webView);

        FloatWindow.ViewInfo info = FloatWindow.ViewInfo.get(_context);

        _webView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, _win.getWindowManager().getDefaultDisplay().getHeight() - info.getStatusBarHeight()));

        _win.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        _win.setHeight(_win.getWindowManager().getDefaultDisplay().getHeight() - info.getStatusBarHeight());
        _win.setFlag(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//        _win.setType(WindowManager.LayoutParams.TYPE_TOAST);
        float b = a;
        float d = 1 - a;

        _win.setButtonBrightness(b);
        _win.setDimAmount(d);
    }

    public void setup(final String url) {
//        Logger.i("web:" + url);

//        String jsUrl = "";
        final WebStateManager state = initManager();

//        _webView.addJavascriptInterface(new WebManager.StateManager(state), "StateManager");

/*        _webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
//                if (consoleMessage != null && consoleMessage.message() != null)
//                    Logger.i(consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }
        });*/

        _webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                state.doExec(StateParams.PageFinished);
            }
        });

        _win.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                state.doExec(0);
                _webView.loadUrl(url);
            }
        }, 3000);
    }

    private WebStateManager initManager() {
        final long timeout = 7000;
        webStateManager = WebStateManager.getWebStateManager();

        // Start -> WaitLong
        webStateManager.define(StateParams.Start, new WebStateManager.WebStateBase() {
            @Override
            protected String judge(int state) {
                return StateParams.WaitLong;
            }
        });

        // WaitLong -> WaitShort PageFinished
        // WaitLong -> Hook Timeout Long
        webStateManager.define(StateParams.WaitLong, new WebStateManager.WebStateBase() {
            private TimeoutHandler _handler;

            @Override
            protected String judge(int state) {
                switch (state) {
                    case StateParams.TimeoutLong:
                        return StateParams.Hook;
                    case StateParams.PageFinished:
                        return StateParams.WaitShort;
                }

                return null;
            }

            @Override
            protected void init() {
                int span = 40 * 1000;

                _handler = new TimeoutHandler(new Runnable() {
                    @Override
                    public void run() {
                        exec(StateParams.TimeoutLong);
                    }
                }, span);

                _handler.start();
            }

            @Override
            protected void release() {
                if (_handler != null)
                    _handler.cancel();
            }
        });

        // WaitShort -> WaitShort PageFinished
        // WaitShort -> Hook Timeout Short
        webStateManager.define(StateParams.WaitShort, new WebStateManager.WebStateBase() {
            private TimeoutHandler _handler;

            @Override
            protected String judge(int state) {
                switch (state) {
                    case StateParams.TimeoutShort:
                        return StateParams.Hook;
                    case StateParams.PageFinished:
                        return StateParams.WaitShort;
                }
                return null;
            }

            @Override
            protected void init() {
                _handler = new TimeoutHandler(new Runnable() {
                    @Override
                    public void run() {
                        exec(StateParams.TimeoutShort);
                    }
                }, timeout);

                _handler.start();
            }

            @Override
            protected void release() {
                if (_handler != null)
                    _handler.cancel();
            }
        });

        // Hook -> WaitLong Notify
        // Hook -> End Close
        webStateManager.define(StateParams.Hook, new WebStateManager.WebStateBase() {
            @Override
            protected String judge(int state) {
                switch (state) {
                    case StateParams.Notify:
                        return StateParams.WaitLong;
                    case StateParams.Close:
                        return StateParams.End;
                }

                return null;
            }

            @Override
            protected void init() {
//                if (!TextUtils.isEmpty(method) && !TextUtils.isEmpty(jsUrl)) {
//                    inject(method, jsUrl);
//                }
                exec(StateParams.Close);

            }
        });

        webStateManager.define(StateParams.End, new WebStateManager.WebStateBase() {
            @Override
            protected String judge(int state) {
                return null;
            }

            @Override
            protected void init() {
                super.init();
                Log.i("123","close");
                close(0);
            }
        });
        webStateManager.setInitState(StateParams.Start);
        return webStateManager;
    }
    private void close(int i) {
        _win.close();
    }
}
