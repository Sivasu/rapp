package job;


import akka.util.Duration;
import constants.AppProperties;
import models.StagingCandidateRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import play.libs.Akka;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class CandidateDataSync implements Runnable {

    private static Runnable candidateDataSync;

    public static void syncCandidateData() {

        System.out.println("syncCandidateData called");
        Akka.system().scheduler().schedule(
                Duration.create(0, TimeUnit.MILLISECONDS),
                Duration.create(1, TimeUnit.MINUTES),
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
        List<StagingCandidateRecord> stagingRecords = getStagingRecords();
        System.out.println("total records = " + stagingRecords.size());
        HashSet<String> uniqueIds = new HashSet<String>();
        for (StagingCandidateRecord stagingRecord : stagingRecords) {
            uniqueIds.add(stagingRecord.personId);
        }
        System.out.println("Unique ids " + uniqueIds.size());
        persistStagingRecords(stagingRecords);

    }

    private void persistStagingRecords(List<StagingCandidateRecord> stagingRecords) {

    }

    private List<StagingCandidateRecord> getStagingRecords() {

        List<StagingCandidateRecord> stagingCandidateRecords = new ArrayList<StagingCandidateRecord>();
        try {

            Document doc = Jsoup.connect(AppProperties.CANDIDATE_URL).timeout(0).get();

            Iterator<Element> rows = doc.select("table").first().select("tr").iterator();
            String[] record = new String[17];
            while (rows.hasNext()) {
                Element row = rows.next();
                Iterator<Element> data = row.select("td").iterator();
                int i = 0;
                while (data.hasNext()) {
                    record[i] = data.next().text();
                    i++;
                }

                stagingCandidateRecords.add(transformRecord(record));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stagingCandidateRecords;
    }

    private StagingCandidateRecord transformRecord(String[] record) {
        StagingCandidateRecord stagingCandidateRecord = new StagingCandidateRecord();
        stagingCandidateRecord.fullName = record[0];
        stagingCandidateRecord.personId = record[0];
        stagingCandidateRecord.source = record[0];
        stagingCandidateRecord.step = record[0];
        stagingCandidateRecord.yearsOfExperience = record[0];
        stagingCandidateRecord.taInReviewDate = record[0];
        stagingCandidateRecord.reviewerName1 = record[0];
        stagingCandidateRecord.recommendation1 = record[0];
        stagingCandidateRecord.dateReceived1 = record[0];
        stagingCandidateRecord.dateEvaluated1 = record[0];
        stagingCandidateRecord.language1 = record[0];
        stagingCandidateRecord.reviewerName2 = record[0];
        stagingCandidateRecord.recommendation2 = record[0];
        stagingCandidateRecord.dateReceived2 = record[0];
        stagingCandidateRecord.dateEvaluated2 = record[0];
        stagingCandidateRecord.language2 = record[0];
        stagingCandidateRecord.tags = record[0];
        return stagingCandidateRecord;
    }
}
