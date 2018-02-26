package controllers;

import Models.assets.CurrencyAsset;
import Models.assets.Good;
import Models.markets.*;
import Models.usersAndCustomers.Company;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.text.DecimalFormat;

public class MarketDetailsController {
    public Label MarketNameLabel;
    public Label MarketCountryLabel;
    public Label MarketCurrencyLabel;
    public Label MarketCityLabel;
    public Label MarketAddressLabel;
    public Label MarketMarginLabel;
    public Pane GoodsMarketContentsPane;
    public ListView<Good> GoodsOnGoodsMarketListView;
    public Pane CurrencyMarketContentsPane;
    public ListView<CurrencyAsset> CurrencyAssetsOnCurrencyMarketListView;
    public Pane StockExchangeContentsPane;
    public ChoiceBox<Index> IndexChoiceBox;
    public ListView<Company> CompaniesOnIndexListView;

    private Scene goBackToScene;
    private Market market;

    private ObservableList<Index> indexObservableList = FXCollections.observableArrayList();
    private ObservableList<Company> companiesOnIndex = FXCollections.observableArrayList();
    private ObservableList<Good> goodObservableList = FXCollections.observableArrayList();
    private ObservableList<CurrencyAsset> currencyAssetObservableList = FXCollections.observableArrayList();

    public MarketDetailsController(Market market, Scene goBackToScene) {
        this.market = market;
        this.goBackToScene = goBackToScene;
        Scene marketDetailsScene = buildMarketDetailsScene();
        renderMarketDetailsPage();
        MainPageController.stage.setScene(marketDetailsScene);
    }

    private void renderMarketDetailsPage() {
        if (market instanceof GoodsMarket) {
            setGoodsMarketData((GoodsMarket) market);
            setMarketFields();
        } else if (market instanceof CurrencyMarket) {
            setCurrencyMarketData((CurrencyMarket) market);
            setMarketFields();
        } else if (market instanceof StockExchange) {
            setStockExchangeData((StockExchange) market);
            setMarketFields();
        }
    }

    private Scene buildMarketDetailsScene() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MarketDetailsView.fxml"));
        loader.setController(this);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (root == null)
            return null;
        else {
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add("/UniversalStylesheet.css");
            return scene;
        }
    }

    private void setMarketFields() {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        MarketNameLabel.setText(market.getName());
        MarketCountryLabel.setText(market.getCountry().toString());
        MarketCurrencyLabel.setText(market.getCurrency().toString());
        MarketCityLabel.setText(market.getCity());
        MarketAddressLabel.setText(market.getAddress());
        MarketMarginLabel.setText(decimalFormat.format(market.getMarginPercent()));
    }

    private void setStockExchangeData(StockExchange market) {
        indexObservableList.setAll(market.getIndexes());
        IndexChoiceBox.setItems(indexObservableList);
        IndexChoiceBox.getSelectionModel().selectFirst();
        StockExchangeContentsPane.setVisible(true);
    }

    private void setCurrencyMarketData(CurrencyMarket market) {
        currencyAssetObservableList.setAll(market.getCurrencyAssets());
        CurrencyAssetsOnCurrencyMarketListView.setItems(currencyAssetObservableList);
        CurrencyMarketContentsPane.setVisible(true);
    }

    private void setGoodsMarketData(GoodsMarket market) {
        goodObservableList.setAll(market.getGoods());
        GoodsOnGoodsMarketListView.setItems(goodObservableList);
        GoodsMarketContentsPane.setVisible(true);
    }

    public void homeButton_onAction(ActionEvent actionEvent) {
        MainPageController.stage.setScene(MainPageController.mainPage);
    }

    public void backButton_onAction(ActionEvent actionEvent) {
        MainPageController.stage.setScene(this.goBackToScene);
    }

    public void onAction_IndexChoiceBox(ActionEvent actionEvent) {
        companiesOnIndex.setAll(IndexChoiceBox.getSelectionModel().getSelectedItem().getCompanies());
        CompaniesOnIndexListView.setItems(companiesOnIndex);
    }
}
