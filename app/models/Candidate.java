package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Candidate extends Model {
    @Id
    public Long id;
    @Constraints.Required
    public String personId;
    public String source;
    public Float experience;
    public String company;
    @OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL)
    public Review review;

    public static Finder<Integer, Candidate> find = new Finder<Integer, Candidate>(Integer.class, Candidate.class);

    public Candidate(String personId) {
        this.personId = personId;
    }

    public static Candidate findOrCreate(String personId) {
        Candidate candidate = find.where().eq("personId", personId).findUnique();
        return candidate == null ? newCandidate(personId) : candidate;
    }

    private static Candidate newCandidate(String personId) {
        Candidate candidate = new Candidate(personId);
        Review review = new Review();
        review.save();
        candidate.review = review;
        candidate.save();
        review.candidate = candidate;
        review.update();
        return candidate;
    }
}
