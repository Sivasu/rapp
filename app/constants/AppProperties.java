package constants;

import play.Play;

public class AppProperties {
    public static final String CANDIDATE_URL = Play.application().configuration().getString("candidate.url");
}