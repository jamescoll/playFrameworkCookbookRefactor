package services;

import models.User;
import play.Logger;
import play.api.inject.ApplicationLifecycle;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Project name: foo_java
 *
 * Package name : services
 *
 * Created by: jamescoll
 *
 * Date: 04/10/2016
 *
 *
 */
@Singleton
public class RegistrationServiceImpl implements RegistrationService {
    private final ApplicationLifecycle applicationLifecycle;

    private final Map<String, User> registrations;

    @Inject
    public RegistrationServiceImpl(ApplicationLifecycle applicationLifecycle) {
        this.applicationLifecycle = applicationLifecycle;
        this.registrations = new LinkedHashMap<String, User>();


        applicationLifecycle.addStopHook(() -> {

            Logger.info("Clearing registrations ");
            registrations.clear();
            return CompletableFuture.completedFuture(null);
        });
    }





    @Override
    public void create(User user) {
        final String id = UUID.randomUUID().toString();
        registrations.put(id, new User(id, user.getUsername(), user.getPassword()));
    }

    @Override
    public Boolean auth(String username, String password) {
        for(Map.Entry<String, User> entry : registrations.entrySet()) {
            if(entry.getValue().getUsername().equals(username) && entry.getValue().getPassword().equals(password)){
                return true;
            }
        }

        return false;
    }
}
