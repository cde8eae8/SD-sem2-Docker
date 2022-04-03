package ru.drozdov.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(using = UserSerializer.class)
public class User {
    public User(int id, String name) {
        this.id = id;
        this.name = name;
        money = 0;
        numberOfStocks = new HashMap<>();
    }

    public final int id;
    public String name;
    public double money;
    public Map<String, Stock> numberOfStocks;
}
