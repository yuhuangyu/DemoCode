package com.test.bind;

import android.app.Activity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by ASUS on 2018/5/25.
 *
 */

public class Observable<T> {

    private Activity activity;
    public Observable(Activity activity) {
        this.activity = activity;
    }

    public void notifyObserve(T t, String methodName) {
        try {
            bindBean(t, methodName, "BindBean");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void notifyObserve(T t, String methodName, String BinderName) {
        try {
            bindBean(t, methodName, BinderName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bindBean(T bean, String methodName, String binder) throws Exception {
        if (binder == null) {
            return;
        }
        Class<?> aClass = bean.getClass();

        Method method = aClass.getDeclaredMethod(methodName, new Class<?>[]{});
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            String simpleName = annotation.annotationType().getSimpleName();
            if (simpleName != null && binder.equals(simpleName)) {

                Object invoke = Reflect.invokeDM(annotation, "value", null, new Object[]{});
                int[] ids = (int[]) invoke;
                if (ids.length<=0) {
                    return;
                }

                Object invoke2 = Reflect.invokeDM(annotation, "method", null, new Object[]{});
                String type = (String) invoke2;

                Object invoke3 = Reflect.invokeDM(annotation, "parameter", null, new Object[]{});
                Class<?> aClass1 = (Class<?>) invoke3;

                for (int id : ids) {
                    Object invoke4 = Binder.get(activity).getView(id);
                    if (invoke4 == null) {
                        invoke4 = Reflect.invokeM(activity, "findViewById", new Class<?>[]{int.class}, id);
                        Binder.get(activity).addViews(id,invoke4);
                    }

                    Object invoke5 = method.invoke(bean, new Object[]{});
                    if (invoke5 == null) {
                        return;
                    }

                    Reflect.invokeM(invoke4, type, new Class<?>[]{aClass1}, invoke5);
                }
            }
        }
    }
}
