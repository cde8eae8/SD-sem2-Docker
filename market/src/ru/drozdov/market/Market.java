package ru.drozdov.market;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.drozdov.common.Company;
import ru.drozdov.common.Stock;
import ru.drozdov.common.User;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class Market {
    Map<Integer, User> users = new HashMap<>();
    Map<Integer, Company> companies = new HashMap<>();
    Map<String, Company> companyByStock = new HashMap<>();
    Random random = new Random();
    int freeId = 0;

    @RequestMapping("/add-user")
    public User addUser(String name) {
        int id = freeId++;
        System.out.println("User added!");
        User user = new User(id, name);
        users.put(id, user);
        System.out.printf("%s %d %f", user.name, user.id, user.money);
        return user;
    }

    @RequestMapping("/add-company")
    public int addCompany(String name) {
        System.out.printf("Company added: %s\n", name);
        int id = freeId++;
        companies.put(id, new Company(id, name));
        return id;
    }

    @RequestMapping("/add-stock")
    public boolean addStock(int companyId, String name, int amount, double price) {
        if (companyByStock.containsKey(name)) {
            return false;
        }
        if (companies.containsKey(companyId)) {
            Company company = companies.get(companyId);
            if (company.numberOfStocks.containsKey(name)) {
                company.numberOfStocks.get(name).amount += amount;
                company.numberOfStocks.get(name).stock.price = price;
            } else {
                company.numberOfStocks.put(name, new Stock(name, price, amount));
            }
            companyByStock.put(name, company);
            System.out.printf("Stock added: company %s, name %s, amount %d (%d total), price %f\n",
                    company.name, name, amount,
                    company.numberOfStocks.get(name).amount,
                    company.numberOfStocks.get(name).stock.price);
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping("/set-price")
    public boolean setPrice(String name, double price) {
        if (!companyByStock.containsKey(name)) {
            System.out.println("");
            return false;
        }
        Company company = companyByStock.get(name);
        Stock stock = company.numberOfStocks.get(name);
        stock.stock.price = price;
        return true;
    }

    @RequestMapping("/buy")
    public boolean buy(int userId, String stockId, int amount) {
        if (!checkBuySell(userId, stockId)) { return false; }
        Company company = companyByStock.get(stockId);
        User user = users.get(userId);
        Stock stock = company.numberOfStocks.get(stockId);
        double price = stock.stock.price;
        if (user.money < price * amount) {
            return false;
        }
        if (!doBuySell(company.numberOfStocks, user.numberOfStocks, stock, amount)) {
            return false;
        }
        user.money -= price * amount;
        return true;
    }

    @RequestMapping("/sell")
    public boolean sell(int userId, String stockId, int amount) {
        if (!checkBuySell(userId, stockId)) { return false; }
        Company company = companyByStock.get(stockId);
        User user = users.get(userId);
        Stock stock = company.numberOfStocks.get(stockId);
        double price = stock.stock.price;
        if (!doBuySell(user.numberOfStocks, company.numberOfStocks, stock, amount)) {
            return false;
        }
        user.money += price * amount;
        return true;
    }

    private boolean checkBuySell(int userId, String stockId) {
        if (!users.containsKey(userId)) { return false; }
        if (!companyByStock.containsKey(stockId)) { return false; }
        Company company = companyByStock.get(stockId);
        return company.numberOfStocks.containsKey(stockId);
    }

    private boolean doBuySell(Map<String, Stock> from, Map<String, Stock> to, Stock stock, int amount) {
        if (!from.containsKey(stock.stock.name)) return false;
        if (from.get(stock.stock.name).amount < amount) {
            return false;
        }
        if (!to.containsKey(stock.stock.name)) {
            to.put(stock.stock.name, new Stock(stock.stock, 0));
        }
        to.get(stock.stock.name).amount += amount;
        from.get(stock.stock.name).amount -= amount;

        return true;
    }

    @RequestMapping("/list-companies")
    public List<Company> listCompanies() {
        return new ArrayList<>(companies.values());
    }

    @RequestMapping("/company-info")
    public Company companyInfo(int companyId) {
        return companies.getOrDefault(companyId, null);
    }

    @RequestMapping("/user-info")
    public User userInfo(int userId) {
        return users.getOrDefault(userId, null);
    }

    @RequestMapping("/add-money")
    public boolean userInfo(int userId, double money) {
        if (!users.containsKey(userId)) return false;
        users.get(userId).money += money;
        return true;
    }

    private double getPrice() {
        return random.nextInt(1000);
    }
}
