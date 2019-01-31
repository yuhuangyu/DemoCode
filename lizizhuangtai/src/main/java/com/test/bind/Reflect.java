package com.test.bind;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by fj on 2018/9/3.
 */

public class Reflect {
    public static Object invokeM(Object host, String name, Class<?>[] clsArray, Object... pArray) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method1 = host.getClass().getMethod(name,clsArray);
        method1.setAccessible(true);
        return method1.invoke(host, pArray);
    }

    public static Object invokeDM(Object host, String name, Class<?>[] clsArray, Object... pArray) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method1 = host.getClass().getDeclaredMethod(name,clsArray);
        method1.setAccessible(true);
        return method1.invoke(host, pArray);
    }
}
