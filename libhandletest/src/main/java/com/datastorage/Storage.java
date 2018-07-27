package com.datastorage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;


public class Storage
{
    public final static StorageType Timeout = StorageType.Timeout;
    public final static StorageType Process = StorageType.Process;

    public final static StorageType Map = StorageType.Map;

    /***
     * params:
     *  dir 文件所在目录
     */
    public final static StorageType Properties = StorageType.Properties;

    /***
     * params:
     *  dir 文件所在目录
     */
    public final static StorageType Object = StorageType.Object;


    public enum StorageType
    {
        Timeout(0x01), Process(0x10), Map(0x02), Properties(0x04), Object(0x08), Share(0x10);

        private int _value;

        StorageType(int type)
        {
            _value = type;
        }

        public int value()
        {
            return _value;
        }

        public static int value(StorageType... types)
        {
            int value = 0;
            for (StorageType type : types)
            {
                value |= type.value();
            }

            return value;
        }

        public static int value(int... types)
        {
            int value = 0;
            for (int type : types)
            {
                value |= type;
            }

            return value;
        }

        @Nullable
        public static StorageType lookup(int value)
        {
            for (StorageType type : StorageType.values())
            {
                if (type.value() == value)
                    return type;
            }

            return null;
        }
    }

    private static final List<StorageType> _supportTypes = new ArrayList<StorageType>();

    private static Map<StorageType, Class<?>> _typeMapping = new HashMap<StorageType, Class<?>>();

    //region Inner Class

    public static class Counter
    {
        private Container _container;

        public Counter(Container container)
        {
            _container = container;
        }

        public void set(String name, int value)
        {
            _container.put(name, value);
        }

        public int get(String name, int def)
        {
            if (!_container.containsKey(name))
                return def;

            return _container.value(name, def);
        }

        public void add(String name, int value)
        {
            int count = get(name, 0);

            _container.put(name, count + value);
        }

        public void add(String name, int def, int value)
        {
            int count = get(name, def);

            _container.put(name, count + value);
        }

        public boolean add(String name, int value, int min, int max)
        {
            int count = get(name, min);

            count += value;

            boolean res;
            if (count >= max)
            {
                count = min;
                res = true;
            }
            else res = false;

            _container.put(name, count);

            return res;
        }

    }

    public static class StorageException extends Exception
    {
        public StorageException(String s)
        {
            super(s);
        }

        public StorageException(String s, Throwable throwable)
        {
            super(s, throwable);
        }

        public StorageException(Throwable throwable)
        {
            super(throwable);
        }
    }

    public static class KeyNotFoundException extends StorageException
    {
        public KeyNotFoundException(String s)
        {
            super(s);
        }

        public KeyNotFoundException(String s, Throwable throwable)
        {
            super(s, throwable);
        }

        public KeyNotFoundException(Throwable throwable)
        {
            super(throwable);
        }
    }

    public final static class Builder
    {
        private String _name;
        private int _type;
        private Map<String, Object> _args = new HashMap<String, java.lang.Object>();

        public Builder(String name, StorageType... types)
        {
            _name = name;
            _type = StorageType.value(types);
        }

        public Builder(String name, int... types)
        {
            _name = name;
            _type = StorageType.value(types);
        }

        public Builder setup(String name, Object value)
        {
            _args.put(name, value);

            return this;
        }

        public Builder timeout(long timeout)
        {
            setup("timeout", timeout);

            _type = _type | Timeout.value();

            return this;
        }

        public Builder dir(File file)
        {
            setup("dir", file);

            return this;
        }

        public Builder process(int port)
        {
            setup("port", port);

            _type = _type | Process.value();

            return this;
        }

        public Container build() throws StorageException
        {
            return create(_name, _type, _args);
        }

        public Counter counter() throws StorageException
        {
            return count(build());
        }
    }

    //endregion

    //region Static

    static
    {
        register(Map, MemoryContainer.class);
        register(Properties, PropertiesDataContainer.class);
        register(Object, ObjectContainer.class);
        register(Process, ProcessContainer.class);
        register(Timeout, TimeoutContainer.class);
    }

    public static void register(StorageType type, Class<?> cls)
    {
        _typeMapping.put(type, cls);
        _supportTypes.add(type);
    }

    private static void handle(Throwable e)
    {
        e.printStackTrace();
    }

    private static <T> T toObject(String data) throws StorageException
    {
        String objectVal = data;
        byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        ObjectInputStream ois = null;
        try
        {
            ois = new ObjectInputStream(bais);
            T t = (T) ois.readObject();
            return t;
        }
        catch (Exception e)
        {
            throw new StorageException(e);
        }
        finally
        {
            try
            {
                bais.close();
                if (ois != null)
                    ois.close();
            }
            catch (IOException e)
            {
                throw new StorageException(e);
            }
        }
    }

    private static String toData(Object object) throws StorageException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try
        {
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            return new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));

        }
        catch (IOException e)
        {
            throw new StorageException(e);
        }
        finally
        {
            try
            {
                baos.close();
                if (out != null)
                    out.close();
            }
            catch (IOException e)
            {
                throw new StorageException(e);
            }
        }
    }

    //endregion

    //region Builder

    public static Counter count(Container container)
    {
        return new Counter(container);
    }

    public static Builder builder(String name, StorageType type)
    {
        return new Builder(name, type);
    }

    private static <T extends Container> T newInstance(StorageType type) throws IllegalAccessException, InstantiationException
    {
        Class<?> cls = _typeMapping.get(type);
        return (T) cls.newInstance();
    }

    private static <T extends Container> T newInstance(StorageType type, Class<?>[] array, Object... args) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException
    {
        Class<?> cls = _typeMapping.get(type);
        Constructor<?> constructor = cls.getConstructor(array);
        return (T) constructor.newInstance(args);
    }

    private static Container create(String name, int type, Map<String, Object> access) throws StorageException
    {
        try
        {
            Container container = null;

            int t = type & (~Timeout.value()) & (~Process.value());

            Verifier.verify(t, t + " is not support type", _supportTypes);

            container = newInstance(StorageType.lookup(t));

            if ((Timeout.value() & type) > 0)
            {
                long span = Safe.get(access, "timeout", 1000 * 60 * 60L);
                container = newInstance(StorageType.Timeout, new Class[]{Container.class, long.class}, container, span);
            }

            if ((Process.value() & type) > 0)
            {
                int port = Safe.get(access, "port", -1);
                if (port == -1)
                    throw new StorageException("not get port");

                container = newInstance(StorageType.Process, new Class[]{Container.class, int.class}, container, port);
            }

            container.config(name, access);

            return container;
        }
        catch (Exception e)
        {
            throw new StorageException(e);
        }
    }

    //endregion

    //region Properties Container

    static class PropertiesDataContainer extends Container
    {
        private File _path;
        private Properties _properties;
        private long _last;

        @Override
        public boolean isMeedClass()
        {
            return true;
        }

        @Override
        public void config(String name, Map<String, Object> access) throws StorageException
        {
            File dir = Safe.get(access, "dir", null);
            if (dir == null)
                throw new StorageException("dir is null");

            _path = new File(dir, name);

            reload();
        }

        private synchronized void reload() throws StorageException
        {
            try
            {
                _properties = new Properties();

                File file = IO.create(_path);

                InputStream in = new FileInputStream(file);
                _properties.load(in);
                in.close();
                _last = file.lastModified();
            }
            catch (IOException e)
            {
                throw new StorageException(e);
            }
        }

        @Override
        public boolean containsKey(String name)
        {
            return _properties.containsKey(name);
        }

        @Override
        public <T> T get(String name, Class<T> cls) throws StorageException
        {
            if (_last != _path.lastModified())
                reload();

            if (Type.isSerializerType(cls))
                return (T) Type.stringToValue(_properties.getProperty(name, null), cls);
            else
                return (T) toObject(_properties.getProperty(name));
        }

        @Override
        public Set<String> list()
        {
            Set<String> set = new HashSet<String>();

            for (Object key : _properties.keySet())
                set.add((String) key);

            return set;
        }

        @Override
        public Editor edit()
        {
            return new Editor()
            {
                @Override
                public Editor save() throws StorageException
                {
                    try
                    {
                        synchronized (PropertiesDataContainer.this)
                        {
                            File bak = new File(_path.getPath() + ".bak");
                            IO.safeOutput(bak, outputStream -> _properties.store(outputStream, ""));

                            if (!bak.renameTo(_path))
                                throw new StorageException("save properties to " + _path + " failed");

                            _last = _path.lastModified();
                        }
                    }
                    catch (Exception e)
                    {
                        throw new StorageException(e);
                    }

                    return this;
                }

                @Override
                public Editor remove(String name)
                {
                    if (_properties.containsKey(name))
                        _properties.remove(name);

                    return this;
                }

                @Override
                public Editor put(String name, Object value) throws StorageException
                {
                    if (value != null)
                    {
                        if (Type.isSerializerType(value.getClass()))
                            _properties.put(name, String.valueOf(value));
                        else
                            _properties.put(name, toData(value));
                    }

                    return this;
                }

                @Override
                public Editor clear()
                {
                    _properties.clear();
                    _path.delete();

                    return this;
                }
            };
        }

        @Override
        public InputStream stream() throws StorageException
        {
            try
            {
                return new FileInputStream(_path);
            }
            catch (FileNotFoundException e)
            {
                throw new StorageException(e);
            }
        }
    }

    //endregion

    //region Memory Container

    static class MemoryContainer extends Container
    {
        private Map<String, Object> _values;

        public MemoryContainer()
        {
            _values = new HashMap<String, Object>();
        }

        @Override
        public boolean isMeedClass()
        {
            return true;
        }

        @Override
        public synchronized boolean containsKey(String name)
        {
            return _values.containsKey(name);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T get(String name, Class<T> cls)
        {
            if (_values.containsKey(name))
                return (T) _values.get(name);
            else return null;
        }

        @Override
        public Editor edit()
        {
            return new Editor()
            {

                @Override
                public Editor save()
                {
                    return this;
                }

                @Override
                public Editor remove(String name)
                {
                    _values.remove(name);

                    return this;
                }

                @Override
                public Editor put(String name, Object value)
                {
                    _values.put(name, value);

                    return this;
                }

                @Override
                public Editor clear()
                {
                    _values.clear();

                    return this;
                }
            };
        }

        @Override
        public Set<String> list()
        {
            return _values.keySet();
        }

        @Override
        public void config(String name, Map<String, Object> access)
        {

        }

        @Override
        public InputStream stream() throws StorageException
        {
            try
            {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

                objectOutputStream.writeObject(_values);

                return new ByteArrayInputStream(outputStream.toByteArray());
            }
            catch (IOException e)
            {
                throw new StorageException(e);
            }
        }
    }

    //endregion

    //region Object Container

    public static final class Value
    {
        public Object value;
        public long lastModified;
    }

    static class ObjectContainer extends Container
    {
        private File _dir;
        private Map<String, Value> _values = new HashMap<String, Value>();

        @Override
        public void config(String name, Map<String, Object> access) throws StorageException
        {
            _dir = Safe.get(access, "dir", null);

            if (_dir == null)
            {
                throw new StorageException("dir path is null");
            }

            _dir = new File(_dir, name);
        }

        @Override
        public boolean containsKey(String name)
        {
            File file = new File(_dir, name);
            return file.exists() && file.length() > 0;
        }

        private Object findCache(String name, long lastModified)
        {
            Value value = _values.get(name);

            if (value != null && value.lastModified == lastModified)
            {
                return value.value;
            }

            return null;
        }

        @Override
        public synchronized <T> T get(String name, Class<T> cls) throws StorageException
        {
            File file = new File(_dir, name);

            if (!file.exists() && file.length() <= 0)
                throw new KeyNotFoundException("not found type use " + name);

            ObjectInputStream inputStream = null;

            long lastModified = file.lastModified();
            Object res = findCache(name, lastModified);

            if (res != null)
                return (T) res;

            try
            {
                FileInputStream fileInputStream = new FileInputStream(file);
                inputStream = new ObjectInputStream(fileInputStream);

                Value value = new Value();
                value.value = inputStream.readObject();
                value.lastModified = lastModified;

                _values.put(name, value);

                return (T) value.value;
            }
            catch (IOException e)
            {
                throw new StorageException(e);
            }
            catch (ClassNotFoundException e)
            {
                throw new StorageException(e);
            }
            finally
            {
                try
                {
                    if (inputStream != null)
                        inputStream.close();
                }
                catch (Exception e)
                {
                    throw new StorageException(e);
                }
            }
        }

        @Override
        public Set<String> list()
        {
            String[] files = _dir.list();
            if (files != null)
                return new HashSet<String>(Arrays.asList(files));
            else
                return new HashSet<String>();
        }

        @Override
        public boolean isMeedClass()
        {
            return false;
        }

        @Override
        public Editor edit()
        {
            return new Editor()
            {
                @Override
                public Editor put(String name, Object value) throws StorageException
                {
                    synchronized (ObjectContainer.this)
                    {

                        FileLock lock = null;
                        ObjectOutputStream objectOutputStream = null;
                        try
                        {
                            File file = new File(_dir, name);

                            IO.create(file);

                            FileOutputStream outputStream = new FileOutputStream(file);
                            lock = outputStream.getChannel().lock();
                            objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));

                            objectOutputStream.writeObject(value);

                            Value v = new Value();
                            v.value = value;
                            v.lastModified = file.lastModified();

                            _values.put(name, v);
                        }
                        catch (Exception e)
                        {
                            throw new StorageException(e);
                        }
                        finally
                        {
                            try
                            {
                                if (lock != null)
                                    lock.release();

                                if (objectOutputStream != null)
                                    objectOutputStream.close();
                            }
                            catch (Exception e)
                            {
                                throw new StorageException(e);
                            }
                        }
                    }

                    return this;
                }

                @Override
                public Editor remove(String name)
                {
                    new File(_dir, name).delete();

                    return this;
                }

                @Override
                public Editor save()
                {
                    return this;
                }

                @Override
                public Editor clear()
                {
                    IO.delete(_dir);

                    return this;
                }
            };
        }

        @Override
        public InputStream stream()
        {
            return null;
        }
    }

    //endregion

    //region Timeout Container

    static class TimeoutContainer extends Container
    {
        private final static String TimeoutList = "timeoutlist";

        private Container _timeoutList;
        private Container _container;
        private long _span;

        @Override
        public void config(String name, Map<String, Object> access) throws StorageException
        {
            _container.config(name, access);
        }

        @Override
        public boolean isMeedClass()
        {
            return _container.isMeedClass();
        }

        public TimeoutContainer(Container container, long span)
        {
            _timeoutList = container;
            _container = container;
            _span = span;
        }

        private String getTimeoutName(String name)
        {
            return "timeout_" + name;
        }

        @Override
        public boolean containsKey(String name)
        {
            boolean isIn = _container.containsKey(name);
            if (!isIn || !_timeoutList.containsKey(getTimeoutName(name)))
                return false;

            long time = System.currentTimeMillis();
            String lastStr = _timeoutList.value(getTimeoutName(name), time + "");

            long last = Long.parseLong(lastStr);

            if (time < last || time - last >= _span)
            {
                remove(name);
                return false;
            }
            else return true;
        }

        @Override
        public <T> T get(String name, Class<T> cls) throws StorageException
        {
            if (containsKey(name))
                return _container.get(name, cls);
            else return null;
        }

        @Override
        public Set<String> list()
        {
            Set<String> keys = new HashSet<String>();

            for (String string : _container.list())
            {
                if (containsKey(string))
                    keys.add(string);
            }

            return keys;
        }

        @Override
        public Editor edit()
        {
            final Editor editor = _container.edit();
            final Editor timeout = _timeoutList.edit();

            return new Editor()
            {
                @Override
                public Editor save() throws StorageException
                {
                    editor.save();
                    timeout.save();

                    return this;
                }

                @Override
                public Editor remove(String name) throws StorageException
                {
                    timeout.remove(getTimeoutName(name));
                    editor.remove(name);

                    return this;
                }

                @Override
                public Editor put(String name, Object value) throws StorageException
                {
                    if (!_container.containsKey(name) || !_timeoutList.containsKey(getTimeoutName(name)))
                    {
                        timeout.put(getTimeoutName(name), new Date().getTime() + "");
                        editor.put(name, value);
                    }
                    else
                    {
                        editor.put(name, value);
                    }

                    return this;
                }

                @Override
                public Editor clear() throws StorageException
                {
                    timeout.clear();
                    editor.clear();

                    return this;
                }
            };
        }

        @Override
        public InputStream stream() throws StorageException
        {
            return _container.stream();
        }
    }

    //endregion

    //region Process Container

    public static class DataPackage implements Serializable
    {
        private int _pid;
        private int _type;
        private int _action;
        private String _name;
        private Serializable[] _values;

        public int pid()
        {
            return _pid;
        }

        public void pid(int _pid)
        {
            this._pid = _pid;
        }

        public void type(int _type)
        {
            this._type = _type;
        }

        public int type()
        {
            return _type;
        }

        public void name(String _name)
        {
            this._name = _name;
        }

        public String name()
        {
            return _name;
        }

        public void value(Serializable... values)
        {
            this._values = values;
        }

        public Serializable[] result()
        {
            return _values;
        }

        public void action(int _action)
        {
            this._action = _action;
        }

        public int action()
        {
            return _action;
        }
    }

    interface DataServer extends IDisposable
    {
        DataClient accept() throws IOException;
    }

    interface DataClient extends IDisposable
    {
        void write(DataPackage p) throws IOException;

        DataPackage read() throws IOException;
    }

    public static class StreamDataPackage extends DataPackage
    {
        private DataPackage _dataPackage;

        public StreamDataPackage(DataPackage dataPackage)
        {
            _dataPackage = dataPackage;
        }

        public StreamDataPackage()
        {
            _dataPackage = this;
        }

        public StreamDataPackage from(ObjectInputStream inputStream) throws IOException
        {
            try
            {
                DataPackage dataPackage = (DataPackage) inputStream.readObject();

                pid(dataPackage.pid());
                type(dataPackage.type());
                action(dataPackage.action());
                name(dataPackage.name());
                value(dataPackage.result());
            }
            catch (ClassNotFoundException e)
            {
                throw new IOException(e);
            }

            return this;
        }

        public StreamDataPackage to(ObjectOutputStream out) throws IOException
        {
            out.writeObject(_dataPackage);

            return this;
        }
    }

    public static class SocketDataClient implements DataClient
    {
        private Socket _client;
        private ObjectInputStream _reader;
        private ObjectOutputStream _writer;

        public SocketDataClient(Socket socket) throws IOException
        {
            _client = socket;
        }

        public SocketDataClient(int port) throws IOException
        {
            this(new Socket("localhost", port));
        }

        private ObjectInputStream reader() throws IOException
        {
            if (_reader == null)
                _reader = new ObjectInputStream(_client.getInputStream());

            return _reader;
        }

        private ObjectOutputStream writer() throws IOException
        {
            if (_writer == null)
                _writer = new ObjectOutputStream(_client.getOutputStream());

            return _writer;
        }

        @Override
        public void write(DataPackage p) throws IOException
        {
            if (p instanceof StreamDataPackage)
                ((StreamDataPackage) p).to(writer());
            else
                new StreamDataPackage(p).to(writer());
        }

        @Override
        public DataPackage read() throws IOException
        {
            return new StreamDataPackage().from(reader());
        }

        @Override
        public void close()
        {
            try
            {
                _client.close();
            }
            catch (IOException e)
            {
                handle(e);
            }
        }

        @Override
        public boolean isDispose()
        {
            return _client.isClosed();
        }
    }

    public static class SocketDataServer implements DataServer
    {
        private ServerSocket _server;

        public SocketDataServer(int port) throws IOException
        {
            _server = new ServerSocket(port);
        }

        @Override
        public void close()
        {
            try
            {
                _server.close();
            }
            catch (IOException e)
            {
                handle(e);
            }
        }

        @Override
        public boolean isDispose()
        {
            return _server.isClosed();
        }

        @Override
        public DataClient accept() throws IOException
        {
            Socket client = _server.accept();

            return new SocketDataClient(client);
        }
    }

    static class ProcessContainer extends Container
    {
        private final static int Get = 1;
        private final static int Add = 2;
        private final static int Contains = 3;
        private final static int List = 4;
        private final static int Remove = 5;

        private SocketDataServer _server;
        private SocketDataClient _client;
        private Container _container;
        private String _name;
        private int _port;

        public ProcessContainer(Container container, int port)
        {
            _container = container;
            _port = port;
        }

        private void tryCreateServer(int port) throws IOException
        {
            _server = new SocketDataServer(port);

            Sync.run(() -> {
                try
                {
                    final DataClient client = _server.accept();

                    handleCLient(client);
                }
                catch (IOException e)
                {
                    handle(e);
                }
            });
        }

        private void handleCLient(final DataClient client)
        {
            Sync.run(() -> {
                try
                {
                    while (true)
                    {
                        DataPackage dataPackage = client.read();
                        handlePackage(client, dataPackage);
                    }
                }
                catch (IOException e)
                {
                    handle(e);
                }
            });
        }

        private void handlePackage(DataClient client, DataPackage dataPackage) throws IOException
        {
            DataPackage p;
            switch (dataPackage.action())
            {
                case Contains:
                    p = new DataPackage();
                    p.value(_container.containsKey((String) dataPackage.result()[0]));
                    client.write(p);
                    return;
                case Add:
                    _container.put((String) dataPackage.result()[0], dataPackage.result()[1]);
                    client.write(new DataPackage());
                    return;
                case List:
                    p = new DataPackage();
                    p.value((Serializable) _container.list());
                    client.write(p);
                    return;
                case Remove:
                    _container.remove((String) dataPackage.result()[0]);
                    client.write(new DataPackage());
                    return;
            }
        }

        @Override
        public void config(String name, Map<String, Object> access) throws StorageException
        {

            _container.config(name, access);
            _name = name;

            try
            {
                tryCreateServer(_port);
            }
            catch (IOException e)
            {

            }

            try
            {
                _client = new SocketDataClient(_port);
            }
            catch (IOException e)
            {
                new StorageException(e);
            }
        }

        @Override
        public boolean containsKey(String name)
        {
            DataPackage dataPackage = new DataPackage();
            dataPackage.pid(0);
            dataPackage.action(Contains);
            dataPackage.name(_name);
            dataPackage.value(name);

            try
            {
                _client.write(dataPackage);
                DataPackage res = _client.read();
                return (Boolean) res.result()[0];
            }
            catch (IOException e)
            {
                handle(e);
            }

            return false;
        }

        @Override
        public <T> T get(String name, Class<T> cls) throws StorageException
        {
            DataPackage dataPackage = new DataPackage();
            dataPackage.pid(0);
            dataPackage.action(Get);
            dataPackage.name(_name);
            dataPackage.value(name);

            try
            {
                _client.write(dataPackage);
                DataPackage res = _client.read();
                return (T) res.result()[0];
            }
            catch (IOException e)
            {
                throw new StorageException(e);
            }
        }

        @Override
        public Set<String> list()
        {
            DataPackage dataPackage = new DataPackage();
            dataPackage.pid(0);
            dataPackage.action(List);
            dataPackage.name(_name);
            dataPackage.value();

            try
            {
                _client.write(dataPackage);
                DataPackage res = _client.read();
                return (Set<String>) res.result()[0];
            }
            catch (IOException e)
            {
                return new HashSet<String>();
            }
        }

        @Override
        public boolean isMeedClass()
        {
            return false;
        }

        @Override
        public Editor edit()
        {
            return new Editor()
            {
                @Override
                public Editor put(String name, Object value) throws StorageException
                {
                    DataPackage dataPackage = new DataPackage();
                    dataPackage.pid(0);
                    dataPackage.action(Add);
                    dataPackage.name(_name);
                    dataPackage.value(name, (Serializable) value);

                    try
                    {
                        _client.write(dataPackage);
                        DataPackage res = _client.read();
                    }
                    catch (IOException e)
                    {
                        throw new StorageException(e);
                    }

                    return this;
                }

                @Override
                public Editor remove(String name) throws StorageException
                {
                    DataPackage dataPackage = new DataPackage();
                    dataPackage.pid(0);
                    dataPackage.action(Remove);
                    dataPackage.name(_name);
                    dataPackage.value(name);

                    try
                    {
                        _client.write(dataPackage);
                        DataPackage res = _client.read();
                    }
                    catch (IOException e)
                    {
                        throw new StorageException(e);
                    }

                    return this;
                }

                @Override
                public Editor save() throws StorageException
                {
                    return this;
                }

                @Override
                public Editor clear()
                {
                    return this;
                }
            };
        }

        @Override
        public InputStream stream() throws StorageException
        {
            return _container.stream();
        }
    }

    //endregion

    //region Base

    public interface Editor
    {
        Editor put(String name, Object value) throws StorageException;

        Editor remove(String name) throws StorageException;

        Editor save() throws StorageException;

        Editor clear() throws StorageException;
    }

    public static abstract class Container
    {
        public abstract void config(String name, Map<String, Object> access) throws StorageException;

        public abstract boolean containsKey(String name);

        public abstract <T> T get(String name, Class<T> cls) throws StorageException;

        public abstract Set<String> list();

        public abstract boolean isMeedClass();

        public abstract InputStream stream() throws StorageException;

        public void remove(String name)
        {
            try
            {
                Editor editor = edit();
                editor.remove(name);
                editor.save();
            }
            catch (StorageException e)
            {
                handle(e);
            }
        }

        public boolean put(String name, Object value)
        {
            try
            {
                Editor editor = edit();
                editor.put(name, value);
                editor.save();

                return true;
            }
            catch (StorageException e)
            {
                handle(e);
            }

            return false;
        }

        public abstract Editor edit();

        @SuppressWarnings("unchecked")
        @Nullable
        public <T> T value(String name)
        {
            if (isMeedClass())
                throw new RuntimeException("not support method meed class");

            try
            {
                Object res = get(name, Object.class);

                if (res == null)
                    return null;
                else return (T) res;
            }
            catch (KeyNotFoundException e)
            {
                return null;
            }
            catch (Exception e)
            {
                handle(e);
                return null;
            }
        }

        @Nullable
        public <T> T value(String name, @NonNull Class<?> cls)
        {
            try
            {
                Object res = get(name, cls);

                if (res == null)
                    return null;
                else
                    return (T) res;
            }
            catch (StorageException e)
            {
                return null;
            }
        }

        @SuppressWarnings("unchecked")
        public <T> T value(String name, @NonNull T def)
        {
            Objects.requireNonNull(def, "def is null");

            Class<?> cls;
            T defValue;

            defValue = def;
            cls = def.getClass();

            try
            {
                Object object = get(name, cls);

                if (object == null)
                    return defValue;
                else return (T) object;
            }
            catch (KeyNotFoundException e)
            {
                return defValue;
            }
            catch (Exception e)
            {
                handle(e);
                return defValue;
            }
        }

        public boolean clear()
        {
            try
            {
                edit().clear().save();

                return true;
            }
            catch (StorageException e)
            {
                e.printStackTrace();

                return false;
            }
        }
    }

    //endregion
}
