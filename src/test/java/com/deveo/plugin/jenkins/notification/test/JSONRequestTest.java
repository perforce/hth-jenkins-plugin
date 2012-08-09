package com.deveo.plugin.jenkins.notification.test;

import org.junit.Test;

import junit.framework.TestSuite;

import com.deveo.plugin.jenkins.notification.Event;
import com.deveo.plugin.jenkins.notification.Request;

import net.sf.json.*;

public class JSONRequestTest extends TestSuite {

	@Test
	public void testRequest() {

		Request r = new Request();
		r.setAccount_key("aaaaaaaaa");
		
		Event event = new Event();
		event.setRepository("rrrrrrr");
		r.setEvent(event);
		JSONObject jsonObject = JSONObject.fromObject(r);
		System.out.println(jsonObject);
	}
}
