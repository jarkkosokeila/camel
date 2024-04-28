package com.sokeila.camel.processor;

import com.sokeila.camel.dto.Results;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class CreateCsvProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(CreateCsvProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.debug("Create csv");
        List<Results.Person> persons = (List<Results.Person>) exchange.getIn().getBody(List.class);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("foobar").append("\r\n");
        for(Results.Person person : persons) {
            stringBuilder.append(person).append("\r\n");
        }

        exchange.getIn().setBody(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }
}
