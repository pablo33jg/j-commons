package com.pj.commons.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomJsonIsoInstantDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser,
                                 DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        //String date = jsonParser.getText().replace("T", " ");
        return LocalDateTime.parse(jsonParser.getText(), formatter);
    }
}
