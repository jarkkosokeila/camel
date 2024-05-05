package com.example.integration.splitter;

import com.example.integration.configuration.CustomerConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ConfigurationSplitter<T extends CustomerConfiguration> {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationSplitter.class);
    private final Class<T[]> aClass;

    public ConfigurationSplitter(Class<T[]> aClass) {
        this.aClass = aClass;
    }

    public List<T> splitConfig(Exchange exchange) {
        String configJson = exchange.getIn().getBody(String.class);

        ObjectMapper mapper = new ObjectMapper();
        logger.debug("Deserializing Config JSON to Client list:");
        T[] configuration;
        try {
            configuration = mapper.readValue(configJson, aClass);
        } catch (JsonProcessingException e) {
            logger.error("Exception in ConfigSplitHandler. {}", e.getMessage());
            return null;
        }

        return Arrays.asList(configuration);
    }
}
