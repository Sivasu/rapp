package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static void addNew(Reviewer reviewer, String projectName, String startDate, String endDate) throws ParseException {
        ProjectHistory projectHistory = new ProjectHistory();
        projectHistory.reviewer = reviewer;
        projectHistory.projectName = projectName;
        projectHistory.startDate = new SimpleDateFormat("dd-MMM-yy").parse(startDate);
        projectHistory.endDate = new SimpleDateFormat("dd-MMM-yy").parse(endDate);
        projectHistory.save();
        reviewer.update();
    }

}
