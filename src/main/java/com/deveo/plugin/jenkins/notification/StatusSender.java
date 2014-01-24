package com.deveo.plugin.jenkins.notification;

import net.sf.json.JSONObject;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

public class StatusSender {

    private static final Logger logger = Logger.getLogger("com.deveo.plugin");

    private static HttpURLConnection getConnection(URL url) throws IOException {
        return getConnection(url, false);
    }

    private static HttpURLConnection getConnection(URL url, boolean acceptInvalidSSLCertificate) throws IOException {
        if (acceptInvalidSSLCertificate) {  // This is here for testing in environments with invalid certs
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

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if (conn instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) conn).setSSLSocketFactory(sslSocketFactory);
                    ((HttpsURLConnection) conn).setHostnameVerifier(new HostnameVerifier() {
                        public boolean verify(String string, SSLSession ssls) {
                            return true;
                        }
                    });
                    return conn;
                }
                return (HttpURLConnection) conn;
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

    public static void send(Endpoint endPoint, String status, String revision, String buildUrl)
            throws IOException {

        URL url = new URL(endPoint.getApiUrl());
        HttpURLConnection connection = getConnection(url);

        String authParamValue = buildAuthenticationParameterValue(endPoint);

        connection.setRequestProperty("Authorization", authParamValue);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        String content = getRequestBody(endPoint, status, revision, buildUrl);
        connection.setRequestProperty("Content-Length",
                "" + Integer.toString(content.getBytes().length));

        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        // Send request
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(content);
        wr.flush();
        wr.close();


        // Get Response
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();

        while ((line = rd.readLine()) != null) {
            logger.info(line);
            response.append(line);
            response.append('\r');
        }
        rd.close();
    }

    private static String getRequestBody(Endpoint endPoint, String status, String revision, String buildUrl) {
        Request request = new Request();
        Event event = new Event();
        if (status.equals("SUCCESS")) {
            event.setOperation("completed");
        } else {
            event.setOperation("failed");
        }
        event.setCommits(new String[]{revision});
        event.setResources(new String[]{buildUrl});
        event.setProject(endPoint.getProjectId());
        event.setRepository(endPoint.getRepositoryId());

        request.setEvent(event);
        JSONObject jsonObject = JSONObject.fromObject(request);
        return jsonObject.toString();
    }

    private static String buildAuthenticationParameterValue(Endpoint endpoint) {
        StringBuilder builder = new StringBuilder();
        builder.append("deveo ");
        builder.append("plugin_key=\"" + endpoint.getPluginKey() + "\",");
        builder.append("company_key=\"" + endpoint.getCompanyKey() + "\",");
        builder.append("account_key=\"" + endpoint.getAccountKey() + "\"");
        return builder.toString();
    }

}
