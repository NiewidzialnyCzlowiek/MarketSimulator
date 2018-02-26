package controllers;

import Models.assets.Asset;
import Models.assets.CurrencyAsset;
import Models.assets.Good;
import Models.dictionaries.Currency;
import Models.dictionaries.TradeUnit;
import dataManagement.services.AssetService;
import interfaces.IPresentable;
import interfaces.IUpdatable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import sampleData.SampleDataGenerator;
import setup.UIDGenerator;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ResourceBundle;

public class AssetController implements Initializable, IUpdatable {
    @FXML TextField CurrencyAssetNameTextField;
    @FXML ChoiceBox<Currency> CurrencyAssetCurrencyChoiceBox;
    @FXML TextField CurrencyAssetSellFactorTextField;
    @FXML TextField CurrencyAssetPurchaseFactorTextField;

    @FXML ListView<Asset> AssetListListView;

    @FXML Pane GoodFormPane;
    @FXML Pane AssetListPane;
    @FXML Pane CurrencyAssetFormPane;

    @FXML ChoiceBox<TradeUnit> GoodTradeUnitChoiceBox;
    @FXML TextField GoodNameTextField;
    @FXML TextField GoodCurrentValueTextField;
    @FXML TextField GoodMinimalValueTextField;
    @FXML TextField GoodMaximalValueTextField;
//    @FXML TextField GoodQuantityTextField;

    private AssetService assetService = new AssetService(MainPageController.dataContext);

    private ObservableList<TradeUnit> tradeUnitObservableList = FXCollections.observableArrayList();
    private ObservableList<Currency> currencyObservableList = FXCollections.observableArrayList();
    private ObservableList<Asset> assetObservableList = FXCollections.observableArrayList();

    public void onAction_AddGood(ActionEvent actionEvent) {
        setGoodTradeUnitChoiceBoxItems();
        if (!GoodFormPane.isVisible()) {
            GoodFormPane.setVisible(true);
            AssetListPane.setVisible(false);
            CurrencyAssetFormPane.setVisible(false);
        }
    }

    public void onAction_AddCurrencyAsset(ActionEvent actionEvent) {
        setCurrencyAssetCurrencyChoiceBoxItems();
        if (!CurrencyAssetFormPane.isVisible()) {
            CurrencyAssetFormPane.setVisible(true);
            GoodFormPane.setVisible(false);
            AssetListPane.setVisible(false);
        }
    }

    private void setCurrencyAssetCurrencyChoiceBoxItems() {
        currencyObservableList.setAll(assetService.getCurrencies());
        CurrencyAssetCurrencyChoiceBox.setItems(currencyObservableList);
    }

    public void onAction_AssetList(ActionEvent actionEvent) {
        setAssetListListViewItems();
        if (!AssetListPane.isVisible()) {
            AssetListPane.setVisible(true);
            GoodFormPane.setVisible(false);
            CurrencyAssetFormPane.setVisible(false);
        }
    }

    public void onAction_HomeButton(ActionEvent actionEvent) {
        MainPageController.stage.setScene(MainPageController.mainPage);
    }

    public void onAction_AddGoodButton(ActionEvent actionEvent) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        Good newGood = null;
        try {
            newGood = new Good(
                    UIDGenerator.getUID(),
                    GoodNameTextField.getText(),
                    GoodTradeUnitChoiceBox.getSelectionModel().getSelectedItem(),
                    decimalFormat.parse(GoodCurrentValueTextField.getText()).doubleValue(),
                    decimalFormat.parse(GoodMinimalValueTextField.getText()).doubleValue(),
                    decimalFormat.parse(GoodMaximalValueTextField.getText()).doubleValue()
//                    decimalFormat.parse(GoodQuantityTextField.getText()).doubleValue()
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assetService.addAsset(newGood);
        clearGoodForm();
    }

    private void clearGoodForm() {
        GoodNameTextField.setText("");
        GoodTradeUnitChoiceBox.getSelectionModel().selectFirst();
        GoodCurrentValueTextField.setText("");
        GoodMaximalValueTextField.setText("");
        GoodMinimalValueTextField.setText("");
//        GoodQuantityTextField.setText("");
    }

    public void onAction_AddCurrencyAssetButton(ActionEvent actionEvent) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        CurrencyAsset currencyAsset = null;
        try {
            currencyAsset = new CurrencyAsset(
                    UIDGenerator.getUID(),
                    CurrencyAssetNameTextField.getText(),
                    CurrencyAssetCurrencyChoiceBox.getSelectionModel().getSelectedItem(),
                    decimalFormat.parse(CurrencyAssetSellFactorTextField.getText()).doubleValue(),
                    decimalFormat.parse(CurrencyAssetPurchaseFactorTextField.getText()).doubleValue()
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assetService.addAsset(currencyAsset);
        clearCurrencyAssetForm();
    }

    private void clearCurrencyAssetForm() {
        CurrencyAssetNameTextField.setText("");
        CurrencyAssetCurrencyChoiceBox.getSelectionModel().selectFirst();
        CurrencyAssetSellFactorTextField.setText("");
        CurrencyAssetPurchaseFactorTextField.setText("");
    }

    public void onMouseReleased_AssetListListView(MouseEvent mouseEvent) {
        IPresentable presentable = (IPresentable) AssetListListView.getSelectionModel().getSelectedItem();
        IPresentableController presentableController = new IPresentableController(presentable, MainPageController.stage.getScene(), this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGoodTradeUnitChoiceBoxItems();
        setAssetListListViewItems();
    }

    private void setGoodTradeUnitChoiceBoxItems() {
        tradeUnitObservableList.setAll(assetService.getTradeUnits());
        GoodTradeUnitChoiceBox.setItems(tradeUnitObservableList);
    }

    private void setAssetListListViewItems() {
        assetObservableList.setAll(assetService.getAssets());
        AssetListListView.setItems(assetObservableList);
    }

    public void onAction_FillGoodFormButton(ActionEvent actionEvent) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        Good randomGood = SampleDataGenerator.createGood(new TradeUnit(0,""));
        GoodNameTextField.setText(randomGood.getName());
        GoodTradeUnitChoiceBox.getSelectionModel().selectFirst();
        GoodCurrentValueTextField.setText(decimalFormat.format(randomGood.getCurrentValue()));
        GoodMinimalValueTextField.setText(decimalFormat.format(randomGood.getMinimalValue()));
        GoodMaximalValueTextField.setText(decimalFormat.format(randomGood.getMaximalValue()));
//        GoodQuantityTextField.setText(decimalFormat.format(randomGood.getQuantity()));
    }

    public void onAction_FillCurrencyAssetFormButton(ActionEvent actionEvent) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        CurrencyAsset randomCurrencyAsset = SampleDataGenerator.createCurrencyAsset(SampleDataGenerator.createCurrency());
        CurrencyAssetNameTextField.setText(randomCurrencyAsset.getName());
        CurrencyAssetCurrencyChoiceBox.getSelectionModel().selectFirst();
        CurrencyAssetSellFactorTextField.setText(decimalFormat.format(randomCurrencyAsset.getBaseCurrencySellFactor()));
        CurrencyAssetPurchaseFactorTextField.setText(decimalFormat.format(randomCurrencyAsset.getBaseCurrencyPurchaseFactor()));
    }

    @Override
    public void update() {
        initialize(null, null);
    }
}
