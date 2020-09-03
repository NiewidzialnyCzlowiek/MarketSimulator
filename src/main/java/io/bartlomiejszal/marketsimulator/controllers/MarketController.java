package io.bartlomiejszal.marketsimulator.controllers;

import io.bartlomiejszal.marketsimulator.model.assets.CurrencyAsset;
import io.bartlomiejszal.marketsimulator.model.assets.Good;
import io.bartlomiejszal.marketsimulator.model.dictionaries.Country;
import io.bartlomiejszal.marketsimulator.model.dictionaries.Currency;
import io.bartlomiejszal.marketsimulator.model.markets.*;
import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Company;
import io.bartlomiejszal.marketsimulator.dataManagement.services.AssetService;
import io.bartlomiejszal.marketsimulator.dataManagement.services.CompanyService;
import io.bartlomiejszal.marketsimulator.dataManagement.services.MarketService;
import io.bartlomiejszal.marketsimulator.interfaces.IUpdatable;
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
import io.bartlomiejszal.marketsimulator.main.Main;
import io.bartlomiejszal.marketsimulator.setup.UIDGenerator;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MarketController implements Initializable, IUpdatable{
    public TextField StockExchangeNameTextField;
    public ChoiceBox<Country> StockExchangeCountryChoiceBox;
    public ChoiceBox<Currency> StockExchangeCurrencyChoiceBox;
    public TextField StockExchangeCityTextField;
    public TextField StockExchangeAddressTextField;
    public TextField StockExchangeMarginTextField;
    public ChoiceBox<Company> CompanyChoiceBox;
    public ListView<Company> CompaniesToIndexListView;
    public TextField StaticIndexNameTextField;
    public TextField DynamicIndexNameTextField;
    public TextField DynamicIndexNoOfCompaniesTextField;
    public ListView<Index> IndexToStockExchangeListView;

    @FXML TextField GoodsMarketNameTextField;
    @FXML ChoiceBox<Country> GoodsMarketCountryChoiceBox;
    @FXML ChoiceBox<Currency> GoodsMarketCurrencyChoiceBox;
    @FXML TextField GoodsMarketCityTextField;
    @FXML TextField GoodsMarketAddressTextField;
    @FXML TextField GoodsMarketMarginTextField;
    @FXML ChoiceBox<Good> GoodChoiceBox;
    @FXML ListView<Good> GoodsOnGoodsMarketListView;

    @FXML TextField CurrencyMarketNameTextField;
    @FXML ChoiceBox<Country> CurrencyMarketCountryChoiceBox;
    @FXML ChoiceBox<Currency> CurrencyMarketCurrencyChoiceBox;
    @FXML TextField CurrencyMarketCityTextField;
    @FXML TextField CurrencyMarketAddressTextField;
    @FXML TextField CurrencyMarketMarginTextField;
    @FXML ChoiceBox<CurrencyAsset> CurrencyAssetChoiceBox;
    @FXML ListView<CurrencyAsset> CurrencyAssetsOnCurrencyMarketListView;

    @FXML ListView<Market> MarketListListView;
    @FXML Pane MarketListPane;
    @FXML Pane StockExchangeFormPane;
    @FXML Pane CurrencyMarketFormPane;
    @FXML Pane GoodsMarketFormPane;

    private MarketService marketService = new MarketService(MainPageController.dataContext);
    private AssetService assetService = new AssetService(MainPageController.dataContext);
    private CompanyService companyService = new CompanyService(MainPageController.dataContext);
    private ObservableList<Country> countryObservableList = FXCollections.observableArrayList(assetService.getCountries());
    private ObservableList<Currency> currencyObservableList = FXCollections.observableArrayList(assetService.getCurrencies());
    private ObservableList<Good> goodObservableList = FXCollections.observableArrayList(assetService.getGoods());
    private ObservableList<CurrencyAsset> currencyAssetObservableList = FXCollections.observableArrayList(assetService.getCurrencyAssets());
    private ObservableList<Good> goodsToMarket = FXCollections.observableArrayList();
    private ObservableList<CurrencyAsset> currencyAssetsToMarket = FXCollections.observableArrayList();
    private ObservableList<Market> markets = FXCollections.observableArrayList();
    private ObservableList<Company> companyObservableList = FXCollections.observableArrayList(companyService.getCompanies());
    private ObservableList<Company> companiesToIndex = FXCollections.observableArrayList();
    private ObservableList<Index> indexesToMarket = FXCollections.observableArrayList();
    private DecimalFormat decimalFormat = new DecimalFormat("#.00");

    public void onAction_HomeButton(ActionEvent actionEvent) {
        MainPageController.stage.setScene(MainPageController.mainPage);
    }


    public void onAction_AddGoodsMarket(ActionEvent actionEvent) {
        if (!GoodsMarketFormPane.isVisible()) {
            GoodsMarketFormPane.setVisible(true);
            CurrencyMarketFormPane.setVisible(false);
            StockExchangeFormPane.setVisible(false);
            MarketListPane.setVisible(false);
        }
        goodsToMarket = FXCollections.observableArrayList();
    }

    public void onAction_AddCurrencyMarket(ActionEvent actionEvent) {
        if (!CurrencyMarketFormPane.isVisible()) {
            CurrencyMarketFormPane.setVisible(true);
            GoodsMarketFormPane.setVisible(false);
            StockExchangeFormPane.setVisible(false);
            MarketListPane.setVisible(false);
        }
        currencyAssetsToMarket = FXCollections.observableArrayList();
    }

    public void onAction_AddStockExchange(ActionEvent actionEvent) {
        if (!StockExchangeFormPane.isVisible()) {
            StockExchangeFormPane.setVisible(true);
            CurrencyMarketFormPane.setVisible(false);
            GoodsMarketFormPane.setVisible(false);
            MarketListPane.setVisible(false);
        }
    }

    public void onAction_MarketList(ActionEvent actionEvent) {
        markets.setAll(marketService.getMarkets());
        MarketListListView.setItems(markets);
        if (!MarketListPane.isVisible()) {
            MarketListPane.setVisible(true);
            StockExchangeFormPane.setVisible(false);
            CurrencyMarketFormPane.setVisible(false);
            GoodsMarketFormPane.setVisible(false);
        }
    }

    public void onAction_AddGoodToMarket(ActionEvent actionEvent) {
        goodsToMarket.add(GoodChoiceBox.getSelectionModel().getSelectedItem());
        GoodsOnGoodsMarketListView.setItems(goodsToMarket);
    }

    public void onAction_AddGoodsMarketButton(ActionEvent actionEvent) {
        try {
            List<Good> goods = new ArrayList<>(goodsToMarket);
            marketService.addMarket(new GoodsMarket(
                        UIDGenerator.getUID(),
                        GoodsMarketNameTextField.getText(),
                        GoodsMarketCountryChoiceBox.getSelectionModel().getSelectedItem(),
                        GoodsMarketCurrencyChoiceBox.getSelectionModel().getSelectedItem(),
                        GoodsMarketCityTextField.getText(),
                        GoodsMarketAddressTextField.getText(),
                        decimalFormat.parse(GoodsMarketMarginTextField.getText()).doubleValue(),
                        goods
                    )
            );
            GoodsMarketNameTextField.setText("");
            GoodsMarketCityTextField.setText("");
            GoodsMarketAddressTextField.setText("");
            GoodsMarketMarginTextField.setText("");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void onAction_AddCurrencyToMarket(ActionEvent actionEvent) {
        currencyAssetsToMarket.add(CurrencyAssetChoiceBox.getSelectionModel().getSelectedItem());
        CurrencyAssetsOnCurrencyMarketListView.setItems(currencyAssetsToMarket);
    }

    public void onAction_AddCurrencyMarketButton(ActionEvent actionEvent) {
        try {
            List<CurrencyAsset> currencyAssets = new ArrayList<>(currencyAssetsToMarket);
            marketService.addMarket(new CurrencyMarket(
                            UIDGenerator.getUID(),
                            CurrencyMarketNameTextField.getText(),
                            CurrencyMarketCountryChoiceBox.getSelectionModel().getSelectedItem(),
                            CurrencyMarketCurrencyChoiceBox.getSelectionModel().getSelectedItem(),
                            CurrencyMarketCityTextField.getText(),
                            CurrencyMarketAddressTextField.getText(),
                            decimalFormat.parse(CurrencyMarketMarginTextField.getText()).doubleValue(),
                            currencyAssets
                    )
            );
            CurrencyMarketNameTextField.setText("");
            CurrencyMarketCityTextField.setText("");
            CurrencyMarketAddressTextField.setText("");
            CurrencyMarketMarginTextField.setText("");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void onAction_AddStockExchangeButton(ActionEvent actionEvent) {
        try {
            List<Index> indexes = new ArrayList<>(indexesToMarket);
            StockExchange stockExchange = new StockExchange(
                    UIDGenerator.getUID(),
                    StockExchangeNameTextField.getText(),
                    StockExchangeCountryChoiceBox.getSelectionModel().getSelectedItem(),
                    StockExchangeCurrencyChoiceBox.getSelectionModel().getSelectedItem(),
                    StockExchangeCityTextField.getText(),
                    StockExchangeAddressTextField.getText(),
                    decimalFormat.parse(StockExchangeMarginTextField.getText()).doubleValue(),
                    indexes
            );
            stockExchange.getIndexes().forEach(rec -> rec.setStockExchange(stockExchange));

            StockExchangeNameTextField.setText("");
            StockExchangeCityTextField.setText("");
            StockExchangeAddressTextField.setText("");
            StockExchangeMarginTextField.setText("");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setChoiceBoxes() {
        GoodsMarketCountryChoiceBox.setItems(countryObservableList);
        GoodsMarketCurrencyChoiceBox.setItems(currencyObservableList);
        CurrencyMarketCountryChoiceBox.setItems(countryObservableList);
        CurrencyMarketCurrencyChoiceBox.setItems(currencyObservableList);
        StockExchangeCountryChoiceBox.setItems(countryObservableList);
        StockExchangeCurrencyChoiceBox.setItems(currencyObservableList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        update();
    }

    @Override
    public void update() {
        setChoiceBoxes();
        GoodChoiceBox.setItems(goodObservableList);
        GoodsOnGoodsMarketListView.setItems(goodsToMarket);
        CurrencyAssetChoiceBox.setItems(currencyAssetObservableList);
        CurrencyAssetsOnCurrencyMarketListView.setItems(currencyAssetsToMarket);
        CompanyChoiceBox.setItems(companyObservableList);
        CompaniesToIndexListView.setItems(companiesToIndex);
    }

    public void onAction_AddStaticButton(ActionEvent actionEvent) {
        List<Company> _companies = new ArrayList<>(companiesToIndex);
        indexesToMarket.add(new Index(
                UIDGenerator.getUID(),
                _companies,
                StaticIndexNameTextField.getText(),
                0
            )
        );
        companiesToIndex = FXCollections.observableArrayList();
        StaticIndexNameTextField.setText("");
        CompaniesToIndexListView.setItems(companiesToIndex);
        IndexToStockExchangeListView.setItems(indexesToMarket);
    }

    public void onAction_CompanyToIndexButton(ActionEvent actionEvent) {
        companiesToIndex.add(CompanyChoiceBox.getSelectionModel().getSelectedItem());
        CompaniesToIndexListView.setItems(companiesToIndex);
    }

    public void onAction_AddDynamicButton(ActionEvent actionEvent) {
        Index index = new Index(
                UIDGenerator.getUID(),
                new ArrayList<>(),
                DynamicIndexNameTextField.getText(),
                0
        );
        index.setDynamicIndex(true);
        index.setNumberOfBestCompanies(Integer.parseInt(DynamicIndexNoOfCompaniesTextField.getText()));
        indexesToMarket.add(index);
        DynamicIndexNameTextField.setText("");
        DynamicIndexNoOfCompaniesTextField.setText("");
        IndexToStockExchangeListView.setItems(indexesToMarket);
    }

    public void onMouseReleased_GoToMarketDetailsPage(MouseEvent mouseEvent) {
        MarketDetailsController marketDetailsController = new MarketDetailsController(MarketListListView.getSelectionModel().getSelectedItem(), MainPageController.stage.getScene());
    }
}
