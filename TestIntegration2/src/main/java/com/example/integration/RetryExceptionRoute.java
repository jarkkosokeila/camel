package com.example.integration;

import com.example.integration.configuration.SftpConfiguration;
import com.example.integration.dto.Results;
import com.example.integration.filter.StartDateEndDateFilter;
import com.example.integration.splitter.ConfigurationSplitter;
import org.apache.camel.Expression;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spi.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RetryExceptionRoute extends AbstractIntegrationRoute {
    private static final Logger logger = LoggerFactory.getLogger(RetryExceptionRoute.class);

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
        //transformer().name("dtoTransformer").withJava(DtoTransformer.class);
        transformer().scan("com.example.integration.transform");

        from
                .log("Reading customer ${header.customer.name} data")
                //.setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .to("rest:get:api?host=https://randomuser.me&queryParameters=results=2")
                .unmarshal().json(JsonLibrary.Jackson, Results.class)
                .transform(new DataType("dtoTransformer"))
                .to(RetryExceptionRoute.SPLIT_RESULTS_ROUTE);

        buildSplitChoiceRoute();
    }

    private void buildSplitChoiceRoute() {
        from(RetryExceptionRoute.SPLIT_RESULTS_ROUTE)
                .log("Create gender handling from customer ${header.customer.name} data")
                .errorHandler(deadLetterChannel(EXCEPTION_ROUTE)
                        .maximumRedeliveries(2)
                        .redeliveryDelay(1000)
                        .retryAttemptedLogLevel(LoggingLevel.WARN))
                .split(body()).stopOnException()
                .to("log:info")
                    .choice()
                        .when(simple("${body.getGender()} == 'male'"))
                            .log("Male handling").process(exchange -> {
                                exchange.setException(new RuntimeException("sgas"));
                                throw new RuntimeException("Test exception");
                            })
                        .otherwise()
                            .log("Female handling").process(exchange -> {
                                exchange.setException(new RuntimeException("sgas"));
                                throw new RuntimeException("Test exception");
                            })
                    .end()
                .end()
                .to("log:info")
                .to(RetryExceptionRoute.SUCCESS_LOG_ROUTE);
    }
}
