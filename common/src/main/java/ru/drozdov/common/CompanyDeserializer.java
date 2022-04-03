package ru.drozdov.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class CompanyDeserializer extends StdDeserializer<Company> {

    public CompanyDeserializer() {
        this(null);
    }

    public CompanyDeserializer(Class<Company> t) {
        super(t);
    }

    @Override
    public Company deserialize(
            JsonParser jsonparser, DeserializationContext context)
            throws IOException {
        var oc = jsonparser.getCodec();
        JsonNode node = oc.readTree(jsonparser);
        Stock[] stocks = new ObjectMapper().treeToValue(node.get("stocks"), Stock[].class);
        var numberOfStocks = Arrays.stream(stocks).collect(Collectors.toMap(s -> s.stock.name, s -> s));
        return new Company(node.get("id").intValue(), node.get("name").textValue(), numberOfStocks);
    }
}

