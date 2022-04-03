package ru.drozdov.client;

import ru.drozdov.common.Company;
import ru.drozdov.common.Stock;
import ru.drozdov.common.User;

import java.util.*;
import java.util.function.Consumer;

public class Main {
    private static final Map<String, Command> commands = Map.of(
            "companies", new Command(Main::companies, "get information about companies"),
            "user", new Command(Main::info, "get information about user"),
            "buy", new Command(Main::buy, "buy something"),
            "sell",  new Command(Main::sell, "sell something"),
            "add-money",  new Command(Main::addMoney, "add money")
    );

    private static Client client;

    public static void main(String[] argv) {
        if (argv.length != 2) {
            usage();
            return;
        }
        IMarket market = new Market();
        if (Objects.equals(argv[0], "name")) {
            client = new Client(market, argv[1]);
        } else if (Objects.equals(argv[0], "id")) {
            client = new Client(market, Integer.parseInt(argv[1]));
        } else {
            usage();
            return;
        }

        new Loop(commands).loop();
    }

    private static void help(List<String> args) {
        for (var command : commands.entrySet()) {
            System.out.printf("%s: %s\n", command.getKey(), command.getValue().description);
        }
    }

    private static void companies(List<String> args) {
        System.out.print("Companies list:\n");
        List<Company> companiesList = client.companies();
        for (Company company : companiesList) {
            System.out.printf("%s\n", company.name);
            for (Stock stock : company.numberOfStocks.values()) {
                System.out.printf("\t[%s]: amount=%d, price=%f\n", stock.stock.name, stock.amount, stock.stock.price);
            }
        }
    }

    private static void usage() {
        System.out.println("Expected user id or user name");
    }

    private static void info(List<String> args) {
        User user = client.userInfo();
        System.out.printf("User %s, %f$, %f$ total\n", client.name(), user.money, client.total());
        var stocks = new ArrayList<>(user.numberOfStocks.values());
        for (Stock stock : stocks) {
            System.out.printf("\t[%s]: amount=%d, price=%f\n", stock.stock.name, stock.amount, stock.stock.price);
        }
    }

    private static void buy(List<String> args) {
        doSellBuy(true, args);
    }

    private static void sell(List<String> args) {
        doSellBuy(false, args);
    }

    private static void doSellBuy(boolean buy, List<String> args) {
        if (args.size() != 2) {
            System.out.println("Expected args: stock amount");
            return;
        }
        String stock = args.get(0);
        int amount = Integer.parseInt(args.get(1));
        boolean ok = client.buy(stock, (buy ? 1 : -1) * amount);
        System.out.println(ok ? "OK" : "FAILED");
    }

    private static void addMoney(List<String> args) {
        if (args.size() != 1) {
            System.out.println("Expected args: amount");
            return;
        }
        boolean ok = client.addMoney(Double.parseDouble(args.get(0)));
        System.out.println(ok ? "OK" : "FAILED");
    }
}
