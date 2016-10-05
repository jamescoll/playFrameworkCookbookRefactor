package modules;

import com.google.inject.AbstractModule;
import services.MessageQueueService;
import services.MessageQueueServiceImpl;

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


public class MessageQueueModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(MessageQueueService.class).to(MessageQueueServiceImpl.class).asEagerSingleton();
    }
}
