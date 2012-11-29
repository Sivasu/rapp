package controllers;

import ch.lambdaj.group.Group;
import models.Review;
import org.codehaus.jackson.node.ObjectNode;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.reviews_in_months;

import java.util.List;

public class ReviewController extends Controller {
    private static String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public static Result reviewsByMonth() {
        return ok(reviews_in_months.render(""));
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
}
