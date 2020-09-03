package io.bartlomiejszal.marketsimulator.controllers;

import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Fund;
import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Investor;
import io.bartlomiejszal.marketsimulator.builders.InvestorBuilder;
import io.bartlomiejszal.marketsimulator.dataManagement.services.CustomerService;
import io.bartlomiejszal.marketsimulator.interfaces.IPresentable;
import io.bartlomiejszal.marketsimulator.interfaces.IUpdatable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import io.bartlomiejszal.marketsimulator.sampleData.SampleDataGenerator;
import io.bartlomiejszal.marketsimulator.setup.UIDGenerator;
import io.bartlomiejszal.marketsimulator.views.FundListViewCell;
import io.bartlomiejszal.marketsimulator.views.InvestorListViewCell;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CustomerController implements IUpdatable {
    public Button HomeButton;
    @FXML Pane InvestorPane;
    @FXML Pane FundPane;
    @FXML Pane FundListPane;
    @FXML Pane InvestorListPane;

    @FXML ScrollPane NavigationScrollPane;
    @FXML TextField PersonalIdTextField;
    @FXML TextField NameTextField;
    @FXML TextField LastNameTextField;
    @FXML TextField InvestmentBudgetTextField;

    @FXML TextField FundNameTextField;
    @FXML TextField FundAdminNameTextField;
    @FXML TextField FundAdminLastNameTextField;
    @FXML TextField FundInvestmentBudgetTextField;

    @FXML ListView<Fund> FundListListView;
    @FXML ListView<Investor> InvestorListListView;

    private CustomerService customerService = new CustomerService(MainPageController.dataContext);

    private ObservableList<Fund> fundObservableList = FXCollections.observableArrayList();
    private ObservableList<Investor> investorObservableList = FXCollections.observableArrayList();

    public void homeButton_onAction(ActionEvent actionEvent) {
        MainPageController.stage.setScene(MainPageController.mainPage);
    }

    public void onAction_AddInvestor(ActionEvent actionEvent) {
        if (!InvestorPane.isVisible()) {
            InvestorPane.setVisible(true);
            FundPane.setVisible(false);
            FundListPane.setVisible(false);
            InvestorListPane.setVisible(false);
        }
    }

    public void onAction_AddFund(ActionEvent actionEvent) {
        if (!FundPane.isVisible()) {
            FundPane.setVisible(true);
            InvestorPane.setVisible(false);
            FundListPane.setVisible(false);
            InvestorListPane.setVisible(false);
        }
    }

    public void onAction_InvestorList(ActionEvent actionEvent) {
        setInvestorListListView();
        if(!InvestorListPane.isVisible()) {
            InvestorListPane.setVisible(true);
            FundPane.setVisible(false);
            InvestorPane.setVisible(false);
            FundListPane.setVisible(false);
        }
    }

    public void onAction_FundList(ActionEvent actionEvent) {
        setFundListListView();
        if(!FundListPane.isVisible()) {
            FundListPane.setVisible(true);
            FundPane.setVisible(false);
            InvestorPane.setVisible(false);
            InvestorListPane.setVisible(false);
        }
    }

    public void onAction_AddInvestorButton(ActionEvent actionEvent) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        Investor investor = null;
        try {
            investor = new InvestorBuilder()
                    .setId(UIDGenerator.getUID())
                    .setPersonalId(Long.parseLong(PersonalIdTextField.getText()))
                    .setName(NameTextField.getText())
                    .setLastName(LastNameTextField.getText())
                    .setInvestmentBudget(decimalFormat.parse(InvestmentBudgetTextField.getText()).doubleValue())
                    .build();
            customerService.addCustomer(investor);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PersonalIdTextField.setText("");
        NameTextField.setText("");
        LastNameTextField.setText("");
        InvestmentBudgetTextField.setText("");
    }

    public void onAction_FillInvestorFormButton(ActionEvent actionEvent) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        Investor investor = SampleDataGenerator.createInvestor();
        PersonalIdTextField.setText(String.valueOf(investor.getPersonalId()));
        NameTextField.setText(investor.getFirstName());
        LastNameTextField.setText(investor.getLastName());
        InvestmentBudgetTextField.setText(decimalFormat.format(investor.getInvestmentBudget()));
    }

    public void onAction_AddFundButton(ActionEvent actionEvent) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        try {
            Fund fund = new Fund(
                    UIDGenerator.getUID(),
                    FundNameTextField.getText(),
                    decimalFormat.parse(FundInvestmentBudgetTextField.getText()).doubleValue(),
                    FundAdminNameTextField.getText(),
                    FundAdminLastNameTextField.getText()
            );
            customerService.addCustomer(fund);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FundNameTextField.setText("");
        FundInvestmentBudgetTextField.setText("");
        FundAdminNameTextField.setText("");
        FundAdminLastNameTextField.setText("");
    }

    public void onAction_FillFundFormButton(ActionEvent actionEvent) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        Fund fund = SampleDataGenerator.createFund();
        FundNameTextField.setText(fund.getName());
        FundInvestmentBudgetTextField.setText(decimalFormat.format(fund.getInvestmentBudget()));
        FundAdminNameTextField.setText(fund.getAdministratorName());
        FundAdminLastNameTextField.setText(fund.getAdministratorLastName());
    }

    public void onMouseReleased_FundListListView(MouseEvent mouseEvent) {
        IPresentable presentable = (IPresentable) FundListListView.getSelectionModel().getSelectedItem();
        IPresentableController presentableController = new IPresentableController(presentable, MainPageController.stage.getScene(), this);
    }

    public void onMouseReleased_InvestorListListView(MouseEvent mouseEvent) {
        IPresentable presentable = (IPresentable) InvestorListListView.getSelectionModel().getSelectedItem();
        IPresentableController presentableController = new IPresentableController(presentable, MainPageController.stage.getScene(), this);
    }

    private void setFundListListView() {
        if(!customerService.getCustomers().isEmpty()) {
            fundObservableList.setAll(
                    customerService.getCustomers()
                    .stream()
                    .filter(record -> record instanceof Fund)
                    .map(record -> (Fund) record)
                    .collect(Collectors.toCollection(ArrayList::new)));
            FundListListView.setItems(fundObservableList);
            FundListListView.setCellFactory(param -> new FundListViewCell());
        }
    }

    private void setInvestorListListView() {
        if(!customerService.getCustomers().isEmpty()) {
            investorObservableList.setAll(
                    customerService.getCustomers()
                    .stream()
                    .filter(record -> record instanceof Investor)
                    .map(record -> (Investor) record)
                    .collect(Collectors.toCollection(ArrayList::new)));
            InvestorListListView.setItems(investorObservableList);
            InvestorListListView.setCellFactory(param -> new InvestorListViewCell());
        }
    }

    @Override
    public void update() {
        setFundListListView();
        setInvestorListListView();
    }
}
