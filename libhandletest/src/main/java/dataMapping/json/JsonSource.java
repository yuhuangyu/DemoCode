package dataMapping.json;



import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import dataMapping.DataException;
import dataMapping.DataMapping;

public class JsonSource
{
    public static DataMapping.DataObject object(String text) throws JSONException
    {
        return new JsonDataObject(new JSONObject(text));
    }

    public static InputStream toStream(DataMapping.DataSource source)
    {
        return new ByteArrayInputStream(source.toString().getBytes());
    }

    public static DataMapping.DataSource from(Object json)
    {
        if (json instanceof JSONObject)
            return new JsonDataObject((JSONObject) json);
        else if (json instanceof JSONArray)
            return new JsonList((JSONArray) json);
        else return new DataMapping.DataValue.ObjectDataValue(json);
    }

    public static Collection<Object> array(String text) throws JSONException
    {
        return new JsonList(new JSONArray(text));
    }

    public static DataMapping.DataFormat format()
    {
        return new DataMapping.ClassDataFormat()
        {
            @Override
            public DataMapping.DataObject createDataObject()
            {
                return new JsonDataObject();
            }

            @Override
            public Collection<Object> createDataList()
            {
                return new JsonList();
            }

            @Override
            public InputStream toStream(DataMapping.DataSource source)
            {
                return new ByteArrayInputStream(source.toString().getBytes());
            }
        };
    }

    public static class JsonList extends DataMapping.DataList
    {
        public JSONArray _array;

        public JsonList()
        {
            _array = new JSONArray();
        }

        public JsonList(JSONArray array)
        {
            _array = array;
        }

        @Override
        public Object get(int i)
        {
            if (i < 0 || i >= _array.length())
                throw new IndexOutOfBoundsException();

            Object value = _array.opt(i);
            if (value instanceof JSONObject)
            {
                return new JsonDataObject((JSONObject) value);
            }
            else if (value instanceof JSONArray)
            {
                return new JsonList((JSONArray) value);
            }
            else
                return value;
        }

        @Override
        public boolean add(Object object)
        {
            if (object instanceof DataMapping.DataSource)
                _array.put(((DataMapping.DataSource) object).toSource());
            else
                _array.put(object);
            return true;
        }

        @Override
        public int size()
        {
            return _array.length();
        }

        @Override
        public void dispose() throws DataException
        {

        }

        @Override
        public Object toSource()
        {
            return _array;
        }

        @Override
        public String toString()
        {
            if (_array != null)
                return _array.toString();
            return "[]";
        }
    }

    public static class JsonDataObject extends DataMapping.DataObject
    {
        private JSONObject _json;
        private Set<Map.Entry<String, Object>> _entrySet;

        public JsonDataObject(JSONObject json)
        {
            _json = json;
        }

        public JsonDataObject()
        {
            _json = new JSONObject();
        }

        public Object get(String name)
        {
            Object value = _json.opt(String.valueOf(name));

            if (value instanceof JSONArray)
                return new JsonList((JSONArray) value);
            else if (value instanceof JSONObject)
                return new JsonDataObject((JSONObject) value);
            else
                return value;
        }


        public Object put(String name, Object value)
        {
            try
            {
                if (value instanceof DataMapping.DataSource)
                {
                    _json.put(name, ((DataMapping.DataSource) value).toSource());
                }
                else
                {
                    _json.put(name, value);
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public Set<Map.Entry<String, Object>> entrySet()
        {
            if (_entrySet == null)
                _entrySet = new AbstractSet<Map.Entry<String, Object>>()
                {
                    @Override
                    public Iterator<Map.Entry<String, Object>> iterator()
                    {
                        final Iterator<String> keys = _json.keys();
                        return new Iterator<Map.Entry<String, Object>>()
                        {
                            @Override
                            public boolean hasNext()
                            {
                                return keys.hasNext();
                            }

                            @Override
                            public Map.Entry<String, Object> next()
                            {
                                final String key = keys.next();
                                return new Map.Entry<String, Object>()
                                {
                                    @Override
                                    public String getKey()
                                    {
                                        return key;
                                    }

                                    @Override
                                    public Object getValue()
                                    {
                                        return JsonDataObject.this.get(key);
                                    }

                                    @Override
                                    public Object setValue(Object o)
                                    {
                                        return put(key, o);
                                    }
                                };
                            }

                            @Override
                            public void remove()
                            {
                                keys.remove();
                            }
                        };
                    }

                    @Override
                    public int size()
                    {
                        return _json.length();
                    }
                };

            return _entrySet;
        }

        @Override
        public void dispose() throws DataException
        {

        }

        @Override
        public Object toSource()
        {
            return _json;
        }

        @Override
        public String toString()
        {
            if (_json != null)
                return _json.toString();
            return "{}";
        }

    }
}
