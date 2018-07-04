package com.example.ListandMap;

/**
 * Created by anye6488 on 2017/8/2.
 * 注释暂时没有,BUG应该会有
 * 请耐心细心用心体会 ╮(╯▽╰)╭
 */

public class MapMain
{
    public static void main(String[] args)
    {
        HashMap<String, String> map = new HashMap<>(30);

        map.put("anye", "12");
        map.put("anye", "13");
       // map.remove("anye");

        System.out.print(map.get("anye"));
    }

    public static class HashMap<TKey, TValue>
    {
        public class Entry<TKey, TValue>
        {
            public TKey key;
            public TValue value;

            private Entry<TKey, TValue> next;
        }

        private Entry<TKey, TValue>[] _entrys;
        private int _cap;

        public HashMap(int cap)
        {
            _cap = cap;
            _entrys = new Entry[cap];
        }

        private int index(TKey key)
        {
            return key.hashCode() % _cap;
        }

        public boolean remove(TKey key)
        {
            int index = index(key);

            if (_entrys[index] == null)
                return false;

            Entry<TKey, TValue> entry = _entrys[index];

            if (entry.key.equals(key))
            {
                _entrys[index] = null;
                return true;
            }

            Entry<TKey, TValue> last;
            while (true)
            {
                last = entry;
                entry = entry.next;

                if (entry == null)
                {
                    return false;
                }

                if (entry.key.equals(key))
                {
                    last.next = entry.next;
                }
            }
        }

        public TValue get(TKey key)
        {
            int index = index(key);

            if (_entrys[index] == null)
                return null;

            Entry<TKey, TValue> entry = _entrys[index];

            do
            {
                if (entry.key.equals(key))
                    return entry.value;
            }
            while ((entry = entry.next) != null);

            return null;
        }

        public void put(TKey key, TValue value)
        {
            int index = index(key);

            Entry<TKey, TValue> entry = new Entry<>();
            entry.key = key;
            entry.value = value;
            if (_entrys[index] == null)
            {
                _entrys[index] = entry;
            }
            else
            {
                Entry<TKey, TValue> e = _entrys[index];
                Entry<TKey, TValue> last = null;
                while (e != null)
                {
                    if(e.key.equals(key))
                    {
                        if(last == null)
                        {
                            _entrys[index] = entry;
                            return;
                        }
                        else
                        {
                            last.next = entry;
                            entry.next = e.next;
                            return;
                        }
                    }

                    last = e;
                    e = e.next;
                }

                e.next = entry;
            }
        }
    }
}
