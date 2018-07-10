package cc.lijingbo.androidstudy.imageloader_ren;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @作者: lijingbo
 * @日期: 2018-07-09 22:36
 */

public class HttpClient_li {

    private static HttpClient_li INSTANCE = new HttpClient_li();
    private OkHttpClient client;

    public static HttpClient_li getInstance() {
        return INSTANCE;
    }

    private HttpClient_li() {
        client = new Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(createSSLSocketFactory(), new TrustAllManager()).build();
    }

    public InputStream get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response execute = client.newCall(request).execute();
        if (execute != null && execute.isSuccessful()) {
            return execute.body().byteStream();
        } else {
            return null;
        }
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory socketFactory = null;
        try {
            SSLContext tsl = SSLContext.getInstance("TLS");
            tsl.init(null, new TrustManager[]{new TrustAllManager()}, null);
            socketFactory = tsl.getSocketFactory();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return socketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
