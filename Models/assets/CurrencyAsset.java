package Models.assets;

import Models.dictionaries.Currency;
import controllers.MainPageController;
import dataManagement.services.AssetService;
import interfaces.IPresentable;
import javafx.util.Pair;
import setup.Setup;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CurrencyAsset extends Asset implements IPresentable {
    private Currency currency;
    private double baseCurrencySellFactor;
    private double baseCurrencyPurchaseFactor;

    private List<Double> sellFactorHistory;
    private List<Double> purchaseFactorHistory;

    public CurrencyAsset(int id, String name, Currency currency, double baseCurrencySellFactor, double baseCurrencyPurchaseFactor) {
        this.setId(id);
        this.setName(name);
        this.currency = currency;
        this.baseCurrencySellFactor = baseCurrencySellFactor;
        this.baseCurrencyPurchaseFactor = baseCurrencyPurchaseFactor;

        sellFactorHistory = new ArrayList<>();
        purchaseFactorHistory = new ArrayList<>();
        Random random = new Random();
        this.setQuantity(100000 + random.nextInt(100000));
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getBaseCurrencySellFactor() {
        return baseCurrencySellFactor;
    }

    public void setBaseCurrencySellFactor(double baseCurrencySellFactor) {
        this.baseCurrencySellFactor = baseCurrencySellFactor;
        recordValueInHistory();
    }

    public double getBaseCurrencyPurchaseFactor() {
        return baseCurrencyPurchaseFactor;
    }

    public void setBaseCurrencyPurchaseFactor(double baseCurrencyPurchaseFactor) {
        this.baseCurrencyPurchaseFactor = baseCurrencyPurchaseFactor;
        recordValueInHistory();
    }

    @Override
    public String getTypeName() { return "Currency"; }

    @Override
    public void recordValueInHistory() {
        sellFactorHistory.add(this.getBaseCurrencySellFactor());
        purchaseFactorHistory.add(this.getBaseCurrencyPurchaseFactor());
        if (this.sellFactorHistory.size() > Setup.getNoOfHistoryRecords())
            this.sellFactorHistory.remove(0);
        if (this.purchaseFactorHistory.size() > Setup.getNoOfHistoryRecords())
            this.purchaseFactorHistory.remove(0);
    }

    @Override
    public void addQuantity(double assetQuantity) {
        fluctuateValue(assetQuantity);
        this.setQuantity(this.getQuantity() + assetQuantity);
    }

    @Override
    public void removeQuantity(double assetQuantity) {
        fluctuateValue(-1 * assetQuantity);
        this.setQuantity(this.getQuantity() - assetQuantity);
    }

    private void fluctuateValue(double assetQuantity) {
        double maxFluctuation, fluctuation;
        if (assetQuantity > 0) {
            maxFluctuation = 0.3 * this.getBaseCurrencySellFactor();
            fluctuation = (maxFluctuation * -1 * assetQuantity / this.getQuantity());
            this.setBaseCurrencySellFactor(this.getBaseCurrencySellFactor() + fluctuation);
            this.setBaseCurrencyPurchaseFactor(this.getBaseCurrencyPurchaseFactor() + 0.9 * fluctuation);
        }
        else {
            maxFluctuation = 0.3 * this.getBaseCurrencyPurchaseFactor();
            fluctuation = (maxFluctuation * -1 * assetQuantity / this.getQuantity());
            this.setBaseCurrencyPurchaseFactor(this.getBaseCurrencyPurchaseFactor() + fluctuation);
            this.setBaseCurrencySellFactor(this.getBaseCurrencySellFactor() + 0.9 * fluctuation);
        }

    }

    @Override
    public double getQuantityForAmount(double transactionBudget) {
        double quantityForAmount = transactionBudget/this.getBaseCurrencyPurchaseFactor();
        if (this.getQuantity() < quantityForAmount)
            quantityForAmount = this.getQuantity();
        return quantityForAmount;
    }

    @Override
    public double getPriceForQuantity(double assetQuantity) {
        if (assetQuantity < 0)
            return this.getBaseCurrencyPurchaseFactor() * assetQuantity;
        else
            return this.getBaseCurrencySellFactor() * assetQuantity;
    }

    @Override
    public boolean showGraph() {
        return true;
    }

    @Override
    public Integer getMaxYAxisValue() {
        if ((sellFactorHistory.size() > 0) && (purchaseFactorHistory.size() > 0)) {
            Double max = sellFactorHistory.get(0);
            for (Double rec: sellFactorHistory) {
                if (rec > max)
                    max = rec;
            }
            for (Double rec: purchaseFactorHistory) {
                if (rec > max)
                    max = rec;
            }
            max += 1;
            return max.intValue();
        }
        else
            return 0;
    }

    @Override
    public List<Pair<String, List<Double>>> getGraphDataSeries() {
        List<Pair<String, List<Double>>> graphDataSeries = new ArrayList<>();
        graphDataSeries.add(new Pair<>("Base Sell Factor", this.sellFactorHistory));
        graphDataSeries.add(new Pair<>("Base Purchase Factor", this.purchaseFactorHistory));
        return graphDataSeries;
    }

    @Override
    public List<Pair<String, String>> getLabelsAndValues() {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        List<Pair<String, String>> labelsAndValues = new ArrayList<>();
        labelsAndValues.add(new Pair<>("Name", this.getName()));
        labelsAndValues.add(new Pair<>("Currency Code", this.getCurrency().getCode()));
        labelsAndValues.add(new Pair<>("Currency Name", this.getCurrency().getName()));
        labelsAndValues.add(new Pair<>("Base Sell Factor", decimalFormat.format(this.getBaseCurrencySellFactor())));
        labelsAndValues.add(new Pair<>("Base Purchase Factor", decimalFormat.format(this.getBaseCurrencyPurchaseFactor())));
        return labelsAndValues;
    }

    @Override
    public void deleteFromDataContext() {
        AssetService assetService = new AssetService(MainPageController.dataContext);
        assetService.deleteAsset(this);
    }

    @Override
    public String toString() {
        return this.getTypeName() + " " + this.getName() + "\n" + "Currency Code: " + this.getCurrency().getCode();
    }
}
