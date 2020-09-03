package io.bartlomiejszal.marketsimulator.model.dictionaries;

import java.io.Serializable;
import java.util.List;

public class Currency implements Serializable {
    private int id;
    private String code;
    private String name;
    private boolean baseCurrency;
    private List<Country> usedInCountries;

    public Currency(int id, String name, String code, boolean baseCurrency, List<Country> usedInCountries) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.baseCurrency = baseCurrency;
        this.usedInCountries = usedInCountries;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(boolean baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public List<Country> getUsedInCountries() {
        return usedInCountries;
    }

    public void setUsedInCountries(List<Country> usedInCountries) {
        this.usedInCountries = usedInCountries;
    }

    @Override
    public String toString() {
        return this.getCode() + "  " + this.getName();
    }
}
