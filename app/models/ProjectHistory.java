package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class ProjectHistory extends Model {
    @Id
    public Long id;
    @ManyToOne
    public Reviewer reviewer;
    public String projectName;
    public Date startDate;
    public Date endDate;
}
