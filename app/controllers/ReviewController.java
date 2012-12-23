package controllers;

import ch.lambdaj.group.Group;
import models.Review;
import models.Reviewer;
import org.codehaus.jackson.node.ObjectNode;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.contributors_by_month;
import views.html.project_contributions;
import views.html.reviews_by_month;
import views.html.turn_around_time;

import java.text.SimpleDateFormat;
import java.util.*;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class ReviewController extends Controller {
    private static String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public static Result reviewsByMonth() {
        return ok(reviews_by_month.render(""));
    }

    public static Result contributorsOfLastMonth() {
        return ok(contributors_by_month.render(""));
    }

    public static Result projectContributions() {
        return ok(project_contributions.render(""));
    }

    public static Result turnAround() {
        return ok(turn_around_time.render());
    }

    @BodyParser.Of(play.mvc.BodyParser.Json.class)
    public static Result reviewsByMonthJson() {
        List<List> reviewsByMonth = new ArrayList<>();
        reviewsByMonth.add(Arrays.asList("x", "Reviews", "Conversion Ratio", "Turn Around Days"));
        Group<Review> reviewGroup = Review.allReviewsByMonth();
        Map<String, Double> turnAroundDaysByMonth = Review.averageTurnAroundDaysByMonth();
        for (String month : months) {
            List<Review> reviews = reviewGroup.find(month);
            int pursued = filter(having(on(Review.class).result), reviews).size();
            int reviewCount = reviews == null ? 0 : reviews.size();
            reviewsByMonth.add(Arrays.asList(month, reviewCount
                    , Math.round((pursued / (double) reviewCount) * 100), turnAroundDaysByMonth.get(month)));
        }
        return ok(Json.toJson(reviewsByMonth));
    }

    @BodyParser.Of(play.mvc.BodyParser.Json.class)
    public static Result contributorsOfLastMonthJson() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String from = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        calendar.add(Calendar.MONTH, 1);
        String to = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        return contributorForPeriodJson(from, to, 1);
    }

    @BodyParser.Of(play.mvc.BodyParser.Json.class)
    public static Result contributorForPeriodJson(String from, String to, int minInterviews) {
        Group<Review> groupByReviewer = Review.reviewsForPeriodByReviewer(from, to);
        List<Map<String, String>> contributors = new ArrayList<>();
        List<Group<Review>> reviewer = filter(having(on(Group.class).getSize(), greaterThanOrEqualTo(minInterviews)), groupByReviewer.subgroups());
        for (Group<Review> reviewGroup : reviewer) {
            if (reviewGroup.findAll().get(0) != null) {
                Reviewer contributor = reviewGroup.findAll().get(0).reviewer;
                Map<String, String> contributorMap = new HashMap<>();
                contributorMap.put("text", contributor.name);
                contributorMap.put("weight", String.valueOf(Math.random() % 5));
                contributors.add(contributorMap);
            }
        }

        return ok(Json.toJson(contributors));
    }

    @BodyParser.Of(play.mvc.BodyParser.Json.class)
    public static Result turnAroundDaysForPeriodJson(String from, String to) {
        double avgTurnAround = Review.averageTurnAroundDaysFor(from, to);
        ObjectNode result = Json.newObject();
        result.put("avg_turn_around", avgTurnAround);
        return ok(result);
    }

    @BodyParser.Of(play.mvc.BodyParser.Json.class)
    public static Result turnAroundDaysByMonthJson() {
        Map<String, Double> trunAroundDaysByMonth = Review.averageTurnAroundDaysByMonth();
        return ok(Json.toJson(trunAroundDaysByMonth));
    }

    @BodyParser.Of(play.mvc.BodyParser.Json.class)
    public static Result projectContributionJson(String from, String to) {
        List<List> projectContributions = new ArrayList<>();
        projectContributions.add(Arrays.asList("Projects", "Reviews"));
        Group<Review> reviewGroup = Review.reviewersForPeriodByProject(from, to);
        for (String groupKey : reviewGroup.keySet()) {
            List<Review> reviews = reviewGroup.find(groupKey);
            groupKey = groupKey.replace("\"", "");
            projectContributions.add(Arrays.asList(groupKey, reviews.size()));
        }
        return ok(Json.toJson(projectContributions));
    }


}
