package io.bartlomiejszal.marketsimulator.model.usersAndCustomers;

import io.bartlomiejszal.marketsimulator.model.assets.Asset;
import io.bartlomiejszal.marketsimulator.model.markets.Market;
import io.bartlomiejszal.marketsimulator.model.markets.Transaction;
import io.bartlomiejszal.marketsimulator.controllers.MainPageController;
import io.bartlomiejszal.marketsimulator.dataManagement.services.CustomerService;
import io.bartlomiejszal.marketsimulator.dataManagement.services.MarketService;
import io.bartlomiejszal.marketsimulator.interfaces.IHistoryTraceable;
import io.bartlomiejszal.marketsimulator.interfaces.IPresentable;
import javafx.util.Pair;
import io.bartlomiejszal.marketsimulator.threadManagement.ThreadManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Investor extends Customer implements IPresentable, IHistoryTraceable, Runnable {
    private String lastName;
    private long personalId;
    private List<Transaction> transactionHistory;

    public Investor(int id, String name, double investmentBudget, String lastName, long personalId) {
        this.setId(id);
        this.setName(name);
        this.setInvestmentBudget(investmentBudget);
        this.lastName = lastName;
        this.personalId = personalId;
        transactionHistory = new ArrayList<>();
        this.setPortfolio(new Portfolio());
        createAndRunInvestorThread();
    }

    private void createAndRunInvestorThread() {
        ThreadManager.addAndStartThread(new Thread(this));
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getPersonalId() {
        return personalId;
    }

    public void setPersonalId(long personalId) {
        this.personalId = personalId;
    }

    @Override
    public String getName() { return super.getName() + " " + this.getLastName(); }

    @Override
    public void buyRandomAsset() {
        Random random = new Random();
        if (random.nextDouble() < 0.7) {
            synchronized (this) {
                MarketService marketService = new MarketService(MainPageController.dataContext);
                Market market = marketService.getRandomMarket();
                Asset asset = market.getRandomAsset();
                market.handlePurchaseTransaction(this, asset, randomBudgetForTransaction());
            }
        } else {
            CustomerService customerService = new CustomerService(MainPageController.dataContext);
            Fund fund = customerService.getRandomFund();
            handleFundPurchaseTransaction(fund, randomBudgetForTransaction());
        }
    }

    private synchronized void handleFundPurchaseTransaction(Fund fund, double amount) {
        fund.addInvestmentBudget(amount);
        this.getPortfolio().addToPortfolio(fund, amount);
        this.setInvestmentBudget(this.getInvestmentBudget() - amount);
        this.addHistoryEntry(new Transaction(null, fund, null, 1.0, amount, this.getInvestmentBudget()));
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

    public String getFirstName() { return super.getName(); }

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
        labelsAndValues.add(new Pair<>("Name", this.getFirstName()));
        labelsAndValues.add(new Pair<>("Last Name", this.getLastName()));
        labelsAndValues.add(new Pair<>("Personal Id", String.valueOf(this.getPersonalId())));
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        labelsAndValues.add(new Pair<>("Investment Budget", decimalFormat.format(this.getInvestmentBudget())));
        return labelsAndValues;
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
                randomlyIncreaseInvestmentBudget();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void randomlyIncreaseInvestmentBudget() {
        Random random = new Random();
        if (random.nextInt(100) > 10) {
            this.setInvestmentBudget(this.getInvestmentBudget() + random.nextInt(8000) + 2000);
        }
    }

    @Override
    public void deleteFromDataContext() {
        CustomerService customerService = new CustomerService(MainPageController.dataContext);
        customerService.deleteCustomer(this);
    }

}
