package ru.drozdov.market;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.drozdov.common.Company;
import ru.drozdov.common.Stock;
import ru.drozdov.common.User;

import java.util.*;

@RestController
public class Market {
    Map<Integer, User> users = new HashMap<>();
    Map<Integer, Company> companies = new HashMap<>();
    Map<String, Company> companyByStock = new HashMap<>();
    Set<String> registeredStockNames = new HashSet<>();
    Random random = new Random();
    int freeId = 0;

    @RequestMapping("/add-user")
    public int addUser(String name) {
        int id = freeId++;
        users.put(id, new User(id, name));
        return id;
    }

    @RequestMapping("/add-company")
    public int addCompany(String name) {
        System.out.printf("Company added: %s\n", name);
        int id = freeId++;
        companies.put(id, new Company(id, name));
        return id;
    }

    @RequestMapping("/add-stock")
    public boolean addStock(int companyId, String name, int amount) {
        if (registeredStockNames.contains(name)) {
            return false;
        }
        if (companies.containsKey(companyId)) {
            Company company = companies.get(companyId);
            if (company.numberOfStocks.containsKey(name)) {
                company.numberOfStocks.get(name).amount += amount;
            } else {
                company.numberOfStocks.put(name, new Stock(name, getPrice(), amount));
            }
            System.out.printf("Stock added: company %s, name %s, amount %d (%d total), price %f\n",
                    company.name, name, amount,
                    company.numberOfStocks.get(name).amount,
                    company.numberOfStocks.get(name).stock.price);
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping("/buy")
    public boolean buy(int userId, String stockId, int amount) {
        return doBuySell(userId, stockId, amount);
    }

    @RequestMapping("/sell")
    public boolean sell(int userId, String stockId, int amount) {
        return doBuySell(userId, stockId, -amount);
    }

    @RequestMapping("/list-companies")
    public Map<Integer, Company> listCompanies() {
        return companies;
    }

    @RequestMapping("/company-info")
    public Company companyInfo(int companyId) {
        return companies.getOrDefault(companyId, null);
    }

    @RequestMapping("/user-info")
    public User userInfo(int userId) {
        return users.getOrDefault(userId, null);
    }

    private boolean doBuySell(int userId, String stockId, int amount) {
        if (!users.containsKey(userId)) return false;
        if (!companyByStock.containsKey(stockId)) return false;
        Company company = companyByStock.get(stockId);
        User user = users.get(userId);
        if (!company.numberOfStocks.containsKey(stockId)) return false;
        Stock stock = company.numberOfStocks.get(stockId);
        if (stock.amount < amount) return false;
        double price = stock.stock.price;
        if (user.money < price * amount) return false;
        if (!user.numberOfStocks.containsKey(stockId)) {
            user.numberOfStocks.put(stockId, new Stock(stock.stock));
        }
        user.numberOfStocks.get(stockId).amount += amount;
        stock.amount -= amount;
        user.money -= price * amount;
        return true;
    }

    private double getPrice() {
        return random.nextInt(1000);
    }
}
