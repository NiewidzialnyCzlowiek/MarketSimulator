package io.bartlomiejszal.marketsimulator.dataManagement.services;

import io.bartlomiejszal.marketsimulator.model.assets.Asset;
import io.bartlomiejszal.marketsimulator.model.assets.CurrencyAsset;
import io.bartlomiejszal.marketsimulator.model.assets.Good;
import io.bartlomiejszal.marketsimulator.model.assets.Share;
import io.bartlomiejszal.marketsimulator.model.markets.CurrencyMarket;
import io.bartlomiejszal.marketsimulator.model.markets.GoodsMarket;
import io.bartlomiejszal.marketsimulator.model.markets.Market;
import io.bartlomiejszal.marketsimulator.model.markets.StockExchange;
import io.bartlomiejszal.marketsimulator.dataManagement.DataContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MarketService {
    private DataContext _context;

    public MarketService(DataContext _context) {
        this._context = _context;
    }

    public List<Market> getMarkets() {
        return _context.getMarkets();
    }

    public Market getRandomMarket() {
        Random random = new Random();
        return _context.getMarkets().get(random.nextInt(_context.getMarkets().size()));
    }

    public Market getRandomMarket(Asset asset) {
        if (asset instanceof Good) {
            return getRandomGoodsMarket();
        }
        else if (asset instanceof CurrencyAsset) {
            return getRandomCurrencyMarket();
        }
        else if (asset instanceof Share) {
            return getRandomStockExchange();
        }
        else return null;
    }

    private Market getRandomCurrencyMarket() {
        Random random = new Random();
        List<CurrencyMarket> currencyMarkets = _context.getMarkets()
                .stream()
                .filter(rec -> rec instanceof CurrencyMarket)
                .map(rec -> (CurrencyMarket) rec)
                .collect(Collectors.toCollection(ArrayList::new));
        return currencyMarkets.get(random.nextInt(currencyMarkets.size()));
    }

    private Market getRandomGoodsMarket() {
        Random random = new Random();
        List<GoodsMarket> goodsMarkets = _context.getMarkets()
                .stream()
                .filter(rec -> rec instanceof GoodsMarket)
                .map(rec -> (GoodsMarket) rec)
                .collect(Collectors.toCollection(ArrayList::new));
        return goodsMarkets.get(random.nextInt(goodsMarkets.size()));
    }

    private Market getRandomStockExchange() {
        Random random = new Random();
        List<StockExchange> stockExchanges = _context.getMarkets()
                .stream()
                .filter(rec -> rec instanceof StockExchange)
                .map(rec -> (StockExchange) rec)
                .collect(Collectors.toCollection(ArrayList::new));
        return stockExchanges.get(random.nextInt(stockExchanges.size()));
    }

    public void addMarket(Market market) {
        _context.getMarkets().add(market);
    }
}
