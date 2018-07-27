package com.datastorage;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Safe
{
    public interface ObjectAccess
    {
        Object onArray(Object host, int index);

        Object onObject(Object host, String name);
    }

    public final static class ArrayAccess
    {
        private Object[] _array;
        private int _index = 0;

        public ArrayAccess(Object[] array)
        {
            _array = array;
        }

        public <T> T get()
        {
            return Safe.get(_array, _index++);
        }

        public <T> T get(T def)
        {
            return Safe.get(_array, _index++, def);
        }
    }

    public final static class MapBuilder<TKey, TValue>
    {
        private Collection<Map.Entry<TKey, TValue>> _list;

        public MapBuilder()
        {
            _list = new ArrayList<>();
        }

        public MapBuilder put(final TKey key, final TValue value)
        {
            _list.add(new Map.Entry<TKey, TValue>()
            {
                @Override
                public TKey getKey()
                {
                    return key;
                }

                @Override
                public TValue getValue()
                {
                    return value;
                }

                @Override
                public TValue setValue(TValue tValue)
                {
                    return tValue;
                }
            });

            return this;
        }

        public Map<TKey, TValue> hash()
        {
            Map<TKey, TValue> map = hashMap();
            for (Map.Entry<TKey, TValue> entry : _list)
            {
                map.put(entry.getKey(), entry.getValue());
            }

            return map;
        }

        public Map<TKey, TValue> linked()
        {
            Map<TKey, TValue> map = new LinkedHashMap<>();
            for (Map.Entry<TKey, TValue> entry : _list)
            {
                map.put(entry.getKey(), entry.getValue());
            }

            return map;
        }
    }

    public static ArrayAccess access(Object... array)
    {
        return new ArrayAccess(array);
    }

    public static ArrayAccess accessAll(Object... array)
    {
        List<Object> list = new ArrayList<Object>();
        for (Object object : array)
        {
            if (object != null && object.getClass().isArray())
            {
                int len = Array.getLength(object);

                for (int i = 0; i < len; i++)
                    list.add(Array.get(object, i));
            }
            else list.add(object);
        }

        return new ArrayAccess(list.toArray());
    }


    public static <TElement> TElement get(Object[] array, int index, TElement def)
    {
        return (TElement) (array.length > index && array[index] != null ? array[index] : def);
    }

    public static <TElement> TElement get(Object[] array, int index)
    {
        return (TElement) (array.length > index && array[index] != null ? array[index] : null);
    }

    public static <TKey, TElement> TElement get(Map<TKey, Object> map, TKey key, TElement def)
    {
        if (map.containsKey(key))
        {
            Object value = map.get(key);
            if (value != null)
                return (TElement) value;
        }

        return def;
    }

    public static <TKey, TElement> TElement value(Map<TKey, TElement> map, TKey key, TElement def)
    {
        if (map.containsKey(key))
        {
            TElement value = map.get(key);
            if (value != null)
                return value;
        }

        return def;
    }

    public static <TValue> List<TValue> asList(TValue... values)
    {
        return Arrays.asList(values);
    }

    public static int parseInt(String value, int def)
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            return def;
        }
    }

    public static <TKey, TValue> void removeSetItem(Map<TKey, Set<TValue>> map, TKey key, TValue value)
    {
        if (map.containsKey(key))
        {
            Set<TValue> set = map.get(key);
            set.remove(value);

            if (set.size() == 0)
                map.remove(key);
        }
    }

    public static <TKey, TValue> void addSetItem(Map<TKey, Set<TValue>> map, TKey key, TValue value)
    {
        Set<TValue> set;
        if (map.containsKey(key))
            set = map.get(key);
        else
        {
            set = new HashSet<TValue>();
            map.put(key, set);
        }

        set.add(value);
    }

    public static <TKey, TValue> void removeListItem(Map<TKey, List<TValue>> map, TKey key, TValue value)
    {
        if (map.containsKey(key))
        {
            List<TValue> set = map.get(key);
            set.remove(value);

            if (set.size() == 0)
                map.remove(key);
        }
    }

    public static <TKey, TValue> void addListItem(Map<TKey, List<TValue>> map, TKey key, TValue value)
    {
        List<TValue> set;
        if (map.containsKey(key))
            set = map.get(key);
        else
        {
            set = new ArrayList<TValue>();
            map.put(key, set);
        }

        set.add(value);
    }

    public static <TName, TKey, TValue> void removeMapItem(Map<TName, Map<TKey, TValue>> map, TName name, TKey key, TValue value)
    {
        if (map.containsKey(key))
        {
            Map<TKey, TValue> set = map.get(name);
            set.remove(value);

            if (set.size() == 0)
                map.remove(key);
        }
    }

    public static <TName, TKey, TValue> void addMapItem(Map<TName, Map<TKey, TValue>> map, TName name, TKey key, TValue value)
    {
        Map<TKey, TValue> set;
        if (map.containsKey(name))
            set = map.get(name);
        else
        {
            set = new HashMap<TKey, TValue>();
            map.put(name, set);
        }

        set.put(key, value);
    }

    public static <TKey, TValue> Map<TValue, List<Map<TKey, TValue>>> group(List<Map<TKey, TValue>> list, TKey id)
    {
        Map<TValue, List<Map<TKey, TValue>>> map = new HashMap<TValue, List<Map<TKey, TValue>>>();

        for (Map<TKey, TValue> values : list)
        {
            Safe.addListItem(map, values.get(id), values);
        }

        return map;
    }

    public static <T> Iterable<T> iterable(final Iterator<T> iterator)
    {
        return () -> iterator;
    }

    public static <TKey, TValue> MapBuilder<TKey, TValue> map()
    {
        return new MapBuilder<>();
    }

    public static <TKey, TValue> Map<TKey, TValue> hashMap()
    {
        return new HashMap<>();
    }

    public static <TKey, TValue> Map<TKey, TValue> linkedMap(int capacity)
    {
        return new HashMap<>(capacity);
    }

    public static <TValue> Set<TValue> hashSet(TValue... values)
    {
        return new HashSet<>(Arrays.asList(values));
    }

    public static Object access(Object host, String path, ObjectAccess access)
    {
        String[] values = path.split("\\.");

        for (String value : values)
        {
            String[] vls = value.split("[\\[\\]]");

            for (String v : vls)
            {
                if (host == null)
                    return null;

                if (v.charAt(0) >= '0' && v.charAt(0) <= '9')
                {
                    try
                    {
                        int index = Integer.parseInt(v);
                        if (access != null)
                            host = access.onArray(host, index);
                    }
                    catch (NumberFormatException e)
                    {
                        if (access != null)
                            host = access.onObject(host, v);
                    }
                }
                else
                {
                    if (access != null)
                        host = access.onObject(host, v);
                }
            }
        }

        return host;
    }

    public static Object access(Object host, String path)
    {
        return access(host, path, new ObjectAccess()
        {
            @Override
            public Object onObject(Object host, String name)
            {
                if (host instanceof Map)
                {
                    return ((Map) host).get(name);
                }
                else
                {
                    Type type = Type.getType(host);
                    Type.Property property = type.getProerty(name);
                    if (property != null)
                        return property.get(host);

                    return null;
                }
            }

            @Override
            public Object onArray(Object host, int index)
            {
                if (host instanceof List)
                {
                    return ((List) host).get(index);
                }
                else if (host.getClass().isArray())
                {
                    return Array.get(host, index);
                }

                return null;
            }
        });
    }
}
