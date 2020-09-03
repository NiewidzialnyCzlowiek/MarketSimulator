package io.bartlomiejszal.marketsimulator.model.assets;

import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Company;
import io.bartlomiejszal.marketsimulator.controllers.MainPageController;
import io.bartlomiejszal.marketsimulator.dataManagement.services.AssetService;
import io.bartlomiejszal.marketsimulator.interfaces.IPresentable;
import javafx.util.Pair;
import io.bartlomiejszal.marketsimulator.setup.Setup;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Share extends Asset implements IPresentable {
    private Company owner;

    private List<Double> valueHistory;

    public Share(int id, Company owner, double quantity) {
        this.setId(id);
        this.setName(owner.getName());
        this.owner = owner;
        this.setQuantity(quantity);
        valueHistory = new ArrayList<>();
    }

    public List<Double> getValueHistory() {
        return valueHistory;
    }

    public Company getOwner() {
        return owner;
    }

    public void setOwner(Company owner) {
        this.owner = owner;
    }

    @Override
    public String getTypeName() {
        return "Share";
    }

    @Override
    public void recordValueInHistory() {
        this.valueHistory.add(this.getOwner().getCurrentSharePrice());
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

    private void fluctuateValue(double assetQuantity) {
        double maxFluctuation = 0.3 * this.getOwner().getCurrentSharePrice();
        if (this.getQuantity() != 0) {
            if (assetQuantity > 0)
                this.getOwner().setCurrentSharePrice(this.getOwner().getCurrentSharePrice() - (maxFluctuation * assetQuantity / (this.getQuantity() + assetQuantity)));
            else
                this.getOwner().setCurrentSharePrice(this.getOwner().getCurrentSharePrice() + (maxFluctuation * assetQuantity / (this.getQuantity() + Math.abs(assetQuantity))));
        }
    }

    @Override
    public double getQuantityForAmount(double transactionBudget) {
        double quantityForAmount = transactionBudget/this.getOwner().getCurrentSharePrice();
        if (this.getQuantity() < quantityForAmount)
            quantityForAmount = this.getQuantity();
        return quantityForAmount;
    }

    @Override
    public double getPriceForQuantity(double assetQuantity) {
        return this.getOwner().getCurrentSharePrice() * Math.abs(assetQuantity);
    }

    @Override
    public boolean showGraph() {
        return true;
    }

    @Override
    public Integer getMaxYAxisValue() {
        if (valueHistory.size() > 0) {
            Double max = valueHistory.get(0);
            for (Double rec : valueHistory) {
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
        graphDataSeries.add(new Pair<>("Value", this.valueHistory));
        return graphDataSeries;
    }

    @Override
    public List<Pair<String, String>> getLabelsAndValues() {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        List<Pair<String, String>> labelsAndValues = new ArrayList<>();
        labelsAndValues.add(new Pair<>("Name", this.getName()));
        labelsAndValues.add(new Pair<>("Owner", this.getOwner().getName()));
        labelsAndValues.add(new Pair<>("Current Price", decimalFormat.format(this.getOwner().getCurrentSharePrice())));
        labelsAndValues.add(new Pair<>("Quantity", decimalFormat.format(this.getQuantity())));
        return labelsAndValues;
    }

    @Override
    public void deleteFromDataContext() {
        AssetService assetService = new AssetService(MainPageController.dataContext);
        assetService.deleteAsset(this);
    }

    @Override
    public String toString() {
        return this.getTypeName() + " " + this.getName() + "\n" + "Owner: " + this.getOwner().getName();
    }
}
