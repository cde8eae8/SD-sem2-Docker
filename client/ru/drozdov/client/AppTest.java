package ru.drozdov.client;

import org.junit.*;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import ru.drozdov.common.Company;
import ru.drozdov.common.Stock;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

class CompanyInfo {
    CompanyInfo(String name, Set<Stock> stock) {
        this.companyName = name;
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "CompanyInfo{" +
                "companyName='" + companyName + '\'' +
                ", stock=" + stock +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyInfo that = (CompanyInfo) o;
        return Objects.equals(companyName, that.companyName) && Objects.equals(stock, that.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName, stock);
    }

    String companyName;
    Set<Stock> stock;
}

public class AppTest {
    static public Set<CompanyInfo> companies =
            Set.of(
                new CompanyInfo("company", Set.of(
                        new Stock(new Stock.StockType("s1", 100), 100),
                        new Stock(new Stock.StockType("s2", 200), 500))),
                new CompanyInfo("company2", Set.of(
                        new Stock(new Stock.StockType("s2.1", 300), 600),
                        new Stock(new Stock.StockType("s2.2", 400), 700))));

    @ClassRule
    public static GenericContainer<?> simpleWebServer;

    @Before
    public void init() {
        simpleWebServer = new FixedHostPortGenericContainer("market:1.0-SNAPSHOT")
                .withFixedExposedPort(8080, 8080)
                .withExposedPorts(8080);
        simpleWebServer.start();
    }

    @After
    public void deinit() {
        simpleWebServer.stop();
        simpleWebServer = null;
    }


    @Test
    public void defaultUserInfo() throws Exception {
        Client client = setUpServer("User", new Market(), companies);
        Assert.assertEquals(client.name(), "User");
        Assert.assertEquals(new ArrayList<>(client.userInfo().numberOfStocks.values()), List.of());
    }

    @Test
    public void cannotBuyWhenNoMoney() throws Exception {
        Client client = setUpServer("User", new Market(), companies);
        Assert.assertFalse(client.buy("s1", 1));
    }

    @Test
    public void canBuyWhenEnoughMoney() throws Exception {
        Client client = setUpServer("User", new Market(), companies);
        client.addMoney(1000);
        Assert.assertEquals(1000, client.userInfo().money, 0.0001);
        Assert.assertTrue(client.buy("s1", 9));
        Assert.assertEquals(100, client.userInfo().money, 0.0001);
        Assert.assertEquals(new ArrayList<>(client.userInfo().numberOfStocks.values()),
                List.of(new Stock(new Stock.StockType("s1", 100), 9)));
    }

    @Test
    public void cannotBuyNonexistingStock() throws Exception {
        Client client = setUpServer("User", new Market(), companies);
        client.addMoney(1000);
        Assert.assertEquals(1000, client.userInfo().money, 0.0001);
        Assert.assertFalse(client.buy("1234567890", 9));
    }

    @Test
    public void selling() throws Exception {
        var market = new Market();
        Client client = setUpServer("User", market, companies);
        client.addMoney(1000);
        Assert.assertEquals(1000, client.userInfo().money, 0.0001);
        Assert.assertTrue(client.buy("s1", 9));
        Assert.assertTrue(market.setPrice(new IMarket.AdminToken(), "s1", 200));
        Assert.assertTrue(client.buy("s1", -9));
        Assert.assertEquals(1900, client.userInfo().money, 0.0001);
    }

    @Test
    public void state() throws Exception {
        var market = new Market();
        Client client = setUpServer("User", market, companies);
        int money = 100000;
        client.addMoney(money);
        Assert.assertTrue(client.buy("s1", 9));
        Assert.assertTrue(client.buy("s2", 9));
        Assert.assertTrue(client.buy("s2.1", 9));
        Assert.assertTrue(client.buy("s2.2", 9));
        money -= 9 * (100 + 200 + 300 + 400);
        Assert.assertEquals(
                client.userInfo().numberOfStocks
                        .values()
                        .stream()
                        .collect(Collectors.toMap(s -> s.stock.name, s -> s.amount)),
                Map.of("s1", 9,
                        "s2", 9,
                        "s2.1", 9,
                        "s2.2", 9)
        );

        market.setPrice(new IMarket.AdminToken(), "s1", 10);
        market.setPrice(new IMarket.AdminToken(), "s2", 20);
        market.setPrice(new IMarket.AdminToken(), "s2.1", 30);
        market.setPrice(new IMarket.AdminToken(), "s2.2", 40);

        Assert.assertEquals(
                client.userInfo().numberOfStocks
                        .values()
                        .stream()
                        .collect(Collectors.toMap(s -> s.stock.name, s -> s.stock.price)),
                Map.of("s1", 10.0,
                        "s2", 20.0,
                        "s2.1", 30.0,
                        "s2.2", 40.0)
        );

        Assert.assertEquals(
                9 * (10 + 20 + 30 + 40) + money,
                client.total(),
                0.001
        );
    }

    private Client setUpServer(String name, IMarket market, Set<CompanyInfo> companies) {
        IMarket.AdminToken token = new IMarket.AdminToken();

        for (CompanyInfo c : companies) {
            int id = market.addCompany(token, c.companyName);
            for (Stock s : c.stock) {
                System.out.printf("Added %s:%s, price=%f, amount=%d\n", c.companyName, s.stock.name, s.stock.price, s.amount);
                assert(market.addStock(token, id, s.stock.name, s.amount, s.stock.price));
            }
        }
        return new Client(market, name);
    }
}
