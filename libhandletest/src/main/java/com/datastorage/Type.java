package com.datastorage;


import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Type
{
    //////////////////////////Static//////////////////////////

    private static final String BoolType = "Z";
    private static final String ByteType = "B";
    private static final String CharType = "C";
    private static final String LongType = "J";
    private static final String FloatType = "F";
    private static final String DoubleType = "D";
    private static final String ShortType = "S";
    private static final String IntType = "I";
    private static final String VoidType = "V";

    private static final String ClassType = "L";
    private static final String ArrayType = "[";

    private static Map<Class<?>, Class<?>> _classToValueMapping;
    private static Map<Class<?>, Class<?>> _valueToClassMapping;
    private static Map<Class<?>, String> _valueToSignatureMapping;

    static
    {
        _classToValueMapping = new HashMap<Class<?>, Class<?>>()
        {
            {
                put(Integer.class, int.class);
                put(Byte.class, byte.class);
                put(Short.class, short.class);
                put(Long.class, long.class);
                put(Boolean.class, boolean.class);
                put(Character.class, char.class);
                put(Float.class, float.class);
                put(Double.class, double.class);
            }
        };

        _valueToClassMapping = new HashMap<Class<?>, Class<?>>()
        {
            {
                put(int.class, Integer.class);
                put(byte.class, Byte.class);
                put(short.class, Short.class);
                put(long.class, Long.class);
                put(boolean.class, Boolean.class);
                put(char.class, Character.class);
                put(float.class, Float.class);
                put(double.class, Double.class);
            }
        };

        _valueToSignatureMapping = new HashMap<Class<?>, String>()
        {
            {
                put(Integer.class, IntType);
                put(Byte.class, ByteType);
                put(Short.class, ShortType);
                put(Long.class, LongType);
                put(Boolean.class, BoolType);
                put(Character.class, CharType);
                put(Float.class, FloatType);
                put(Double.class, DoubleType);

                put(int.class, IntType);
                put(byte.class, ByteType);
                put(short.class, ShortType);
                put(long.class, LongType);
                put(boolean.class, BoolType);
                put(char.class, CharType);
                put(float.class, FloatType);
                put(double.class, DoubleType);

                put(Void.class, VoidType);
            }
        };
    }

    //////////////////////////Help Method//////////////////////////


    public static String toClassSignature(Class<?> cls)
    {
        String sign = "";

        if ("void".equals(cls.getName()))
            return "V";

        if (cls.isArray())
        {
            if (isPrimitiveType(cls.getComponentType()))
                sign = _valueToSignatureMapping.get(cls.getComponentType());
            else
                sign = ClassType + cls.getComponentType().getName().replace("\\.", "/") + ";";

            sign = ArrayType + sign;
        }
        else
        {
            if (isPrimitiveType(cls))
                sign = _valueToSignatureMapping.get(cls);
            else
                sign = ClassType + cls.getName().replace(".", "/") + ";";
        }

        return sign;
    }

    public static String toMethodSignature(Method method)
    {
        StringBuilder paramString = new StringBuilder();
        paramString.append("(");
        Class<?>[] types = method.getParameterTypes();
        if (types != null && types.length > 0)
        {
            for (Class<?> item : types)
            {
                paramString.append(toClassSignature(item));
            }
        }
        else
        {
            paramString.append("V");
        }
        paramString.append(")");
        paramString.append(toClassSignature(method.getReturnType()));

        return paramString.toString();
    }

    public static boolean isSerializerType(Class<?> cls)
    {
        return isPrimitiveType(cls) || String.class.equals(cls) || Date.class.equals(cls);
    }

    public static Class<?> wrapObject(Class<?> cls)
    {
        Class<?> tar = _valueToClassMapping.get(cls);

        if (tar == null)
            return cls;
        else
            return tar;
    }

    public static boolean isSub(Class<?> cls, Class<?> supper)
    {
        return supper.isAssignableFrom(cls);
    }

    /*
     * 判断类型是不是原子类型
     * */
    public static boolean isPrimitiveType(Class<?> cls)
    {
        return _classToValueMapping.containsKey(cls) || _valueToClassMapping.containsKey(cls);
    }

    //////////////////////////Convert Method//////////////////////////

    public static <T> Type.Convert<T> createConvert(Class<T> cls)
    {
        return new Type.Convert<T>();
    }

    public interface IConvert<T>
    {
        Object convert(T value);
    }

    public static class Convert<T>
    {
        public static final int String = 0;
        public static final int Boolean = 1;
        public static final int Byte = 2;
        public static final int Short = 3;
        public static final int Int = 4;
        public static final int Long = 5;
        public static final int Float = 6;
        public static final int Double = 7;

        private Map<Class<?>, IConvert<T>> _convertMap = new HashMap<Class<?>, IConvert<T>>();

        public Convert<T> put(int type, IConvert<T> convert)
        {
            switch (type)
            {
                case String:
                    _convertMap.put(java.lang.String.class, convert);
                    break;
                case Boolean:
                    _convertMap.put(boolean.class, convert);
                    _convertMap.put(java.lang.Boolean.class, convert);
                    break;
                case Byte:
                    _convertMap.put(byte.class, convert);
                    _convertMap.put(java.lang.Byte.class, convert);
                    break;
                case Short:
                    _convertMap.put(short.class, convert);
                    _convertMap.put(java.lang.Short.class, convert);
                    break;
                case Int:
                    _convertMap.put(int.class, convert);
                    _convertMap.put(Integer.class, convert);
                    break;
                case Long:
                    _convertMap.put(long.class, convert);
                    _convertMap.put(java.lang.Long.class, convert);
                    break;
                case Float:
                    _convertMap.put(float.class, convert);
                    _convertMap.put(java.lang.Float.class, convert);
                    break;
                case Double:
                    _convertMap.put(double.class, convert);
                    _convertMap.put(java.lang.Double.class, convert);
                    break;
                default:
                    break;
            }

            return this;
        }

        public Object convert(T value, Class<?> to)
        {
            if (value == null)
                return null;

            IConvert convert = _convertMap.get(to);
            if (convert != null)
                return convert.convert(value);

            return value;
        }
    }

    /*
     * 由装箱类型获得他的原子类型
     * */
    public static Class<?> convertValue(Class<?> cls)
    {
        return _classToValueMapping.get(cls);
    }

    /*
     * 由原子类型获得他的装箱类型
     * */
    public static Class<?> convertClass(Class<?> value)
    {
        return _valueToClassMapping.get(value);
    }

    public static Class<?> toClass(Class<?> cls)
    {
        if (_classToValueMapping.containsKey(cls))
            return cls;

        if (_valueToClassMapping.containsKey(cls))
            return _valueToClassMapping.get(cls);

        return null;
    }

    public static Class<?> toValue(Class<?> cls)
    {
        if (_valueToClassMapping.containsKey(cls))
            return cls;

        if (_classToValueMapping.containsKey(cls))
            return _classToValueMapping.get(cls);

        return null;
    }

    public static String toFirstUpper(String name)
    {
        if (name == null || name.length() == 0)
            return name;
        if (name.length() == 1)
            return name.toUpperCase().charAt(0) + "";
        else
            return name.toUpperCase().charAt(0) + name.substring(1);
    }

    public static Object stringToValue(String str, Class<?> cls)
    {
        if ("null".equals(str))
            return null;

        if (String.class.equals(cls))
            return str;

        if (Integer.class.equals(cls) || int.class.equals(cls))
        {
            return Integer.parseInt(str);
        }
        else if (Long.class.equals(cls) || long.class.equals(cls))
        {
            return Long.parseLong(str);
        }
        else if (Byte.class.equals(cls) || byte.class.equals(cls))
        {
            return Byte.parseByte(str);
        }
        else if (Short.class.equals(cls) || byte.class.equals(cls))
        {
            return Short.parseShort(str);
        }
        else if (Float.class.equals(cls) || float.class.equals(cls))
        {
            return Float.parseFloat(str);
        }
        else if (Double.class.equals(cls) || double.class.equals(cls))
        {
            return Double.parseDouble(str);
        }
        else if (Boolean.class.equals(cls) || boolean.class.equals(cls))
        {
            return Boolean.parseBoolean(str);
        }
        else if (Date.class.equals(cls))
        {
            return Date.parse(str);
        }

        return null;
    }

    //region Type

    public static final String Method = "Method";
    public static final String Field = "Field";


    private final static Set<String> NonProperties = new HashSet<String>()
    {
        {
            add("getClass");
        }
    };

    private static Map<String, Type> _types;

    public static Type getType(Class<?> cls)
    {
        return getType(cls, "Method");
    }

    public static Type getType(Object obj)
    {
        return getType(obj, "Method");
    }

    public static Type getType(Class<?> cls, String t)
    {
        String name = cls.getName();

        if (getTypes().containsKey(name))
            return getTypes().get(name);

        Type type = new Type(cls, t);
        getTypes().put(name, type);

        return type;
    }

    public static Type getType(Object object, String t)
    {
        if (object == null)
            return null;

        String name = object.getClass().getName();

        if (getTypes().containsKey(name))
            return getTypes().get(name);

        Type type = new Type(object.getClass(), t);
        getTypes().put(name, type);

        return type;
    }

    private static Map<String, Type> getTypes()
    {
        if (_types == null)
        {
            _types = new HashMap<>();
        }

        return _types;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD,
            ElementType.TYPE,
            ElementType.FIELD})
    public @interface Display
    {
        String value() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD,
            ElementType.FIELD})
    public @interface Ignore
    {

    }

    //endregion

    //region Type Property

    public abstract static class Property
    {
        private Class<?> _cls;
        private Display _tag;
        private Map<Class<?>, Annotation> _tags;
        private String _name;

        public Property(String name, Class<?> cls)
        {
            _cls = cls;
            _name = name;

            _tags = new HashMap<>();
        }

        public Class<?> getType()
        {
            return _cls;
        }

        public abstract Class<?>[] getGenericTypes();

        private void setDisplayName(Display tag)
        {
            _tag = tag;
        }

        private void addAnnotation(Annotation tag)
        {
            _tags.put(tag.annotationType(), tag);
        }

        public Annotation[] getTags()
        {
            return _tags.values().toArray(new Annotation[0]);
        }

        @SuppressWarnings("unchecked")
        public <T> T getTag(Class<T> cls)
        {
            if (_tags.containsKey(cls))
                return (T) _tags.get(cls);

            else return null;
        }

        public String getName()
        {
            if (_tag != null)
                return _tag.value();

            return _name;
        }

        public abstract void setValue(Object host, Object value) throws IllegalAccessException, InvocationTargetException;

        public abstract Object getValue(Object host) throws IllegalAccessException, InvocationTargetException;

        public Object get(Object host)
        {
            try
            {
                return getValue(host);
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        public void set(Object host, Object value)
        {
            try
            {
                setValue(host, value);
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }

        public abstract boolean canGet();

        public abstract boolean canSet();

        private static Property createMethod(String name, Class<?> cls)
        {
            return new MethodProperty(name, cls);
        }

        private static Property createField(String name, Class<?> cls, Field field)
        {
            return new FieldProperty(name, cls).setField(field);
        }

        private static Property setDisplayName(Property property, Display tag)
        {
            property.setDisplayName(tag);

            return property;
        }

        private static Property addAnnotation(Property property, Annotation tag)
        {
            property.addAnnotation(tag);

            return property;
        }

        @Override
        public String toString()
        {
            return "property:" + getName();
        }
    }

    private static class FieldProperty extends Property
    {
        private Field _field;
        private Class<?>[] _gclsArray;

        public FieldProperty(String name, Class<?> cls)
        {
            super(name, cls);
        }

        FieldProperty setField(Field field)
        {
            _field = field;

            return this;
        }

        @Override
        public Class<?>[] getGenericTypes()
        {
            if (_gclsArray != null)
                return _gclsArray;

            if (_field.getGenericType() instanceof ParameterizedType)
            {
                java.lang.reflect.Type[] types = ((ParameterizedType) _field.getGenericType()).getActualTypeArguments();

                Class<?>[] array = new Class<?>[types.length];

                for (int i = 0; i < types.length; i++)
                    array[i] = (Class<?>) types[i];

                return _gclsArray = array;
            }

            return new Class[0];
        }

        @Override
        public void setValue(Object host, Object value) throws IllegalAccessException
        {
            _field.setAccessible(true);
            _field.set(host, value);
        }

        @Override
        public Object getValue(Object host) throws IllegalAccessException
        {
            _field.setAccessible(true);
            return _field.get(host);
        }

        @Override
        public boolean canGet()
        {
            return _field != null;
        }

        @Override
        public boolean canSet()
        {
            return _field != null;
        }
    }

    private static class MethodProperty extends Property
    {
        private Method _set, _get;
        private Display _tag;

        private Class<?>[] _gclsArray;


        @Override
        public Class<?>[] getGenericTypes()
        {
            if (_gclsArray != null)
                return _gclsArray;

            if (_set != null)
            {
                return getSetGenericTypes();
            }
            else if (_get != null)
            {
                return getGetGenericTypes();
            }

            return new Class<?>[0];
        }

        private Class<?>[] getGetGenericTypes()
        {
            if (_get.getGenericReturnType() instanceof ParameterizedType)
            {
                ParameterizedType type = (ParameterizedType) (_get.getGenericReturnType());
                java.lang.reflect.Type[] types = type.getActualTypeArguments();

                Class<?>[] array = new Class<?>[types.length];

                for (int i = 0; i < types.length; i++)
                    array[i] = (Class<?>) types[i];

                return _gclsArray = array;
            }
            return new Class<?>[0];
        }

        private Class<?>[] getSetGenericTypes()
        {
            if (_set.getGenericParameterTypes()[0] instanceof ParameterizedType)
            {
                ParameterizedType type = (ParameterizedType) (_set.getGenericParameterTypes()[0]);
                java.lang.reflect.Type[] types = type.getActualTypeArguments();

                Class<?>[] array = new Class<?>[types.length];

                for (int i = 0; i < types.length; i++)
                    array[i] = (Class<?>) types[i];

                return _gclsArray = array;
            }
            return null;
        }

        public MethodProperty(String name, Class<?> cls)
        {
            super(name, cls);
        }

        public boolean canGet()
        {
            return _get != null;
        }

        public boolean canSet()
        {
            return _set != null;
        }

        private MethodProperty setSetter(Method set)
        {
            _set = set;
            return this;
        }

        private MethodProperty setGetter(Method get)
        {
            _get = get;
            return this;
        }

        public void setValue(Object obj, Object value) throws InvocationTargetException, IllegalAccessException
        {
            _set.invoke(obj, value);
        }

        public Object getValue(Object obj) throws InvocationTargetException, IllegalAccessException
        {
            return _get.invoke(obj);
        }
    }

    //endregion

    //region Type Property parser

    /***
     Type Property parser
     */
    private Map<String, Property> _props;

    public Type(Class<?> cls, String type)
    {
        if (type.equals("Field"))
            _props = getFieldPropertiesMap(cls);
        else
            _props = getPropertiesMap(cls);
    }

    public Property getProerty(String name)
    {
        return _props.get(name);
    }

    public Iterable<Property> getProperties()
    {
        return _props.values();
    }

    //endregion

    //region Type Property parser Fieled

    private Map<String, Property> getFieldPropertiesMap(Class<?> cls)
    {
        Map<String, Property> _properties = new HashMap<String, Property>();

        for (Field field : Reflect.findFields(cls, field -> !Modifier.isStatic(field.getModifiers()) &&
                !field.getName().startsWith("this$") &&
                field.getAnnotation(Ignore.class) == null &&
                !Modifier.isTransient(field.getModifiers())))
        {
            Property property = Property.createField(field.getName(), field.getType(), field);

            for (Annotation item : field.getAnnotations())
            {
                property.addAnnotation(item);
            }

            Display tag = field.getAnnotation(Display.class);
            property.setDisplayName(tag);

            _properties.put(field.getName(), property);
        }

        return _properties;
    }

    //endregion

    //region Type Property parser Method

    /*
     * 获得cls中所有Property
     */
    private Map<String, Property> getPropertiesMap(Class<?> cls)
    {
        Map<String, Property> props = new HashMap<String, Property>();

        for (Method item : getMethods(cls))
        {
            parseMethod(props, item);
        }

        Map<String, Property> propertyMap = new HashMap<>();

        for (Map.Entry<String, Property> entry : props.entrySet())
        {
            propertyMap.put(entry.getValue().getName(), entry.getValue());
        }

        return propertyMap;
    }

    private Iterable<Method> getMethods(Class<?> cls)
    {
        List<Method> methods = new ArrayList<Method>();
        Class<?> clazz = cls;
        while (clazz != null)
        {
            for (Method method : clazz.getMethods())
            {
                method.setAccessible(true);
                methods.add(method);
            }

            clazz = clazz.getSuperclass();
        }
        return methods;
    }

    private void parseMethod(Map<String, Property> props, Method method)
    {
        MethodProperty property = null;
        if (method.getAnnotation(Ignore.class) != null)
            return;

        if (method.getParameterTypes().length == 0)
        {
            parseGetter(props, method);
        }
        else if (method.getParameterTypes().length == 1)
        {
            parseSetter(props, method);
        }
    }

    private void parseSetter(Map<String, Property> props, Method method)
    {
        MethodProperty property;
        String name = getPropertyName(method, "set");

        if (name == null || "".equals(name))
            return;

        Display tag = method.getAnnotation(Display.class);

        Class<?>[] clsArray = method.getParameterTypes();
        if (clsArray.length != 1)
            return;

        if (props.containsKey(name))
            property = ((MethodProperty) props.get(name)).setSetter(method);
        else
            props.put(name, property = ((MethodProperty) Property.createMethod(name, clsArray[0])).setSetter(method));

        if (tag != null)
            Property.setDisplayName(property, tag);

        for (Annotation item : method.getAnnotations())
        {
            Property.addAnnotation(property, item);
        }
    }

    private void parseGetter(Map<String, Property> props, Method method)
    {
        MethodProperty property;
        String name = getPropertyName(method, "is", "get");
        Display tag = method.getAnnotation(Display.class);

        if (name == null || "".equals(name))
            return;

        Class<?> cls = method.getReturnType();

        if (cls == null)
            return;

        if (props.containsKey(name))
            property = ((MethodProperty) props.get(name)).setGetter(method);
        else
            props.put(name, property = ((MethodProperty) Property.createMethod(name, cls)).setGetter(method));

        Property.setDisplayName(property, tag);

        for (Annotation item : method.getAnnotations())
        {
            Property.addAnnotation(property, item);
        }
    }

    private String getPropertyName(Method method, String... prex)
    {
        String name = method.getName();

        if (NonProperties.contains(name))
            return null;

        for (String item : prex)
        {
            if (name.startsWith(item) && !name.equals(item))
            {
                char[] properyName = new char[name.length() - item.length()];
                char[] src = name.toCharArray();
                properyName[0] = (char) (name.charAt(item.length()) - 'A' + 'a');
                System.arraycopy(src, item.length() + 1, properyName, 1, properyName.length - 1);

                return new String(properyName);
            }
        }

        return null;
    }

    //endregion
}
