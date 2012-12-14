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
                e.printStackTrace();
                System.out.println(stagingCandidateRecord.personId);
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
        candidate.review.technology = language(stagingCandidateRecord);
        candidate.update();
        return candidate.review;
    }

    private String language(StagingCandidateRecord stagingCandidateRecord) {
        return stagingCandidateRecord.language1 == null || "".equals(stagingCandidateRecord.language1.trim())
                ? stagingCandidateRecord.language2 : stagingCandidateRecord.language1;
    }

    private Candidate updateCandidate(StagingCandidateRecord stagingCandidateRecord) {
        Candidate candidate = Candidate.findOrCreate(stagingCandidateRecord.personId);
        candidate.experience = experience(stagingCandidateRecord);
        candidate.source = stagingCandidateRecord.source;
        candidate.update();
        return candidate;
    }

    private void updateReviewer(Reviewer reviewer, Review review) {
        reviewer.addReview(review);
        reviewer.update();
    }

    private boolean result(StagingCandidateRecord stagingCandidateRecord) {
        return stagingCandidateRecord.recommendation1 == null || "".equals(stagingCandidateRecord.recommendation1.trim())
                ? stagingCandidateRecord.recommendation2.equals("Pursue") : stagingCandidateRecord.recommendation1.equals("Pursue");
    }

    private Date startDate(StagingCandidateRecord stagingCandidateRecord) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return stagingCandidateRecord.taInReviewDate == null || "".equals(stagingCandidateRecord.taInReviewDate.trim())
                ? startDateByReviewer(stagingCandidateRecord) : simpleDateFormat.parse(stagingCandidateRecord.taInReviewDate);
    }

    private Date startDateByReviewer(StagingCandidateRecord stagingCandidateRecord) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String startDate = stagingCandidateRecord.dateReceived1 == null || "".equals(stagingCandidateRecord.dateReceived1.trim()) ?
                stagingCandidateRecord.dateReceived2 : stagingCandidateRecord.dateReceived1;
        return startDate == null || "".equals(startDate.trim()) ? null : simpleDateFormat.parse(startDate);
    }

    private Date endDate(StagingCandidateRecord stagingCandidateRecord) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String endDate = stagingCandidateRecord.dateEvaluated1 == null || "".equals(stagingCandidateRecord.dateEvaluated1.trim()) ?
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
