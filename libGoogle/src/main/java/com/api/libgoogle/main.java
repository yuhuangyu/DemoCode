package com.api.libgoogle;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpHost;
//import org.apache.http.HttpResponse;
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.conn.params.ConnRouteParams;
//import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.Proxy;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;


import static com.api.libgoogle.HttpUtil.REQUEST_HEADER_USER_AGENT_KEY;
import static com.api.libgoogle.HttpUtil.REQUEST_HEADER_USER_AGENT_VALUE;
import static com.api.libgoogle.HttpUtil.connectTimeout;
import static com.api.libgoogle.HttpUtil.readTimeout;
import static java.net.HttpURLConnection.HTTP_PROXY_AUTH;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * Created by ASUS on 2018/6/27.
 */

public class main {

    private static OkHttpClient client;

    public static void main(String[] args) {
       /* String response = null;
        try {
            response = run("https://play.google.com/store");
//            response = run("http://www.csdn.net");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response);*/

//        test("https://www.google.com");
//        test("https://play.google.com/store");
//        test("http://www.csdn.net");

        int i = HttpUtil.proxyGetMethodDown("http://www.csdn.net");
        System.out.println("=====   "+i);
    }

    public static String run(String url) throws IOException {
        String proxy = "149.129.218.75";
        int port = 8888;
        final String user = "jojo";
        final String password = "5IBYnvAdtLsan60J";

/*
        Authenticator proxyAuthenticator = new Authenticator() {
            public Request authenticate(Route route, Response response) throws IOException {
                int responseCode = response.code();
                System.out.println("proxyAuthenticator for response: " + response);
                System.out.println("proxyAuthenticator Challenges: " + response.challenges());
                switch (responseCode) {
                    case HTTP_PROXY_AUTH:
                        Proxy selectedProxy = route != null
                                ? route.proxy()
                                : client.proxy();
                        if (selectedProxy.type() != Proxy.Type.HTTP) {
                            throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
                        }
                        // fall-through
                        String credential = Credentials.basic(user, password);
                        return response.request().newBuilder()
                                .header("Proxy-Authorization", credential)
                                .build();
                    case HTTP_UNAUTHORIZED:
                        return client.authenticator().authenticate(route, response);
                }
                return null;
                String credential = Credentials.basic(user, password);
                return response.request().newBuilder()
                        .header("Proxy-Authorization", credential)
                        .build();
            }
        };
*/

        Authenticator proxyAuthenticator = new Authenticator() {
            public Request authenticate(Route route, Response response) throws IOException {
                System.out.println("proxyAuthenticator for response: " + response);
                System.out.println("proxyAuthenticator Challenges: " + response.challenges());
                if (response != null && response.code() == 407)
                {
                    return null;
                }

                String credential = Credentials.basic(user, password);
                return response.request().newBuilder()
                        .header("Proxy-Authorization", credential)
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0")
                        .build();
            }
        };

        client = new OkHttpClient.Builder()
                .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1, Protocol.SPDY_3))
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy, port)))
                .proxyAuthenticator(proxyAuthenticator)
                .authenticator(proxyAuthenticator)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println("response " + response.code());
        return response.body().string();
    }

    //设置代理
    public static HttpClient getHttpClient() {
        /*DefaultHttpClient httpClient = new DefaultHttpClient();
        String proxyHost = "149.129.218.75";
        int proxyPort = 8888;
//        String userName = "a";
        String userName = "jojo";
        String password = "5IBYnvAdtLsan60J";
//        String password = "a";
        httpClient.getCredentialsProvider().setCredentials(
                new AuthScope(proxyHost, proxyPort),
                new UsernamePasswordCredentials(userName, password));
        HttpHost proxy = new HttpHost(proxyHost,proxyPort);
        httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
        return httpClient;*/
        String proxy = "149.129.218.75";
        int port = 8888;
        final String user = "jojo";
        final String password = "5IBYnvAdtLsan60J";
        HttpClient httpClient = new HttpClient();
        httpClient.getHostConfiguration().setProxy(proxy, port);
        httpClient.getParams().setAuthenticationPreemptive(true);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeout);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
        // 如果代理需要密码验证，这里设置用户名密码
        httpClient.getState().setProxyCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(user, password));
        return httpClient;
    }
    public static void test(String url){
        StringBuffer sb = new StringBuffer();
        //创建HttpClient实例
//        HttpClient client = getHttpClient();
        /*//创建httpGet
        HttpGet httpGet = new HttpGet(url);
        //执行
        try {
            HttpResponse response = client.execute(httpGet);

            HttpEntity entry = response.getEntity();

            if(entry != null)
            {
                InputStreamReader is = new InputStreamReader(entry.getContent());
                BufferedReader br = new BufferedReader(is);
                String str = null;
                while((str = br.readLine()) != null)
                {
                    sb.append(str.trim());
                }
                br.close();
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        String responseBodyAsString = "";
        try {
            HttpClient httpClient = getHttpClient();
            GetMethod getMethod = null;
            getMethod = new GetMethod(url);
            getMethod.addRequestHeader(REQUEST_HEADER_USER_AGENT_KEY, REQUEST_HEADER_USER_AGENT_VALUE);
            httpClient.executeMethod(getMethod);
            responseBodyAsString = getMethod.getResponseBodyAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("===   "+responseBodyAsString.toString());
    }

}
