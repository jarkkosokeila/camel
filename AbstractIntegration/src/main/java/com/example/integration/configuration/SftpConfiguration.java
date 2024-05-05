package com.example.integration.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SftpConfiguration extends CustomerConfiguration {
    @IntegrationConfig(key = ConfigKeyValue.SFTP_HOST)
    @JsonProperty(ConfigKeyValue.SFTP_HOST)
    private String sftpHost;
    @IntegrationConfig(key = ConfigKeyValue.SFTP_USERNAME)
    @JsonProperty(ConfigKeyValue.SFTP_USERNAME)
    private String sftpUsername;
    @IntegrationConfig(key = ConfigKeyValue.SFTP_PASSWORD)
    @JsonProperty(ConfigKeyValue.SFTP_PASSWORD)
    private String sftpPassword;
    @IntegrationConfig(key = ConfigKeyValue.SFTP_PORT)
    @JsonProperty(ConfigKeyValue.SFTP_PORT)
    private int sftpPort;

    public String getSftpHost() {
        return sftpHost;
    }

    public void setSftpHost(String sftpHost) {
        this.sftpHost = sftpHost;
    }

    public String getSftpUsername() {
        return sftpUsername;
    }

    public void setSftpUsername(String sftpUsername) {
        this.sftpUsername = sftpUsername;
    }

    public String getSftpPassword() {
        return sftpPassword;
    }

    public void setSftpPassword(String sftpPassword) {
        this.sftpPassword = sftpPassword;
    }

    public int getSftpPort() {
        return sftpPort;
    }

    public void setSftpPort(int sftpPort) {
        this.sftpPort = sftpPort;
    }
}
