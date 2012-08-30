package com.deveo.plugin.jenkins.notification;

import hudson.Extension;
import hudson.model.Result;
import hudson.model.TaskListener;
import hudson.model.Run;
import hudson.model.listeners.RunListener;

import java.util.logging.Logger;

@Extension
@SuppressWarnings("rawtypes")
public class JobListener extends RunListener<Run> {

	Logger logger = Logger.getLogger("com.deveo.plugin");
	
	public JobListener() {
		super(Run.class);
	}

//	@Override
//	public void onStarted(Run r, TaskListener listener) {
//		Phase.STARTED.handlePhase(r, getStatus(r), listener);
//	}
//
//	@Override
//	public void onCompleted(Run r, TaskListener listener) {
//		Phase.COMPLETED.handlePhase(r, getStatus(r), listener);
//	}
//
	@Override
	public void onFinalized(Run r) {
		logger.info(" Job finalized !");
		Phase.FINISHED.handlePhase(r, getStatus(r), TaskListener.NULL);
	}

	private String getStatus(Run r) {
		Result result = r.getResult();
		String status = null;
		if (result != null) {
			status = result.toString();
		}
		return status;
	}
}