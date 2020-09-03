package io.bartlomiejszal.marketsimulator.model.markets;


import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Index implements Serializable {
    private int id;
    private List<Company> companies;
    private String name;
    private double value;
    private StockExchange stockExchange;
    private boolean dynamicIndex = false;
    private int numberOfBestCompanies = 0;

    public Index(int id, List<Company> companies, String name, double value) {
        this.id = id;
        this.companies = companies;
        this.name = name;
        this.value = value;
    }

    public Index(int id, List<Company> companies, String name, double value, StockExchange stockExchange) {
        this.id = id;
        this.companies = companies;
        this.name = name;
        this.value = value;
        this.stockExchange = stockExchange;
    }

    public boolean isDynamicIndex() {
        return dynamicIndex;
    }

    public void setDynamicIndex(boolean dynamicIndex) {
        this.dynamicIndex = dynamicIndex;
    }

    public int getNumberOfBestCompanies() {
        return numberOfBestCompanies;
    }

    public void setNumberOfBestCompanies(int numberOfBestCompanies) {
        this.numberOfBestCompanies = numberOfBestCompanies;
    }

    public void setStockExchange(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }

    private void updateValue() {
        double newValue = 0;
        for(Company company : companies)
        {
            newValue += company.getCurrentSharePrice() * company.getShares();
        }
        value = newValue;
    }

    private void updateCompanies() {
        if (this.isDynamicIndex() && stockExchange != null) {
            List<Company> _companies = new ArrayList<>(stockExchange.getAllCompanies());
            _companies.sort(Comparator.comparing(Company::getAllSharesValue));
            if  (this.companies.size() > this.numberOfBestCompanies)
                this.companies = _companies.subList(0, this.numberOfBestCompanies);
            else
                this.companies = _companies;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public double getValue() {
        updateCompanies();
        updateValue();
        return this.value;
    }

    @Override
    public String toString() {
        return (this.isDynamicIndex() ? "Dynamic" : "Static") + this.getName() + ", Value: " + this.getValue();
    }
}
