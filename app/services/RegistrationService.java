package services;

import models.User;

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
public interface RegistrationService {



    void create(User user);
    Boolean auth(String username, String password);
}
