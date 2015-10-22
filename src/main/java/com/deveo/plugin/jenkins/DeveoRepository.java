package com.deveo.plugin.jenkins;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeveoRepository {

    private static final String PATH_PATTERN = "\\A/?([^/]*)/projects/([^/]*)/repositories/(mercurial|git|subversion)/([^/]*)/?\\z";

    private String projectId;
    private String id;

    public DeveoRepository(String projectId, String id) {
        this.projectId = projectId;
        this.id = id;
    }

    public DeveoRepository(String repositoryURL) throws MalformedURLException, DeveoURLException {
        String repositoryPath = new URL(repositoryURL).getPath();
        Pattern pattern = Pattern.compile(PATH_PATTERN);
        Matcher matcher = pattern.matcher(repositoryPath);

        if (matcher.matches()) {
            this.projectId = matcher.group(2);
            this.id = matcher.group(4);
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
