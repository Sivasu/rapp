package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Candidate extends Model {
    @Id
    public Long id;
    @Constraints.Required
    public String candidateId;
    public String name;
    public String source;
    public Float experience;
    public String company;
    @OneToMany(cascade = CascadeType.ALL)
    public List<Review> reviews;

}
