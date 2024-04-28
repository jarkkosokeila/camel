package com.sokeila.camel.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SftpClient extends Client {
    @IntegrationConfig(key = ConfigValues.SFTP_HOST)
    @JsonProperty(ConfigValues.SFTP_HOST)
    private String sftpHost;
    @IntegrationConfig(key = ConfigValues.SFTP_USERNAME)
    @JsonProperty(ConfigValues.SFTP_USERNAME)
    private String sftpUsername;
    @IntegrationConfig(key = ConfigValues.SFTP_PASSWORD)
    @JsonProperty(ConfigValues.SFTP_PASSWORD)
    private String sftpPassword;
    @IntegrationConfig(key = ConfigValues.SFTP_PORT)
    @JsonProperty(ConfigValues.SFTP_PORT)
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
