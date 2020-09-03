package io.bartlomiejszal.marketsimulator.controllers;

import io.bartlomiejszal.marketsimulator.model.assets.Asset;
import io.bartlomiejszal.marketsimulator.dataManagement.services.AssetService;
import io.bartlomiejszal.marketsimulator.interfaces.IPresentable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GroupChartController implements Initializable {

    public ChoiceBox<Asset> AssetChoiceBox;
    public ListView<Asset> AssetListView;
    public LineChart GroupChart;

    private AssetService assetService = new AssetService(MainPageController.dataContext);
    private ObservableList<Asset> assetObservableList = FXCollections.observableArrayList(assetService.getAssets());
    private ObservableList<Asset> assetsToChart = FXCollections.observableArrayList();

    public void homeButton_onAction(ActionEvent actionEvent) {
        MainPageController.stage.setScene(MainPageController.mainPage);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AssetChoiceBox.setItems(assetObservableList);
    }

    public void onAction_AddToAssetGroupButton(ActionEvent actionEvent) {
        assetsToChart.add(AssetChoiceBox.getSelectionModel().getSelectedItem());
        AssetListView.setItems(assetsToChart);
    }

    public void onAction_CreateChartButton(ActionEvent actionEvent) {
        if (assetsToChart.size() > 0) {
            double maximalValue = getMaximalValueOfAssets();
            ObservableList<XYChart.Series<Integer, Double>> chartData = createDataSeriesFromAssetsToChart(maximalValue);
            GroupChart.setData(chartData);
        }

    }

    private ObservableList<XYChart.Series<Integer, Double>> createDataSeriesFromAssetsToChart(double maximalValue) {
        ObservableList<XYChart.Series<Integer, Double>> chartData = FXCollections.observableArrayList();
        for (Asset asset: assetsToChart) {
            List<Pair<String, List<Double>>> assetData = ((IPresentable) asset).getGraphDataSeries();
            List<XYChart.Series<Integer, Double>> dataSeries = createDataSeriesFromAssetGraphData(assetData, maximalValue, asset);
            chartData.addAll(dataSeries);
        }
        return chartData;
    }

    private List<XYChart.Series<Integer,Double>> createDataSeriesFromAssetGraphData(List<Pair<String, List<Double>>> assetData, double maximalValue, Asset asset) {
        List<XYChart.Series<Integer,Double>> dataSeries = new ArrayList<>();
        for (Pair<String, List<Double>> assetDatum : assetData) {
            XYChart.Series<Integer, Double> series = new XYChart.Series<>();
            series.setName(asset.getName() + " " + assetDatum.getKey());
            int i = 0;
            for (Double value : assetDatum.getValue()) {
                series.getData().add(new XYChart.Data<>(i, value/maximalValue));
                i++;
            }
            dataSeries.add(series);
        }
        return dataSeries;
    }

    private double getMaximalValueOfAssets() {
        double maximal = 0;
        for (Asset asset: assetsToChart){
            double local = ((IPresentable) asset).getMaxYAxisValue();
            if (local > maximal)
                maximal = local;
        };
        return maximal;
    }

    public void onActionRemoveFromAssetGroupButton(ActionEvent actionEvent) {
        assetsToChart.remove(AssetListView.getSelectionModel().getSelectedItem());
        AssetListView.setItems(assetsToChart);
    }
}
