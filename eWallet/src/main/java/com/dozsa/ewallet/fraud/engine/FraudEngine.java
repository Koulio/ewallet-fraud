package com.dozsa.ewallet.fraud.engine;

import com.dozsa.ewallet.fraud.model.Transaction;

public interface FraudEngine {

	public abstract boolean isFraud(Transaction transaction);

}