package com.deveo.plugin.jenkins;

import javax.net.ssl.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class DeveoAPI {

    private String apiUrl;

    private DeveoAuthorizationKeys deveoAuthorizationKeys;

    public DeveoAPI(String apiUrl, DeveoAuthorizationKeys deveoAuthorizationKeys) {
        this.apiUrl = apiUrl;
        this.deveoAuthorizationKeys = deveoAuthorizationKeys;
    }

    public DeveoAuthorizationKeys getDeveoAuthorizationKeys() {
        return deveoAuthorizationKeys;
    }

    private static HttpURLConnection getConnection(URL url) throws IOException {
        return getConnection(url, false);
    }

    private static HttpURLConnection getConnection(URL url, boolean acceptInvalidSSLCertificate) throws IOException {
        if (acceptInvalidSSLCertificate) { // This is here for testing in environments with invalid certs
            try {
                final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
                    }

                    @Override
                    public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }};

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                if (connection instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) connection).setSSLSocketFactory(sslSocketFactory);
                    ((HttpsURLConnection) connection).setHostnameVerifier(new HostnameVerifier() {
                        public boolean verify(String string, SSLSession ssls) {
                            return true;
                        }
                    });
                    return connection;
                }
                return connection;
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            } catch (KeyManagementException ex) {
                ex.printStackTrace();
            }
            return null;
        } else {
            return (HttpURLConnection) url.openConnection();
        }
    }

    public void create(String endpoint, String content) {
        URL url;
        HttpURLConnection connection;
        try {
            url = new URL(String.format("%s/%s", apiUrl, endpoint));

            connection = getConnection(url);
            connection.setRequestProperty("Authorization", getDeveoAuthorizationKeys().toString());
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", String.valueOf(content.getBytes().length));
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(content);
            wr.flush();
            wr.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
