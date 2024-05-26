package com.example.integration;

import com.example.integration.filter.CustomerFilter;
import com.example.integration.processor.CustomerHeadersProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jackson.ListJacksonDataFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractRemoteShell extends RouteBuilder {
    private ServerSocket serverSocket;
    private AtomicReference<String> customer = new AtomicReference<>();

    @Override
    public void configure() throws Exception {
        JacksonDataFormat format = new ListJacksonDataFormat(Map.class);
        List<String> integrationRoutes = getIntegrationRoutes();
        for(String route : integrationRoutes) {
            from("timer://" + route + "?repeatCount=1").routeId(route).autoStartup(false)
                    .onCompletion()
                    .process(exchange -> {
                        getContext().getRouteController().stopRoute(route);
                    })
                    .end()
                    .pollEnrich("file://config/?fileName=integration.config.json&noop=true&idempotent=false")
                    .log("Read configuration and split configuration json array to Configuration bean list")
                    .convertBodyTo(String.class)
                    .unmarshal(format)
                    .split(body()).streaming()
                    .process(new CustomerHeadersProcessor())
                    .filter(new CustomerFilter(customer))
                    //.to("log:info")
                    .to("direct:initRoute" + route);
        }



        serverSocket = new ServerSocket(2222);
        Runnable basic = () -> {
            while (true) {
                try {
                    new EchoClientHandler(serverSocket.accept(), integrationRoutes, customer, AbstractRemoteShell.this, getContext()).start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        // Instantiating two thread classes
        Thread thread1 = new Thread(basic);

        // Running two threads for the same task
        thread1.start();
    }

    protected abstract String getWelcomeMessage();

    protected abstract List<String> getIntegrationRoutes();

    private static class EchoClientHandler extends Thread {
        private final Socket clientSocket;
        private final List<String> integrationRoutes;
        private final AtomicReference<String> customer;
        private final AbstractRemoteShell abstractRemoteShell;
        private final CamelContext context;

        public EchoClientHandler(Socket socket, List<String> integrationRoutes, AtomicReference<String> customer, AbstractRemoteShell abstractRemoteShell, CamelContext context) {
            this.clientSocket = socket;
            this.integrationRoutes = integrationRoutes;
            this.customer = customer;
            this.abstractRemoteShell = abstractRemoteShell;
            this.context = context;
        }

        public void run() {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                out.println(abstractRemoteShell.getWelcomeMessage());
                printui(out, integrationRoutes);
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if ("exit".equals(inputLine)) {
                        out.println("bye");
                        break;
                    } else {
                        String[] command = inputLine.split(" ");
                        if (command[0].equals("start")) {
                            if(command.length == 2) {
                                customer.set(null);
                            } else if (command.length == 3) {
                                customer.set(command[2]);
                            }

                            context.getRouteController().startRoute(command[1]);
                            out.println("Integration started");
                        } else {
                            out.println("Unknown command");
                        }
                    }
                    printui(out, integrationRoutes);
                }
            } catch (Exception e) {

            }
            try {
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void printui(PrintWriter out, List<String> integrationRoutes) {
            out.println("Commands:\n");
            for (String route : integrationRoutes) {
                out.println("start " + route + " <customer>");
            }
            out.println("exit");
            out.print("> ");
            out.flush();
        }
    }
}
