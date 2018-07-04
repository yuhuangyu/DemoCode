package com.test.huisuo;

import android.app.IServiceConnection;
import android.content.Intent;

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
        void onBindService(Intent intent, IServiceConnection conn);
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
            public void onBindService(Intent intent, IServiceConnection conn) {
                for (OnActivityManagerHooker hooker : _list)
                {
                    hooker.onBindService(intent, conn);
                }
            }


        });
    }

    public static void hook(final OnActivityManagerHooker _hocker)
    {
        try
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
        }
    }

    private static Object getActivityManagerProxy(final Object host, Class<?> ia, final OnActivityManagerHooker _hocker)
    {
        return Proxy.newProxyInstance(Hooker.class.getClassLoader(), new Class<?>[]{ia}, new InvocationHandler()
        {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
            {
                if ("bindService".equals(method.getName())) {
                    Intent intent = null;
                    IServiceConnection conn2 = null;
                    for (Object arg : args)
                    {
                        if (arg instanceof Intent)
                        {
                            intent = (Intent) arg;
                        }else if (arg instanceof IServiceConnection) {
                            conn2 = (IServiceConnection) arg;
                        }
                    }

                    if (_hocker != null && intent != null && conn2 != null) {
                        _hocker.onBindService(intent, conn2);
                    }
                }

                return method.invoke(host, args);
            }
        });
    }
}

