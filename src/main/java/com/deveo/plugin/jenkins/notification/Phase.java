package com.deveo.plugin.jenkins.notification;

import hudson.EnvVars;
import hudson.model.Run;
import hudson.model.TaskListener;

import java.io.IOException;
import java.util.List;
import org.apache.commons.lang.StringUtils;


public enum Phase {
	STARTED, COMPLETED, FINISHED;
        
        private String getRevisionID(EnvVars environment) {
            String revisionID = environment.get("GIT_COMMIT");
            if (StringUtils.isBlank(revisionID))
                revisionID = environment.get("SVN_REVISION");
            return revisionID;
        }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void handlePhase(Run run, String status, TaskListener listener) {
                

		String revisionID = null;
                String buildUrl = null;
		try {
			EnvVars environment = run.getEnvironment(TaskListener.NULL);
			revisionID = getRevisionID(environment);
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
                                    StatusSender.send(target, status, revisionID, buildUrl);
                            } catch (IOException e) {
                                e.printStackTrace(listener.error("Failed to notify "+target));
                            }
				
                        }
		}
	}
}