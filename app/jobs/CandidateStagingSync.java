package jobs;

import constants.AppProperties;
import models.StagingCandidateRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import play.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CandidateStagingSync {
    public void stageRecords() {
        List<StagingCandidateRecord> stagingRecords = getStagingRecords();
        Logger.info("total records synced = " + stagingRecords.size());
        persistStagingRecords(stagingRecords);
    }

    private void persistStagingRecords(List<StagingCandidateRecord> stagingRecords) {
        for (StagingCandidateRecord stagingRecord : stagingRecords) {
            stagingRecord.save();
        }
    }

    private List<StagingCandidateRecord> getStagingRecords() {

        List<StagingCandidateRecord> stagingCandidateRecords = new ArrayList<StagingCandidateRecord>();
        try {

            Document doc = Jsoup.connect(AppProperties.CANDIDATE_URL).timeout(0).get();

            Iterator<Element> rows = doc.select("table").first().select("tr").iterator();
            String[] record = new String[17];
            rows.next();
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
        stagingCandidateRecord.personId = record[1];
        stagingCandidateRecord.source = record[2];
        stagingCandidateRecord.step = record[3];
        stagingCandidateRecord.yearsOfExperience = record[4];
        stagingCandidateRecord.taInReviewDate = record[5];
        stagingCandidateRecord.reviewerName1 = record[6];
        stagingCandidateRecord.recommendation1 = record[7];
        stagingCandidateRecord.dateReceived1 = record[8];
        stagingCandidateRecord.dateEvaluated1 = record[9];
        stagingCandidateRecord.language1 = record[10];
        stagingCandidateRecord.reviewerName2 = record[11];
        stagingCandidateRecord.recommendation2 = record[12];
        stagingCandidateRecord.dateReceived2 = record[13];
        stagingCandidateRecord.dateEvaluated2 = record[14];
        stagingCandidateRecord.language2 = record[15];
        stagingCandidateRecord.tags = record[16];
        return stagingCandidateRecord;
    }
}
