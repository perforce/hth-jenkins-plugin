package com.deveo.plugin.jenkins;

import net.sf.json.JSONObject;

public class DeveoEvent {

    private String target = "build";
    private String operation;
    private String project;
    private String repository;
    private String[] commits;
    private String[] resources;

    public DeveoEvent(String operation, DeveoRepository deveoRepository, String revisionId, String buildUrl) {
        this.project = deveoRepository.getProjectId();
        this.repository = deveoRepository.getId();
        this.operation = operation;
        this.commits = new String[]{revisionId};
        this.resources = new String[]{buildUrl};
    }

    public String toJSON() {
        JSONObject json = new JSONObject();
        json.put("event", JSONObject.fromObject(this));
        return json.toString();
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String[] getCommits() {
        return commits;
    }

    public void setCommits(String[] commit) {
        this.commits = commit;
    }

    public String[] getResources() {
        return resources;
    }

    public void setResources(String[] resources) {
        this.resources = resources;
    }

}
