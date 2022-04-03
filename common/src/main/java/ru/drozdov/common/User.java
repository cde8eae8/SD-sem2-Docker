package ru.drozdov.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@JsonSerialize(using = UserSerializer.class)
@JsonDeserialize(using = UserDeserializer.class)
public class User {
    public User(int id, String name) {
        this(id, name, 0, new HashMap<>());
    }

    public User(int id, String name, double money, Map<String, Stock> numberOfStocks) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.numberOfStocks = new HashMap<>(numberOfStocks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Double.compare(user.money, money) == 0 && Objects.equals(name, user.name) && Objects.equals(numberOfStocks, user.numberOfStocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, money, numberOfStocks);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", money=" + money +
                ", numberOfStocks=" + numberOfStocks +
                '}';
    }

    public final int id;
    public String name;
    public double money;
    public Map<String, Stock> numberOfStocks;
}
