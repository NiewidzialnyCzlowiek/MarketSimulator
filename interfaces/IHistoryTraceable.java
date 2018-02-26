package interfaces;

import Models.markets.Transaction;

import java.util.List;

public interface IHistoryTraceable {
    void addHistoryEntry(Transaction transaction);
    List<Transaction> getHistoryOfTransactions();
}
