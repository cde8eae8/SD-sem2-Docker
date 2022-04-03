package ru.drozdov.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class StockDeserializer extends StdDeserializer<Stock> {
    public StockDeserializer() {
        this(null);
    }

    public StockDeserializer(Class<Stock> t) {
        super(t);
    }

    @Override
    public Stock deserialize(
            JsonParser jsonparser, DeserializationContext context)
            throws IOException {
        var oc = jsonparser.getCodec();
        JsonNode node = oc.readTree(jsonparser);
        return new Stock(new Stock.StockType(node.get("name").textValue(), node.get("price").floatValue()),
                node.get("amount").intValue());
    }
}

