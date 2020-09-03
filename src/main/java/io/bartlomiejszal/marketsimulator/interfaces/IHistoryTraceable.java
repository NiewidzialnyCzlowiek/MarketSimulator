package io.bartlomiejszal.marketsimulator.interfaces;

import io.bartlomiejszal.marketsimulator.model.markets.Transaction;

import java.util.List;

public interface IHistoryTraceable {
    void addHistoryEntry(Transaction transaction);
    List<Transaction> getHistoryOfTransactions();
}
