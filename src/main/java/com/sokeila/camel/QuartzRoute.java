package com.sokeila.camel;

import com.sokeila.camel.configuration.SftpClient;
import com.sokeila.camel.filter.StartDateEndDateFilter;
import com.sokeila.camel.processor.AppRestApiProcessor;
import com.sokeila.camel.processor.ClientHeadersProcessor;
import com.sokeila.camel.processor.CreateCsvProcessor;
import com.sokeila.camel.splitter.ConfigSplitter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class QuartzRoute extends RouteBuilder {
    private static final Logger logger = LoggerFactory.getLogger(QuartzRoute.class);

    @Override
    public void configure() throws Exception {
        /*from("quartz://integrationScheduler?cron={{cronScheduler}}")
                .setBody(constant("Test"))
                .to("log:info");*/

        from("file://config/?fileName=integration.config.json&noop=true&idempotent=false&scheduler=quartz&scheduler.cron={{cronScheduler}}")
                .convertBodyTo(String.class)
                //.process(new ConfigSplitProcessor<>(SftpClient[].class))
                .split(method(new ConfigSplitter<>(SftpClient[].class), "splitConfig")).streaming()
                //.to("log:info")
                .to("direct:initFlow");

        from("direct:initFlow")
                .filter(new StartDateEndDateFilter())
                .process(new ClientHeadersProcessor())
                //.to("log:info");
                .to("direct:integrationFlowChannel");


        buildIntegrationFlow();

    }

    private void buildIntegrationFlow() {
        from("direct:integrationFlowChannel")
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
                .to("direct:createCsv");

        from("direct:createCsv")
                .process(new CreateCsvProcessor())
                //.to("log:info");
                .to("direct:writeCsvFile");

        from("direct:writeCsvFile")
                .process(exchange -> {
                    logger.info("Rest response: {}", exchange.getIn().getBody().toString());
                })
                .to("file://output/fileWritingFlow?fileName=${header.customer.name}_data_${date:now:ddMMyyyy_hh-mm-ss}.csv");

    }
}
