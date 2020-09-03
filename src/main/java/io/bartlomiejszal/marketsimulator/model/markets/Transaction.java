package io.bartlomiejszal.marketsimulator.model.markets;

import io.bartlomiejszal.marketsimulator.model.assets.Asset;
import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Customer;

import java.io.Serializable;

public class Transaction implements Serializable {
    private Market market;
    private Customer customer;
    private Asset asset;
    private Double quantity;
    private Double amount;
    private Double balanceAfterTransaction;

    public Transaction(Market market, Customer customer, Asset asset, Double quantity, Double amount, Double balanceAfterTransaction) {
        this.market = market;
        this.customer = customer;
        this.asset = asset;
        this.quantity = quantity;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    public Market getMarket() {
        return market;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Asset getAsset() {
        return asset;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }
}
