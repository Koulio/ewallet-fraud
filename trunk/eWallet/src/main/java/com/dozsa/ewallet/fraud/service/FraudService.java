package com.dozsa.ewallet.fraud.service;

import com.dozsa.ewallet.fraud.model.Transaction;

public interface FraudService {

	public void initFraudEngines();

	public boolean isFraudWithReply(Transaction transaction);

	public void isFraudWithAlert(Transaction transaction);
}