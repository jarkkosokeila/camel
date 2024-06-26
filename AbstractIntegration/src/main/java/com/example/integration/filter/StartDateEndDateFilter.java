package com.example.integration.filter;

import com.example.integration.configuration.ConfigKeyValue;
import com.example.integration.exception.IntegrationException;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class StartDateEndDateFilter implements Predicate {
    private static final Logger logger = LoggerFactory.getLogger(StartDateEndDateFilter.class);

    @Override
    public boolean matches(Exchange exchange) {
        Date now = Calendar.getInstance().getTime();

        String startDateStr = exchange.getProperty(ConfigKeyValue.START_DATE, String.class);
        String endDateStr = exchange.getProperty(ConfigKeyValue.END_DATE, String.class);
        try {
            Date startDate = StringUtils.isNoneBlank(startDateStr) ? new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(startDateStr) : null;
            Date endDate = StringUtils.isNoneBlank(endDateStr) ? new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(endDateStr) : null;

            if((startDate != null && startDate.before(now)) && (endDate == null || endDate.after(now))) {
                return true;
            }
        } catch (ParseException e) {
            //logger.error("Failed to parse date values. Client: {}", client);
            //logger.error("Exception", e);
            logger.error("Failed to parse date value. {}", e.getMessage());
            //System.out.println("@@@@@@@@@@@@@@@@@@@@@" + e.getMessage());
            //logger.info("Customer {} was filtered out", customerConfiguration.getCustomerName());

            throw new IntegrationException("Failed to parse date value. " + e.getMessage(), e);
        }

        logger.info("Customer {} was filtered out. Integration was configured to run between {} and {}", exchange.getProperty(ConfigKeyValue.CUSTOMER_NAME, String.class), startDateStr, endDateStr);
        return false;
    }
}
