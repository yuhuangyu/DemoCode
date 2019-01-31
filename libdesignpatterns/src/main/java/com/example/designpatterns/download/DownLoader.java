package com.example.designpatterns.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by anye6488 on 2016/9/5.
 */
public abstract class DownLoader
{
    public interface IDownLoaderListener
    {
        void onCompleted(Task task);

        boolean onProgress(long total, long progress);

        void onError(Exception e);

        void onCancel();
    }

    public static abstract class DownLoaderListener implements IDownLoaderListener
    {

        @Override
        public void onCompleted(Task task)
        {

        }

        @Override
        public boolean onProgress(long total, long progress)
        {
            return false;
        }

        @Override
        public void onError(Exception e)
        {

        }

        @Override
        public void onCancel()
        {

        }
    }

    public static class Task
    {
        private String url;
        private String path;
        private Map<String, Object> params = new HashMap<String, Object>();
        private Map<String, String> headers = new HashMap<String, String>();

        public String getUrl()
        {
            return url;
        }

        public void setUrl(String url)
        {
            this.url = url;
        }

        public String getPath()
        {
            return path;
        }

        public void setPath(String path)
        {
            this.path = path;
        }

        public Map<String, Object> getParams()
        {
            return params;
        }

        public <T> T param(String name, T def)
        {
            Object value = getParams().get(name);

            if (value == null)
                return def;
            else
                return (T) value;
        }

        public Map<String, String> getHeaders()
        {
            return headers;
        }
    }

    public abstract void download(Task task, IDownLoaderListener listener);

    public abstract void download(Task task) throws Exception;

    private static TrustManager myX509TrustManager = new X509TrustManager() {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

    };

    public static HttpURLConnection buildConnection(URL url) throws Exception {
        if (url.getProtocol().equals("http")) {
            return (HttpURLConnection) url.openConnection();
        } else {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{myX509TrustManager}, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            httpsURLConnection.setSSLSocketFactory(sslSocketFactory);
            return httpsURLConnection;
        }
    }

    public static long getContentLength(String uri, Map<String, String> headers) throws Exception
    {
        URL url = new URL(uri);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        HttpURLConnection connection = buildConnection(url);

        connection.setRequestMethod("HEAD");
        connection.setDoInput(true);
        connection.setConnectTimeout(15 * 1000);
        connection.setRequestProperty("Connection", "close");
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        for (String key : headers.keySet())
        {
            connection.setRequestProperty(key, headers.get(key));
        }

        int code = connection.getResponseCode();

        if(code == 200)
        {
            return  connection.getContentLength();
        }

        return  -1;
    }

    public static class HttpDownLoader extends DownLoader
    {
        @Override
        public void download(final Task task, final IDownLoaderListener listener)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        downloadFile(task, listener);

                        if (listener != null)
                            listener.onCompleted(task);
                    }
                    catch (Exception e)
                    {
                        if (listener != null)
                            listener.onError(e);
                    }
                }
            }, "DownloadThread").start();
        }

        public void download(final Task task) throws Exception
        {
            downloadFile(task, null);
        }

        private void downloadFile(Task task, IDownLoaderListener listener) throws Exception
        {
            URL url = new URL(task.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setConnectTimeout(task.param("ConnectTimeout", 15 * 1000));
            connection.setReadTimeout(task.param("ReadTimeout", 180 * 1000));
            connection.setRequestProperty("Connection", "close");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            for (String key : task.getHeaders().keySet())
            {
                connection.setRequestProperty(key, task.getHeaders().get(key));
            }

            int code = connection.getResponseCode();
            Set<Integer> resSet = task.param("AccptCodes", new HashSet<Integer>()
            {
                {
                    add(200);
                }
            });

            if (!resSet.contains(code))
            {
                throw new IOException("http response is " + code + " not in " + resSet);
            }

            InputStream in = connection.getInputStream();

            File file = new File(task.getPath());

            if (!file.exists())
            {
                if (file.getParentFile() != null && !file.getParentFile().exists())
                    file.getParentFile().mkdirs();

                file.createNewFile();
            }
            OutputStream out;
            if (task.param("IsOveride", true))
                out = new FileOutputStream(file);
            else
                out = new FileOutputStream(file, true);

            try
            {
                if (!write(in, out, connection.getContentLength(), listener))
                    if (listener != null)
                        listener.onCancel();
            }
            finally
            {
                in.close();
                out.close();
            }

            connection.disconnect();
        }

        private boolean write(InputStream in, OutputStream out, long total, IDownLoaderListener listener) throws Exception
        {
            int len = 0;
            long current = 0;
            byte[] buff = new byte[4 * 1024];

            while ((len = in.read(buff, 0, buff.length)) > 0)
            {
                out.write(buff, 0, len);
                out.flush();

                current += len;
                if (listener != null)
                    if (listener.onProgress(total, current))
                        return false;
            }

            return true;
        }
    }
}
