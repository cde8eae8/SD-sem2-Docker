package ru.drozdov.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CompanySerializer extends StdSerializer<Company> {

    public CompanySerializer() {
        this(null);
    }

    public CompanySerializer(Class<Company> t) {
        super(t);
    }

    @Override
    public void serialize(
            Company value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeStringField("name", value.name);
        jgen.writeArrayFieldStart("stocks");
        for (Stock stock : value.numberOfStocks.values()) {
            jgen.writeObject(stock);
        }
        jgen.writeEndArray();
        jgen.writeEndObject();
    }
}

