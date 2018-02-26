package controllers;

import Models.usersAndCustomers.Investor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.io.IOException;
import java.text.DecimalFormat;

public class InvestorCellController {
    @FXML Label NameLabel;
    @FXML Label PersonalIdLabel;
    @FXML Label InvestmentBudgetLabel;

    public void setInvestor(Investor investor) {
        NameLabel.setText(investor.getName());
        PersonalIdLabel.setText(String.valueOf(investor.getPersonalId()));
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        InvestmentBudgetLabel.setText(decimalFormat.format(investor.getInvestmentBudget()));
    }

    public Node getCellView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/InvestorListCell.fxml"));
        try {
            loader.setController(this);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
