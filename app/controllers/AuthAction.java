package controllers;

import play.Logger;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class AuthAction extends play.mvc.Action.Simple {


    public CompletionStage<Result> call(Http.Context ctx) {

        Http.Cookie authCookie = ctx.request().cookie("auth");

        if(authCookie != null) {
            Logger.info("Cookie: " + authCookie);
            return delegate.call(ctx);
        } else {
            Logger.info("Redirecting to login page");
            return CompletableFuture.completedFuture(redirect(controllers.routes.
                    Application.login()));
        }
    }
}