package com.example.integration;

import com.example.integration.configuration.SftpConfiguration;
import com.example.integration.filter.StartDateEndDateFilter;
import com.example.integration.processor.AppRestApiProcessor;
import com.example.integration.processor.CreateCsvProcessor;
import com.example.integration.splitter.ConfigurationSplitter;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IntegrationRoute extends AbstractIntegrationRoute {
    private static final Logger logger = LoggerFactory.getLogger(IntegrationRoute.class);

    private static final String CREATE_CSV_ROUTE = "direct:createCsv";
    private static final String WRITE_CSV_ROUTE = "direct:writeCsvFile";

    @Override
    protected Expression getIntegrationConfigurationSplitter() {
        return method(new ConfigurationSplitter<>(SftpConfiguration[].class), "splitConfig");
    }

    @Override
    protected Predicate getCustomerFilter() {
        return new StartDateEndDateFilter();
    }

    @Override
    protected void buildIntegrationFlow(RouteDefinition integrationRouteDefinition) {
        integrationRouteDefinition
                /*.process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {

                        logger.debug("Customer flow process");
                    }
                });*/
                //.from("rest:get:https://randomuser.me/api/?results=10")
                //.toD("rest:get:https?host=randomuser.me/api/?results=10")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .to("https://randomuser.me/api/?results=10")
                .convertBodyTo(String.class)
                /*.process(exchange -> {
                    logger.info("Rest response: {}", exchange.getIn().getBody().toString());
                });*/
                .process(new AppRestApiProcessor())
                .to(CREATE_CSV_ROUTE);

        from(CREATE_CSV_ROUTE)
                .process(new CreateCsvProcessor())
                .to("log:info")
                .to(WRITE_CSV_ROUTE);

        from(WRITE_CSV_ROUTE)
                /*.process(exchange -> {
                    logger.info("Rest response: {}", exchange.getIn().getBody().toString());
                })*/
                .to("file://output/fileWritingFlow?fileName=${header.customer.name}_data_${date:now:ddMMyyyy_hh-mm-ss}.csv")
                .to(SUCCESS_LOG_ROUTE);
    }
}
