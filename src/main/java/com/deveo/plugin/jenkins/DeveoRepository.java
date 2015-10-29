package com.deveo.plugin.jenkins;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeveoRepository {

    private static final String PATH_PATTERN = ".+/projects/([^/]+)/repositories/(?:mercurial|git|subversion)/([^/]+)";

    private String projectId;
    private String id;

    public DeveoRepository(String projectId, String id) {
        this.projectId = projectId;
        this.id = id;
    }

    public DeveoRepository(String repositoryURL) throws DeveoURLException {
        Pattern pattern = Pattern.compile(PATH_PATTERN);
        Matcher matcher = pattern.matcher(repositoryURL);

        if (matcher.matches()) {
            this.projectId = matcher.group(1);
            this.id = matcher.group(2);
        } else {
            throw new DeveoURLException("The URL doesn't appear to be a Deveo URL.");
        }
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
