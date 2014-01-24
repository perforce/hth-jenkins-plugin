package com.deveo.plugin.jenkins.notification;

import hudson.util.FormValidation;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

public class Endpoint {

    private String accountKey;
    private String pluginKey;
    private String companyKey;
    private String companyId;
    private String projectId;
    private String repositoryId;
    private String apiUrl;

    @DataBoundConstructor
    public Endpoint(String accountKey,
                    String pluginKey, String companyKey, String companyId,
                    String projectId, String repositoryId,
                    String apiUrl) {
        this.setAccountKey(accountKey);
        this.setPluginKey(pluginKey);
        this.setCompanyKey(companyKey);
        this.setCompanyId(companyId);
        this.setProjectId(projectId);
        this.setRepositoryId(repositoryId);
        this.setApiUrl(apiUrl);
    }

    public FormValidation doCheckURL(@QueryParameter(value = "url", fixEmpty = true) String url) {
        if (url.equals("111")) {
            return FormValidation.ok();
        } else {
            return FormValidation.error("There's a problem here");
        }
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

}
