package com.example.integration;

import com.example.integration.configuration.SftpConfiguration;
import com.example.integration.splitter.ConfigurationSplitter;
import org.apache.camel.Expression;
import org.springframework.stereotype.Component;

@Component
public class RemoteShell extends AbstractRemoteShell {

    @Override
    protected String getWelcomeMessage() {
        return "Welcome to TestIntegration2 shell";
    }

    @Override
    protected Expression getIntegrationConfigurationSplitter() {
        return method(new ConfigurationSplitter<>(SftpConfiguration[].class), "splitConfig");
    }
}
