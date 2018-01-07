package com.fjmpaez.queuing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
public class MessageErrorHandler implements ErrorHandler {

    private static Logger logger = LoggerFactory.getLogger(MessageErrorHandler.class);

    @Override
    public void handleError(Throwable t) {
        logger.warn("Unprocessable Message from queue");
        throw new AmqpRejectAndDontRequeueException(t);
    }

}
