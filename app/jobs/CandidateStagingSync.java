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

            Iterator<Element> rows = doc.select("table").first().select("f").iterator();
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
        stagingCandidateRecord.personId = record[0];
        stagingCandidateRecord.source = record[1];
        stagingCandidateRecord.step = record[2];
        stagingCandidateRecord.yearsOfExperience = record[3];
        stagingCandidateRecord.taInReviewDate = record[4].replaceAll("[\\s\\u00A0]+$", " ");
        stagingCandidateRecord.reviewerName1 = record[5].replaceAll("[\\s\\u00A0]+$", " ");
        stagingCandidateRecord.recommendation1 = record[6].replaceAll("[\\s\\u00A0]+$", " ");
        stagingCandidateRecord.dateReceived1 = record[7].replaceAll("[\\s\\u00A0]+$", " ");
        stagingCandidateRecord.dateEvaluated1 = record[8].replaceAll("[\\s\\u00A0]+$", " ");
        stagingCandidateRecord.language1 = record[9].replaceAll("[\\s\\u00A0]+$", " ");
        stagingCandidateRecord.reviewerName2 = record[10].replaceAll("[\\s\\u00A0]+$", " ");
        stagingCandidateRecord.recommendation2 = record[11];
        stagingCandidateRecord.dateReceived2 = record[12].replaceAll("[\\s\\u00A0]+$", " ");
        stagingCandidateRecord.dateEvaluated2 = record[13].replaceAll("[\\s\\u00A0]+$", " ");
        stagingCandidateRecord.language2 = record[14].replaceAll("[\\s\\u00A0]+$", " ");
        stagingCandidateRecord.tags = record[15].replaceAll("[\\s\\u00A0]+$", " ");
        return stagingCandidateRecord;
    }
}
