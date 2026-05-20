package com.pj.commons.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class CustomJsonDateTimeWtzDeserializer extends JsonDeserializer<OffsetDateTime> {

    @Override
    public OffsetDateTime deserialize(JsonParser jsonParser,
                                     DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss XXX");
        String date = jsonParser.getText();
        date = date.replace("GMT", "");
        return OffsetDateTime.parse(date, formatter);
    }
}
