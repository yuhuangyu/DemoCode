package com.test.bind2;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.test.bind.EventType;
import com.test.bind.InjectString;
import com.test.bind.InjectView;
import com.test.bind.onClick;
import com.test.bind.onLongClick;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by fj on 2018/8/28.
 */

public class Bind {

    public static void init(Activity activity) {
        injectView(activity);
        injectEvent(activity);
    }

    private static void injectView(Activity activity) {
        if (null == activity)
            return;

        Class<? extends Activity> activityClass = activity.getClass();
        Field[] declaredFields = activityClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(InjectView.class)) {
                InjectView annotation = field.getAnnotation(InjectView.class);
                int value = annotation.value();
                try {
                    Method findViewByIdMethod = activityClass.getMethod("findViewById", int.class);
                    findViewByIdMethod.setAccessible(true);
                    Object invoke = findViewByIdMethod.invoke(activity, value);

                    field.setAccessible(true);
                    field.set(activity,invoke);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (field.isAnnotationPresent(InjectString.class)) {
                InjectString annotation = field.getAnnotation(InjectString.class);
                String value = annotation.value();

                try {
                    field.setAccessible(true);
                    field.set(activity,value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void injectEvent(Activity activity) {
        if (null == activity)
            return;

        Class<? extends Activity> activityClass = activity.getClass();
        Method[] declaredMethods = activityClass.getDeclaredMethods();

        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(onClick.class) || method.isAnnotationPresent(onLongClick.class)) {
                Log.i("sdk", method.getName());
                Class listenerType = null;
                String listenerSetter = null;
                String methodName = null;
                int[] value = null;
                if (method.isAnnotationPresent(onClick.class)) {
                    onClick annotation = method.getAnnotation(onClick.class);
                    value = annotation.value();

                    EventType eventType = annotation.annotationType().getAnnotation(EventType.class);
                    listenerType = eventType.listenerType();
                    listenerSetter = eventType.listenerSetter();
                    methodName = eventType.methodName();

                }else {
                    onLongClick annotation = method.getAnnotation(onLongClick.class);
                    value = annotation.value();

                    EventType eventType = annotation.annotationType().getAnnotation(EventType.class);
                    listenerType = eventType.listenerType();
                    listenerSetter = eventType.listenerSetter();
                    methodName = eventType.methodName();
                }

                ProxyHandler proxyHandler = new ProxyHandler(activity);
                Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, proxyHandler);

                proxyHandler.mapMethod(methodName, method);
                try {
                    for (int id : value) {
                        Method findViewByIdMethod = activityClass.getMethod("findViewById", int.class);
                        findViewByIdMethod.setAccessible(true);
                        View btn = (View) findViewByIdMethod.invoke(activity, id);

                        Method listenerSetMethod = btn.getClass().getMethod(listenerSetter, listenerType);
                        listenerSetMethod.setAccessible(true);
                        listenerSetMethod.invoke(btn, listener);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
