package controllers;

import actors.FileReaderActor;
import actors.FileReaderProtocol;
import akka.actor.ActorRef;
import akka.actor.Props;
import models.User;
import play.cache.CacheApi;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Akka;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.With;
import services.JWTServiceImpl;
import services.ProductServiceImpl;
import views.html.form;
import views.html.index;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static akka.pattern.Patterns.ask;
import static play.libs.Json.toJson;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class Application extends Controller {

    @Inject
    CacheApi cacheApi;
    @Inject
    ProductServiceImpl productService;
    @Inject
    JWTServiceImpl jwtService;
    @Inject
    FormFactory formFactory;
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }


    public CompletionStage<Result> asyncExample() {
      ActorRef fileReaderActor = Akka.system().actorOf(Props.create(FileReaderActor.class));
      FileReaderProtocol words = new FileReaderProtocol("/usr/share/dict/words");

      return scala.compat.java8.FutureConverters.toJava(ask(fileReaderActor, words, 3000)).thenApplyAsync(
              new Function<Object, Result>() {
                  public Result apply(Object response) {
                      return ok(response.toString());
                  }
              }
      );
    }

    public Result uploadForm() {
        return ok(form.render());
    }

    public Result handleUpload() {
        MultipartFormData body = request().body().asMultipartFormData();
        FilePart profileImage = body.getFile("profile");

        if (profileImage != null) {
            try {
                String fileName = profileImage.getFilename();
                String contentType = profileImage.getContentType();
                File file = (File) profileImage.getFile();

                Path path = FileSystems.getDefault().getPath("/tmp/" + fileName);
                Files.write(path, Files.readAllBytes(file.toPath()));
                return ok("Image uploaded");
            } catch(Exception e) {
                return internalServerError(e.getMessage());
            }
        } else {
            flash("error", "Please upload a valid file");
            return redirect(routes.Application.uploadForm());
        }
    }

    @With(AuthAction.class)
    public Result dashboard() {
        return ok("User dashboard");
    }

    public Result login() {
        return ok("Please login");
    }

    public Result modifySession() {
        final String sessionVar = "user_pref";
        final String userPref = session(sessionVar);
        if (userPref == null) {
            session(sessionVar, "tw");
            return ok("Setting session var: " + sessionVar);
        } else {
            return ok("Found user_pref: " + userPref);
        }
    }



    public Result modifyCookies() {
        response().setCookie("source", "tw", (60*60));
        return ok("Cookie Modification Example");
    }

    public Result modifyHeaders() {
        response().setHeader("ETag", "foo_java");
        return ok("Header Modification Example");
    }

    public Result displayFromCache() {
        final String key = "myKey";
        String value = (String) cacheApi.get(key);

        if (value != null && value.trim().length() > 0) {
            return ok("Retrieved from Cache: " + value);
        } else {
            cacheApi.set(key, "Let's Play with Redis!");
            return ok("Setting key value in the cache");
        }
    }

    public Result bootstrapped(){
        return ok(views.html.bootstrapped.render());
    }

    public Result generateProductId() {
        return ok("Your generated product id: " + productService.generateProductId());
    }

    public Result generateUserId() {
        return ok("Your generated user id: " + User.generateUserId());
    }

    @With(JWTSigned.class)
    public Result adminOnly() {
        return ok(" ");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result auth() {
        try {

            Form<Login> form = formFactory.form(Login.class).bindFromRequest();
            Login login = form.get();

            if (login.getUsername().equals("ned") && login.getPassword().equals("flanders")) {

                final String token = jwtService.sign(login.getUsername());
                final Map<String, String> map = new HashMap<>();
                map.put("token", token);
                return ok(toJson(map));

            } else {
                return forbidden();
            }
        } catch (Exception e) {
            return internalServerError( e.getMessage());
        }
    }


}
