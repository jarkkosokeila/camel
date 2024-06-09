package com.example.jsonata;

import com.example.integration.AbstractIntegrationRoute;
import com.example.integration.filter.StartDateEndDateFilter;
import com.example.integration.processor.AppRestApiProcessor;
import com.example.integration.processor.CreateCsvJsonataProcessor;
import com.example.integration.processor.CreateCsvProcessor;
import com.example.jsonata.aggregate.CsvAggregate;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IntegrationRoute extends AbstractIntegrationRoute {
    private static final Logger logger = LoggerFactory.getLogger(IntegrationRoute.class);

    private static final String CREATE_CSV_ROUTE = "direct:createCsv";
    private static final String WRITE_CSV_ROUTE = "direct:writeCsvFile";

    @Value("${cronScheduler}")
    private String cron;

    @Override
    protected String getCron() {
        return cron;
    }

    @Override
    protected Predicate getCustomerFilter() {
        return new StartDateEndDateFilter();
    }

    @Override
    protected void buildIntegrationRoute(RouteDefinition integrationRouteDefinition) {
        buildReadPersonDataRoute(integrationRouteDefinition);
    }

    private void buildReadPersonDataRoute(RouteDefinition from) {
        CsvDataFormat csv = new CsvDataFormat();
        csv.setDelimiter(';');

        from
                .process(exchange->exchange.setProperty("start_time", System.currentTimeMillis()))
                .log("Reading customer ${exchangeProperty.customer.name} data")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .to("https://randomuser.me/api/?results=100")
                .convertBodyTo(String.class)
                .split().jsonpathWriteAsString("$.results[*]", List.class).aggregationStrategy(new CsvAggregate())
                    .process(new CreateCsvJsonataProcessor())
                    .marshal(csv)
                    .process(exchange -> {
                        logger.info("Csv row: {}", exchange.getIn().getBody(String.class));
                    })
                .end()
                .to(IntegrationRoute.WRITE_CSV_ROUTE);

        buildWriteCSVRoute();
    }

    private void buildWriteCSVRoute() {
        from(IntegrationRoute.WRITE_CSV_ROUTE)
                .log("Write csv file from customer ${exchangeProperty.customer.name} data")
                .process(exchange -> {
                    logger.info("Csv to be written: {}", exchange.getIn().getBody().toString());
                })
                //.to("sftp:localhost:2222/test?username=demo&password=demo&fileName=${header.customer.name}_data_${date:now:ddMMyyyy_hh-mm-ss}.csv")
                .to("file://output/fileWritingFlow?fileName=${exchangeProperty.customer.name}_data_${date:now:ddMMyyyy_hh-mm-ss}.csv")
                .process(exchange-> {
                    Long startTime = exchange.getProperty("start_time", Long.class);
                    logger.info("Execution time: {}", TimeUtils.printDuration(System.currentTimeMillis() - startTime));
                })
                .to(SUCCESS_LOG_ROUTE);
    }
}
