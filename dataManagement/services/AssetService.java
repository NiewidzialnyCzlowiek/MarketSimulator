package dataManagement.services;

import Models.assets.Asset;
import Models.assets.CurrencyAsset;
import Models.assets.Good;
import Models.dictionaries.Country;
import Models.dictionaries.Currency;
import Models.dictionaries.TradeUnit;
import Models.markets.CurrencyMarket;
import Models.markets.GoodsMarket;
import dataManagement.DataContext;
import sampleData.SampleDataGenerator;
import setup.Setup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AssetService {
    private DataContext _context;

    public AssetService(DataContext dataContext) {
        this._context = dataContext;
    }

    public void addAsset(Asset asset) {
        _context.getAssets().add(asset);
        while ((double)_context.getAssets().size()/_context.getCustomers().size() > Setup.getAssetsPerCustomer())
            createRandomCustomer();
    }

    private void createRandomCustomer() {
        Random random = new Random();
        if (random.nextDouble() > 0.5)
            _context.getCustomers().add(SampleDataGenerator.createInvestor());
        else
            _context.getCustomers().add(SampleDataGenerator.createFund());
    }

    public void deleteAsset(Asset asset) {
        _context.getAssets().remove(asset);
        if (asset instanceof Good)
            deleteGoodFromAllMarkets((Good) asset);
        else if (asset instanceof CurrencyAsset)
            deleteCurrencyAssetsFromAllMarkets((CurrencyAsset) asset);
    }

    public List<TradeUnit> getTradeUnits() {
        return _context.getTradeUnits();
    }

    public List<Asset> getAssets() {
        return _context.getAssets();
    }

    private void deleteCurrencyAssetsFromAllMarkets(CurrencyAsset asset) {
        _context.getMarkets()
                .forEach(rec -> {
                    if (rec instanceof CurrencyMarket)
                        ((CurrencyMarket) rec).getCurrencyAssets().remove(asset);
                });
    }

    private void deleteGoodFromAllMarkets(Good asset) {
        _context.getMarkets()
                .forEach(rec -> {
                    if (rec instanceof GoodsMarket) {
                        ((GoodsMarket) rec).getGoods().remove(asset);
                    }
                });
    }

    public List<Currency> getCurrencies() {
        return _context.getCurrencies();
    }

    public List<Country> getCountries() {
        return _context.getCountries();
    }

    public List<Good> getGoods() {
        return _context.getAssets()
                .stream()
                .filter(rec -> rec instanceof Good)
                .map(rec -> (Good) rec)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<CurrencyAsset> getCurrencyAssets() {
        return _context.getAssets()
                .stream()
                .filter(rec -> rec instanceof CurrencyAsset)
                .map(rec -> (CurrencyAsset) rec)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
