package ru.drozdov.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class UserSerializer extends StdSerializer<User> {

    public UserSerializer() {
        this(null);
    }

    public UserSerializer(Class<User> t) {
        super(t);
    }

    @Override
    public void serialize(
            User value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeStringField("name", value.name);
        jgen.writeNumberField("money", value.money);
        jgen.writeArrayFieldStart("stocks");
        for (Stock stock : value.numberOfStocks.values()) {
            jgen.writeObject(stock);
        }
        jgen.writeEndArray();
        jgen.writeEndObject();
    }
}
