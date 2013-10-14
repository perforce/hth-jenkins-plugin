package com.deveo.plugin.jenkins.notification;

import hudson.util.FormValidation;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

public class Endpoint {
	private String accountKey;
	private String pluginKey;
	private String companyKey;
	private String companyName;
	private String projectName;
	private String repositoryName;
	private String apiURL;

	@DataBoundConstructor
	public Endpoint(String accountKey,
			String pluginKey, String companyKey, String companyName,
			String projectName, String repositoryName,
			String apiURL) {
		this.setAccountKey(accountKey);
		this.setPluginKey(pluginKey);
		this.setCompanyKey(companyKey);
		this.setCompanyName(companyName);
		this.setProjectName(projectName);
		this.setRepositoryName(repositoryName);
		this.setApiURL(apiURL);
	}


	public FormValidation doCheckURL(
			@QueryParameter(value = "url", fixEmpty = true) String url) {
		if (url.equals("111"))
			return FormValidation.ok();
		else
			return FormValidation.error("There's a problem here");
	}

	public String getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(String accountKey) {
		this.accountKey = accountKey;
	}

	public String getPluginKey() {
		return pluginKey;
	}

	public void setPluginKey(String pluginKey) {
		this.pluginKey = pluginKey;
	}

	public String getCompanyKey() {
		return companyKey;
	}

	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public String getApiURL() {
		return apiURL;
	}

	public void setApiURL(String apiURL) {
		this.apiURL = apiURL;
	}
}