package com.example.integration;

import com.example.integration.configuration.ConfigKeyValue;
import com.example.integration.processor.CustomerHeadersProcessor;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class AbstractIntegrationRoute extends RouteBuilder {
    private static final Logger logger = LoggerFactory.getLogger(AbstractIntegrationRoute.class);

    protected final String EXCEPTION_ROUTE = "direct:exceptionRoute" + getClass().getSimpleName();

    private final String INIT_ROUTE = "direct:initRoute" + getClass().getSimpleName();;
    private final String INTEGRATION_ROUTE = "direct:integrationRoute" + getClass().getSimpleName();;

    protected final String SUCCESS_LOG_ROUTE = "direct:successLogRoute" + getClass().getSimpleName();;

    @Override
    public void configure() throws Exception {
        errorHandler(deadLetterChannel(EXCEPTION_ROUTE));
        createExceptionRoute();

        buildConfigurationReaderRoute();

        buildInitRoute();

        RouteDefinition integrationRouteDefinition = from(INTEGRATION_ROUTE);
        buildIntegrationRoute(integrationRouteDefinition);

        buildSuccessfulRoute();
    }

    /**
     * This method reads configuration (json) file and split each customer configuration to configuration bean
     */
    private void buildConfigurationReaderRoute() {
        JacksonDataFormat format = new ListJacksonDataFormat(Map.class);
        from("file://config/?fileName=integration.config.json&noop=true&idempotent=false&scheduler=quartz&scheduler.cron=" + getCron())
                .log("Read configuration and split configuration json array to Configuration bean list")
                .convertBodyTo(String.class)
                .unmarshal(format)
                .split(body()).streaming()
                //.to("log:info")
                .to(INIT_ROUTE);
    }

    protected abstract String getCron();

    /**
     * This method set customer configuration values into to header parameters.<br>
     * Also, this method filters customers from integration if filter is defined in integration implementation
     */
    private void buildInitRoute() {
        from(INIT_ROUTE)
                .log("Set customer configuration values into header parameters and do filtering if filter is defined")
                .process(new CustomerHeadersProcessor())
                .filter(getCustomerFilter())
                //.to("log:info");
                .to(INTEGRATION_ROUTE);
    }

    /**
     * This method send success rest request into log endpoint
     */
    private void buildSuccessfulRoute() {
        from(SUCCESS_LOG_ROUTE)
                .log("Integration was completed successfully for customer ${header.customer.name}. Send log data to logger endpoint")
                .process(exchange -> {
                    String customer = (String) exchange.getIn().getHeader(ConfigKeyValue.CUSTOMER_NAME);

                    StringBuilder bodyBuilder = new StringBuilder();
                    bodyBuilder.append("{");
                    bodyBuilder.append("\"status\"").append(":").append("\"SUCCESS\"");
                    bodyBuilder.append(",");
                    bodyBuilder.append("\"message\"").append(":").append("\"Integration finish successfully for customer: ").append(customer).append("\"");
                    bodyBuilder.append("}");

                    exchange.getIn().setBody(bodyBuilder.toString());
                })
                .setHeader("Content-Type", constant("application/json"));
                //.toD("http:${header.app.host}/api/save-interface-log?httpMethod=POST");
    }

    /**
     * This method send failed rest request into log endpoint if exception is occurred in integration
     */
    private void createExceptionRoute() {
        from(EXCEPTION_ROUTE)
                .log(LoggingLevel.ERROR, "Exception was occurred during integration process")
                .process(exchange -> {
                    String customer = (String) exchange.getIn().getHeader(ConfigKeyValue.CUSTOMER_NAME);

                    StringBuilder bodyBuilder = new StringBuilder();
                    bodyBuilder.append("{");
                    bodyBuilder.append("\"status\"").append(":").append("\"FAILED\"");

                    Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                    if(cause != null) {
                        logger.error("Error message: {}", cause.getMessage());
                        String message = cause.getMessage();
                        message = message.replaceAll("\"", "'");
                        bodyBuilder.append(",");
                        bodyBuilder.append("\"message\"").append(":").append("\"failed for customer: ").append(customer).append(". Error message: ").append(message).append("\"");
                    }

                    bodyBuilder.append("}");

                    exchange.getIn().setBody(bodyBuilder.toString());
                })
                .setHeader("Content-Type", constant("application/json"));
                //.toD("http:${header.app.host}/api/save-interface-log?httpMethod=POST");
    }

    protected Predicate getCustomerFilter() {
        return exchange -> true;
    }

    /**
     * Build integration route in this method
     */
    protected abstract void buildIntegrationRoute(RouteDefinition integrationRouteDefinition);
}
