package io.bartlomiejszal.marketsimulator.model.assets;

import io.bartlomiejszal.marketsimulator.model.dictionaries.TradeUnit;
import io.bartlomiejszal.marketsimulator.controllers.MainPageController;
import io.bartlomiejszal.marketsimulator.dataManagement.services.AssetService;
import io.bartlomiejszal.marketsimulator.interfaces.IPresentable;
import javafx.util.Pair;
import io.bartlomiejszal.marketsimulator.setup.Setup;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Good extends Asset implements IPresentable {
    private TradeUnit tradeUnit;
    private double currentValue;
    private double minimalValue;
    private double maximalValue;

    private List<Double> valueHistory;

//    public Good(int id, String name, TradeUnit tradeUnit, double currentValue, double minimalValue, double maximalValue, double quantity) {
 public Good(int id, String name, TradeUnit tradeUnit, double currentValue, double minimalValue, double maximalValue) {
        this.setId(id);
        this.setName(name);
        this.tradeUnit = tradeUnit;
        this.currentValue = currentValue;
        this.minimalValue = minimalValue;
        this.maximalValue = maximalValue;
//        this.setQuantity(quantity);
        valueHistory = new ArrayList<>();
        Random random = new Random();
        this.setQuantity(100000 + random.nextInt(100000));
    }

    public TradeUnit getTradeUnit() {
        return tradeUnit;
    }

    public void setTradeUnit(TradeUnit tradeUnit) {
        this.tradeUnit = tradeUnit;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
        if(this.currentValue < this.minimalValue)
            this.minimalValue = currentValue;
        if (this.currentValue > this.maximalValue)
            this.maximalValue = currentValue;

        recordValueInHistory();
    }

    public double getMinimalValue() {
        return minimalValue;
    }

    public void setMinimalValue(double minimalValue) {
        this.minimalValue = minimalValue;
    }

    public double getMaximalValue() {
        return maximalValue;
    }

    public void setMaximalValue(double maximalValue) {
        this.maximalValue = maximalValue;
    }

    @Override
    public String getTypeName() {
        return "Good";
    }

    @Override
    public void recordValueInHistory() {
        this.valueHistory.add(this.getCurrentValue());
        if (this.valueHistory.size() > Setup.getNoOfHistoryRecords())
            this.valueHistory.remove(0);
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

    private synchronized void fluctuateValue(double assetQuantity) {
        Random random = new Random();
        if (this.getQuantity() != 0) {
            double maxFluctuation = 0.3 * this.getCurrentValue() * random.nextDouble();
            if (assetQuantity > 0) {
                this.setCurrentValue(this.getCurrentValue() - maxFluctuation);
            }
            else {
                this.setCurrentValue(this.getCurrentValue() + maxFluctuation);
            }
        }
    }

    @Override
    public double getQuantityForAmount(double transactionBudget) {
        Double quantityForAmount = transactionBudget/this.getCurrentValue();
        if (this.getQuantity() < quantityForAmount)
            quantityForAmount = this.getQuantity();
        return quantityForAmount.intValue();
    }

    @Override
    public double getPriceForQuantity(double assetQuantity) {
        return this.getCurrentValue() * Math.abs(assetQuantity);
    }

    @Override
    public boolean showGraph() {
        return true;
    }

    @Override
    public Integer getMaxYAxisValue() {
        return (int)this.getMaximalValue();
    }

    @Override
    public List<Pair<String, List<Double>>> getGraphDataSeries() {
        List<Pair<String, List<Double>>> dataSeries = new ArrayList<>();
        dataSeries.add(new Pair<>("Value", this.valueHistory));
        return dataSeries;
    }

    @Override
    public List<Pair<String, String>> getLabelsAndValues() {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        List<Pair<String, String>> labelsAndValues = new ArrayList<>();
        labelsAndValues.add(new Pair<>("Name", this.getName()));
        labelsAndValues.add(new Pair<>("Trade Unit", this.getTradeUnit().getName()));
        labelsAndValues.add(new Pair<>("Current Value", decimalFormat.format(this.getCurrentValue())));
        labelsAndValues.add(new Pair<>("Maximal Value", decimalFormat.format(this.getMaximalValue())));
        labelsAndValues.add(new Pair<>("Minimal Value", decimalFormat.format(this.getMinimalValue())));
        return labelsAndValues;
    }

    @Override
    public void deleteFromDataContext() {
        AssetService assetService = new AssetService(MainPageController.dataContext);
        assetService.deleteAsset(this);
    }

    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return this.getTypeName() + " " + this.getName() + "\n" + "Current Value: " + decimalFormat.format(this.getCurrentValue());
    }
}
