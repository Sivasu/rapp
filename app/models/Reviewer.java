package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Reviewer extends Model {
    @Id
    public Long id;
    public String name;
    @Constraints.Required
    public String consultantId;
    @OneToMany(cascade = CascadeType.ALL)
    public List<Review> reviews;
    @OneToMany(cascade = CascadeType.ALL)
    public List<ProjectHistory> projectHistories;
}
