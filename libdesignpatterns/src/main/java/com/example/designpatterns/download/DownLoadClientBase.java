package com.example.designpatterns.download;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class DownLoadClientBase
{
    public static void main(String[] args) throws IOException
    {
        DownLoadClientBase base = DownLoadClientBase.create();

        base.download("http://192.168.1.157:7898/group1/M00/00/02/wKgBnVuXbkaAYGTPABuKaKJhBfw2398257", "D:\\test1111111.apk", new DownloadListener()
        {
            @Override
            public void onCompleted(DownloadTask task)
            {
                System.out.println("suc");
            }

            @Override
            public void onError(Exception e)
            {
                System.out.println(e.getMessage());
            }

            @Override
            public void onProgress(long current, long total)
            {
                System.out.println("total:" + total + " current:" + current);
            }
        });

        /*System.in.read();

        base.download("http://jkl.hiouter.com:8080/apk/com.playrix.fishdomdd.gplay.apk", "C:\\Users\\longyu\\Downloads\\test.apk", new DownloadListener()
        {
            @Override
            public void onCompleted(DownloadTask task)
            {
                System.out.println("suc");
            }

            @Override
            public void onError(Exception e)
            {
                System.out.println(e.getMessage());
            }

            @Override
            public void onProgress(long current, long total)
            {
                System.out.print("\r2 total:" + total + " current:" + current);
            }
        });

        System.in.read();*/
    }

    public static final String Replace = "Replace";
    public static final String QueueMax = "QueueMax";


    public interface DownloadTaskListener
    {
        void onProgress(long current, long total);

        void onCompleted(DownloadTask task);

        void onError(Exception e);

        void onCancel();
    }

    public static abstract class DownloadListener implements DownloadTaskListener
    {
        public void onProgress(long current, long total)
        {

        }

        public void onCompleted(DownloadTask task)
        {

        }

        public void onError(Exception e)
        {

        }

        public void onCancel()
        {

        }
    }

    public static <T> T getValue(Map<String, Object> map, String name, T def)
    {
        Object value = map.get(name);

        if (value == null)
            return def;
        else return (T) value;
    }

    public static class DownloadTask
    {
        private String _url;
        private String _path;
        private Map<String, Object> _data;
        private String _flag;
        private DownloadingTask _host;

        private DownloadTaskListener _listener;
        private DownloadTaskListener _innerListener;

        public DownloadTask()
        {
            _data = new HashMap<String, Object>();
        }

        public DownloadTask(DownloadTaskListener listener)
        {
            _data = new HashMap<String, Object>();
            _listener = listener;
        }

        public String getFlag()
        {
            return _flag;
        }

        public void setFlag(String _flag)
        {
            this._flag = _flag;
        }

        public String getUrl()
        {
            return _url;
        }

        public void setUrl(String _url)
        {
            this._url = _url;
        }

        public void setPath(String _path)
        {
            this._path = _path;
        }

        public String getPath()
        {
            return _path;
        }

        public Map<String, Object> getData()
        {
            return _data;
        }

        public void setData(Map<String, Object> data)
        {
            _data = data;
        }

        public DownloadTaskListener getDownLoadTaskListener()
        {
            return _listener;
        }

        public void setDownLoadTaskListener(DownloadTaskListener _listener)
        {
            this._listener = _listener;
        }

        private void setInnerDownLoadTaskListener(DownloadTaskListener listener)
        {
            _innerListener = listener;
        }

        private DownloadTaskListener getInnerDownLoadTaskListener()
        {
            return _innerListener;
        }

        private DownloadingTask getHost()
        {
            return _host;
        }

        private void setHost(DownloadingTask task)
        {
            _host = task;
        }

        public <T> T param(String name, T def)
        {
            Object value = getData().get(name);

            if (value == null)
                return def;
            else
                return (T) value;
        }
    }

    // 下载任务
    static class DownloadingTask
    {
        private DownloadTask _task;

        private int _itemCount;
        private AtomicInteger _currentCount = new AtomicInteger(0);
        private long _total;
        private boolean _isError;
        private boolean _isCompleted;
        private boolean _isCancel = false;

        private List<DownLoadItemTask> _items;
        private DownloadTaskListener _listener;

        public String getUrl()
        {
            return _task.getUrl();
        }

        public void setUrl(String _url)
        {
            _task.setUrl(_url);
        }

        public void setPath(String _path)
        {
            _task.setPath(_path);
        }

        public String getPath()
        {
            return _task.getPath();
        }

        public Map<String, Object> getData()
        {
            return _task.getData();
        }

        public void setData(Map<String, Object> data)
        {
            _task.setData(data);
        }

        public DownloadTaskListener getDownLoadTaskListener()
        {
            if (_task.getInnerDownLoadTaskListener() != null)
                return _task.getInnerDownLoadTaskListener();

            if (_listener == null)
                return _task.getDownLoadTaskListener();

            return _listener;
        }

        public void setDownLoadTaskListener(DownloadTaskListener _listener)
        {
            this._listener = _listener;
        }

        public DownloadingTask(DownloadTask task, DownloadTaskListener listener)
        {
            _task = task;
            _listener = listener;
            _items = new ArrayList<DownLoadItemTask>();

            _task.setHost(this);
        }

        public DownloadingTask(DownloadTask task)
        {
            _task = task;
            _items = new ArrayList<DownLoadItemTask>();

            _task.setHost(this);
        }

        private void cancel()
        {
            _isCancel = true;
        }

        private boolean isCancel()
        {
            return _isCancel;
        }

        private long getTotal()
        {
            return _total;
        }

        private void setTotal(long _total)
        {
            this._total = _total;
        }

        private int getItemCount()
        {
            return _itemCount;
        }

        public String getFlag()
        {
            return _task.getFlag();
        }

        public void setFlag(String _flag)
        {
            _task.setFlag(_flag);
        }

        private void setItemCount(int _itemCount)
        {
            this._itemCount = _itemCount;
        }

        private int getCurrentCount()
        {
            return _currentCount.get();
        }

        private synchronized void submit()
        {
            _currentCount.incrementAndGet();
            if (getCurrentCount() == getItemCount())
            {
                try
                {
                    merge();
                    complete();
                }
                catch (IOException e)
                {
                    if (getDownLoadTaskListener() != null)
                        getDownLoadTaskListener().onError(e);
                }
                catch (Exception e)
                {
                    if (getDownLoadTaskListener() != null)
                        getDownLoadTaskListener().onError(e);
                }
            }
        }

        private synchronized void onError(Exception e)
        {
            if (!_isError)
            {
                _isError = true;

                if (getDownLoadTaskListener() != null)
                    getDownLoadTaskListener().onError(e);
            }
        }

        private synchronized void onCancel()
        {
            if (!_isError)
            {
                _isError = true;

                if (getDownLoadTaskListener() != null)
                    getDownLoadTaskListener().onCancel();
            }
        }

        private synchronized void complete()
        {
            _isCompleted = true;
            if (getDownLoadTaskListener() != null)
                getDownLoadTaskListener().onCompleted(_task);
        }

        private void progress()
        {
            if (getDownLoadTaskListener() != null)
                getDownLoadTaskListener().onProgress(getCurrent(), getTotal());
        }

        private void progress(long current, long total)
        {
            if (getDownLoadTaskListener() != null)
                getDownLoadTaskListener().onProgress(current, total);
        }

        private void merge() throws IOException
        {
            File file = IOManager.create(getPath() + ".temp");

            OutputStream out = new FileOutputStream(file);
            List<File> temps = new ArrayList<File>();
            for (DownLoadItemTask item : getItems())
            {
                File temp = new File(item.getPath());
                temps.add(temp);
                if (file.exists())
                {
                    InputStream in = new FileInputStream(temp);
                    IOManager.write(in, out);
                    in.close();
                }
            }

            for (File item : temps)
            {
                if (item.exists())
                    item.delete();
            }

            out.close();

            if (file.exists())
                file.renameTo(new File(getPath()));
        }

        private long getCurrent()
        {
            long current = 0;

            for (DownLoadItemTask item : _items)
            {
                current += item.getCrrent();
            }

            return current;
        }

        private List<DownLoadItemTask> getItems()
        {
            return _items;
        }

        @Override
        public boolean equals(Object o)
        {
            if (o == null)
                return false;

            if (!(o instanceof DownLoadItemTask))
                return false;

            return ((DownloadingTask) o).getPath().equals(getPath());
        }
    }

    class DownLoadItemTask
    {
        long _offset;
        long _size;
        String _path;
        long _crrent;

        public long getCrrent()
        {
            return _crrent;
        }

        public long getOffset()
        {
            return _offset;
        }

        public String getPath()
        {
            return _path;
        }

        public long getSize()
        {
            return _size;
        }

        public void setCrrent(long _crrent)
        {
            this._crrent = _crrent;
        }

        public void setOffset(long _offset)
        {
            this._offset = _offset;
        }

        public void setPath(String _path)
        {
            this._path = _path;
        }

        public void setSize(long _size)
        {
            this._size = _size;
        }

        public long getBegin()
        {
            return getOffset() + getCrrent();
        }

        public long getEnd()
        {
            return getOffset() + getSize() - 1;
        }
    }

    public static DownLoadClientBase createStatic()
    {
        return new StaticDownLoadClient();
    }

    public static DownLoadClientBase create()
    {
        return new HttpDownLoadClient();
    }

    public abstract int getDownloadTaskState(DownloadTask task);

    public abstract void download(DownloadTask task);

    public abstract void cancel(DownloadTask task);

    public void download(String url, String path, DownloadTaskListener listener)
    {
        DownloadTask task = new DownloadTask(listener);

        task.setUrl(url);
        task.setPath(path);

        download(task);
    }

    class TaskQueue
    {
        private Map<String, DownloadTask> _waitTasks;
        private Queue<DownloadTask> _waitQueue;
        private Map<String, DownloadTask> _downloadTasks;
        private int _downloadMax;
        private AtomicInteger _currentCount = new AtomicInteger(0);

        public TaskQueue(int max)
        {
            _downloadMax = max;
            _waitQueue = new LinkedList<DownloadTask>();
            _waitTasks = new ConcurrentHashMap<String, DownloadTask>();
            _downloadTasks = new ConcurrentHashMap<String, DownloadTask>();
        }

        public DownloadTask get(DownloadTask downloadTask)
        {
            if (_downloadTasks.containsKey(getKey(downloadTask)))
                return _downloadTasks.get(getKey(downloadTask));

            return null;
        }

        private String getKey(DownloadTask task)
        {
            return task.getPath();
        }

        public synchronized void put(DownloadTask task)
        {
            if (_waitTasks.containsKey(getKey(task)))
            {
                DownloadTask oldTask = _downloadTasks.get(getKey(task));
                oldTask.setDownLoadTaskListener(task.getDownLoadTaskListener());

                boolean replace = getValue(task.getData(), Replace, false);
                if (replace)
                    oldTask.setFlag(task.getFlag());

                return;
            }

            _waitTasks.put(getKey(task), task);
            _waitQueue.offer(task);
        }

        public synchronized DownloadTask get()
        {
            if (_currentCount.get() >= _downloadMax)
                return null;

            if (!_waitQueue.isEmpty())
                return _waitQueue.peek();

            return null;
        }

        public synchronized DownloadTask pop()
        {
            if (_currentCount.get() >= _downloadMax)
                return null;

            if (!_waitQueue.isEmpty())
            {
                _currentCount.incrementAndGet();
                DownloadTask task = _waitQueue.poll();
                _downloadTasks.put(getKey(task), task);
                return task;
            }

            return null;
        }

        public synchronized void remove(DownloadTask task)
        {
            if (_downloadTasks.containsKey(getKey(task)))
            {
                _waitTasks.remove(getKey(task));
                _downloadTasks.remove(getKey(task));
            }

            _currentCount.decrementAndGet();
        }
    }

    class TaskIssuer
    {
        private Map<String, TaskQueue> _map;

        public TaskIssuer()
        {
            _map = new ConcurrentHashMap<String, TaskQueue>();
        }

        public synchronized void put(DownloadTask task)
        {
            TaskQueue queue = _map.get(task.getFlag());
            if (queue != null)
            {
                queue.put(task);
            }
            else
            {
                int max = getValue(task.getData(), QueueMax, 1000);
                queue = new TaskQueue(max);

                queue.put(task);
                _map.put(task.getFlag(), queue);
            }
        }

        public DownloadTask get(String flag)
        {
            TaskQueue queue = _map.get(flag);

            if (queue != null)
                return queue.pop();

            return null;
        }

        public synchronized void remove(DownloadTask task)
        {
            TaskQueue queue = _map.get(task.getFlag());

            if (queue != null)
                queue.remove(task);
        }
    }

    static class StaticDownLoadClient extends DownLoadClientBase
    {
        private TaskIssuer _issuer;
        private DownLoadClientBase _client;

        public StaticDownLoadClient()
        {
            _issuer = new TaskIssuer();
            _client = create();
        }

        private void createListener(final DownloadTask task)
        {
            task.setInnerDownLoadTaskListener(new DownloadTaskListener()
            {
                @Override
                public void onProgress(long current, long total)
                {
                    if (task.getDownLoadTaskListener() != null)
                        task.getDownLoadTaskListener().onProgress(current, total);
                }

                @Override
                public void onCompleted(DownloadTask task)
                {
                    _issuer.remove(task);
                    beginDownload(task.getFlag());

                    if (task.getDownLoadTaskListener() != null)
                        task.getDownLoadTaskListener().onCompleted(task);
                }

                @Override
                public void onError(Exception e)
                {
                    _issuer.remove(task);
                    beginDownload(task.getFlag());

                    if (task.getDownLoadTaskListener() != null)
                        task.getDownLoadTaskListener().onError(e);
                }

                @Override
                public void onCancel()
                {
                    _issuer.remove(task);
                    beginDownload(task.getFlag());

                    if (task.getDownLoadTaskListener() != null)
                        task.getDownLoadTaskListener().onCancel();
                }
            });
        }

        @Override
        public int getDownloadTaskState(DownloadTask task)
        {
            return 0;
        }

        @Override
        public void download(DownloadTask task)
        {
            if (task.getFlag() == null)
                task.setFlag("def");

            _issuer.put(task);

            beginDownload(task.getFlag());
        }

        private void beginDownload(String flag)
        {
            DownloadTask task = _issuer.get(flag);

            if (task != null)
            {
                createListener(task);
                _client.download(task);
            }
        }

        @Override
        public void cancel(DownloadTask task)
        {

        }
    }

    /**
     * one step download a task
     */
    static class HttpDownLoadClient extends DownLoadClientBase
    {
        final static int ThreadCount = 3;
        final static long ChildPackageSize = 1024 * 1024 * 2;

        private DownLoader _downLoader;

        public HttpDownLoadClient()
        {
            _downLoader = new DownLoader.HttpDownLoader();
        }

        @Override
        public int getDownloadTaskState(DownloadTask task)
        {
            return 0;
        }

        @Override
        public void download(DownloadTask inTask)
        {
            final DownloadingTask task = new DownloadingTask(inTask);


            File file = new File(task.getPath());

            if (file.exists() && file.length() != 0)
            {
                task.progress(file.length(), file.length());
                task.complete();
                return;
            }

            try
            {
                long total = DownLoader.getContentLength(task.getUrl(), new HashMap<String, String>());

                if (total == -1)
                {
                    task.getDownLoadTaskListener().onError(new IOException("get " + task.getUrl() + " size fail"));
                    return;
                }

                startDownLoadTasks(task, total);
            }
            catch (Exception e)
            {
                task.onError(e);
            }
        }

        void startDownLoadTasks(final DownloadingTask task, long totalSize)
        {
            long childSize = ChildPackageSize;
            if (totalSize > ChildPackageSize * ThreadCount)
                childSize = totalSize / ThreadCount + 1;

            File file = new File(task.getPath());

            if (file.exists() && file.length() == totalSize)
            {
                task.progress(totalSize, totalSize);
                task.complete();
                return;
            }

            int count = 0;

            while (true)
            {

                DownLoadItemTask itemTask = new DownLoadItemTask();
                itemTask.setOffset(count * childSize);
                itemTask.setSize(childSize);
                File tempFile = new File(file.getParent(), file.getName() + count + ".temp");
                itemTask.setPath(tempFile.getPath());

                if (itemTask.getOffset() + itemTask.getSize() >= totalSize)
                {
                    itemTask.setSize(totalSize - itemTask.getOffset());
                    initItemTask(itemTask);
                    task.getItems().add(itemTask);
                    count++;
                    break;
                }
                else
                {
                    initItemTask(itemTask);
                    task.getItems().add(itemTask);
                    count++;
                }
            }

            task.setItemCount(count);
            task.setTotal(totalSize);

            if (task.getDownLoadTaskListener() != null)
                task.getDownLoadTaskListener().onProgress(task.getCurrent(), task.getTotal());

            if (task.getData().containsKey("step"))
            {

            }
            else
            {
                for (final DownLoadItemTask item : task.getItems())
                {
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            startDownLoadItemTask(task, item);
                        }
                    }).start();
                }
            }
        }


        void startDownLoadItemTask(final DownloadingTask task, final DownLoadItemTask itemTask)
        {
            if (itemTask.getCrrent() == itemTask.getSize())
            {
                task.submit();
                return;
            }

            DownLoader.Task loaderTask = new DownLoader.Task();
            loaderTask.setUrl(task.getUrl());
            loaderTask.setPath(itemTask.getPath());
            loaderTask.getParams().put("ConnectTimeout", 1000 * 15);
            loaderTask.getParams().put("ReadTimeout", 1000 * 60);
            loaderTask.getParams().put("IsOveride", false);

            Set<Integer> resSet = new HashSet<Integer>()
            {
                {
                    add(200);
                    add(206);
                }
            };

            loaderTask.getParams().put("AccptCodes", resSet);

            loaderTask.getHeaders().put("Range", String.format("bytes=%d-%d", itemTask.getBegin(), itemTask.getEnd()));

            final long pre = itemTask.getCrrent();

            _downLoader.download(loaderTask, new DownLoader.IDownLoaderListener()
            {
                @Override
                public void onCompleted(DownLoader.Task loaderTask)
                {
                    task.submit();
                }

                @Override
                public boolean onProgress(long total, long progress)
                {
                    itemTask.setCrrent(pre + progress);
                    task.progress();

                    return task.isCancel();
                }

                @Override
                public void onError(Exception e)
                {
                    task.cancel();
                    task.onError(e);
                }

                @Override
                public void onCancel()
                {
                    task.onCancel();
                }
            });

        }

        // 初始化下载子任务
        void initItemTask(DownLoadItemTask itemTask)
        {
            File file = new File(itemTask.getPath());

            if (file.exists() && file.isFile())
            {
                itemTask.setCrrent(file.length());
            }
            else itemTask.setCrrent(0);
        }

        @Override
        public void cancel(DownloadTask task)
        {
            if (task.getHost() != null)
                task.getHost().cancel();
        }
    }
}
