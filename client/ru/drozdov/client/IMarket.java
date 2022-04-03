package ru.drozdov.client;

import ru.drozdov.common.Company;
import ru.drozdov.common.User;

import java.util.List;

interface IMarket {
    class AdminToken {}

    User userInfo(int userId);
    boolean addMoney(int userId, double money);
    User addUser(String name);
    boolean buy(int userId, String stock, int amount);
    List<Company> getCompanies();

    int addCompany(AdminToken token, String name);
    boolean addStock(AdminToken token, int companyId, String name, int amount, double price);
    boolean setPrice(AdminToken token, String name, double price);
}
