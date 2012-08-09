package com.deveo.plugin.jenkins.notification;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

public class StatusSender {

	public static void send(Endpoint endPoint, String status)
			throws IOException {

		URL url = new URL(endPoint.getApiURL());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");

		String content = getRequestBody(endPoint, status);
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
			response.append(line);
			response.append('\r');
		}
		rd.close();
	}

	private static String getRequestBody(Endpoint endPoint, String status) {
		Request request = new Request();
		request.setAccount_key(endPoint.getAccountKey());
		request.setPlugin_key(endPoint.getPluginKey());
		request.setCompany_key(endPoint.getCompanyKey());

		Event event = new Event();
		if (status.equals("SUCCESS")) {
			event.setOperation("completed");
		} else {
			event.setOperation("failed");
		}
		event.setCommit(new String[] { "xxx" });
		event.setScope(endPoint.getCompanyName());
		event.setResources(new String[] { endPoint.getBuildURL() });
		event.setProject(endPoint.getProjectName());
		event.setRepository(endPoint.getRepositoryName());
		request.setEvent(event);

		JSONObject jsonObject = JSONObject.fromObject(request);
		return jsonObject.toString();
	}
}
