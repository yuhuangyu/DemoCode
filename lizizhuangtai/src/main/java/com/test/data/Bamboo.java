package com.test.data;

import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * Created by fj on 2018/7/27.
 */

public class Bamboo {


    private LocalServerSocket serverSocket;
    private LocalSocket server;

    private String addr;
    private String read_file_name = "";
    private boolean isCreate;
    private IOServer ioServer;
    private IBambooServer bambooServer;
    public static final byte END_TAG = -0x10;

    /**
     * <p>竹子数据持久化系统，创意来源：因数据结构像竹子的节一样而得名</p>
     * <p>该系统应该配合数据缓存使用，因为直接操作io，会降低系统性能</p>
     * <p>理论上该数据支持物理最大化存储，数据结构应当保持不变，不变的前提是初始化数据的长度要尽量的合理，否则导致后面的长度变长而
     * 导致性能下降</p>
     * <p>该系统容量扩展会根据内容增加而增加，但是大小基本上跟数据大小一致</p>
     * <p>使用方式：
     * 先初始化
     *
     * @see com.test.data.Bamboo.getInstance().init()
     * 再获取服务
     * @see Bamboo.getInstance().getBambooServer().remove(key);
     * @see Bamboo.getInstance().getBambooServer().read(key);
     * @see Bamboo.getInstance().getBambooServer().write(key, value);
     * @see Bamboo.getInstance().getBambooServer().cut(key)
     * @see Bamboo.getInstance().getBambooServer().clearRef()
     * <p>
     * 系统优缺点：
     * 优点，支持多进程，对增量写友好，不耗费多余资源，对文件操作快速（因为基于随机读写）
     * 缺点：扩展实体长度困难，收缩缓慢
     * </p>
     */

    public IBambooServer getBambooServer() {
        if (!isCreate || bambooServer.isClose()) {
            try {
                init(new File(read_file_name));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bambooServer;
    }

    private void setFile(File file) throws Exception {
        if (file == null) {
            throw new NullPointerException("setFile is null");
        }

        if (!file.exists() || !file.isFile()) {
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                throw new Exception("create file dir fail:" + file);
            }
            if (!file.createNewFile()) {
                throw new Exception("create file fail:" + file);
            }
        }
        read_file_name = file.getAbsolutePath();

        byte[] hash = MessageDigest.getInstance("MD5").digest(read_file_name.getBytes());
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        this.addr = hex.toString();
    }

    public Bamboo(File file) throws Exception {
        init(file);
    }

    private void init(File file) throws Exception {
        if (isCreate && !bambooServer.isClose()) return;
        setFile(file);
        try {
            serverSocket = new LocalServerSocket(addr);
            ioServer = new IOServer(file);
            bambooServer = new BambooServer(serverSocket, ioServer);
            isCreate = true;
        } catch (Exception e) {
            System.out.println("connect");
            server = new LocalSocket();
            try {
                server.connect(new LocalSocketAddress(addr));
                bambooServer = new BambooClient(server);
                isCreate = true;
            } catch (Exception e1) {
                isCreate = false;
                e1.printStackTrace();
            }
        }
    }

    public void close() throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }

        if (server != null) {
            server.close();
        }

        if (ioServer != null) {
            ioServer.destroy();
        }
    }
}
