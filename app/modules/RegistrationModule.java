package modules;

import com.google.inject.AbstractModule;
import services.RegistrationService;
import services.RegistrationServiceImpl;

/**
 * Project name: foo_java
 *
 * Package name : modules
 *
 * Created by: jamescoll
 *
 * Date: 04/10/2016
 *
 *
 */
public class RegistrationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RegistrationService.class).to(RegistrationServiceImpl.class).asEagerSingleton();
    }
}
