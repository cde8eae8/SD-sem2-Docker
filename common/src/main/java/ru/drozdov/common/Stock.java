package ru.drozdov.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Objects;

@JsonSerialize(using = StockSerializer.class)
@JsonDeserialize(using = StockDeserializer.class)
public class Stock {
    public static class StockType {
        public StockType(String name, double price) {
            this.name = name;
            this.price = price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StockType stockType = (StockType) o;
            return Double.compare(stockType.price, price) == 0 && Objects.equals(name, stockType.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, price);
        }

        @Override
        public String

        toString() {
            return "StockType{" +
                    "name='" + name + '\'' +
                    ", price=" + price +
                    '}';
        }

        public final String name;
        public double price;
    }

    public Stock(String name, double price, int amount) {
        stock = new StockType(name, price);
        this.amount = amount;
    }

    public Stock(StockType stockType, int amount) {
        stock = stockType;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock1 = (Stock) o;
        return amount == stock1.amount && Objects.equals(stock, stock1.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stock, amount);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "stock=" + stock +
                ", amount=" + amount +
                '}';
    }

    public StockType stock;
    public int amount;
}

