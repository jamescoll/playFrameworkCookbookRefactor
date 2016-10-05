package services;

import akka.actor.ActorSystem;
import com.google.common.io.Files;
import play.Logger;
import play.inject.ApplicationLifecycle;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
public class FileMonitorServiceImpl implements FileMonitorService {

    private final Clock clock;
    private final ApplicationLifecycle applicationLifecycle;
    private final ActorSystem actorSystem;
    private final Instant start;


    @Inject
    public FileMonitorServiceImpl(ApplicationLifecycle applicationLifecycle,  Clock clock) {

        this.applicationLifecycle = applicationLifecycle;
        this.actorSystem = ActorSystem.create("filemon");
        this.clock = clock;

        File file =  new File("/var/tmp/foo");

        byte[] emptyArray = "".getBytes();
        try {
            Files.write(emptyArray, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        start = clock.instant();


        Logger.info("Starting file monitor service at " + start);

        actorSystem.scheduler().schedule(
                Duration.create(0, TimeUnit.SECONDS),
                Duration.create(1, TimeUnit.SECONDS),

                () -> {
                    if (file.exists()) {
                        System.out.println(file.toString() + " exists..");
                    } else {
                        System.out.println(file.toString() + " does not exist..");
                    }
                },
                actorSystem.dispatcher()

        );




        applicationLifecycle.addStopHook(() -> {
            Instant stop = clock.instant();
            Logger.info("Stopping file monitor service at " + stop);
            actorSystem.shutdown();
            return CompletableFuture.completedFuture(null);
        });


    }



}


