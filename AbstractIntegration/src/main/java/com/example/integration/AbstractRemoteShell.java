package com.example.integration;

import org.apache.camel.CamelContext;
import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractRemoteShell extends RouteBuilder {
    private ServerSocket serverSocket;

    @Override
    public void configure() throws Exception {
        from("timer://runOnce?repeatCount=1").routeId("start").autoStartup(false)
                .onCompletion()
                    .process(exchange -> {
                        getContext().getRouteController().stopRoute("start");
                    })
                .end()
                .pollEnrich("file://config/?fileName=integration.config.json&noop=true&idempotent=false")
                .log("Read configuration and split configuration json array to Configuration bean list")
                .convertBodyTo(String.class)
                .split(getIntegrationConfigurationSplitter()).streaming()
                //.to("log:info")
                .to("direct:initRoute");

        serverSocket = new ServerSocket(2222);
        Runnable basic = () -> {
            while (true) {
                try {
                    new EchoClientHandler(serverSocket.accept(), AbstractRemoteShell.this, getContext()).start();
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

    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private AbstractRemoteShell abstractRemoteShell;
        private CamelContext context;

        public EchoClientHandler(Socket socket, AbstractRemoteShell abstractRemoteShell, CamelContext context) {
            this.clientSocket = socket;
            this.abstractRemoteShell = abstractRemoteShell;
            this.context = context;
        }

        public void run() {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                out.println(abstractRemoteShell.getWelcomeMessage());
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (".".equals(inputLine)) {
                        out.println("bye");
                        break;
                    }
                    if ("start".equals(inputLine)) {
                        context.getRouteController().startRoute("start");
                        out.println("Integration started");
                    }
                    out.println(inputLine);
                }
            } catch (Exception e) {

            }
            try {
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected abstract Expression getIntegrationConfigurationSplitter();
}
