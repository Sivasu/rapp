package models;

import ch.lambdaj.group.Group;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.group.Groups.by;
import static ch.lambdaj.group.Groups.group;

@Entity
public class Review extends Model {
    @Id
    public Long id;
    @ManyToOne
    public Reviewer reviewer;
    @OneToOne
    public Candidate candidate;
    public String technology;
    public Date startDate;
    public Date endDate;
    public boolean result;

    public Review(Reviewer reviewer, Candidate candidate, String technology, Date startDate, Date endDate, boolean result) {
        this.reviewer = reviewer;
        this.candidate = candidate;
        this.technology = technology;
        this.startDate = startDate;
        this.endDate = endDate;
        this.result = result;
    }

    public static Finder<Integer, Review> find = new Finder<Integer, Review>(Integer.class, Review.class);

    public Review() {
    }

    private static Group<Review> allReviewsByMonth(String personId) {
        return group(find.all(), by(on(Review.class).getMonth()));
    }

    private String getMonth() {
        return new SimpleDateFormat("MMMM").format(endDate);
    }
}
