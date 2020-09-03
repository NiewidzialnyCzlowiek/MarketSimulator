package io.bartlomiejszal.marketsimulator.views;

import io.bartlomiejszal.marketsimulator.model.markets.Transaction;
import io.bartlomiejszal.marketsimulator.controllers.TransactionCellController;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

public final class TransactionListViewCell extends ListCell<Transaction> {
    private final TransactionCellController transactionCellController = new TransactionCellController();
    private final Node cellView = transactionCellController.getCellView();

    @Override
    public void updateItem(Transaction transaction, boolean empty) {
        super.updateItem(transaction, empty);
        if((transaction != null) && !empty) {
            transactionCellController.setTransaction(transaction);
            setGraphic(cellView);
        }
        else
            setGraphic(null);
    }
}
