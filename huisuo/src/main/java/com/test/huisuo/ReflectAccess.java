package com.test.huisuo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by anye6488 on 2016/6/1.
 */
public class ReflectAccess
{
    public static class ReflectMethod
    {
        private Method _method;

        public ReflectMethod(Method method)
        {
            _method = method;
        }

        public ReflectMethod setAccessible(boolean flag)
        {
            _method.setAccessible(flag);
            return this;
        }

        public Object invoke(Object host, Object... args) throws InvocationTargetException, IllegalAccessException
        {
            return _method.invoke(host, args);
        }

        public Object invokeNoException(Object host, Object... args)
        {
            try
            {
                return invoke(host, args);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static class ReflectField
    {
        private Field _field;

        public ReflectField(Field field)
        {
            _field = field;
        }

        public ReflectField setAccessible(boolean flag)
        {
            _field.setAccessible(flag);
            return this;
        }

        public void set(Object host, Object arg) throws InvocationTargetException, IllegalAccessException
        {
            _field.set(host, arg);
        }

        public Object get(Object host) throws InvocationTargetException, IllegalAccessException
        {
            return _field.get(host);
        }
    }

    public static ReflectField getField(Class<?> cls, String name) throws NoSuchFieldException
    {
        return new ReflectField(getInnerField(cls, name));
    }

    public static Field getInnerField(Class<?> obj, String name)
    {
        Class<?> cls = obj;
        while (true)
        {
            if (cls == null)
                return null;

            try
            {
                return cls.getDeclaredField(name);
            }
            catch (Exception e)
            {
                cls = cls.getSuperclass();
            }
        }
    }

    public static <T> T getValue(String host, String name)
    {
        try
        {
            return (T) getField(Class.forName(host), name).setAccessible(true).get(null);
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T getValue(Object host, String name)
    {
        try
        {
            return (T) getField(host.getClass(), name).setAccessible(true).get(host);
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T getValue(Class<?> host, String name)
    {
        try
        {
            return (T) getField(host, name).setAccessible(true).get(null);
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static void setValue(Class<?> host, String name, Object value)
    {
        try
        {
            getField(host, name).setAccessible(true).set(null, value);
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void setValue(Object host, String name, Object value)
    {
        try
        {
            getField(host.getClass(), name).setAccessible(true).set(host, value);
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static boolean setValueAll(Object host, String name, Object value)
    {
        Class<?> cls = host.getClass();
        boolean _isSet = false;

        while (cls != null)
        {
            try
            {
                getField(cls, name).setAccessible(true).set(host, value);
                _isSet = true;
            }
            catch (Exception e)
            {

            }

            cls = cls.getSuperclass();
        }

        return _isSet;
    }

    public static <T> T invoke(Class<?> cls, String name, Class<?>[] clsArray, Object host, Object... pArray) throws NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException
    {
        Method method = cls.getDeclaredMethod(name, clsArray);
        method.setAccessible(true);
        return (T) method.invoke(host, pArray);
    }

    public static <T> T invoke(Object host, String name, Class<?>[] clsArray, Object... pArray) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException
    {
        if (host == null)
            throw new NullPointerException("host is null");

        Class<?> cls = host.getClass();
        return invoke(cls, name, clsArray, host, pArray);
    }

    public static <T> T staticInvoke(String calssName, String method, Class<?>[] clsArray, Object... pArray) throws ClassNotFoundException, IllegalArgumentException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        Class<?> cls = Class.forName(calssName);
        return staticInvoke(cls, method, clsArray, pArray);
    }


    public static <T> T staticInvoke(Class<?> cls, String name, Class<?>[] clsArray, Object... pArray) throws NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException
    {
        return invoke(cls, name, clsArray, null, pArray);
    }
}
