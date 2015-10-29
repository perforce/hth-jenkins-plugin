package com.deveo.plugin.jenkins;

public class DeveoAPIKeys {

    private static final String FORMAT = "deveo plugin_key=\"%s\",company_key=\"%s\",account_key=\"%s\"";

    private String pluginKey;
    private String companyKey;
    private String accountKey;

    public DeveoAPIKeys(String pluginKey, String companyKey, String accountKey) {
        this.pluginKey = pluginKey;
        this.companyKey = companyKey;
        this.accountKey = accountKey;
    }

    public String getPluginKey() {
        return pluginKey;
    }

    public String getCompanyKey() {
        return companyKey;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public String toString() {
        return String.format(FORMAT, pluginKey, companyKey, accountKey);
    }

}
