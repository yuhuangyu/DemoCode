package com.test.data;

import android.net.LocalSocket;
import android.util.Base64;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by fj on 2018/7/27.
 */

class BambooClient implements IBambooServer {


    private final LocalSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean isConnected;

    public BambooClient(LocalSocket socket) throws IOException {
        isConnected = true;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        this.socket = socket;
    }

    @Override
    public boolean write(String key, String data) throws Exception {

        ResultWrapper<Boolean> result = new ResultWrapper<>(false);

        JSONObject object = new JSONObject();
        try {
            object.put("key", key);
            object.put("value", data);
            object.put("op", "set");
            String value = object.toString();
            outputStream.write(value.getBytes());
            outputStream.write('\0');

            String res = null;
            res = readFrom();
            if ("ok".equals(res)) {
                result.set(true);
            }

            return result.get();
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    @Override
    public String read(String key) throws Exception {

        ResultWrapper<String> result = new ResultWrapper<>("");

        JSONObject object = new JSONObject();
        try {
            object.put("key", key);
            object.put("op", "get");
            String value = object.toString();
            outputStream.write(value.getBytes());
            outputStream.write('\0');

            String res = null;
            try {
                res = readFrom();
                result.set(res);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result.get();
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    /**
     * 保存数据，会覆盖前面的数据
     *
     * @param key
     * @param data
     * @return
     * @throws Exception
     */
    @Override
    public boolean write(String key, byte[] data) throws Exception {
        ResultWrapper<Boolean> result = new ResultWrapper<>(false);

        JSONObject object = new JSONObject();
        try {
            object.put("key", key);
            object.put("isBase64", true);
            object.put("value", Base64.encodeToString(data, Base64.NO_CLOSE | Base64.NO_PADDING | Base64.NO_WRAP));
            object.put("op", "set");
            String value = object.toString();
            outputStream.write(value.getBytes());
            outputStream.write('\0');

            String res = null;
            res = readFrom();
            if ("ok".equals(res)) {
                result.set(true);
            }

            return result.get();
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    /**
     * 读取数据，读取失败或没有值返回空字符“”
     *
     * @param key
     * @return
     * @throws Exception
     */
    @Override
    public byte[] readBytes(String key) throws Exception {

        JSONObject object = new JSONObject();
        try {
            object.put("key", key);
            object.put("op", "get");
            String value = object.toString();
            outputStream.write(value.getBytes());
            outputStream.write('\0');
            return readBytes();
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    @Override
    public boolean cut(String key) throws Exception {
        ResultWrapper<Boolean> result = new ResultWrapper<>(false);

        JSONObject object = new JSONObject();
        try {
            object.put("key", key);
            object.put("op", "cut");
            String value = object.toString();
            outputStream.write(value.getBytes());
            outputStream.write('\0');

            String res = null;
            res = readFrom();
            if ("ok".equals(res)) {
                result.set(true);
            }
        } catch (Exception e) {
            close();
            throw e;
        }

        return result.get();
    }

    @Override
    public boolean remove(String key) throws Exception {
        ResultWrapper<Boolean> result = new ResultWrapper<>(false);

        JSONObject object = new JSONObject();
        try {
            object.put("key", key);
            object.put("op", "remove");
            String value = object.toString();
            outputStream.write(value.getBytes());
            outputStream.write('\0');

            String res = null;
            res = readFrom();
            if ("ok".equals(res)) {
                result.set(true);
            }
        } catch (Exception e) {
            close();
            throw e;
        }

        return result.get();
    }

    @Override
    public boolean clearRef() throws Exception {
        final ResultWrapper<Boolean> result = new ResultWrapper<>(false);

        JSONObject object = new JSONObject();
        try {
            object.put("op", "clearRef");
            String value = object.toString();
            outputStream.write(value.getBytes());
            outputStream.write('\0');

            String res = null;
            res = readFrom();
            if ("ok".equals(res)) {
                result.set(true);
            }
            return result.get();
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    @Override
    public boolean isClose() {
        return !isConnected;
    }

    private void close() {
        isConnected = false;
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String readFrom() throws IOException {
        byte[] buffer = new byte[1024];
        int read = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while (read != -1) {
            read = inputStream.read(buffer);
            if (read > 0) {
                bos.write(buffer, 0, read);
                if (buffer[read - 1] == '\0') {
                    return new String(bos.toByteArray()).trim();
                }
            }
        }
        return "";
    }

    private byte[] readBytes() throws IOException {
        byte[] buffer = new byte[1024];
        int read = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while (read != -1) {
            read = inputStream.read(buffer);
            if (read > 0) {
                bos.write(buffer, 0, read);
                if (buffer[read - 1] == '\0') {
                    return bos.toByteArray();
                }
            }
        }
        return null;
    }
}