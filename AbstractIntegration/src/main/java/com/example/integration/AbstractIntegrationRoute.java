package com.example.integration;

import com.example.integration.configuration.ConfigValues;
import com.example.integration.processor.CustomerHeadersProcessor;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractIntegrationRoute extends RouteBuilder {
    private static final Logger logger = LoggerFactory.getLogger(AbstractIntegrationRoute.class);

    private static final String EXCEPTION_ROUTE = "direct:exceptionRoute";

    private static final String INIT_ROUTE = "direct:initRoute";
    private static final String INTEGRATION_ROUTE = "direct:integrationRoute";

    protected static final String SUCCESS_LOG_ROUTE = "direct:successLogRoute";

    @Override
    public void configure() throws Exception {
        errorHandler(deadLetterChannel(EXCEPTION_ROUTE));
        createExceptionRoute();

        /*from("quartz://integrationScheduler?cron={{cronScheduler}}")
                .setBody(constant("Test"))
                .to("log:info");*/

        from("file://config/?fileName=integration.config.json&noop=true&idempotent=false&scheduler=quartz&scheduler.cron={{cronScheduler}}")
                .convertBodyTo(String.class)
                //.process(new ConfigSplitProcessor<>(SftpClient[].class))
                .split(getIntegrationConfigurationSplitter()).streaming()
                //.to("log:info")
                .to(INIT_ROUTE);

        from(INIT_ROUTE)
                .process(new CustomerHeadersProcessor())
                .filter(getCustomerFilter())
                //.to("log:info");
                .to(INTEGRATION_ROUTE);

        RouteDefinition integrationRouteDefinition = from(INTEGRATION_ROUTE);

        buildIntegrationFlow(integrationRouteDefinition);

        from(SUCCESS_LOG_ROUTE)
                .process(exchange -> {
                    String customer = (String) exchange.getIn().getHeader(ConfigValues.CUSTOMER_NAME);

                    StringBuilder bodyBuilder = new StringBuilder();
                    bodyBuilder.append("{");
                    bodyBuilder.append("\"status\"").append(":").append("\"SUCCESS\"");
                    bodyBuilder.append(",");
                    bodyBuilder.append("\"message\"").append(":").append("\"Integration finish successfully for customer: ").append(customer).append("\"");

                    bodyBuilder.append("}");

                    exchange.getIn().setBody(bodyBuilder.toString());
                })
                .setHeader("Content-Type", constant("application/json"))
                .toD("http:${header.app.host}/api/save-interface-log?httpMethod=POST");
    }

    private void createExceptionRoute() {
        from(EXCEPTION_ROUTE)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        String customer = (String) exchange.getIn().getHeader(ConfigValues.CUSTOMER_NAME);

                        StringBuilder bodyBuilder = new StringBuilder();
                        bodyBuilder.append("{");
                        bodyBuilder.append("\"status\"").append(":").append("\"FAILED\"");

                        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                        logger.info("Exception flow process");
                        if(cause != null) {
                            logger.error("Error message {}", cause.getMessage());
                            String message = cause.getMessage();
                            message = message.replaceAll("\"", "'");
                            bodyBuilder.append(",");
                            bodyBuilder.append("\"message\"").append(":").append("\"failed for customer: ").append(customer).append(". Error message: ").append(message).append("\"");
                        }

                        bodyBuilder.append("}");

                        exchange.getIn().setBody(bodyBuilder.toString());
                    }
                })
                .setHeader("Content-Type", constant("application/json"))
                .toD("http:${header.app.host}/api/save-interface-log?httpMethod=POST");
    }

    protected Predicate getCustomerFilter() {
        return exchange -> true;
    }

    /**
     * Splitter which get integration.config.json content and map array of configurations to configuration bean object list
     */
    protected abstract Expression getIntegrationConfigurationSplitter();

    /**
     * Build integration flow in this method
     */
    protected abstract void buildIntegrationFlow(RouteDefinition integrationRouteDefinition);
}
