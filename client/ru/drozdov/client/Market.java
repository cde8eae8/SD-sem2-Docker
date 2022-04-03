package ru.drozdov.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.shaded.com.google.common.collect.Multimap;
import ru.drozdov.common.Company;
import ru.drozdov.common.User;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class Market implements IMarket {

    @Override
    public User userInfo(int userId) {
        var params = new LinkedMultiValueMap<String, String>();
        params.put("userId", List.of(Integer.toString(userId)));
        return request("user-info", params, User.class);
    }

    @Override
    public boolean addMoney(int userId, double money) {
        var params = new LinkedMultiValueMap<String, String>();
        params.put("userId", List.of(Integer.toString(userId)));
        params.put("money", List.of(Double.toString(money)));
        return request("add-money", params, Boolean.class);
    }

    @Override
    public User addUser(String name) {
        var params = new LinkedMultiValueMap<String, String>();
        params.put("name", List.of(name));
        return request("add-user", params, User.class);
    }

    @Override
    public boolean buy(int userId, String stock, int amount) {
        boolean sell = amount < 0;
        amount = Math.abs(amount);
        var params = new LinkedMultiValueMap<String, String>();
        params.put("userId", List.of(Integer.toString(userId)));
        params.put("stockId", List.of(stock));
        params.put("amount", List.of(Integer.toString(amount)));
        System.out.printf("buy %d %s %d\n", userId, stock, amount);
        return request(sell ? "sell" : "buy", params, Boolean.class);
    }

    @Override
    public List<Company> getCompanies() {
        var params = new LinkedMultiValueMap<String, String>();
        Company[] companies = request("list-companies", params, Company[].class);
        return Arrays.asList(companies);
    }

    @Override
    public int addCompany(AdminToken token, String name) {
        var params = new LinkedMultiValueMap<String, String>();
        params.put("name", List.of(name));
        return request("add-company", params, Integer.class);
    }

    @Override
    public boolean addStock(AdminToken token, int companyId, String name, int amount, double price) {
        var params = new LinkedMultiValueMap<String, String>();
        params.put("companyId", List.of(Integer.toString(companyId)));
        params.put("name", List.of(name));
        params.put("amount", List.of(Integer.toString(amount)));
        params.put("price", List.of(Double.toString(price)));
        return request("add-stock", params, Boolean.class);
    }

    @Override
    public boolean setPrice(AdminToken token, String name, double price) {
        var params = new LinkedMultiValueMap<String, String>();
        params.put("name", List.of(name));
        params.put("price", List.of(Double.toString(price)));
        return request("set-price", params, Boolean.class);
    }

    private <T> T request(String path, MultiValueMap<String, String> params, Class<T> clazz) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(UriComponentsBuilder.fromUriString("http://localhost:8080/" + path)
                            .queryParams(params)
                            .toUriString()))
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
