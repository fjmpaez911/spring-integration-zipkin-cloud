package com.fjmpaez.queuing;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import com.fjmpaez.queuing.pojo.Event;

public interface RetryGatewayEvents {

    void send(@Payload Event event, @Header("routingKey") String routingKey,
            @Header(MessageProperties.X_DELAY) Integer delay);

}
