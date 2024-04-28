package com.sokeila.camel.filter;

import com.sokeila.camel.configuration.Client;
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
        Client client = exchange.getIn().getBody(Client.class);
        Date now = Calendar.getInstance().getTime();

        try {
            String startDateStr = client.getStartDate();
            Date startDate = StringUtils.isNoneBlank(startDateStr) ? new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(startDateStr) : null;
            String endDateStr = client.getEndDate();
            Date endDate = StringUtils.isNoneBlank(endDateStr) ? new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(endDateStr) : null;

            if((startDate != null && startDate.before(now)) && (endDate == null || endDate.after(now))) {
                return true;
            }
        } catch (ParseException e) {
            //logger.error("Failed to parse date values. Client: {}", client);
            //logger.error("Exception", e);
            logger.error("Failed to parse date value. {}", e.getMessage());
            //System.out.println("@@@@@@@@@@@@@@@@@@@@@" + e.getMessage());
            logger.info("Client {} was filtered out", client.getCustomerName());
            return false;
        }

        logger.info("Client {} was filtered out", client.getCustomerName());
        return false;
    }
}
