package com.fjmpaez.queuing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fjmpaez.queuing.pojo.Event;
import com.fjmpaez.queuing.pojo.Notification;

@Component
public class Routing {

    private static Logger LOG = LoggerFactory.getLogger(Routing.class);

    private static final String RETRY_ROUTING_KEY_PREFIX = "retry.";

    @Value("${queuing.retry.delay.minutes}")
    private Integer defaultDelay;

    @Value("${queuing.retry.delay.max-retries}")
    private Integer maxRetries;

    @Value("${queuing.notifications.routing-key.activation}")
    private String routingKeyActivation;
    
    @Value("${queuing.notifications.routing-key.disconnection}")
    private String routingKeyDisconnection;
    
    @Value("${queuing.notifications.routing-key.unknown}")
    private String routingKeyUnknown;

    @Autowired
    private Gateway gateway;

    @Autowired
    private RetryGatewayNotifications retryGatewayNotifications;

    @Autowired
    private RetryGatewayEvents retryGatewayEvents;

    public void send(Notification notification) {

        String routingKey = routingKeyUnknown;
        
        if ("Activation".equals(notification.getNotificationType())) {
        	routingKey = routingKeyActivation;
        } else if ("Disconnection".equals(notification.getNotificationType())) {
        	routingKey = routingKeyDisconnection;
        }
        
		gateway.send(notification, routingKey);
        LOG.info("Publishing to RabbitMQ. RoutingKey: {}; Notification: {}", routingKey,
                notification);
    }

    public void retry(Notification notification, String receivedRoutingKey, Integer currentDelay) {

        Integer retryCount = getRetryCount(currentDelay);

        if (retryCount < maxRetries) {
            Integer delay = getDelay(currentDelay);
            String routingKey = getRetryRoutingKey(receivedRoutingKey);

            retryGatewayNotifications.send(notification, routingKey, delay);
            LOG.info("Publishing to RabbitMQ - Retry. Delay: {}; RoutingKey: {}; Notification: {}",
                    delay, routingKey, notification);
        } else {
            LOG.warn("Max retries exceded. The notification will be discard. Notification: {}",
                    notification);
        }
    }

    public void retry(Event event, String receivedRoutingKey, Integer currentDelay) {

        Integer retryCount = getRetryCount(currentDelay);

        if (retryCount < maxRetries) {
            Integer delay = getDelay(currentDelay);
            String routingKey = getRetryRoutingKey(receivedRoutingKey);

            retryGatewayEvents.send(event, routingKey, delay);
            LOG.info("Publishing to RabbitMQ - Retry. Delay: {}; RoutingKey: {}; Event: {}", delay,
                    routingKey, event);
        } else {
            LOG.warn("Max retries exceded. The event will be discard. Event: {}", event);
        }
    }

    private Integer getDelay(Integer currentDelay) {
        Integer delay = defaultDelay;

        if (currentDelay != null) {
            delay *= getDelayInMinutes(currentDelay);
        }
        return delay * 60 * 1000;
    }

    private Integer getRetryCount(Integer currentDelay) {
        Integer retryCount = 0;

        if (currentDelay != null) {
            Integer currentDelayInMinutes = getDelayInMinutes(currentDelay);

            Integer delayMultiplier = currentDelayInMinutes;

            while (delayMultiplier >= defaultDelay) {
                delayMultiplier /= defaultDelay;
                retryCount++;
            }
        }
        return retryCount;
    }

    private Integer getDelayInMinutes(Integer currentDelay) {
        Integer delay = 0;

        if (currentDelay != null) {
            delay = currentDelay / 1000 / 60;
        }
        return delay;
    }

    private String getRetryRoutingKey(String receivedRoutingKey) {
        String retryRoutingKey = receivedRoutingKey;

        if (receivedRoutingKey != null && !receivedRoutingKey.contains(RETRY_ROUTING_KEY_PREFIX)) {
            retryRoutingKey = RETRY_ROUTING_KEY_PREFIX + receivedRoutingKey;
        }
        return retryRoutingKey;
    }

}
