package com.deveo.plugin.jenkins;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.scm.SCM;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import java.io.PrintStream;

public class DeveoNotifier extends Notifier {

    private static final String SCM_GIT = "hudson.plugins.git.GitSCM";

    private static final String SCM_SUBVERSION = "hudson.scm.SubversionSCM";

    private static final String SCM_MERCURIAL = "hudson.plugins.mercurial.MercurialSCM";

    private final String accountKey;

    @DataBoundConstructor
    public DeveoNotifier(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getAccountKey() {
        return accountKey;
    }

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
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return environment;
    }

    private String getRevisionId(SCM scm, EnvVars environment) {
        String id;

        switch (scm.getType()) {
            case SCM_GIT:
                id = environment.get("GIT_COMMIT");
                break;
            case SCM_SUBVERSION:
                id = environment.get("SVN_REVISION");
                break;
            case SCM_MERCURIAL:
                id = environment.get("MERCURIAL_REVISION");
                break;
            default:
                id = "";
        }

        return id;
    }

    private String getBuildUrl(EnvVars environment) {
        return environment.get("BUILD_URL");
    }

    private String getOperation(AbstractBuild build) {
        return build.getResult() == Result.SUCCESS ? "completed" : "failed";
    }

    private String getJobName(AbstractBuild build) {
        return build.getProject().getDisplayName();
    }

    private String getRef(SCM scm, EnvVars environment) {
        return scm.getType().equals(SCM_GIT) ? environment.get("GIT_BRANCH").replace("origin/", "") : "";
    }

    private String getRepositoryURL(SCM scm, EnvVars environment) {
        String repositoryURL;

        switch (scm.getType()) {
            case SCM_GIT:
                repositoryURL = environment.get("GIT_URL");
                break;
            case SCM_SUBVERSION:
                repositoryURL = environment.get("SVN_URL");
                break;
            case SCM_MERCURIAL:
                repositoryURL = environment.get("MERCURIAL_REPOSITORY_URL");
                break;
            default:
                repositoryURL = "";
        }

        return repositoryURL;
    }

    private DeveoAPIKeys getApiKeys(DeveoBuildStepDescriptor descriptor) {
        return new DeveoAPIKeys(descriptor.getPluginKey(), descriptor.getCompanyKey(), getAccountKey());
    }

    public void notifyDeveo(AbstractBuild build, BuildListener listener) {
        EnvVars environment = getEnvironment(build, listener);
        SCM scm = build.getProject().getScm();

        String operation = getOperation(build);
        String jobName = getJobName(build);
        String ref = getRef(scm, environment);
        String revisionId = getRevisionId(scm, environment);
        String buildUrl = getBuildUrl(environment);
        String repositoryURL = getRepositoryURL(scm, environment);

        DeveoRepository repository;

        try {
            repository = new DeveoRepository(repositoryURL);
        } catch (DeveoURLException ex) {
            logError(listener, "The configured repository URL is not a Deveo URL.", ex);
            return;
        }

        DeveoAPI api = new DeveoAPI(getDescriptor().getHostname(), getApiKeys(getDescriptor()));
        DeveoEvent event = new DeveoEvent(operation, jobName, repository, ref, revisionId, buildUrl);

        try {
            api.create("events", event.toJSON());
        } catch (DeveoException ex) {
            logError(listener, "Failed to create event.", ex);
        }
    }

    private void logError(BuildListener listener, String message, Exception ex) {
        PrintStream logger = listener.getLogger();
        logger.println(String.format("Deveo: %s", message));
        logger.println(String.format("Deveo: %s", ex.getMessage()));
    }

    @Override
    public DeveoBuildStepDescriptor getDescriptor() {
        return (DeveoBuildStepDescriptor) super.getDescriptor();
    }

    @Extension
    public static final class DeveoBuildStepDescriptor extends BuildStepDescriptor<Publisher> {

        private String hostname = "https://app.deveo.com";
        private String pluginKey = "3c94d47d6257ca0d3bc54a9b6a91aa64";
        private String companyKey = "";

        public DeveoBuildStepDescriptor() {
            load();
        }

        public String getHostname() {
            return hostname;
        }

        public String getPluginKey() {
            return pluginKey;
        }

        public String getCompanyKey() {
            return companyKey;
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Deveo Notification";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
            this.hostname = json.getString("hostname");
            this.pluginKey = json.getString("pluginKey");
            this.companyKey = json.getString("companyKey");

            save();

            return super.configure(req, json);
        }

    }

}
