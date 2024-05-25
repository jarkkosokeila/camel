package com.example.integration;

import org.springframework.stereotype.Component;

@Component
public class RemoteShell extends AbstractRemoteShell {

    @Override
    protected String getWelcomeMessage() {
        return "Welcome to TestIntegration2 shell";
    }
}
