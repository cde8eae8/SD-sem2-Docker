package ru.drozdov.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(using = CompanySerializer.class)
public class Company {
    public Company(int id, String name) {
        this.id = id;
        this.name = name;
        numberOfStocks = new HashMap<>();
    }

    public final int id;
    public final String name;
    public final Map<String, Stock> numberOfStocks;
}
