package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Review extends Model {
    @Id
    public Long id;
    @ManyToOne
    public Reviewer reviewer;
    @ManyToOne
    public Candidate candidate;
    public String technology;
    public Date startDate;
    public Date endDate;
    public boolean result;
}
