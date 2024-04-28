package com.sokeila.camel.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sokeila.camel.dto.Results;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class AppRestApiProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(AppRestApiProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        String responseBody = exchange.getIn().getBody(String.class);

        logger.debug(responseBody);

        ObjectMapper mapper = new ObjectMapper();
        logger.debug("Deserializing JSON to Object:");
        Results results;
        try {
            results = mapper.readValue(responseBody, Results.class);
            List<Results.Person> persons = results.getResults();
            exchange.getIn().setBody(persons);
        } catch (JsonProcessingException e) {
            logger.error("Exception in AppRestApiHandler. {}", e.getMessage());
        }
    }
}
