package com.fjmpaez.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fjmpaez.queuing.Routing;
import com.fjmpaez.queuing.pojo.Notification;

@RestController
@RequestMapping(path = "/v1.0")
public class NotificationRestController {

    private static Logger logger = LoggerFactory.getLogger(NotificationRestController.class);

    @Autowired
    private Routing routing;

    @RequestMapping(path = "/notifications", method = RequestMethod.GET)
    public String notifications(@RequestParam String msisdn, @RequestParam String action) {

        Notification notification = new Notification();
        notification.setMsisdn(msisdn);
        notification.setNotificationType(action);

        logger.info("Notification received: {}", notification);

        routing.send(notification);

        return "ok";
    }

}
