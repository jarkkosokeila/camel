package com.example.integration.filter;

import com.example.integration.configuration.ConfigKeyValue;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

import java.util.concurrent.atomic.AtomicReference;

public class CustomerFilter implements Predicate {
    private final AtomicReference<String> customer;

    public CustomerFilter(AtomicReference<String> customer) {
        this.customer = customer;
    }

    @Override
    public boolean matches(Exchange exchange) {
        if(customer.get() != null) {
            String customer = exchange.getProperty(ConfigKeyValue.CUSTOMER_NAME, String.class);
            return customer.equals(this.customer.get());
        }
        return true;
    }
}
