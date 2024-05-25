package com.example.integration;

import com.example.integration.filter.StartDateEndDateFilter;
import com.example.integration.processor.AppRestApiProcessor;
import com.example.integration.processor.CreateCsvProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
        from
                .log("Reading customer ${exchangeProperty.customer.name} data")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .to("https://randomuser.me/api/?results=10")
                .convertBodyTo(String.class)
                .process(new AppRestApiProcessor())
                .to(IntegrationRoute.CREATE_CSV_ROUTE);

        buildCreateCSVRoute();
    }

    private void buildCreateCSVRoute() {
        from(IntegrationRoute.CREATE_CSV_ROUTE)
                .log("Create csv from customer ${exchangeProperty.customer.name} data")
                .process(new CreateCsvProcessor())
                .to("log:info")
                .to(IntegrationRoute.WRITE_CSV_ROUTE);

        buildWriteCSVRoute();
    }

    private void buildWriteCSVRoute() {
        from(IntegrationRoute.WRITE_CSV_ROUTE)
                .log("Write csv file from customer ${exchangeProperty.customer.name} data")
                /*.process(exchange -> {
                    logger.info("Rest response: {}", exchange.getIn().getBody().toString());
                })*/
                //.to("sftp:localhost:2222/test?username=demo&password=demo&fileName=${header.customer.name}_data_${date:now:ddMMyyyy_hh-mm-ss}.csv")
                .to("file://output/fileWritingFlow?fileName=${exchangeProperty.customer.name}_data_${date:now:ddMMyyyy_hh-mm-ss}.csv")
                .to(SUCCESS_LOG_ROUTE);
    }
}
