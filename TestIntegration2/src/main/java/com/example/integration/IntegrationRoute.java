package com.example.integration;

import com.example.integration.configuration.SftpConfiguration;
import com.example.integration.filter.StartDateEndDateFilter;
import com.example.integration.processor.AppRestApiProcessor;
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

    private static final String SPLIT_RESULTS_ROUTE = "direct:splitResults";
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
    protected void buildIntegrationRoute(RouteDefinition integrationRouteDefinition) {
        buildReadPersonDataRoute(integrationRouteDefinition);
    }

    private void buildReadPersonDataRoute(RouteDefinition from) {
        from
                .log("Reading customer ${header.customer.name} data")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .to("https://randomuser.me/api/?results=10")
                .convertBodyTo(String.class)
                .process(new AppRestApiProcessor())
                .to(IntegrationRoute.SPLIT_RESULTS_ROUTE);

        buildSplitChoiceRoute();
    }

    private void buildSplitChoiceRoute() {
        from(IntegrationRoute.SPLIT_RESULTS_ROUTE)
                .log("Create gender handling from customer ${header.customer.name} data")
                .split(body())
                    .to("log:info")
                    .choice()
                        .when(simple("${body.getGender()} == 'male'"))
                            .log("Male handling")
                        .otherwise()
                            .log("Female handling")
                    .end()
                .end()
                .to(IntegrationRoute.SUCCESS_LOG_ROUTE);
    }
}
