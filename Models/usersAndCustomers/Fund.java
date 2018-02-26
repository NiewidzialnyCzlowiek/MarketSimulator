package Models.usersAndCustomers;

import Models.assets.Asset;
import Models.markets.Market;
import Models.markets.Transaction;
import controllers.MainPageController;
import dataManagement.services.CustomerService;
import dataManagement.services.MarketService;
import interfaces.IHistoryTraceable;
import interfaces.IPresentable;
import javafx.util.Pair;
import threadManagement.ThreadManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Fund extends Customer implements IPresentable, IHistoryTraceable, Runnable {
    private String administratorName;
    private String administratorLastName;
    private List<Transaction> transactionHistory;
    private double totalInvestedAmount;

    public Fund(int id, String name, double investmentBudget, String administratorName, String administratorLastName) {
        this.setId(id);
        this.setName(name);
        this.setInvestmentBudget(investmentBudget);
        this.administratorName = administratorName;
        this.administratorLastName = administratorLastName;
        transactionHistory = new ArrayList<>();
        this.setPortfolio(new Portfolio());
        totalInvestedAmount = this.getInvestmentBudget();
        createAndRunFundThread();
    }

    private void createAndRunFundThread() {
        ThreadManager.addAndStartThread(new Thread(this));
    }

    public String getAdministratorName() {
        return administratorName;
    }

    public void setAdministratorName(String administratorName) {
        this.administratorName = administratorName;
    }

    public String getAdministratorLastName() {
        return administratorLastName;
    }

    public void setAdministratorLastName(String administratorLastName) {
        this.administratorLastName = administratorLastName;
    }

    public synchronized void addInvestmentBudget(Double budget) {
        this.totalInvestedAmount += budget;
        this.setInvestmentBudget(this.getInvestmentBudget() + budget);
    }

    @Override
    public void addHistoryEntry(Transaction transaction) {
        transactionHistory.add(transaction);
        if (transactionHistory.size() > 50)
            transactionHistory.remove(0);
    }

    @Override
    public List<Transaction> getHistoryOfTransactions() {
        return transactionHistory;
    }

    @Override
    public boolean showGraph() {
        return false;
    }

    @Override
    public Integer getMaxYAxisValue() {
        return 0;
    }

    @Override
    public List<Pair<String, List<Double>>> getGraphDataSeries() {
        return null;
    }

    @Override
    public List<Pair<String, String>> getLabelsAndValues() {
        List<Pair<String, String>> labelsAndValues = new ArrayList<>();
        labelsAndValues.add(new Pair<>("Name", this.getName()));
        labelsAndValues.add(new Pair<>("Administrator's Name", this.getAdministratorName()));
        labelsAndValues.add(new Pair<>("Administrator's Last Name", this.getAdministratorLastName()));
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        labelsAndValues.add(new Pair<>("Investment Budget", decimalFormat.format(this.getInvestmentBudget())));
        return labelsAndValues;
    }

    @Override
    public void deleteFromDataContext() {
        CustomerService customerService = new CustomerService(MainPageController.dataContext);
        customerService.deleteCustomer(this);
    }

    @Override
    public synchronized void buyRandomAsset() {
        MarketService marketService = new MarketService(MainPageController.dataContext);
        Market market = marketService.getRandomMarket();
        Asset asset = market.getRandomAsset();
        market.handlePurchaseTransaction(this, asset, randomBudgetForTransaction());
    }

    private double randomBudgetForTransaction() {
        Random random = new Random();
        return ((double)(random.nextInt(20) + 10)/100) * this.getInvestmentBudget();
    }

    @Override
    public synchronized void sellRandomAsset() {
        Asset asset = this.getPortfolio().getRandomAsset();
        if (asset != null) {
            double quantity = this.getPortfolio().getRandomQuantity(asset);
            MarketService marketService = new MarketService(MainPageController.dataContext);
            Market market = marketService.getRandomMarket(asset);
            market.handleSellTransaction(this, asset, quantity);
        }
    }

    @Override
    public void run() {
        Random random = new Random();
        while (MainPageController.dataContext == null && ThreadManager.isRunThreads()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (ThreadManager.isRunThreads() && this.isRunThread()) {
            try {
                Thread.sleep(random.nextInt(1000) + 1000);
                this.buyRandomAsset();
                Thread.sleep(random.nextInt(1000) + 1000);
                this.sellRandomAsset();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
