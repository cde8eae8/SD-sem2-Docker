package ru.drozdov.client;

import ru.drozdov.common.Company;
import ru.drozdov.common.User;

import java.util.List;

public class Client {
    private final IMarket market;
    User user;

    Client(IMarket market, int id) {
        this.market = market;
        this.user = market.userInfo(id);
    }

    Client(IMarket market, String name) {
        this.market = market;
        this.user = market.addUser(name);
    }

    String name() {
        return this.user.name;
    }

    User userInfo() {
        return market.userInfo(user.id);
    }

    List<Company> companies() {
        return List.copyOf(market
                .getCompanies());
    }

    double total() {
        User u = market.userInfo(user.id);
        return u.money + u.numberOfStocks.values().stream()
                .reduce(0.0, (l, r) -> l + r.amount * r.stock.price, Double::sum);
    }

    boolean buy(String stock, int amount) {
        return market.buy(user.id, stock, amount);
    }

    boolean addMoney(double amount) {
        return market.addMoney(user.id, amount);
    }
}
