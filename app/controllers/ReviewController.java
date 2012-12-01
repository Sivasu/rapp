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
import views.html.reviews_by_month;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectMax;
import static ch.lambdaj.group.Groups.by;
import static ch.lambdaj.group.Groups.group;

public class ReviewController extends Controller {
    private static String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public static Result reviewsByMonth() {
        return ok(reviews_by_month.render(""));
    }

    public static Result contributorsByMonth() {
        return ok(contributors_by_month.render(""));
    }

    @BodyParser.Of(play.mvc.BodyParser.Json.class)
    public static Result reviewsByMonthJson() {
        Group<Review> reviewGroup = Review.allReviewsByMonth();
        ObjectNode result = Json.newObject();
        for (String month : months) {
            List<Review> reviews = reviewGroup.find(month);
            result.put(month, reviews == null ? 0 : reviews.size());
        }
        return ok(result);
    }

    @BodyParser.Of(play.mvc.BodyParser.Json.class)
    public static Result contributorsByMonthJson() {
        Group<Review> reviewGroup = Review.allReviewsByMonth();
        ObjectNode result = Json.newObject();
        for (String month : months) {
            List<Review> reviews = reviewGroup.find(month);
            Group<Review> groupByReviewer = group(reviews, by(on(Review.class).getReviewer()));
            Group<Review> reviewer = selectMax(groupByReviewer.subgroups(), on(Group.class).getSize());
            if (reviewer != null && reviewer.findAll().get(0) != null) {
                Reviewer mostContributorOfTheMonth = reviewer.findAll().get(0).reviewer;
                Map<String, String> strings = new HashMap<String, String>();
                strings.put(mostContributorOfTheMonth.name, String.valueOf(reviewer.getSize()));
                result.put(month, Json.toJson(strings));
            }
        }
        return ok(result);
    }

    @BodyParser.Of(play.mvc.BodyParser.Json.class)
    public static Result contributorForPeriodJson(String from, String to) {
        Group<Review> groupByReviewer = Review.reviewsForPeriodByReviewer(from, to);
        ObjectNode result = Json.newObject();
        Group<Review> reviewer = selectMax(groupByReviewer.subgroups(), on(Group.class).getSize());
        if (reviewer != null && reviewer.findAll().get(0) != null) {
            Reviewer mostContributor = reviewer.findAll().get(0).reviewer;
            result.put(mostContributor.name, reviewer.getSize());
        }
        return ok(result);
    }

    @BodyParser.Of(play.mvc.BodyParser.Json.class)
    public static Result turnAroundDaysForPeriodJson(String from, String to) {
        double avgTurnAround = Review.averageTurnAroundDaysFor(from, to);
        ObjectNode result = Json.newObject();
        result.put("avg_turn_around", avgTurnAround);
        return ok(result);
    }


}
