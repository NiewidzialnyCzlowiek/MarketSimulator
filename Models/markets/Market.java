package Models.markets;

import Models.assets.Asset;
import Models.dictionaries.Country;
import Models.dictionaries.Currency;
import Models.usersAndCustomers.Customer;

import java.io.Serializable;

public abstract class Market implements Serializable {
    private int id;
    private String name;
    private Country country;
    private Currency currency;
    private String city;
    private String address;
    private double marginPercent;

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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getMarginPercent() {
        return marginPercent;
    }

    public void setMarginPercent(double marginPercent) {
        if (marginPercent > 1)
            marginPercent /= 100;
        this.marginPercent = marginPercent;
    }

    public abstract Asset getRandomAsset();

    public abstract void handlePurchaseTransaction(Customer customer, Asset asset, double transactionBudget);

    public abstract void handleSellTransaction(Customer customer, Asset asset, double quantity);

    protected double takeMarginFromTransaction(double transactionBudget) {
        return transactionBudget - (this.getMarginPercent() * transactionBudget);
    }
    @Override
    public String toString() {
        return this.getName() + ", " + this.getCountry().toString() + ", " + this.getCity() + ", " + this.getAddress() + "\n";
    }
}
