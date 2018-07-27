package com.test.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by fj on 2018/7/27.
 */


public class IOServer {
    private final File file;
    private RandomAccessFile accessFile = null;
    private final Map<String, FileInfo> fileHead = new ConcurrentHashMap<>();
    private final Stack<FileInfo> headQueue = new Stack<>();
    private FileLock lock = null;
    private final Object writeLock = new Object();
    static final ExecutorService THREAD_SERVICE = Executors.newScheduledThreadPool(5);
    private Task task;
    private final Map<String, byte[]> writeCache = new ConcurrentHashMap<>(500);
    private final int _2MB = 2 * 1024 * 1024;

    private class Task {
        private final LinkedBlockingQueue<TaskInfo> taskInfoQueue = new LinkedBlockingQueue<>();
        private boolean isRunning = true;

        void addTask(TaskInfo taskInfo) {
            if (taskInfoQueue.contains(taskInfo)) {
                taskInfoQueue.remove(taskInfo);
            }
            taskInfoQueue.offer(taskInfo);
        }

        Task() {
            THREAD_SERVICE.execute(new Runnable() {
                @Override
                public void run() {
                    while (isRunning) {
                        try {
                            TaskInfo taskInfo = taskInfoQueue.take();
                            if (taskInfo != null) {
                                String op = taskInfo.op;
                                if ("cut".equals(op)) {
                                    cutImpl(taskInfo.key);
                                } else if ("write".equals(op)) {
                                    writeImpl(taskInfo.key, writeCache.get(taskInfo.key));
                                }
                                writeCache.remove(taskInfo.key);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            stop();
                        }
                    }
                }
            });
        }

        void stop() {
            isRunning = false;
            taskInfoQueue.clear();
        }
    }

    private class TaskInfo {
        String op;
        String key;

        TaskInfo(String op, String key) {
            this.op = op;
            this.key = key;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof TaskInfo) {
                TaskInfo objTask = (TaskInfo) obj;
                return objTask.key.equals(this.key) && objTask.op.equals(this.op);
            }
            return super.equals(obj);
        }
    }

    private void close() throws IOException {
        if (task != null) {
            task.stop();
        }
        task = null;
        if (lock != null) {
            lock.release();
        }
        lock = null;
        if (accessFile != null) {
            accessFile.close();
        }

        synchronized (writeLock) {
            writeLock.notifyAll();
        }
        accessFile = null;
    }

    void destroy() throws IOException {
        close();
        fileHead.clear();
        headQueue.clear();
        writeCache.clear();
    }

    private class FileInfo {
        final String key;
        long startPosition;
        int length;
        byte state;//0正常，-1被删除
        int dataSize;

        FileInfo(String key, long startPosition, int dataSize, int length, byte state) {
            this.key = key;
            this.startPosition = startPosition;
            this.dataSize = dataSize;
            this.length = length;
            this.state = state;
        }

        @Override
        public String toString() {
            return String.format("[key:%s,tartPosition:%s,length:%s,state:%s,dataSize:%s]", key, startPosition, length, state, dataSize);
        }
    }

    IOServer(File file) throws Exception {
        if (file == null) throw new FileNotFoundException();
        if (file.isDirectory()) {
            throw new FileNotFoundException();
        }
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new FileNotFoundException();
                }
            } catch (Exception e) {
                throw e;
            }
        }
        this.file = file;
        initFile(file);
    }

    private void initFile(File file) throws IOException {
        int rety = 3;
        while (true) {
            try {
                synchronized (IOServer.class) {
                    accessFile = new RandomAccessFile(file, "rw");
                    lock = accessFile.getChannel().lock();
                    readHead();
                    task = new Task();
                }
                break;
            } catch (Exception e) {
                e.printStackTrace();
                close();
                if (rety > 0)
                    rety--;
                else
                    throw e;
            }
        }
    }

    boolean write(String key, byte[] data) {
        try {
            writeCache.put(key, data);
            task.addTask(new TaskInfo("write", key));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    byte[] read(String key) {
        synchronized (writeLock) {
            if (writeCache.containsKey(key)) {
                byte[] res = writeCache.get(key);
                if (res != null)
                    return res;
            }
            if (!fileHead.containsKey(key)) {
                return null;
            }
            if (!file.exists()) {
                return null;
            }
            try {
                FileInfo info = fileHead.get(key);
                if (info == null) return null;
                if (info.state != 0) return null;
                long offset = info.startPosition;
                int length = info.length;
                byte[] data = new byte[length];
                synchronized (writeLock) {
                    accessFile.seek(offset);
                    int read = accessFile.read(data);
                    if (read != length)
                        return null;
                }
                return data;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    boolean cut(String key) {

        synchronized (writeLock) {
            if (!fileHead.containsKey(key)) {
                return false;
            }

            FileInfo info = fileHead.get(key);
            if (info != null) {
                info.state = -1;
            }

            if (writeCache.containsKey(key)) {
                writeCache.remove(key);
            }

            task.addTask(new TaskInfo("cut", key));
        }
        return true;
    }

    boolean remove(String key) {
        if (!fileHead.containsKey(key))
            return false;
        FileInfo info = fileHead.get(key);
        if (info == null)
            return false;
        return remove(info);
    }

    /**
     * 清除数据库垃圾，压缩数据功能
     *
     * @return
     */
    void clearRef() {
        LinkedList<FileInfo> queue = new LinkedList<>(headQueue);
        Iterator<FileInfo> it = queue.iterator();
        while (it.hasNext()) {
            FileInfo item = it.next();
            if (item != null && item.state == -1) {
                remove(item);
            }
        }
    }

    private void writeImpl(String key, byte[] data) throws Exception {
        if (data == null) return;
        synchronized (writeLock) {
            if (!file.exists() || !file.isFile()) {

                fileHead.clear();
                headQueue.clear();

                if (!file.createNewFile()) {
                    return;
                }

                initFile(file);
            }

            FileInfo info = null;
            if (fileHead.containsKey(key)) {
                info = fileHead.get(key);
                if (info != null) {
                    if (data.length > info.dataSize) {

                        remove(info);

                        info = createHead(key, data.length);
                        if (info == null) return;
                    } else {
                        info.length = data.length;
                        info.state = 0;
                        saveHead(info);
                    }
                }
            }

            if (info == null)
                info = createHead(key, data.length);

            if (info == null) return;

            fileHead.put(key, info);
            headQueue.push(info);
            accessFile.seek(info.startPosition);
            accessFile.write(data);
        }
    }

    private boolean cutImpl(String key) throws IOException {
        FileInfo info = fileHead.get(key);
        if (info == null) return false;
        info.state = -1;
        saveHead(info);
        return true;
    }

    private boolean remove(FileInfo info) {
        info.state = -1;
        try {
            synchronized (writeLock) {

                headQueue.remove(info);
                fileHead.remove(info.key);

                if (accessFile.length() <= 0) return true;
                //先保存头部，不然可能因为写入失败,后面会读出异常数据
                saveHead(info);

                LinkedList<FileInfo> queue = new LinkedList<>(headQueue);

                Iterator<FileInfo> it = queue.iterator();

                int keySize = getKeySize();
                int keyLen = getKeyStrLength(info.key);

                //先计算出整个head+body的尺寸
                int size = info.dataSize + getKeySize() + keyLen;

                long offsetStart = info.startPosition - keySize - keyLen;
                long offsetOriginal = info.startPosition + info.dataSize;

                if (accessFile.length() <= offsetOriginal) {
                    synchronized (writeLock) {
                        accessFile.setLength(accessFile.length() - size);
                    }
                    return true;
                }

                //2MB
                if (accessFile.length() <= _2MB) {
                    byte[] buffer = readBlock(offsetOriginal, (int) (accessFile.length() - offsetOriginal));
                    writeBlock(offsetStart, buffer);
                } else {
                    int block = (int) (accessFile.length() / _2MB);
                    int m = (int) (accessFile.length() % _2MB);
                    for (int i = 0; i < block; i++) {
                        offsetStart += _2MB * i;
                        if (i == block - 1) {
                            if (m != 0) {
                                byte[] buffer = readBlock(offsetOriginal + (_2MB * i), m);
                                writeBlock(offsetStart, buffer);
                                break;
                            }
                        }
                        byte[] buffer = readBlock(offsetOriginal + (_2MB * i), _2MB);
                        writeBlock(offsetStart, buffer);
                    }
                }

                while (it.hasNext()) {
                    FileInfo item = it.next();
                    if (item.startPosition > info.startPosition) {
                        item.startPosition = item.startPosition - size;
                        saveHead(item);
                    }
                }

                accessFile.setLength(accessFile.length() - size);

            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private int getKeySize() {
        return 21;
    }

    private void writeBlock(long start, byte[] buffer) {
        if (buffer == null || buffer.length == 0) return;
        try {
            synchronized (writeLock) {
                accessFile.seek(start);
                accessFile.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] readBlock(long start, int len) {
        if (len == 0)
            return null;
        try {
            if (start > accessFile.length()) return null;
            byte[] buffer;
            int read;
            synchronized (writeLock) {
                accessFile.seek(start);
                buffer = new byte[len];
                read = accessFile.read(buffer);
            }
            if (read != len) return null;
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private synchronized FileInfo createHead(String key, int dataSize) throws Exception {
        boolean needCreateMagic = accessFile.length() == 0;

        try {
            return createHeadImpl(key, dataSize);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (needCreateMagic) {
                    synchronized (writeLock) {
                        accessFile.seek(0);
                        accessFile.writeChar(0xfc);
                        accessFile.seek(2);
                        accessFile.writeInt(getVersion());
                    }
                }
            } catch (Exception e) {
                try {
                    close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                e.printStackTrace();
            }
        }
    }

    private int getVersion() {
        return 1;
    }

    /**
     * head 区域从第6个位置开始，前面的0-1 2个字节文件类型 2-5 4个字节当前版本
     * 每个key的长度=int4+key字符长度+long8文件开始偏移+int4文件长度+int4字段尺寸+byte1文件状态=21+key(size)
     */
    private FileInfo createHeadImpl(String key, int dataSize) throws Exception {
        long currentPos;
        synchronized (writeLock) {
            currentPos = accessFile.length();

            if (currentPos == 0) {
                currentPos = getMagicSize();
            }

            int keySize = getKeyStrLength(key);

            //key size int4
            accessFile.seek(currentPos);
            accessFile.writeInt(keySize);
            currentPos += 4;
            //key str
            accessFile.seek(currentPos);
            accessFile.write(key.getBytes());
            currentPos += keySize;
            //state
            accessFile.seek(currentPos);
            accessFile.writeByte(0);
            currentPos += 1;
            //body size int4
            accessFile.seek(currentPos);
            accessFile.writeInt(dataSize);
            currentPos += 4;
            //MaxSize
            accessFile.seek(currentPos);
            accessFile.writeInt(dataSize);
            currentPos += 4;
            //body startPos long8
            accessFile.seek(currentPos);
            currentPos += 8;
            accessFile.writeLong(currentPos);
        }
        return new FileInfo(key, currentPos, dataSize, dataSize, (byte) 0);
    }

    private int getKeyStrLength(String key) {
        return key.getBytes().length;
    }

    private int getMagicSize() {
        return 6;
    }

    private void saveHead(FileInfo info) throws IOException {
        int keyLen=getKeyStrLength(info.key);
        long pos = info.startPosition - getKeySize() - keyLen;
        if (pos <= 0) return;
        if (pos > accessFile.length()) return;
        try {
            synchronized (writeLock) {
                accessFile.seek(pos);
                accessFile.writeInt(keyLen);
                pos += 4;
                accessFile.seek(pos);
                accessFile.write(info.key.getBytes());
                pos += keyLen;
                accessFile.seek(pos);
                accessFile.writeByte(info.state);
                pos += 1;
                accessFile.seek(pos);
                accessFile.writeInt(info.length);
                pos += 4;
                accessFile.seek(pos);
                accessFile.writeInt(info.dataSize);
                pos += 4;
                accessFile.seek(pos);
                info.startPosition = pos + 8;
                accessFile.writeLong(info.startPosition);
            }
        } catch (IOException e) {
            throw e;
        }
    }

    private synchronized void readHead() throws IOException {
        fileHead.clear();
        headQueue.clear();
        if (accessFile.length() == 0) return;
        accessFile.seek(0);
        int type = accessFile.readChar();
        if (type != 0xfc || !file.exists()) {
            throw new IOException("head type error");
        }
        accessFile.seek(2);

        int position = getMagicSize();

        Runtime rt = Runtime.getRuntime();
        long maxMemory = rt.maxMemory();

        long fileSize = accessFile.length();

        do {
            if (accessFile.length() < position + 4) {
                break;
            }
            if (position > fileSize)
                throw new IOException("head len error");

            accessFile.seek(position);
            int keySize = accessFile.readInt();
            if (keySize <= 0 || keySize >= maxMemory || keySize > fileSize) {
                position++;
                continue;
            }
            position += 4;
            accessFile.seek(position);
            byte[] buffer = new byte[keySize];
            int read = accessFile.read(buffer);
            if (read != buffer.length) {
                continue;
            }
            String key = new String(buffer);
            position += keySize;

            accessFile.seek(position);
            byte state = accessFile.readByte();
            position += 1;

            accessFile.seek(position);
            int bodySize = accessFile.readInt();
            if (bodySize <= 0 || keySize >= maxMemory || bodySize > fileSize) {
                continue;
            }
            position += 4;
            accessFile.seek(position);

            int dataSize = accessFile.readInt();
            position += 4;
            accessFile.seek(position);

            long startPos = accessFile.readLong();
            if (startPos <= 0 || startPos > fileSize) {
                continue;
            }
            position += 8;
            position += dataSize;

            FileInfo info = new FileInfo(key, startPos, dataSize, bodySize, state);
            fileHead.put(key, info);
            headQueue.push(info);
        } while (true);
    }
}

