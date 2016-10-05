package services;

import io.iron.ironmq.*;
import play.api.inject.ApplicationLifecycle;
import java.util.concurrent.CompletableFuture;

import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;


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
public class MessageQueueServiceImpl implements MessageQueueService {

    final private Integer messageSize = 10;


    private Client client;
    private Queue queue;
    private final ApplicationLifecycle applicationLifecycle;
    private Cloud cloud;

    @Inject
    public MessageQueueServiceImpl(ApplicationLifecycle applicationLifecycle) {

        Logger.info("Starting message queue");

        this.applicationLifecycle = applicationLifecycle;
        cloud = new Cloud("https", "mq-aws-eu-west-1-1.iron.io", 443);
        client = new Client("57f3c131ce60dc0007dd361f", "J2kjFMAzXzrpOK4OIEdZ", cloud);
        queue = client.queue("TestPullQueue");




        applicationLifecycle.addStopHook(() -> {
            try {
                Logger.info("Shutting down message queue");
                    queue.clear();
                    queue.destroy();
                    client = null;
            } catch (Exception e) {
                Logger.error(e.toString());
            }
            return CompletableFuture.completedFuture(null);

        });
    }

    @Override
    public void send(String msg) throws Exception {
        try {
            queue.push(msg);
        } catch (Exception e) {
            //Logger.info(e.getMessage());
        }
    }

    public Message[] retrieve() throws Exception {
        Messages messages = queue.get(messageSize);
        if (messages.getSize() > 0) {
            Message[] msgArray = messages.getMessages();

            for (Message m : msgArray) {
                queue.deleteMessage(m);
            }

            return msgArray;
        }

        return new Message[] {};
    }

    @Override
    public boolean enabled() {
        return true;
    }


}
