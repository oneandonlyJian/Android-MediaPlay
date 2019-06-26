package com.diggoods.api.only_one;

import android.support.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by  FengJianyi on 2019/5/20
 */
public class Httputils {
    private static OkHttpClient okHttpClient;
    private static Httputils httputils;
    public ApiService apiService;
    private TrustManager tm;

    /**
     * 双重检验锁 单例 创建实体类
     *
     * @return
     */
    public static Httputils getInstanse() {
        if (null == httputils) {
            synchronized (Httputils.class) {
                if (null == httputils) {
                    httputils = new Httputils();
                }
            }
        }
        return httputils;
    }

    private Httputils() {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("TLS");
            tm = new MyTrustManager(readCert(CER_CLIENT));
            sc.init(null, new TrustManager[]{
                    tm
            }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(35, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)//设置超时)
                .addInterceptor(new LogginInstance())
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .sslSocketFactory(sc.getSocketFactory(), (X509TrustManager) tm)
                .hostnameVerifier(hostnameVerifier)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    /**
     * 自定义的，重试N次的拦截器
     * 通过：addInterceptor 设置
     */
    public static class Retry implements Interceptor {
        public int maxRetry;//最大重试次数
        private int retryNum = 0;//假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

        public Retry(int maxRetry) {
            this.maxRetry = maxRetry;
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            while (!response.isSuccessful() && retryNum < maxRetry) {
                retryNum++;
                response = chain.proceed(request);
            }
            return response;
        }
    }

    //拦截器
    class LogginInstance implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            //请求
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
//            builder.addHeader("ak", APKVersionCodeUtils.getVersionCode(App.getApp()) + "");
            builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
            Request build = builder.build();
            return chain.proceed(build);
        }
    }


    String CER_CLIENT = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDXTCCAkUCCQDNhr7+xMtU3jANBgkqhkiG9w0BAQUFADBoMQswCQYDVQQGEwJD\n" +
            "TjELMAkGA1UECAwCeDExCzAJBgNVBAcMAngyMQswCQYDVQQKDAJ4MzELMAkGA1UE\n" +
            "CwwCeDUxCzAJBgNVBAMMAmJ3MRgwFgYJKoZIhvcNAQkBFgkxQDE2My5jb20wHhcN\n" +
            "MTgwOTE3MTEyNjI2WhcNMjgwOTE0MTEyNjI2WjB5MQswCQYDVQQGEwJDTjELMAkG\n" +
            "A1UECAwCeDExCzAJBgNVBAcMAngyMQ4wDAYDVQQKDAViYXdlaTEPMA0GA1UECwwG\n" +
            "YmF3ZWkyMQswCQYDVQQDDAJidzEiMCAGCSqGSIb3DQEJARYTMTg2MDAxNTE1NjhA\n" +
            "MTYzLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMRn5BnuG5Qm\n" +
            "GBJV+aBdVpCPkXNs7sCZPpKT6K6gi1t2L88DOiZqsIi+06KsN55w/51YeuAHYq9w\n" +
            "QFx9X34eFB/n//SKA/qkWznxZdtsAJUD3hkKkR3jhj+JP1EZWxwgIP5Dp1RyuBxH\n" +
            "gEoe7UmK9o/V2hJ3HTAYF20vQquFltucl5svnmtQvF4aofhFQ3gqXYvXD6pxcIuI\n" +
            "UOePK49hnlxz7v5t5s/0VXHHz+5THsEg14oW+kAPFKVPS59tjQV7LzDMXjunEBzc\n" +
            "A/Jslafx32BF4Fy1aCbWCmIJSKou9MBnrP1MuheIpMO1qEMBXx/9MuLMFdnyj20N\n" +
            "9M+WlaMBiMUCAwEAATANBgkqhkiG9w0BAQUFAAOCAQEAJf/W2zTuf9D36js7766T\n" +
            "xpfWCVy0POqkdXNKvPThd/U6Qwi2QXc0CmNvr02lfVRu11cX4inR9RiJUXWoeG7J\n" +
            "DDWBSBPKTpeF8+k2w+DjDAkE3mj3iCQdeydkhCUYquSxtFNC6mFZ9zrkMs7sGuBc\n" +
            "GoDnueL8B2IiNfLtA3vUzvAkqh9b7rOBk1VXem4JFnIoisFufdzH1RhNWxZTgtlG\n" +
            "+Po5VSrMpKgtPYLHFIprMIUwGfW7j36hhvnEArEVXLWjY3hhNvyJ4jBf0WRp44GA\n" +
            "8OZ1zDEyVxxtOAQXXlfiYusPuy5Wup2P7RYo17xMVoHeQg6yF+iszlBHoJ5250iv\n" +
            "kA==\n" +
            "-----END CERTIFICATE-----";

    class MyTrustManager implements X509TrustManager {
        X509Certificate cert;

        MyTrustManager(X509Certificate cert) {
            this.cert = cert;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // 我们在客户端只做服务器端证书校验。
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // 确认服务器端证书和代码中 hard code 的 CRT 证书相同。
            if (chain[0].equals(this.cert)) {

                return;// found match
            }
            throw new CertificateException("checkServerTrusted No trusted server cert found!");
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    //主机地址验证
    final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            boolean equals = hostname.equals("api.apiopen.top");
            return equals;
        }
    };

    /**
     * 根据字符串读取出证书
     *
     * @param cer
     * @return
     */
    private static X509Certificate readCert(String cer) {
        if (cer == null || cer.trim().isEmpty())
            return null;
        InputStream caInput = new ByteArrayInputStream(cer.getBytes());
        X509Certificate cert = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            cert = (X509Certificate) cf.generateCertificate(caInput);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (caInput != null) {
                    caInput.close();
                }
            } catch (Throwable ex) {
            }
        }
        return cert;
    }


}