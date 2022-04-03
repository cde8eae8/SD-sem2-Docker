package ru.drozdov.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@JsonSerialize(using = CompanySerializer.class)
@JsonDeserialize(using = CompanyDeserializer.class)
public class Company {
    public Company(int id, String name) {
        this.id = id;
        this.name = name;
        numberOfStocks = new HashMap<>();
    }

    public Company(int id, String name, Map<String, Stock> numberOfStocks) {
        this.id = id;
        this.name = name;
        this.numberOfStocks = numberOfStocks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return id == company.id && Objects.equals(name, company.name) && Objects.equals(numberOfStocks, company.numberOfStocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, numberOfStocks);
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numberOfStocks=" + numberOfStocks +
                '}';
    }

    public final int id;
    public final String name;
    public final Map<String, Stock> numberOfStocks;
}
