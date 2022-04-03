package ru.drozdov.client;

import ru.drozdov.common.Company;
import ru.drozdov.common.Stock;

import java.util.*;
import java.util.function.Consumer;

public class AdminMain {
    private static final Map<String, Command> commands = Map.of(
            "companies", new Command(AdminMain::companies, "get information about companies"),
            "add-company", new Command(AdminMain::addCompany, "add new company"),
            "add-stock", new Command(AdminMain::addStock, "add new stock"),
            "set-price",  new Command(AdminMain::setPrice, "set stock price")
    );

    static IMarket market = new Market();

    public static void main(String[] argv) {
        new Loop(commands).loop();
    }

    private static void companies(List<String> args) {
        System.out.print("Companies list:\n");
        List<Company> companiesList = market.getCompanies();
        for (Company company : companiesList) {
            System.out.printf("%s\n", company.name);
            for (Stock stock : company.numberOfStocks.values()) {
                System.out.printf("\t[%s]: amount=%d, price=%f\n", stock.stock.name, stock.amount, stock.stock.price);
            }
        }
    }

    private static void addStock(List<String> args) {
        if (args.size() != 4) {
            System.out.println("Expected args: companyId name price amount");
            return;
        }
        boolean ok = market.addStock(new IMarket.AdminToken(),
                Integer.parseInt(args.get(0)),
                args.get(1),
                Integer.parseInt(args.get(3)),
                Double.parseDouble(args.get(2)));
        System.out.println(ok ? "OK" : "FAILED");
    }

    private static void addCompany(List<String> args) {
        if (args.size() != 1) {
            System.out.println("Expected args: name");
            return;
        }
        int id = market.addCompany(new IMarket.AdminToken(), args.get(0));
        System.out.printf("Company added, id: %d\n", id);
    }

    private static void setPrice(List<String> args) {
        if (args.size() != 2) {
            System.out.println("Expected args: stock price");
            return;
        }
        boolean ok = market.setPrice(new IMarket.AdminToken(), args.get(0), Double.parseDouble(args.get(1)));
        System.out.println(ok ? "OK" : "FAILED");
    }
}
