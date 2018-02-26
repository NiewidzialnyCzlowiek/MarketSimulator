package Models.usersAndCustomers;

import Models.assets.Share;
import controllers.MainPageController;
import dataManagement.services.CustomerService;
import setup.UIDGenerator;
import threadManagement.ThreadManager;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Random;

public class Company implements Runnable, Serializable {
    private int id;
    private String name;
    private LocalDate firstAssessmentDate;
    private double openingQuotation;
    private double currentSharePrice;
    private double minimalSharePrice;
    private double maximalSharePrice;
    private double shares;
    private double profit;
    private double revenue;
    private double equity;
    private double shareCapital;
    private double volume;
    private double turnover;
    private Share share;
    private boolean runThread;

    public Company(int id, String name, LocalDate firstAssessmentDate,
                   double openingQuotation, double currentSharePrice,
                   double minimalSharePrice, double maximalSharePrice,
                   double shares, double profit,
                   double revenue, double equity,
                   double shareCapital, double volume,
                   double turnover) {
        this.id = id;
        this.name = name;
        this.firstAssessmentDate = firstAssessmentDate;
        this.openingQuotation = openingQuotation;
        this.currentSharePrice = currentSharePrice;
        this.minimalSharePrice = minimalSharePrice;
        this.maximalSharePrice = maximalSharePrice;
        this.shares = shares;
        this.profit = profit;
        this.revenue = revenue;
        this.equity = equity;
        this.shareCapital = shareCapital;
        this.volume = volume;
        this.turnover = turnover;
        this.share = new Share(UIDGenerator.getUID(),this,0.0);
        this.runThread = true;
        createAndRunCompanyThread();
    }

    private void createAndRunCompanyThread() {
        ThreadManager.addAndStartThread(new Thread(this));
    }

    public boolean isRunThread() {
        return runThread;
    }

    public void setRunThread(boolean runThread) {
        this.runThread = runThread;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getFirstAssessmentDate() {
        return firstAssessmentDate;
    }

    public void setFirstAssessmentDate(LocalDate firstAssessmentDate) {
        this.firstAssessmentDate = firstAssessmentDate;
    }

    public double getOpeningQuotation() {
        return openingQuotation;
    }

    public void setOpeningQuotation(double openingQuotation) {
        this.openingQuotation = openingQuotation;
    }

    public double getCurrentSharePrice() {
        return currentSharePrice;
    }

    public void setCurrentSharePrice(double currentSharePrice) {
        if (currentSharePrice > this.maximalSharePrice)
            this.maximalSharePrice = currentSharePrice;
        if (currentSharePrice < this.minimalSharePrice)
            this.minimalSharePrice = currentSharePrice;
        this.currentSharePrice = currentSharePrice;
        this.getShare().recordValueInHistory();
    }

    public double getMinimalSharePrice() {
        return minimalSharePrice;
    }

    public void setMinimalSharePrice(double minimalSharePrice) {
        this.minimalSharePrice = minimalSharePrice;
    }

    public double getMaximalSharePrice() {
        return maximalSharePrice;
    }

    public void setMaximalSharePrice(double maximalSharePrice) {
        this.maximalSharePrice = maximalSharePrice;
    }

    public double getShares() {
        return shares;
    }

    public void setShares(double shares) {
        this.shares = shares;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getEquity() {
        return equity;
    }

    public void setEquity(double equity) {
        this.equity = equity;
    }

    public double getShareCapital() {
        return shareCapital;
    }

    public void setShareCapital(double shareCapital) {
        this.shareCapital = shareCapital;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }

    public Share getShare() {
        return share;
    }

    public void setShare(Share share) {
        this.share = share;
    }

    @Override
    public String toString() {
        return "\n" + this.getName() + "\n";
    }

    public void handlePurchaseTransaction(double sharesQuantity, double sharesPrice) {
        this.setVolume(this.getVolume() + sharesQuantity);
        this.setTurnover(this.getTurnover() + sharesPrice);
    }

    private synchronized void generateRevenueAndProfit() {
        Random random = new Random();
        double revenueIncrease = Math.log(this.getRevenue()) * (double)(random.nextInt(10) + 3)/100 * this.getRevenue();
        this.setRevenue(this.getRevenue() + revenueIncrease);
        double profitIncrease = revenueIncrease * 0.8;
        this.setProfit(this.getProfit() + profitIncrease);
    }

    public synchronized void buyAllActions(double price) {
        this.getShare().setQuantity(0);
        CustomerService customerService = new CustomerService(MainPageController.dataContext);
        customerService.getCustomers().forEach(customer -> {
            synchronized (customer) {
                double sharesQuantity = customer.getPortfolio().removeFromPortfolio(this.getShare());
                customer.setInvestmentBudget(customer.getInvestmentBudget() + (sharesQuantity * price));
            }
        });
    }

    private synchronized void randomlyGenerateShares() {
        Random random = new Random();
        Double generatedShares = Math.log(this.getProfit()) + random.nextInt(1000);
        this.setShares(generatedShares);
        this.getShare().setQuantity(this.getShare().getQuantity() + generatedShares);
    }

    @Override
    public void run() {
        Random random = new Random();
        long profitPeriodPart = random.nextInt(2000) + 1000;
        while (MainPageController.dataContext == null && ThreadManager.isRunThreads()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (ThreadManager.isRunThreads() && this.isRunThread()) {
            try {
                for (int i=0; i< 5; i++) {
                    if (ThreadManager.isRunThreads() && this.isRunThread()) {
                        Thread.sleep(profitPeriodPart);
                        if (random.nextDouble() > 0.7)
                            randomlyGenerateShares();
                    }
                }
                generateRevenueAndProfit();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleSellTransaction(double quantity, double sharesPrice) {
        this.setVolume(this.getVolume() - quantity);
    }

    public double getAllSharesValue() {
        return this.getShares() * this.getCurrentSharePrice();
    }
}
