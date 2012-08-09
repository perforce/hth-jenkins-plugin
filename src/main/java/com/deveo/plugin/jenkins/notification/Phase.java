package com.deveo.plugin.jenkins.notification;

import hudson.model.Run;
import hudson.model.TaskListener;

import java.io.IOException;
import java.util.List;


public enum Phase {
	STARTED, COMPLETED, FINISHED;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void handlePhase(Run run, String status, TaskListener listener) {
		DeveoNotificationProperty property = (DeveoNotificationProperty) run.getParent().getProperty(DeveoNotificationProperty.class);
		if (property != null) {
			List<Endpoint> targets = property.getEndpoints();
			for (Endpoint target : targets) {
                try {
                	StatusSender.send(target, status);
                } catch (IOException e) {
                    e.printStackTrace(listener.error("Failed to notify "+target));
                }
				
            }
		}
	}
}