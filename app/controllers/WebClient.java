package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import play.libs.oauth.OAuth;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * Project name: foo_java
 *
 * Package name : controllers
 *
 * Created by: jamescoll
 *
 * Date: 04/10/2016
 *
 *
 */
public class WebClient extends Controller {

    @Inject
    WSClient ws;


    public CompletionStage<Result> getTodos() {
        WSRequest request = ws.url("http://jsonplaceholder.typicode.com/todos");

        CompletionStage<WSResponse> todos = request.get();

        return todos.thenApplyAsync(
                new Function<WSResponse, Result>() {
                    @Override
                    public Result apply(WSResponse wsResponse) {
                        JsonNode json = wsResponse.asJson();
                        return ok("Todo Title: " + json.findValuesAsText("title"));
                    }
                }
        );
    }

    public CompletionStage<Result> getTweets(String hashtag) {
        final String url = "https://api.twitter.com/1.1/search/tweets.json?q=%40" + hashtag;

        final OAuth.ConsumerKey consumerInfo = new OAuth.ConsumerKey(

                "V6cjRNneHyVDKEAm5KIXBNert",
                "v0t7IEzdR2KTN9jlxxibvbPbPuDfU7AzVygeFudkYM3XKILvmC"
        );

        final OAuth.RequestToken tokens = new OAuth.RequestToken(
                "1306962458-P1oc56dihJENRxJ00Icc8QaS6G9QPRkY9YPKWqK",
                "FeYQmYgPSSjZ44dEnaagO8QfGb2keX7eK0YYKAbc8q3US"
        );

        CompletionStage<WSResponse> twRequest = ws.url(url).sign(new OAuth.OAuthCalculator(consumerInfo, tokens)).get();

        return twRequest.thenApplyAsync(
                new Function<WSResponse, Result>() {
                    @Override
                    public Result apply(WSResponse wsResponse) {
                        Map<String, String> map = new HashMap<String, String>();
                        JsonNode root = wsResponse.asJson();

                        for(JsonNode json : root.get("statuses")){
                            map.put(json.findValue("user").findValue("screen_name").asText(),
                                    json.findValue("text").asText()
                            );
                        }

                        return ok(views.html.tweets.render(map));
                    }
                }
        );

    }
}
