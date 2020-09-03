package io.bartlomiejszal.marketsimulator.controllers;

import io.bartlomiejszal.marketsimulator.model.markets.Transaction;
import io.bartlomiejszal.marketsimulator.interfaces.IHistoryTraceable;
import io.bartlomiejszal.marketsimulator.interfaces.IPresentable;
import io.bartlomiejszal.marketsimulator.interfaces.IUpdatable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import io.bartlomiejszal.marketsimulator.setup.Setup;
import io.bartlomiejszal.marketsimulator.views.TransactionListViewCell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IPresentableController implements IUpdatable {
    @FXML VBox DetailsVBox;
    @FXML ListView<Transaction> TransactionListView;
    @FXML LineChart ValueChart;

    private IPresentable presentable;
    private Scene goBackToScene;
    private IUpdatable goBackToSceneController;

    private ObservableList<Transaction> transactionObservableList = FXCollections.observableArrayList();

    public IPresentableController(IPresentable presentable, Scene goBackToScene, IUpdatable updatable) {
        this.presentable = presentable;
        this.goBackToScene = goBackToScene;
        this.goBackToSceneController = updatable;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/IPresentableDetailsPage.fxml"));
            loader.setController(this);
            Scene currentScene = new Scene(loader.load(), 1000, 700);
            currentScene.getStylesheets().add("/styles/UniversalStylesheet.css");
            renderIPresentableLayout();
            MainPageController.stage.setScene(currentScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void renderIPresentableLayout() {
        setDetailsVBoxFields();
        if (presentable.showGraph())
            addChartData();
        else
            hideValueChart();
        if (IHistoryTraceable.class.isAssignableFrom(presentable.getClass())) {
            createHistoryOfTransactions();
        }
    }

    private void hideValueChart() {
        ValueChart.setVisible(false);
        TransactionListView.setLayoutY(0);
    }

    private void createHistoryOfTransactions() {
        IHistoryTraceable historyTraceable = (IHistoryTraceable) presentable;
        if ((historyTraceable.getHistoryOfTransactions() != null) && (historyTraceable.getHistoryOfTransactions().size() > 0)) {
            transactionObservableList.setAll(historyTraceable.getHistoryOfTransactions());
            TransactionListView.setItems(transactionObservableList);
            TransactionListView.setCellFactory(param -> new TransactionListViewCell());
            TransactionListView.setVisible(true);
        }
    }

    private void addChartData() {
        ObservableList<XYChart.Series<Integer, Double>> chartData = getChartData();
        ValueChart.setData(chartData);
        ValueChart.setVisible(true);
    }

    private ObservableList<XYChart.Series<Integer, Double>> getChartData() {
        List<Pair<String, List<Double>>> dataSeries = presentable.getGraphDataSeries();
        List<XYChart.Series<Integer, Double>> chartData = new ArrayList<>();
        for (Pair<String, List<Double>> data: dataSeries) {
            XYChart.Series<Integer, Double> series = new XYChart.Series<>();
            series.setName(data.getKey());
            int i = 0;
            for (Double value: data.getValue()) {
                series.getData().add(new XYChart.Data<>(i, value));
                i++;
            }
            chartData.add(series);
        }
        return FXCollections.observableArrayList(chartData);
    }

    private void setDetailsVBoxFields() {
        DetailsVBox.setSpacing(10);
        List<Pair<String, String>> fields = presentable.getLabelsAndValues();
        for (Pair<String, String> field : fields) {
            DetailsVBox.getChildren().add(createHBoxWithLabelAndValue(field));
        }
    }

    private HBox createHBoxWithLabelAndValue(Pair<String, String> field) {
        Label label = new Label();
        label.setText(field.getKey());
        label.setPrefSize(250,30);
        Label value = new Label();
        value.setText(field.getValue());
        value.setPrefSize(200,30);
        HBox hbox = new HBox();
        hbox.getChildren().add(label);
        hbox.getChildren().add(value);
        return hbox;
    }

    public void homeButton_onAction(ActionEvent actionEvent) {
        MainPageController.stage.setScene(MainPageController.mainPage);
    }

    public void backButton_onAction(ActionEvent actionEvent) {
        if (goBackToScene != null)
            MainPageController.stage.setScene(goBackToScene);
    }

    public void onAction_DeleteIPresentableButton(ActionEvent actionEvent) {
        presentable.deleteFromDataContext();
        MainPageController.stage.setScene(goBackToScene);
        goBackToSceneController.update();
    }

    @Override
    public void update() {
        renderIPresentableLayout();
    }
}