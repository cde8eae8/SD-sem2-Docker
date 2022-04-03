package ru.drozdov.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

@JsonSerialize(using = StockSerializer.class)
public class Stock {
    public static class StockType {
        StockType(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public final String name;
        public double price;
    }

    public Stock(String name, double price, int amount) {
        stock = new StockType(name, price);
        this.amount = amount;
    }

    public Stock(StockType stockType) {
        stock = stockType;
        amount = 0;
    }

    public StockType stock;
    public long amount;
}

