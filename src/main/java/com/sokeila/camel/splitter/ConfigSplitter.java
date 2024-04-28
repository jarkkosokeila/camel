package com.sokeila.camel.splitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sokeila.camel.configuration.Client;
import com.sokeila.camel.processor.ConfigSplitProcessor;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ConfigSplitter<T extends Client> {
    private static final Logger logger = LoggerFactory.getLogger(ConfigSplitProcessor.class);
    private final Class<T[]> aClass;

    public ConfigSplitter(Class<T[]> aClass) {
        this.aClass = aClass;
    }

    public List splitConfig(Exchange exchange) {
        String configJson = exchange.getIn().getBody(String.class);

        ObjectMapper mapper = new ObjectMapper();
        logger.debug("Deserializing Config JSON to Client list:");
        T[] clients;
        try {
            clients = mapper.readValue(configJson, aClass);
        } catch (JsonProcessingException e) {
            logger.error("Exception in ConfigSplitHandler. {}", e.getMessage());
            return null;
        }

        return Arrays.asList(clients);
    }
}
