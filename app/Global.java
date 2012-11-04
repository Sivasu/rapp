import job.CandidateDataSync;
import play.*;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {
        Logger.info("Application has started");
        System.out.println("Application has started");
        CandidateDataSync.syncCandidateData();
    }

    @Override
    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }

}