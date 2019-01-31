package com.api.notification;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anye6488 on 2016/6/1.
 */
public class Reflect
{
    public interface FieldFilter
    {
        boolean accept(Field field);
    }

    public static class ReflectMethod
    {
        private Method _method;

        public ReflectMethod(Method method)
        {
            _method = method;
        }

        public ReflectMethod accessible(boolean flag)
        {
            _method.setAccessible(flag);
            return this;
        }

        public Object call(Object host, Object... args) throws InvocationTargetException, IllegalAccessException
        {
            return _method.invoke(host, args);
        }

        public Object invokeNoException(Object host, Object... args)
        {
            try
            {
                return call(host, args);
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

    public static ReflectMethod method(Class<?> cls, String name, Class<?>... args) throws NoSuchMethodException
    {
        return new ReflectMethod(getInnerMethod(cls, name, args));
    }

    public static ReflectMethod method(Object host, String name, Class<?>... args) throws NoSuchMethodException
    {
        return new ReflectMethod(getInnerMethod(host.getClass(), name, args));
    }

    public static ReflectMethod method(String host, String name, Class<?>... args) throws NoSuchMethodException, ClassNotFoundException
    {
        return new ReflectMethod(getInnerMethod(Class.forName(host), name, args));
    }

    public static ReflectField field(Class<?> cls, String name) throws NoSuchFieldException
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

    public static Method getInnerMethod(Class<?> obj, String name, Class<?>... clsArray)
    {
        Class<?> cls = obj;
        while (true)
        {
            if (cls == null)
                return null;

            try
            {
                return cls.getDeclaredMethod(name, clsArray);
            }
            catch (Exception e)
            {
                cls = cls.getSuperclass();
            }
        }
    }

    public static <T> T get(Object host, String name)
    {
        try
        {
            return (T) field(host.getClass(), name).setAccessible(true).get(host);
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

    public static <T> T get(String host, String name)
    {
        try
        {
            return (T) field(Class.forName(host), name).setAccessible(true).get(host);
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

    public static <T> T get(Class<?> host, String name)
    {
        try
        {
            return (T) field(host, name).setAccessible(true).get(null);
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

    public static void set(Class<?> host, String name, Object value)
    {
        try
        {
            field(host, name).setAccessible(true).set(null, value);
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

    public static void set(String host, String name, Object value)
    {
        try
        {
            field(Class.forName(host), name).setAccessible(true).set(null, value);
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

    public static void set(Object host, String name, Object value)
    {
        try
        {
            field(host.getClass(), name).setAccessible(true).set(host, value);
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

    public static boolean setAll(Object host, String name, Object value)
    {
        Class<?> cls = host.getClass();
        boolean _isSet = false;

        while (cls != null)
        {
            try
            {
                field(cls, name).setAccessible(true).set(host, value);
                _isSet = true;
            }
            catch (Exception e)
            {

            }

            cls = cls.getSuperclass();
        }

        return _isSet;
    }

    public static Iterable<Field> findFields(Class<?> cls, final FieldFilter filter)
    {
        List<Field> fileds = new ArrayList<Field>();
        for (Field field : getFields(cls))
        {
            if (filter == null || filter.accept(field))
                fileds.add(field);
        }

        return fileds;
    }

    private static Iterable<Field> getFields(Class<?> cls)
    {
        List<Field> fields = new ArrayList<Field>();
        Class<?> clazz = cls;

        while (clazz != null)
        {
            for (Field field : clazz.getDeclaredFields())
            {
                fields.add(field);
            }

            clazz = clazz.getSuperclass();
        }

        return fields;
    }
}
