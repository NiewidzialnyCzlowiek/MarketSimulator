package builders;

import Models.usersAndCustomers.Investor;

public class InvestorBuilder {
    private int id;
    private String name;
    private String lastName;
    private long personalId;
    private double investmentBudget;

    public InvestorBuilder() {
        this.id = 0;
        this.name = "";
        this.lastName = "";
        this.personalId = 0;
        this.investmentBudget = 0.0;
    }

    public InvestorBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public InvestorBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public InvestorBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public InvestorBuilder setPersonalId(long personalId) {
        this.personalId = personalId;
        return this;
    }

    public InvestorBuilder setInvestmentBudget(double investmentBudget) {
        this.investmentBudget = investmentBudget;
        return this;
    }
    public Investor build() {
        Investor investor = new Investor(0,"",0.0,"",0);
        investor.setId(id);
        investor.setName(name);
        investor.setLastName(lastName);
        investor.setPersonalId(personalId);
        investor.setInvestmentBudget(investmentBudget);
        return investor;
    }
}
