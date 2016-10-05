package modules;

import com.google.inject.AbstractModule;
import services.JWTService;
import services.JWTServiceImpl;

/**
 * Project name: foo_java
 *
 * Package name : modules
 *
 * Created by: jamescoll
 *
 * Date: 05/10/2016
 *
 *
 */
public class JWTModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(JWTService.class).to(JWTServiceImpl.class).asEagerSingleton();
    }
}
