package ru.drozdov.client;

import ru.drozdov.common.Company;
import ru.drozdov.common.Stock;
import ru.drozdov.common.User;

import java.util.List;

public class Client {
    private final IMarket market;
    User user;

    Client(IMarket market, int id) {
        this.market = market;
        User user = market.userInfo(id);
        if (user == null) {
            // throw
        }
        this.user = user;
    }

    Client(IMarket market, String name) {
        this.market = market;
        User user = market.addUser(name);
        if (user == null) {
            // throw
        }
        this.user = user;
    }

    String name() {
        return this.user.name;
    }

    List<Stock> stocks() {
        return List.copyOf(market
                .userInfo(user.id)
                .numberOfStocks
                .values());
    }

    List<Company> companies() {
        return List.copyOf(market
                .getCompanies());
    }

    double total() {
        return market.userInfo(user.id).numberOfStocks.values().stream()
                .reduce(0.0, (l, r) -> l + r.amount * r.stock.price, Double::sum);
    }

    boolean buy(String stock, int amount) {
        return market.buy(user.id, stock, amount);
    }

    void addMoney(double amount) {
        market.addMoney(user.id, amount);
    }
}
