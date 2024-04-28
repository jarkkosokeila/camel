package com.sokeila.camel.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Client {
    @IntegrationConfig(key = ConfigValues.CUSTOMER_NAME)
    @JsonProperty(ConfigValues.CUSTOMER_NAME)
    private String customerName;
    @IntegrationConfig(key = ConfigValues.APP_HOST)
    @JsonProperty(ConfigValues.APP_HOST)
    private String appHost;
    @IntegrationConfig(key = ConfigValues.APP_API_KEY)
    @JsonProperty(ConfigValues.APP_API_KEY)
    private String appApiKey;
    @IntegrationConfig(key = ConfigValues.START_DATE)
    @JsonProperty(ConfigValues.START_DATE)
    private String startDate;
    @IntegrationConfig(key = ConfigValues.END_DATE)
    @JsonProperty(ConfigValues.END_DATE)
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
