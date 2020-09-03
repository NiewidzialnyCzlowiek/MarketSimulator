package io.bartlomiejszal.marketsimulator.controllers;

import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Fund;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.io.IOException;
import java.text.DecimalFormat;

public class FundCellController {
    @FXML Label NameLabel;
    @FXML Label AdministratorNameLabel;
    @FXML Label AdministratorLastNameLabel;
    @FXML Label InvestmentBudgetLabel;


    public void setFund(Fund fund) {
        NameLabel.setText(fund.getName());
        AdministratorNameLabel.setText(fund.getAdministratorName());
        AdministratorLastNameLabel.setText(fund.getAdministratorLastName());
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        InvestmentBudgetLabel.setText(decimalFormat.format(fund.getInvestmentBudget()));
    }

    public Node getCellView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FundListCell.fxml"));
        try {
            loader.setController(this);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
