package com.sokeila.camel.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sokeila.camel.configuration.Client;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ConfigSplitProcessor<T extends Client> implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(ConfigSplitProcessor.class);
    private final Class<T[]> aClass;

    public ConfigSplitProcessor(Class<T[]> aClass) {
        this.aClass = aClass;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String configJson = exchange.getIn().getBody(String.class);

        ObjectMapper mapper = new ObjectMapper();
        logger.debug("Deserializing Config JSON to Client list:");
        try {
            T[] clients = mapper.readValue(configJson, aClass);
            exchange.getIn().setBody(Arrays.asList(clients));
        } catch (JsonProcessingException e) {
            logger.error("Exception in ConfigSplitHandler. {}", e.getMessage());
        }
    }
}
