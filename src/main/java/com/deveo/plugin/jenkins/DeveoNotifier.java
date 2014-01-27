package com.deveo.plugin.jenkins;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;

public class DeveoNotifier extends Notifier {

    private final DeveoAuthorizationKeys deveoAuthorizationKeys;
    private final DeveoRepository deveoRepository;

    @DataBoundConstructor
    public DeveoNotifier(String pluginKey, String companyKey, String accountKey, String projectId, String repositoryId) {
        this.deveoAuthorizationKeys = new DeveoAuthorizationKeys(pluginKey, companyKey, accountKey);
        this.deveoRepository = new DeveoRepository(projectId, repositoryId);
    }

    public String getPluginKey() {
        return getDeveoAuthorizationKeys().getPluginKey();
    }

    public String getCompanyKey() {
        return getDeveoAuthorizationKeys().getCompanyKey();
    }

    public String getAccountKey() {
        return getDeveoAuthorizationKeys().getAccountKey();
    }

    public String getProjectId() {
        return getDeveoRepository().getProjectId();
    }

    public String getRepositoryId() {
        return getDeveoRepository().getId();
    }

    public DeveoAuthorizationKeys getDeveoAuthorizationKeys() {
        return deveoAuthorizationKeys;
    }

    public DeveoRepository getDeveoRepository() {
        return deveoRepository;
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public boolean needsToRunAfterFinalized() {
        return true;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        notifyDeveo(build, listener);
        return true;
    }

    private EnvVars getEnvironment(AbstractBuild build, BuildListener listener) {
        EnvVars environment = null;

        try {
            environment = build.getEnvironment(listener);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return environment;
    }

    private String getRevisionId(EnvVars environment) {
        String revisionId = "";

        revisionId = environment.get("GIT_COMMIT");
        if (StringUtils.isBlank(revisionId)) {
            revisionId = environment.get("SVN_REVISION");
        }

        return revisionId;
    }

    private String getBuildUrl(EnvVars environment) {
        return environment.get("BUILD_URL");
    }

    public void notifyDeveo(AbstractBuild build, BuildListener listener) {
        EnvVars environment = getEnvironment(build, listener);
        String operation = build.getResult() == Result.SUCCESS ? "completed" : "failed";

        DeveoEvent deveoEvent = new DeveoEvent(operation, getDeveoRepository(), getRevisionId(environment), getBuildUrl(environment));

        DeveoAPI api = new DeveoAPI(getDescriptor().getApiUrl(), getDeveoAuthorizationKeys());
        api.create("events", deveoEvent.toJSON());
    }

    @Override
    public DeveoBuildStepDescriptor getDescriptor() {
        return (DeveoBuildStepDescriptor) super.getDescriptor();
    }

    @Extension
    public static final class DeveoBuildStepDescriptor extends BuildStepDescriptor<Publisher> {

        private String apiUrl = "https://deveo.com/api/v0";

        public String getApiUrl() {
            return apiUrl;
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Deveo notification";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
            this.apiUrl = json.getString("apiUrl");
            save();
            return super.configure(req, json);
        }

    }

}
