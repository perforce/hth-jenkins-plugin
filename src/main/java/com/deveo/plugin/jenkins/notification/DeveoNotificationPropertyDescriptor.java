package com.deveo.plugin.jenkins.notification;

import hudson.Extension;
import hudson.model.Job;
import hudson.model.JobPropertyDescriptor;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

import java.util.ArrayList;
import java.util.List;

@Extension
public final class DeveoNotificationPropertyDescriptor extends JobPropertyDescriptor {

    public DeveoNotificationPropertyDescriptor() {
        super(DeveoNotificationProperty.class);
        load();
    }

    private List<Endpoint> endpoints = new ArrayList<Endpoint>();

    public boolean isEnabled() {
        return !endpoints.isEmpty();
    }

    public List<Endpoint> getTargets() {
        return endpoints;
    }

    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public boolean isApplicable(@SuppressWarnings("rawtypes") Class<? extends Job> jobType) {
        return true;
    }

    public String getDisplayName() {
        return "Jenkins Job Notification";
    }

    @Override
    public DeveoNotificationProperty newInstance(StaplerRequest req, JSONObject formData) throws FormException {

        List<Endpoint> endpoints = new ArrayList<Endpoint>();
        if (formData != null && !formData.isNullObject()) {
            JSON endpointsData = (JSON) formData.get("endpoints");
            if (endpointsData != null && !endpointsData.isEmpty()) {
                if (endpointsData.isArray()) {
                    JSONArray endpointsArrayData = (JSONArray) endpointsData;
                    endpoints.addAll(req.bindJSONToList(Endpoint.class, endpointsArrayData));
                } else {
                    JSONObject endpointsObjectData = (JSONObject) endpointsData;
                    endpoints.add(req.bindJSON(Endpoint.class, endpointsObjectData));
                }
            }
        }
        DeveoNotificationProperty notificationProperty = new DeveoNotificationProperty(endpoints);
        return notificationProperty;
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject formData) {
        save();
        return true;
    }

}
