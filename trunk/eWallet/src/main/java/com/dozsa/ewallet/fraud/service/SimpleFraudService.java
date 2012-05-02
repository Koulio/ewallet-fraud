package com.dozsa.ewallet.fraud.service;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.dozsa.ewallet.fraud.engine.FraudEngine;
import com.dozsa.ewallet.fraud.engine.FraudEngineFactory;
import com.dozsa.ewallet.fraud.engine.SimpleFraudEngine;
import com.dozsa.ewallet.fraud.model.Customer;
import com.dozsa.ewallet.fraud.model.Transaction;

public class SimpleFraudService implements FraudService {

	private Properties properties;
	private TransactionService transactionService;
	private CustomerService customerService;
	private Map<String, FraudEngine> fraudEngines;
	private FraudEngineFactory fraudEngineFactory;

	public SimpleFraudService(Properties properties, TransactionService transactionService,
			CustomerService customerService, FraudEngineFactory fraudEngineFactory) {
		this.properties = properties;
		this.transactionService = transactionService;
		this.customerService = customerService;
		this.fraudEngineFactory = fraudEngineFactory;
	}

	public void initFraudEngines() {
		fraudEngines = new ConcurrentHashMap<String, FraudEngine>();
		for (Customer customer : customerService.getCustomersList()) {
			fraudEngines.put(customer.getPan(), fraudEngineFactory.newFraudEngine(customer));
		}
	}

	public boolean isFraud(Transaction transaction) {
		Customer customer = customerService.getCustomer(transaction.getPan());

		if (!fraudEngines.containsKey(customer.getPan())) {
			fraudEngines.put(customer.getPan(), new SimpleFraudEngine(customer));
		}
		FraudEngine fraudEngine = fraudEngines.get(customer.getPan());

		boolean response = fraudEngine.isFraud(transaction);

		return response;
	}
}
