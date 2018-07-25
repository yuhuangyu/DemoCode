package com.cache;


import java.io.File;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by anye6488 on 2016/10/17.
 */
public class CacheManager
{
    public interface ICache<TKey, TValue>
    {
        TValue get(TKey key);

        void put(TKey key, TValue value);

        void config(Options config);

        String getName();
    }

    public interface IMetaData<TKey>
    {

        Object metaData(TKey key, String name);
    }

    public interface ICleanup
    {
        void cleanup(Object obj);
    }

    //region Cache Impl

    public static abstract class LocalCache<TKey, TValue> implements ICache<TKey, TValue>, IMetaData<TKey>
    {
        @Override
        public synchronized TValue get(TKey tKey)
        {
            File file = getFileFromKey(tKey);
            return loadFromFile(file);
        }

        @Override
        public synchronized void put(TKey tKey, TValue value)
        {
            File file = getFileFromKey(tKey);

            saveToFile(value, file);
        }

        @Override
        public void config(Options config)
        {

        }

        @Override
        public Object metaData(TKey tKey, String name)
        {
            if ("SaveTime".equals(name))
            {
                File file = getFileFromKey(tKey);
                if (file != null)
                    return file.lastModified();

                return null;
            }

            return null;
        }

        abstract File getFileFromKey(TKey key);

        abstract void saveToFile(TValue value, File file);

        abstract TValue loadFromFile(File file);
    }

    public static class MemoryCache<TKey, TValue> implements ICache<TKey, TValue>
    {
        private Map<TKey, TValue> _map = new ConcurrentHashMap<TKey, TValue>();

        @Override
        public TValue get(TKey tKey)
        {
            return _map.get(tKey);
        }

        @Override
        public void put(TKey tKey, TValue value)
        {
            _map.put(tKey, value);
        }

        @Override
        public void config(Options config)
        {

        }

        @Override
        public String getName()
        {
            return "Memory";
        }
    }

    public static class LRUMemoryCache<TKey, TValue> implements ICache<TKey, TValue>
    {
        private int _limit = 1;
        private Map<TKey, TValue> _map = new HashMap<TKey, TValue>();
        private List<TKey> queue = new LinkedList<TKey>();

        @Override
        public synchronized TValue get(TKey tKey)
        {
            TValue value = _map.get(tKey);
            if (value != null)
            {
                queue.remove(tKey);
                queue.add(tKey);
            }

            return value;
        }

        @Override
        public synchronized void put(TKey tKey, TValue value)
        {
            if (queue.size() >= _limit)
            {
                TKey item = queue.remove(0);
                _map.remove(item);
            }

            queue.add(tKey);
            _map.put(tKey, value);
        }

        @Override
        public void config(Options config)
        {
            _limit = config.value("LRU.Limit", 0);
        }

        @Override
        public String getName()
        {
            return "LRU";
        }
    }

    public static class KeySoftReference<TKey, TValue> extends SoftReference<TValue>
    {
        private TKey _key;

        public TKey getKey()
        {
            return _key;
        }

        public KeySoftReference(TKey key, TValue r)
        {
            super(r);

            _key = key;
        }

        public KeySoftReference(TKey key, TValue r, ReferenceQueue<? super TValue> q)
        {
            super(r, q);

            _key = key;
        }
    }

    public static class SoftMemoryCache<TKey, TValue> implements ICache<TKey, TValue>
    {
        private Map<TKey, SoftReference<TValue>> _map = new ConcurrentHashMap<TKey, SoftReference<TValue>>();
        private ReferenceQueue<TValue> _queue = new ReferenceQueue<TValue>()
        {

        };

        SoftMemoryCache()
        {
            new Thread()
            {
                @Override
                public void run()
                {
                    while (!isInterrupted())
                    {
                        try
                        {
                            Thread.sleep(60 * 1000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }

                        while (true)
                        {
                            Reference<? extends TValue> ref = _queue.poll();
                            if (ref != null)
                            {
                                _map.remove(((KeySoftReference<TKey, TValue>) ref).getKey());
                            }
                            else
                                break;
                        }
                    }
                }
            }.start();
        }

        @Override
        public TValue get(TKey tKey)
        {
            SoftReference<TValue> ref = _map.get(tKey);

            if (ref == null)
                return null;

            if (ref.get() != null)
                return ref.get();
            else
            {
                _map.remove(tKey);

                return null;
            }
        }

        @Override
        public void put(TKey tKey, TValue value)
        {
            _map.put(tKey, new KeySoftReference<>(tKey, value, _queue));
        }

        @Override
        public void config(Options config)
        {

        }

        @Override
        public String getName()
        {
            return "Soft";
        }
    }

    public static class Cache<TKey, TValue> implements ICache<TKey, TValue>
    {
        private ICache<TKey, TValue>[] _cacheList;

        public Cache(ICache<TKey, TValue>... cacheList)
        {
            _cacheList = cacheList;
        }

        public ICache<TKey, TValue>[] list()
        {
            return _cacheList;
        }

        @Override
        public TValue get(TKey tKey)
        {
            List<ICache<TKey, TValue>> noCacheList = new ArrayList<ICache<TKey, TValue>>();
            for (ICache<TKey, TValue> cache : _cacheList)
            {
                TValue value = cache.get(tKey);

                if (value == null)
                    noCacheList.add(cache);
                else
                {
                    for (ICache<TKey, TValue> preCache : noCacheList)
                    {
                        preCache.put(tKey, value);
                    }

                    return value;
                }
            }

            return null;
        }

        @Override
        public void put(TKey tKey, TValue value)
        {
            for (ICache<TKey, TValue> cache : _cacheList)
            {
                cache.put(tKey, value);
            }
        }

        @Override
        public void config(Options config)
        {
            for (ICache<TKey, TValue> cache : _cacheList)
            {
                cache.config(config);
            }
        }

        @Override
        public String getName()
        {
            return "Cache";
        }
    }

    public static class ExpiresCahce<TKey, TValue> implements ICache<TKey, TValue>
    {
        private long _expires = -1;
        private ICache<TKey, TValue> _cache;

        public ExpiresCahce(ICache<TKey, TValue> cache)
        {
            _cache = cache;
        }

        @Override
        public TValue get(TKey tKey)
        {
            if (_expires >= 0 && _cache instanceof IMetaData)
            {
                Long time = (Long) ((IMetaData<TKey>) _cache).metaData(tKey, "SaveTime");
                if (time != null)
                {
                    if (time + _expires >= System.currentTimeMillis())
                    {
                        return null;
                    }
                }
            }

            return _cache.get(tKey);
        }

        @Override
        public void put(TKey tKey, TValue tValue)
        {
            _cache.put(tKey, tValue);
        }

        @Override
        public void config(Options config)
        {
            if (config.containsKey(_cache.getName() + ".Expires"))
                _expires = (Long) config.get(_cache.getName() + ".Expires");

            _cache.config(config);
        }

        void setExpires(long expires)
        {
            _expires = expires;
        }

        @Override
        public String getName()
        {
            return _cache.getName();
        }
    }

    //endregion

    private static Map<String, ICache> _creators = new HashMap<String, ICache>()
    {
        {
            put("LRU", new LRUMemoryCache());
            put("Soft", new SoftMemoryCache());
            put("Memory", new MemoryCache());
        }
    };

    public static void register(String name, Class<?> cls)
    {
//        _creators.put(name, new Creaters.ObjectCreator<ICache>(cls));
    }

    public static <TKey, TValue> Access<TKey, TValue> cache(Options options)
    {
        ICache<TKey, TValue>[] caches = getCacheList(options).toArray(new ICache[0]);
        ICache<TKey, TValue> cache = new Cache<TKey, TValue>(caches);
        cache.config(options);
        return new Access<>(cache);
    }

    public static <TKey, TValue> Builder builder()
    {
        return new Builder();
    }

    private static <TKey, TValue> List<ICache<TKey, TValue>> getCacheList(Options options)
    {
        List<ICache<TKey, TValue>> list = new ArrayList<>();
        for (Object name : options.value("CacheList", new HashSet()))
        {
            ICache<TKey, TValue> cache = null;

            if (_creators.containsKey(name))
            {
                try
                {
                    cache = _creators.get(name);
                }
                catch (Exception e)
                {
                    throw new RuntimeException("newInstance cache error use " + options);
                }
            }

            if (cache != null)
            {
                if (options.containsKey(cache.getName() + ".Expires"))
                    cache = new ExpiresCahce<>(cache);

                list.add(cache);
            }
            else
                throw new RuntimeException("newInstance cache error use " + options);
        }

        return list;
    }

    public static class Access<TKey, TValue>
    {
        private ICache<TKey, TValue> _cache;

        public Access(ICache<TKey, TValue> cache)
        {
            _cache = cache;
        }

        public TValue get(TKey key, TValue def)
        {
            TValue value = _cache.get(key);
            if (value == null)
            {
                return def;
            }
            else
                return value;
        }

        public ICache<TKey, TValue> cache()
        {
            return _cache;
        }

        public TValue get(TKey key)
        {
            return _cache.get(key);
        }

        public void put(TKey key, TValue value)
        {
            _cache.put(key, value);
        }
    }

    public static class Options extends HashMap<String, Object>
    {
        public Options option(String name, Object value)
        {
            super.put(name, value);
            return this;
        }

        public <T> T value(String name, T def)
        {
            if (containsKey(name))
                return (T) get(name);

            return def;
        }
    }

    public static class Builder
    {
        private Options _options = new Options();

        public class CacheOption
        {
            private String _name;

            public CacheOption(String name)
            {
                _name = name;
            }

            public CacheOption option(String name, Object value)
            {
                _options.put(_name + "." + name, value);
                return this;
            }

            public Builder apply()
            {
                Builder.this.addToSet("CacheList", _name);
                return Builder.this;
            }

            public CacheOption expires(long expires)
            {
                _options.option("Expires", expires);

                return this;
            }
        }

        public Options options()
        {
            return _options;
        }

        public Builder option(String name, Object value)
        {
            _options.put(name, value);
            return this;
        }

        public <T> T value(String name, T def)
        {
            if (_options.containsKey(name))
                return (T) _options.get(name);

            return def;
        }

        public CacheOption cache(String name)
        {
            return new CacheOption(name);
        }

        public CacheOption lru(int limit)
        {
            return new CacheOption("LRU").option("Limit", limit);
        }

        public CacheOption soft()
        {
            return new CacheOption("Soft");
        }

        public CacheOption memory()
        {
            return new CacheOption("Memory");
        }

        public Builder addToSet(String name, Object value)
        {
            Set<Object> list;
            if (_options.containsKey(name))
                list = (Set<Object>) _options.get(name);
            else
            {
                list = new HashSet<Object>();
                _options.put(name, list);
            }

            list.add(value);

            return this;
        }

        public Builder merge(Options options)
        {
            for (Map.Entry<String, Object> option : options.entrySet())
                if (!"CacheList".equals(option.getKey()))
                    _options.option(option.getKey(), option.getValue());

            return this;
        }

        public <TKey, TValue> Access<TKey, TValue> build()
        {
            return CacheManager.cache(_options);
        }
    }

    private static void handleException(Exception e)
    {
        e.printStackTrace();
    }
}
