package com.test.markdemo.eg;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.WebView;

import com.test.markdemo.utils.InputSimulator;

import java.util.Random;

/**
 * Created by ASUS on 2017/10/25.
 */

public class EgSave {
    private Context _context;
    private WebView _webView;

    // webview 滑动 例子保存
    private void executeRoll(int remainLength, boolean scrollDown)
    {
        int startY = _webView.getScrollY();

        int rm = innerRoll(startY, remainLength, scrollDown);
        if (rm > 200)
            innerRoll(_webView.getScrollY(), rm, scrollDown);
    }

    private int innerRoll(int startY, int remainLength, boolean scrollDown)
    {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) _context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int clientWidth = metrics.widthPixels;
        int clientHeight = metrics.heightPixels;
        int ty;
        Random random = new Random();
        int totalLength = remainLength;
        while (remainLength > 0)
        {
            int fx = (int) (Math.floor(clientWidth * (0.4 + Math.random() * 0.1)));
            int fy = (int) Math.floor(clientHeight * (0.8 + Math.random() * 0.1));
            int tx = (int) Math.floor(clientWidth * (0.3 + Math.random() * 0.2));
            int scrollLength = (int) Math.floor(clientHeight * (0.5 + Math.random() * 0.2));

            int length;
            if (remainLength < scrollLength)
            {
                length = remainLength;
            }
            else
            {
                length = scrollLength;
            }
            ty = fy - length;
            int r = 1 + random.nextInt(2);
            int n = 2 + random.nextInt(5);
            if (scrollDown)
            {
                // fy < ty（起始坐标 < 目标坐标）往下滑动
                InputSimulator.roll(_webView, fx, fy, tx, ty, 100, (r * 1000 + n * 100));
            }
            else
            {
                //起始坐标 > 目标坐标 往上滑动
                InputSimulator.roll(_webView, tx, ty, fx, fy, 100, (r * 1000 + n * 100));
            }

            try
            {
                Thread.sleep((6 + random.nextInt(5)) * 100);
            }
            catch (Exception e)
            {

            }

            int tmpLength;
            tmpLength = totalLength - Math.abs(_webView.getScrollY() - startY);

            if (tmpLength == remainLength || remainLength <= (int) (20 * _context.getResources().getDisplayMetrics().density))
            {
//                Logger.e("tmpLength:" + tmpLength + ",remainLength:" + remainLength);
                break;
            }
            remainLength = tmpLength;
        }

        if ((totalLength - Math.abs(_webView.getScrollY() - startY) > 300))
        {
//            Logger.showOnUI(_context, "滑动相差过大");
//            Logger.e("滑动相差过大, " + ((totalLength - Math.abs(_webView.getScrollY() - startY))));
        }

        return remainLength;
    }
}
