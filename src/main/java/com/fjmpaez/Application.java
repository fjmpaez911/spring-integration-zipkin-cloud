package com.fjmpaez;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({ "classpath:queuing/integration-configuration-notifications.xml",
        "classpath:queuing/integration-configuration-events.xml",
        "classpath:queuing/integration-routing.xml",
        "classpath:queuing/integration-routing-notifications.xml",
        "classpath:queuing/integration-routing-events.xml" })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
