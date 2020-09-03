package io.bartlomiejszal.marketsimulator.controllers;

import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Company;
import io.bartlomiejszal.marketsimulator.dataManagement.services.CompanyService;
import io.bartlomiejszal.marketsimulator.interfaces.IUpdatable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class CompanyDetailsController implements IUpdatable {
    @FXML Label NameDataLabel;
    @FXML Label FirstAssessmentDateDataLabel;
    @FXML Label OpeningQuotationDataLabel;
    @FXML Label CurrentSharePriceDataLabel;
    @FXML Label MinimalSharePriceDataLabel;
    @FXML Label MaximalSharePriceDataLabel;
    @FXML Label SharesDataLabel;
    @FXML Label ProfitDataLabel;
    @FXML Label RevenueDataLabel;
    @FXML Label EquityDataLabel;
    @FXML Label ShareCapitalDataLabel;
    @FXML Label VolumeDataLabel;
    @FXML Label TurnoverDataLabel;

    private Company company;
    private Scene goBackToScene;
    private CompanyService companyService;
    private CompanyController companyController;

    public CompanyDetailsController(Company companyToSet, Scene goBackToScene, CompanyController companyController) {
        this.goBackToScene = goBackToScene;
        this.companyController = companyController;
        companyService = new CompanyService(MainPageController.dataContext);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CompanyDetailsView.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 1000, 700);
            scene.getStylesheets().add("/styles/UniversalStylesheet.css");
            setCompany(companyToSet);
            setCompanyLayoutData();
            MainPageController.stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAction_HomeButton(ActionEvent actionEvent) {
        MainPageController.stage.setScene(MainPageController.mainPage);
    }

    public void onAction_BackButton(ActionEvent actionEvent) {
        MainPageController.stage.setScene(goBackToScene);
    }

    public void onAction_DeleteCompanyButton(ActionEvent actionEvent) {
        companyService.deleteCompany(company);
        onAction_BackButton(new ActionEvent());
        companyController.update();
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    private void setCompanyLayoutData() {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        NameDataLabel.setText(company.getName());
        FirstAssessmentDateDataLabel.setText(company.getFirstAssessmentDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        OpeningQuotationDataLabel.setText(decimalFormat.format(company.getOpeningQuotation()));
        CurrentSharePriceDataLabel.setText(decimalFormat.format(company.getCurrentSharePrice()));
        MinimalSharePriceDataLabel.setText(decimalFormat.format(company.getMinimalSharePrice()));
        MaximalSharePriceDataLabel.setText(decimalFormat.format(company.getMaximalSharePrice()));
        SharesDataLabel.setText(decimalFormat.format(company.getShares()));
        ProfitDataLabel.setText(decimalFormat.format(company.getProfit()));
        RevenueDataLabel.setText(decimalFormat.format(company.getProfit()));
        RevenueDataLabel.setText(decimalFormat.format(company.getRevenue()));
        EquityDataLabel.setText(decimalFormat.format(company.getEquity()));
        ShareCapitalDataLabel.setText(decimalFormat.format(company.getShareCapital()));
        VolumeDataLabel.setText(decimalFormat.format(company.getVolume()));
        TurnoverDataLabel.setText(decimalFormat.format(company.getTurnover()));
    }

    @Override
    public void update() {
        setCompanyLayoutData();
    }
}
