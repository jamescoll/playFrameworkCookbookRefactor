package controllers;

import com.google.inject.Inject;
import io.iron.ironmq.Message;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.MessageQueueServiceImpl;

import java.util.HashMap;
import java.util.Map;

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
public class Messenger extends Controller {

    @Inject
    MessageQueueServiceImpl messageQueueService;
    @Inject
    FormFactory formFactory;

    @BodyParser.Of(BodyParser.Json.class)
    public Result sendMessage() {

        try {
            Form<Message> form = formFactory.form(Message.class).bindFromRequest();
            Message m = form.get();
            Logger.info("Attempting to send " + m.getBody());
            messageQueueService.send(m.getBody());
            Map<String, String> map = new HashMap<>();
            map.put("status", "Message sent.");
            return created(toJson(map));
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    public Result getMessages() {
        try {
            return ok(toJson(messageQueueService.retrieve()));
        } catch (Exception e) {
            Logger.error(e.toString());
            return internalServerError();
        }
    }
}
