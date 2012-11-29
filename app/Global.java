import play.Application;
import play.GlobalSettings;
import play.Logger;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {
        Logger.info("Application has started");
        //CandidateDataSync.syncCandidateData();
        //ConsultantDataSync.syncConsultantData();
    }

    @Override
    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }

}