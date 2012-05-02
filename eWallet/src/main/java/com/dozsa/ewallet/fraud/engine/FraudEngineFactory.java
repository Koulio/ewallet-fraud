package com.dozsa.ewallet.fraud.engine;

import com.dozsa.ewallet.fraud.model.Customer;

public interface FraudEngineFactory {

	public FraudEngine newFraudEngine(Customer customer);
}
