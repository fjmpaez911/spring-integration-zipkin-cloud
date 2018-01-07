package com.fjmpaez.queuing;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import com.fjmpaez.queuing.pojo.Notification;

public interface Gateway {

    void send(@Payload Notification request, @Header("routingKey") String routingKey);

}
