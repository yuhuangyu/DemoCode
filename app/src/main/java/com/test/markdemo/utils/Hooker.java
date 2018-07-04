package com.test.markdemo.utils;

import android.content.Intent;
import android.os.Build;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class Hooker
{
    public static class Handle
    {
        public boolean IsCancel = false;
    }

    public interface OnActivityManagerHooker
    {
        void onStartActivity(Intent intent, Handle handle);
    }

    private static List<OnActivityManagerHooker> _list = new ArrayList<OnActivityManagerHooker>();

    public static void addHocker(OnActivityManagerHooker hooker)
    {
        _list.add(hooker);
    }

    public static void clearHock(){
        if(!_list.isEmpty()){
            _list.clear();
        }
    }

    public static void hook()
    {
        hook(new OnActivityManagerHooker()
        {
            @Override
            public void onStartActivity(Intent intent, Handle handle)
            {
                for (OnActivityManagerHooker hooker : _list)
                {
                    hooker.onStartActivity(intent, handle);
                }
            }
        });
    }
    private static Object activityManagerProxy;
    public static void hook(final OnActivityManagerHooker _hocker)
    {
        /*try
        {
            Object v = ReflectAccess.getValue("android.app.ActivityManagerNative", "gDefault");
            Class<?> ia = Class.forName("android.app.IActivityManager");
            if (v.getClass().isAssignableFrom(ia))
            {
                ReflectAccess.setValue("android.app.ActivityManagerNative", "gDefault", getActivityManagerProxy(v, ia, _hocker));
            }
            else
            {
                Object m = ReflectAccess.getValue(v, "mInstance");
                ReflectAccess.setValue(v, "mInstance", getActivityManagerProxy(m, ia, _hocker));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/

        try
        {
            Class<?> ia = Class.forName("android.app.IActivityManager");
            if (Build.VERSION.SDK_INT >= 26) {
                Object iamSingleton = ReflectAccess.getValue(Class.forName("android.app.ActivityManager"), "IActivityManagerSingleton");
                Object iamInstance = ReflectAccess.getValue(iamSingleton,"mInstance");
                if (iamInstance != activityManagerProxy) {
                    activityManagerProxy = getActivityManagerProxy(iamInstance, ia, _hocker);
                    ReflectAccess.setValue(iamSingleton, "mInstance", activityManagerProxy);
                }
            } else {
                Object v = ReflectAccess.getValue("android.app.ActivityManagerNative", "gDefault");
                if (v.getClass().isAssignableFrom(ia)) {
                    if (v != activityManagerProxy) {
                        activityManagerProxy = getActivityManagerProxy(v, ia, _hocker);
                        ReflectAccess.setValue("android.app.ActivityManagerNative", "gDefault", activityManagerProxy);
                    }
                } else {
                    Object m = ReflectAccess.getValue(v, "mInstance");
                    if (m != activityManagerProxy) {
                        activityManagerProxy = getActivityManagerProxy(m, ia, _hocker);
                        ReflectAccess.setValue(v, "mInstance", activityManagerProxy);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static Object getActivityManagerProxy(final Object host, Class<?> ia, final OnActivityManagerHooker _hocker)
    {
        return Proxy.newProxyInstance(Hooker.class.getClassLoader(), new Class<?>[]{ia}, new InvocationHandler()
        {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
            {
                if ("startActivity".equals(method.getName()))
                {
                    for (Object arg : args)
                    {
                        if (arg instanceof Intent)
                        {
                            Intent intent = (Intent) arg;

                            if (_hocker != null)
                            {
                                Handle handle = new Handle();
                                _hocker.onStartActivity(intent, handle);
                                if (handle.IsCancel){
                                    if (method.getReturnType().toString().startsWith("int")) {
                                        return 0;
                                    }else {
                                        return null;
                                    }
                                }
                            }

                            break;
                        }
                    }
                }

                return method.invoke(host, args);
            }
        });
    }
}
