package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class StagingCandidateRecord extends Model {
    @Id
    public Long id;
    @Constraints.Required
    public String fullName;
    @Constraints.Required
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
}
