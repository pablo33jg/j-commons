package com.pj.commons.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class CustomJsonDateTimeWtzSerializer extends JsonSerializer<OffsetDateTime> {

    @Override
    public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss XXX");
        OffsetDateTime local = value.withOffsetSameInstant(ZoneOffset.of("-06:00"));
        String formattedLocalDate = local.format(formatter);
        gen.writeString(formattedLocalDate);
    }
}
