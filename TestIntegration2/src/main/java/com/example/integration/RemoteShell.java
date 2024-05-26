package com.example.integration;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RemoteShell extends AbstractRemoteShell {

    @Override
    protected String getWelcomeMessage() {
        return "Welcome to TestIntegration2 shell";
    }

    @Override
    protected List<String> getIntegrationRoutes() {
        return List.of("TestRoute");
    }
}
