package com.deveo.plugin.jenkins;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class DeveoAPI {

    private String apiUrl;

    private DeveoAPIKeys deveoAPIKeys;

    public DeveoAPI(String apiUrl, DeveoAPIKeys deveoAPIKeys) {
        this.apiUrl = apiUrl;
        this.deveoAPIKeys = deveoAPIKeys;
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

    public void create(String endpoint, String content) throws DeveoException {
        URL url;
        HttpURLConnection connection;
        try {
            url = new URL(String.format("%s/%s", apiUrl, endpoint));

            connection = getConnection(url);
            connection.setRequestProperty("Authorization", deveoAPIKeys.toString());
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

            if (connection.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                StringBuffer responseContent = new StringBuffer();
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    String responseLine;
                    while ((responseLine = in.readLine()) != null) {
                        responseContent.append(responseLine);
                    }
                    in.close();
                } catch (Exception ex) {
                    // Ignore
                } finally {
                    throw new DeveoException(String.format("%s %s - %s", connection.getResponseCode(), connection.getResponseMessage(), responseContent.toString()));
                }
            }
        } catch (MalformedURLException e) {
            throw new DeveoException(String.format("Deveo API URL could not be parsed: %s", apiUrl));
        } catch (ProtocolException e) {
            throw new DeveoException(String.format("Deveo connection could not be established due to ProtocolException: %s", e.getMessage()));
        } catch (IOException e) {
            throw new DeveoException(String.format("Deveo connection could not be established due to IOException: %s", e.getMessage()));
        }
    }

}
