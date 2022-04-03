package ru.drozdov.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class StockSerializer extends StdSerializer<Stock> {

    public StockSerializer() {
        this(null);
    }

    public StockSerializer(Class<Stock> t) {
        super(t);
    }

    @Override
    public void serialize(
            Stock value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeStringField("name", value.stock.name);
        jgen.writeNumberField("price", value.stock.price);
        jgen.writeNumberField("amount", value.amount);
        jgen.writeEndObject();
    }
}