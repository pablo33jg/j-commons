package com.pj.commons.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CustomSTPTimeSerializer extends JsonSerializer<LocalTime> {

    @Override
    public void serialize(LocalTime localTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
        String formattedLocalDate = localTime.format(formatter);
        jsonGenerator.writeString(formattedLocalDate);
    }
}
