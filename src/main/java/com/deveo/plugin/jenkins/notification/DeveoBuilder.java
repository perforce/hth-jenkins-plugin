package com.deveo.plugin.jenkins.notification;

import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.tasks.Builder;

import java.io.IOException;
import java.io.Serializable;

public class DeveoBuilder extends Builder implements Serializable {

	
	static final String GIT_COMMIT = "GIT_COMMIT";
	
	@Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) 
    		throws InterruptedException, IOException {
		
		String gitCommit = build.getEnvironment().get(GIT_COMMIT);
		return true;
	}

}
