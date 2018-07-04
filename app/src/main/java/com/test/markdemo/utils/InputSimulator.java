package com.test.markdemo.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
/*
* 自动点击，滑动
* 寻找view的点击
* */
public class InputSimulator {
    private static Handler _handler = new Handler(Looper.getMainLooper());

    public static void waitViewChangedAsAD(final ViewFindListener listener, final String searchTag, final long timeout)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                long last = SystemClock.uptimeMillis();
                while (true)
                {
                    if (SystemClock.uptimeMillis() - last > timeout)
                        return;

                    View view = searchView(searchTag);
                    if (view != null && InputSimulator.find(view, new ViewFilter()
                    {
                        @Override
                        public boolean onFilter(View view)
                        {

                            return true;
                        }
                    }) != null)
                    {
                        listener.onFind(view);
                        return;
                    }
                }
            }
        }).start();
    }

    public static void waitViewChanged(final ViewFindListener listener, final String old, final long timeout) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                long last = SystemClock.uptimeMillis();
                while (true) {
                    if (SystemClock.uptimeMillis() - last > timeout)
                        return;

                    List<View> list = getAllRootViews();
                    if (list != null) {
                        /**
                         * 新建list解决多线程冲突问题
                         */
                        List<View> tempList = new ArrayList<View>();
                        tempList.addAll(list);
                        list = tempList;
                        for (View view : list) {
                            if (view != null && InputSimulator.find(view, new ViewFilter() {
                                @Override
                                public boolean onFilter(View view) {
                                    return view.getClass().getName().contains(old);
                                }
                            }) != null) {
                                listener.onFind(view);
                                return;
                            }
                        }
                    }

                }
            }
        }).start();
    }

    public static View searchViewBd() {
        List<View> list = getAllRootViews();
        for (int i = list.size() - 1; i >= 0; i--) {

            View view = list.get(i);
            View fv = forViewBd(view);
            if (fv != null) {

                return fv;
            }
        }
        return null;
    }

    private synchronized static View forViewBd(View view) {
        if (view == null) {
            return null;
        }

        if (view.getClass() != null) {
            String s = view.getClass().getName();
            if (!TextUtils.isEmpty(s) && s.contains(".remote.d.h")) {

                return view;
            }
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            if (group != null && group.getChildCount() > 0) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    View fv = group.getChildAt(i);
                    if (fv != null && fv.getClass() != null) {
                        String s = fv.getClass().getName();
                        if (!TextUtils.isEmpty(s) && s.contains(".remote.d.h")) {
                            return fv;
                        } else {
                            View v = forViewBd(fv);
                            if (v != null)
                                return v;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static void waitViewChanged(final ViewFindListener listener, final long timeout) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                long last = SystemClock.uptimeMillis();
                while (true) {
                    if (SystemClock.uptimeMillis() - last > timeout)
                        return;
                    View view = searchViewBd();

                    if (view != null && InputSimulator.find(view, new ViewFilter() {
                        @Override
                        public boolean onFilter(View view) {

                            return true;
                        }
                    }) != null) {
                        listener.onFind(view);
                        return;
                    }
                }
            }
        }).start();
    }

    public static void waitViewChanged(final ViewFindListener listener, final View old, final long timeout) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                long last = SystemClock.uptimeMillis();
                while (true) {
                    if (SystemClock.uptimeMillis() - last > timeout)
                        return;

                    View view = getFoucsView();

                    if (view != null && !view.equals(old)) {
                        listener.onFind(view);
                        return;
                    }
                }
            }
        }).start();
    }

    public static void waitFoucsView(final ViewFindListener listener, final long delay, final long timeout) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long last = SystemClock.uptimeMillis();
                while (true) {
                    if (SystemClock.uptimeMillis() - last > timeout)
                        return;

                    View view = getFoucsView();

                    if (view != null) {
                        listener.onFind(view);
                        return;
                    }
                }
            }
        }).start();
    }

    public static void clickRandom(View view, int x, int y) {
        click(view, x, y, RandomUtil.randInt(30, 150));
    }

    private static MotionEvent create(int type, float x, float y, long downTime, long time) {
        MotionEvent event = MotionEvent.obtain(downTime, time, type, x, y, 0);
        return event;
    }

    public static void clickNoDelay(final View view, final int x, final int y) {
        long time = SystemClock.uptimeMillis();
        view.dispatchTouchEvent(create(MotionEvent.ACTION_DOWN, x, y, time, time));
        view.dispatchTouchEvent(create(MotionEvent.ACTION_UP, x, y, time, time));
    }

    public static void click(final View view, final int x, final int y, final long span) {
        _handler.post(new Runnable() {
            public void run() {
                long time = SystemClock.uptimeMillis();
                long eventTime = time;
                view.dispatchTouchEvent(create(MotionEvent.ACTION_DOWN, x, y, time, time));
                //点击坐标位置随机偏移
                float dx = 0;
                float dy = 0;
                int random1 = new Random().nextInt(10);
                Random random = new Random(System.nanoTime());
                if (random1 > 4) {
                    dx = (float) (random.nextGaussian()*1.5);
                    if (Math.abs(dx) > 5){
                        dx = 0;
                    } else {
                        if (random1 > 4)
                            dx = 0;
                    }

                    dy = (float) (random.nextGaussian()*1.5);
                    if (Math.abs(dy) > 5)
                        dy = 0;
                }
                else
                {
                    dy = (float) (random.nextGaussian()*1.5);
                    if (Math.abs(dy) > 5){
                        dy = 0;
                    } else {
                        if (random1 > 4)
                            dy = 0;
                    }
                    dx = (float) (random.nextGaussian()*1.5);
                    if (Math.abs(dx) > 5)
                        dx = 0;
                }
                if (span != 0) {
                    try {
                        Thread.sleep(span);
                    } catch (InterruptedException e) {
                    }
                    eventTime = SystemClock.uptimeMillis();
                }
                view.dispatchTouchEvent(create(MotionEvent.ACTION_UP, x + dx, y + dy, time, eventTime));
            }
        });
    }

    private static boolean dispatch(final View view, final MotionEvent event) {
        if (view.getContext() instanceof Activity) {
            Activity ac = (Activity) view.getContext();
            if (ac.isFinishing()) {
//                Logger.e(view.getContext(),"dispatchTouchEvent error,because activity has finish");
                return false;
            }
        }
        _handler.post(new Runnable() {
            @Override
            public void run() {
                view.dispatchTouchEvent(event);
            }
        });

        return true;
    }

    private static MotionEvent createRoll(int type, float x, float y, long eventTime, long downTime) {
        MotionEvent event = MotionEvent.obtain(downTime, eventTime, type, x, y, 0);
        return event;
    }

    public static void roll(final View view, final int fx, final int fy, final int tx, final int ty, final int rate, final int span) {
        try {
            long sleepTime = span / rate;
            long beginTime = SystemClock.uptimeMillis();
            long time = SystemClock.uptimeMillis();
            if (!dispatch(view, createRoll(MotionEvent.ACTION_DOWN, fx, fy, time, beginTime)))
                return;
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < rate; i++) {
                int x = fx + (tx - fx + 1) * i / rate;
                int y = fy + (ty - fy + 1) * i / rate;

                time = SystemClock.uptimeMillis();
                if (!dispatch(view, createRoll(MotionEvent.ACTION_MOVE, x, y, time, beginTime)))
                    return;

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            time = SystemClock.uptimeMillis();
            dispatch(view, createRoll(MotionEvent.ACTION_UP, tx, ty, time, beginTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class FindTarget {
        private String _type;
        private Map<String, Object> _attrMap = new HashMap<String, Object>();

        public FindTarget(String type) {
            _type = type;
        }

        public FindTarget() {
        }

        public String getType() {
            return _type;
        }

        public void setType(String _type) {
            this._type = _type;
        }

        public Class<?> getTargetClass(Context context) {
            if (_type == null)
                return null;

            try {
                String name = getType().contains(".") ? getType()
                        : "android.widget." + getType();
                return context.getClassLoader().loadClass(name);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

        public void add(String name, Object value) {
            _attrMap.put(name, value);
        }

        public void add(String name, String fromat, String value) {
            String f = fromat.toLowerCase();
            if ("integer".equals(f))
                _attrMap.put(name, Integer.parseInt(value));
            else if ("long".equals(f))
                _attrMap.put(name, Long.parseLong(value));
            else if ("boolean".equals(f))
                _attrMap.put(name, Boolean.parseBoolean(value));
            else if ("float".equals(f))
                _attrMap.put(name, Float.parseFloat(value));
            else if ("double".equals(f))
                _attrMap.put(name, Double.parseDouble(value));
            else
                _attrMap.put(name, value);
        }

        public boolean matchView(View view) {
            Class<?> cls = getTargetClass(view.getContext());

            if (cls != null && !cls.isAssignableFrom(view.getClass())
                    && !cls.getName().equals(view.getClass().getName()))
                return false;

            boolean r = true;

            Set<String> keys = _attrMap.keySet();
            for (String key : keys) {
                Object value = _attrMap.get(key);

                String name = ((char) (key.charAt(0) - 'a' + 'A'))
                        + key.substring(1);

                String methodName = null;
                if (value instanceof Boolean)
                    methodName = "is" + name;
                else
                    methodName = "get" + name;

                try {
                    Method method = getMethod(view, methodName);

                    if (method == null) {
                        r = false;
                        continue;
                    }

                    Object res = method.invoke(view);

                    if (!value.equals(res.toString())) {
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            return r;
        }

        private Method getMethod(Object obj, String name) {
            try {
                Method method = obj.getClass().getMethod(name);
                method.setAccessible(true);
                return method;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public interface ViewFilter {
        boolean onFilter(View view);
    }

    public interface ViewFindListener {
        boolean cancel();

        void onFind(View view);
    }

    public abstract static class OneViewFindListener implements
            ViewFindListener {
        private boolean _cancel = false;

        @Override
        public boolean cancel() {
            return _cancel;
        }

        @Override
        public void onFind(View view) {
            _cancel = true;

            onViewFind(view);
        }

        public abstract void onViewFind(View view);
    }

    public static void waitForFind(final ViewFilter filter,
                                   final ViewFindListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (listener == null || listener.cancel())
                        return;

                    final View view = findFoucs(filter);

                    if (view != null) {
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null)
                                    listener.onFind(view);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public static <T extends View> T findFoucs(ViewFilter filter) {
        View view = getFoucsView();

        if (view == null)
            return null;

        return find(view, filter);
    }

    public static <T extends View> T find(View view, ViewFilter filter) {
        return (T) findView(view, filter, false);
    }

    public static <T extends View> T find(View view, boolean isDeep,
                                          final FindTarget... targetList) {
        return (T) findView(view, new ViewFilter() {
            @Override
            public boolean onFilter(View view) {
                for (FindTarget findTarget : targetList) {
                    if (view == null)
                        return false;

                    if (findTarget.matchView(view))
                        return true;
                }

                return false;
            }
        }, isDeep);
    }

    private static Rect getViewOnScreenRect(View view) {
        int[] pos = new int[2];
        view.getLocationOnScreen(pos);

        return new Rect(pos[0], pos[1], pos[0] + view.getWidth(), pos[1]
                + view.getHeight());
    }

    public static <T extends View> T findByPosition(View view, final Point pos) {
        return (T) findView(view, new ViewFilter() {
            @Override
            public boolean onFilter(View view) {
                Rect rect = getViewOnScreenRect(view);
                return rect.contains((int) pos.x, (int) pos.y);
            }
        }, true);
    }

    private static View findView(View view, ViewFilter filter, boolean isDeep) {
        if (!isDeep) {
            if (filter != null && filter.onFilter(view))
                return view;
        }

        if (view instanceof ViewGroup) {
            View res = findGroup((ViewGroup) view, filter, isDeep);
            if (res != null)
                return res;
        }

        if (isDeep) {
            if (filter != null && filter.onFilter(view))
                return view;
        }

        return null;
    }

    private static View findGroup(ViewGroup group, ViewFilter filter,
                                  boolean isDeep) {
        int count = group.getChildCount();
        for (int index = 0; index < count; index++) {
            View view = findView(group.getChildAt(index), filter, isDeep);
            if (view != null)
                return view;
        }

        return null;
    }

    public static View getFoucsView() {
        List<View> list = getAllRootViews();
        for (int i = list.size() - 1; i >= 0; i--) {
            View view = list.get(i);
            if (view.hasWindowFocus()) {
                return view;
            }
        }

        return null;
    }

    public static List<View> getAllRootViews() {
        Object viewsObject = getAllRootViewsObject();

        if (viewsObject instanceof View[]) {
            List<View> views = new ArrayList<View>();

            for (View view : (View[]) viewsObject) {
                views.add(view);
            }

            return views;
        } else if (viewsObject instanceof List) {
            return (List<View>) viewsObject;
        }

        return new ArrayList<View>();
    }

    private static Object getAllRootViewsObject() {
        try {
            Object host;
            if (Build.VERSION.SDK_INT > 16) {
                Field field = InputSimulator.class.getClassLoader()
                        .loadClass("android.view.WindowManagerGlobal")
                        .getDeclaredField("sDefaultWindowManager");
                field.setAccessible(true);
                host = field.get(null);
            } else {
                Method method = InputSimulator.class.getClassLoader()
                        .loadClass("android.view.WindowManagerImpl")
                        .getDeclaredMethod("getDefault");
                method.setAccessible(true);
                host = method.invoke(null);
            }

            Field field = host.getClass().getDeclaredField("mViews");
            field.setAccessible(true);

            return field.get(host);
        } catch (Exception e) {
            return null;
        }
    }


    public static View searchView(String searchTag){
        List<View> list = getAllRootViews();
        for (int i = list.size() - 1; i >= 0; i--) {
            View view = list.get(i);
            View fv = forViewAll(view, searchTag);
            if(fv != null){
                return view;
            }
        }
        return null;
    }
    public synchronized static View forViewAll(View view, String searchTag){
        if(view == null){
            return null;
        }
        if (view.getClass() != null) {
            String s = view.getClass().getName();
            if (!TextUtils.isEmpty(s) && s.contains(searchTag)) {
                return view;
            }
        }
        if(view instanceof ViewGroup){
            ViewGroup group = (ViewGroup)view;
            if(group != null && group.getChildCount() > 0){
                for(int i = 0; i < group.getChildCount(); i++){
                    View  fv = group.getChildAt(i);
                    if(fv != null && fv.getClass() != null){
                        String s = fv.getClass().getName();
                        if(!TextUtils.isEmpty(s) && s.contains(searchTag)){
                            return fv;
                        }
                        else {
                            View v = forViewAll(fv, searchTag);
                            if(v != null)
                                return v;
                        }
                    }
                }
            }
        }
        return null;
    }


    public static  void sendTap(Activity activity, float x, float y)
    {
        sendTap(activity, InputDevice.SOURCE_TOUCHSCREEN, x, y);
    }

    public static void sendTap(Activity activity, int inputSource, float x, float y)
    {
        long now = SystemClock.uptimeMillis();
        injectMotionEvent(activity, inputSource, MotionEvent.ACTION_DOWN, now, now, x, y, 1.0f);
        injectMotionEvent(activity, inputSource, MotionEvent.ACTION_UP, now, now + 50, x, y, 0.0f); //第二次事件 + 50毫秒 不可行
    }


    private static void injectMotionEvent(Activity activity, int inputSource, int action, long downTime, long when, float x, float y, float pressure)
    {
        final float DEFAULT_SIZE = 1.0f;
        final int DEFAULT_META_STATE = 0;
        final float DEFAULT_PRECISION_X = 1.0f;
        final float DEFAULT_PRECISION_Y = 1.0f;
        final int DEFAULT_DEVICE_ID = 0;
        final int DEFAULT_EDGE_FLAGS = 0;

        MotionEvent event = MotionEvent.obtain(downTime, when, action, x, y, pressure, DEFAULT_SIZE,
                DEFAULT_META_STATE, DEFAULT_PRECISION_X, DEFAULT_PRECISION_Y, DEFAULT_DEVICE_ID,
                DEFAULT_EDGE_FLAGS);
        event.setSource(inputSource);
        activity.dispatchTouchEvent(event);

        event.recycle();
    }

    public static void convertActivityToTranslucent(Activity activity)
    {
        try
        {
            Class[] t = Activity.class.getDeclaredClasses();
            Class translucentConversionListenerClazz = null;
            Class[] method = t;
            int len = t.length;

            for (int i = 0; i < len; ++i)
            {
                Class clazz = method[i];
                if (clazz.getSimpleName().contains("TranslucentConversionListener"))
                {
                    translucentConversionListenerClazz = clazz;
                    break;
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                Method var8 = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz, ActivityOptions.class);
                var8.setAccessible(true);
                var8.invoke(activity, new Object[]{null, null});
            }
            else
            {
                Method var8 = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz);
                var8.setAccessible(true);
                var8.invoke(activity, new Object[]{null});
            }
        }
        catch (Throwable e)
        {
        }

    }

    public static void hideActivity(final Activity activity, Context context) {
        // 获取屏幕宽高
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;// 屏幕宽（像素，如：480px）  
        int screenHeight = dm.heightPixels;// 屏幕高（像素，如：800px）

        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );

        convertActivityToTranslucent(activity);
        WindowManager.LayoutParams windowLP = activity.getWindow().getAttributes();
        windowLP.x = screenWidth-1;
        windowLP.y = screenHeight-1;
        windowLP.gravity =  Gravity.LEFT | Gravity.TOP;
        windowLP.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        windowLP.alpha = 0.0f;
        activity.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
            }
        },1);
    }

    //回到桌面
    public static void backToHome(Context context) {
        Intent home=new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(home);
    }
}
