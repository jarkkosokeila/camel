package com.example.integration;

import com.example.integration.configuration.SftpConfiguration;
import com.example.integration.splitter.ConfigurationSplitter;
import org.apache.camel.Expression;
import org.apache.camel.model.RouteDefinition;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestRoute extends AbstractIntegrationRoute {

    @Override
    protected Expression getIntegrationConfigurationSplitter() {
        return method(new ConfigurationSplitter<>(SftpConfiguration[].class), "splitConfig");
    }

    @Override
    protected void buildIntegrationRoute(RouteDefinition from) {
        from
                .log("Reading customer ${header.customer.name} data")
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
