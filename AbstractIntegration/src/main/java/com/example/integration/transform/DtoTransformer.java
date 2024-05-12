package com.example.integration.transform;

import com.example.integration.dto.Results;
import org.apache.camel.Message;
import org.apache.camel.spi.DataType;
import org.apache.camel.spi.DataTypeTransformer;
import org.apache.camel.spi.Transformer;

@DataTypeTransformer(name = "dtoTransformer")
public class DtoTransformer extends Transformer {
    @Override
    public void transform(Message message, DataType from, DataType to) throws Exception {
        message.setBody(message.getBody(Results.class).getResults());
    }
}
