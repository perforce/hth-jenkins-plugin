package deveo.jenkins.plugin;
import hudson.Launcher;
import hudson.Extension;
import hudson.util.FormValidation;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.tasks.Builder;
import hudson.tasks.Recorder;
import hudson.tasks.BuildStepDescriptor;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.IOException;

public class DeveoPlugin extends Builder {

    private final String apiurl;
    private final String bot;
    private final String access;

    private boolean continuous_reports;
    private final String script = null;

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public DeveoPlugin(String access, String apiurl, String bot) {
        this.access = access;
        this.apiurl = apiurl;
        this.bot = bot;
    }

    //We'll use this from the <tt>config.jelly</tt>.
    public String getAccess() {
        return this.access;
    }
    public String getApiUrl() {
        return this.apiurl;
    }
    public String getBot() {
        return this.bot;
    }
    public boolean getContinuous(){
        return this.continuous_reports;
    }
    
    
    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
            
        return true;
    }
    

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }
    


    @Extension 
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        private boolean useFrench;
        private boolean continuousReport;
        
        public FormValidation doCheckAccess(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set a access");
            return FormValidation.ok();
        }
        
        public FormValidation doCheckBot(@QueryParameter String value)
        throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set a bot");
            return FormValidation.ok();
        }
        
        public FormValidation doCheckApiurl(@QueryParameter String value)
        throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set a apiurl");
//            if (value.length() < 4)
//                return FormValidation.warning("Isn't the name too short?");
            return FormValidation.ok();
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            continuousReport = formData.getBoolean("continuousReport");
            save();
            return super.configure(req,formData);
        }

        public boolean getContinuous() {
            return continuousReport;
        }
        
        
        
        
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }
        
        public String getDisplayName() {
            return "Report to Deveo";
        }
    }
}

