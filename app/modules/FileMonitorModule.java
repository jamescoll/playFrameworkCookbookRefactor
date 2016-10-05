package modules;

import com.google.inject.AbstractModule;
import services.FileMonitorService;
import services.FileMonitorServiceImpl;

import java.time.Clock;


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

//TODO this is not yet working - the methods in FileMonitorService and FMS..Impl are not being called...review this
public class FileMonitorModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(Clock.class).toInstance(Clock.systemDefaultZone());
        bind(FileMonitorService.class).to(FileMonitorServiceImpl.class).asEagerSingleton();
    }






}
