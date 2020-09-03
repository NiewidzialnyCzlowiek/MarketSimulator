package io.bartlomiejszal.marketsimulator.controllers;

import io.bartlomiejszal.marketsimulator.model.markets.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.io.IOException;
import java.text.DecimalFormat;

public class TransactionCellController {

    @FXML Label CustomerNameLabel;
    @FXML Label AssetTypeAndNameLabel;
    @FXML Label QuantityLabel;
    @FXML Label BalanceLabel;

    public Node getCellView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TransactionListCell.fxml"));
        try {
            loader.setController(this);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setTransaction(Transaction transaction) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        CustomerNameLabel.setText(transaction.getCustomer().getName());
        if (transaction.getAsset() != null)
            AssetTypeAndNameLabel.setText(transaction.getAsset().getTypeName() + " " + transaction.getAsset().getName());
        QuantityLabel.setText(decimalFormat.format(transaction.getQuantity()));
        BalanceLabel.setText(decimalFormat.format(transaction.getBalanceAfterTransaction()));
    }
}
