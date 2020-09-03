package io.bartlomiejszal.marketsimulator.dataManagement.services;

import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Customer;
import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Fund;
import io.bartlomiejszal.marketsimulator.dataManagement.DataContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CustomerService {
    private DataContext _context;

    public CustomerService(DataContext dataContext) {
        this._context = dataContext;
    }

    public void addCustomer(Customer customer) {
        _context.getCustomers().add(customer);
    }

    public List<Customer> getCustomers() {
        return _context.getCustomers();
    }

    public void deleteCustomer(Customer customer) {
        customer.setRunThread(false);
        _context.getCustomers().remove(customer);
    }

    public Fund getRandomFund() {
        List<Fund> funds = _context.getCustomers()
                .stream()
                .filter(rec -> rec instanceof Fund)
                .map(rec -> (Fund) rec)
                .collect(Collectors.toCollection(ArrayList::new));
        Random random = new Random();
        return funds.get(random.nextInt(funds.size()));
    }
}
