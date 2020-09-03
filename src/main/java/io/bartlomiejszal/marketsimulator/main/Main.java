package io.bartlomiejszal.marketsimulator.main;

import io.bartlomiejszal.marketsimulator.controllers.MainPageController;
import io.bartlomiejszal.marketsimulator.dataManagement.DataContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import io.bartlomiejszal.marketsimulator.sampleData.SampleDataGenerator;
import io.bartlomiejszal.marketsimulator.threadManagement.ThreadManager;

import java.io.IOException;

public class Main extends Application {

    private Scene buildMainScene(DataContext dataContext, Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/MainView.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().add("/styles/MainPageStylesheet.css");
        setApplicationData(stage, scene, dataContext);
        return scene;
    }

    private void setApplicationData(Stage stage, Scene scene, DataContext dataContext) {
        MainPageController.stage = stage;
        MainPageController.mainPage = scene;
        MainPageController.dataContext = dataContext;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SampleDataGenerator.loadData();
        DataContext dataContext = new DataContext();
        dataContext.createBlankDatabase();
        primaryStage.setTitle("Market Simulator");
        primaryStage.setScene(buildMainScene(dataContext, primaryStage));
        primaryStage.setOnCloseRequest(event -> ThreadManager.safelyStopAllThreads());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
