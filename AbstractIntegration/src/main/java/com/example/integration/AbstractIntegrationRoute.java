package com.example.integration;

import com.example.integration.processor.CustomerHeadersProcessor;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractIntegrationRoute extends RouteBuilder {
    private static final Logger logger = LoggerFactory.getLogger(AbstractIntegrationRoute.class);

    @Override
    public void configure() throws Exception {
        errorHandler(deadLetterChannel("direct:exceptionFlow"));
        from("direct:exceptionFlow")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                        logger.info("Exception flow process");
                        if(cause != null) {
                            logger.error("Error message {}", cause.getMessage());
                        }
                    }
                })
                .toD("""
                        ${header.app.host}
                        ?httpMethod=POST
                        &authMethod=Basic
                        &authUsername=
                        &authPassword=
                        """);

        /*from("quartz://integrationScheduler?cron={{cronScheduler}}")
                .setBody(constant("Test"))
                .to("log:info");*/

        from("file://../config/?fileName=integration.config.json&noop=true&idempotent=false&scheduler=quartz&scheduler.cron={{cronScheduler}}")
                .convertBodyTo(String.class)
                //.process(new ConfigSplitProcessor<>(SftpClient[].class))
                .split(getIntegrationConfigurationSplitter()).streaming()
                //.to("log:info")
                .to("direct:initFlow");

        from("direct:initFlow")
                .filter(getCustomerFilter())
                .process(new CustomerHeadersProcessor())
                //.to("log:info");
                .to("direct:integrationFlowChannel");

        RouteDefinition integrationRouteDefinition = from("direct:integrationFlowChannel");

        buildIntegrationFlow(integrationRouteDefinition);
    }

    protected Predicate getCustomerFilter() {
        return exchange -> true;
    }

    protected abstract Expression getIntegrationConfigurationSplitter();

    protected abstract void buildIntegrationFlow(RouteDefinition integrationRouteDefinition);
}
