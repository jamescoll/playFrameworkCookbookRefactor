package controllers;

import com.google.inject.Inject;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import services.JWTServiceImpl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Project name: foo_java
 *
 * Package name : controllers
 *
 * Created by: jamescoll
 *
 * Date: 05/10/2016
 *
 *
 */
public class JWTSigned extends play.mvc.Action.Simple {

    private final String AUTHORIZATION = "Authorization";
    private final String WWW_AUTHENTICATE = "WWW-Authenticate";
    private final String APP_REALM = "Protected Realm";
    private final String AUTH_HEADER_PREFIX = "Bearer";

    @Inject
    JWTServiceImpl jwtService;

    @Override
    public CompletionStage<Result> call(Http.Context context) {

        try {
            final String authHeader = context.request().getHeader(AUTHORIZATION);

            if( authHeader != null && authHeader.startsWith(AUTH_HEADER_PREFIX)) {
                if (jwtService.verify(authHeader)) {
                    return delegate.call(context);
                } else {
                    return CompletableFuture.completedFuture((Result) unauthorized());
                }
            }

        } catch (Exception e) {
            Logger.error("Error during session authentication: " + e);
        }

        context.response().setHeader(WWW_AUTHENTICATE, APP_REALM);

        return CompletableFuture.completedFuture((Result) forbidden());

    }
}
