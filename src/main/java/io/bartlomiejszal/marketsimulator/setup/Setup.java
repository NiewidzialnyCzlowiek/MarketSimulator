package io.bartlomiejszal.marketsimulator.setup;

import io.bartlomiejszal.marketsimulator.model.dictionaries.Currency;
import io.bartlomiejszal.marketsimulator.dataManagement.DataContext;

public class Setup {
    private static Currency baseCurrency;
    private static int noOfHistoryRecords;
    private static int assetsPerCustomer;

    public static Currency getBaseCurrency() {
        return baseCurrency;
    }

    public static int getAssetsPerCustomer() {
        return assetsPerCustomer;
    }

    public static int getNoOfHistoryRecords() {
        return noOfHistoryRecords;
    }

    public static void setNoOfHistoryRecords(int noOfHistoryRecords) {
        Setup.noOfHistoryRecords = noOfHistoryRecords;
    }

    public static void setDefault(DataContext context) {
        if (!context.getCurrencies().isEmpty())
            baseCurrency = context.getCurrencies().get(0);
        else
            baseCurrency = null;
        noOfHistoryRecords = 20;
        assetsPerCustomer = 10;
    }

    public static void setBaseCurrency(Currency baseCurrency) {
        Setup.baseCurrency = baseCurrency;
    }

    public static void setAssetsPerCustomer(int noOfAssetsPerCustomer) {
        Setup.assetsPerCustomer = noOfAssetsPerCustomer;
    }
}
