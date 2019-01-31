package dataMapping;



import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class DataMapping
{
    public static DataMapping instance(DataFormat format)
    {
        DataMapping dataMapping = new TypeDataMapping();
        dataMapping.setCreator(format);
        return dataMapping;
    }

    //region Inner Class

    public interface DataFormat
    {
        Object createObject(Class<?> cls);

        DataObject createDataObject();

        Collection<Object> createDataList();

        InputStream toStream(DataSource source);
    }

    public interface DataSource
    {
        void dispose() throws DataException;

        Object toSource();
    }

    public static abstract class DataObject extends AbstractMap<String, Object> implements DataSource
    {
        public static class MapDataObject extends DataObject
        {
            private Map<String, Object> _current = new HashMap<String, Object>();

            public MapDataObject()
            {

            }

            public MapDataObject(Map<String, Object> map)
            {
                _current = map;
            }

            @Override
            public Set<Entry<String, Object>> entrySet()
            {
                return _current.entrySet();
            }

            @Override
            public void dispose() throws DataException
            {

            }

            @Override
            public Object toSource()
            {
                return _current;
            }

            @Override
            public String toString()
            {
                return _current.toString();
            }
        }

    }

    public static abstract class DataList extends AbstractList<Object> implements DataSource
    {
    }

    public static abstract class DataValue implements DataSource
    {
        @Override
        public void dispose() throws DataException
        {

        }

        public abstract Object getValue(Class<?> cls);

        public static class ObjectDataValue extends DataValue
        {
            private Object _value;

            public ObjectDataValue(Object value)
            {
                _value = value;
            }

            @Override
            public Object getValue(Class<?> cls)
            {
                return _value;
            }

            @Override
            public Object toSource()
            {
                return _value;
            }

        }
    }

    public static abstract class ClassDataFormat implements DataFormat
    {
        public Object createObject(Class<?> cls)
        {
            try
            {
                return cls.newInstance();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }

    public interface DataLog
    {
        void onMappingError(String msg, Exception e);
    }

    //endregion

    //region Inner

    private DataFormat _dataFormat = new ClassDataFormat()
    {
        @Override
        public DataObject createDataObject()
        {
            return new DataObject.MapDataObject();
        }

        @Override
        public Collection<Object> createDataList()
        {
            return new ArrayList<Object>();
        }

        @Override
        public InputStream toStream(DataSource source)
        {
            return new ByteArrayInputStream(source.toString().getBytes());
        }
    };

    private DataLog _dataEvent;

    public InputStream toStream(DataSource source)
    {
        return _dataFormat.toStream(source);
    }

    public DataFormat getCreator()
    {
        return _dataFormat;
    }

    public void setDataLog(DataLog event)
    {
        _dataEvent = event;
    }

    DataLog getDataLog()
    {
        return _dataEvent;
    }

    public void setCreator(DataFormat creator)
    {
        _dataFormat = creator;
    }

    //endregion

    //region To Method

    public Collection<Object> toDataList(List list) throws DataException
    {
        Collection<Object> source = getCreator().createDataList();

        for (Object item : list)
        {
            if (item == null)
                continue;
            if (Type.isSerializerType(item.getClass()))
                source.add(item);
            else if (Type.isSub(item.getClass(), List.class))
                source.add(toDataList((List) item));
            else if (Type.isSub(item.getClass(), Map.class))
                source.add(toDataObject((Map) item));
            else source.add(convert(item));
        }

        return source;
    }

    public Collection<Object> toDataArray(Object array) throws DataException
    {
        Collection<Object> source = getCreator().createDataList();
        int len = Array.getLength(array);
        Class<?> cls = array.getClass().getComponentType();
        for (int i = 0; i < len; i++)
        {
            Object item = Array.get(array, i);

            if (item == null)
                continue;
            if (Type.isSerializerType(item.getClass()))
                source.add(item);
            else if (Type.isSub(item.getClass(), List.class))
                source.add(toDataList((List) item));
            else if (Type.isSub(item.getClass(), Map.class))
                source.add(toDataObject((Map) item));
            else source.add(convert(item));
        }

        return source;
    }

    public DataObject toDataObject(Map list) throws DataException
    {
        DataObject source = getCreator().createDataObject();
        for (Object key : list.keySet())
        {
            if (key == null)
                continue;
            Object value = list.get(key);

            if (value == null)
                continue;

            if (Type.isSerializerType(value.getClass()))
                source.put(key + "", value);
            else if (Type.isSub(value.getClass(), List.class))
                source.put(key + "", toDataList((List) value));
            else if (Type.isSub(value.getClass(), Map.class))
                source.put(key + "", toDataObject((Map) value));
            else source.put(key + "", convert(value));
        }

        return source;
    }

    public DataSource toDataSource(Object value) throws DataException
    {
        if (value instanceof List)
        {
            return (DataSource) toDataList((List) value);
        }
        else if (value instanceof Map)
        {
            return toDataObject((Map) value);
        }
        else if (value.getClass().isArray())
        {
            return (DataSource) toDataArray(value);
        }
        else if (!Type.isSerializerType(value.getClass()))
        {
            return convert(value);
        }
        else
            return new DataValue.ObjectDataValue(value);
    }

    //endregion

    //region From Method

    protected <T> List<T> fromDataList(DataList source, String name, Class<T> cls) throws DataException
    {
        List<T> list = new ArrayList<T>();

        int index = 0;
        for (Object item : source)
        {
            if (item == null)
                continue;

            if (cls.equals(String.class))
                list.add((T) String.valueOf(convertTo(list.getClass(), index, cls, item)));
            else if (item instanceof DataValue)
                list.add((T) convertTo(list.getClass(), index, cls, ((DataValue) item).getValue(cls)));
            else if (!Type.isSerializerType(cls))
                list.add((T) convert((DataObject) convertTo(list.getClass(), index, cls, item), cls));
            else
                list.add((T) convertTo(list.getClass(), index, cls, item));
            index++;
        }

        return list;
    }

    protected Object fromDataArray(DataList source, String name, Class<?> cls) throws DataException
    {
        Object array = Array.newInstance(cls, source.size());

        int index = 0;
        for (Object item : source)
        {
            if (item == null)
                continue;

            if (cls.equals(String.class))
                Array.set(array, index, String.valueOf(convertTo(array.getClass(), index, cls, item)));
            else if (!Type.isSerializerType(cls))
                Array.set(array, index, convert((DataObject) convertTo(array.getClass(), index, cls, item), cls));
            else if (item instanceof DataValue)
                Array.set(array, index, convertTo(array.getClass(), index, cls, ((DataValue) item).getValue(cls)));
            else
                Array.set(array, index, convertTo(array.getClass(), index, cls, item));
            index++;
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    protected <T> Map<String, T> fromDataObject(DataObject source, String name, Class<?> cls) throws DataException
    {
        Map<String, T> list = new HashMap<String, T>();

        if (name == null)
            source = convertTo(list.getClass(), null, cls, source);

        for (String key : source.keySet())
        {
            Object value = source.get(key);
            if (value == null)
                continue;

            if (cls.equals(String.class))
                list.put(key, (T) String.valueOf(convertTo(list.getClass(), key, cls, value)));
            else if (!Type.isSerializerType(cls))
                list.put(key, (T) convert((DataObject) convertTo(list.getClass(), key, cls, value), cls));
            else if (value instanceof DataValue)
                list.put(key, (T) convertTo(list.getClass(), key, cls, ((DataValue) value).getValue(cls)));
            else list.put(key, (T) convertTo(list.getClass(), key, cls, value));
        }

        return list;
    }

    public <T> Map<String, T> fromDataObject(DataObject source, Class<?> cls) throws DataException
    {
        return fromDataObject(source, null, cls);
    }

    public <T> Collection<T> fromDataList(DataList source, Class<T> cls) throws DataException
    {
        return fromDataList(source, null, cls);
    }

    public Object fromDataArray(DataList source, Class<?> cls) throws DataException
    {
        return fromDataArray(source, null, cls);
    }

    public Object fromDataSource(DataSource source, Class<?> cls) throws DataException
    {
        if (source instanceof DataObject)
        {
            return convert((DataObject) source, cls);
        }
        else if (cls.isArray())
        {
            return fromDataArray((DataList) source, cls.getComponentType());
        }
        else if (source instanceof DataList)
        {
            return fromDataList((DataList) source, cls.getComponentType());
        }
        else if (source instanceof DataValue)
        {
            return ((DataValue) source).getValue(cls);
        }

        throw new DataException("not support newInstance");
    }

    //endregion

    //region Convert Method

    public abstract Object convert(DataObject source, Class<?> cls) throws DataException;

    public abstract DataSource convert(Object obj) throws DataException;

    public <T> T convertTo(Class<?> host, Object name, Class<?> propertyClass, Object value) throws DataException
    {
        return value instanceof DataValue ? (T) ((DataValue) value).getValue(propertyClass) : (T) value;
    }

    public <T> T convertFrom(Class<?> host, Object name, Class<?> propertyClass, Object value) throws DataException
    {
        return (T) value;
    }

    //endregion

    public static class TypeDataMapping extends DataMapping
    {
        public Object convert(DataObject source, Class<?> cls) throws DataException
        {
            Type type = Type.getType(cls);
            Object host = getCreator().createObject(cls);

            for (Type.Property property : type.getProperties())
            {
                if (!property.canSet())
                    continue;

                try
                {
                    setProperty(source, cls, host, property);
                }
                catch (Exception e)
                {
                    if (getDataLog() != null)
                        getDataLog().onMappingError("set property " + property.getName(), e);
                }
            }

            return host;
        }

        private void setProperty(DataObject source, Class<?> cls, Object host, Type.Property property) throws DataException
        {
            Object value = null;
            String name = property.getName();
            Class<?> propertyClass = property.getType();

            if (propertyClass.isArray())
            {
                DataList valueSource = convertTo(cls, name, propertyClass, source.get(name));
                if (valueSource != null)
                    value = fromDataArray(valueSource, name, propertyClass.getComponentType());
            }
            else if (Type.isSub(propertyClass, List.class))
            {
                DataList valueSource = convertTo(cls, name, propertyClass, source.get(name));
                if (valueSource != null)
                    value = fromDataList(valueSource, name, property.getGenericTypes()[0]);
            }
            else if (Type.isSub(property.getType(), Map.class))
            {
                DataObject valueSource = convertTo(cls, name, propertyClass, source.get(name));
                if (valueSource != null)
                    value = fromDataObject(valueSource, name, property.getGenericTypes()[1]);
            }
            else if (!Type.isSerializerType(propertyClass))
            {
                DataObject valueSource = convertTo(cls, name, propertyClass, source.get(name));
                if (valueSource != null)
                    value = convert(valueSource, propertyClass);
            }
            else if (propertyClass.equals(String.class))
            {
                value = convertTo(cls, name, propertyClass, source.get(name));
                if (value != null)
                    value = String.valueOf(value);
            }
            else
            {
                value = convertTo(cls, name, propertyClass, source.get(name));

                if (value instanceof DataValue)
                {
                    value = ((DataValue) value).getValue(propertyClass);
                }
            }

            property.set(host, value);
        }

        // obj  - javabean
        @Override
        public DataSource convert(Object obj) throws DataException
        {
            if (obj instanceof List)
                return (DataSource) toDataList((List) obj);
            else if (obj.getClass().isArray())
                return (DataSource) toDataArray(obj);
            else if (Type.isSerializerType(obj.getClass()))
                return new DataSource()
                {
                    @Override
                    public void dispose() throws DataException
                    {

                    }

                    @Override
                    public Object toSource()
                    {
                        return obj;
                    }

                    @Override
                    public String toString()
                    {
                        return obj.toString();
                    }
                };

            DataObject source = getCreator().createDataObject();

            Type type = Type.getType(obj);
            for (Type.Property property : type.getProperties())
            {
                setDataSource(obj, source, property);
            }

            return source;
        }

        private void setDataSource(Object obj, DataObject source, Type.Property property) throws DataException
        {
            if (!property.canGet())
                return;

            Object value;
            String name = property.getName();
            Object temp = convertFrom(obj.getClass(), name, property.getType(), property.get(obj));

            if (temp == null)
                return;

            Class<?> propertyClass = temp.getClass();

            if (propertyClass.isArray())
            {
                value = toDataArray(temp);
            }
            else if (Type.isSub(propertyClass, List.class))
            {
                value = toDataList((List) temp);
            }
            else if (Type.isSub(propertyClass, Map.class))
            {
                value = toDataObject((Map) temp);
            }
            else if (!Type.isSerializerType(propertyClass))
            {
                value = convert(temp);
            }
            else
            {
                value = temp;
            }

            source.put(name, value);
        }
    }
}
