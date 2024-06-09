package com.example.integration.processor;

import com.dashjoin.jsonata.Jsonata;
import com.example.integration.configuration.ConfigKeyValue;
import com.example.integration.dto.Results;
import com.example.integration.exception.IntegrationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static com.dashjoin.jsonata.Jsonata.jsonata;

public class CreateCsvJsonataProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(CreateCsvJsonataProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        try {
            logger.debug("Create csv");
            String json = exchange.getIn().getBody(String.class);
            String customer = exchange.getProperty(ConfigKeyValue.CUSTOMER_NAME, String.class);

            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();

            // Read HR json
            //String json = IOUtils.toString(Path.of("src/main/resources/employee.json").toUri(), StandardCharsets.UTF_8);
            Map<String, Object> input = mapper.readValue(json, Map.class);
            System.out.println("INPUT:\n" + objectWriter.writeValueAsString(input));

            // Transform HR json to ValueFrame json
            String transformer = customer + "_transformation.jsonata";
            String transformation;
                    //String transformation = IOUtils.toString(Path.of("src/main/resources/transformation.jsonata").toUri(), StandardCharsets.UTF_8);
            try {
                transformation = IOUtils.toString(Path.of("config/" + transformer).toUri(), StandardCharsets.UTF_8);
            } catch (FileNotFoundException e) {
                transformation = IOUtils.toString(Path.of("config/transformation.jsonata").toUri(), StandardCharsets.UTF_8);
            }
            Jsonata expression = jsonata(transformation);
            var result = expression.evaluate(input);

            // Print ValueFrame json
            System.out.println("OUTPUT:\n" + objectWriter.writeValueAsString(result));

            exchange.getIn().setBody(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IntegrationException(e.getMessage(), e);
        }
    }
}
