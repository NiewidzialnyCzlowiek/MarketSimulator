package io.bartlomiejszal.marketsimulator.dataManagement;

import io.bartlomiejszal.marketsimulator.model.assets.Asset;
import io.bartlomiejszal.marketsimulator.model.assets.CurrencyAsset;
import io.bartlomiejszal.marketsimulator.model.assets.Good;
import io.bartlomiejszal.marketsimulator.model.dictionaries.Country;
import io.bartlomiejszal.marketsimulator.model.dictionaries.Currency;
import io.bartlomiejszal.marketsimulator.model.dictionaries.TradeUnit;
import io.bartlomiejszal.marketsimulator.model.markets.*;
import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Company;
import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Customer;
import io.bartlomiejszal.marketsimulator.sampleData.SampleDataGenerator;
import io.bartlomiejszal.marketsimulator.setup.Setup;
import io.bartlomiejszal.marketsimulator.setup.UIDGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DataContext implements Serializable {
    private List<Asset> assets;
    private List<Market> markets;
    private List<Customer> customers;
    private List<Company> companies;
    private List<Country> countries;
    private List<Currency> currencies;
    private List<TradeUnit> tradeUnits;
    private transient Setup setup;
    private int lastUID;

    public void restoreLastUID() {
        UIDGenerator.setCurrentUID(lastUID);
    }

    public void backupLastUID() {
        this.lastUID = UIDGenerator.getUID();
    }

    public DataContext() {
        assets = Collections.synchronizedList(new ArrayList<>());
        markets = Collections.synchronizedList(new ArrayList<>());
        customers = Collections.synchronizedList(new ArrayList<>());
        companies = Collections.synchronizedList(new ArrayList<>());
        countries = Collections.synchronizedList(new ArrayList<>());
        currencies = Collections.synchronizedList(new ArrayList<>());
        tradeUnits = Collections.synchronizedList(new ArrayList<>());
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public List<Market> getMarkets() {
        return markets;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public List<TradeUnit> getTradeUnits() {
        return tradeUnits;
    }

    public Setup getSetup() {
        return setup;
    }

    public synchronized void setSetup(Setup setup) {
        this.setup = setup;
    }

    public void populateSampleData() {
        populateTradeUnits();
        populateCountries();
        populateCurrencies();
        assignCountriesToCurrencies();
        populateCustomers();
        populateCompanies();
        populateSharesForCompanies();
        populateMarketsAndAssets();
        Setup.setDefault(this);
    }

    public void createBlankDatabase() {
        populateTradeUnits();
        populateCurrencies();
        populateCountries();
        Setup.setDefault(this);
    }

    private void populateMarketsAndAssets() {
        populateGoodsMarkets();
        populateCurrencyMarkets();
        populateStockExchanges();
    }

    private void populateStockExchanges() {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            StockExchange stockExchange = SampleDataGenerator.createStockExchange(
                    countries.get(random.nextInt(countries.size())),
                    currencies.get(random.nextInt(currencies.size())));
            for (int x = 0; x < 3; x++) {
                Index index = new Index(UIDGenerator.getUID(), new ArrayList<>(), String.format("%s Index %d", stockExchange.getName(), x), 0, stockExchange);
                for (int y = 0; y < 3; y++) {
                    index.getCompanies().add(companies.get(random.nextInt(companies.size())));
                }
                stockExchange.addIndex(index);
            }
            markets.add(stockExchange);
        }
    }

    private void populateCurrencyMarkets() {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            CurrencyMarket currencyMarket = SampleDataGenerator.createCurrencyMarket(
                    countries.get(random.nextInt(countries.size())),
                    currencies.get(random.nextInt(currencies.size())));
            for (int x = 0; x < 5; x++) {
                CurrencyAsset currencyAsset = SampleDataGenerator.createCurrencyAsset(currencies.get(random.nextInt(currencies.size())));
                currencyMarket.getCurrencyAssets().add(currencyAsset);
                assets.add(currencyAsset);
            }
            markets.add(currencyMarket);
        }
    }

    private void populateGoodsMarkets() {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            GoodsMarket goodsMarket = SampleDataGenerator.createGoodsMarket(
                    countries.get(random.nextInt(countries.size())),
                    currencies.get(random.nextInt(currencies.size())));
            for (int x = 0; x < 5; x++) {
                Good good = SampleDataGenerator.createGood(tradeUnits.get(random.nextInt(tradeUnits.size())));
                goodsMarket.getGoods().add(good);
                assets.add(good);
            }
            markets.add(goodsMarket);
        }
    }

    private void populateSharesForCompanies() {
        Random random = new Random();
        for (Company rec: companies) {
            assets.add(SampleDataGenerator.createShare(rec));
        }
    }

    private void populateCompanies() {
        for (int i = 0; i < 20; i++) {
            companies.add(SampleDataGenerator.createCompany());
        }
    }

    private void populateCustomers() {
        for (int i = 0; i < 20; i++)
            customers.add(SampleDataGenerator.createInvestor());
        for (int i = 0; i < 10; i++)
            customers.add(SampleDataGenerator.createFund());
    }

    private void populateTradeUnits() {
        tradeUnits = SampleDataGenerator.getTradeUnits();
    }

    private void assignCountriesToCurrencies() {
        for (Currency curr: currencies) {
            List<Country> _countries = new ArrayList<>();
            Random random = new Random();
            int howMany = random.nextInt(4);
            for (int i = 0; i < howMany; i++) {
                _countries.add(countries.get(random.nextInt(countries.size())));
            }
            curr.setUsedInCountries(_countries);
        }
    }

    private void populateCountries() {
        for (int i = 0; i < 100; i++)
            countries.add(SampleDataGenerator.createCountry());
    }

    private void populateCurrencies() {
        for (int i = 0; i < 100; i++)
            currencies.add(SampleDataGenerator.createCurrency());
    }
}
