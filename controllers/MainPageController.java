package controllers;

import dataManagement.DataContext;
import interfaces.IUpdatable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPageController implements IUpdatable {
    @FXML Label StockSimulatorLabel;

    public static Stage stage;
    public static Scene mainPage;
    public static DataContext dataContext;

    public void onAction_CustomersButton(ActionEvent actionEvent) {
        try {
            stage.setScene(buildCustomerScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onAction_AssetsButton(ActionEvent actionEvent) {
        try {
            stage.setScene(buildAssetScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAction_CompanyButton(ActionEvent actionEvent) {
        try {
            stage.setScene(buildCompanyScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Scene buildCustomerScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CustomerView.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().add("/UniversalStylesheet.css");
        return scene;
    }

    private Scene buildAssetScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AssetView.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().add("/UniversalStylesheet.css");
        return scene;
    }

    private Scene buildMarketScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MarketView.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().add("/UniversalStylesheet.css");
        return scene;
    }

    private Scene buildCompanyScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CompanyView.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().addAll("/UniversalStylesheet.css");
        return scene;
    }

    private Scene buildSetupScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SetupView.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().addAll("/UniversalStylesheet.css");
        return scene;
    }

    private Scene buildGroupChartScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/GroupChartView.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().addAll("/UniversalStylesheet.css");
        return scene;
    }

    @Override
    public void update() {
    }

    public void onAction_MarketsButton(ActionEvent actionEvent) {
        try {
            stage.setScene(buildMarketScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAction_SetupButton(ActionEvent actionEvent) {
        try {
            stage.setScene(buildSetupScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAction_GroupChartButton(ActionEvent actionEvent) {
        try {
            stage.setScene(buildGroupChartScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
