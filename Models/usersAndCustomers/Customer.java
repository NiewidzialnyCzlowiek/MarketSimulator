package Models.usersAndCustomers;

import java.io.Serializable;

public abstract class Customer implements Serializable {
    private int id;
    private String name;
    private double investmentBudget;
    private Portfolio portfolio;
    private boolean runThread = true;

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

    public double getInvestmentBudget() {
        return investmentBudget;
    }

    public void setInvestmentBudget(double investmentBudget) {
        this.investmentBudget = investmentBudget;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public abstract void buyRandomAsset();

    public abstract void sellRandomAsset();

}
