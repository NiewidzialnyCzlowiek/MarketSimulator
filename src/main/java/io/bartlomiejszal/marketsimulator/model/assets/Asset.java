package io.bartlomiejszal.marketsimulator.model.assets;

import java.io.Serializable;

public abstract class Asset implements Serializable {
    private int id;
    private String name;
    private double quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public abstract String getTypeName();

    public abstract void recordValueInHistory();

    public abstract void addQuantity(double assetQuantity);

    public abstract void removeQuantity(double assetQuantity);

    public abstract double getQuantityForAmount(double transactionBudget);

    public abstract double getPriceForQuantity(double assetQuantity);
}
