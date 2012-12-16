package jobs;


import akka.util.Duration;
import play.Logger;
import play.libs.Akka;

import java.util.concurrent.TimeUnit;


public class CandidateDataSync implements Runnable {

    private static Runnable candidateDataSync;
    private CandidateStagingSync candidateStagingSync = new CandidateStagingSync();
    private CandidateStagingProcessor candidateStagingProcessor = new CandidateStagingProcessor();

    public static void syncCandidateData() {

        Logger.info("syncCandidateData called");
        Akka.system().scheduler().schedule(
                Duration.create(0, TimeUnit.MILLISECONDS),
                Duration.create(60, TimeUnit.MINUTES),
                getInstance()
        );
    }

    private static Runnable getInstance() {
        if (candidateDataSync == null) {
            candidateDataSync = new CandidateDataSync();
        }
        return candidateDataSync;
    }

    @Override
    public void run() {
        candidateStagingSync.stageRecords();
        candidateStagingProcessor.processStaged();
    }

}
