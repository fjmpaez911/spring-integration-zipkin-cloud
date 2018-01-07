package com.fjmpaez.queuing;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import com.fjmpaez.queuing.pojo.Notification;

public interface RetryGatewayNotifications {

    void send(@Payload Notification request, @Header("routingKey") String routingKey,
            @Header(MessageProperties.X_DELAY) Integer delay);

}
