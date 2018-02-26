package controllers;

import Models.usersAndCustomers.Company;
import dataManagement.services.CompanyService;
import interfaces.IUpdatable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import sampleData.SampleDataGenerator;
import setup.UIDGenerator;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CompanyController implements Initializable, IUpdatable {

    @FXML ListView<Company> CompanyListListView;
    @FXML Pane CompanyFormPane;
    @FXML Pane CompanyListPane;
    @FXML TextField NameTextField;
    @FXML TextField FirstAssessmentDateTextField;
    @FXML TextField OpeningQuotationTextField;
    @FXML TextField CurrentSharePriceTextField;
    @FXML TextField MinimalSharePriceTextField;
    @FXML TextField MaximalSharePriceTextField;
    @FXML TextField SharesTextField;
    @FXML TextField ProfitTextField;
    @FXML TextField RevenueTextField;
    @FXML TextField EquityTextField;
    @FXML TextField ShareCapitalTextField;
    @FXML TextField VolumeTextField;
    @FXML TextField TurnoverTextField;

    private ObservableList<Company> companyObservableList = FXCollections.observableArrayList();
    private CompanyService companyService;

    public void homeButton_onAction(ActionEvent actionEvent) {
        MainPageController.stage.setScene(MainPageController.mainPage);
    }

    public void onAction_AddCompany(ActionEvent actionEvent) {
        if (!CompanyFormPane.isVisible()) {
            CompanyFormPane.setVisible(true);
            CompanyListPane.setVisible(false);
        }
    }

    public void onAction_CompanyList(ActionEvent actionEvent) {
        setCompanyListListView();
        if (!CompanyListPane.isVisible()) {
            CompanyListPane.setVisible(true);
            CompanyFormPane.setVisible(false);
        }
    }

    private void setCompanyListListView() {
        companyObservableList.setAll(companyService.getCompanies());
        CompanyListListView.setItems(companyObservableList);
    }

    public void onAction_FillCompanyFormButton(ActionEvent actionEvent) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        Company company = SampleDataGenerator.createCompany();
        NameTextField.setText(company.getName());
        FirstAssessmentDateTextField.setText(company.getFirstAssessmentDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        OpeningQuotationTextField.setText(decimalFormat.format(company.getOpeningQuotation()));
        CurrentSharePriceTextField.setText(decimalFormat.format(company.getCurrentSharePrice()));
        MinimalSharePriceTextField.setText(decimalFormat.format(company.getMinimalSharePrice()));
        MaximalSharePriceTextField.setText(decimalFormat.format(company.getMaximalSharePrice()));
        SharesTextField.setText(decimalFormat.format(company.getShares()));
        ProfitTextField.setText(decimalFormat.format(company.getProfit()));
        RevenueTextField.setText(decimalFormat.format(company.getProfit()));
        RevenueTextField.setText(decimalFormat.format(company.getRevenue()));
        EquityTextField.setText(decimalFormat.format(company.getEquity()));
        ShareCapitalTextField.setText(decimalFormat.format(company.getShareCapital()));
        VolumeTextField.setText(decimalFormat.format(company.getVolume()));
        TurnoverTextField.setText(decimalFormat.format(company.getTurnover()));
    }

    public void onAction_AddCompanyButton(ActionEvent actionEvent) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        try {
            Company company = new Company(
                    UIDGenerator.getUID(),
                    NameTextField.getText(),
                    LocalDate.parse(FirstAssessmentDateTextField.getText(), DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                    decimalFormat.parse(OpeningQuotationTextField.getText()).doubleValue(),
                    decimalFormat.parse(CurrentSharePriceTextField.getText()).doubleValue(),
                    decimalFormat.parse(MinimalSharePriceTextField.getText()).doubleValue(),
                    decimalFormat.parse(MaximalSharePriceTextField.getText()).doubleValue(),
                    decimalFormat.parse(SharesTextField.getText()).doubleValue(),
                    decimalFormat.parse(ProfitTextField.getText()).doubleValue(),
                    decimalFormat.parse(RevenueTextField.getText()).doubleValue(),
                    decimalFormat.parse(EquityTextField.getText()).doubleValue(),
                    decimalFormat.parse(ShareCapitalTextField.getText()).doubleValue(),
                    decimalFormat.parse(VolumeTextField.getText()).doubleValue(),
                    decimalFormat.parse(TurnoverTextField.getText()).doubleValue()
            );
            companyService.addCompany(company);
            clearCompanyFormTextFields();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void clearCompanyFormTextFields() {
        NameTextField.setText("");
        FirstAssessmentDateTextField.setText("");
        OpeningQuotationTextField.setText("");
        CurrentSharePriceTextField.setText("");
        MinimalSharePriceTextField.setText("");
        MaximalSharePriceTextField.setText("");
        SharesTextField.setText("");
        ProfitTextField.setText("");
        RevenueTextField.setText("");
        EquityTextField.setText("");
        ShareCapitalTextField.setText("");
        VolumeTextField.setText("");
        TurnoverTextField.setText("");
    }

    public void onMouseReleased_CompanyListListView(MouseEvent mouseEvent) {
        CompanyDetailsController companyDetailsController = new CompanyDetailsController(
                CompanyListListView.getSelectionModel().getSelectedItem(),
                MainPageController.stage.getScene(),
                this
        );
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        companyService = new CompanyService(MainPageController.dataContext);
        setCompanyListListView();
    }

    @Override
    public void update() {
        setCompanyListListView();
    }
}
