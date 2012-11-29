package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Reviewer extends Model {
    @Id
    public Long id;
    @Constraints.Required
    public String consultantId;
    public String name;
    @OneToMany(cascade = CascadeType.ALL)
    public List<Review> reviews;
    @OneToMany(cascade = CascadeType.ALL)
    public List<ProjectHistory> projectHistories;
    public static Finder<Integer, Reviewer> find = new Finder<Integer, Reviewer>(Integer.class, Reviewer.class);

    public static Reviewer findByName(String name) {
        Reviewer reviewer = find.where().like("name", name.trim()).findUnique();
        return reviewer;
    }

    public void addReview(Review review) {
        this.reviews = this.reviews == null ? new ArrayList<Review>() : reviews;
        if (!this.reviews.contains(review)) {
            this.reviews.add(review);
        }
    }
}
