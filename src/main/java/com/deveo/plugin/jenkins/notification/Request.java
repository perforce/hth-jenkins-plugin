package com.deveo.plugin.jenkins.notification;


public class Request {
	private String account_key;
	private String plugin_key;
	private String company_key;
	private Event event;
	public String getAccount_key() {
		return account_key;
	}
	public void setAccount_key(String account_key) {
		this.account_key = account_key;
	}
	public String getPlugin_key() {
		return plugin_key;
	}
	public void setPlugin_key(String plugin_key) {
		this.plugin_key = plugin_key;
	}
	public String getCompany_key() {
		return company_key;
	}
	public void setCompany_key(String company_key) {
		this.company_key = company_key;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
}
