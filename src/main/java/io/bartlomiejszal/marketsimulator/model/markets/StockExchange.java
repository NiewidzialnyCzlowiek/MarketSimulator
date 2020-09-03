package io.bartlomiejszal.marketsimulator.model.markets;

import io.bartlomiejszal.marketsimulator.model.assets.Asset;
import io.bartlomiejszal.marketsimulator.model.assets.Share;
import io.bartlomiejszal.marketsimulator.model.dictionaries.Country;
import io.bartlomiejszal.marketsimulator.model.dictionaries.Currency;
import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Company;
import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Customer;
import io.bartlomiejszal.marketsimulator.interfaces.IHistoryTraceable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StockExchange extends Market {
    private List<Index> indexes;
    private List<Company> allCompanies;
    //private static final Object globalLock = new Object();

    public StockExchange(int id, String name, Country country, Currency currency, String city, String address, double marginPercent) {
        this.setId(id);
        this.setName(name);
        this.setCountry(country);
        this.setCurrency(currency);
        this.setCity(city);
        this.setAddress(address);
        this.setMarginPercent(marginPercent);
        this.indexes = new ArrayList<>();
        this.allCompanies = new ArrayList<>();
    }
    public StockExchange(int id, String name, Country country, Currency currency, String city, String address, double marginPercent, List<Index> indexes) {
        this.setId(id);
        this.setName(name);
        this.setCountry(country);
        this.setCurrency(currency);
        this.setCity(city);
        this.setAddress(address);
        this.setMarginPercent(marginPercent);
        this.indexes = indexes;
        this.allCompanies = new ArrayList<>();

        setAllCompanies(indexes);
    }

    private void setAllCompanies(List<Index> indexes) {
        for (Index index : indexes) {
            for (Company company : index.getCompanies()) {
                if (!allCompanies.contains(company))
                    allCompanies.add(company);
            }
        }
    }

    public List<Company> getAllCompanies() {
        return allCompanies;
    }

    public List<Index> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<Index> indexes) {
        this.indexes = indexes;
        setAllCompanies(indexes);
    }

    public  void addIndex(Index index) {
        indexes.add(index);
        addToAllCompanies(index);
    }

    private void addToAllCompanies(Index index) {
        for (Company company : index.getCompanies()) {
            if (!allCompanies.contains(company))
                allCompanies.add(company);
        }
    }

    @Override
    public Asset getRandomAsset() {
        Random random = new Random();
        Index index = this.getIndexes().get(random.nextInt(this.getIndexes().size()));
        Company company = index.getCompanies().get(random.nextInt(index.getCompanies().size()));
        return company.getShare();
    }

    @Override
    public void handlePurchaseTransaction(Customer customer, Asset asset, double transactionBudget) {
        Share share = (Share) asset;
            synchronized (share.getOwner()) {
                if (transactionBudget > 0) {
                    double sharesQuantity = share.getQuantityForAmount(takeMarginFromTransaction(transactionBudget));
                    if (sharesQuantity > 0) {
                        double sharesPrice = share.getPriceForQuantity(-1 * sharesQuantity);
                        sharesPrice += sharesPrice * this.getMarginPercent();
                        customer.setInvestmentBudget(customer.getInvestmentBudget() - sharesPrice);
                        share.removeQuantity(sharesQuantity);
                        share.getOwner().handlePurchaseTransaction(sharesQuantity, sharesPrice);
                        IHistoryTraceable historyTraceable = (IHistoryTraceable) customer;
                        historyTraceable.addHistoryEntry(new Transaction(this, customer, asset, sharesQuantity, sharesPrice, customer.getInvestmentBudget()));
                    }
                    customer.getPortfolio().addToPortfolio(asset, sharesQuantity);
                }
            }
    }

    @Override
    public void handleSellTransaction(Customer customer, Asset asset, double quantity) {
        Share share = (Share) asset;
        synchronized (share.getOwner()) {
            if (quantity > 0) {
                double sharesPrice = takeMarginFromTransaction(quantity * share.getOwner().getCurrentSharePrice());
                customer.setInvestmentBudget(customer.getInvestmentBudget() + sharesPrice);
                share.addQuantity(quantity);
                share.getOwner().handleSellTransaction(quantity, sharesPrice);
                IHistoryTraceable historyTraceable = (IHistoryTraceable) customer;
                historyTraceable.addHistoryEntry(new Transaction(this, customer, asset, -1 * quantity, sharesPrice, customer.getInvestmentBudget()));
            }
        }
    }
}
