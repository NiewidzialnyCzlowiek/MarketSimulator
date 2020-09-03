package io.bartlomiejszal.marketsimulator.dataManagement.services;

import io.bartlomiejszal.marketsimulator.model.markets.StockExchange;
import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Company;
import io.bartlomiejszal.marketsimulator.dataManagement.DataContext;

import java.util.List;

public class CompanyService {
    private DataContext _context;

    public CompanyService(DataContext dataContext) {
        this._context = dataContext;
    }

    public void addCompany(Company company) {
        _context.getCompanies().add(company);
    }

    public void deleteCompany(Company company) {
        company.setRunThread(false);
        company.buyAllActions(company.getCurrentSharePrice());
        _context.getCompanies().remove(company);
        deleteCompanyFromAllMarkets(company);
    }

    private void deleteCompanyFromAllMarkets(Company company) {
        _context.getMarkets()
                .forEach(rec -> {
            if (rec instanceof StockExchange) {
                deleteCompanyFromAllIndexes((StockExchange) rec, company);
            }
        });
    }

    private void deleteCompanyFromAllIndexes(StockExchange stockExchange, Company company) {
        stockExchange.getIndexes()
                .forEach(rec -> rec.getCompanies().remove(company));
    }

    public List<Company> getCompanies() {
        return _context.getCompanies();
    }
}
