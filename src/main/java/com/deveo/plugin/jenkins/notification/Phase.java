package com.deveo.plugin.jenkins.notification;

import hudson.EnvVars;
import hudson.model.Run;
import hudson.model.TaskListener;

import java.io.IOException;
import java.util.List;




public enum Phase {
	STARTED, COMPLETED, FINISHED;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void handlePhase(Run run, String status, TaskListener listener) {
                

		String gitCommit = null;
                String buildUrl = null;
		try {
			EnvVars environment = run.getEnvironment(TaskListener.NULL);
			gitCommit = environment.get("GIT_COMMIT");
                        buildUrl = environment.get("BUILD_URL");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		DeveoNotificationProperty property = (DeveoNotificationProperty) run.getParent().getProperty(DeveoNotificationProperty.class);
		if (property != null) {
			List<Endpoint> targets = property.getEndpoints();
			for (Endpoint target : targets) {
                            try {
                                    StatusSender.send(target, status, gitCommit, buildUrl);
                            } catch (IOException e) {
                                e.printStackTrace(listener.error("Failed to notify "+target));
                            }
				
                        }
		}
	}
}