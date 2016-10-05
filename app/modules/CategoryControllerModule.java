package modules;

import com.google.inject.AbstractModule;
import services.CategoryService;
import services.CategoryServiceImpl;

/**
 * Project name: foo_java
 *
 * Package name : modules
 *
 * Created by: jamescoll
 *
 * Date: 03/10/2016
 *
 *
 */
public class CategoryControllerModule extends AbstractModule {

    @Override
    protected void configure(){

        bind(CategoryService.class).to(CategoryServiceImpl.class).asEagerSingleton();
    }
}
