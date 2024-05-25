package com.example.integration;

import org.apache.camel.model.RouteDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestRoute extends AbstractIntegrationRoute {

    @Value("${cronScheduler3}")
    private String cron;

    @Override
    protected String getCron() {
        return cron;
    }

    @Override
    protected void buildIntegrationRoute(RouteDefinition from) {
        from
                .log("Reading customer ${exchangeProperty.customer.name} data")
                //.setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .to("rest:get:api?host=https://randomuser.me&queryParameters=results=2")
                    .choice()
                        .when(simple("${header.CamelHttpResponseCode} == 200"))
                            .log("OK handling")
                        .otherwise()
                            .log("Error handling")
                    .end()
                .convertBodyTo(String.class)
                .to("log:info")
                .process(exchange -> {
                    exchange.getIn();
                })
                .split().jsonpathWriteAsString("$.results[*]", List.class)
                .to("log:info");

    }
}
