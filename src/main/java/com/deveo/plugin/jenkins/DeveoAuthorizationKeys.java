package com.deveo.plugin.jenkins;

public class DeveoAuthorizationKeys {

    private String pluginKey;
    private String companyKey;
    private String accountKey;

    public DeveoAuthorizationKeys(String pluginKey, String companyKey, String accountKey) {
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
        StringBuilder builder = new StringBuilder();
        builder.append("deveo ");
        builder.append("plugin_key=\"" + pluginKey + "\",");
        builder.append("company_key=\"" + companyKey + "\",");
        builder.append("account_key=\"" + accountKey + "\"");
        return builder.toString();
    }

}
