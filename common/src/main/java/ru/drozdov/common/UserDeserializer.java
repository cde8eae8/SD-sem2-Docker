package ru.drozdov.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class UserDeserializer extends StdDeserializer<User> {

    public UserDeserializer() {
        this(null);
    }

    public UserDeserializer(Class<User> t) {
        super(t);
    }

    @Override
    public User deserialize(
            JsonParser jsonparser, DeserializationContext context)
            throws IOException {
        var oc = jsonparser.getCodec();
        JsonNode node = oc.readTree(jsonparser);
        Stock[] stocks = new ObjectMapper().treeToValue(node.get("stocks"), Stock[].class);
        var numberOfStocks = Arrays.stream(stocks).collect(Collectors.toMap(s -> s.stock.name, s -> s));
        return new User(node.get("id").intValue(), node.get("name").textValue(), node.get("money").floatValue(), numberOfStocks);
    }
}

