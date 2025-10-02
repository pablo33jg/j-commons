package com.pj.commons.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomSTPDateDeserializer extends JsonDeserializer<LocalDate> {

    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        var formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(jsonParser.getText(), formatter);
    }
}
