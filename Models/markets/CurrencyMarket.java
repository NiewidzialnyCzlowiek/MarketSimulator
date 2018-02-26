package Models.markets;

import Models.assets.Asset;
import Models.assets.CurrencyAsset;
import Models.assets.Good;
import Models.dictionaries.Country;
import Models.dictionaries.Currency;
import Models.usersAndCustomers.Customer;
import interfaces.IHistoryTraceable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CurrencyMarket extends Market {
    private List<CurrencyAsset> currencyAssets;
//    private static final Object globalLock = new Object();

    public CurrencyMarket(int id, String name, Country country, Currency currency, String city, String address, double marginPercent) {
        this.setId(id);
        this.setName(name);
        this.setCountry(country);
        this.setCurrency(currency);
        this.setCity(city);
        this.setAddress(address);
        this.setMarginPercent(marginPercent);
        this.currencyAssets = new ArrayList<>();
    }
    public CurrencyMarket(int id, String name, Country country, Currency currency, String city, String address, double marginPercent, List<CurrencyAsset> currencyAssets) {
        this.setId(id);
        this.setName(name);
        this.setCountry(country);
        this.setCurrency(currency);
        this.setCity(city);
        this.setAddress(address);
        this.setMarginPercent(marginPercent);
        this.currencyAssets = currencyAssets;
    }

    public List<CurrencyAsset> getCurrencyAssets() {
        return currencyAssets;
    }

    public void setCurrencyAssets(List<CurrencyAsset> currencyAssets) {
        this.currencyAssets = currencyAssets;
    }

    @Override
    public Asset getRandomAsset() {
        Random random = new Random();
        return this.getCurrencyAssets().get(random.nextInt(this.getCurrencyAssets().size()));
    }

    @Override
    public void handlePurchaseTransaction(Customer customer, Asset asset, double transactionBudget) {
        synchronized (asset) {
            if (transactionBudget > 0) {
                double assetQuantity;
                assetQuantity = asset.getQuantityForAmount(takeMarginFromTransaction(transactionBudget));
                if (assetQuantity > 0) {
                    double assetPrice = asset.getPriceForQuantity(-1 * assetQuantity);
                    assetPrice += assetPrice * this.getMarginPercent();
                    customer.setInvestmentBudget(customer.getInvestmentBudget() - assetPrice);
                    asset.removeQuantity(assetQuantity);
                    IHistoryTraceable historyTraceable = (IHistoryTraceable) customer;
                    historyTraceable.addHistoryEntry(new Transaction(this, customer, asset, assetQuantity, assetPrice, customer.getInvestmentBudget()));
                }
                customer.getPortfolio().addToPortfolio(asset, assetQuantity);
            }
        }
    }

    @Override
    public void handleSellTransaction(Customer customer, Asset asset, double quantity) {
        synchronized (asset) {
            if (quantity > 0 && asset instanceof Good) {
                double price = asset.getPriceForQuantity(quantity);
                asset.addQuantity(quantity);
                customer.setInvestmentBudget(customer.getInvestmentBudget() + takeMarginFromTransaction(price));
                customer.getPortfolio().addToPortfolio(asset, -1 * quantity);
                IHistoryTraceable historyTraceable = (IHistoryTraceable) customer;
                historyTraceable.addHistoryEntry(new Transaction(this, customer, asset, -1 * quantity, takeMarginFromTransaction(price), customer.getInvestmentBudget()));
            }
        }
    }
}
