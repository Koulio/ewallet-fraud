package com.dozsa.ewallet.fraud.service;

import com.dozsa.ewallet.fraud.model.Transaction;

public interface FraudService {

	public abstract void initFraudEngines();

	public abstract boolean isFraud(Transaction transaction);

}