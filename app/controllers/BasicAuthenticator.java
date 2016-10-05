package controllers;

import org.apache.commons.codec.binary.Base64;
import play.mvc.Result;
import play.mvc.Http;
import play.mvc.Security;

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
public class BasicAuthenticator extends Security.Authenticator {

    private final String AUTHORIZATION = "authorization";
    private final String WWW_AUTHENTICATE = "WWW-Authenticate";
    private final String REALM = "Basic realm=\"API Realm\"";

    @Override
    public String getUsername(Http.Context ctx){

        try {
            String authHeader = ctx.request().getHeader(AUTHORIZATION);

            if(authHeader != null) {
                ctx.response().setHeader(WWW_AUTHENTICATE, REALM);
                String auth = authHeader.substring(6);
                byte[] decodedAuth = Base64.decodeBase64(auth);
                String[] credentials = new String(decodedAuth, "UTF-8").split(":");

                if(credentials !=null && credentials.length == 2)
                {
                    String username = credentials[0];
                    String password = credentials[1];
                    if(isAuthenticated(username, password)) {
                        return username;
                    } else {
                        return null;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isAuthenticated(String username, String password) {
        return username !=null && username.equals("ned") && password != null && password.equals("flanders");
    }

    @Override
    public Result onUnauthorized(Http.Context context){
        return unauthorized();
    }
}
