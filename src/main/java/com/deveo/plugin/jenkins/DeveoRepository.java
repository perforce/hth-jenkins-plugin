package com.deveo.plugin.jenkins;

public class DeveoRepository {

    private String projectId;
    private String id;

    public DeveoRepository(String projectId, String id) {
        this.projectId = projectId;
        this.id = id;
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
