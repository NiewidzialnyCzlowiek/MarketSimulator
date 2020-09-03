package io.bartlomiejszal.marketsimulator.model.dictionaries;

import java.io.Serializable;

public class TradeUnit implements Serializable {
    private int id;
    private String name;

    public TradeUnit(int id, String name) {
        this.id = id;
        this.name = name;
    }

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

    @Override
    public String toString() {
        return this.name;
    }
}
