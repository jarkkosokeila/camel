package com.sokeila.camel.processor;

import com.sokeila.camel.configuration.Client;
import com.sokeila.camel.configuration.IntegrationConfig;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class ClientHeadersProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(ClientHeadersProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        Client client = exchange.getIn().getBody(Client.class);
        logger.info("Process header values for client {}", client.getCustomerName());

        Class<?> current = client.getClass();
        while(current.getSuperclass() != null) { // we don't want to process Object.class
            for (Field field : current.getDeclaredFields()) {
                try {
                    if (field.isAnnotationPresent(IntegrationConfig.class)) {
                        IntegrationConfig integrationConfig = field.getAnnotation(IntegrationConfig.class);
                        // changed the access to public
                        field.setAccessible(true);
                        Object value = field.get(client);

                        logger.trace("Config value key: '{}' and value '{}'", integrationConfig.key(), value);

                        exchange.getIn().setHeader(integrationConfig.key(), value);
                    }
                } catch (IllegalAccessException e) {
                    logger.error("Exception in ClientHeadersHandler. {}", e.getMessage());
                }
            }
            current = current.getSuperclass();
        }

        for (Field field : client.getClass().getDeclaredFields()) {
            try {
                if (field.isAnnotationPresent(IntegrationConfig.class)) {
                    IntegrationConfig integrationConfig = field.getAnnotation(IntegrationConfig.class);
                    // changed the access to public
                    field.setAccessible(true);
                    Object value = field.get(client);

                    logger.trace("Config value key: '{}' and value '{}'", integrationConfig.key(), value);

                    exchange.getIn().setHeader(integrationConfig.key(), value);
                }
            } catch (IllegalAccessException e) {
                logger.error("Exception in ClientHeadersHandler. {}", e.getMessage());
            }
        }
    }
}
