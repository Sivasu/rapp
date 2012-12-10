package models;

import ch.lambdaj.group.Group;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import play.db.ebean.Model;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sumFrom;
import static ch.lambdaj.group.Groups.by;
import static ch.lambdaj.group.Groups.group;

@Entity
public class Review extends Model {
    @Id
    public Long id;
    @ManyToOne
    public Reviewer reviewer;
    @OneToOne
    @JoinColumn(name = "candidate_id", nullable = false)
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

    public static Finder<Integer, Review> finder = new Finder<Integer, Review>(Integer.class, Review.class);

    public Review() {
    }

    public static Group<Review> allReviewsByMonth() {
        return group(finder.all(), by(on(Review.class).getMonth()));
    }

    public static double averageTurnAroundDaysFor(String from, String to) {
        List<Review> list = finder.select("startDate,endDate").where().ne("startDate", "NULL")
                .ge("datediff(endDate,startDate)", 0).between("startDate", from, to).findList();
        return sumFrom(list).turnAroundDays() * 1d / list.size();
    }

    public int turnAroundDays() {
        return Days.daysBetween(new LocalDate(startDate.getTime()), new LocalDate(endDate.getTime())).getDays();
    }

    public String getMonth() {
        String month = "";
        if (startDate != null)
            month = new SimpleDateFormat("MMMM").format(startDate);
        return month;
    }

    public String getReviewer() {
        return reviewer.name;
    }

    public static Group<Review> reviewsForPeriodByReviewer(String from, String to) {
        return group(finder.select("reviewer").where().between("startDate", from, to).findList(), by(on(Review.class).getReviewer()));
    }

    public static Group<Review> reviewersForPeriodByProject(String from, String to) {
        return group(finder.select("reviewer").where().between("startDate", from, to).findList(), by(on(Review.class).getReviewerProject()));
    }

    public String getReviewerProject() {
        return reviewer.projectNameForDate(this.startDate);
    }
}
