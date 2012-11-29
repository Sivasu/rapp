package jobs;

import models.Candidate;
import models.Review;
import models.Reviewer;
import models.StagingCandidateRecord;
import play.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CandidateStagingProcessor {
    void processStaged() {
        List<StagingCandidateRecord> stagingCandidateRecords = StagingCandidateRecord.toProcess();
        for (StagingCandidateRecord stagingCandidateRecord : stagingCandidateRecords) {
            //TODO: get the latest review alone and make update on that
            //TODO get values for technology,company
            try {
                Candidate candidate = updateCandidate(stagingCandidateRecord);
                Reviewer reviewer = Reviewer.findByName(reviewerName(stagingCandidateRecord));
                Review review = updateReview(stagingCandidateRecord, candidate, reviewer);
                if (reviewer != null) {
                    updateReviewer(reviewer, review);
                }
            } catch (ParseException e) {
                Logger.error("Error while processing record of " + stagingCandidateRecord.fullName + ", Exception " + e);
            }
            stagingCandidateRecord.processed = true;
            stagingCandidateRecord.update();
        }

        Logger.info("total records processed = " + stagingCandidateRecords.size());
    }

    private Review updateReview(StagingCandidateRecord stagingCandidateRecord, Candidate candidate, Reviewer reviewer) throws ParseException {
        candidate.review.reviewer = reviewer;
        candidate.review.startDate = startDate(stagingCandidateRecord);
        candidate.review.endDate = endDate(stagingCandidateRecord);
        candidate.review.result = result(stagingCandidateRecord);
        candidate.update();
        return candidate.review;
    }

    private Candidate updateCandidate(StagingCandidateRecord stagingCandidateRecord) {
        Candidate candidate = Candidate.findOrCreate(stagingCandidateRecord.personId);
        candidate.experience = experience(stagingCandidateRecord);
        candidate.name = stagingCandidateRecord.fullName;
        candidate.source = stagingCandidateRecord.source;
        candidate.update();
        return candidate;
    }

    private void updateReviewer(Reviewer reviewer, Review review) {
        reviewer.addReview(review);
        reviewer.update();
    }

    private boolean result(StagingCandidateRecord stagingCandidateRecord) {
        return stagingCandidateRecord.recommendation1 != null
                ? stagingCandidateRecord.recommendation1.equals("Pursue") : stagingCandidateRecord.recommendation2.equals("Pursue");
    }

    private Date startDate(StagingCandidateRecord stagingCandidateRecord) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String startDate = stagingCandidateRecord.dateReceived1 == null ?
                stagingCandidateRecord.dateReceived2 : stagingCandidateRecord.dateReceived1;

        return simpleDateFormat.parse(startDate);
    }

    private Date endDate(StagingCandidateRecord stagingCandidateRecord) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String endDate = stagingCandidateRecord.dateEvaluated1 == null ?
                stagingCandidateRecord.dateEvaluated2 : stagingCandidateRecord.dateEvaluated1;

        return simpleDateFormat.parse(endDate);
    }

    private String reviewerName(StagingCandidateRecord stagingCandidateRecord) {
        String reviewerName = stagingCandidateRecord.reviewerName1 == null || "".equals(stagingCandidateRecord.reviewerName1.trim()) ?
                stagingCandidateRecord.reviewerName2 : stagingCandidateRecord.reviewerName1;
        //TODO get the firstname and last name for reviewer as review has only lastname,firstname
        if (reviewerName.contains(",")) {
            String[] split = reviewerName.split(",");
            return split[1] + " " + split[0];
        }
        return reviewerName;
    }

    private Float experience(StagingCandidateRecord stagingCandidateRecord) {
        Float experience;
        try {
            experience = Float.valueOf(stagingCandidateRecord.yearsOfExperience);
        } catch (NumberFormatException e) {
            experience = new Float(0);
        }
        return experience;
    }
}
