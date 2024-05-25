package com.example.integration.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * This processor set all customer configuration values into header map
 */
public class CustomerHeadersProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(CustomerHeadersProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, String> config = exchange.getIn().getBody(Map.class);

        for(String key : config.keySet()) {
            logger.trace("Config value key: '{}' and value '{}'", key, config.get(key));
            exchange.setProperty(key, config.get(key));
        }
    }
}
