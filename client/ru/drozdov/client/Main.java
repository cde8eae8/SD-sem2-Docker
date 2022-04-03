package ru.drozdov.client;

import ru.drozdov.common.Company;
import ru.drozdov.common.Stock;
import ru.drozdov.market.Company;
import ru.drozdov.market.Stock;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class Main {
    static class Command {
        Command(Consumer<List<String>> runner, String description) {
            this.runner = runner;
            this.description = description;
        }

        final public Consumer<List<String>> runner;
        final public String description;
    }

    private static final Map<String, Command> commands = Map.of(
            "companies", new Command(Main::companies, "get information about companies"),
            "user", new Command(Main::info, "get information about user"),
            "buy", new Command(Main::buy, "buy something"),
            "sell",  new Command(Main::sell, "sell something"),
            "add-money",  new Command(Main::addMoney, "add money"),
            "help", new Command(Main::help, "print this text")
    );


    private static Client client;

    public static int main(String[] argv) {
        if (argv.length != 3) {
            usage();
            return 1;
        }
        IMarket market = null;
        if (Objects.equals(argv[1], "name")) {
            client = new Client(market, argv[2]);
        } else if (Objects.equals(argv[1], "id")) {
            client = new Client(market, Integer.parseInt(argv[2]));
        } else {
            usage();
            return 1;
        }
        return 0;
    }

    private static void help(List<String> args) {
        for (var command : commands.entrySet()) {
            System.out.printf("%s: %s", command.getKey(), command.getValue().description);
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
        System.out.printf("User %s\n", client.name());
        var stocks = client.stocks();
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
            System.out.println("Exepected args: stock amount");
        }
        String stock = args.get(0);
        int amount = Integer.parseInt(args.get(1));
        client.buy(stock, (buy ? 1 : -1) * amount);
    }

    private static void addMoney(List<String> args) {
        if (args.size() != 1) {
            System.out.println("Expected args: amount");
        }
        client.addMoney(Double.parseDouble(args.get(0)));
    }
}
