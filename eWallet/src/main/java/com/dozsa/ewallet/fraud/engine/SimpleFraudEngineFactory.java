package com.dozsa.ewallet.fraud.engine;

import com.dozsa.ewallet.fraud.model.Customer;

public class SimpleFraudEngineFactory implements FraudEngineFactory {

	public FraudEngine newFraudEngine(Customer customer) {
		return new SimpleFraudEngine(customer);
	}

}
