package com.dozsa.ewallet.fraud.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dozsa.ewallet.fraud.model.Customer;

public class CustomerService {

	private Map<String, Customer> customers;

	public CustomerService() {
		customers = new ConcurrentHashMap<String, Customer>();
	}

	public CustomerService(Map<String, Customer> customers) {
		this();
		this.customers.putAll(customers);
	}

	public void addCustomer(Customer customer) {
		customers.put(customer.getPan(), customer);
	}

	public Customer getCustomer(String pan) {
		return customers.get(pan);
	}

	public List<Customer> getCustomersList() {
		return new ArrayList<Customer>(customers.values());
	}
}
