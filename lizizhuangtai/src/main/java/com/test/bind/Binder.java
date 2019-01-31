package com.test.bind;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fj on 2018/8/28.
 */

public class Binder {
    private Map<String, Event> eventLists = new HashMap();
    private HashMap<String, Method> mMethodHashMap = new HashMap();
    private HashMap<Integer, Object> mViews = new HashMap();
    private Activity activity;
    private static Map<String, Binder> Binders = new HashMap<>();
    private Binder(Activity activity){
        this.activity = activity;
    }

    public static Binder get(Activity activity){
        if (Binders.size() > 0 && Binders.get(activity.getClass().getName()) != null) {
            return Binders.get(activity.getClass().getName());
        }else {
            Binder binder = new Binder(activity);
            Binders.put(activity.getClass().getName(), binder);
            return binder;
        }
    }

    public void build() {
        addEvent("onClick", new Event() {
            @Override
            protected void init(Object[] objects) {
                try {
                    for (Object object : objects) {
                        if (object instanceof View) {
                            View view = (View) object;
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Method realMethod = getRealMethod("onClick");
                                    if (null != realMethod) {
                                        try {
                                            realMethod.invoke(activity, v);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        addEvent("onLongClick", new Event() {
            @Override
            protected void init(Object[] objects) {
                try {
                    for (Object object : objects) {
                        if (object instanceof View) {
                            View view = (View) object;
                            view.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    Method method = getRealMethod("onLongClick");
                                    if (null != method){
                                        try {
                                            return (boolean) method.invoke(activity, v);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    return false;
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        addEvent("onEditor", new Event() {
            @Override
            protected void init(Object[] objects) {
                try {
                    for (Object object : objects) {
                        if (object instanceof EditText) {
                            EditText edit = (EditText) object;
                            edit.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    // 输入的内容变化的监听
//                                    Log.e("sdk", "文字变化 "+s);
                                }

                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    // 输入前的监听
//                                    Log.e("sdk", "开始输入 "+s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    // 输入后的监听
//                                    Log.e("sdk", "输入结束 "+s);
                                    Method method = getRealMethod("onEditor");
                                    if (null != method){
                                        try {
                                            method.invoke(activity, s);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        injectView();
        injectEvent();

    }

    public Binder addEvent(String type, Event event){
        eventLists.put(type, event);
        return this;
    }

    private Event getEvent(String type){
        return eventLists.get(type);
    }

    private void addRealMethod(String simpleName, Method method){
        mMethodHashMap.put(simpleName, method);
    }

    public Method getRealMethod(String simpleName){
        return mMethodHashMap.get(simpleName);
    }
    public void addViews(int id, Object view){
        mViews.put(id, view);
    }

    public Object getView(int id){
        return mViews.get(id);
    }

    public Activity getActivity(){
        return this.activity;
    }

    public abstract static class Event{
        protected abstract void init(Object[] objects);
    }

    private void injectView() {
        if (null == activity)
            return;

        Class<? extends Activity> activityClass = activity.getClass();
        Field[] declaredFields = activityClass.getDeclaredFields();
        for (Field field : declaredFields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                String simpleName = annotation.annotationType().getSimpleName();

                try {
                    Object invoke = Reflect.invokeDM(annotation, "value", null, new Object[]{});

                    Object view = null;
                    if (invoke instanceof Integer) {
                        try {
                            int id = (int) invoke;

                            view = getView(id);
                            if (view == null) {
                                view = Reflect.invokeM(activity, "findViewById", new Class<?>[]{int.class}, id);
                                addViews(id,view);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                            view = invoke;
                        }
                    }else {
                        view = invoke;
                    }

                    field.setAccessible(true);
                    field.set(activity,view);

                    Object[] views = new Object[]{view};

                    if (simpleName != null && getEvent(simpleName) != null) {
                        getEvent(simpleName).init(views);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void injectEvent() {
        if (null == activity)
            return;

        Class<? extends Activity> activityClass = activity.getClass();
        Method[] declaredMethods = activityClass.getDeclaredMethods();

        for (Method method : declaredMethods) {

            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                String simpleName = annotation.annotationType().getSimpleName();
                if (simpleName != null && getEvent(simpleName) != null) {
                    addRealMethod(simpleName, method);
                    try {
                        Object invoke = Reflect.invokeDM(annotation, "value", null, new Object[]{});

                        int[] ids = (int[]) invoke;
                        if (ids.length<=0) {
                            return;
                        }

                        Object[] objects = new Object[ids.length];
                        int i = 0;
                        for (int id : ids) {
                            Object invoke1 = getView(id);
                            if (invoke1 == null) {
                                invoke1 = Reflect.invokeM(activity, "findViewById", new Class<?>[]{int.class}, id);
                                addViews(id,invoke1);
                            }

                            objects[i++] = invoke1;
                        }
                        getEvent(simpleName).init(objects);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
