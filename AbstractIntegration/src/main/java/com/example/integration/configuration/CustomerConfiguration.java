package com.example.integration.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerConfiguration {
    @IntegrationConfig(key = ConfigKeyValue.CUSTOMER_NAME)
    @JsonProperty(ConfigKeyValue.CUSTOMER_NAME)
    private String customerName;
    @IntegrationConfig(key = ConfigKeyValue.APP_HOST)
    @JsonProperty(ConfigKeyValue.APP_HOST)
    private String appHost;
    @IntegrationConfig(key = ConfigKeyValue.APP_API_KEY)
    @JsonProperty(ConfigKeyValue.APP_API_KEY)
    private String appApiKey;
    @IntegrationConfig(key = ConfigKeyValue.START_DATE)
    @JsonProperty(ConfigKeyValue.START_DATE)
    private String startDate;
    @IntegrationConfig(key = ConfigKeyValue.END_DATE)
    @JsonProperty(ConfigKeyValue.END_DATE)
    private String endDate;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAppHost() {
        return this.appHost;
    }

    public void setAppHost(String appHost) {
        this.appHost = appHost;
    }

    public String getAppApiKey() {
        return this.appApiKey;
    }

    public void setAppApiKey(String appApiKey) {
        this.appApiKey = appApiKey;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
