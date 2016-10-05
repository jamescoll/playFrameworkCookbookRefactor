package controllers;

import com.google.inject.Inject;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.RegistrationServiceImpl;
import static play.libs.Json.toJson;

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
public class Registrations extends Controller {

    @Inject
    RegistrationServiceImpl registrationService;
    @Inject
    FormFactory formFactory;


    @BodyParser.Of(BodyParser.Json.class)
    public Result register() {
        try {
            Form<User> form = formFactory.form(User.class).bindFromRequest();
            User user = form.get();
            registrationService.create(user);
            return created(toJson(user));
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    public Result login() {
        try {
            Form<User> form = formFactory.form(User.class).bindFromRequest();
            User user = form.get();
            if (registrationService.auth(user.getUsername(), user.getPassword())) {
                return ok();
            } else {
                return forbidden();
            }
            //registrationService.create(user);
           // return created(toJson(user));
        } catch (Exception e) {
           return internalServerError(e.getMessage());
        }
    }

}
