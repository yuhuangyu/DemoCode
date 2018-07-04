package com.test.markdemo.utils;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class ActivityHooker {
    private static final String TAG = "Sdk ActivityHooker";
    private static OnActivityHooker hooker;
    private static OnActivityLifeHooker lifeHooker;

    public static void hock(final Context context) {
        try {
            Object o = ReflectAccess.staticInvoke("android.app.ActivityThread", "currentActivityThread", new Class[0]);
            ReflectAccess.setValue(o, "mInstrumentation", new Instrumentation() {
                @Override
                public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
                    return super.newActivity(cl, className, intent);
                }

                @Override
                public void callActivityOnResume(final Activity activity) {
                    super.callActivityOnResume(activity);
                }

                @Override
                public void callActivityOnStop(Activity activity) {
                    super.callActivityOnStop(activity);
                }

                @Override
                public void callActivityOnPause(Activity activity) {
                    super.callActivityOnPause(activity);
                }

                @Override
                public void callActivityOnCreate(Activity activity, Bundle icicle) {
                    onCreateBefore(context, activity);
                    super.callActivityOnCreate(activity, icicle);
                    onCreateAfter(context, activity);
                }

                @Override
                public boolean onException(Object obj, Throwable e) {
                    return super.onException(obj, e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addHooker(OnActivityHooker onActivityHooker) {
        hooker = onActivityHooker;
    }
    public static void addLifeHooker(OnActivityLifeHooker activityLifeHooker) {
        lifeHooker = activityLifeHooker;
    }
    private static Object lifeHookInstrumentation;
    public static void lifeHock(final Context context) {
        try {
            Object o = ReflectAccess.staticInvoke("android.app.ActivityThread", "currentActivityThread", new Class[0]);
            if (o != lifeHookInstrumentation) {
                lifeHookInstrumentation = new Instrumentation() {
                    @Override
                    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
                        return super.newActivity(cl, className, intent);
                    }

                    @Override
                    public void callActivityOnResume(final Activity activity) {
                        super.callActivityOnResume(activity);
                        if (lifeHooker != null) {
                            lifeHooker.onResume(context, activity);
                        }
                    }

                    @Override
                    public void callActivityOnStop(Activity activity) {
                        super.callActivityOnStop(activity);
                        if (lifeHooker != null) {
                            lifeHooker.onStop(context, activity);
                        }
                    }

                    @Override
                    public void callActivityOnPause(Activity activity) {
                        super.callActivityOnPause(activity);
                        if (lifeHooker != null) {
                            lifeHooker.onPause(context, activity);
                        }
                    }

                    @Override
                    public void callActivityOnCreate(Activity activity, Bundle icicle) {
                        if (lifeHooker != null) {
                            lifeHooker.onCreateBefore(context, activity);
                        }
                        super.callActivityOnCreate(activity, icicle);
                        if (lifeHooker != null) {
                            lifeHooker.onCreateAfter(context, activity);
                        }
                    }

                    @Override
                    public void callActivityOnDestroy(Activity activity) {
                        super.callActivityOnDestroy(activity);
                        if (lifeHooker != null) {
                            lifeHooker.onDestroy(context, activity);
                        }
                    }

                    @Override
                    public void callActivityOnStart(Activity activity) {
                        super.callActivityOnStart(activity);
                        if (lifeHooker != null) {
                            lifeHooker.onStart(context, activity);
                        }
                    }

                    @Override
                    public boolean onException(Object obj, Throwable e) {
                        if (e.toString().contains("com.core.PackageAssist$VPackageContext cannot be cast to android.app.ContextImpl")) {
                            return true;
                        }
                        return super.onException(obj, e);
                    }
                };
                ReflectAccess.setValue(o, "mInstrumentation", lifeHookInstrumentation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void onCreateBefore(Context context, Activity activity) {

        if (hooker != null) {
            hooker.onCreateBefore(context, activity);
        }
    }

    private static void onCreateAfter(Context context, Activity activity) {
        if (hooker != null) {
            hooker.onCreateAfter(context, activity);
        }
    }

    public interface OnActivityHooker {
        void onCreateBefore(Context context, Activity activity);

        void onCreateAfter(Context context, Activity activity);
    }

    public interface OnActivityLifeHooker {
        void onCreateBefore(Context context, Activity activity);

        void onCreateAfter(Context context, Activity activity);
        void onStart(Context context, Activity activity);
        void onResume(Context context, Activity activity);
        void onPause(Context context, Activity activity);
        void onStop(Context context, Activity activity);
        void onDestroy(Context context, Activity activity);
    }
}
