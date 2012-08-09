package com.deveo.plugin.jenkins.notification;

import hudson.model.JobProperty;
import hudson.model.AbstractProject;

import java.util.List;

import org.kohsuke.stapler.DataBoundConstructor;

public class DeveoNotificationProperty extends JobProperty<AbstractProject<?, ?>> {

	final public List<Endpoint> endpoints;

	@DataBoundConstructor
	public DeveoNotificationProperty(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	public List<Endpoint> getEndpoints() {
		return endpoints;
	}

	public DeveoNotificationPropertyDescriptor getDescriptor() {
		return (DeveoNotificationPropertyDescriptor) super.getDescriptor();
	}
}