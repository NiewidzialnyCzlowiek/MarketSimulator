package controllers;

import Models.dictionaries.Currency;
import dataManagement.DataContext;
import dataManagement.services.AssetService;
import dataManagement.services.DataSerializationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import setup.Setup;
import threadManagement.ThreadManager;

import java.net.URL;
import java.util.ResourceBundle;

public class SetupController implements Initializable {
    public ChoiceBox<Currency> CurrencyChoiceBox;
    public TextField NoOfHistoryRecordsTextField;
    public TextField NoOfAssetsPerCustomerTextField;
    public Label SerializationStatusLabel;
    public Label DatabaseCreationStatusLabel;

    private ObservableList<Currency> currencies = FXCollections.observableArrayList(new AssetService(MainPageController.dataContext).getCurrencies());

    public void onAction_HomeButton(ActionEvent actionEvent) {
        MainPageController.stage.setScene(MainPageController.mainPage);
    }

    public void onAction_ApplySettingsButton(ActionEvent actionEvent) {
        Setup.setBaseCurrency(CurrencyChoiceBox.getSelectionModel().getSelectedItem());
        Setup.setNoOfHistoryRecords(Integer.parseInt(NoOfHistoryRecordsTextField.getText()));
        Setup.setAssetsPerCustomer(Integer.parseInt(NoOfAssetsPerCustomerTextField.getText()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CurrencyChoiceBox.setItems(currencies);
        CurrencyChoiceBox.getSelectionModel().select(Setup.getBaseCurrency());
        NoOfHistoryRecordsTextField.setText(String.valueOf(Setup.getNoOfHistoryRecords()));
        NoOfAssetsPerCustomerTextField.setText(String.valueOf(Setup.getAssetsPerCustomer()));
    }

    public void onAction_SerializeButton(ActionEvent actionEvent) {
        SerializationStatusLabel.setText("");
        if (DataSerializationService.serializeDataContext())
            SerializationStatusLabel.setText("Serialization successful");
        else
            SerializationStatusLabel.setText("Serialization unsuccessful");
    }

    public void onAction_DeserializeButton(ActionEvent actionEvent) {
        SerializationStatusLabel.setText("");
        if (DataSerializationService.deserializeDataContext())
            SerializationStatusLabel.setText("Deserialization successful");
        else
            SerializationStatusLabel.setText("Deserialization unsuccessful");
    }

    public void onAction_RestoreDefaultsButton(ActionEvent actionEvent) {
        Setup.setDefault(MainPageController.dataContext);
        initialize(null, null);
    }

    public void onAction_CreateSampleDatabase(ActionEvent actionEvent) {
        ThreadManager.safelyStopAllThreads();
        ThreadManager.setRunThreads(true);
        MainPageController.dataContext = null;
        DataContext dataContext = new DataContext();
        dataContext.populateSampleData();
        MainPageController.dataContext = dataContext;
        if (MainPageController.dataContext != null)
            DatabaseCreationStatusLabel.setText("Sample database creation successful!");
        else
            DatabaseCreationStatusLabel.setText("Something went wrong during sample database creation");
    }
}
