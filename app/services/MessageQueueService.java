package services;

import io.iron.ironmq.Message;
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

public interface MessageQueueService {

    void send(String message) throws Exception;
    Message[] retrieve() throws Exception;
    boolean enabled();

}
