package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class StagingCandidateRecord extends Model {
    @Id
    public Long id;
    public String fullName;
    public String personId;
    public String source;
    public String step;
    public String yearsOfExperience;
    public String taInReviewDate;
    public String reviewerName1;
    public String recommendation1;
    public String dateReceived1;
    public String dateEvaluated1;
    public String language1;
    public String reviewerName2;
    public String recommendation2;
    public String dateReceived2;
    public String dateEvaluated2;
    public String language2;
    public String tags;
    public boolean processed;

    public static Finder<Integer, StagingCandidateRecord> find = new Finder<Integer, StagingCandidateRecord>(Integer.class, StagingCandidateRecord.class);


    public static List<StagingCandidateRecord> toProcess() {
        return find.where().eq("processed", false).findList();
    }

    private static StagingCandidateRecord findByPersonId(String personId) {
        return find.where().eq("personId", personId).findUnique();
    }

}
