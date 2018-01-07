package com.fjmpaez.queuing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fjmpaez.queuing.pojo.Event;
import com.fjmpaez.queuing.pojo.Notification;

@Service
public class WorkingService {

    private static Logger logger = LoggerFactory.getLogger(WorkingService.class);

    private static final String ERROR = "error";
    private static final String RETRY = "retry";

    @Autowired
    private Routing routing;

    public void processNotificationFromQueue(Notification notification,
            @Header(name = AmqpHeaders.RECEIVED_ROUTING_KEY) String receivedRoutingKey,
            @Header(name = AmqpHeaders.RECEIVED_DELAY, required = false) Integer currentDelay,
            @Header(name = AmqpHeaders.CONSUMER_QUEUE) String consumerQueue) {

        logger.info("Notification received from queue. Notification: {}; Queue: {}", notification,
                consumerQueue);

        if (RETRY.equals(notification.getNotificationType())) {
            logger.info("NotificationType {}", notification.getNotificationType());
            routing.retry(notification, receivedRoutingKey, currentDelay);
        } else if (ERROR.equals(notification.getNotificationType())) {
            logger.info("NotificationType {}", notification.getNotificationType());
            throw new NullPointerException();
        } else {
        	logger.info("NotificationType {}", notification.getNotificationType());
        }
    }

    public void processEventFromQueue(Event event,
            @Header(name = AmqpHeaders.RECEIVED_ROUTING_KEY) String receivedRoutingKey,
            @Header(name = AmqpHeaders.RECEIVED_DELAY, required = false) Integer currentDelay,
            @Header(name = AmqpHeaders.CONSUMER_QUEUE) String consumerQueue) {

        logger.info("Event received from queue. Event: {}; Queue: {}", event, consumerQueue);

        if (Boolean.valueOf(event.getEventParams().get(RETRY))) {
            logger.info("EventType {}", RETRY);
            routing.retry(event, receivedRoutingKey, currentDelay);
        } else if (Boolean.valueOf(event.getEventParams().get(ERROR))) {
            logger.info("EventType {}", ERROR);
            throw new NullPointerException();
        } else {
        	logger.info("EventType {}", event.getEventType());
        }
    }

}
