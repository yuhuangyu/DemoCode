package com.test.data;

import android.net.LocalSocket;
import android.text.TextUtils;
import android.util.Base64;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.test.data.IOServer.THREAD_SERVICE;

/**
 * Created by fj on 2018/7/27.
 */

public class ResponseInvoker {
    private InputStream inputStream;
    private OutputStream outputStream;
    private LocalSocket client;
    private IBambooServer server;

    public ResponseInvoker(IBambooServer server, LocalSocket client) throws FileNotFoundException {
        this.server = server;
        this.client = client;
        invoke();
    }

    private void invoke() {
        try {
            inputStream = client.getInputStream();
            outputStream = client.getOutputStream();
            recv();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.shutdownInput();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                client.shutdownOutput();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void recv() {
        byte[] buffer = new byte[1024];
        int read = 0;
        String value = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while (true) {
            try {
                read = inputStream.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            if (read > 0) {
                bos.write(buffer, 0, read);
                if (buffer[read - 1] == '\0') {
                    value = new String(bos.toByteArray()).trim();
                    bos = new ByteArrayOutputStream();
                    response(value);
                }
            }
        }
    }

    private void response(final String value) {
        THREAD_SERVICE.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject(value);
                    String k = object.optString("key");
                    String v = object.optString("value");
                    boolean isBase64 = object.optBoolean("isBase64", false);
                    String op = object.optString("op");
                    if (!TextUtils.isEmpty(k) && !TextUtils.isEmpty("op")) {
                        operation(op, k, v, isBase64);
                    } else {
                        answerError();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        answerError();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private void operation(String op, String key, String value, boolean isBase64) throws Exception {
        if ("set".equals(op)) {
            set(key, value, isBase64);
        } else if ("get".equals(op)) {
            get(key);
        } else if ("cut".equals(op)) {
            set(key, null, isBase64);
        } else if ("remove".equals(op)) {
            remove(key);
        } else if ("clearRef".equals(op)) {
            clearRef();
        } else {
            try {
                answerError();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void answerError() throws IOException {
        outputStream.write("".getBytes());
        outputStream.write('\0');
    }

    private void set(String key, String value, boolean isBase64) throws Exception {
        if (value == null) {
            if (server.cut(key)) {
                try {
                    outputStream.write("ok".getBytes());
                    outputStream.write('\0');
                } catch (IOException e) {
                    e.printStackTrace();
                    close();
                }
                return;
            }
        } else if (isBase64) {
            byte[] data = Base64.decode(value, Base64.NO_CLOSE | Base64.NO_PADDING | Base64.NO_WRAP);
            if (server.write(key, data)) {
                try {
                    outputStream.write("ok".getBytes());
                    outputStream.write('\0');
                } catch (IOException e) {
                    e.printStackTrace();
                    close();
                }
                return;
            }
        } else if (server.write(key, value)) {
            try {
                outputStream.write("ok".getBytes());
                outputStream.write('\0');
            } catch (IOException e) {
                e.printStackTrace();
                close();
            }
            return;
        }
        try {
            answerError();
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }

    }

    private void remove(String key) throws Exception {

        if (server.remove(key)) {
            try {
                outputStream.write("ok".getBytes());
                outputStream.write('\0');
                return;
            } catch (IOException e) {
                e.printStackTrace();
                close();
            }
        }
        try {
            answerError();
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    private void clearRef() throws Exception {

        if (server.clearRef()) {
            try {
                outputStream.write("ok".getBytes());
                outputStream.write('\0');
                return;
            } catch (IOException e) {
                e.printStackTrace();
                close();
            }
        }
        try {
            answerError();
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    private void close() {

    }

    private void get(String key) throws Exception {
        byte[] value = server.read(key).getBytes();

        try {
            outputStream.write(value);
            outputStream.write('\0');
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }
}

