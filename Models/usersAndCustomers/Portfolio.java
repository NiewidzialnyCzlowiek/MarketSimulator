package Models.usersAndCustomers;

import Models.assets.Asset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Portfolio implements Serializable {
    private Map<Asset, Double> assets = new HashMap<>();
    private Map<Fund, Double> funds = new HashMap<>();

    public Map<Asset, Double> getAssets() {
        return assets;
    }

    public Map<Fund, Double> getFunds() {
        return funds;
    }

    public synchronized void addToPortfolio(Asset asset, double assetQuantity) {
        if (assets.containsKey(asset)) {
            assets.replace(asset, assets.get(asset) + assetQuantity);
        }
        else {
            assets.put(asset, assetQuantity);
        }
    }

    public synchronized void addToPortfolio(Fund fund, double amount) {
        if (funds.containsKey(fund)) {
            funds.replace(fund, funds.get(fund) + amount);
        }
        else {
            funds.put(fund, amount);
        }
    }

    public Asset getRandomAsset() {
        Random random = new Random();
        if (assets.size() > 0) {
            return new ArrayList<>(assets.keySet()).get(random.nextInt(assets.size()));
        }
        return null;
    }

    public double getRandomQuantity(Asset asset) {
        if (assets.containsKey(asset)) {
            Random random = new Random();
            Double quantity = assets.get(asset) * (double)(random.nextInt(40) + 10)/100;
            return quantity.intValue();
        }
        else
            return 0.0;
    }

    public synchronized double removeFromPortfolio(Asset asset) {
        double quantity = getAssetQuantity(asset);
        if (assets.containsKey(asset))
            assets.remove(asset);
        return quantity;
    }

    public double getAssetQuantity(Asset asset) {
        if (assets.containsKey(asset))
            return assets.get(asset);
        else
            return 0;
    }
}
